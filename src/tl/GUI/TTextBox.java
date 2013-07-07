package tl.GUI;

import java.lang.StringBuffer;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class TTextBox extends TGUIComponent
{
	public static TTextBox activeTB;
	
	private String oldText;
	private String text;
	private Position tPosition;
	private float typos; // text y tPosition
	
	private TGUITextEvent textChange;
	private TGUITextEvent enterKey;
	
	public TTextBox()
	{
		super();
		type = ComponentType.textBox;
		if (TTextBox.activeTB == null)
			TTextBox.activeTB = this;
	}

	public TTextBox(TGUIComponent parent)
	{
		super(parent);
		type = ComponentType.textBox;
		if (TTextBox.activeTB == null)
			TTextBox.activeTB = this;
		text = oldText = "";
		tPosition = new Position();
	}
	
	public TTextBox(TGUIComponent parent, float x, float y, int w, int h) throws SlickException
	{
		super(parent, x, y, w, h);
		type = ComponentType.textBox;
		if (TTextBox.activeTB == null)
			TTextBox.activeTB = this;
		text = oldText = "";
		tPosition = new Position();
		changed = true;
	}

	@SuppressWarnings("deprecation")
	public TTextBox(TGUIComponent parent, float x, float y, int w, int h, String def) throws SlickException
	{
		super(parent, x, y, w, h);
		type = ComponentType.textBox;
		if (TTextBox.activeTB == null)
			TTextBox.activeTB = this;
		text = oldText = def;
		tPosition = new Position();
		typos = size.height / 2 - (TGUIManager.guiFont.getHeight(text) / 2) - 2;
		changed = true;
	}

	public boolean isActive()
	{
		return enabled && TTextBox.activeTB == this;
	}
	
	@SuppressWarnings("deprecation")
	private void updateText() throws SlickException
	{
		if (graphic == TGUIManager.emptyImage)
			graphic = new Image(size.width, size.height);
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setFont(TGUIManager.guiFont);
		canvas.setColor(new Color(0, 0, 0));
		canvas.drawRect(0, 0, size.width - 1, size.height - 1);
		typos = size.height / 2 - (TGUIManager.guiFont.getHeight(text) / 2) - 2;
		if (isActive())
			canvas.drawString("_", TGUIManager.guiFont.getWidth(tPosition.get() < text.length() ? text.substring(0, tPosition.get()) : text), typos + 2);
		canvas.drawString(text, 3, typos);
		canvas.flush();
	}
	
	private boolean arrowdown;
	private boolean backspacedown = false;
	private boolean ctrlDown = false;
	private Time bstime = new Time(15f, 3f);
	private Time arrowtime = new Time(15f, 3f);

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
		
		if (isActive() && !text.isEmpty())
		{
			ctrlDown = TGUIManager.guiInput.isKeyDown(Input.KEY_LCONTROL) || TGUIManager.guiInput.isKeyDown(Input.KEY_RCONTROL);
			
			if (backspacedown) // if the backspace key is being held down
			{
				if (bstime.hasFinished())
				{
					if (TGUIManager.guiInput.isKeyDown(Input.KEY_BACK))
					{
						if (tPosition.get() - 1 > -1)
						{
							oldText = text;
							text = new StringBuffer(text).deleteCharAt(tPosition.get() - 1).toString();
							tPosition.left();
							changed = true;
						}
					}
					else if (TGUIManager.guiInput.isKeyDown(Input.KEY_DELETE))
					{
						if (tPosition.get() < text.length())
						{
							oldText = text;
							text = new StringBuffer(text).deleteCharAt(tPosition.get()).toString();
							changed = true;
						}
					}
				}
			}
			else if (arrowdown)
			{
				if (arrowtime.hasFinished())
				{
					if (TGUIManager.guiInput.isKeyDown(Input.KEY_LEFT))
					{
						tPosition.left();
						changed = true;
					}
					else if (TGUIManager.guiInput.isKeyDown(Input.KEY_RIGHT))
					{
						tPosition.right();
						changed = true;
					}
				}
			}
		}
		
		if (!oldText.contentEquals(text))
		{
			oldText = text;
			if (textChange != null)
			{
				textChange.execute(text, this);
				changed = true;
			}
		}
		
		try
		{
			if (changed)
			{
				updateText();
				changed = false;
			}
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}

		if (visible && alpha > 0.00F)
			g.drawImage(graphic, screenPos.x, screenPos.y);
	}
	
	public void mousePressed(int button, int x, int y)
	{
		if (enabled)
		{
			if (mouseIsOver())
			{
				if (button == 0)
				{
					if (TTextBox.activeTB != null)
						TTextBox.activeTB.changed = true;
					TTextBox.activeTB = this;
					changed = true;
				}
			}
			
			if (mousePress != null)
			{
				mousePress.execute(button, x, y, this);
				changed = true;
			}
		}
		
		if (button == 1)
		{
			if (TTextBox.activeTB != null)
				TTextBox.activeTB.changed = true;
			TTextBox.activeTB = null;
			changed = true;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void keyPressed(int key, char c)
	{
		if (enabled)
		{
			if (isActive())
			{
				if (c >= 32 && c <= 126)
				{
					int w = TGUIManager.guiFont.getWidth(text + Character.toString(c));
					if (w < size.width - 4)
					{
						oldText = text;
						text = new StringBuffer(text).insert(tPosition.get(), c).toString();
						tPosition.right();
						changed = true;
					}
				}
				
				if (key == Input.KEY_BACK)
				{
					backspacedown = true;
					bstime.start();
					
					if (!text.isEmpty())
					{
						if (tPosition.get() - 1 > -1)
						{
							oldText = text;
							text = new StringBuffer(text).deleteCharAt(tPosition.get() - 1).toString();
							tPosition.left();
							changed = true;
						}
					}
				}
				else if (key == Input.KEY_DELETE)
				{
					backspacedown = true;
					bstime.start();
					
					if (!text.isEmpty())
					{
						if (tPosition.get() < text.length())
						{
							oldText = text;
							text = new StringBuffer(text).deleteCharAt(tPosition.get()).toString();
							changed = true;
						}
					}
				}
				else if (key == Input.KEY_V)
				{
					if (ctrlDown)
					{
						try
						{
							oldText = text;
							text += (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor); // paste data from clipboard into this text box
							changed = true;
						}
						catch (HeadlessException e)
						{
							e.printStackTrace();
						}
						catch (UnsupportedFlavorException e)
						{
							e.printStackTrace();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						ctrlDown = false;
					}
				}
				else if (key == Input.KEY_ENTER)
				{
					// oldText = text; 			perhaps?
					if (enterKey != null)
					{
						enterKey.execute(text, this);
						changed = true;
					}
				}
				else if (key == Input.KEY_LEFT)
				{
					arrowtime.start();
					tPosition.left();
					arrowdown = true;
					changed = true;
				}
				else if (key == Input.KEY_RIGHT)
				{
					arrowtime.start();
					tPosition.right();
					arrowdown = true;
					changed = true;
				}
			}
		}
	}
	
	public void keyReleased(int key, char c)
	{
		if (key == Input.KEY_BACK)
			backspacedown = false;
		else if (key == Input.KEY_DELETE)
			backspacedown = false;
		else if (key == Input.KEY_LEFT)
			arrowdown = false;
		else if (key == Input.KEY_RIGHT)
			arrowdown = false;
	}
	
	public void onTextChange(TGUITextEvent function)
	{
		textChange = function;
	}
	
	public void onEnterPressed(TGUITextEvent function)
	{
		enterKey = function;
	}
	
	public void setText(String text)
	{
		oldText = text;
		this.text = text;
		if (tPosition.get() > text.length())
			tPosition.set(text.length());
		changed = true;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void clear()
	{
		text = "";
		tPosition.set(0);
	}
	
	private class Position
	{
		int tPosition;
		public void set(int i) { tPosition = i; }
		public void left() { tPosition = tPosition - 1 > -1 ? tPosition - 1 : 0; }
		public void right() { tPosition = tPosition + 1 < text.length() + 1 ? tPosition + 1 : text.length(); }
		public int get() { return tPosition; }
	}
	
	private class Time
	{
		double wait;
		boolean waitdone;
		public double limit;
		public double time;
		
		public Time(double waittime, double limit) { wait = waittime; this.limit = limit; }
		
		public void start()
		{
			waitdone = false;
			time = 0;
		}
		
		public boolean hasFinished()
		{
			if (time < wait && !waitdone)
				++time;
			else
				waitdone = true;
			if (waitdone)
			{
				if (time < limit)
					++time;
				else
				{
					time = 0;
					return true;
				}
			}
			return false;
		}
	}
}