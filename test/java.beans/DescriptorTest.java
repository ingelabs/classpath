import java.beans.*;

public class DescriptorTest {
	public static void main(String[] args) {
		try {
			new PropertyDescriptor("class",java.lang.Object.class);
			System.out.println("PASSED: Property Object.class");
		} catch(IntrospectionException e) {
			System.out.println("FAILED: Property Object.class");
			e.printStackTrace();
		}

		try {
			new IndexedPropertyDescriptor("test",TestClass.class);
			System.out.println("PASSED: Indexed Property Component.location");
		} catch(IntrospectionException e) {
			System.out.println("FAILED: Indexed Property Component.location");
			e.printStackTrace();
		}

		try {
			new EventSetDescriptor(java.awt.Button.class,"action",java.awt.event.ActionListener.class,"actionPerformed");
			System.out.println("PASSED: Event Set Button.action");
		} catch(IntrospectionException e) {
			System.out.println("FAILED: Event Set Button.action");
			e.printStackTrace();
		}

		try {
			new MethodDescriptor(java.awt.Component.class.getMethod("getLocation",new Class[0]));
			System.out.println("PASSED: Method Component.getLocation");
		} catch(NoSuchMethodException e) {
			System.out.println("FAILED: No such method: Component.getLocation()");
			e.printStackTrace();
		}
	}
}