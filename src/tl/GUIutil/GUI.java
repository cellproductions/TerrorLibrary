package tl.GUIutil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.*;

import tl.Util.Cursor;

public class GUI implements GUIInterface
{
	public float x;
	public float y;
	public int width;
	public int height;
	public Image graphic;
	private boolean visible;
	public int ID;
	protected int numControls; // may be obselete
	protected Color background;
	public List<Control> controls;

	protected GUIFunction mouseClick;
	protected GUIFunction mouseOver;

	private boolean setvisibilities = false;

	public GUI()
	{
		x = 0;
		y = 0;
		width = 0;
		height = 0;
		visible = false;
		graphic = null;
		ID = GUIManager.numGUIs;
		GUIManager.numGUIs++;
		numControls = 0;
		controls = new ArrayList<Control>();
	}

	public GUI(String s)
	{
		x = 0;
		y = 0;
		visible = false;
		try
		{
			graphic = new Image(s);
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		width = graphic.getWidth();
		height = graphic.getHeight();
		ID = GUIManager.numGUIs;
		GUIManager.numGUIs++;
		numControls = 0;
		controls = new ArrayList<Control>();
	}

	public GUI(String s, int i, int j, boolean v)
	{
		x = i;
		y = j;
		visible = v;
		try
		{
			graphic = new Image(s);
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		width = graphic.getWidth();
		height = graphic.getHeight();
		ID = GUIManager.numGUIs;
		GUIManager.numGUIs++;
		numControls = 0;
		controls = new ArrayList<Control>();

		setvisibilities = !v;
	}

	public GUI(int i, int j, boolean v, int w, int h, float transparency) throws SlickException
	{
		x = i;
		y = j;
		visible = v;
		width = w;
		height = h;
		ID = GUIManager.numGUIs;
		GUIManager.numGUIs++;
		numControls = 0;

		graphic = new Image(w, h);
		graphic.setAlpha(transparency);
		controls = new ArrayList<Control>();

		setvisibilities = !v;
	}

	public GUI(int i, int j, boolean v, int w, int h, Color border, Color main, float transparency) throws SlickException
	{
		x = i;
		y = j;
		visible = v;
		width = w;
		height = h;
		ID = GUIManager.numGUIs;
		GUIManager.numGUIs++;
		numControls = 0;
		background = main;

		graphic = new Image(w, h);
		Graphics s = graphic.getGraphics();
		s.setColor(main);
		s.fillRect(0, 0, w - 1, h - 1);
		s.setColor(border);
		s.drawRect(0, 0, w - 1, h - 1);
		s.flush();
		graphic.setAlpha(transparency);
		controls = new ArrayList<Control>();

		setvisibilities = !v;
	}

	public void addControl(String name, GUIControl control)
	{
		try
		{
			control.setGUI(this);
		}
		catch (TGUIException e)
		{
			e.printStackTrace();
		}
		Iterator<Control> itr = controls.iterator();
		for (int i = 0; itr.hasNext(); ++i)
		{
			if (control.priority <= itr.next().guiControl.priority)
			{
				controls.add(i, new Control(name, control));
				return;
			}
		}
		controls.add(new Control(name, control));
	}

	public void removeControl(String name) throws TGUIException
	{
		Iterator<Control> itr = controls.iterator();
		for (int i = 0; itr.hasNext(); ++i)
		{
			Control control = itr.next();
			if (control.name.equals(name))
			{
				controls.remove(i);
				return;
			}
		}
		throw new TGUIException("control '" + name + "' does not exist!");
	}

	public GUIControl getControl(String name)
	{
		Iterator<Control> itr = controls.iterator();
		while (itr.hasNext())
		{
			Control c = itr.next();
			if (c.name.equals(name))
				return c.guiControl;
		}
		try
		{
			throw new TGUIException("control '" + name + "' does not exist!");
		}
		catch (TGUIException e)
		{
			e.printStackTrace();
		}
		return null; // here for the sake of syntax
	}

	public void setControlAlphas() // sets all controls' alphas of this GUI to this GUI's alpha
	{
		for (Control itr : controls)
			itr.guiControl.graphic.setAlpha(graphic.getAlpha());
	}

	public void setControlAlphas(float alpha)
	{
		for (Control itr : controls)
			itr.guiControl.graphic.setAlpha(alpha);
	}

	public void setAllEnabled(boolean enabled) // enables all controls, and this gui (add enabled field to GUI)
	{
		for (Control itr : controls)
			itr.guiControl.enabled = enabled;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
		for (Control itr: controls)
			itr.guiControl.setVisible(visible);
	}

	public void setPosition(float i, float j)
	{
		x = i;
		y = j;
		GUIControl control;
		for (Control itr : controls)
		{
			control = itr.guiControl;
			float xx = control.x;
			float yy = control.y;
			control.gx = x + xx;
			control.gy = y + yy;
		}
	}

	public boolean getVisibility()
	{
		return visible;
	}

	public void update(Graphics g)
	{
		if (setvisibilities)
		{
			for (Control itr: controls)
				if (itr.guiControl.getVisibility() != visible)
					itr.guiControl.setVisible(visible);
			setvisibilities = false;
		}
		if (graphic.getAlpha() > 0.00F)
			if (visible)
				g.drawImage(graphic, x, y);
		for (Control itr : controls)
			itr.guiControl.update(g);
	}

	public void onMouseClick(GUIFunction function) // implement consequence of this later
	{
		mouseClick = function;
	}

	public void onMouseOver(GUIFunction function) // implement consequence of this later
	{
		mouseOver = function;
	}

	public boolean mouseIsOver()
	{
		return Cursor.getX() >= x && Cursor.getX() <= x + width && Cursor.getY() >= y && Cursor.getY() <= y + height;
	}

	public boolean mouseButtonDown(int button) // may be irrelevant now
	{
		return GUIManager.guiInput.isMouseButtonDown(button) && mouseIsOver() && visible;
	}

	@Override
	public void mousePressed(int button, int x, int y) 
	{
		for (Control itr : controls)
			itr.guiControl.mousePressed(button, x, y);
	}

	@Override
	public void mouseReleased(int button, int x, int y) 
	{
		for (Control itr : controls)
			itr.guiControl.mouseReleased(button, x, y);
	}

	@Override
	public void mouseWheelMoved(int change)
	{
		for (Control itr: controls)
			itr.guiControl.mouseWheelMoved(change);
	}

	@Override
	public void keyPressed(int key, char c) 
	{
		for (Control itr : controls)
			itr.guiControl.keyPressed(key, c);
	}

	@Override
	public void keyReleased(int key, char c)
	{
		for (Control itr : controls)
			itr.guiControl.keyReleased(key, c);
	}

	public class Control
	{
		public String name;
		public GUIControl guiControl;

		public Control(String n, GUIControl c)
		{
			name = n;
			guiControl = c;
		}
	}
}