package me.captain.dnc;

/**
 * Used whenever a non-unique name is detected.
 * 
 * @author Mark 'SniperFodder' Gunnett
 *
 */
public class NonUniqueNickException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 20120718154042934L;
	
	private String sBadName;
	
	public NonUniqueNickException(String name)
	{
		sBadName = name;
	}

	@Override
	public String getMessage()
	{
		return sBadName + " is Non-Unique!";
	}
	
	public String getBadName()
	{
		return sBadName;
	}
}
