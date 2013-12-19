package tl.Util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * TConfigLoader is a static class used to read configuration files in a specific format and place the info into TConfig instances.<br>
 * The format is very simple. Anything after a hash (#) is ignored, while the rest is treated as a setting. E.g.:<br>
 * <pre>
 * {@code
 * # this is a comment
 * # option on the left, value on the right
 * fullscreen=false
 * music=85 # everything after the hash is a comment
 * }
 * </pre>
 * 
 * @author Callum Nichols
 * @since 8th August, 2013
 */
public class TConfigLoader
{
	/**
	 * Reads a configuration and stores it in a TConfig instance.
	 * @param config - The TConfig container to store all the config data in
	 * @throws FileNotFoundException
	 */
	public static void readConfig(TConfig config) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(new FileInputStream(config.getPath()));
		while (scanner.hasNext())
		{
			String line = scanner.nextLine();
			if (isLegit(line))
				config.addSetting(new Setting(line));
		}
		scanner.close();
	}
	
	/**
	 * Writes all the configuration data from a TConfig to a file.
	 * @param config - The TConfig container that holds all the config data
	 * @throws IOException
	 */
	public static void writeConfig(TConfig config) throws IOException
	{
		Path path = Paths.get(config.getPath());
		String data = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		
		for (Setting setting : config)
		{
			String start = setting.getOption() + "=";
			String replace = new String(start);
			
			replace += setting.value;
			
			data = data.replace(start + setting.getValue(), replace);
		}
		Files.write(path, data.getBytes());
	}
	
	/**
	 * Checks a line from a file for any errors, or whether or not it is a comment line.
	 * @param line - The setting to check
	 * @return - True if the setting is correct and not a comment
	 */
	private static boolean isLegit(String line)
	{
		String test = line.trim();
		if (test.isEmpty())
			return false;
		return !test.startsWith("#") && test.contains("=");
	}
	
	/**
	 * Setting is a class that represents an option and it's value.
	 * 
	 * @author Callum Nichols
	 * @since 8th August, 2013
	 */
	public static class Setting
	{
		/** The option as a String. */
		String option;
		
		/** The option's value as a String. */
		String value;
		
		public Setting(String line)
		{
			option = line.substring(0, line.indexOf('=')).trim();
			value = line.substring(line.indexOf('=') + 1);
			if (value.contains("#"))
				value = value.substring(0, value.indexOf('#')).trim();
		}
		
		final public String getOption()
		{
			return option;
		}
		
		final public String getValue()
		{
			return value;
		}
		
		/**
		 * Returns the option's value, trimmed or un-trimmed.
		 * @param trim - Whether or not the value should be trimmed before being returned
		 * @return - The option's value
		 */
		final public String getValue(boolean trim)
		{
			if (trim)
				return value.trim();
			return value;
		}
	}
}
