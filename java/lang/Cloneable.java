package java.lang;

/**
 * This interface should be implemented by classes wishing to
 * support or override Object.clone().  Cloning an object generally
 * results in a deep copy of that object.  If Object.clone() is called
 * on an object which does not implement the Cloneable interface,
 * a CloneNotSupportedException will be thrown.
 *
 * @see java.lang.Object#clone
 * @see java.lang.CloneNotSupportedException
 */
public interface Cloneable { }
