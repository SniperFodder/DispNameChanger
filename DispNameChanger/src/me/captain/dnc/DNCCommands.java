package me.captain.dnc;

public enum DNCCommands
{
	/**
	 * The command used to trigger a check action.
	 */
	CHECK("check"),
	
	/**
	 * The command used to trigger a rename action.
	 */
	RENAME("rename"),
	
	/**
	 * The command used to trigger a reset action.
	 */
	RESET("reset");
	
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
