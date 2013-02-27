package tl.Util;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Cursor extends Object
{
	public static Image graphic;
	public static Input mouseInput;

	public static void init(Input input)
	{
		mouseInput = input;
	}

	public static void init(Input input, String s)
	{
		try
		{
			graphic = new Image(s);
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}

	public static void setImage(String s) // basically initialises the entire cursor
	{
		try
		{
			graphic = new Image(s);
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}

	public static void setImage(Image i)
	{
		graphic = new Image(i.getTexture());
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