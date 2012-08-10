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
	 * Signature last updated on July 18, 2012 @ 15:40:42:934
	 */
	private static final long serialVersionUID = 20120718154042934L;
	
	private String sBadName;
	
	/**
	 * Creates a new Exception with the bad nick.
	 * 
	 * @param name
	 *            The nick that triggered this exception.
	 */
	public NonUniqueNickException(String name)
	{
		sBadName = name;
	}
	
	@Override
	public String getMessage()
	{
		return sBadName + " is Non-Unique!";
	}
	
	/**
	 * Gets the Bad Nick that triggered this exception.
	 * 
	 * @return A string containing the bad nick.
	 */
	public String getBadName()
	{
		return sBadName;
	}
}
