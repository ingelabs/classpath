package javax.swing;

import java.awt.*;

public interface Icon
{
  int getIconHeight();
  int getIconWidth();
  void paintIcon(Component c, Graphics g, int x, int y);
}
