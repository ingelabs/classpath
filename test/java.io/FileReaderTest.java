/*************************************************************************
/* FileReaderTest.java -- Test of FileReader and InputStreamReader classes
/*
/* Copyright (c) 1998 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This program is free software; you can redistribute it and/or modify
/* it under the terms of the GNU General Public License as published 
/* by the Free Software Foundation, either version 2 of the License, or
/* (at your option) any later version.
/*
/* This program is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU General Public License for more details.
/*
/* You should have received a copy of the GNU General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

import java.io.*;

public class FileReaderTest
{

public static void
main(String[] argv)
{
  System.out.println("Starting test of FileReader and InputStreamReader");

  System.out.println("Test 1: File Read Test");
  try
    {
      FileReader fr = new FileReader("/etc/services");

      System.out.println("Dumping file. Note that first 100 bytes will be skipped");
      fr.skip(100);

      char[] buf = new char[32];
      int chars_read = 0;

      while((chars_read = fr.read(buf)) != -1)
        System.out.print(new String(buf, 0, chars_read));

      fr.close();
      System.out.println("PASSED: File read test");
    }
  catch(IOException e)
    {
      System.out.println("FAILED: File read test: " + e);
    }

  System.out.println("Test 2: File Not Found Test");
  try
    {
      FileReader fr = new FileReader("/etc/yourmommasawhore");
      System.out.println("FAILED: File Not Found Test");
    }
  catch (FileNotFoundException e)
    {
      System.out.println("PASSED: File Not Found Test");
    }

  System.out.println("Finished test of FileReader");
}

} // class FileReaderTest

