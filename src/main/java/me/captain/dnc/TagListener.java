package me.captain.dnc;

import java.text.MessageFormat;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

/**
 * This Class holds all the listeners used for Changing the tag of players
 * (Floating name) In vanilla Minecraft.
 * 
 * @author Mark 'SniperFodder' Gunnett
 * 
 */
public class TagListener implements Listener
{
	private static final Logger log = Bukkit.getLogger();
	
	private DispNameChanger plugin;
	
	private MessageFormat formatter;
	
	private DNCLocalization locale;
	
	public TagListener()
	{
		super();
		
		plugin = DispNameChanger.getInstance();
		
		locale = plugin.getLocalization();
		
		formatter = new MessageFormat("");
		
		formatter.setLocale(plugin.getLocale());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onNameTag(PlayerReceiveNameTagEvent event)
	{
		if(!plugin.useTagTitle())
		{
			return;
		}
		
		Player target = event.getNamedPlayer();
		
		if(event.isModified())
		{
			Object[] tagArgs = new Object[3];
			
			tagArgs[0] = target.getName();
			
			tagArgs[1] = target.getDisplayName();
			
			tagArgs[2] = event.getTag();
			
			formatter.applyPattern(locale.getString(DNCStrings.INFO_TAG_MODIFIED));
			
			log.info(DNCStrings.dnc_long + formatter.format(tagArgs));
			
			return;
		}
		
		if(!target.getName().equals(target.getDisplayName()))
		{
			event.setTag(target.getDisplayName());
		}
	}
}
