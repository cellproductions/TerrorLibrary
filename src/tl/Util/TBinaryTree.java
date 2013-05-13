package tl.Util;

public class TBinaryTree<T extends Comparable<T>> implements TiTree<T>
{
	private TreeNode<T> root;
	
	private TBinaryTree()
	{
		root = new TreeNode<T>();
	}
	
	public void addObject(T object)
	{
		if (root == null)
			root = new TreeNode<T>(null, object);
		else
			root.addObject(object);
	}
	
	public void removeObject(T Object) throws TException
	{
		
	}
	
	public int size()
	{
		if (root == null)
			return 0;
		return root.size(0);
	}
	
	private static class TreeNode<T extends Comparable<T>>
	{
		private T object;
		private TreeNode<T> parent;
		private TreeNode<T> left;
		private TreeNode<T> right;
		
		public TreeNode()
		{
			parent = left = right = null;
		}
		
		private TreeNode(TreeNode<T> parent, T object)
		{
			this.object = object;
		}

		public void addObject(T object) // recursive, change to non-recursive using while loop
		{
			if (object.compareTo(this.object) <= 0)
			{
				if (left == null)
					left = new TreeNode<T>(this, object);
				else
					left.addObject(object);
			}
			else
			{
				if (right == null)
					right = new TreeNode<T>(this, object);
				else
					right.addObject(object);
			}
		}

		public int size(int i) // recursive, change to non-recursive using while loop
		{
			if (left != null)
				i = left.size(i);
			if (right != null)
				i = right.size(i);
			return ++i;
		}
	}
}
