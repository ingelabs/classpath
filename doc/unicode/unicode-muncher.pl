#!/usr/bin/perl
# This is lots of really ugly hacked up Perl code.
# It works.  Don't touch it, or it'll break.
# I'm far from proud of it.  If you want to fix it so it
# works with strict mode, please do so.
# See unicode.database.format for information on the files
# this program generates.

# usage: unicode-muncher.pl UnicodeData-2.1.2.txt

# Code for reading BLOCKS.TXT and generating stubs for Character.Subset
#
#  open (BLOCKS, $ARGV[1]) || die "Can't open Unicode block file: $!\n";
#  &read_block;
#
#  sub read_block {
#      ($start, $end, $block) = split(/; /, <BLOCKS>);
#      chop $block; chop $block;
#      if ($start =~ /#./) {
#  	&read_block;
#      } else { 
#  	$curr_block++;
#
#  # Generate Character.Subset information	
#  #	$block =~ tr/a-z/A-Z/;
#  #	$block =~ y/ ,-/_/;
#  #	print "$block = new Subset('\\u$start', '\\u$end'),\n";
#
#  	$start = hex($start);
#  	$end   = hex($end);
#
#      };
#  }

sub write_char {
    $S = $jnobreakspace << 5;
    print CHAR 
      (pack ("C", $S | $cat));
    print CHAR (pack ("n", $jnum));
    print CHAR (pack ("n", $jupper));
    print CHAR (pack ("n", $jlower));
}

sub end_block {
    print BLOCK (pack ("n", $start));
    print BLOCK (pack ("n", $lhex));
    $comp = 0 if ($comp != 1);
    print BLOCK (pack ("C", $comp));
    print BLOCK (pack ("N", $offset));

    # calculate next offset
    if ($comp == 1) { 
	$offset += 7; 
    } else { 
	$offset += (7 * ($lhex - $start + 1)); 
    }
    # setup next starting block
    $start = $hex;
    # default to unknown compression
    $comp = 2;
}

open (DATA, $ARGV[0]) || die "Can't open Unicode attribute file: $!\n";

open (TITLECASE, ">titlecase.uni") || die ("Can't open titlecase file: $!\n");
open (CHAR, ">character.uni") || die ("Can't open character file: $!\n");
open (BLOCK, ">block.uni") || die ("Can't open block file: $!\n");

$| = 1;
print "GNU Classpath Unicode Attribute Database Generator 1.0\n";
print "Copyright (C) 1998, Free Software Foundation, Inc.\n";
print "Creating";

$offset = 0;
$hex = -1;
while (<DATA>) {
    $llhex = $lhex;
    $lhex = $hex;
    ($hex, $name, $category, $comb_class, $bidir, $decomp, $decimal, $digit,
     $numeric, $mirrored, $unicode1n, $comment, $upcase, $lowcase, $titlecase)
	= split(/;/);

    print "." if (++$count % 1000 == 0);
	
    # convert unicode index strings to hex
    $hex = hex($hex);
    $upcase = hex($upcase);
    $lowcase = hex($lowcase);
    $titlecase = hex($titlecase);

# read blocks - code used only when counting absolute blocks
#    print "end of block\n" if (($lhex+1) != $hex);
#     # read blocks until we find a valid range
#    while ($hex > $end) {
#	&read_block;
#    }
#    # $curr_block now contains the correct block

# handle isIdentifierIgnorable()
# this is according to the javadoc spec, which is broken.
#    $jidentignore =  ($category =~ /(Cf)|(Cc)/ && 
#		      !($unicode1n =~ /SEPARATOR|RETURN|TAB|FEED/)) ? 1 : 0;

    $jnobreakspace = ($category eq "Zs" && $decomp =~ /noBreak/) ? 1 : 0;

    # handle getNumericValue()
    if ($numeric eq "") {
	$jnum = 65535;		# does not have a numeric value
    } else {
	if ($numeric =~ /^[0-9]+$/) {
	    $jnum = $numeric;	# nonnegative integer value
	    die "sanity: numeric out of range!\n" if ($jnum >= 65534);
	} else { 
	    $jnum = 65534;	# other integer value
	}
    }

    # handle uppercase mapping
    $jupper = $upcase;

    # handle lowercase mapping
    $jlower = $lowcase;

    # handle titlecase mapping - go ahead and output to file
    if ($titlecase != $upcase && $titlecase != 0 && $upcase != 0) {
	print TITLECASE (pack ("n2", $hex, $titlecase));
    }

    # handle category
    $_ = $category;
    CAT: {
	if (/Cn/) { $cat = 0; last CAT; } # unassigned
	if (/Lu/) { $cat = 1; last CAT; } # letter, uppercase
	if (/Ll/) { $cat = 2; last CAT; } # letter, lowercase
	if (/Lt/) { $cat = 3; last CAT; } # letter, titlecase
	if (/Lm/) { $cat = 4; last CAT; } # letter, modifier
	if (/Lo/) { $cat = 5; last CAT; } # letter, other
	if (/Mn/) { $cat = 6; last CAT; } # mark, non-spacing
	if (/Me/) { $cat = 7; last CAT; } # mark, enclosing
	if (/Mc/) { $cat = 8; last CAT; } # mark, spacing combining
	if (/Nd/) { $cat = 9; last CAT; } # number, decimal digit
	if (/Nl/) { $cat = 10; last CAT; } # number, letter
	if (/No/) { $cat = 11; last CAT; } # number, other
	if (/Zs/) { $cat = 12; last CAT; } # separator, space
	if (/Zl/) { $cat = 13; last CAT; } # separator, line
	if (/Zp/) { $cat = 14; last CAT; } # separator, paragraph
	if (/Cc/) { $cat = 15; last CAT; } # other, control
	if (/Cf/) { $cat = 16; last CAT; } # other, format
	# Sun skipped 17 - don't ask me why -- rao
	if (/Co/) { $cat = 18; last CAT; } # other, private use
	if (/Cs/) { $cat = 19; last CAT; } # other, surrogate
	if (/Pd/) { $cat = 20; last CAT; } # punctuation, dash
	if (/Ps/) { $cat = 21; last CAT; } # punctuation, open
	if (/Pe/) { $cat = 22; last CAT; } # punctuation, close
	if (/Pc/) { $cat = 23; last CAT; } # punctuation, connector
	if (/Po/) { $cat = 24; last CAT; } # punctuation, other
	if (/Sm/) { $cat = 25; last CAT; } # symbol, math
	if (/Sc/) { $cat = 26; last CAT; } # symbol, currency
	if (/Sk/) { $cat = 27; last CAT; } # symbol, modifier
	if (/So/) { $cat = 28; last CAT; } # symbol, other
    }

  CHAR: {
      # starting point
      if ($hex == 0) { 
	  &write_char;
	  $comp = 2;		# compressed block state unknown until next ch
	  $start = 0;
	  $offset = 0;
	  last CHAR; 
      }

      # handle mandatory blocks
      if ($name =~ /First>$/) {
	  &end_block;
	  &write_char;
	  $comp = 1;
	  $mand_block = 1;
	  last CHAR;
      }
      # end mandatory block
      if ($mand_block) {
	  $mand_block = 0;
	  last CHAR;
      }

      # not sequential, end block.
      if (($lhex+1) != $hex) { 
	  &end_block; 
	  &write_char; 
	  last CHAR; 
      }

      # check to see if we can compress this character into the current block
      if ($cat == $lcat &&
	  $jnum == $ljnum &&
	  $jnobreakspace == $ljnobreakspace &&
	  $jupper == $ljupper &&
	  $jlower == $ljlower) {
	  if ($comp == 2) { $comp = 1; } # start compressing
	  # end uncompressed block
	  if ($comp == 0) { 
	      $tmp = $lhex;
	      $lhex = $llhex;
	      &end_block;
	      $start = $lhex = $tmp;
	      $comp = 1; 
	  }
      } else { 
  	  if ($comp == 2) { $comp = 0; };
  	  if ($comp == 1) { &end_block; $comp = 2; }
  	  &write_char;
      }
  }

    # copy over all the variables to their "last" counterparts
    $lcat = $cat;
    $ljnum = $jnum;
    $ljnobreakspace = $jnobreakspace;
    $ljupper = $jupper;
    $ljlower = $jlower;
}
$lhex = $hex;			# setup final block write
&end_block;			# write final block

close(DATA);
close(TITLECASE);
close(CHAR);
close(BLOCK);

print "ok\n";

# Second step of compression -- elimate duplicate compressed blocks
# in char.uni.  Should save about 2k; might implement later.
#
#  open (DATA, "<block.uni") || die ("Can't open block file: $!\n");
#  open (CHAR, "<character.uni") || die ("Can't open char file: $!\n");
#  $offset = 0;
#  $comp = 0;
#  while (read DATA, $buf, 9) {
#      ($start, $end, $comp, $off) = unpack("nnCN", $buf);
#      if ($comp == 1) {
#  	seek CHAR, $off, 0;
#  	read CHAR, $cbuf, 7;
#  	push @$cbuf, $off;
#  	$arrays{$cbuf} = $cbuf;
#      }
#  }
