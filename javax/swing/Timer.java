package javax.swing;

import java.awt.event.*;
import java.util.*;


public class Timer
{
  int ticks;
  static boolean verbose;
  boolean running;
  boolean repeat_ticks = true;
  long interval, init_delay;
  Vector actions = new Vector();
    
  class Waker extends Thread
  {
    public void run()
    {
      running = true;
      try {
	sleep(init_delay);
		
	while (running)
	  {
	    sleep(interval);

	    if (verbose)
	      {
		System.out.println("javax.swing.Timer -> clocktick");
	      }

	    ticks++;
	    fireActionPerformed();
  
	    if (! repeat_ticks)
	      break;
	  }
	running = false;
      } catch (Exception e) {
	System.out.println("swing.Timer::" + e);
      }
    }
  }

  public void addActionListener(ActionListener listener)
  {
    actions.addElement(listener);
  }
  public void removeActionListener(ActionListener listener)
  {
    actions.removeElement(listener);
  }

  void fireActionPerformed()
  {
    for (int i=0;i<actions.size();i++)
      {
	ActionListener a = (ActionListener) actions.elementAt(i);
	a.actionPerformed(new ActionEvent(this, ticks, "Timer"));
      }
  }
  


  public static void setLogTimers(boolean flag)
  {
    verbose = flag;
  }

  public static boolean getLogTimers()
  {
    return verbose;
  }
    

  public void setDelay(int delay)
  {
    interval = delay;
  }

  public int getDelay()
  {
    return (int)interval;
  }


  public void setInitialDelay(int initialDelay)
  {
    init_delay = initialDelay;
  }

  public void setRepeats(boolean flag)
  {
    repeat_ticks = flag;
  }

  boolean isRunning()
  {
    return running;
  }

  void start()
  {
    if (isRunning())
      {
	System.err.println("attempt to start a running timer");
	return;
      }
    new Waker().start();
  }

  void stop()
  {
    running = false;
  }
}
