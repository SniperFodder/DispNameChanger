package me.captain.dnc;

import java.util.TimerTask;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

public class DPLSpout implements Listener
{
	private final DispNameChanger plugin;
	
	public DPLSpout()
	{
		plugin = DispNameChanger.getInstance();
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
