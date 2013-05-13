package tl.GUI;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.*;

public class TGUI implements TGUIInterface
{
	public List<TGUIComponent> controls;
	
	private TGUI()
	{
		controls = new LinkedList<TGUIComponent>();
	}
	
	public TGUI(TGUIComponent ... components)
	{
		this();
		for (TGUIComponent component : components)
			addControl(component);
	}

	public void addControl(TGUIComponent component)
	{
		Iterator<TGUIComponent> itr = controls.iterator();
		for (int i = 0; itr.hasNext(); ++i)
		{
			if (component.compareTo(itr.next()) <= 0)
			{
				controls.add(i, component);
				return;
			}
		}
		controls.add(component);
	}

	public void removeControl(int index) throws TGUIException
	{
		if (index < 0 || index >= controls.size())
			throw new TGUIException("index " + index + " out of bounds! [" + controls.size() + "]");
		controls.remove(index);
	}
	
	public void removeControl(TGUIComponent component) throws TGUIException
	{
		if (!controls.remove(component))
			throw new TGUIException("index " + component.ID + " does not exist!");
	}
	
	public void setVisibility(boolean visible)
	{
		for (TGUIComponent itr : controls)
			itr.setVisible(visible);
	}
	
	public void setTransparency(float transparency) throws TGUIException
	{
		for (TGUIComponent itr : controls)
			itr.setTransparency(transparency);
	}
	
	public void setEnabled(boolean enabled)
	{
		for (TGUIComponent itr : controls)
			itr.setEnabled(enabled);
	}

	public void update(Graphics g)
	{
		for (TGUIComponent itr : controls)
			itr.update(g);
	}

	public void mousePressed(int button, int x, int y) 
	{
		for (TGUIComponent itr : controls)
			itr.mousePressed(button, x, y);
	}

	public void mouseReleased(int button, int x, int y) 
	{
		for (TGUIComponent itr : controls)
			itr.mouseReleased(button, x, y);
	}

	public void mouseWheelMoved(int change)
	{
		for (TGUIComponent itr: controls)
			itr.mouseWheelMoved(change);
	}

	public void keyPressed(int key, char c) 
	{
		for (TGUIComponent itr : controls)
			itr.keyPressed(key, c);
	}

	public void keyReleased(int key, char c)
	{
		for (TGUIComponent itr : controls)
			itr.keyReleased(key, c);
	}
}