package me.captain.dnc;

/**
 * Used for the checking and registering of commands in DNC.
 * 
 * @author Mark 'SniperFodder' Gunnett
 * 
 */
public enum DNCCommands
{
	/**
	 * The command used to trigger a check action.
	 */
	CHECK("checkname", DNCStrings.CMD_CHECKNAME_DESCRIPTION, DNCStrings.CMD_CHECKNAME_USAGE, DNCStrings.CMD_CHECKNAME_ALIAS),
	
	/**
	 * The command used to trigger a rename action.
	 */
	RENAME("rename", DNCStrings.CMD_RENAME_DESCRIPTION, DNCStrings.CMD_RENAME_USAGE, DNCStrings.CMD_RENAME_ALIAS),
	
	/**
	 * The command used to trigger a reset action.
	 */
	RESET("reset", DNCStrings.CMD_RESET_DESCRIPTION, DNCStrings.CMD_RESET_USAGE, DNCStrings.CMD_RESET_ALIAS),
	
	/**
	 * The command used to trigger a list action.
	 */
	LIST("displaylist", DNCStrings.CMD_DISPLAYLIST_DESCRIPTION, DNCStrings.CMD_RESET_USAGE, DNCStrings.CMD_RESET_ALIAS);
	
	private String sCommand;
	
	private DNCStrings slDescription;
	
	private DNCStrings slUsage;
	
	private DNCStrings sAlias;
	
	DNCCommands(String command, DNCStrings description, DNCStrings usage, DNCStrings alias)
	{
		sCommand = command;
		
		slDescription = description;
		
		slUsage = usage;
		
		sAlias = alias;
	}
	
	/**
	 * Retrieves the command string related to this command.
	 * 
	 * @return the actual command string.
	 */
	public String getName()
	{
		return sCommand;
	}
	
	/**
	 * Retrieves the DNCStrings enum for the command's description.
	 * 
	 * @return The Description Enumeration for that command.
	 */
	public DNCStrings getDescription()
	{
		return slDescription;
	}
	
	/**
	 * Retrieves the DNCStrings enum for the command's usage.
	 * 
	 * @return The Usage Enumeration for that command.
	 */
	public DNCStrings getUsage()
	{
		return slUsage;
	}
	
	/**
	 * Retrieves the DNCStrings enum for the Command's Localized Alias.
	 * 
	 * @return A DNCString object representing the command's localized alias.
	 */
	public DNCStrings getAlias()
	{
		return sAlias;
	}
}
