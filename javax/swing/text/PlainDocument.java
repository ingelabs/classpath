package javax.swing.text;


public class PlainDocument extends AbstractDocument
{
    PlainDocument()
    {
	super(new GapContent());
    }

    public Element getDefaultRootElement()
    {
	return null;
    }

    public Element getParagraphElement(int  pos)
    {
	return null;
    }
}

