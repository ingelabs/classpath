/*************************************************************************
/* TextComponentPeer.java -- Superclass interface for text components
/*
/* Copyright (c) 1999 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This library is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, either version 2 of the License, or
/* (at your option) any later verion.
/*
/* This library is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU Library General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this library; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.awt.peer;

public interface TextComponentPeer extends ComponentPeer
{

public abstract int getSelectionEnd();
public abstract int getSelectionStart();
public abstract String getText();
public abstract void setText(String text);
public abstract void select(int start_pos, int end_pos);
public abstract void setEditable(boolean editable);
public abstract void getCaretPosition();
public abstract void setCaretPosition(int pos);

} // interface TextComponentPeer 

