#!/usr/bin/perl
# unicode-muncher -- Script to generate Unicode data for java.lang.Character
# Copyright (C) 1998, 2002 Free Software Foundation, Inc.
#
# This file is part of GNU Classpath.
#
# GNU Classpath is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2, or (at your option)
# any later version.
#
# GNU Classpath is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with GNU Classpath; see the file COPYING.  If not, write to the
# Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
# 02111-1307 USA.
#
# Linking this library statically or dynamically with other modules is
# making a combined work based on this library.  Thus, the terms and
# conditions of the GNU General Public License cover the whole
# combination.
#
# As a special exception, the copyright holders of this library give you
# permission to link this library with independent modules to produce an
# executable, regardless of the license terms of these independent
# modules, and to copy and distribute the resulting executable under
# terms of your choice, provided that you also meet, for each linked
# independent module, the terms and conditions of the license of that
# module.  An independent module is a module which is not derived from
# or based on this library.  If you modify this library, you may extend
# this exception to your version of the library, but you are not
# obligated to do so.  If you do not wish to do so, delete this
# exception statement from your version.


# This is lots of really ugly hacked up Perl code.
# It works.  Don't touch it, or it'll break.
# I'm far from proud of it.  If you want to fix it so it
# works with strict mode, please do so.
# See unicode.database.format for information on the files
# this program generates.

# usage: unicode-muncher.pl UnicodeData-3.0.0.txt

sub write_char {
    my $wnum = ($jnum < 65534) ? $hex - $jnum : $jnum;
    $buf = pack ("Cn3c", ($jmirrored << 6) | ($jnobreakspace << 5) | $cat,
                 $wnum, $jupper, $jlower, $jdir);
    print CHAR $buf;
    if (! defined $hash{$buf}) {
        $hash{$buf} = $offset;
    }
    $offset += 8;
}

sub end_block {
    my $joffset;
    print BLOCK (pack ("n", $start));
    print BLOCK (pack ("n", $lhex));
    $characters += $lhex - $start + 1;
    $blocks++;

    # calculate next offset
    if ($comp == 1) {
        $joffset = $hash{$buf};
        if ($joffset + 8 < $offset) { # Reuse a previous attribute
            $offset -= 8;
            seek CHAR, $offset, SEEK_SET;
        }
    } else {
        $joffset = $offset - 8 * ($lhex - $start + 1);
        $comp = 0; # Create a string of uncompressed attributes
    }
    die "sanity: offset out of range!\n" if ($joffset >= 32768);
    print BLOCK (pack ("n", ($comp << 15) | $joffset));

    # setup next starting block
    $start = $hex;
    # default to unknown compression
    $comp = 2;
}

open (DATA, $ARGV[0]) || die "Can't open Unicode attribute file: $!\n";

open (TITLECASE, ">titlecase.uni") || die ("Can't open titlecase file: $!\n");
binmode TITLECASE;
open (CHAR, ">character.uni") || die ("Can't open character file: $!\n");
binmode CHAR;
open (BLOCK, ">block.uni") || die ("Can't open block file: $!\n");
binmode BLOCK;

$| = 1;
print "GNU Classpath Unicode Attribute Database Generator 1.1\n";
print "Copyright (C) 1998, 2002 Free Software Foundation, Inc.\n";
print "Creating";

$offset = 0;
$hex = -1;
$buf = "";
$characters = 0;
$blocks = 0;
$ignored = 0;
while (<DATA>) {
    $llhex = $lhex;
    $lhex = $hex;
    ($hex, $name, $category, $comb_class, $bidir, $decomp, $decimal, $digit,
     $numeric, $mirrored, $unicode1n, $comment, $upcase, $lowcase, $titlecase)
	= split(/;/);

    print "." if (++$count % 1000 == 0);
	
    # convert unicode index strings to hex
    $hex = hex($hex);
    if ($hex > 0xffff) { # Ignore surrogates, since Java does
        $ignored++;
        $lhex = $llhex;
        $hex = $lhex;
        next;
    }
    $upcase = hex($upcase);
    $lowcase = hex($lowcase);
    $titlecase = hex($titlecase);

    $jnobreakspace = ($category eq "Zs" && $decomp =~ /noBreak/) ? 1 : 0;

    # handle getNumericValue(). $jnum is either -1 for no value, -2 for
    # non-positive integer value, or the offset of the value from $hex
    if ($numeric eq "") {
	$jnum = 65535;		# does not have a numeric value
        # Special case sequences of 'a'-'z'
        if ($hex >= 0x0041 && $hex <= 0x005a) {
            $jnum = 0x0037;
        } elsif ($hex >= 0x0061 && $hex <= 0x007a) {
            $jnum = 0x0057;
        } elsif ($hex >= 0xff21 && $hex <= 0xff3a) {
            $jnum = 0xff17;
        } elsif ($hex >= 0xff41 && $hex <= 0xff5a) {
            $jnum = 0xff37;
        }
    } else {
	if ($numeric =~ /^[0-9]+$/) {
	    $jnum = $hex - $numeric;	# nonnegative integer value
	    die "sanity: numeric out of range!\n" if ($jnum >= 65534 ||
                                                      $jnum <= -65534);
	} else {
	    $jnum = 65534;	# other integer value
	}
    }

    # handle uppercase mapping - use the offset from $hex
    $jupper = ($upcase == 0) ? 0 : ($hex - $upcase);

    # handle lowercase mapping - use the offset from $hex
    $jlower = ($lowcase == 0) ? 0 : ($hex - $lowcase);

    # handle titlecase mapping - go ahead and output to file
    if ($titlecase != $upcase) {
        $titlecase = $hex if ($titlecase == 0);
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
        if (/Pi/) { $cat = 29; last CAT; } # punctuation, initial quote
        if (/Pf/) { $cat = 30; last CAT; } # punctuation, final quote
        $cat = 0; # unassigned
    }

    # handle mirrored characters
    $jmirrored = ($mirrored eq "Y") ? 1 : 0;

    # handle directionality
    $_ = $bidir;
    BIDIR: {
	if (/^L$/) { $jdir = 0; last BIDIR; } # Left-to-Right
	if (/^R$/) { $jdir = 1; last BIDIR; } # Right-to-Left
	if (/^AL$/) { $jdir = 2; last BIDIR; } # Right-to-Left Arabic
	if (/^EN$/) { $jdir = 3; last BIDIR; } # European Number
	if (/^ES$/) { $jdir = 4; last BIDIR; } # European Numer Separator
	if (/^ET$/) { $jdir = 5; last BIDIR; } # European Numer Terminator
	if (/^AN$/) { $jdir = 6; last BIDIR; } # Arabic Number
	if (/^CS$/) { $jdir = 7; last BIDIR; } # Common Number Separator
	if (/^NSM$/) { $jdir = 8; last BIDIR; } # Non-Spacing Mark
	if (/^BN$/) { $jdir = 9; last BIDIR; } # Boundary Neutral
	if (/^B$/) { $jdir = 10; last BIDIR; } # Paragraph Separator
	if (/^S$/) { $jdir = 11; last BIDIR; } # Segment Separator
	if (/^WS$/) { $jdir = 12; last BIDIR; } # Whitespace
	if (/^ON$/) { $jdir = 13; last BIDIR; } # Other Neutral
	if (/^LRE$/) { $jdir = 14; last BIDIR; } # Left-to-Right Embedding
	if (/^LRO$/) { $jdir = 15; last BIDIR; } # Left-to-Right Override
	if (/^RLE$/) { $jdir = 16; last BIDIR; } # Right-to-Left Embedding
	if (/^RLO$/) { $jdir = 17; last BIDIR; } # Right-to-Left Override
	if (/^PDF$/) { $jdir = 18; last BIDIR; } # Pop Directional Format
        $jdir = -1; # undefined
    }

  CHAR: {
      # starting point
      if ($hex == 0) {
	  &write_char;
	  $comp = 2;		# compressed block state unknown until next ch
	  $start = 0;
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
	  $jlower == $ljlower &&
          $jmirrored == $ljmirrored &&
          $jdir == $ljdir) {
	  if ($comp == 2) { $comp = 1; } # start compressing
	  # end uncompressed block
	  if ($comp == 0) {
	      $tmp = $lhex;
	      $lhex = $llhex;
              $offset -= 8;
	      &end_block;
	      $start = $lhex = $tmp;
              $offset += 8;
	      $comp = 1;
	  }
      } else {
  	  if ($comp == 2) { $comp = 0; };
  	  if ($comp == 1) { &end_block; }
  	  &write_char;
      }
  }

    # copy over all the variables to their "last" counterparts
    $lcat = $cat;
    $ljnum = $jnum;
    $ljnobreakspace = $jnobreakspace;
    $ljupper = $jupper;
    $ljlower = $jlower;
    $ljmirrored = $jmirrored;
    $ljdir = $jdir;
}
$lhex = $hex;			# setup final block write
&end_block;			# write final block
truncate CHAR, $offset + 8;     # remove final duplicate record, if it exists

close(DATA);
close(TITLECASE);
close(CHAR);
close(BLOCK);

print "ok\n";
print "Created " . ($offset + 8)/8 . " attributes for $characters "
    . "Unicode characters in $blocks blocks.\n"
    . "(Ignored $ignored surrogates.)\n";
