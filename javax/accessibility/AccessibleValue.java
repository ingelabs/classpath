package javax.accessibility;

public interface AccessibleValue {
    public abstract Number getCurrentAccessibleValue();
    public abstract Number getMaximumAccessibleValue();
    public abstract Number getMinimumAccessibleValue();
    public abstract boolean setCurrentAccessibleValue(Number number);
}
