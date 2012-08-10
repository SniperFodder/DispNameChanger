package me.captain.dnc;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Loads localizations into memory stored by key from {@link DNCStrings}
 * 
 * @author Mark 'SniperFodder' Gunnett
 * 
 */
public class DNCLocalization
{
	private final DispNameChanger plugin;
	
	private HashMap<DNCStrings, String> sStrings;
	
	/**
	 * Default Constructor.
	 */
	public DNCLocalization()
	{
		plugin = DispNameChanger.getInstance();
		
		sStrings = new HashMap<DNCStrings, String>();
	}
	
	/**
	 * Loads Translations from file. The file selected for translation is
	 * set in the plugin.yml file, and is accessed through
	 * {@link DispNameChanger#getLocale}.
	 */
	public void loadTranslations()
	{
		ResourceBundle dncStrings = ResourceBundle.getBundle(
				"translations/DNCStrings", plugin.getLocale());
		
		for (DNCStrings key : DNCStrings.values())
		{
			sStrings.put(key,
					dncStrings.getString(key.toString().toLowerCase()));
		}
	}
	
	/**
	 * Retrieves the localized string for the given {@link DNCStrings} key.
	 * 
	 * @param key
	 *            The DNCStrings Enum for the string to pull.
	 * 
	 * @return The actual localized string.
	 */
	public String getString(DNCStrings key)
	{
		return sStrings.get(key);
	}
	
	/**
	 * Allows the program to adjust the localized string used. This method
	 * is used by DispNameChanger for updating localization strings.
	 * 
	 * @param key
	 *            the DNCStrings enum to change.
	 * 
	 * @param value
	 *            the actual changed string.
	 */
	public void setString(DNCStrings key, String value)
	{
		sStrings.put(key, value);
	}
}
