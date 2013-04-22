package tl.GUIutil;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;

@SuppressWarnings("deprecation")
public class GUIManager
{
	public static TrueTypeFont guiFont;
	public static int screenHeight;
	public static int screenWidth;
	public static int numGUIs = 0;
	public static Input guiInput;
	public static boolean debug = false;
	public static enum GUIColor
	{
		GUI_MAIN(128, 128, 128), GUI_BORDER(192, 192, 192), BUTTON_MAIN(160, 160, 160), BUTTON_BORDER(70, 70, 70), WHITE(255, 255, 255), BLACK(0, 0, 0);
		
		Color col;
		GUIColor(int r, int g, int b)
		{
			col = new Color(r, g, b);
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