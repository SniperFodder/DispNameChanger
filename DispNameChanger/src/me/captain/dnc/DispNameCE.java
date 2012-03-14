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
 * <code>'rename':'newname'</code>, <code>'reset':'resetname'</code>, and
 * <code>'check':'checkname':'namecheck':'realname'</code>.
 * 
 * @author captainawesome7, itsatacoshop247, Daxiongmao87, Luke Zwekii,
 *         Sammy, SniperFodder
 * 
 */
public class DispNameCE implements CommandExecutor
{
	private DispNameChanger plugin;
	
	private MessageFormat formatter;
	
	private DNCLocalization locale;
	
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
		
		locale = plugin.getLocalization();
		
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
	
	public boolean canBroadcast(Player p)
	{
		if(p == null)
		{
			return true;
		}
		
		return p.hasPermission("dispname.announce");
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
		if (cmd.getName().equalsIgnoreCase(DNCCommands.RENAME.getName()))
		{
			// parse Arguments into <target> <Name>
			String[] saArgs = plugin.parseArguments(args);
			
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
								+ locale.error_console);
						
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
						if (!checkUnique(saArgs[0]))
						{
							changer.sendMessage(ChatColor.RED
									+ plugin.dnc_short
									+ locale.error_non_unique);
							
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
						Player[] players = plugin.checkName(saArgs[0]);
						
						if (players == null)
						{
							if (changer != null)
							{
								changer.sendMessage(ChatColor.RED
										+ plugin.dnc_short
										+ locale.error_bad_user);
							}
							else
							{
								sender.sendMessage(plugin.dnc_short
										+ locale.error_bad_user);
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
													+ locale.error_non_unique);
											
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
											+ locale.permission_spaces);
									
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
													+ locale.error_non_unique);
											
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
													+ locale.error_non_unique);
											
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
										+ locale.error_multi_match);
							}
							else
							{
								sender.sendMessage(plugin.dnc_short
										+ locale.error_multi_match);
							}
							return true;
						}
					}
					else
					{
						changer.sendMessage(ChatColor.RED + plugin.dnc_short
								+ locale.permission_other);
						
						return true;
					}
				}
			}
			else
			{
				changer.sendMessage(ChatColor.RED + plugin.dnc_short
						+ locale.permission_use);
				
				return true;
			}
		}
		// Reset name command.
		else if (cmd.getName().equalsIgnoreCase(DNCCommands.RESET.getName()))
		{
			if (args.length == 0)
			{
				if (changer == null)
				{
					sender.sendMessage(plugin.dnc_short
							+ locale.error_console_rename);
					
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
								+ locale.permission_use);
						
						return true;
					}
				}
			}
			else
			{
				if (canUseChangeName(changer)
						&& canUseChangeNameOther(changer))
				{
					String[] saArgs = plugin.parseArguments(args);
					
					if (saArgs == null)
					{
						if (changer != null)
						{
							changer.sendMessage(ChatColor.RED
									+ plugin.dnc_short + locale.error_bad_user);
						}
						else
						{
							sender.sendMessage(plugin.dnc_short
									+ locale.error_bad_user);
						}
						
						return false;
					}
					if (saArgs.length > 1)
					{
						if (changer != null)
						{
							changer.sendMessage(ChatColor.RED
									+ plugin.dnc_short + locale.error_bad_args);
						}
						else
						{
							sender.sendMessage(plugin.dnc_short
									+ locale.error_bad_args);
						}
						
						return false;
					}
					else if (saArgs.length == 1)
					{
						Player[] players = plugin.checkName(saArgs[0]);
						
						if (players == null)
						{
							sender.sendMessage(ChatColor.RED
									+ plugin.dnc_short + locale.error_bad_user);
							
							return true;
						}
						
						if (players.length > 1)
						{
							if (changer != null)
							{
								changer.sendMessage(ChatColor.RED
										+ plugin.dnc_short
										+ locale.error_multi_match);
							}
							else
							{
								sender.sendMessage(plugin.dnc_short
										+ locale.error_multi_match);
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
							+ locale.permission_other);
					
					return true;
				}
			}
		}
		// Check name command.
		else if (cmd.getName().equalsIgnoreCase(DNCCommands.CHECK.getName()))
		{
			if (canUseCheckName(changer))
			{
				String[] saArgs = plugin.parseArguments(args);
				
				if (saArgs == null)
				{
					return false;
				}
				if (saArgs.length == 1)
				{
					Player[] players = plugin.checkName(saArgs[0]);
					
					if (players == null)
					{
						sender.sendMessage(ChatColor.RED + plugin.dnc_short
								+ locale.error_bad_user);
						
						return true;
					}
					else if (players.length == 1)
					{
						String sName = players[0].getName();
						
						String sDisplay = players[0].getDisplayName();
						
						sDisplay = plugin.stripPrefix(sDisplay);
						
						sDisplay = ChatColor.stripColor(sDisplay);
						
						Object[] users = new Object[2];
						
						formatter.applyPattern(locale.info_check_single);
						
						StringBuilder sb = new StringBuilder();
						
						if (changer != null)
						{
							sb.append(ChatColor.GREEN);
						}
						
						sb.append(plugin.dnc_short);
						
						if (sDisplay.equals(saArgs[0]))
						{
							users[0] = (players[0].getDisplayName() + ChatColor.GREEN);
							
							users[1] = sName;
						}
						else
						{
							users[0] = sName;
							
							users[1] = (players[0].getDisplayName() + ChatColor.GREEN);
						}
						
						sb.append(formatter.format(users));
						
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
									+ locale.info_check_multi);
						}
						else
						{
							sender.sendMessage(plugin.dnc_short
									+ locale.info_check_multi);
						}
						
						Object[] users = new Object[2];
						
						formatter.applyPattern(locale.info_check_multi_list);
						
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
								+ locale.error_bad_args);
					}
					else
					{
						sender.sendMessage(plugin.dnc_short
								+ locale.error_bad_args);
					}
					
					return false;
				}
				
			}
			else
			{
				changer.sendMessage(ChatColor.RED + plugin.dnc_short
						+ locale.permission_check);
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Changes the display name of the target.
	 * 
	 * @param target
	 *            the player to change.
	 * 
	 * @param newName
	 *            the new name to use.
	 */
	private void changeDisplayName(Player target, String newName)
	{
		changeDisplayName(null, target, null, newName);
	}
	
	/**
	 * Changes the display name of the target and sends notifications.
	 * 
	 * @param caller
	 *            The person trying to make the change.
	 * 
	 * @param target
	 *            The intended target of the change.
	 * 
	 * @param oldName
	 *            The old name of the target.
	 * 
	 * @param newName
	 *            The new name to use.
	 */
	private void changeDisplayName(Player caller, Player target,
			String oldName, String newName)
	{
		Object[] users = new Object[2];
		
		if (oldName == null)
		{
			users[0] = (target.getDisplayName() + ChatColor.GREEN);
		}
		else
		{
			users[0] = (oldName + ChatColor.GREEN);
		}
		
		String spoutName = newName;
		
		// Parse Color Codes.
		spoutName = plugin.parseColors(spoutName);
		
		// Attach the prefix if needed.
		if (!target.getName().equals(newName))
		{
			spoutName = plugin.prefixNick(spoutName);
		}
		
		users[1] = (spoutName + ChatColor.GREEN);
		
		// Set the DisplayName
		target.setDisplayName(spoutName);
		
		if (plugin.useScoreboard())
		{
			String sListName;
			
			if (spoutName.length() > 16)
			{
				sListName = spoutName.substring(0, 16);
				
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
		
		formatter.applyPattern(locale.info_nick_target);
		
		StringBuilder sbTarget = new StringBuilder();
		
		StringBuilder sbCaller;
		
		sbTarget.append(ChatColor.GREEN).append(plugin.dnc_short)
				.append(formatter.format(users));
		
		target.sendMessage(sbTarget.toString());
		
		if (caller != null && !caller.equals(target))
		{
			sbCaller = new StringBuilder();
			
			formatter.applyPattern(locale.info_nick_caller);
			
			sbCaller.append(ChatColor.GREEN).append(plugin.dnc_short)
					.append(formatter.format(users));
			
			caller.sendMessage(sbCaller.toString());
		}
		
		if (plugin.isSpoutEnabled() == true)
		{
			SpoutPlayer spoutTarget = (SpoutPlayer) target;
			
			if (spoutTarget.isSpoutCraftEnabled())
			{
				spoutTarget.sendNotification(locale.info_spout_target,
						spoutName, Material.DIAMOND);
			}
			
			if (caller != null && !caller.equals(target))
			{
				SpoutPlayer spoutCaller = (SpoutPlayer) caller;
				
				if (spoutCaller.isSpoutCraftEnabled())
				{
					formatter.applyPattern(locale.info_spout_caller);
					
					spoutCaller.sendNotification(formatter.format(users),
							spoutName, Material.DIAMOND);
				}
			}
			
			spoutTarget.setTitle(spoutName);
		}
		
		if (plugin.useGlobalAnnounce())
		{
			Player[] exclude;
			
			if(caller != null)
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
			
			if(targets != null)
			{
				sbCaller = new StringBuilder();
				
				formatter.applyPattern(locale.info_nick_caller);
				
				sbCaller.append(ChatColor.GREEN).append(plugin.dnc_short)
						.append(formatter.format(users));
				
				if(plugin.isBroadcastAll())
				{
					for(Player p: targets)
					{
						p.sendMessage(sbCaller.toString());
					}
				}
				else
				{
					for(Player p: targets)
					{
						if(canBroadcast(p))
						{
							p.sendMessage(sbCaller.toString());
						}
					}
				}
			}
		}
		
		plugin.storeNick(target);
	}
	
	/**
	 * Takes an arrya of players
	 * @param exclude
	 * @return
	 */
	private Player[] getAnnounceTargets(Player[] exclude)
	{
		if (exclude == null || exclude.length == 0)
		{
			throw new IllegalArgumentException("Exclude can not be null or of length 0.");
		}
		
		ArrayList<Player> targets = new ArrayList<Player>();
		
		Player[] onlinePlayers = plugin.getServer().getOnlinePlayers();
		
		for (Player online: onlinePlayers)
		{
			for(Player p: exclude)
			{
				if(!p.equals(online))
				{
					targets.add(online);
				}
			}
		}
		
		targets.trimToSize();
		
		if(targets.size() > 0)
		{
			Player[] result = new Player[targets.size()];
			
			targets.toArray(result);
			
			return result;
		}
		
		return null;
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
	private boolean checkUnique(String name)
	{
		Player[] players = plugin.getServer().getOnlinePlayers();
		
		String sDisplayName = plugin.parseColors(name);
		
		sDisplayName = plugin.prefixNick(sDisplayName);
		
		for (Player p : players)
		{
			if (sDisplayName.equals(p.getDisplayName()))
			{
				return false;
			}
		}
		
		return true;
	}
}
