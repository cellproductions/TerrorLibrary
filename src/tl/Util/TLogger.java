package tl.Util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

public class TLogger
{
	private static FileOutputStream stream;
	private static PrintStream print;
	private static boolean paused = false;
	
	public static void init()
	{
		try
		{
			stream = new FileOutputStream("log.txt");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		print = new PrintStream(stream);
	}
	
	public static void init(FileOutputStream outStream)
	{
		stream = outStream;
		print = new PrintStream(stream);
	}
	
	public static void init(PrintStream outStream)
	{
		print = outStream;
	}
	
	public static void log(String message)
	{
		if (!paused)
			print.println(getDate() + message);
	}
	
	public static void log(Exception e)
	{
		if (!paused)
		{
			print.print(getDate());
			e.printStackTrace(print);
		}
	}
	
	public static void log(String message, Exception e)
	{
		if (!paused)
		{
			print.println(getDate() + message);
			e.printStackTrace(print);
		}
	}
	
	public static void pause()
	{
		if (!paused)
			paused = true;
	}
	
	public static void unPause()
	{
		if (paused)
			paused = false;
	}
	
	public static void deinit()
	{
		try
		{
			if (stream != null)
				stream.close();
			if (print != null)
				if (print != System.out && print != System.err)
					print.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private static String getDate()
	{
		Date date = new Date();
		return date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + " - ";
	}
}
