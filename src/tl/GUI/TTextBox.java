package tl.GUI;

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
import org.newdawn.slick.geom.Rectangle;

public class TTextBox extends TGUIComponent
{
	public static final ComponentType type = ComponentType.textBox;
	public static TTextBox activeTB;
	
	private String oldText;
	private String text;
	
	private TGUITextEvent textChange;
	private TGUITextEvent enterKey;
	
	public TTextBox()
	{
		super();
		if (TTextBox.activeTB == null)
			TTextBox.activeTB = this;
	}

	public TTextBox(TGUIComponent parent)
	{
		super(parent);
		if (TTextBox.activeTB == null)
			TTextBox.activeTB = this;
		text = oldText = "";
	}
	
	public TTextBox(TGUIComponent parent, float x, float y, int w, int h) throws SlickException
	{
		super(parent, x, y, w, h);
		if (TTextBox.activeTB == null)
			TTextBox.activeTB = this;
		text = oldText = "";
		changed = true;
	}

	public TTextBox(TGUIComponent parent, float x, float y, int w, int h, String def) throws SlickException
	{
		super(parent, x, y, w, h);
		if (TTextBox.activeTB == null)
			TTextBox.activeTB = this;
		text = oldText = def;
		changed = true;
	}

	public boolean isActive()
	{
		return enabled && TTextBox.activeTB == this;
	}

	private String toDraw;
	
	@SuppressWarnings("deprecation")
	private void updateText() throws SlickException
	{
		if (graphic == null)
			graphic = new Image(width, height);
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setFont(TGUIManager.guiFont);
		canvas.setColor(new Color(0, 0, 0));
		canvas.draw(new Rectangle(1, 1, width - 1, height - 1));
		int w = TGUIManager.guiFont.getWidth(toDraw + "_");
		if (w > width + 16)
			toDraw = toDraw.substring(0, toDraw.length() - 1);
		canvas.drawString(toDraw, 3, height / 2 - (TGUIManager.guiFont.getHeight(toDraw) / 2) - 2);
		canvas.flush();
	}
	
	private boolean backspaceDown = false;
	private boolean ctrlDown = false;
	private double time = 0; // used to count each frame
	private final double frames = 3.0;

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
		
		if (isActive())
			ctrlDown = TGUIManager.guiInput.isKeyDown(Input.KEY_LCONTROL) || TGUIManager.guiInput.isKeyDown(Input.KEY_RCONTROL);
		
		if (backspaceDown) // if the backspace key is being held down
		{
			if (time < frames) 
				time++;
			else // if timer is complete
			{
				if (TGUIManager.guiInput.isKeyDown(Input.KEY_BACK))
				{
					if (!text.isEmpty())
					{
						oldText = text;
						text = text.substring(0, text.length() - 1);
						changed = true;
					}
				}
				time = 0;
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
		
		toDraw = (TTextBox.activeTB == this ? text + "_" : text);
		
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

		if (visible && graphic.getAlpha() > 0.00F)
			g.drawImage(graphic, gx, gy);
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
			
			if (mouseClick != null)
			{
				mouseClick.execute(button, x, y, this);
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
					if (toDraw.charAt(toDraw.length() - 1) == '_')
					{
						int w = TGUIManager.guiFont.getWidth(text + Character.toString(c));
						if (w < width - 4)
						{
							oldText = text;
							text += Character.toString(c);
							changed = true;
						}
					}
				}
				
				if (key == Input.KEY_BACK)
				{
					time = 0;
					backspaceDown = true;
					
					if (!text.isEmpty())
					{
						oldText = text;
						text = text.substring(0, text.length() - 1);
						changed = true;
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
			}
		}
	}
	
	public void keyReleased(int key, char c)
	{
		if (key == Input.KEY_BACK)
		{
			backspaceDown = false;
			time = 0;
		}
	}
	
	public void onMouseClick(TGUIClickedEvent function)
	{
		mouseClick = function;
	}
	
	public void onMouseOver(TGUIMouseOverEvent function)
	{
		mouseOver = function;
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
		changed = true;
	}
	
	public String getText()
	{
		return text;
	}
}