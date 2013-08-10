package tl.Util;

import java.util.Random;

import org.newdawn.slick.Color;

public class TColor
{
	final static short MAX = 255;
	
	public static Color SLtoRGB(float r, float g, float b)
	{
		return new Color((int)(r * MAX), (int)(g * MAX), (int)(b * MAX));
	}
	
	public static Color SLtoRGB(float r, float g, float b, float a)
	{
		return new Color((int)(r * MAX), (int)(g * MAX), (int)(b * MAX), (int)(a * MAX));
	}
	
	public static Color SLtoRGB(Color colour)
	{
		return new Color((int)(colour.r * MAX), (int)(colour.g * MAX), (int)(colour.b * MAX), (int)(colour.a * MAX));
	}
	
	public static Color RGBtoSL(int r, int g, int b)
	{
		return new Color(r / MAX, g / MAX, b / MAX);
	}
	
	public static Color RGBtoSL(int r, int g, int b, int a)
	{
		return new Color(r / MAX, g / MAX, b / MAX, a / MAX);
	}
	
	public static Color RGBtoSL(Color colour)
	{
		return new Color(colour.r / MAX, colour.g / MAX, colour.b / MAX, colour.a / MAX);
	}
	
	public static Color randomColor(boolean alpha)
	{
		Random rand = new Random();
		return new Color((float)rand.nextInt(101) / 100f, (float)rand.nextInt(101) / 100f, (float)rand.nextInt(101) / 100f, alpha ? ((float)rand.nextInt(101) / 100f) : 1f);
	}
	
	public static Color randomShade(boolean alpha)
	{
		Random rand = new Random();
		float shade = rand.nextInt(101) / 100f;
		return new Color(shade, shade, shade, alpha ? ((float)rand.nextInt(101) / 100f) : 1f);
	}
}
