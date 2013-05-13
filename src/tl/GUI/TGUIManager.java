package tl.GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;

@SuppressWarnings("deprecation")
public class TGUIManager
{
	public static TrueTypeFont guiFont;
	public static int screenHeight;
	public static int screenWidth;
	public static int numGUIs;
	public static Input guiInput;
	public static boolean debug;
	public static enum GUIColor
	{
		COMPONENT_GREYED(128, 128, 128, .8f), GUI_MAIN(192, 192, 192), GUI_BORDER(128, 128, 128), BUTTON_MAIN(160, 160, 160), BUTTON_BORDER(70, 70, 70), 
		LISTBOX_BACKGROUND(168, 168, 168), WHITE(255, 255, 255), BLACK(0, 0, 0);
		
		Color col;
		GUIColor(int r, int g, int b)
		{
			col = new Color(r, g, b);
		}
		
		GUIColor(int r, int g, int b, float a)
		{
			col = new Color(r, g, b, a);
		}
		
		public Color get()
		{
			return col;
		}
	};

	public static void init(Input input, int width, int height, TrueTypeFont font)
	{
		guiInput = input;
		screenWidth = width;
		screenHeight = height;
		guiFont = font;
	}
}