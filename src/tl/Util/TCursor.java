package tl.Util;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

public class TCursor extends Object
{
	public static Image graphic;
	public static Input mouseInput;

	public static void init(Input input)
	{
		mouseInput = input;
	}

	public static void setImage(Image image)
	{
		graphic = image;
	}

	public static int getX()
	{
		return mouseInput.getMouseX();
	}

	public static int getY()
	{
		return mouseInput.getMouseY();
	}
}