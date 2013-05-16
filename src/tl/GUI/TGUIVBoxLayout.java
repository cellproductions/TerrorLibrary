package tl.GUI;

public class TGUIVBoxLayout extends TGUILayout
{
	public TGUIVBoxLayout(TGUIComponent parent, boolean onAdd)
	{
		super(parent, onAdd);
	}
	
	public void setSpacing(int spacing) throws TGUIException
	{
		if (spacing >= (parent == null ? TGUIManager.screenHeight : parent.height()))
			throw new TGUIException("spacing [" + spacing + "] must be between 0 and " + (parent == null ? TGUIManager.screenHeight : parent.height()) + ".");
		super.setSpacing(spacing);
	}
	
	public void addSpacing(int spacing) throws TGUIException
	{
		if (spacing >= (parent == null ? TGUIManager.screenHeight : parent.height()))
			throw new TGUIException("spacing [" + spacing + "] must be between 0 and " + (parent == null ? TGUIManager.screenHeight : parent.height()) + ".");
		super.addSpacing(spacing);
	}
	
	public void pOrganise()
	{
		int w = parent == null ? TGUIManager.screenWidth : parent.width();
		int h = parent == null ? TGUIManager.screenHeight : parent.height();
		
		int total = 0;
		if (stretch)
			total = ((h + space) - ((components.size() + 1) * space)) / components.size(); // total size each component must be to fit into the parent if stretching is on
		float itr = position.y + space;
		for (TGUIComponent component : components)
		{
			if (stretch)
				component.setSize(w - space * 2, total);
			component.setPosition(!stretch ? component.getX() : position.x + space, itr);
			itr += component.height() + space;
		}
	}
}
