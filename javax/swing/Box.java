package javax.swing;



/**
 * Needs some work I guess....
 *
 * @author Ronald Veldema (rveldema@cs.vu.nl)
 */




public class Box extends JComponent
{
    Box(int a)
    {
        setLayout(new BoxLayout(this, 
				a));	
    }
}
