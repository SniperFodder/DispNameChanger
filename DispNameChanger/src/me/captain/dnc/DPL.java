package me.captain.dnc;

import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
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
			Object[] user = { player.getDisplayName() };
			
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
		
		if(!sName.equals(sDName))
		{
			sDName = plugin.prefixNick(sDName);
		}
		
		System.out.println(plugin.dnc_short +  "restorenick: " + sDName);

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
			
			if(sDName.length() > 16)
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