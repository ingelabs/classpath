package javax.swing;

import java.awt.*;

public interface Scrollable
{
    Dimension getPreferredScrollableViewportSize();
    int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction);
    int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction);     
    boolean getScrollableTracksViewportWidth();
    boolean getScrollableTracksViewportHeight();
}
