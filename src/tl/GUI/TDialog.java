package tl.GUI;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class TDialog extends TGUIComponent
{
	private String title;
	private String text;
	private boolean frameOn;
	private TButton bQuit;
	private ArrayList<TButton> buttons;
	public final Color frame_background = new Color(TGUIManager.BUTTON_MAIN);
	public final Color frame_border = new Color(TGUIManager.BUTTON_BORDER);
	public final Color title_colour = new Color(TGUIManager.BLACK);
	public final Color message_colour = new Color(TGUIManager.BLACK);
	public final Color background = new Color(TGUIManager.GUI_MAIN);
	public final Color border = new Color(TGUIManager.GUI_BORDER);
	
	public TDialog(String title, String message, boolean frame, TButton... buttons)
	{/*
		super(null, (float)TGUIManager.screenWidth / 2, (float)TGUIManager.screenHeight / 2, 150, 100);
		this.title = title;
		this.text = message;
		frameOn = frame;
		this.buttons = new ArrayList<TButton>(buttons.length);
		for (TButton button : buttons)
			this.buttons.add(button);
		bQuit = new TButton(this, size.width - 23, 3, 20, 20, "X")
		{
			onMouseRelease(new TGUIClickedEvent()
			{
				public void execute(int button, int x, int y, TGUIComponent control)
				{
					
				}
			});
		};*/
	}
	
	protected void change()
	{
		changed = false;
	}
	
	protected void draw(Graphics g)
	{
		g.fillRect(screenPos.x, screenPos.y, size.width, 26);
	}
	
	public void update(Graphics g)
	{
		
	}
}
