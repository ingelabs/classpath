package javax.swing.text;

import java.awt.*;
import javax.swing.event.*;

public interface Caret
{
    void addChangeListener(ChangeListener l);
    void deinstall(JTextComponent c);
    int getBlinkRate();
    int getDot();
    Point getMagicaretPosition();
    int getMark();
    void install(JTextComponent c);
    boolean isSelectionVisible();
    boolean isVisible();
    void moveDot(int dot);
    void paint(Graphics g);
    void removeChangeListener(ChangeListener l);
    void setBlinkRate(int rate);
    void setDot(int dot);
    void setMagicCaretPosition(Point p);
    void setSelectionVisible(boolean v);
    void setVisible(boolean v);
}
