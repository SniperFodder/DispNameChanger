package me.captain.dnc;

import java.text.MessageFormat;
import java.util.List;
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
	
	private DispNameChanger plugin;
	
	private MessageFormat formatter;
	
	/**
	 * Constructs a new DispNameChange Player Listener.
	 * 
	 * @param plugin
	 *            the plugin that created this listener.
	 */
	public DPL(DispNameChanger plugin)
	{
		this.plugin = plugin;
		
		formatter = new MessageFormat("");
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(final PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		
		if (plugin.changeDeath())
		{
			Object[] user =
			{ player.getDisplayName() };
			
			formatter.applyPattern(plugin.info_player_death);
			
			event.setDeathMessage(formatter.format(user));
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		restoreNick(player);
		
		if (plugin.changeLogin())
		{
			Object[] user =
			{ event.getPlayer().getDisplayName(), ChatColor.YELLOW };
			
			formatter.applyPattern(plugin.info_player_join);
			
			event.setJoinMessage(ChatColor.YELLOW + formatter.format(user));
		}
		
		if (plugin.isSpoutEnabled())
		{
			SpoutPlayer spoutTarget = (SpoutPlayer) player;
			
			spoutTarget.setTitle(player.getDisplayName());
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerKick(final PlayerKickEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		
		storeNick(event.getPlayer());
		
		if (plugin.changeKick())
		{
			Object[] user =
			{ event.getPlayer().getDisplayName(), ChatColor.YELLOW };
			
			formatter.applyPattern(plugin.info_player_kick);
			
			event.setLeaveMessage(ChatColor.YELLOW + formatter.format(user));
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		storeNick(event.getPlayer());
		
		if (plugin.changeLogin())
		{
			Object[] user =
			{ event.getPlayer().getDisplayName(), ChatColor.YELLOW };
			
			formatter.applyPattern(plugin.info_player_quit);
			
			event.setQuitMessage(ChatColor.YELLOW + formatter.format(user));
			
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
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
				
				player.sendMessage(plugin.dnc_short + plugin.error_multi_match);
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
					
					if(!target[0].getName().equalsIgnoreCase(s))
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
					
					player.sendMessage(plugin.dnc_short
							+ plugin.error_multi_match);
					
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
	
	private void restoreNick(Player player)
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
		
		sDName = plugin.parseColors(sDName);
		
		if (!sName.equals(sDName))
		{
			sDName = plugin.prefixNick(sDName);
		}
		
		if (plugin.useScoreboard())
		{
			Player[] players = this.plugin.getServer().getOnlinePlayers();
			
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
					
					player.sendMessage(ChatColor.RED + plugin.dnc_short
							+ plugin.info_nick_conflict);
					
					break;
				}
			}
			
			if (sDName.length() > 16)
			{
				sDName = sDName.substring(0, 16);
			}
			
			player.setPlayerListName(sDName);
		}
		
		player.setDisplayName(sDName);
	}
	
	private void storeNick(Player player)
	{
		String sName = player.getName();
		
		String sDName = player.getDisplayName();
		
		sDName = plugin.stripPrefix(sDName);
		
		DP pClass = (DP) plugin.getDatabase().find(DP.class).where()
				.ieq("PlayerName", sName).findUnique();
		
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
	
}