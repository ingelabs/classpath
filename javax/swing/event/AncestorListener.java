package javax.swing.event;


public interface AncestorListener extends EventListener
{
  void ancestorAdded(AncestorEvent event);
    //        Called when the source or one of its ancestors is made visible either by setVisible(true) being called or by its being added to the component hierarchy.
  void ancestorMoved(AncestorEvent event);
  //          Called when either the source or one of its ancestors is moved.
  void ancestorRemoved(AncestorEvent event);
  //          Called when the source or one of its ancestors is made invisible either by setVisible(false) being called or by its being remove from the component hierarchy. 

}
