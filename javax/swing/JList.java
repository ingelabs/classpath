package javax.swing;

import javax.swing.event.*;

import java.awt.*;
import javax.swing.plaf.*;
import java.util.*;


public class JList extends JComponent implements Scrollable
{
    Color select_back, select_fore;
    ListCellRenderer render;
    int visibles = 8;
    
    ListModel          model;
    ListSelectionModel sel_model;

    public JList()
    {	
	init();
    }

    public JList(Object[] listData)
    {
	init();
	setListData(listData);
    }


    public JList(Vector listData)
    {
	init();
	setListData(listData);
    }


    public JList(ListModel listData)
    {
	init();
	setModel(listData);
    }
    void init()
    {
	render = new DefaultCellRenderer();
	
	sel_model = new DefaultListSelectionModel();
	setModel(new DefaultListModel());

	select_back = new Color(0,0,255);
	select_fore = new Color(255,255,255);

	updateUI();
    }

    
    public int getVisibleRowCount()
    { return visibles; }
    public void setVisibleRowCount(int visibleRowCount)
    {
	visibles =  visibleRowCount;
	invalidate();
	repaint();
    }

    void addListSelectionListener(ListSelectionListener listener)
    { sel_model.addListSelectionListener(listener);    }
    void removeListSelectionListener(ListSelectionListener listener)
    { sel_model.removeListSelectionListener(listener);    }

    void setSelectionMode(int a)
    {  sel_model.setSelectionMode(a);   }
    void setSelectedIndex(int a)
    {  sel_model.setSelectionInterval(a,a); }
    int getSelectedIndex()
    {	return sel_model.getMinSelectionIndex();    }
    Object getSelectedValue()
    {  
	int index = getSelectedIndex();
	if (index == -1)
	    return null;
	return getModel().getElementAt(index);
    }

    Color getSelectionBackground()
    { return select_back;    }    
    Color getSelectionForeground()
    { return select_fore;    }


    public void setListData(final Object[] listData)
    {
	class AL extends AbstractListModel
	{
	    public int getSize()              { return listData.length; }
	    public Object getElementAt(int i) { return listData[i];     }
	};
	
	setModel (new AL());
    }
    
    public void setListData(final Vector listData)
    {
	class AL extends AbstractListModel 
	{
	    public int getSize()              { return listData.size(); }
	    public Object getElementAt(int i) { return listData.elementAt(i); }
	};
	
        setModel (new AL());
    }
    
    
    public ListCellRenderer getCellRenderer()
    {    return  render; }
    public void setCellRenderer(ListCellRenderer cellRenderer)
    {
	render = cellRenderer;
	invalidate();
	repaint();
    }
    
    public void setModel(ListModel model)
    {
	ListDataListener l = new ListDataListener()
	    {
		public void intervalAdded(ListDataEvent e) {
		    repaint();
		}
		public void intervalRemoved(ListDataEvent e) {
		    repaint();
		}
		public void contentsChanged(ListDataEvent e) {
		    repaint();
		}
	    };
	
	this.model = model;  
	model.addListDataListener(l);	
    }

    public ListModel getModel() 
    {  return model;        }
    
    
    public ListUI getUI()
    {  return (ListUI) ui;    }
    public void setUI(ListUI ui)
    {   super.setUI(ui);      }

    public void updateUI()
    {
        setUI((ListUI)UIManager.getUI(this));
    }

    public String getUIClassID()
    {
	return "JList";
    }


    public AccessibleContext getAccessibleContext()
    {
      return null;
    }

    public Dimension getPreferredScrollableViewportSize()
    {
	return null;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect,
					  int orientation,
					  int direction)
    {
	return 1;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect,
					   int orientation,
					   int direction)
    {
	return 1;
    }

    public boolean getScrollableTracksViewportWidth()
    {
	return false;
    }

    public boolean getScrollableTracksViewportHeight()
    {
	return false;
    }
    
}
