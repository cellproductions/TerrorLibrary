package tl.GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

@SuppressWarnings("deprecation")
public class TGUIManager
{
	public static Image emptyImage;
	public static TrueTypeFont guiFont;
	public static int screenHeight;
	public static int screenWidth;
	public static int numGUIs;
	public static Input guiInput;
	public static boolean debug;

	public static void init(Input input, int width, int height, TrueTypeFont font)
	{
		emptyImage = createEmptyImage();
		guiInput = input;
		screenWidth = width;
		screenHeight = height;
		guiFont = font;
	}
	
	private static Image createEmptyImage()
	{
		Image i = null;
		try
		{
			i = new Image(0, 0);
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		return i;
	}
	
	public static Color COMPONENT_GREYED = new Color(128, 128, 128, .8f);
	public static Color GUI_MAIN = new Color(192, 192, 192);
	public static Color GUI_BORDER = new Color(128, 128, 128);
	public static Color BUTTON_MAIN = new Color(160, 160, 160);
	public static Color BUTTON_BORDER = new Color(70, 70, 70);
	public static Color LISTBOX_BACKGROUND = new Color(168, 168, 168);
	public static Color WHITE = new Color(255, 255, 255);
	public static Color BLACK = new Color(0, 0, 0);
}