package me.captain.dnc;

import java.text.MessageFormat;

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
						if (!checkUnique(saArgs[0]))
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
						Player[] players = plugin.checkName(saArgs[0]);
						
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
					String[] saArgs = plugin.parseArguments(args);
					
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
						Player[] players = plugin.checkName(saArgs[0]);
						
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
		
		users[0] = oldName;
		
		String spoutName = newName;
		
		// Attatch the prefix if needed.
		if(!target.getName().equals(newName))
		{
			spoutName = plugin.prefixNick(spoutName);
		}
		
		// Parse Color Codes.
		spoutName = plugin.parseColors(spoutName);
		
		users[1] = spoutName;
		
		// Set the DisplayName
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
		
		/*
		DP pClass = (DP) plugin.getDatabase().find(DP.class).where()
				.ieq("PlayerName", target.getName()).findUnique();
		
		if (pClass == null)
		{
			pClass = new DP();
		}
		
		pClass.setPlayerName(target.getName());
		
		pClass.setDisplayName(newName);
		
		plugin.getDatabase().save(pClass);
		*/
	}
	
	/**
	 * Ensure that a name is unique to the current player list.
	 * 
	 * @param name
	 *            The name to check for uniqueness.
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
			
			sDisplayName = plugin.stripPrefix(sDisplayName);
			
			if (name.equals(sDisplayName))
			{
				return false;
			}
		}
		
		return true;
	}
}
