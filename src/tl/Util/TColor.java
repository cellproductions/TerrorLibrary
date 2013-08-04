package tl.Util;

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
}
