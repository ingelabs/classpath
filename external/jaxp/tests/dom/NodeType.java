package tests.dom;


import org.w3c.dom.*; 
import javax.xml.parsers.*; 
import java.io.*; 
 
/** 
 * NodeType DOM test.
 * Test that the values of Dom nodes are correct.
 *
 * 
 * @author <a href="mailto:arnaud.vandyck@ulg.ac.be">Arnaud Vandyck</a>
 * @created Thu Oct 10 21:44:07 2002 
 * @version 
 */ 
public class NodeType 
{ 
  
    public static String[] NODETYPE =  
    { 
        "Unknow Node", "ELEMENT_NODE", "ATTRIBUTE_NODE", "TEXT_NODE",  
        "CDATA_SECTION_NODE", "ENTITY_REFERENCE_NODE", "ENTITY_NODE",  
        "PROCESSING_INSTRUCTION_NODE", "COMMENT_NODE", "DOCUMENT_NODE",  
        "DOCUMENT_TYPE_NODE", "DOCUMENT_FRAGMENT_NODE", "NOTATION_NODE" 
    }; 
 
    public NodeType () 
    { 
    } 
     
    public static void main(String[] args) 
    { 
        try 
	  { 
	    System.out.println( "Starting processing..." ); 
            
	    DocumentBuilder domManager = DocumentBuilderFactory.newInstance().newDocumentBuilder(); 
                 
	    FileInputStream fis = new FileInputStream("tests/dom/nodetype.xml"); 
	    Document d = domManager.parse( fis ); 
	    System.out.println( "File has been passed to the processor..." ); 

	    Element root = d.getDocumentElement(); 
	    System.out.println( "Root tag name: " + root.getTagName() ); 
                
	    printChildNodes( root.getChildNodes() ); 
	  } 
        catch (Exception e1) 
	  { 
	    e1.printStackTrace(); 
	  } 
    } 
 
    static private void printChildNodes( NodeList list ) 
    { 
        int l = list.getLength(); 
        for (int i=0; i<l; i++)  
	  { 
	    Node node = list.item( i ); 
	    printNode( node ); 
	    printChildNodes( node.getChildNodes() ); 
	  } 
    } 
     
    static private void printNode( Node node ) 
    { 
        System.out.println( node.getNodeName()
			    + " [" + NODETYPE[node.getNodeType()] + "]: "
			    + node.getNodeValue() ); 
    } 
     
}
