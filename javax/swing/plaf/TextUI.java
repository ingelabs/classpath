package javax.swing.plaf;

import javax.swing.text.*;
import java.awt.*;

public abstract class TextUI extends ComponentUI
{
    TextUI()
    {
    }
 
    abstract  void damageRange(JTextComponent t, int p0, int p1);
    abstract  void damageRange(JTextComponent t, int p0, int p1, Position.Bias firstBias, Position.Bias secondBias);
    abstract  EditorKit getEditorKit(JTextComponent t);
    abstract  int getNextVisualPositionFrom(JTextComponent t, 
					    int pos,
					    Position.Bias b, 
					    int direction,
					    Position.Bias[] biasRet);
    abstract  View getRootView(JTextComponent t);
    abstract  Rectangle modelToView(JTextComponent t, int pos);
    abstract  Rectangle modelToView(JTextComponent t, int pos, Position.Bias bias);
    abstract  int viewToModel(JTextComponent t, Point pt);
    abstract  int viewToModel(JTextComponent t, Point pt, Position.Bias[] biasReturn);
 
}
