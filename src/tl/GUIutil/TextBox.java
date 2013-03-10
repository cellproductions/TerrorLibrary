package tl.GUIutil;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class TextBox extends GUIControl
{
	public static int activeTB = -1;
	private static void setActiveTB(int i) { TextBox.activeTB = i; };
	
	private String oldText;
	private String text;
	
	private GUITextFunction textChange;
	private GUITextFunction enterKey;

	public TextBox()
	{
		super();
		type = GUIControl.ControlType.textBox;
		priority = 0;
		text = oldText = null;
	}
	
	public TextBox(int x, int y, int w, int h) throws SlickException
	{
		super(x, y, w, h);
		type = GUIControl.ControlType.textBox;
		priority = 0;
		text = "";
		oldText = "";
		graphic = new Image(w, h);
		changed = true;
	}

	public TextBox(int x, int y, int w, int h, String def) throws SlickException
	{
		super(x, y, w, h);
		type = GUIControl.ControlType.textBox;
		priority = 0;
		graphic = new Image(w, h);
		text = oldText = def;
		changed = true;
	}

	public boolean isActive()
	{
		return enabled && TextBox.activeTB == ID;
	}

	private String toDraw;
	
	@SuppressWarnings("deprecation")
	private void updateText() throws SlickException
	{
		Graphics s = graphic.getGraphics();
		s.clear();
		s.setFont(GUIManager.guiFont);
		s.setColor(new Color(0, 0, 0));
		s.draw(new Rectangle(1, 1, width - 1, height - 1));
		int w = GUIManager.guiFont.getWidth(toDraw + "_");
		if (w > width + 16)
			toDraw = toDraw.substring(0, toDraw.length() - 1);
		s.drawString(toDraw, 3, height / 2 - (GUIManager.guiFont.getHeight(toDraw) / 2) - 2);
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
			ctrlDown = GUIManager.guiInput.isKeyDown(Input.KEY_LCONTROL) || GUIManager.guiInput.isKeyDown(Input.KEY_RCONTROL);
		
		if (backspaceDown) // if the backspace key is being held down
		{
			if (time < frames) 
				time++;
			else // if timer is complete
			{
				if (GUIManager.guiInput.isKeyDown(Input.KEY_BACK))
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
		
		if (oldText == null) System.out.println("old");
		if (text == null) System.out.println("text");
		if (!oldText.contentEquals(text))
		{
			oldText = text;
			if (textChange != null)
			{
				textChange.execute(text, this);
				changed = true;
			}
		}
		
		toDraw = (TextBox.activeTB == ID ? text + "_" : text);
		
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
					if (TextBox.activeTB > -1) // go through the owningGUI's controls, find the one with the same ID as the activeTB's ID and set it to changed to get rid of its '_'
					{
						Iterator<GUI.Control> itr = owningGUI.controls.iterator();
						GUI.Control ctrl;
						while (itr.hasNext())
						{
							ctrl = itr.next();
							if (ctrl.guiControl.type == GUIControl.ControlType.textBox)
							{
								if (ctrl.guiControl.ID == TextBox.activeTB)
								{
									ctrl.guiControl.changed = true;
									break; // break out of loop
								}
							}
						}
					}
					setActiveTB(ID);
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
			setActiveTB(-1);
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
						int w = GUIManager.guiFont.getWidth(text + Character.toString(c));
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
	
	public void onMouseClick(GUIClickedFunction function)
	{
		mouseClick = function;
	}
	
	public void onMouseOver(GUIMouseOverFunction function)
	{
		mouseOver = function;
	}
	
	public void onTextChange(GUITextFunction function)
	{
		textChange = function;
	}
	
	public void onEnterPressed(GUITextFunction function)
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