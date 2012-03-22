package me.captain.dnc;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Logger;

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
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 * Contains functions for ensuring the player is named properly.
 * 
 * @author captainawesome7, itsatacoshop247, Daxiongmao87, Luke Zwekii,
 *         Sammy, SniperFodder
 * 
 */
public class DPL implements Listener
{
	private static final Logger log = Bukkit.getLogger();
	
	private HashMap<String, Integer> entityID;
	
	private DispNameChanger plugin;
	
	private MessageFormat formatter;
	
	private DNCLocalization locale;
	
	/**
	 * Constructs a new DispNameChange Player Listener.
	 * 
	 * @param plugin
	 *            the plugin that created this listener.
	 */
	public DPL()
	{
		plugin = DispNameChanger.getInstance();
		
		locale = plugin.getLocalization();
		
		formatter = new MessageFormat("");
		
		entityID = new HashMap<String, Integer>();
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
			
			formatter.applyPattern(locale.getString(DNCStrings.INFO_PLAYER_DEATH));
			
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
		
		entityID.put(player.getName(), new Integer(player.getEntityId()));
		
		plugin.restoreNick(player);
		
		if (plugin.changeLogin())
		{
			Object[] user =
			{ event.getPlayer().getDisplayName(), ChatColor.YELLOW };
			
			formatter.applyPattern(locale.getString(DNCStrings.INFO_PLAYER_JOIN));
			
			event.setJoinMessage(ChatColor.YELLOW + formatter.format(user));
		}
		
		if (plugin.isSpoutEnabled())
		{
			SpoutPlayer spoutTarget = (SpoutPlayer) player;
			
			spoutTarget.setTitle(player.getDisplayName());
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
		
		int iEntityID = player.getEntityId();
		
		int iStoredID = entityID.get(player.getName()).intValue();
		
		if (iEntityID == iStoredID)
		{
			plugin.storeNick(player);
			
			entityID.put(player.getName(), -1);
		}
		
		if (plugin.changeKick())
		{
			Object[] user =
			{ player.getDisplayName(), ChatColor.YELLOW };
			
			formatter.applyPattern(locale.getString(DNCStrings.INFO_PLAYER_KICK));
			
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
		
		int iEntityID = player.getEntityId();
		
		int iStoredID = entityID.get(player.getName()).intValue();
		
		if (iEntityID == iStoredID)
		{
			plugin.storeNick(player);
			
			entityID.remove(player.getName());
		}
		else if (iEntityID == -1)
		{
			entityID.remove(player.getName());
		}
		
		if (plugin.changeLogin())
		{
			Object[] user =
			{ player.getDisplayName(), ChatColor.YELLOW };
			
			formatter.applyPattern(locale.getString(DNCStrings.INFO_PLAYER_QUIT));
			
			event.setQuitMessage(ChatColor.YELLOW + formatter.format(user));
			
		}
	}
	
	/**
	 * Handles setting the title for each player after they join the
	 * server.
	 * 
	 * @param event
	 *            the event triggered when a player with SpoutCraft joins
	 *            the server.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event)
	{
		// TODO Fix this when Spout fixes their damn code.
		
		plugin.getServer().getScheduler()
				.scheduleSyncDelayedTask(plugin, new RenameTask(), 150L);
	}
	
	/**
	 * Changes any display names to real names in any commands a user sends
	 * to the server.
	 * 
	 * @param event
	 *            The event that was triggered by sending a command.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
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
			target = plugin.checkName(args[1]);
			
			if (target == null)
			{
				break;
			}
			
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
				
				player.sendMessage(ChatColor.RED + plugin.dnc_short
						+ locale.getString(DNCStrings.ERROR_MULTI_MATCH));
			}
			break;
		// Possible Command + Multipart DispName
		default:
			String[] saArgs = event.getMessage().split(" ", 2);
			
			String[] saParsed = plugin.parseArgumentsAll(saArgs[1]);
			
			boolean bNamesAdded = false;
			
			sbCommand = new StringBuilder();
			
			sbCommand.append(args[0]).append(" ");
			
			for (String s : saParsed)
			{
				target = plugin.checkName(s);
				
				if (target == null)
				{
					sbCommand.append(s).append(" ");
					
					continue;
				}
				
				switch (target.length)
				{
				case 1:
					
					if (!target[0].getName().equalsIgnoreCase(s))
					{
						
						sbCommand.append(target[0].getName()).append(" ");
						
						bNamesAdded = true;
					}
					else
					{
						
						sbCommand.append(s).append(" ");
					}
					break;
				default:
					
					event.setCancelled(true);
					
					player.sendMessage(ChatColor.RED + plugin.dnc_short
							+ locale.getString(DNCStrings.ERROR_MULTI_MATCH));
					
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
			
			sb.append(plugin.dnc_long).append("Converted |")
					.append(event.getMessage()).append("| to |")
					.append(sbCommand.toString()).append("|.");
			
			log.info(sb.toString());
		}
	}
	
	/**
	 * Checks to see if a command matches one of the current plugin
	 * commands.
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
		
		return false;
	}
	
	/**
	 * Handles setting the title for everyone when a new player joins.
	 * 
	 * @author Mark 'SniperFodder' Gunnett
	 * 
	 */
	private class RenameTask extends TimerTask
	{
		@Override
		public void run()
		{
			SpoutPlayer[] onlinePlayers = SpoutManager.getOnlinePlayers();
			
			for (SpoutPlayer p : onlinePlayers)
			{
				if (!p.getName().equals(p.getDisplayName()))
				{
					p.setTitle(p.getDisplayName());
				}
			}
			
		}
	}
}