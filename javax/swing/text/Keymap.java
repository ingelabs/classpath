package javax.swing.text;

import javax.swing.*;

public interface Keymap
{
    void addActionForKeyStroke(KeyStroke key, Action a); 
    Action getAction(KeyStroke key); 
    Action[] getBoundActions(); 
    KeyStroke[] getBoundKeyStrokes(); 
    Action getDefaultAction(); 
    KeyStroke[] getKeyStrokesForAction(Action a); 
    String getName(); 
    Keymap getResolveParent(); 
    boolean isLocallyDefined(KeyStroke key); 
    void removeBindings(); 
    void removeKeyStrokeBinding(KeyStroke keys); 
    void setDefaultAction(Action a); 
    void setResolveParent(Keymap parent);
}


