package javax.swing;

import java.awt.*;

import java.awt.event.*;
import javax.swing.event.*;



public abstract 
interface ButtonModel extends ItemSelectable
{  
    boolean isArmed();     
    void setArmed(boolean b);


    boolean isEnabled();
    void setEnabled(boolean b);

    void setPressed(boolean b);
    boolean isPressed();


    void removeActionListener(ActionListener l);
    void addActionListener(ActionListener l);

    void addItemListener(ItemListener l);
    void removeItemListener(ItemListener l);
    
    void addChangeListener(ChangeListener l);
    void removeChangeListener(ChangeListener l);

    void setRollover(boolean b);
    boolean isRollover();

    int  getMnemonic();
    void setMnemonic(int key);

    void setActionCommand(String s);
    String getActionCommand();

    void setGroup(ButtonGroup group);

    void setSelected(boolean b);
    boolean isSelected();


    // there are not in the spec !!


    void fireItemStateChanged(ItemEvent event);
    void fireStateChanged(ChangeEvent event);    
    void fireActionPerformed(ActionEvent event);
}
