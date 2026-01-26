#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <dirent.h>
#include <limits.h>
#include <signal.h>


/* This is defined by posix_spawn */
#define EXIT_ERROR 127

#define RESTARTABLE_IO(_cmd, _ret) \
    { \
        do { \
            _ret = _cmd; \
        } while ((_ret == -1) && (errno == EINTR)); \
    }

/* One of the first things we do is to dup2 the supplied "stderr" fd to
 * our fd 2 (STDERR_FILENO). So we can't just write error messages to stderr
 * as they would end up being sent to the parent through the stderr pipe. */

#define print_error(func)

static int set_fds_cloexec(int fromfd)
{
    DIR *dp;

    dp = opendir("/proc/self/fd");
    if (dp)
    {
        /* opendir() itself is probably implemented using a fd, and we don't
         * want to close it while it's in use. However since we are just
         * setting the CLOEXEC flag, that shouldn't be a problem. */

        struct dirent *dirp;
        char *endp;
        long fd;

        while ((dirp = readdir(dp)) != NULL) {
            fd = strtol(dirp->d_name, &endp, 10);
            if (dirp->d_name != endp && *endp == '\0' && fd >= fromfd && fd < INT_MAX) {
                fcntl((int) fd, F_SETFD, FD_CLOEXEC);
            }
        }
        closedir(dp);
    }
    else
    {
    	/* Can't read /proc/self/fd, so fall back to the old way */

    	int open_max = sysconf(_SC_OPEN_MAX);
    	int fd;

        for (fd = fromfd; fd < open_max; fd++) {
            fcntl(fd, F_SETFD, FD_CLOEXEC);
        }
    }

    return 0;
}

int main(int argc, char ** argv)
{
    int fds[4];
    int i;
    int ret;
    int lasterr;
    char c;
    sigset_t newmask;

    if (argc < 7) {
        fprintf(stderr, "Usage: %s stdin# stdout# stderr# statusfd# chdir program [argv1 ... ]\n", argv[0]);

        /* No status fd so we can't report errors -- just exit */
        return EXIT_ERROR;
    }

    fds[0] = atoi(argv[1]);
    fds[1] = atoi(argv[2]);
    fds[2] = atoi(argv[3]);
    fds[3] = atoi(argv[4]);

    for (i = 0; i < 3; i++) {
        if (dup2(fds[i], i) == -1) {
            lasterr = errno;
            print_error("dup2");
            goto error;
        }
    }

    if (!(strlen(argv[5]) == 1 && strncmp(argv[5], ".", 1) == 0)) {
        if (chdir(argv[5]) != 0) {
            lasterr = errno;
            print_error("chdir");
            goto error;
        }
    }

    /* Mark all fds except stdin,stdout,stderr as close on exec. Note that
     * this includes the status fd. */

    set_fds_cloexec(3);

    /* We have been exec'ed with all signals blocked. All signal handlers
     * from the parent have been cleared already as part of execve(). Now
     * reset the signal mask. */

    sigemptyset(&newmask);
    sigprocmask(SIG_SETMASK, &newmask, NULL);

    execvp(argv[6], argv + 6);

    /* If we got this far, execvp() failed. */

    lasterr = errno;
    print_error("execvp");

error:
    /* On success, the status fd is closed and this point is never reached
     * (exec does not return). On failure, the status fd is still open. We
     * use this to send an error code to the parent.
     *
     * The error code is a single byte containing the last errno value.
     * Although 'errno' is defined to be an int, in practice actual errno
     * values are <= 255, at least on Linux. If we find an errno value
     * that does not fit in a byte, we send 255 instead.
     */
    c = (lasterr <= 255)? ((char) lasterr) : 255;
    RESTARTABLE_IO( write(fds[3], &c, 1), ret );

    return EXIT_ERROR;
}
