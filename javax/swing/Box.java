package javax.swing;



public class Box extends JComponent
{
    Box(int a)
    {
        setLayout(new BoxLayout(this, 
				a));	
    }
}
