package tl.GUI;

import tl.Util.TPoint;
import tl.Util.TSize;

public class TGUIVBoxLayout extends TGUILayout
{
	public TGUIVBoxLayout(TGUIObject parent, boolean onAdd)
	{
		super(parent, onAdd);
	}
	
	public void setSpacing(int spacing) throws TGUIException
	{
		if (spacing >= (parent == null ? TGUIManager.screenHeight : parent.getSize().height))
			throw new TGUIException("spacing [" + spacing + "] must be between 0 and " + (parent == null ? TGUIManager.screenHeight : parent.getSize().height) + ".");
		super.setSpacing(spacing);
	}
	
	public void addSpacing(int spacing) throws TGUIException
	{
		if (spacing >= (parent == null ? TGUIManager.screenHeight : parent.getSize().height))
			throw new TGUIException("spacing [" + spacing + "] must be between 0 and " + (parent == null ? TGUIManager.screenHeight : parent.getSize().height) + ".");
		super.addSpacing(spacing);
	}
	
	public void pOrganise()
	{
		if (TGUIManager.debug)
			System.out.println("vertical org:");
		TSize parentSize = getParentSize();
		int largestWidth = 0;
		
		int total = childSizes(parentSize, TEDirection.TOP_TO_BOTTOM);
		if (TGUIManager.debug)
			System.out.println("total " + total);
		float itr = position.y + space;
		for (TGUIObject component : components)
		{
			if (stretch)
				component.setSize(parentSize.width - (int)position.x - space * 2, total);
			component.setPosition(!stretch ? component.getPosition().x : position.x + space, itr);
			if (TGUIManager.debug)
				System.out.println("pos: " + component.getPosition().toFormattedString() + " size: " + component.getSize().toFormattedString());
			itr += component.getSize().height + space;
			int width = component.getSize().width;
			if (largestWidth < width)
				largestWidth = width;
		}
		size.set(stretch ? parentSize.width - (int)position.x : largestWidth, (int)(itr - position.y));
	}
	
	public void setPosition(float x, float y)
	{
		TPoint other = new TPoint(x, y);
		TPoint diff = other.subtract(position);
		super.setPosition(other);
		
		for (TGUIObject component : components)
			component.setPosition(component.getPosition().add(diff));
	}
	
	public void setPosition(TPoint position)
	{
		TPoint other = new TPoint(position);
		TPoint diff = other.subtract(this.position);
		super.setPosition(other);
		
		for (TGUIObject component : components)
			component.setPosition(component.getPosition().add(diff));
	}
	
	public void setSize(int width, int height)
	{
		TSize size = new TSize(width, height);
		float percentage = this.size.height / height;
		if (TGUIManager.debug)
			System.out.println("perc: " + percentage);
		super.setSize(this.size.width, size.height);

		float i = 0;
		for (TGUIObject component : components)
		{
			TSize comp = new TSize(component.getSize());
			component.setSize(comp.width, (int)(comp.height * percentage));
			TPoint pos = component.getPosition();
			component.setPosition(pos.x, pos.y * (i * percentage));
			++i;
		}
	}
	
	public void setSize(TSize size) // ignores width, meant to be used after organisation
	{
		float percentage = this.size.height / size.height;
		if (TGUIManager.debug)
			System.out.println("perc: " + percentage);
		super.setSize(this.size.width, size.height);

		float i = 0;
		for (TGUIObject component : components)
		{
			TSize comp = new TSize(component.getSize());
			component.setSize(comp.width, (int)(comp.height * percentage));
			TPoint pos = component.getPosition();
			component.setPosition(pos.x, pos.y * (i * percentage));
			++i;
		}
	}
}
