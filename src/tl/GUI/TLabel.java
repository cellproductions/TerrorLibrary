package tl.GUI;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import tl.Util.TException;
import tl.Util.TTriBool;


public class TLabel extends TGUIComponent
{
	protected String text;
	protected String toDraw;
	protected TTriBool position; // TRUE = left, FALSE = right, UNDEFINED = centre
	
	protected TGUITextEvent textChange;
	
	public TLabel()
	{
		super();
		type = ComponentType.label;
		position = TTriBool.TRUE;
	}

	public TLabel(TGUIComponent parent)
	{
		super(parent);
		type = ComponentType.label;
		position = TTriBool.TRUE;
		text = "";
	}

	public TLabel(TGUIComponent parent, float x, float y, int w, int h, String t) throws SlickException
	{
		super(parent, x, y, w, h);
		type = ComponentType.label;
		position = TTriBool.TRUE;
		text = t;
		changed = true;
	}
	
	public TLabel(TGUIComponent parent, float x, float y, int w, int h, String t, byte position) throws SlickException
	{
		this(parent, x, y, w, h, t);
		try
		{
			this.position = TTriBool.parseTriBool(position);
		}
		catch (TException e)
		{
			e.printStackTrace();
		}
	}
	
	protected class Split
	{
		public String line;
		public int w;
		Split(String l, int w) { line = l; this.w = w; }
	}
	
	@SuppressWarnings("deprecation")
	protected void fillListWithLines(LinkedList<Split> list)
	{
		toDraw = text;
		LinkedList<Split> brackets = new LinkedList<>(); // add all the new lines depending on any new line symbols in the text ([[)
		if (toDraw.contains("[["))
		{
			while (toDraw.contains("[["))
			{
				int i = toDraw.indexOf("[[");
				String s = toDraw.substring(0, i);
				int w = TGUIManager.guiFont.getWidth(s);
				brackets.add(new Split(s, w));
				toDraw = toDraw.substring(i + 2);
			}
			brackets.add(new Split(toDraw, TGUIManager.guiFont.getWidth(toDraw)));
		}
		else
			brackets.add(new Split(toDraw, TGUIManager.guiFont.getWidth(toDraw)));
		
		for (Split split : brackets) // go through the new lines, create new lines depending on wrapping, store them in list
		{
			if (TGUIManager.guiFont.getWidth(split.line) >= width) // check the final part, is it greater than the width?
			{
				String cut[] = split.line.split(" ");
				String word = new String();
				for (String n : cut)
				{
					if (TGUIManager.guiFont.getWidth(word + " " + n) < width)
						word += n + " ";
					else
					{
						list.add(new Split(word, TGUIManager.guiFont.getWidth(word)));
						word = n + " ";
					}
				}
				list.add(new Split(cut[cut.length - 1], TGUIManager.guiFont.getWidth(cut[cut.length - 1])));
			}
			else
				list.add(new Split(split.line, TGUIManager.guiFont.getWidth(split.line)));
		}
	}

	private void updateLabel() throws SlickException
	{
		LinkedList<Split> list = new LinkedList<Split>();
		fillListWithLines(list);
		
		if (graphic == null)
			graphic = new Image(width, height);
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setFont(TGUIManager.guiFont);
		canvas.setColor(TGUIManager.BLACK);
		int i = 0;
		for (Split s : list) // draw it in the position decided by 'position'
		{
			canvas.drawString(s.line, position == TTriBool.TRUE ? 0 : (position == TTriBool.FALSE ? width - s.w - 1 : (width / 2) - (s.w / 2)), i * 24);
			++i;
		}

		if (TGUIManager.debug)
		{
			canvas.setColor(Color.yellow);
			canvas.drawRect(0, 0, width - 1, height - 1);
		}
		canvas.flush();
	}

	public void update(Graphics g)
	{
		try
		{
			if (changed)
			{
				updateLabel();
				changed = false;
			}
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		if (visible && graphic.getAlpha() > 0.00F)
			g.drawImage(graphic, gx, gy);
	}
	
	public void mousePressed(int button, int x, int y)
	{
		if (enabled)
		{
			if (!mouseIsOver())
				return;
			if (mouseClick != null)
			{
				mouseClick.execute(button, x, y, this);
				changed = true;
			}
		}
	}
	
	public void onMouseClick(TGUIClickedEvent function)
	{
		mouseClick = function;
	}
	
	public void onTextChange(TGUITextEvent function)
	{
		textChange = function;
	}
	
	public int getWidthOfText()
	{
		LinkedList<Split> list = new LinkedList<Split>();
		fillListWithLines(list);
		
		int biggest = 0;
		for (Split s : list)
		{
			if (biggest < s.w)
				biggest = s.w;
		}
		return biggest;
	}
	
	public void setText(String text)
	{
		this.text = text;
		if (textChange != null)
			textChange.execute(text, this);
		changed = true;
	}
	
	public String getText()
	{
		return text;
	}
}