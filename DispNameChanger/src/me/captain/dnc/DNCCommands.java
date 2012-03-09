package me.captain.dnc;

public enum DNCCommands
{
	RENAME("rename"),
	
	RESET("reset"),
	
	CHECK("check");
	
	private String sCommand;
	
	DNCCommands(String command)
	{
		sCommand = command;
	}
	
	public String getName()
	{
		return sCommand;
	}
}
