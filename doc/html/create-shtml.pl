#!/usr/bin/perl -w
#
# The purpose of this script is to create from the template.shtml file in
# root directory of the Classpath web site all the .shtml files needed for
# any file.  This script only creates an .shtml file whenever the appropriate 
# .shtml file already exists.
#
# For example if in the news/ directory there were the files 'index.shtml', 
# 'news.html', 'old-news.html', 'japhar.html', 'japhar.shtml' then this script
# would update the 'index.shtml' and the 'japhar.shtml' from the template.
# It would replace #FILE# in the template with 'news' for 'index.shtml' and 
# 'japhar' for japhar.shtml.  As you can see the index to the directory is
# treated specially.

use strict;

my ($debug) = 1;
my ($template) = "template.shtml";

# really should use Cwd module here, oh well :)
my ($root) = `pwd`;
chop($root);

use vars qw($debug $template $root);

# main
{
    &make();
}

sub make
{
    my ($dir, $lastdir) = @_;
    my (@entries) = ();
    my ($entry) = "";
    my ($is_root) = 0;
    my ($file, $junk) = "";

    if (!($dir) || (($dir) && ($dir eq "")))
    {
	$dir = `pwd`;
	chop($dir);
	$is_root = 1;
	$file = "root";
    }
    opendir(DIR, "$dir") || die "could not opendir $dir\n";
    @entries = grep( !/^\.\S+/, readdir(DIR)); # no .*
    closedir(DIR);
    foreach $entry (sort @entries)
    {
	if (($entry eq ".") || ($entry eq "..")) { next; }
	if (-d "$dir/$entry")
	{
#	    print "$dir/$entry/\n" if ($debug);
	    &make("$dir/$entry", $entry);
	}
	elsif (-e "$dir/$entry")
	{
	    if (($entry eq $template) && ($is_root)) { next; }
#	    print "$dir/$entry\n" if ($debug);
	    if ($entry =~ /\.shtml$/)
	    {
	        if ($entry =~ /^index\./)
		{
		    if (!($is_root)) { $file = $lastdir; }
		} else {
		    ($file,$junk) = split(/\./, $entry, 2);
		}
		open(FILE, ">$dir/$entry") || 
		    die "could not open $dir/$entry\n";
		open(TEMPLATE, "<$root/$template") || 
		    die "could not open $root/$template\n";
		while(<TEMPLATE>)
		{
		    chop;
		    $_ =~ s/\#FILE\#/$file/g;
		    print FILE $_, "\n";
		}
		close(TEMPLATE);
		close(FILE);
		print "Updated $dir/$entry\n";
	    }
	}
    }
}
