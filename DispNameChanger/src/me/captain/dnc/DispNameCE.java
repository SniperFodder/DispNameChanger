package me.captain.dnc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import me.captain.dnc.DispNameAPI.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command executor for the DispNameChanger Plugin.
 * 
 * Valid commands:aliases that this executor handles are
 * <code>'rename':'newname'</code>, <code>'reset':'resetname'</code>, and
 * <code>'check':'checkname', 'namecheck', 'realname'</code>.
 * 
 * @author captainawesome7, itsatacoshop247, Daxiongmao87, Luke Zwekii,
 *         Sammy, SniperFodder
 * 
 */
public class DispNameCE implements CommandExecutor
{
	/**
	 * The max players per page allowed for the pagination of DisplayList.
	 */
	public static final int MAX_PLAYERS_PER_PAGE = 7;
	
	private static final Logger log = Bukkit.getLogger();
	
	private DispNameChanger plugin;
	
	private DispNameAPI api;
	
	/**
	 * Constructs a new DispNameCE.
	 * 
	 * @param plugin
	 *            The plugin using this Command Executor.
	 */
	public DispNameCE()
	{
		super();
		
		plugin = DispNameChanger.getInstance();
		
		api = DispNameAPI.getInstance();
	}
	
	/**
	 * Handles checking of a player's name.
	 * 
	 * @param sender
	 *            the entity that executed the command.
	 * 
	 * @param args
	 *            the args given with the command.
	 * 
	 * @return True if a valid command was given.
	 */
	public boolean check(CommandSender sender, String[] args)
	{
		String[] saArgs = api.parseArguments(args);
		
		// No Name to check for.
		if (saArgs.length == 0)
		{
			return false;
		}
		// Name Given to check for.
		else if (saArgs.length == 1)
		{
			Player[] players = api.checkName(saArgs[0]);
			
			if (players.length == 0)
			{
				api.sendMessage(DNCStrings.ERROR_BAD_USER, sender, null,
						MessageType.ERROR);
				
				return true;
			}
			else if (players.length == 1)
			{
				String sName = players[0].getName();
				
				String sDisplay = players[0].getDisplayName();
				
				sDisplay = api.stripPrefix(sDisplay);
				
				sDisplay = ChatColor.stripColor(sDisplay);
				
				Object[] users = new Object[2];
				
				if (sDisplay.equals(saArgs[0]))
				{
					users[0] = (players[0].getDisplayName());
					
					users[1] = sName;
				}
				else
				{
					users[0] = sName;
					
					users[1] = (players[0].getDisplayName());
				}
				
				api.sendMessage(DNCStrings.INFO_CHECK_SINGLE, sender, users,
						MessageType.INFO);
				
				return true;
			}
			else
			{
				Object[] users = new Object[2];
				
				users[0] = saArgs[0];
				
				String sUsers = "";
				
				for (Player p : players)
				{
					sUsers += p.getName() + ", ";
				}
				
				sUsers = sUsers.substring(0, sUsers.lastIndexOf(','));
				
				users[1] = sUsers;
				
				api.sendMessage(DNCStrings.INFO_CHECK_MULTI, sender, null,
						MessageType.CONFIRMATION);
				
				api.sendMessage(DNCStrings.INFO_CHECK_MULTI_LIST, sender,
						users, MessageType.INFO);
				
				return true;
			}
		}
		else if (saArgs.length > 1)
		{
			api.sendMessage(DNCStrings.ERROR_BAD_ARGS, sender, null,
					MessageType.ERROR);
		}
		
		return false;
	}
	
	/**
	 * Handles listing of user with changed display names. Has pagination.
	 * 
	 * @param sender
	 *            the entity that executed the command.
	 * 
	 * @param args
	 *            the args given with the command.
	 * 
	 * @return True if a valid command was given.
	 */
	public boolean list(CommandSender sender, String[] args)
	{
		Object[] oArgs = new Object[4];
		
		Object[] oNames = new Object[2];
		
		Player[] aPlayers;
		
		int iPageNumber;
		
		int iMaxPages;
		
		int iStartIndex;
		
		// Get the page Number
		if (args.length == 0)
		{
			iPageNumber = 1;
		}
		else if (args.length > 1)
		{
			api.sendMessage(DNCStrings.ERROR_BAD_ARGS, sender, null,
					MessageType.ERROR);
			
			return false;
		}
		else
		{
			try
			{
				iPageNumber = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException e)
			{
				api.sendMessage(DNCStrings.ERROR_BAD_INPUT, sender, null,
						MessageType.ERROR);
				
				return false;
			}
			
			if (iPageNumber <= 0)
			{
				iPageNumber = 1;
			}
		}
		
		aPlayers = getPlayerList();
		
		// Get the page Variables
		iMaxPages = getMaxPages(aPlayers.length);
		
		iStartIndex = getMinPlayerIndex(iPageNumber, iMaxPages);
		
		// Ensure we don't exceed max pages.
		if (iPageNumber >= iMaxPages)
		{
			iPageNumber = iMaxPages;
		}
		
		oArgs[0] = "" + iPageNumber;
		
		oArgs[1] = "" + iMaxPages;
		
		oArgs[2] = DNCCommands.LIST.getName();
		
		oArgs[3] = "" + (iPageNumber + 1);
		
		if (iPageNumber >= iMaxPages)
		{
			api.sendMessage(DNCStrings.INFO_LIST_MAX, sender, oArgs,
					MessageType.CONFIRMATION);
		}
		else
		{
			api.sendMessage(DNCStrings.INFO_LIST, sender, oArgs,
					MessageType.CONFIRMATION);
		}
		
		for (int iLoop1 = iStartIndex; iLoop1 <= ((iStartIndex + MAX_PLAYERS_PER_PAGE) - 1); iLoop1++)
		{
			if (iLoop1 > (aPlayers.length - 1))
			{
				break;
			}
			
			oNames[0] = aPlayers[iLoop1].getName();
			
			oNames[1] = aPlayers[iLoop1].getDisplayName();
			
			api.sendMessage(DNCStrings.INFO_CHECK_SINGLE, sender, oNames,
					MessageType.INFO);
		}
		
		return true;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args)
	{
		// Check for the rename command.
		if (cmd.getName().equalsIgnoreCase(DNCCommands.RENAME.getName()))
		{
			return rename(sender, args);
		}
		// Reset name command.
		else if (cmd.getName().equalsIgnoreCase(DNCCommands.RESET.getName()))
		{
			return reset(sender, args);
		}
		// Check name command.
		else if (cmd.getName().equalsIgnoreCase(DNCCommands.CHECK.getName()))
		{
			return check(sender, args);
		}
		// List Names command
		else if (cmd.getName().equalsIgnoreCase(DNCCommands.LIST.getName()))
		{
			return list(sender, args);
		}
		
		return false;
	}
	
	/**
	 * Handles renaming of users on the server with the given name.
	 * 
	 * @param sender
	 *            the entity that executed the command.
	 * 
	 * @param args
	 *            the args given with the command.
	 * 
	 * @return True if a valid command was given.
	 */
	public boolean rename(CommandSender sender, String[] args)
	{
		// parse Arguments into <target> <Name>
		String[] saArgs = api.parseArguments(args);
		
		// No arguments given. Return false.
		if (saArgs.length == 0)
		{
			return false;
		}
		
		switch (saArgs.length)
		{
		// Target = Self
		case 1:
			// Ensure we aren't console.
			if (api.isConsole(sender))
			{
				api.sendMessage(DNCStrings.ERROR_CONSOLE_RENAME, sender, null,
						MessageType.ERROR);
				
				return true;
			}
			
			// Check for colors we aren't allowed to use.
			ChatColor badColor = api.checkForIllegalColors((Player) sender,
					saArgs[0]);
			
			if (badColor != null)
			{
				Object[] obj =
				{ badColor };
				
				if (badColor.isColor())
				{
					api.sendMessage(DNCStrings.PERMISSION_COLOR, sender, obj,
							MessageType.ERROR);
				}
				else
				{
					api.sendMessage(DNCStrings.PERMISSION_STYLE, sender, obj,
							MessageType.ERROR);
				}
				
				return true;
			}
			
			// Ensure that we are allowed to use Spaces.
			if (saArgs[0].contains(" "))
			{
				if (!api.canUseChangeNameSpace(sender))
				{
					api.sendMessage(DNCStrings.PERMISSION_SPACES, sender,
							null, MessageType.ERROR);
					
					return true;
				}
			}
			
			// Attempt to Change Name.
			try
			{
				api.changeDisplayName((Player) sender, saArgs[0]);
				
				return true;
			}
			catch (NonUniqueNickException e)
			{
				api.sendMessage(DNCStrings.ERROR_NON_UNIQUE, sender, null,
						MessageType.ERROR);
				
				return true;
			}
			
			// Target = Other
		case 2:
			// Pull the target we are changing.
			Player[] players = api.checkName(saArgs[0]);
			
			if (players.length == 0)
			{
				api.sendMessage(DNCStrings.ERROR_BAD_USER, sender, null,
						MessageType.ERROR);
				
				return true;
			}
			// Target Match Found
			else if (players.length == 1)
			{
				if (saArgs[1].contains(" "))
				{
					if (!api.canUseChangeNameSpace(sender))
					{
						api.sendMessage(DNCStrings.PERMISSION_SPACES, sender,
								null, MessageType.ERROR);
						
						return true;
					}
				}
				
				try
				{
					api.changeDisplayName((Player) sender, players[0],
							saArgs[1]);
					
					return true;
				}
				catch (NonUniqueNickException e)
				{
					api.sendMessage(DNCStrings.ERROR_NON_UNIQUE, sender, null,
							MessageType.ERROR);
					
					return true;
				}
			}
			else if (players.length > 1)
			{
				api.sendMessage(DNCStrings.ERROR_MULTI_MATCH, sender, null,
						MessageType.ERROR);
				
				return true;
			}
			
			break;
		
		// Default Case: Should never occur.
		default:
			log.severe("API.parseArguments(String[]) returned more than 2 arguments!"
					+ " Inform the Author of this error!");
		}
		
		return false;
	}
	
	/**
	 * Resets a targets name to their login name.
	 * 
	 * @param sender
	 *            the entity that executed the command.
	 * 
	 * @param args
	 *            the args given with the command.
	 * 
	 * @return True if a valid command was given.
	 */
	public boolean reset(CommandSender sender, String[] args)
	{
		String[] saArgs = api.parseArguments(args);
		
		// Reset = Self
		if (saArgs.length == 0)
		{
			if (api.isConsole(sender))
			{
				api.sendMessage(DNCStrings.ERROR_CONSOLE_RENAME, sender, null,
						MessageType.ERROR);
				
				return true;
			}
			else
			{
				Player p = (Player) sender;
				
				try
				{
					api.changeDisplayName(p, p.getName());
					
					return true;
				}
				catch (NonUniqueNickException e)
				{
					log.severe("There was an error reverting from the DisplayName: Display - "
							+ p.getDisplayName()
							+ " | BadNick: "
							+ e.getBadName());
					
					return true;
				}
			}
		}
		// Reset = Other
		else if (saArgs.length == 1)
		{
			if (!api.canUseChangeNameOther(sender))
			{
				api.sendMessage(DNCStrings.PERMISSION_OTHER, sender, null,
						MessageType.ERROR);
				
				return true;
			}
			
			Player[] players = api.checkName(saArgs[0]);
			
			switch (players.length)
			{
			// No User Matched
			case 0:
				api.sendMessage(DNCStrings.ERROR_BAD_USER, sender, null,
						MessageType.ERROR);
				
				return true;
				// One User Matched
			case 1:
				Player p = (Player) sender;
				
				try
				{
					api.changeDisplayName(p, players[0], players[0].getName());
					
					return true;
				}
				catch (NonUniqueNickException e)
				{
					log.severe("There was an error reverting from the DisplayName: Display - "
							+ p.getDisplayName()
							+ " | BadNick: "
							+ e.getBadName());
					
					return true;
				}
				// To Many Users Match
			default:
				api.sendMessage(DNCStrings.ERROR_MULTI_MATCH, sender, null,
						MessageType.ERROR);
				return true;
			}
		}
		else if (saArgs.length > 1)
		{
			api.sendMessage(DNCStrings.ERROR_BAD_ARGS, sender, null,
					MessageType.ERROR);
			
			return false;
		}
		return false;
	}
	
	/**
	 * Gets the maximum number of pages based upon number of entries.
	 * 
	 * @param size
	 *            number of entries total.
	 * 
	 * @return the max number of pages.
	 */
	private int getMaxPages(int size)
	{
		int iReturn = size % MAX_PLAYERS_PER_PAGE;
		
		if (iReturn == 0)
		{
			return size / MAX_PLAYERS_PER_PAGE;
		}
		else
		{
			return (size / MAX_PLAYERS_PER_PAGE) + 1;
		}
	}
	
	/**
	 * Gets the player index to start from in the array.
	 * 
	 * @param pageNumber
	 *            the page number requested.
	 * 
	 * @param maxPages
	 *            the max number of pages.
	 * 
	 * @return the array index to start listing users from.
	 */
	private int getMinPlayerIndex(int pageNumber, int maxPages)
	{
		int iIndex;
		
		if (pageNumber > maxPages)
		{
			iIndex = maxPages;
		}
		else
		{
			iIndex = pageNumber;
		}
		
		return ((iIndex - 1) * MAX_PLAYERS_PER_PAGE);
	}
	
	/**
	 * Returns an array of Players currently on the server ordered by login
	 * name.
	 * 
	 * @return an array of players.
	 */
	private Player[] getPlayerList()
	{
		ArrayList<Player> alPlayers = new ArrayList<Player>();
		
		Player[] aPlayers;
		
		Set<Entry<String, Player>> set = plugin.getOrderedPlayers().entrySet();
		
		Iterator<Entry<String, Player>> i = set.iterator();
		
		while (i.hasNext())
		{
			Map.Entry<String, Player> me = i.next();
			
			if (!me.getValue().getName()
					.equals(me.getValue().getDisplayName()))
			{
				alPlayers.add(me.getValue());
			}
		}
		
		alPlayers.trimToSize();
		
		aPlayers = new Player[alPlayers.size()];
		
		alPlayers.toArray(aPlayers);
		
		return aPlayers;
	}
}
