package tl.GUI;

import tl.Util.TPoint;
import tl.Util.TSize;

public class TGUIHBoxLayout extends TGUILayout
{
	public TGUIHBoxLayout(TGUIObject parent, boolean onAdd)
	{
		super(parent, onAdd);
	}

	public void setSpacing(int spacing) throws TGUIException
	{
		if (spacing >= (parent == null ? TGUIManager.screenWidth : parent.getSize().width))
			throw new TGUIException("spacing [" + spacing + "] must be between 0 and " + (parent == null ? TGUIManager.screenWidth : parent.getSize().width) + ".");
		super.setSpacing(spacing);
	}
	
	public void addSpacing(int spacing) throws TGUIException
	{
		if (spacing >= (parent == null ? TGUIManager.screenWidth : parent.getSize().width))
			throw new TGUIException("spacing [" + spacing + "] must be between 0 and " + (parent == null ? TGUIManager.screenWidth : parent.getSize().width) + ".");
		super.addSpacing(spacing);
	}
	
	public void pOrganise()
	{
		System.out.println("horizontal org:");
		TSize parentSize = getParentSize();
		int largestHeight = 0;
		
		int total = childSizes(parentSize, TEDirection.LeftToRight);
		System.out.println("total " + total);
		float itr = position.x + space;
		for (TGUIObject component : components)
		{
			if (stretch)
				component.setSize(total, parentSize.height - space * 2);
			component.setPosition(itr, !stretch ? component.getPosition().y : position.y + space);
			System.out.println("pos: " + component.getPosition().toFormattedString() + " size: " + component.getSize().toFormattedString());
			itr += component.getSize().width + space;
			int height = component.getSize().height;
			if (largestHeight < height)
				largestHeight = height;
		}
		size.set((int)(itr - position.x), stretch ? parentSize.height : largestHeight);
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
	
	public void setSize(int width, int height) // ignores height, meant to be used after organisation
	{
		TSize size = new TSize(width, height);
		float percentage = this.size.width / width;
		super.setSize(size.width, this.size.height);
		
		float i = 0;
		for (TGUIObject component : components)
		{
			TSize comp = new TSize(component.getSize());
			component.setSize((int)(comp.width * percentage), comp.height);
			TPoint pos = component.getPosition();
			component.setPosition(pos.x * (i * percentage), pos.y);
			++i;
		}
	}
	
	public void setSize(TSize size) // ignores height, meant to be used after organisation
	{
		float percentage = this.size.height / size.height;
		super.setSize(size.width, this.size.height);
		
		float i = 0;
		for (TGUIObject component : components)
		{
			TSize comp = new TSize(component.getSize());
			component.setSize((int)(comp.width * percentage), comp.height);
			TPoint pos = component.getPosition();
			component.setPosition(pos.x * (i * percentage), pos.y);
			++i;
		}
	}
}
