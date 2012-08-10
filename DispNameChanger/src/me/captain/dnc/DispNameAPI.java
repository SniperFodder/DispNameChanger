package me.captain.dnc;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 * Provides API Calls for DNC code and any related plugins.
 * 
 * @author Mark 'SniperFodder' Gunnett
 */
public class DispNameAPI
{
	private static DispNameAPI instance;
	
	private DispNameChanger plugin;
	
	private MessageFormat formatter;
	
	private DNCLocalization locale;
	
	/**
	 * Provides Color for various message types for the @link
	 * {@link DispNameAPI#sendMessage(DNCStrings, CommandSender, Object[], MessageType)}
	 * method.
	 * 
	 * @author Mark 'SniperFodder' Gunnett
	 * 
	 */
	public enum MessageType
	{
		/**
		 * An AQUA Color for information.
		 */
		INFO(ChatColor.AQUA),
		
		/**
		 * A Red Color for Errors.
		 */
		ERROR(ChatColor.RED),
		
		/**
		 * A GREEN color for confirmations.
		 */
		CONFIRMATION(ChatColor.GREEN);
		
		private ChatColor color;
		
		MessageType(ChatColor c)
		{
			color = c;
		}
		
		/**
		 * Get's the ChatColor associated with this MessageType.
		 * 
		 * @return ChatColor to use in coloring a message.
		 */
		public ChatColor getColor()
		{
			return color;
		}
	}
	
	/**
	 * Constructs a new API instance.
	 */
	private DispNameAPI()
	{
		if (instance == null)
		{
			instance = this;
		}
		
		plugin = DispNameChanger.getInstance();
		
		locale = plugin.getLocalization();
		
		formatter = new MessageFormat("");
		
		formatter.setLocale(plugin.getLocale());
	}
	
	/**
	 * Checks to see if the given Sender has the 'dispname.announce'
	 * permission for nick broadcasts.
	 * 
	 * @param s
	 *            the Sender to check.
	 * 
	 * @return true if they have it, false otherwise.
	 */
	public boolean canBroadcast(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.announce");
	}
	
	/**
	 * Checks to see if the Sender can use the command.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseChangeName(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.change");
	}
	
	/**
	 * Checks to see if the Sender can use the command.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseChangeNameOther(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.changeother");
	}
	
	/**
	 * Checks to see if the Sender can use the command.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseChangeNameSpace(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.changespace");
	}
	
	/**
	 * Checks to see if the Sender can use the command.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseCheckName(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.check");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorAqua(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.aqua");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorBlack(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.black");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorBrightGreen(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.brightgreen");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorDarkAqua(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.darkaqua");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorDarkBlue(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.darkblue");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorDarkGreen(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.darkgreen");
	}
	
	public boolean canUseColorDarkGrey(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.darkgrey");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorDarkRed(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.darkred");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorGrey(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.grey");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorIndigo(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.indigo");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorOrange(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.orange");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorPink(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.pink");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorPurple(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.purple");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorRed(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.red");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorWhite(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.white");
	}
	
	/**
	 * Checks to see if the Sender can use the color.
	 * 
	 * @param s
	 *            The Sender to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseColorYellow(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.yellow");
	}
	
	/**
	 * Checks to see if the Sender can use the list command.
	 * 
	 * @param s
	 *            the player to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseList(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.list");
	}
	
	/**
	 * Checks to see if the given Sender has the 'dispname.color.bold'
	 * permission for color code usage.
	 * 
	 * @param s
	 *            the sender to check.
	 * 
	 * @return true if they have it, false otherwise.
	 */
	public boolean canUseStyleBold(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.style.bold");
	}
	
	/**
	 * Checks to see if the given Sender has the
	 * 'dispname.color.style.italic' permission for color code usage.
	 * 
	 * @param p
	 *            the Sender to check.
	 * 
	 * @return true if they have it, false otherwise.
	 */
	public boolean canUseStyleItalic(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.style.italic");
	}
	
	/**
	 * Checks to see if the given Sender has the 'dispname.color.style.magic'
	 * permission for color code usage.
	 * 
	 * @param p
	 *            the Sender to check.
	 * 
	 * @return true if they have it, false otherwise.
	 */
	public boolean canUseStyleMagic(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.style.magic");
	}
	
	/**
	 * Checks to see if the given Sender has the 'dispname.color.style.strike'
	 * permission for color code usage.
	 * 
	 * @param p
	 *            the Sender to check.
	 * 
	 * @return true if they have it, false otherwise.
	 */
	public boolean canUseStyleStrikethrough(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.style.strike");
	}
	
	/**
	 * Checks to see if the given Sender has the 'dispname.color.style.underline'
	 * permission for color code usage.
	 * 
	 * @param p
	 *            the Sender to check.
	 * 
	 * @return true if they have it, false otherwise.
	 */
	public boolean canUseStyleUnderline(CommandSender s)
	{
		if (s == null)
		{
			return true;
		}
		
		return s.hasPermission("dispname.color.style.underline");
	}
	
	/**
	 * Changes the display name of the target.
	 * 
	 * @param target
	 *            the player to change.
	 * 
	 * @param newName
	 *            the new name to use.
	 * 
	 * @throws NonUniqueNickException
	 *             if Non-Unique name is specified and Scoreboard is
	 *             Enabled.
	 */
	public void changeDisplayName(Player target, String newName)
			throws NonUniqueNickException
	{
		changeDisplayName(null, target, newName);
	}
	
	/**
	 * Changes the display name of the target and sends notifications using
	 * regular chat and spout if spout is enabled and the client exists.
	 * 
	 * @param caller
	 *            The person trying to make the change.
	 * 
	 * @param target
	 *            The intended target of the change.
	 * 
	 * @param newName
	 *            The new name to use.
	 * 
	 * @throws NonUniqueNickException
	 *             if Non-Unique name is specified and Scoreboard
	 *             integration is enabled.
	 */
	public void changeDisplayName(Player caller, Player target, String newName)
			throws NonUniqueNickException
	{
		Object[] users = new Object[2];
		
		users[0] = (target.getDisplayName() + ChatColor.GREEN);
		
		String spoutName = newName;
		
		// Parse Color Codes.
		spoutName = parseColors(spoutName);
		
		// Attach the prefix if needed.
		if (!target.getName().equals(newName))
		{
			spoutName = prefixNick(spoutName);
		}
		
		if (plugin.useScoreboard())
		{
			// Ensure unique
			if (!checkUnique(spoutName))
			{
				throw new NonUniqueNickException(spoutName);
			}
		}
		
		users[1] = (spoutName + ChatColor.GREEN);
		
		// Set the DisplayName
		target.setDisplayName(spoutName);
		
		// Ensure Name will fit with scoreboard.
		if (plugin.useScoreboard())
		{
			String sListName;
			
			if (spoutName.length() > 16)
			{
				sListName = spoutName.substring(0, 16);
				
				// Ensure no chat color char is appended to a name.
				if (sListName.endsWith(String.valueOf(ChatColor.COLOR_CHAR)))
				{
					sListName = sListName.substring(0, 15);
				}
			}
			else
			{
				sListName = spoutName;
			}
			
			target.setPlayerListName(sListName);
		}
		
		sendMessage(DNCStrings.INFO_NICK_TARGET, target, users,
				MessageType.CONFIRMATION);
		
		if (caller != null && !caller.equals(target))
		{
			sendMessage(DNCStrings.INFO_NICK_CALLER, caller, users,
					MessageType.CONFIRMATION);
		}
		
		if (plugin.isSpoutEnabled() == true)
		{
			SpoutPlayer spoutTarget = (SpoutPlayer) target;
			
			if (spoutTarget.isSpoutCraftEnabled())
			{
				spoutTarget.sendNotification(
						locale.getString(DNCStrings.INFO_SPOUT_TARGET),
						spoutName, Material.DIAMOND);
			}
			
			if (caller != null && !caller.equals(target))
			{
				SpoutPlayer spoutCaller = (SpoutPlayer) caller;
				
				if (spoutCaller.isSpoutCraftEnabled())
				{
					formatter.applyPattern(locale
							.getString(DNCStrings.INFO_SPOUT_CALLER));
					
					spoutCaller.sendNotification(formatter.format(users),
							spoutName, Material.DIAMOND);
				}
			}
			
			spoutTarget.setTitle(spoutName);
		}
		
		if (plugin.useGlobalAnnounce())
		{
			Player[] exclude;
			
			// Exclude the people we've sent a message to already.
			if (caller != null)
			{
				exclude = new Player[2];
				
				exclude[0] = caller;
				
				exclude[1] = target;
			}
			else
			{
				exclude = new Player[1];
				
				exclude[0] = target;
			}
			
			Player[] targets = getAnnounceTargets(exclude);
			
			if (targets.length > 0)
			{
				for (Player p : targets)
				{
					if (plugin.isBroadcastAll())
					{
						sendMessage(DNCStrings.INFO_NICK_CALLER, p, users,
								MessageType.INFO);
					}
					else
					{
						if (canBroadcast(p))
						{
							sendMessage(DNCStrings.INFO_NICK_CALLER, p, users,
									MessageType.INFO);
						}
					}
				}
			}
		}
		
		if (plugin.isSaveOnRename())
		{
			storeNick(target);
		}
	}
	
	/**
	 * Checks to see if a nick has colors in it.
	 * 
	 * @param nick
	 *            The nick to check for colors.
	 * 
	 * @return True if colors present, false otherwise.
	 */
	public boolean checkForColors(String nick)
	{
		for (ChatColor c : ChatColor.values())
		{
			if (nick.contains(c.toString()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks for illegal characters in the name (characters being used
	 * without permission).
	 * 
	 * @param target
	 *            Player who called the name change.
	 * 
	 * @param input
	 *            the name to check.
	 * 
	 * @return One of the Illegal Chat Colors, null otherwise.
	 */
	public ChatColor checkForIllegalColors(Player target, String input)
	{
		String sName = parseColors(input);
		
		ChatColor[] colors = getColorsUsed(sName);
		
		if (colors.length == 0)
		{
			return null;
		}
		
		boolean bBadColor = false;
		
		for (ChatColor c : colors)
		{
			switch (c)
			{
			case AQUA:
				bBadColor = !canUseColorAqua(target);
				break;
			case BLACK:
				bBadColor = !canUseColorBlack(target);
				break;
			case BLUE:
				bBadColor = !canUseColorIndigo(target);
				break;
			case BOLD:
				bBadColor = !canUseStyleBold(target);
				break;
			case DARK_AQUA:
				bBadColor = !canUseColorDarkAqua(target);
				break;
			case DARK_BLUE:
				bBadColor = !canUseColorDarkBlue(target);
				break;
			case DARK_GRAY:
				bBadColor = !canUseColorDarkGrey(target);
				break;
			case DARK_GREEN:
				bBadColor = !canUseColorDarkGreen(target);
				break;
			case DARK_PURPLE:
				bBadColor = !canUseColorPurple(target);
				break;
			case DARK_RED:
				bBadColor = !canUseColorDarkRed(target);
				break;
			case GOLD:
				bBadColor = !canUseColorOrange(target);
				break;
			case GRAY:
				bBadColor = !canUseColorGrey(target);
				break;
			case GREEN:
				bBadColor = !canUseColorDarkGreen(target);
				break;
			case ITALIC:
				bBadColor = !canUseStyleItalic(target);
				break;
			case LIGHT_PURPLE:
				bBadColor = !canUseColorPink(target);
				break;
			case MAGIC:
				bBadColor = !canUseStyleMagic(target);
				break;
			case RED:
				bBadColor = !canUseColorRed(target);
				break;
			case RESET:
				break;
			case STRIKETHROUGH:
				bBadColor = !canUseStyleStrikethrough(target);
				break;
			case UNDERLINE:
				bBadColor = !canUseStyleUnderline(target);
				break;
			case WHITE:
				bBadColor = !canUseColorWhite(target);
				break;
			case YELLOW:
				bBadColor = !canUseColorYellow(target);
				break;
			}
			
			if (bBadColor)
			{
				return c;
			}
			
		}
		
		return null;
	}
	
	/**
	 * Checks for a valid player based upon a given name. May return 0 to N
	 * players currently in the server at the time of the check, where N =
	 * MAX_PLAYERS. Names are cross checked. against both login and
	 * DisplayNames.
	 * 
	 * @param name
	 *            the name to check for.
	 * 
	 * @return an array of players matching the name of 0 to N in size.
	 */
	public Player[] checkName(String name)
	{
		if (name == null)
		{
			throw new IllegalArgumentException("Name can not be null.");
		}
		
		Player[] players = Bukkit.getServer().getOnlinePlayers();
		
		ArrayList<Player> alPlayers = new ArrayList<Player>();
		
		String sName;
		
		String sDisplayName;
		
		String sTarget = name;
		
		if (!plugin.useScoreboard())
		{
			sTarget = sTarget.toLowerCase();
		}
		
		for (Player p : players)
		{
			sName = p.getName();
			
			sDisplayName = p.getDisplayName();
			
			sDisplayName = ChatColor.stripColor(sDisplayName);
			
			sDisplayName = stripPrefix(sDisplayName);
			
			if (!plugin.useScoreboard())
			{
				sName = sName.toLowerCase();
				
				sDisplayName = sDisplayName.toLowerCase();
			}
			
			if (sTarget.equals(sDisplayName))
			{
				alPlayers.add(p);
			}
			else if (sTarget.equals(sName))
			{
				alPlayers.add(p);
			}
		}
		
		alPlayers.trimToSize();
		
		if (alPlayers.size() == 0)
		{
			return new Player[0];
		}
		
		Player[] p = new Player[alPlayers.size()];
		
		alPlayers.toArray(p);
		
		return p;
	}
	
	/**
	 * Ensure that a name is unique to the current player list. This
	 * includes color codes & the prefix if enabled.
	 * 
	 * @param name
	 *            The name to check for uniqueness.
	 * 
	 * @return true if unique, false otherwise.
	 */
	public boolean checkUnique(String name)
	{
		Player[] players = plugin.getServer().getOnlinePlayers();
		
		String sDisplayName = parseColors(name);
		
		sDisplayName = prefixNick(sDisplayName);
		
		for (Player p : players)
		{
			if (sDisplayName.equals(p.getDisplayName()))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks for color codes used in a string.
	 * 
	 * @param input
	 *            The string to check
	 * 
	 * @return an array of color codes found. Only returns one of each
	 *         color code.
	 */
	public ChatColor[] getColorsUsed(String input)
	{
		ArrayList<ChatColor> al = new ArrayList<ChatColor>();
		ChatColor[] colors;
		
		for (ChatColor c : ChatColor.values())
		{
			if (input.contains(c.toString()))
			{
				al.add(c);
			}
		}
		
		al.trimToSize();
		
		if (al.size() == 0)
		{
			return new ChatColor[0];
		}
		else
		{
			colors = new ChatColor[al.size()];
			
			al.toArray(colors);
			
			return colors;
		}
	}
	
	/**
	 * Takes an array of players to match against, and returns all players
	 * not in the given array.
	 * 
	 * @param exclude
	 *            Array of players to exclude from resulting array.
	 * 
	 * @return An array of players that excludes the given array.
	 */
	public Player[] getAnnounceTargets(Player[] exclude)
	{
		if (exclude == null || exclude.length == 0)
		{
			throw new IllegalArgumentException(
					"Exclude can not be null or of length 0.");
		}
		
		ArrayList<Player> targets = new ArrayList<Player>();
		
		Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		
		boolean bFound = false;
		
		for (Player online : onlinePlayers)
		{
			
			for (Player excluded : exclude)
			{
				if (excluded.getName().equals(online.getName()))
				{
					bFound = true;
					
					break;
				}
			}
			
			if (!bFound)
			{
				targets.add(online);
				
				bFound = false;
			}
		}
		
		targets.trimToSize();
		
		if (targets.size() > 0)
		{
			Player[] result = new Player[targets.size()];
			
			targets.toArray(result);
			
			return result;
		}
		else
		{
			return new Player[0];
		}
	}
	
	/**
	 * Checks to see if the Given CommandSender is an instance of either
	 * {@link ConsoleCommandSender} or {@link RemoteConsoleCommandSender}.
	 * 
	 * @param sender
	 *            The sender to check.
	 * 
	 * @return True if Console. False otherwise.
	 */
	public boolean isConsole(CommandSender sender)
	{
		if (sender instanceof ConsoleCommandSender)
		{
			return true;
		}
		else if (sender instanceof RemoteConsoleCommandSender)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check to see if the last color used in a string is the Reset Color.
	 * 
	 * @param input
	 *            The string to check.
	 * 
	 * @return True if reset is last, false if it isn't in the string or
	 *         isn't the last used.
	 */
	public boolean isLastColorReset(String input)
	{
		if (!input.contains(ChatColor.RESET.toString()))
		{
			return false;
		}
		
		ChatColor ccColor = lastColorUsed(input);
		
		if (ccColor != null && ccColor == ChatColor.RESET)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns the last color used in a string.
	 * 
	 * @param nick
	 *            The nick to check
	 * 
	 * @return The ChatColor, or null if no colors used.
	 */
	public ChatColor lastColorUsed(String input)
	{
		if (!checkForColors(input))
		{
			return null;
		}
		
		String[] saNick = input.split(String.valueOf(ChatColor.COLOR_CHAR));
		
		if (saNick.length > 1)
		{
			return ChatColor.getByChar(saNick[saNick.length - 1]);
		}
		else
		{
			return ChatColor.getByChar(saNick[1]);
		}
	}
	
	/**
	 * Parses a given set of arguments returning a list where each element
	 * is one word (separated by spaces) or a phrase (surrounded by
	 * quotes).
	 * 
	 * Credit:
	 * http://stackoverflow.com/questions/366202/regex-for-splitting
	 * -a-string-using-space-when-not-surrounded-by-single-or-double
	 * 
	 * @param input
	 *            The string to split.
	 * 
	 * @return An array with each word/phrase as 1 element.
	 */
	public String[] parseArgumentsAll(String input)
	{
		ArrayList<String> matchList = new ArrayList<String>();
		
		Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
		
		Matcher regexMatcher = regex.matcher(input);
		
		while (regexMatcher.find())
		{
			if (regexMatcher.group(1) != null)
			{
				// Add double-quoted string without the quotes
				matchList.add(regexMatcher.group(1));
			}
			else if (regexMatcher.group(2) != null)
			{
				// Add single-quoted string without the quotes
				matchList.add(regexMatcher.group(2));
			}
			else
			{
				// Add unquoted word
				matchList.add(regexMatcher.group());
			}
		}
		
		matchList.trimToSize();
		
		String[] output = new String[matchList.size()];
		
		matchList.toArray(output);
		
		return output;
	}
	
	/**
	 * Parses the argument string, returning 1 or 2 arguments matching
	 * <target> <name>.
	 * 
	 * @param args
	 *            the arguments string from the command.
	 * 
	 * @return A String array containing the arguments <code>target</code>
	 *         and/or <code>name</code> in that order.
	 */
	public String[] parseArguments(String[] args)
	{
		String sTarget, sName = "";
		
		if (args.length == 0)
		{
			return new String[0];
		}
		
		// Only one argument.
		else if (args.length == 1)
		{
			sName = args[0].replace("\"", "");
			
			String[] sa =
			{ sName };
			
			return sa;
		}
		// Check to see if target/name given.
		else if (args.length == 2)
		{
			// Check to see if Name with Space
			if (args[0].startsWith("\""))
			{
				if (!args[0].endsWith("\""))
				{
					sTarget = args[0] + " " + args[1];
					
					sTarget = sTarget.replace("\"", "");
					
					String[] sa =
					{ sTarget };
					
					return sa;
				}
				else
				{
					sTarget = args[0].replace("\"", "");
					
					sName = args[1].replace("\"", "");
					
					String[] sa =
					{ sTarget, sName };
					
					return sa;
				}
			}
			// <target> + <name> given.
			else
			{
				String[] sa =
				{ args[0], args[1] };
				
				return sa;
			}
		}
		// Arguments with spaces given.
		else
		{
			// Check to see if first argument has space.
			if (args[0].startsWith("\""))
			{
				sTarget = "";
				
				int iLoop1;
				
				// Look for the end quote for the first argument.
				for (iLoop1 = 0; iLoop1 < args.length; iLoop1++)
				{
					sTarget += args[iLoop1];
					
					if (args[iLoop1].endsWith("\""))
					{
						break;
					}
					
					sTarget += " ";
				}
				
				sTarget = sTarget.replace("\"", "");
				
				iLoop1++;
				
				// Check to see if really only one argument.
				if (iLoop1 >= args.length)
				{
					
					String[] sa =
					{ sTarget };
					
					return sa;
				}
				
				// Parse out second argument.
				for (int iLoop2 = iLoop1; iLoop2 < args.length; iLoop2++)
				{
					
					sName += args[iLoop2] + " ";
				}
				
				int iIndex = sName.lastIndexOf(" ");
				
				if (iIndex > -1)
				{
					sName = sName.substring(0, iIndex);
				}
				
				sName = sName.replace("\"", "");
				
				String[] sa =
				{ sTarget, sName };
				
				return sa;
			}
			// First arg doesn't have space.
			else
			{
				sTarget = args[0].replace("\"", "");
				
				if (args[1].startsWith("\""))
				{
					int iLoop1 = 0;
					
					for (iLoop1 = 1; iLoop1 < args.length; iLoop1++)
					{
						sName += args[iLoop1];
						
						if (args[iLoop1].endsWith("\""))
						{
							break;
						}
						
						sName += " ";
					}
					
					if (iLoop1 < (args.length - 1))
					{
						return new String[0];
					}
					
					sName = sName.replace("\"", "");
					
					String[] sa =
					{ sTarget, sName };
					
					return sa;
				}
				else
				{
					return new String[0];
				}
			}
		}
	}
	
	/**
	 * Parses all manually added color codes and changes them to actual
	 * codes. This also appends the reset code (<code>&r</code>) to the
	 * name.
	 * 
	 * @param name
	 *            the name to parse colors for.
	 * 
	 * @return the resultant string, with color codes converted to proper
	 *         format.
	 */
	public String parseColors(String name)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(name.replaceAll("(&([0-9a-fklmnorA-FKLMNOR]))", "§$2"));
		
		String sName = sb.toString();
		
		if (!checkForColors(sName))
		{
			return sb.toString();
		}
		
		if (isLastColorReset(sName))
		{
			return sb.toString();
		}
		
		sb.append(ChatColor.RESET);
		
		return sb.toString();
	}
	
	/**
	 * Prefixes a nick with the prefix. Prepends prefix based upon
	 * settings.
	 * 
	 * @param nick
	 *            The nick to format with the Prefix.
	 * 
	 * @return the formated string.
	 */
	public String prefixNick(String nick)
	{
		if (nick == null)
		{
			throw new NullPointerException();
		}
		
		String sNick = null;
		
		if (plugin.usePrefix() && plugin.usePrefixColor())
		{
			if (nick.startsWith(plugin.getPrefixFull()))
			{
				return nick;
			}
			else if (nick.startsWith(plugin.getPrefixShort()))
			{
				return nick;
			}
			
			if (startsWithColor(nick))
			{
				sNick = plugin.getPrefixShort() + nick;
			}
			else
			{
				sNick = plugin.getPrefixFull() + nick;
			}
		}
		else if (plugin.usePrefix() && !plugin.usePrefixColor())
		{
			String prefix = Character.toString(plugin.getPrefix());
			
			if (nick.startsWith(prefix))
			{
				return nick;
			}
			
			sNick = prefix + nick;
		}
		else
		{
			sNick = nick;
		}
		
		return sNick;
	}
	
	/**
	 * Restores a display name for a player from the database.
	 * 
	 * @param player
	 *            the player to restore the name from.
	 */
	public void restoreNick(Player player)
	{
		String sName = player.getName();
		
		String sDName = player.getDisplayName();
		
		DP pClass = (DP) plugin.getDatabase().find(DP.class).where()
				.ieq("PlayerName", sName).findUnique();
		
		if (pClass == null)
		{
			pClass = new DP();
			
			pClass.setPlayerName(sName);
			
			pClass.setDisplayName(sDName);
		}
		
		sDName = pClass.getDisplayName();
		
		sDName = parseColors(sDName);
		
		if (!sName.equals(sDName))
		{
			sDName = prefixNick(sDName);
		}
		
		if (plugin.useScoreboard())
		{
			Player[] players = Bukkit.getServer().getOnlinePlayers();
			
			for (Player p : players)
			{
				if (player.equals(p))
				{
					continue;
				}
				
				if (p.getDisplayName().equals(sDName))
				{
					sDName = pClass.getPlayerName();
					
					pClass.setDisplayName(sDName);
					
					player.sendMessage(ChatColor.RED
							+ DNCStrings.dnc_short
							+ plugin.getLocalization().getString(
									DNCStrings.INFO_NICK_CONFLICT));
					
					break;
				}
			}
			
			String sListName;
			
			if (sDName.length() > 16)
			{
				sListName = sDName.substring(0, 16);
			}
			else
			{
				sListName = sDName;
			}
			
			player.setPlayerListName(sListName);
		}
		
		player.setDisplayName(sDName);
		
		if (plugin.isSpoutEnabled())
		{
			SpoutPlayer spoutPlayer = (SpoutPlayer) player;
			
			spoutPlayer.setTitle(sDName);
		}
	}
	
	/**
	 * Sends a message to the CommandSender specified, pulled from the
	 * Localization Strings.
	 * 
	 * @param message
	 *            The message to send.
	 * 
	 * @param target
	 *            The target of the message.
	 * 
	 * @param args
	 *            Any arguments that the formatter will need to embed in
	 *            the string.
	 * 
	 * @param type
	 *            The type of message to send, specifically how it will be
	 *            colored.
	 */
	public void sendMessage(DNCStrings message, CommandSender target,
			Object[] args, MessageType type)
	{
		StringBuilder sb = new StringBuilder();
		
		// Colorize the String using the Plugin and Message Colors
		sb.append(ChatColor.GOLD).append(DNCStrings.dnc_short)
				.append(type.getColor());
		
		// Add the message
		if (args != null)
		{
			formatter.applyPattern(locale.getString(message));
			
			sb.append(formatter.format(args));
		}
		else
		{
			sb.append(locale.getString(message));
		}
		
		// Ensure that the entire string is colored correctly.
		if (isLastColorReset(sb.toString()))
		{
			String input = sb.toString();
			
			int iIndex = input.lastIndexOf(ChatColor.RESET.toString());
			
			int iCombinedIndex = iIndex + ChatColor.RESET.toString().length();
			
			if (input.length() > iCombinedIndex)
			{
				String s1 = input.substring(0, iCombinedIndex);
				
				String s2 = input.substring(iCombinedIndex, input.length());
				
				sb = new StringBuilder();
				
				sb.append(s1).append(type.getColor()).append(s2);
			}
		}
		
		// Send the actual message to the target.
		target.sendMessage(sb.toString());
	}
	
	/**
	 * Checks for the ChatColor codes for Bold, Italic, Strikethrough, or
	 * Underline. Note, this does not include the reset code.
	 * 
	 * @param input
	 *            string to check for color codes.
	 * 
	 * @return true if one of the special color codes is found, false
	 *         otherwise.
	 */
	public boolean specialChatColor(String input)
	{
		if (input.contains(ChatColor.BOLD.toString())
				|| input.contains(ChatColor.ITALIC.toString())
				|| input.contains(ChatColor.STRIKETHROUGH.toString())
				|| input.contains(ChatColor.UNDERLINE.toString()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Checks to see if a string starts with a color.
	 * 
	 * @param input
	 *            the string to test.
	 * 
	 * @return true if it starts with a color. False otherwise.
	 */
	public boolean startsWithColor(String input)
	{
		for (ChatColor c : ChatColor.values())
		{
			if (input.startsWith(c.toString()))
			{
				if (c.isColor())
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Stores the player's display name to the database if it is different
	 * from the login name. Strips out all database information when a nick
	 * is reset.
	 * 
	 * @param player
	 *            The player to store the display name for.
	 */
	public void storeNick(Player player)
	{
		String sName = player.getName();
		
		String sDName = player.getDisplayName();
		
		sDName = stripPrefix(sDName);
		
		DP pClass = (DP) plugin.getDatabase().find(DP.class).where()
				.ieq("PlayerName", sName).findUnique();
		
		if (sName.equals(sDName))
		{
			
			if (pClass != null)
			{
				plugin.getDatabase().delete(pClass);
			}
			
			return;
		}
		
		if (pClass == null)
		{
			pClass = new DP();
			
			pClass.setPlayerName(sName);
			
			pClass.setDisplayName(sDName);
		}
		else
		{
			pClass.setDisplayName(sDName);
		}
		
		plugin.getDatabase().save(pClass);
	}
	
	/**
	 * Strips the prefix from a name. Can be called with prefix option
	 * disabled.
	 * 
	 * @param nick
	 *            the nick to strip.
	 * 
	 * @return the name without the prefix.
	 */
	public String stripPrefix(String nick)
	{
		String sNick = nick;
		
		String prefix = Character.toString(plugin.getPrefix());
		
		if (plugin.usePrefix() && plugin.usePrefixColor())
		{
			if (sNick.startsWith(plugin.getPrefixFull()))
			{
				sNick = sNick.substring(plugin.getPrefixFull().length());
			}
			else if (sNick.startsWith(plugin.getPrefixColor()
					+ Character.toString(plugin.getPrefix())))
			{
				sNick = sNick.substring((plugin.getPrefixColor() + Character
						.toString(plugin.getPrefix())).length());
			}
			else
			{
				if (sNick.contains(prefix))
				{
					String[] split = Pattern.compile(Pattern.quote(prefix))
							.split(sNick, 2);
					
					sNick = split[0] + split[1];
				}
			}
		}
		else if (plugin.usePrefix() && !plugin.usePrefixColor())
		{
			
			if (sNick.contains(prefix))
			{
				String[] split = Pattern.compile(Pattern.quote(prefix)).split(
						sNick, 2);
				
				sNick = split[0] + split[1];
			}
		}
		
		return sNick;
	}
	
	/**
	 * Retrieves an instance of DispNameAPI if it already exists, or
	 * creates a new one if it doesn't.
	 * 
	 * @return The current DispNameAPI instance, or a new one if it hasn't
	 *         been called yet.
	 */
	public static DispNameAPI getInstance()
	{
		if (instance != null)
		{
			return instance;
		}
		else
		{
			return new DispNameAPI();
		}
	}
}
