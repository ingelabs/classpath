package javax.swing.event;

import java.awt.*;

public class AncestorEvent extends AWTEvent
{
  AncestorEvent(Component src,
		int id)
  {
    super(src, id);
  }
}
