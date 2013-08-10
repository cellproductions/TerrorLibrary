package tl.Util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import tl.Util.TException;

import tl.Util.TConfigLoader.Setting;

/**
 * TConfig represents a configuration file and all its options and their values.<br>
 * An option's value can be changed to anything that overrides the toString() method.
 * 
 * @author Callum Nichols
 * @since 8th August, 2013
 */
public class TConfig implements Iterable<Setting>
{
	/** The path name of the configuration file. */
	private String path;
	/** A list of all the settings from a configuration file. */
	private LinkedList<Setting> list;
	
	public TConfig(String path)
	{
		this.path = new String(path);
		list = new LinkedList<>();
	}
	
	/**
	 * Set's the path name to the path name specified.
	 * @param path - The path name to set
	 */
	public void setPath(String path)
	{
		this.path = new String(path);
	}
	
	/**
	 * Adds a setting to the list of settings.
	 * @param setting - The setting to add
	 */
	void addSetting(Setting setting)
	{
		list.add(setting);
	}
	
	/**
	 * Returns an option's value. If the option isn't found, a TException is thrown.
	 * @param option - The name of the option as it appears in its respective configuration file
	 * @return - The option's value, or throws an exception
	 * @throws TException
	 */
	public String getValue(String option) throws TException
	{
		for (Setting setting : list)
			if (setting.option.contentEquals(option))
				return setting.value;
		throw new TException("Option " + option + " not found!");
	}
	
	/**
	 * Sets an option's value to anything that overrides the toString() method.<br>
	 * If the option isn't found, a TException is thrown.
	 * @param option - The option that's value needs changing
	 * @param value - The value to replace with
	 * @throws TException
	 */
	public <T> void setValue(String option, T value) throws TException
	{
		for (Setting setting : list)
			if (setting.option.contentEquals(option))
				setting.value = value.toString();
		throw new TException("Option " + option + " not found!");
	}
	
	/**
	 * Returns a list of all the options found within the configuration file.
	 * @return - A List of all the options 
	 */
	public List<String> getOptions()
	{
		LinkedList<String> options = new LinkedList<>();
		for (Setting setting : list)
			options.add(setting.option);
		return options;
	}
	
	public String getPath()
	{
		return path;
	}
	
	/**
	 * Empties the list of settings, and sets the path name to an empty String.
	 */
	public void clear()
	{
		list.clear();
		path = "";
	}

	@Override
	public Iterator<Setting> iterator()
	{
		return list.iterator();
	}
}
