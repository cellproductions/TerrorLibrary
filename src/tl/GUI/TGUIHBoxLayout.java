package tl.GUI;

public class TGUIHBoxLayout extends TGUILayout
{
	public TGUIHBoxLayout(TGUIComponent parent, boolean onAdd)
	{
		super(parent, onAdd);
	}

	public void setSpacing(int spacing) throws TGUIException
	{
		if (spacing >= (parent == null ? TGUIManager.screenWidth : parent.width()))
			throw new TGUIException("spacing [" + spacing + "] must be between 0 and " + (parent == null ? TGUIManager.screenWidth : parent.width()) + ".");
		super.setSpacing(spacing);
	}
	
	public void addSpacing(int spacing) throws TGUIException
	{
		if (spacing >= (parent == null ? TGUIManager.screenWidth : parent.width()))
			throw new TGUIException("spacing [" + spacing + "] must be between 0 and " + (parent == null ? TGUIManager.screenWidth : parent.width()) + ".");
		super.addSpacing(spacing);
	}
	
	public void pOrganise()
	{
		int w = parent == null ? TGUIManager.screenWidth : parent.width();
		int h = parent == null ? TGUIManager.screenHeight : parent.height();
		
		int total = 0;
		if (stretch)
			total = ((w + space) - ((components.size() + 1) * space)) / components.size(); // total size each component must be to fit into the parent if stretching is on
		float itr = position.x + space;
		for (TGUIComponent component : components)
		{
			if (stretch)
				component.setSize(total, h - space * 2);
			component.setPosition(itr, !stretch ? component.getY() : position.y + space);
			itr += component.width() + space;
		}
	}
}
