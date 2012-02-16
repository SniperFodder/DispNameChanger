package me.captain.dnc;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 * Command executor for the DispNameChanger Plugin.
 * 
 * Valid commands:aliases that this executor handles are
 * <code>'rename':'newname'</code>, <code>'reset':'resetname':</code>, and
 * <code>'check':'checkname':'namecheck'</code>.
 * 
 * @author captainawesome7, itsatacoshop247, Daxiongmao87, Luke Zwekii,
 *         Sammy, SniperFodder
 * 
 */
public class DispNameCE implements CommandExecutor
{
	private DispNameChanger plugin;
	
	private MessageFormat formatter;
	
	/**
	 * Constructs a new DispNameCE.
	 * 
	 * @param plugin
	 *            The plugin using this Command Executor.
	 */
	public DispNameCE(DispNameChanger plugin)
	{
		super();
		
		this.plugin = plugin;
		
		formatter = new MessageFormat("");
		
		formatter.setLocale(plugin.getLocale());
	}
	
	/**
	 * Checks to see if the Player can use the command.
	 * 
	 * @param p
	 *            The player to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseCheckName(Player p)
	{
		if (p == null)
		{
			return true;
		}
		
		return p.hasPermission("dispname.check");
	}
	
	/**
	 * Checks to see if the Player can use the command.
	 * 
	 * @param p
	 *            The player to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseChangeName(Player p)
	{
		if (p == null)
		{
			return true;
		}
		
		return p.hasPermission("dispname.change");
	}
	
	/**
	 * Checks to see if the Player can use the command.
	 * 
	 * @param p
	 *            The player to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseChangeNameSpace(Player p)
	{
		if (p == null)
		{
			return true;
		}
		
		return p.hasPermission("dispname.changespace");
	}
	
	/**
	 * Checks to see if the Player can use the command.
	 * 
	 * @param p
	 *            The player to check the permission for.
	 * 
	 * @return true if the player can use the command, false otherwise.
	 */
	public boolean canUseChangeNameOther(Player p)
	{
		if (p == null)
		{
			return true;
		}
		
		return p.hasPermission("dispname.changeother");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args)
	{
		Player changer = null;
		
		if (sender instanceof Player)
		{
			changer = (Player) sender;
		}
		
		// Check for the rename command.
		if (commandLabel.equalsIgnoreCase("rename")
				|| commandLabel.equalsIgnoreCase("newname"))
		{
			// parse Arguments into <target> <Name>
			String[] saArgs = parseArguments(args);
			
			// Ensure that the user can run the command.
			if (canUseChangeName(changer))
			{
				// Changing own name.
				if (args.length == 1)
				{
					// Ensure we aren't console.
					if (changer == null)
					{
						sender.sendMessage(plugin.dnc_short
								+ plugin.error_console);
						
						return false;
					}
				}
				
				// Error parsing args
				if (saArgs == null)
				{
					return false;
				}
				
				switch (saArgs.length)
				{
				
				/*
				 * If Args returned is of size 1, changing own name.
				 */
				case 1:
					if (plugin.useScoreboard())
					{
						// Ensure unique
						if (!checkUnique(stripPrefix(saArgs[0])))
						{
							changer.sendMessage(ChatColor.RED
									+ plugin.dnc_short
									+ plugin.error_non_unique);
							
							return true;
						}
						
						// Ensure that smaller than 16 characters
						if (saArgs[0].length() > 16)
						{
							changer.sendMessage(ChatColor.RED
									+ plugin.dnc_short
									+ plugin.error_max_length);
							
							return true;
						}
					}
					
					changeDisplayName(changer, saArgs[0]);
					
					return true;
					
					/*
					 * If Args returned is of size 2, Changing someone
					 * else's name.
					 */
				case 2:
					// Ensure we can change other names.
					if (canUseChangeNameOther(changer))
					{
						// Pull the target we are changing.
						Player[] players = checkName(saArgs[0]);
						
						if (players == null)
						{
							if (changer != null)
							{
								changer.sendMessage(ChatColor.RED
										+ plugin.dnc_short
										+ plugin.error_bad_user);
							}
							else
							{
								sender.sendMessage(plugin.dnc_short
										+ plugin.error_bad_user);
							}
							
							return true;
						}
						
						// Ensure there is only one target.
						if (players.length == 1)
						{
							if (saArgs[1].contains(" "))
							{
								// Ensure we can change spaces.
								if (canUseChangeNameSpace(changer))
								{
									if (plugin.useScoreboard())
									{
										if (!checkUnique(saArgs[1]))
										{
											changer.sendMessage(ChatColor.RED
													+ plugin.dnc_short
													+ plugin.error_non_unique);
											
											return true;
										}
										
										if (saArgs[1].length() > 16)
										{
											changer.sendMessage(ChatColor.RED
													+ plugin.dnc_short
													+ plugin.error_max_length);
											
											return true;
										}
									}
									// Change the targets name.
									changeDisplayName(changer, players[0],
											saArgs[0], saArgs[1]);
									
									return true;
								}
								else
								{
									changer.sendMessage(ChatColor.RED
											+ plugin.dnc_short
											+ plugin.permission_spaces);
									
									return true;
								}
							}
							else
							{
								if (changer != null
										&& changer.equals(players[0]))
								{
									if (plugin.useScoreboard())
									{
										if (!checkUnique(saArgs[1]))
										{
											changer.sendMessage(ChatColor.RED
													+ plugin.dnc_short
													+ plugin.error_non_unique);
											
											return true;
										}
										
										if (saArgs[1].length() > 16)
										{
											changer.sendMessage(ChatColor.RED
													+ plugin.dnc_short
													+ plugin.error_max_length);
											
											return true;
										}
									}
									
									changeDisplayName(changer, saArgs[1]);
								}
								else
								{
									if (plugin.useScoreboard())
									{
										if (!checkUnique(saArgs[1]))
										{
											changer.sendMessage(ChatColor.RED
													+ plugin.dnc_short
													+ plugin.error_non_unique);
											
											return true;
										}
										
										if (saArgs[1].length() > 16)
										{
											changer.sendMessage(ChatColor.RED
													+ plugin.dnc_short
													+ plugin.error_max_length);
											
											return true;
										}
									}
									// Change the targets name.
									changeDisplayName(changer, players[0],
											saArgs[0], saArgs[1]);
								}
								
								return true;
							}
							
						}
						else
						{
							if (changer != null)
							{
								changer.sendMessage(ChatColor.RED
										+ plugin.dnc_short
										+ plugin.error_multi_match);
							}
							else
							{
								sender.sendMessage(plugin.dnc_short
										+ plugin.error_multi_match);
							}
							return true;
						}
					}
					else
					{
						changer.sendMessage(ChatColor.RED + plugin.dnc_short
								+ plugin.permission_other);
						
						return true;
					}
				}
			}
			else
			{
				changer.sendMessage(ChatColor.RED + plugin.dnc_short
						+ plugin.permission_use);
				
				return true;
			}
		}
		// Reset name command.
		else if (commandLabel.equalsIgnoreCase("resetname")
				|| commandLabel.equalsIgnoreCase("reset"))
		{
			if (args.length == 0)
			{
				if (changer == null)
				{
					sender.sendMessage(plugin.dnc_short
							+ plugin.error_console_rename);
					
					return false;
				}
				else
				{
					if (canUseChangeName(changer))
					{
						changeDisplayName(changer, changer.getName());
						
						return true;
					}
					else
					{
						changer.sendMessage(ChatColor.RED + plugin.dnc_short
								+ plugin.permission_use);
						
						return true;
					}
				}
			}
			else
			{
				if (canUseChangeName(changer)
						&& canUseChangeNameOther(changer))
				{
					String[] saArgs = parseArguments(args);
					
					if (saArgs == null)
					{
						if (changer != null)
						{
							changer.sendMessage(ChatColor.RED
									+ plugin.dnc_short + plugin.error_bad_user);
						}
						else
						{
							sender.sendMessage(plugin.dnc_short
									+ plugin.error_bad_user);
						}
						
						return false;
					}
					if (saArgs.length > 1)
					{
						if (changer != null)
						{
							changer.sendMessage(ChatColor.RED
									+ plugin.dnc_short + plugin.error_bad_args);
						}
						else
						{
							sender.sendMessage(plugin.dnc_short
									+ plugin.error_bad_args);
						}
						
						return false;
					}
					else if (saArgs.length == 1)
					{
						Player[] players = checkName(saArgs[0]);
						
						if (players == null)
						{
							sender.sendMessage(ChatColor.RED
									+ plugin.dnc_short + plugin.error_bad_user);
							
							return true;
						}
						
						if (players.length > 1)
						{
							if (changer != null)
							{
								changer.sendMessage(ChatColor.RED
										+ plugin.dnc_short
										+ plugin.error_multi_match);
							}
							else
							{
								sender.sendMessage(plugin.dnc_short
										+ plugin.error_multi_match);
							}
							
							return true;
						}
						else
						{
							changeDisplayName(changer, players[0], saArgs[0],
									players[0].getName());
							
							return true;
						}
					}
				}
				else
				{
					changer.sendMessage(ChatColor.RED + plugin.dnc_short
							+ plugin.permission_other);
					
					return true;
				}
			}
		}
		// Check name command.
		else if (commandLabel.equalsIgnoreCase("checkname")
				|| commandLabel.equalsIgnoreCase("check")
				|| commandLabel.equalsIgnoreCase("namecheck"))
		{
			if (canUseCheckName(changer))
			{
				String[] saArgs = parseArguments(args);
				
				if (saArgs == null)
				{
					return false;
				}
				if (saArgs.length == 1)
				{
					Player[] players = checkName(saArgs[0]);
					
					if (players == null)
					{
						sender.sendMessage(ChatColor.RED + plugin.dnc_short
								+ plugin.error_bad_user);
						
						return true;
					}
					else if (players.length == 1)
					{
						String sName = players[0].getName();
						
						String sDisplay = players[0].getDisplayName();
						
						Object[] users = new Object[2];
						
						formatter.applyPattern(plugin.info_check_single);
						
						StringBuilder sb = new StringBuilder();
						
						if (changer != null)
						{
							sb.append(ChatColor.GREEN);
						}
						
						sb.append(plugin.dnc_short);
						
						if (sDisplay.equals(saArgs[0]))
						{
							users[0] = sDisplay;
							
							users[1] = sName;
							
							sb.append(formatter.format(users));
						}
						else
						{
							users[0] = sName;
							
							users[1] = sDisplay;
							
							sb.append(formatter.format(users));
						}
						
						if (changer != null)
						{
							changer.sendMessage(sb.toString());
						}
						else
						{
							sender.sendMessage(sb.toString());
						}
						
						return true;
					}
					else
					{
						if (changer != null)
						{
							changer.sendMessage(ChatColor.GREEN
									+ plugin.dnc_short
									+ plugin.info_check_multi);
						}
						else
						{
							sender.sendMessage(plugin.dnc_short
									+ plugin.info_check_multi);
						}
						
						Object[] users = new Object[2];
						
						formatter.applyPattern(plugin.info_check_multi_list);
						
						StringBuilder sb = new StringBuilder();
						
						if (changer != null)
						{
							sb.append(ChatColor.GREEN);
						}
						
						sb.append(plugin.dnc_short);
						
						users[0] = saArgs[0];
						
						String sUsers = "";
						
						for (Player p : players)
						{
							sUsers += p.getName() + ", ";
						}
						
						sUsers = sUsers.substring(0, sUsers.lastIndexOf(','));
						
						users[1] = sUsers;
						
						sb.append(formatter.format(users));
						
						if (changer != null)
						{
							changer.sendMessage(sb.toString());
						}
						else
						{
							sender.sendMessage(sb.toString());
						}
					}
				}
				else
				{
					if (changer != null)
					{
						changer.sendMessage(ChatColor.RED + plugin.dnc_short
								+ plugin.error_bad_args);
					}
					else
					{
						sender.sendMessage(plugin.dnc_short
								+ plugin.error_bad_args);
					}
					
					return false;
				}
				
			}
			else
			{
				changer.sendMessage(ChatColor.RED + plugin.dnc_short
						+ plugin.permission_check);
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Changes the display name of the target.
	 * 
	 * @param target the player to change.
	 * 
	 * @param newName the new name to use.
	 */
	private void changeDisplayName(Player target, String newName)
	{
		changeDisplayName(null, target, null, newName);
	}
	
	/**
	 * Changes the display name of the target and sends notifications.
	 * 
	 * @param caller The person trying to make the change.
	 * 
	 * @param target The intended target of the change.
	 * 
	 * @param oldName The old name of the target.
	 * 
	 * @param newName The new name to use.
	 */
	private void changeDisplayName(Player caller, Player target,
			String oldName, String newName)
	{
		Object[] users =
		{ oldName, newName };
		
		// Remove all the colors
		String spoutName = ChatColor.stripColor(newName);
		
		// Attatch the prefix if needed.
		spoutName = prefixNick(spoutName);
		
		
		target.setDisplayName(spoutName);
		
		if (plugin.useScoreboard())
		{
			target.setPlayerListName(spoutName);
		}
		
		formatter.applyPattern(plugin.info_nick_target);
		
		StringBuilder sbTarget = new StringBuilder();
		
		StringBuilder sbCaller;
		
		sbTarget.append(ChatColor.GREEN).append(plugin.dnc_short)
				.append(formatter.format(users));
		
		target.sendMessage(sbTarget.toString());
		
		if (caller != null && !caller.equals(target))
		{
			sbCaller = new StringBuilder();
			
			formatter.applyPattern(plugin.info_nick_caller);
			
			sbCaller.append(ChatColor.GREEN).append(plugin.dnc_short)
					.append(formatter.format(users));
			
			caller.sendMessage(sbCaller.toString());
		}
		
		if (plugin.isSpoutEnabled() == true)
		{
			SpoutPlayer spoutTarget = (SpoutPlayer) target;
			
			if (spoutTarget.isSpoutCraftEnabled())
			{
				spoutTarget.sendNotification(plugin.info_spout_target,
						spoutName, Material.DIAMOND);
			}
			
			if (caller != null && !caller.equals(target))
			{
				SpoutPlayer spoutCaller = (SpoutPlayer) caller;
				
				if (spoutCaller.isSpoutCraftEnabled())
				{
					formatter.applyPattern(plugin.info_spout_caller);
					
					spoutCaller.sendNotification(formatter.format(users),
							spoutName, Material.DIAMOND);
				}
			}
			
			spoutTarget.setTitle(spoutName);
		}
		
		DP pClass = (DP) plugin.getDatabase().find(DP.class).where()
				.ieq("PlayerName", target.getName()).findUnique();
		
		if (pClass == null)
		{
			pClass = new DP();
		}
		
		pClass.setPlayerName(target.getName());
		
		pClass.setDisplayName(newName);
		
		plugin.getDatabase().save(pClass);
	}
	
	/**
	 * Checks for a valid player based upon a given name.
	 * 
	 * @param name the name to check for.
	 * 
	 * @return an array of players matching the name.
	 */
	private Player[] checkName(String name)
	{
		if (name == null)
		{
			throw new IllegalArgumentException("Name can not be null.");
		}
		
		Player[] players = plugin.getServer().getOnlinePlayers();
		
		ArrayList<Player> alPlayers = new ArrayList<Player>();
		
		String sName;
		
		String sDisplayName;
		
		String sTarget;
		
		if(!plugin.useScoreboard())
		{
			sTarget = name.toLowerCase();
		}
		else
		{
			sTarget = name;
		}
		 
		
		for (Player p : players)
		{
			sName = p.getName().toLowerCase();
			
			sDisplayName = p.getDisplayName();
			
			if(!plugin.useScoreboard())
			{
				sDisplayName = sDisplayName.toLowerCase();
			}
			
			sDisplayName = ChatColor.stripColor(sDisplayName);
			
			sDisplayName = stripPrefix(sDisplayName);
			
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
			return null;
		}
		
		Player[] p = new Player[alPlayers.size()];
		
		alPlayers.toArray(p);
		
		return p;
	}
	
	/**
	 * Prefixes a nick with the prefix and shortens it if it
	 * exceeds 16 characters.
	 * 
	 * @param nick The nick to format with the Prefix.
	 * 
	 * @return the formated string.
	 */
	private String prefixNick(String nick)
	{
		if (nick == null)
		{
			throw new NullPointerException();
		}
		
		String sNick = null;
		
		if (plugin.usePrefix())
		{
			sNick = plugin.getPrefix() + nick;
		}
		else
		{
			sNick = nick;
		}
		
		if (plugin.useScoreboard())
		{
			if (sNick.length() > 16)
			{
				sNick = sNick.substring(0, 16);
			}
		}
		
		return sNick;
	}
	
	private String stripPrefix(String nick)
	{
		String sNick = nick;
		
		if(plugin.usePrefix())
		{
			String prefix = Character.toString(plugin.getPrefix());
			
			if(sNick.contains(prefix))
			{
				String[] split = sNick.split(prefix, 2);
				
				sNick = split[0] + split[1];
			}
		}
		
		return sNick;
	}
	
	/**
	 * Parses the argument string, returning 1 or 2 arguments matching
	 * <target> <name>
	 * 
	 * @param args the arguments string from the command.
	 * 
	 * @return A String array containing the arguments <target> and/or
	 * <name> in that order.
	 */
	private String[] parseArguments(String[] args)
	{
		String sTarget, sName = "";
		
		if (args.length == 0)
		{
			return null;
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
						return null;
					}
					
					sName = sName.replace("\"", "");
					
					String[] sa =
					{ sTarget, sName };
					
					return sa;
				}
				else
				{
					return null;
				}
			}
		}
	}
	
	/**
	 * Ensure that a name is unique to the current player list.
	 * 
	 * @param name The name to check for uniqueness.
	 * 
	 * @return true if unique, false otherwise.
	 */
	private boolean checkUnique(String name)
	{
		Player[] players = plugin.getServer().getOnlinePlayers();
		
		String sDisplayName;
		
		for (Player p : players)
		{
			sDisplayName = ChatColor.stripColor(p.getDisplayName());
			
			sDisplayName = stripPrefix(sDisplayName);
			
			if (name.equals(sDisplayName))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/*
	 * Convert all the color tags and such to color codes.
	 * 
	 * @credit MiracleM4n http://mdev.in/
	 * 
	 * private String addColour(String string) { string =
	 * string.replace("`e", "") .replace("`r", "\u00A7c") .replace("`R",
	 * "\u00A74") .replace("`y", "\u00A7e") .replace("`Y", "\u00A76")
	 * .replace("`g", "\u00A7a") .replace("`G", "\u00A72") .replace("`a",
	 * "\u00A7b") .replace("`A", "\u00A73") .replace("`b", "\u00A79")
	 * .replace("`B", "\u00A71") .replace("`p", "\u00A7d") .replace("`P",
	 * "\u00A75") .replace("`k", "\u00A70") .replace("`s", "\u00A77")
	 * .replace("`S", "\u00A78") .replace("`w", "\u00A7f");
	 * 
	 * string = string.replace("<r>", "") .replace("<black>", "\u00A70")
	 * .replace("<navy>", "\u00A71") .replace("<green>", "\u00A72")
	 * .replace("<teal>", "\u00A73") .replace("<red>", "\u00A74")
	 * .replace("<purple>", "\u00A75") .replace("<gold>", "\u00A76")
	 * .replace("<silver>", "\u00A77") .replace("<gray>", "\u00A78")
	 * .replace("<blue>", "\u00A79") .replace("<lime>", "\u00A7a")
	 * .replace("<aqua>", "\u00A7b") .replace("<rose>", "\u00A7c")
	 * .replace("<pink>", "\u00A7d") .replace("<yellow>", "\u00A7e")
	 * .replace("<white>", "\u00A7f");
	 * 
	 * string = string.replaceAll("(§([a-fA-F0-9]))", "\u00A7$2");
	 * 
	 * string = string.replaceAll("(&([a-fA-F0-9]))", "\u00A7$2");
	 * 
	 * return string.replace("&&", "&"); }
	 */
	
	/*
	 * Remove all color codes.
	 * 
	 * @credit MiracleM4n http://mdev.in/
	 * 
	 * private String removeColour(String string) { addColour(string);
	 * 
	 * string = string.replaceAll("(§([a-fA-F0-9]))", "& $2");
	 * 
	 * string = string.replaceAll("(&([a-fA-F0-9]))", "& $2");
	 * 
	 * return string.replace("&&", "&"); }
	 */
}
