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
	CHECK("checkname"),
	
	/**
	 * The command used to trigger a rename action.
	 */
	RENAME("rename"),
	
	/**
	 * The command used to trigger a reset action.
	 */
	RESET("reset"),
	
	/**
	 * The command used to trigger a list action.
	 */
	LIST("displaylist");
	
	private String sCommand;
	
	DNCCommands(String command)
	{
		sCommand = command;
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
}
