/* ThreadReferenceCommandSet.java -- class to implement the ThreadReference
   Command Set Copyright (C) 2005 Free Software Foundation
 
This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package gnu.classpath.jdwp.processor;

import gnu.classpath.jdwp.VMFrame;
import gnu.classpath.jdwp.IVirtualMachine;
import gnu.classpath.jdwp.Jdwp;
import gnu.classpath.jdwp.JdwpConstants;
import gnu.classpath.jdwp.exception.InvalidObjectException;
import gnu.classpath.jdwp.exception.JdwpException;
import gnu.classpath.jdwp.exception.JdwpInternalErrorException;
import gnu.classpath.jdwp.exception.NotImplementedException;
import gnu.classpath.jdwp.id.IdManager;
import gnu.classpath.jdwp.id.ObjectId;
import gnu.classpath.jdwp.id.ThreadId;
import gnu.classpath.jdwp.util.JdwpString;
import gnu.classpath.jdwp.util.Location;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * A class representing the ThreadReference Command Set.
 * 
 * @author Aaron Luchko <aluchko@redhat.com>
 */
public class ThreadReferenceCommandSet implements CommandSet
{
  // Our hook into the jvm
  private final IVirtualMachine vm = Jdwp.getIVirtualMachine();

  // Manages all the different ids that are assigned by jdwp
  private final IdManager idMan = Jdwp.getIdManager();

  public boolean runCommand(ByteBuffer bb, DataOutputStream os, byte command)
      throws JdwpException
  {
    try
      {
        switch (command)
          {
          case JdwpConstants.CommandSet.ThreadReference.NAME:
            executeName(bb, os);
            break;
          case JdwpConstants.CommandSet.ThreadReference.SUSPEND:
            executeSuspend(bb, os);
            break;
          case JdwpConstants.CommandSet.ThreadReference.RESUME:
            executeResume(bb, os);
            break;
          case JdwpConstants.CommandSet.ThreadReference.STATUS:
            executeStatus(bb, os);
            break;
          case JdwpConstants.CommandSet.ThreadReference.THREAD_GROUP:
            executeThreadGroup(bb, os);
            break;
          case JdwpConstants.CommandSet.ThreadReference.FRAMES:
            executeFrames(bb, os);
            break;
          case JdwpConstants.CommandSet.ThreadReference.FRAME_COUNT:
            executeFrameCount(bb, os);
            break;
          case JdwpConstants.CommandSet.ThreadReference.OWNED_MONITORS:
            executeOwnedMonitors(bb, os);
            break;
          case JdwpConstants.CommandSet.ThreadReference.CURRENT_CONTENDED_MONITOR:
            executeCurrentContendedMonitor(bb, os);
            break;
          case JdwpConstants.CommandSet.ThreadReference.STOP:
            executeStop(bb, os);
            break;
          case JdwpConstants.CommandSet.ThreadReference.INTERRUPT:
            executeInterrupt(bb, os);
            break;
          case JdwpConstants.CommandSet.ThreadReference.SUSPEND_COUNT:
            executeSuspendCount(bb, os);
            break;
          default:
            throw new NotImplementedException("Command " + command + 
              " not found in Thread Reference Command Set.");
          }
      }
    catch (IOException ex)
      {
        // The DataOutputStream we're using isn't talking to a socket at all
        // So if we throw an IOException we're in serious trouble
        throw new JdwpInternalErrorException(ex);
      }
    return true;
  }

  private void executeName(ByteBuffer bb, DataOutputStream os)
      throws JdwpException, IOException
  {
    ThreadId tid = (ThreadId) idMan.readId(bb);
    Thread thread = (Thread) tid.getObject();
    JdwpString.writeString(os, thread.getName());
  }

  private void executeSuspend(ByteBuffer bb, DataOutputStream os)
      throws JdwpException, IOException
  {
    ThreadId tid = (ThreadId) idMan.readId(bb);
    Thread thread = (Thread) tid.getObject();
    vm.suspendThread(thread);
  }

  private void executeResume(ByteBuffer bb, DataOutputStream os)
      throws JdwpException, IOException
  {
    ThreadId tid = (ThreadId) idMan.readId(bb);
    Thread thread = (Thread) tid.getObject();
    vm.suspendThread(thread);
  }

  private void executeStatus(ByteBuffer bb, DataOutputStream os)
      throws InvalidObjectException, IOException
  {
    ThreadId tid = (ThreadId) idMan.readId(bb);
    Thread thread = (Thread) tid.getObject();
    int threadStatus = vm.getThreadStatus(thread);
    // There's only one possible SuspendStatus...
    int suspendStatus = JdwpConstants.SuspendStatus.SUSPEND_STATUS_SUSPENDED;

    os.writeInt(threadStatus);
    os.writeInt(suspendStatus);
  }

  private void executeThreadGroup(ByteBuffer bb, DataOutputStream os)
      throws InvalidObjectException, IOException
  {
    ThreadId tid = (ThreadId) idMan.readId(bb);
    Thread thread = (Thread) tid.getObject();
    ThreadGroup group = thread.getThreadGroup();
    ObjectId groupId = idMan.getId(group);
    groupId.write(os);
  }

  private void executeFrames(ByteBuffer bb, DataOutputStream os)
      throws JdwpException, IOException
  {
    ThreadId tid = (ThreadId) idMan.readId(bb);
    Thread thread = (Thread) tid.getObject();
    int startFrame = bb.getInt();
    int length = bb.getInt();

    ArrayList frames = vm.getVMFrames(thread, startFrame, length);
    os.writeInt(frames.size());
    for (int i = 0; i < frames.size(); i++)
      {
        VMFrame frame = (VMFrame) frames.get(i);
        os.writeLong(frame.getId());
        Location loc = frame.getLocation();
        loc.write(os);
      }
  }

  private void executeFrameCount(ByteBuffer bb, DataOutputStream os)
      throws JdwpException, IOException
  {
    ThreadId tid = (ThreadId) idMan.readId(bb);
    Thread thread = (Thread) tid.getObject();

    int frameCount = vm.getFrameCount(thread);
    os.writeInt(frameCount);
  }

  private void executeOwnedMonitors(ByteBuffer bb, DataOutputStream os)
      throws JdwpException
  {
    // This command is optional, determined by VirtualMachines CapabilitiesNew
    // so we'll leave it till later to implement
    throw new NotImplementedException(
      "Command OwnedMonitors not implemented.");
  }

  private void executeCurrentContendedMonitor(ByteBuffer bb,
                                              DataOutputStream os)
      throws JdwpException
  {
    // This command is optional, determined by VirtualMachines CapabilitiesNew
    // so we'll leave it till later to implement
    throw new NotImplementedException(
      "Command CurrentContentedMonitors not implemented.");
  }

  private void executeStop(ByteBuffer bb, DataOutputStream os)
      throws JdwpException, IOException
  {
    ThreadId tid = (ThreadId) idMan.readId(bb);
    Thread thread = (Thread) tid.getObject();
    ObjectId exception = idMan.readId(bb);
    vm.stopThread(thread, (Exception) exception.getObject());
  }

  private void executeInterrupt(ByteBuffer bb, DataOutputStream os)
      throws JdwpException, IOException
  {
    ThreadId tid = (ThreadId) idMan.readId(bb);
    Thread thread = (Thread) tid.getObject();
    thread.interrupt();
  }

  private void executeSuspendCount(ByteBuffer bb, DataOutputStream os)
      throws JdwpException, IOException
  {
    ThreadId tid = (ThreadId) idMan.readId(bb);
    Thread thread = (Thread) tid.getObject();
    int suspendCount = vm.getSuspendCount(thread);
    os.writeInt(suspendCount);
  }
}
