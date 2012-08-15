package me.captain.dnc;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Logger;

import me.captain.dnc.DispNameAPI.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

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
	
	private MessageFormat formatter;
	
	private DNCLocalization locale;
	
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
		
		locale = plugin.getLocalization();
		
		formatter = new MessageFormat("");
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
		Player player = event.getEntity();
		
		if (plugin.changeDeath())
		{
			Object[] user =
			{ player.getDisplayName() };
			
			formatter.applyPattern(locale
					.getString(DNCStrings.INFO_PLAYER_DEATH));
			
			event.setDeathMessage(formatter.format(user));
		}
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
			Object[] user =
			{ event.getPlayer().getDisplayName(), ChatColor.YELLOW };
			
			formatter.applyPattern(locale
					.getString(DNCStrings.INFO_PLAYER_JOIN));
			
			event.setJoinMessage(ChatColor.YELLOW + formatter.format(user));
		}
		
		if (plugin.isSpoutEnabled())
		{
			
			SpoutPlayer spoutTarget = (SpoutPlayer) player;
			
			System.out.println("Join - DisplayName: " + player.getDisplayName());
			
			System.out.println("Join - Title - Before: " + spoutTarget.getTitle());
			
			spoutTarget.setTitle(player.getDisplayName());
			
			System.out.println("Join - Title - After: " + spoutTarget.getTitle());
			
			plugin.getServer().getScheduler()
				.scheduleSyncDelayedTask(plugin, new RenameTask(player), 1L);
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
		
		Player player = event.getPlayer();

		savePlayer(player);
		
		if (plugin.changeKick())
		{
			Object[] user =
			{ player.getDisplayName(), ChatColor.YELLOW };
			
			formatter.applyPattern(locale
					.getString(DNCStrings.INFO_PLAYER_KICK));
			
			event.setLeaveMessage(ChatColor.YELLOW + formatter.format(user));
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
			Object[] user =
			{ player.getDisplayName(), ChatColor.YELLOW };
			
			formatter.applyPattern(locale
					.getString(DNCStrings.INFO_PLAYER_QUIT));
			
			event.setQuitMessage(ChatColor.YELLOW + formatter.format(user));
			
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
				
				api.sendMessage(DNCStrings.ERROR_MULTI_MATCH, player, null, MessageType.ERROR);
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
						if(iReplaceMax > -1)
						{
							if(iReplaceCount < iReplaceMax)
							{
								sbCommand.append(target[0].getName()).append(" ");
								
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
					
					api.sendMessage(DNCStrings.ERROR_MULTI_MATCH, player, null, MessageType.ERROR);
					
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
			event.setCancelled(true);
			
			player.chat(sbCommand.toString());
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(DNCStrings.dnc_long).append("Converted |")
					.append(event.getMessage()).append("| to |")
					.append(sbCommand.toString()).append("|.");
			
			log.info(sb.toString());
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
		
		for(String s: cmdFilter.keySet())
		{
			if(s.equalsIgnoreCase(sCommand) && cmdFilter.get(s) == 0)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks to see if the given command is in the filtered list.
	 * @param command
	 * @return
	 */
	private int checkFiltered(String command)
	{
		String sCommand;
		
		if(command.startsWith("/"))
		{
			sCommand = command.substring(1, command.length());
		}
		else
		{
			sCommand = command;
		}
		
		HashMap<String, Integer> cmdFilter = plugin.getCommandList();
		
		for(String s: cmdFilter.keySet())
		{
			if(s.equalsIgnoreCase(sCommand))
			{
				return cmdFilter.get(s);
			}
		}
		return -1;
	}
	
	/**
	 * Remove a player that was in the ordered list.
	 * 
	 * @param p the player to remove.
	 */
	private void removeOrderedPlayer(Player p)
	{
		if(plugin.getOrderedPlayers().containsKey(p.getName()))
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
		if(plugin.isSaveOnQuit())
		{
			api.storeNick(player);
		}
		
		removeOrderedPlayer(player);
	}
	
	/**
	 * Handles setting the title for everyone when a new player joins.
	 * 
	 * @author Mark 'SniperFodder' Gunnett
	 * 
	 */
	private class RenameTask extends TimerTask
	{
		private SpoutPlayer player;
		
		public RenameTask(Player player)
		{
			this.player = (SpoutPlayer) player;
		}
		
		@Override
		public void run()
		{
			System.out.println("Rename - DisplayName: " + player.getDisplayName());
			
			System.out.println("Rename - Title - Before: " + player.getTitle());
			
			player.setTitle(player.getDisplayName());
			
			System.out.println("Rename - Title - After: " + player.getTitle());
		}
	}
}