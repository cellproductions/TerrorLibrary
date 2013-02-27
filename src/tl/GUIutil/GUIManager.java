package tl.GUIutil;

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

	public static void init(Input input, int width, int height, TrueTypeFont font)
	{
		guiInput = input;
		screenWidth = width;
		screenHeight = height;
		guiFont = font;
	}
}