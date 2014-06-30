package me.captain.dnc;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import me.captain.dnc.DispNameAPI.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Contains functions for ensuring the player is named properly, as well as
 * changing kick/death messages. Command PreProcessing is also done here;
 * This is where display names are replaced in command strings.
 * 
 * @author captainawesome7, itsatacoshop247, Daxiongmao87, Luke Zwekii,
 *         Sammy, SniperFodder
 * 
 */
public class DPL implements Listener
{
	private static final Logger log = Bukkit.getLogger();
	
	private DispNameChanger plugin;
	
	private DispNameAPI api;
	
	/**
	 * Constructs a new DispNameChange Player Listener.
	 * 
	 * @param plugin
	 *            the plugin that created this listener.
	 */
	public DPL()
	{
		plugin = DispNameChanger.getInstance();
		
		api = DispNameAPI.getInstance();
	}
	
	/**
	 * Triggered on player death.
	 * 
	 * @param event
	 *            the event triggered by the death.
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(final PlayerDeathEvent event)
	{
		if (!plugin.changeDeath())
			return;
		
		Player[] players = api.checkForNames(event.getDeathMessage());
		
		String sReplace = event.getDeathMessage();
		
		for(Player target : players)
		{
			sReplace = sReplace.replaceFirst(target.getName(), target.getDisplayName());
		}
		
		event.setDeathMessage(sReplace);
	}
	
	/**
	 * Triggered whenever a player has joined the server.
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		api.restoreNick(player);
		
		plugin.getOrderedPlayers().put(player.getName(), player);
		
		if (plugin.changeLogin())
		{
			if (api.isNameChanged(player))
			{
				event.setJoinMessage(event.getJoinMessage().replaceFirst(
						player.getName(), player.getDisplayName()));
			}
		}
	}
	
	/**
	 * Triggered whenever a player is kicked from the server.
	 * 
	 * @param event
	 *            The event triggered by the action.
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerKick(final PlayerKickEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		
		if(plugin.isDebug())
		{
			log.info("onPlayerKick - before:" + event.getLeaveMessage());
		}
		
		Player player = event.getPlayer();
		
		savePlayer(player);
		
		if (plugin.changeKick())
		{
			if (api.isNameChanged(player))
			{
				event.setLeaveMessage(event.getLeaveMessage().replaceFirst(
						player.getName(), player.getDisplayName()));
			}
		}
	}
	
	/**
	 * Called whenever the player quits the server.
	 * 
	 * @param event
	 *            the event triggered by the action.
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		
		savePlayer(player);
		
		if (plugin.changeLogin())
		{
			if (api.isNameChanged(player))
			{
				event.setQuitMessage(event.getQuitMessage().replaceFirst(
						player.getName(), player.getDisplayName()));
			}
		}
	}
	
	/**
	 * Changes any display names to real names in any commands a user sends
	 * to the server.
	 * 
	 * @param event
	 *            The event that was triggered by sending a command.
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		/*
		 * TODO This does not handle Console Events. Re-write to handle
		 * ServerCommandEvent.
		 */
		Player player = event.getPlayer();
		
		StringBuilder sbCommand = null;
		
		Player[] target;
		
		String[] args = event.getMessage().split(" ");
		
		// Ensure that we aren't matching our command.
		if (args[0].startsWith("/"))
		{
			if (checkCommands(args[0]))
			{
				return;
			}
		}
		
		switch (args.length)
		{
		// Whatever it is, it can't have a DispName
		case 1:
			break;
		// Possible command + DispName
		case 2:
			target = api.checkName(args[1]);
			
			switch (target.length)
			{
			case 0:
				break;
			case 1:
				if (!target[0].getName().equalsIgnoreCase(args[1]))
				{
					sbCommand = new StringBuilder();
					
					sbCommand.append(args[0]).append(" ")
							.append(target[0].getName());
					
					break;
				}
				break;
			default:
				event.setCancelled(true);
				
				api.sendMessage(DNCStrings.ERROR_MULTI_MATCH, player, null,
						MessageType.ERROR);
			}
			break;
		// Possible Command + Multipart DispName
		default:
			String[] saArgs = event.getMessage().split(" ", 2);
			
			String[] saParsed = api.parseArgumentsAll(saArgs[1]);
			
			boolean bNamesAdded = false;
			
			int iReplaceMax = checkFiltered(saArgs[0]);
			
			int iReplaceCount = 0;
			
			sbCommand = new StringBuilder();
			
			sbCommand.append(args[0]).append(" ");
			
			for (String s : saParsed)
			{
				target = api.checkName(s);
				
				if (target.length == 0)
				{
					sbCommand.append(s).append(" ");
					
					continue;
				}
				
				switch (target.length)
				{
				case 1:
					
					if (!target[0].getName().equalsIgnoreCase(s))
					{
						if (iReplaceMax > -1)
						{
							if (iReplaceCount < iReplaceMax)
							{
								sbCommand.append(target[0].getName()).append(
										" ");
								
								iReplaceCount++;
							}
							else
							{
								sbCommand.append(s).append(" ");
							}
						}
						else
						{
							sbCommand.append(target[0].getName()).append(" ");
						}
						
						bNamesAdded = true;
					}
					else
					{
						sbCommand.append(s).append(" ");
					}
					break;
				default:
					
					event.setCancelled(true);
					
					api.sendMessage(DNCStrings.ERROR_MULTI_MATCH, player,
							null, MessageType.ERROR);
					
					sbCommand = null;
					
					bNamesAdded = false;
					
					break;
				}
			}
			
			if (bNamesAdded == false)
			{
				sbCommand = null;
			}
		}
		
		if (sbCommand != null)
		{
			event.setMessage(sbCommand.toString());
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(DNCStrings.dnc_long).append("Converted |")
					.append(event.getMessage()).append("| to |")
					.append(sbCommand.toString()).append("|.");
			
			if(plugin.isDebug())
			{
				log.info(sb.toString());
			}
		}
	}
	
	/**
	 * Checks to see if a command matches one of the current plugin
	 * commands, or a command we should not filter as indicated by the
	 * configuration file.
	 * 
	 * @param command
	 *            the command to check for.
	 * 
	 * @return true if the command matches, false otherwise.
	 */
	private boolean checkCommands(String command)
	{
		String sCommand;
		
		if (command.startsWith("/"))
		{
			sCommand = command.substring(1);
		}
		else
		{
			sCommand = command;
		}
		
		List<String> aliases;
		
		for (DNCCommands cmd : DNCCommands.values())
		{
			if (cmd.getName().equalsIgnoreCase(sCommand))
			{
				return true;
			}
			
			aliases = plugin.getCommand(cmd.getName()).getAliases();
			
			for (String s : aliases)
			{
				if (s.equalsIgnoreCase(sCommand))
				{
					return true;
				}
			}
		}
		
		HashMap<String, Integer> cmdFilter = plugin.getCommandList();
		
		for (String s : cmdFilter.keySet())
		{
			if (s.equalsIgnoreCase(sCommand) && cmdFilter.get(s) == 0)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks to see if the given command is in the filtered list.
	 * 
	 * @param command
	 * @return
	 */
	private int checkFiltered(String command)
	{
		String sCommand;
		
		if (command.startsWith("/"))
		{
			sCommand = command.substring(1, command.length());
		}
		else
		{
			sCommand = command;
		}
		
		HashMap<String, Integer> cmdFilter = plugin.getCommandList();
		
		for (String s : cmdFilter.keySet())
		{
			if (s.equalsIgnoreCase(sCommand))
			{
				return cmdFilter.get(s);
			}
		}
		return -1;
	}
	
	/**
	 * Remove a player that was in the ordered list.
	 * 
	 * @param p
	 *            the player to remove.
	 */
	private void removeOrderedPlayer(Player p)
	{
		if (plugin.getOrderedPlayers().containsKey(p.getName()))
		{
			plugin.getOrderedPlayers().remove(p.getName());
		}
	}
	
	/**
	 * Saves a players name on save if Saving on quit is enabled.
	 * 
	 * @param player
	 */
	private void savePlayer(Player player)
	{
		if (plugin.isSaveOnQuit())
		{
			api.storeNick(player);
		}
		
		removeOrderedPlayer(player);
	}
}