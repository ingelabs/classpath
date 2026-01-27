#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <dirent.h>
#include <fcntl.h>

static int parse_int(const char *value, int *out)
{
  char *end = NULL;
  long parsed;

  if (value == NULL || value[0] == '\0')
    return -1;

  parsed = strtol(value, &end, 10);
  if (end == NULL || *end != '\0')
    return -1;

  if (parsed < -2147483647L || parsed > 2147483647L)
    return -1;

  *out = (int) parsed;
  return 0;
}

int main(int argc, char **argv)
{
  int pipe_count;
  int redirect;
  int pipe_fds[6];
  int i;
  int status_fd;
  const char *cwd;
  int cmd_index;
  char **cmd;

  if (argc < 14)
    _exit(127);

  if (parse_int(argv[1], &pipe_count) != 0
      || parse_int(argv[2], &redirect) != 0)
    _exit(127);

  for (i = 0; i < 6; i++)
    {
      if (parse_int(argv[3 + i], &pipe_fds[i]) != 0)
	_exit(127);
    }

  if (parse_int(argv[9], &status_fd) != 0)
    _exit(127);

  cwd = argv[10];
  cmd_index = 11;
  if (strcmp(argv[cmd_index], "--") != 0)
    _exit(127);

  cmd = &argv[cmd_index + 1];
  if (cmd[0] == NULL)
    _exit(127);

  if (dup2(pipe_fds[0], STDIN_FILENO) < 0)
    _exit(126);
  if (dup2(pipe_fds[3], STDOUT_FILENO) < 0)
    _exit(126);
  if (redirect)
    {
      if (dup2(STDOUT_FILENO, STDERR_FILENO) < 0)
	_exit(126);
    }
  else if (pipe_count == 3)
    {
      if (dup2(pipe_fds[5], STDERR_FILENO) < 0)
	_exit(126);
    }
  else
    {
      if (dup2(STDOUT_FILENO, STDERR_FILENO) < 0)
	_exit(126);
    }

  for (i = 0; i < 6; i++)
    {
      int fd = pipe_fds[i];
      if (fd > STDERR_FILENO
	  && fd != STDIN_FILENO
	  && fd != STDOUT_FILENO
	  && fd != STDERR_FILENO)
	close(fd);
    }

  if (!(cwd[0] == '-' && cwd[1] == '\0'))
    {
      if (chdir(cwd) < 0)
	_exit(125);
    }

  {
    DIR *dp = opendir("/proc/self/fd");
    if (dp != NULL)
      {
	struct dirent *dirp;

	while ((dirp = readdir(dp)) != NULL)
	  {
	    char *endp = NULL;
	    long fd = strtol(dirp->d_name, &endp, 10);
	    if (dirp->d_name == endp || *endp != '\0')
	      continue;
	    if (fd < 3 || fd > 2147483647L)
	      continue;
	    fcntl((int) fd, F_SETFD, FD_CLOEXEC);
	  }
	closedir(dp);
      }
    else
      {
	long max_fd = sysconf(_SC_OPEN_MAX);
	int limit = (max_fd > 0 && max_fd < 65536) ? (int) max_fd : 1024;

	for (i = 3; i < limit; i++)
	  fcntl(i, F_SETFD, FD_CLOEXEC);
      }
  }

  execvp(cmd[0], cmd);
  if (status_fd >= 0)
    {
      int err = errno;
      ssize_t ret;
      do
	{
	  ret = write(status_fd, &err, sizeof(err));
	}
      while (ret < 0 && errno == EINTR);
      close(status_fd);
    }
  _exit(errno == ENOENT ? 127 : 126);
}
