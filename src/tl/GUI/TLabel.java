package tl.GUI;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import tl.Util.TPoint;
import tl.Util.TSize;


public class TLabel extends TGUIComponent
{
	protected String text;
	protected String toDraw;
	protected TEAlignment textAlign;
	protected int fontHeight;
	protected int gap = 2; // change this or something
	LinkedList<Split> lineList;
	public final Color font_colour = new Color(TGUIManager.BLACK);
	
	protected TGUITextEvent textChange;
	
	@SuppressWarnings("deprecation")
	public TLabel()
	{
		super();
		type = ComponentType.label;
		textAlign = TEAlignment.CENTRE_LEFT;
		lineList = new LinkedList<Split>();
		fontHeight = TGUIManager.guiFont.getHeight();
	}

	@SuppressWarnings("deprecation")
	public TLabel(TGUIComponent parent)
	{
		super(parent);
		type = ComponentType.label;
		textAlign = TEAlignment.CENTRE_LEFT;
		lineList = new LinkedList<Split>();
		fontHeight = TGUIManager.guiFont.getHeight();
		text = "";
	}

	@SuppressWarnings("deprecation")
	public TLabel(TGUIComponent parent, float x, float y, int width, int height, String t) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.label;
		textAlign = TEAlignment.CENTRE_LEFT;
		lineList = new LinkedList<Split>();
		fontHeight = TGUIManager.guiFont.getHeight(t);
		text = t;
		changed = true;
	}
	
	public TLabel(TGUIComponent parent, float x, float y, int width, int height, String t, TEAlignment position) throws SlickException
	{
		this(parent, x, y, width, height, t);
		this.textAlign = position;
	}
	
	protected class Split
	{
		public String line;
		public TSize size;
		public TPoint pos;
		Split(String l, int w, int h) { line = l; size = new TSize(w, h); pos = new TPoint(); }
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
				final String s = toDraw.substring(0, i);
				brackets.add(new Split(s, TGUIManager.guiFont.getWidth(s), TGUIManager.guiFont.getHeight(s)));
				toDraw = toDraw.substring(i + 2);
			}
			brackets.add(new Split(toDraw, TGUIManager.guiFont.getWidth(toDraw), TGUIManager.guiFont.getHeight(toDraw)));
		}
		else
			brackets.add(new Split(toDraw, TGUIManager.guiFont.getWidth(toDraw), TGUIManager.guiFont.getHeight(toDraw)));
		
		int i = 0;
		for (Split split : brackets) // go through the new lines, create new lines depending on wrapping, store them in list
		{
			if (split.size.width >= size.width) // check the final part, is it greater than the width?
			{
				String cut[] = split.line.split(" ");
				String word = new String();
				for (String n : cut)
				{
					if (TGUIManager.guiFont.getWidth(word + " " + n) < size.width)
						word += n + " ";
					else
					{
						list.add(new Split(word, TGUIManager.guiFont.getWidth(word), TGUIManager.guiFont.getHeight(word)));
						word = n + " ";
					}
				}
				int width = TGUIManager.guiFont.getWidth(cut[cut.length - 1]);
				int height = TGUIManager.guiFont.getHeight(cut[cut.length - 1]);
				list.add(new Split(cut[cut.length - 1], width, height));
				list.getLast().pos.set(textAlign == TEAlignment.CENTRE_LEFT ? 0 : (textAlign == TEAlignment.CENTRE_RIGHT ? size.width - width - 1 : (size.width / 2) - (width / 2)), i * fontHeight + ((i + 1) * gap));
			}
			else
			{
				int width = TGUIManager.guiFont.getWidth(split.line);
				int height = TGUIManager.guiFont.getHeight(split.line);
				list.add(new Split(split.line, width, height));
				list.getLast().pos.set(textAlign == TEAlignment.CENTRE_LEFT ? 0 : (textAlign == TEAlignment.CENTRE_RIGHT ? size.width - width - 1 : (size.width / 2) - (width / 2)), i * fontHeight + ((i + 1) * gap));
			}
			++i;
		}
	}
	
	protected void change()
	{
		lineList.clear();
		fillListWithLines(lineList);
		font_colour.a = alpha;
		changed = false;
	}

	protected void draw(Graphics g) throws SlickException
	{/*
		if (graphic == TGUIManager.emptyImage)
			graphic = new Image(size.width, size.height);
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setFont(TGUIManager.guiFont);
		canvas.setColor(TGUIManager.BLACK);
		int i = 0;
		for (Split s : list) // draw it in the position decided by 'position'
		{
			canvas.drawString(s.line, position == TTriBool.TRUE ? 0 : (position == TTriBool.FALSE ? size.width - s.w - 1 : (size.width / 2) - (s.w / 2)), i * 24);
			++i;
		}

		if (TGUIManager.debug)
		{
			canvas.setColor(Color.yellow);
			canvas.drawRect(0, 0, size.width - 1, size.height - 1);
		}
		canvas.flush();*/
		
		g.setFont(TGUIManager.guiFont);
		g.setColor(font_colour);
		for (Split s : lineList)
			g.drawString(s.line, screenPos.x + s.pos.x, screenPos.y + s.pos.y);
		if (TGUIManager.debug)
		{
			g.setColor(TGUIManager.YELLOW);
			g.drawRect(screenPos.x, screenPos.y, size.width, size.height);
		}
		g.setColor(TGUIManager.BLACK);
	}

	public void update(Graphics g)
	{
		if (mouseIsOver())
		{
			if (mouseOver != null)
			{
				mouseOver.execute(this);
				changed = true;
			}
		}
		
		if (changed)
			change();
		
		try
		{
			if (visible && alpha > 0.00f)
				draw(g);
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
	
	public void mousePressed(int button, int x, int y)
	{
		if (enabled)
		{
			if (!mouseIsOver())
				return;
			if (mousePress != null)
			{
				mousePress.execute(button, x, y, this);
				changed = true;
			}
		}
	}
	
	public void mouseReleased(int button, int x, int y)
	{
		if (enabled)
		{
			if (!mouseIsOver())
				return;
			if (mouseRelease != null)
			{
				mouseRelease.execute(button, x, y, this);
				changed = true;
			}
		}
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
			if (biggest < s.size.width)
				biggest = s.size.width;
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
	
	public void setText(String text, TEAlignment alignment)
	{
		setText(text);
		textAlign = alignment;
	}
	
	public void setAlignment(TEAlignment alignment)
	{
		textAlign = alignment;
	}
	
	public String getText()
	{
		return text;
	}
}