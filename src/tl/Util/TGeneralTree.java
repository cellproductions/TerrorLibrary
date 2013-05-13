package tl.Util;

import java.util.LinkedList;
import java.util.List;

public class TGeneralTree<T>
{
	private TreeNode<T> root;
	
	@SuppressWarnings("unused")
	private TGeneralTree() {}
	
	public TGeneralTree(T object)
	{
		root = new TreeNode<T>(object);
	}
	
	public TGeneralTree(TGeneralTree<T> tree)
	{
		root = tree.root;
	}
	
	public TreeNode<T> getRoot()
	{
		return root;
	}
	
	public int size()
	{
		int s = 0;
		for (TreeNode<T> node : root.children)
			s += node.size();
		return s;
	}
	
	public static class TreeNode<T>
	{
		private T object;
		private TreeNode<T> parent;
		private List<TreeNode<T>> children;
		
		private TreeNode()
		{
			object = null;
			parent = null;
			children = new LinkedList<TreeNode<T>>();
		}
		
		private TreeNode(T object)
		{
			this();
			this.object = object;
		}
		
		public TreeNode(TreeNode<T> parent)
		{
			this();
			this.parent = parent;
		}
		
		public TreeNode(TreeNode<T> parent, T object)
		{
			this(parent);
			this.object = object;
		}
		
		public void addObject(T object)
		{
			children.add(new TreeNode<T>(this, object));
		}
		
		public T getObject()
		{
			return object;
		}
		
		public void setObject(T object)
		{
			this.object = object;
		}
		
		public TreeNode<T> getParent()
		{
			return parent;
		}
		
		public TreeNode<T> getChild(int index) throws TException
		{
			if (index < 0 || index >= children.size())
				throw new TException("index " + index + " out of bounds! [" + children.size() + "]");
			return children.get(index);
		}
		
		public void removeChild(int index) throws TException
		{
			if (index < 0 || index >= children.size())
				throw new TException("index " + index + " out of bounds! [" + children.size() + "]");
			children.remove(index);
		}
		
		public int childCount()
		{
			return children.size();
		}
		
		public int size()
		{
			int s = 0;
			for (TreeNode<T> node : children)
				s += node.size();
			return s;
		}
		
		public void clear()
		{
			children.clear();
		}
	}
}
