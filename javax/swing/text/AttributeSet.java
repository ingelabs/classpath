package javax.swing.text;

import java.util.*;


public interface AttributeSet
{ 
    boolean containsAttribute(Object name, Object value);
    boolean containsAttributes(AttributeSet attributes);
    AttributeSet copyAttributes();
    Object getAttribute(Object key);
    int getAttributeCount();
    Enumeration getAttributeNames();
    AttributeSet getResolveParent();
    boolean isDefined(Object attrName);
    boolean isEqual(AttributeSet attr);     
}
