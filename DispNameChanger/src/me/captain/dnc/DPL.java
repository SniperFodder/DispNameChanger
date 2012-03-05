package me.captain.dnc;

import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		restoreNick(player);
		
		if (plugin.changeLogin())
		{
			Object[] user =
			{ event.getPlayer().getDisplayName() };
			
			formatter.applyPattern(plugin.info_player_join);
			
			event.setJoinMessage(ChatColor.YELLOW + formatter.format(user));
		}
		
		if (plugin.isSpoutEnabled())
		{
			SpoutPlayer spoutTarget = (SpoutPlayer) player;
			
			spoutTarget.setTitle(player.getDisplayName());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		storeNick(event.getPlayer());
		
		if (plugin.changeLogin())
		{
			Object[] user =
			{ event.getPlayer().getDisplayName() };
			
			formatter.applyPattern(plugin.info_player_quit);
			
			event.setQuitMessage(ChatColor.YELLOW + formatter.format(user));
			
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
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
			{ event.getPlayer().getDisplayName() };
			
			formatter.applyPattern(plugin.info_player_kick);
			
			event.setLeaveMessage(ChatColor.YELLOW + formatter.format(user));
		}
	}
	
	private void storeNick(Player player)
	{
		String sName = player.getName();
		
		String sDName = player.getDisplayName();
		
		if(plugin.usePrefix())
		{
			String prefix = Character.toString(plugin.getPrefix());
			
			if(sDName.contains(prefix))
			{
				String[] split = sDName.split(prefix, 2);
				
				sDName = split[0] + split[1];
			}
		}
		
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
	
	private void restoreNick(Player player)
	{
		System.out.println("!!! RESTORE NICK !!!");
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
		
		//sDName = ChatColor.stripColor(sDName);
		
		//sDName = sDName.replaceAll("(&([a-f0-9]))", "§$2");
		
		if(plugin.usePrefix() && !sName.equals(ChatColor.stripColor(sDName)))
		{
			System.out.println("!!! USE PREFIX !!!!");
			
			if(!sDName.startsWith(Character.toString(plugin.getPrefix())))
			{
				sDName = plugin.getPrefix() + sDName;
			}
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
			
			if(sDName.length() > 16)
			{
				sDName = sDName.substring(0, 16);
			}
			
			player.setPlayerListName(sDName);
		}
		
		player.setDisplayName(sDName);
	}
	
}