#!/usr/bin/perl
# Script which creates part of a table entry
# Example below.
#
#       <center>
#       <b>Classpath Pages</b>
#       <hr width="75%">
#       </center>
#       <li>Home Page
#       <li><a href="announce.html">Project Announcement</a>
#       <li><a href="packages.html">Package Listing</a> 
#       <li><a href="status.html">Project Status</a>
#       <li><a href="mailing-list.html">Mailing Lists</a>
#       <li><a href="jcl-hacking.html">Hacker's Guide</a>
#       <p>
#       <center>
#       <b>Official Specs</b>
#       <hr width="75%">
#       </center>
#       <li><a href="http://java.sun.com/docs/">JavaSoft</a>
#       <p>
#       <center>
#       <b>Free JVM's</b>
#       <hr width="75%">
#       </center>
#       <li><a href="http://www.hungry.com/products/japhar/">Japhar</a>
#       <li><a href="http://www.transvirtual.com/">Kaffe</a> 
#

use conf qw();

my ($i) = "";
use vars qw($i);

# main
{
    &writeMainMenu();
    &writeSubMenus();
}

sub writeMainMenu
{
    my ($i, $j, $tmp) = "";

    foreach $i (@Classpath::HTML::conf::menu)
    {
	open(MENU, ">$i-menu.html") || die "cannot open $i-menu.html\n";
	print MENU "<td align=\"left\" bgcolor=\"\#FFFFC8\" width=\"175\">\n";
#	print MENU "<center>\n";
	print MENU "<b>Classpath Pages</b>\n";
	print MENU "<hr align=\"left\" width=\"75\%\">\n";
#	print MENU "</center>\n";
#        print MENU "<font size=\"-1\">\n";
	print MENU "<UL>\n";
    
	foreach $j (@Classpath::HTML::conf::menu)
	{
	    if ($i eq $j)
	    {
		print MENU "<LI>", ${"Classpath::HTML::conf::$j"}{'label'}, "</LI>\n";
	    }
	    else
	    {
		if (${"Classpath::HTML::conf::$j"}{'root'} ne "")
		{
		    $tmp = "";
		    $tmp .= "<LI><A HREF=\"http://$Classpath::HTML::conf::website{'url'}/";
		    $tmp .= ${"Classpath::HTML::conf::$j"}{'root'} . "/";
		    $tmp .= ${"Classpath::HTML::conf::$j"}{'file'} . "\">";
		    $tmp .= ${"Classpath::HTML::conf::$j"}{'label'};
                    $tmp .= "</A></LI>";
		    print MENU $tmp, "\n";
		} 
		else 
		{
		    $tmp = "";
		    $tmp .= "<LI><A HREF=\"http://$Classpath::HTML::conf::website{'url'}/";
		    $tmp .= ${"Classpath::HTML::conf::$j"}{'file'} . "\">";
		    $tmp .= ${"Classpath::HTML::conf::$j"}{'label'};
                    $tmp .= "</A></LI>";
		    print MENU $tmp, "\n";
		}
	    }
	    
	    if (($i eq $j) && 
		(${"Classpath::HTML::conf::$j"}{'child'} eq "yes"))
	    {
		&menuRecurseChild("Classpath::HTML::conf::$j", *MENU{IO});
	    }
	}
	
	print MENU "</UL>\n";
#        print MENU "</font>\n";
	print MENU "<br>\n";
#	print MENU "<center>\n";
	print MENU "<b>Official Specs</b>\n";
	print MENU "<hr align=\"left\" width=\"75\%\">\n";
#	print MENU "</center>\n";
#        print MENU "<font size=\"-1\">\n";
	print MENU "<ul>\n";
	print MENU "<li><a href=\"http://java.sun.com/docs/\">JavaSoft</a></li>\n";
	print MENU "</ul>\n";
#        print MENU "</font>\n";
	print MENU "<br>\n";
#	print MENU "<center>\n";
	print MENU "<b>Free JVM\'s</b>\n";
	print MENU "<hr align=\"left\" width=\"75\%\">\n";
#	print MENU "</center>\n";
#        print MENU "<font size=\"-1\">\n";
	print MENU "<ul>\n";
	print MENU "<li><a href=\"http://www.hungry.com/products/japhar/\">Japhar</a></li>\n";
	print MENU "<li><a href=\"http://www.transvirtual.com/\">Kaffe</a> </li>\n";
	print MENU "</ul>\n";
#        print MENU "</font>\n";
	print MENU "</td>\n";

	close(MENU);
    }
}


sub menuRecurseChild
{
    my ($item, $FH) = @_;
    my ($k, $tmp, $tmp2) = "";

    print $FH "<UL>\n";
    $tmp = $item . "_child";

    foreach $k (@$tmp)
    {
	if (${$item}{'root'} ne "")
	{
	    $tmp2 = $item . "_" . $k;
	    $tmp = "";
	    $tmp .= "<LI><A HREF=\"http://$Classpath::HTML::conf::website{'url'}/";
	    $tmp .= ${$item}{'root'} . "/";
	    $tmp .= ${$tmp2}{'file'} . "\">";
	    $tmp .= ${$tmp2}{'label'};
	    $tmp .= "</A></LI>";
            print $FH $tmp, "\n";
	} 
	else 
	{
	    $tmp2 = $item . "_" . $k;
	    $tmp = "";
	    $tmp .= "<LI><A HREF=\"http://$Classpath::HTML::conf::website{'url'}/";
	    $tmp .= ${$tmp2}{'file'} . "\">";
	    $tmp .= ${$tmp2}{'label'};
	    $tmp .= "</A></LI>";
	    print $FH $tmp, "\n";
	}
    }
    print $FH "</UL>\n";
}

#--- SUB MENU FILES BELOW

# for each menu item
# if it has a child node
#   open child node file
#   foreach menu node
sub writeSubMenus
{
    my ($i, $j, $k, $tmp, $tmp2) = "";

    foreach $i (@Classpath::HTML::conf::menu)
    {
	if (${"Classpath::HTML::conf::$i"}{'child'} eq "yes")
	{
	    $tmp2 = "Classpath::HTML::conf::" . $i . "_child";
	    foreach $j (@{$tmp2})
	    {
		open(MENU, ">$j-menu.html") || die "cannot open $j-menu.html\n";
		print MENU "<td align=\"left\" bgcolor=\"\#FFFFC8\" width=\"175\">\n";
#		print MENU "<center>\n";
		print MENU "<b>Classpath Pages</b>\n";
		print MENU "<hr align=\"left\" width=\"75\%\">\n";
#		print MENU "</center>\n";
#                print MENU "<font size=\"-1\">\n";
		print MENU "<UL>\n";
		
                foreach $k (@Classpath::HTML::conf::menu)
		{
		    if (${"Classpath::HTML::conf::$k"}{'root'} ne "")
		    {
		        $tmp = "";
		        $tmp .= "<LI><A HREF=\"http://$Classpath::HTML::conf::website{'url'}/";
		        $tmp .= ${"Classpath::HTML::conf::$k"}{'root'} . "/";
		        $tmp .= ${"Classpath::HTML::conf::$k"}{'file'} . "\">";
		        $tmp .= ${"Classpath::HTML::conf::$k"}{'label'};
                        $tmp .= "</A></LI>";
		        print MENU $tmp, "\n";
		    } 
		    else 
		    {
		        $tmp = "";
		        $tmp .= "<LI><A HREF=\"http://$Classpath::HTML::conf::website{'url'}/";
		        $tmp .= ${"Classpath::HTML::conf::$k"}{'file'} . "\">";
		        $tmp .= ${"Classpath::HTML::conf::$k"}{'label'};
                        $tmp .= "</A></LI>";
		        print MENU $tmp, "\n";
		    }
		    if ((${"Classpath::HTML::conf::$k"}{'child'} eq "yes") &&
                        ($i eq $k))
		    {
			# specify which file we're doing now with $i, $j
			&SubMenuRecurseChild("Classpath::HTML::conf::$k", 
					     *MENU{IO}, $i, $j, $k); 
		    }
		}

		print MENU "</ul>\n";
#                print MENU "</font>\n";
		print MENU "<br>\n";
#		print MENU "<center>\n";
		print MENU "<b>Official Specs</b>\n";
		print MENU "<hr align=\"left\" width=\"75\%\">\n";
#		print MENU "</center>\n";
#                print MENU "<font size=\"-1\">\n";
		print MENU "<ul>\n";
		print MENU "<li><a href=\"http://java.sun.com/docs/\">JavaSoft</a></li>\n";
		print MENU "</ul>\n";
#                print MENU "</font>\n";
		print MENU "<br>\n";
#		print MENU "<center>\n";
		print MENU "<b>Free JVM\'s</b>\n";
		print MENU "<hr align=\"left\" width=\"75\%\">\n";
#		print MENU "</center>\n";
#                print MENU "<font size=\"-1\">\n";
		print MENU "<ul>\n";
		print MENU "<li><a href=\"http://www.hungry.com/products/japhar/\">Japhar</a></li>\n";
		print MENU "<li><a href=\"http://www.transvirtual.com/\">Kaffe</a> </li>\n";
		print MENU "</UL>\n";
#                print MENU "</font>\n";
		print MENU "</td>\n";
		
                close(MENU);
            }
        }
    }
}

sub SubMenuRecurseChild
{
    my ($item, $FH, $this_parent, $this_child, $this_item) = @_;
    my ($k, $tmp, $tmp2) = "";

    print $FH "<UL>\n";

    $tmp2 = $item . "_child";
    foreach $k (@{$tmp2})
    {
	if ("$this_parent_$this_child" eq "$this_item_$k") 
	{
	    $tmp2 = $item . "_" . $k;
	    print $FH "<LI>", ${$tmp2}{'label'}, "</LI>\n";
	}
	else
	{
	    if ($$item{'root'} ne "")
	    {
		$tmp2 = $item . "_" . $k;
	        $tmp = "";
	        $tmp .= "<LI><A HREF=\"http://$Classpath::HTML::conf::website{'url'}/";
	        $tmp .= ${$item}{'root'} . "/";
	        $tmp .= ${$tmp2}{'file'} . "\">";
	        $tmp .= ${$tmp2}{'label'};
	        $tmp .= "</A></LI>";
	        print $FH $tmp, "\n";
	    } 
	    else 
	    {
		$tmp2 = $item . "_" . $k;    
	        $tmp = "";
	        $tmp .= "<LI><A HREF=\"http://$Classpath::HTML::conf::website{'url'}/";
	        $tmp .= ${$tmp2}{'file'} . "\">";
	        $tmp .= ${$tmp2}{'label'};
	        $tmp .= "</A></LI>";
	        print $FH $tmp, "\n";
	    }
	}
    }
    print $FH "</UL>\n";
}
