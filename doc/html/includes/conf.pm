package Classpath::HTML::conf;

#--- Web Based System Administration Tool Configuration File----
# This contains a number of different variable declarations used
# throughout the admintool cgi program, sysadmin.cgi

BEGIN
{
    use Exporter ();
    use vars qw(@EXPORT @EXPORT_OK %EXPORT_TAGS);

    @EXPORT = qw();
    @EXPORT_OK = qw(%website @menu %root %news %packages %archive %docs
		    @packages_child %packages_java %packages_javax);

}
use vars @EXPORT_OK;

# no trailing slash here
$website{'url'} = "www.classpath.org";
$website{'images'} = "images";

# main menu
$menu[$#menu+1] = "root";
$menu[$#menu+1] = "news";
$menu[$#menu+1] = "packages";
$menu[$#menu+1] = "archive";
$menu[$#menu+1] = "docs";

# root - the directory beneath the root directory
# label - used on the menu as a label for the url
# child - answer yes or no depending on whether there is a sublist

# home 
$root{'root'} = "";
$root{'label'} = "Home Page";
$root{'child'} = "no";
$root{'file'} = "index.shtml";

# news
$news{'root'} = "news";
$news{'label'} = "News";
$news{'child'} = "yes";
$news{'file'} = "index.shtml";

# packages
$packages{'root'} = "packages";
$packages{'label'} = "Package Listing";
$packages{'child'} = "yes";
$packages{'file'} = "index.shtml";

# archive
$archive{'root'} = "archive";
$archive{'label'} = "Archives";
$archive{'child'} = "no";
$archive{'file'} = "index.shtml";

# docs
$docs{'root'} = "docs";
$docs{'label'} = "Documentation";
$docs{'child'} = "yes";
$docs{'file'} = "index.shtml";

#-- sub lists
# label - as above
# file - file name for the URL; sub lists remain in parent list directory
$packages_child[$#packages_child+1] = "java";
$packages_child[$#packages_child+1] = "javax";

# sub list java under packages
$packages_java{'label'} = "java.*";
$packages_java{'file'} = "java.shtml";

# sub list javax under packages
$packages_javax{'label'} = "javax.*";
$packages_javax{'file'} = "javax.shtml";

$news_child[$#news_child+1] = "announce";
$news_child[$#news_child+1] = "status";

$news_announce{'label'} = "Announcements";
$news_announce{'file'} = "announce.shtml";

$news_status{'label'} = "Status";
$news_status{'file'} = "status.shtml";

$docs_child[$#docs_child+1] = "hacking";
$docs_child[$#docs_child+1] = "japhar";

$docs_hacking{'label'} = "Hacker's Guide";
$docs_hacking{'file'} = "hacking.shtml";

$docs_japhar{'label'} = "Japhar Integration";
$docs_japhar{'file'} = "japhar.shtml";

1;
