package javax.swing.tree;

import java.util.*;

public interface TreeNode
{
    Enumeration children();
    boolean getAllowsChildren();
    TreeNode getChildAt(int childIndex);
    int getChildCount();
    int getIndex(TreeNode node);
    TreeNode getParent();
    boolean isLeaf();
}
 
 
