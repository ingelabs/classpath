package gnu.java.beans.info;

import gnu.java.beans.*;
import java.beans.*;

/** BeanInfo class for java.awt.Component.
 ** This provides a few properties, but that's
 ** it.
 ** @author John Keiser
 ** @version 1.1.0, Aug 1 1998
 **/
public class ComponentBeanInfo extends SimpleBeanInfo {
	static PropertyDescriptor[] properties;
	static {
		try {
		properties = new PropertyDescriptor[6];
		properties[0] = new PropertyDescriptor("name",java.awt.Component.class);
		properties[1] = new PropertyDescriptor("background",java.awt.Component.class);
		properties[2] = new PropertyDescriptor("foreground",java.awt.Component.class);
		properties[3] = new PropertyDescriptor("font",java.awt.Component.class);
		properties[4] = new PropertyDescriptor("enabled",java.awt.Component.class);
		properties[5] = new PropertyDescriptor("visible",java.awt.Component.class);
		} catch(IntrospectionException E) {
			properties = null;
			throw new UnknownError("Could not introspect some java.awt.Component properties.");
		}
	}
	public ComponentBeanInfo() {
		super();
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		return properties;
	}
}

