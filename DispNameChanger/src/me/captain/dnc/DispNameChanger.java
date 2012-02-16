package me.captain.dnc;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;

/**
 * Allows a user to change their display name, or the display name of
 * others. Requires spout integration for changing the name above the head.
 * 
 * @author captainawesome7, itsatacoshop247, Daxiongmao87, Luke Zwekii,
 *         Sammy, SniperFodder
 * 
 */
public class DispNameChanger extends JavaPlugin
{
	public String error_bad_args = "error_bad_args";
	public String error_bad_user = "error_bad_user";
	public String error_console = "error_console";
	public String error_console_rename = "error_console_rename";
	public String error_max_length = "error_max_length";
	public String error_multi_match = "error_multi_match";
	public String error_non_unique = "error_non_unique";
	
	public String info_check_multi = "info_check_multi";
	public String info_check_multi_list = "info_check_multi_list";
	public String info_check_single = "info_check_single";
	public String info_nick_conflict = "info_nick_conflict";
	public String info_nick_target = "info_nick_target";
	public String info_nick_caller = "info_nick_caller";
	public String info_no_spout = "info_no_spout";
	public String info_player_join = "info_player_join";
	public String info_player_kick = "info_player_kick";
	public String info_player_quit = "info_player_quit";
	public String info_spout = "info_spout";
	public String info_spout_target = "info_spout_target";
	public String info_spout_caller = "info_spout_caller";
	public String info_db_make = "info_db_make";
	public String info_dnc_disabled = "info_dnc_disabled";
	public String info_dnc_enabled = "info_dnc_enabled";
	
	public String permission_check = "permission_check";
	public String permission_other = "permission_other";
	public String permission_spaces = "permission_spaces";
	public String permission_use = "permission_use";
	
	public final String dnc_long = "[DispNameChanger] ";
	public final String dnc_short = "[DNC] ";
	
	private static DispNameChanger instance;
	
	private final Logger log = Bukkit.getLogger();
	
	private final DispNameCE executor;
	
	private final DPL playerlistener;
	
	private Locale locale;
	
	private SpoutManager spout;
	
	private Plugin pSpout;
	
	private boolean bChangeKick;
	
	private boolean bChangeLogin;
	
	private boolean bUsePrefix;
	
	private boolean bUseSpout;
	
	private boolean bUseScoreboard;
	
	private boolean bStatsEnabled;
	
	private boolean bStatsMessage;
	
	private boolean bStatsPlayers;
	
	private boolean bStatsPlayersFine;
	
	private char cPrefix;
	
	/**
	 * Constructs a new DispNameChanger.
	 */
	public DispNameChanger()
	{
		super();
		
		executor = new DispNameCE(this);
		
		playerlistener = new DPL(this);
		
		if (instance == null)
		{
			instance = this;
		}
		
		bUsePrefix = false;
		
		bChangeLogin = true;
		
		bChangeKick = true;
		
		bStatsEnabled = true;
		
		bStatsMessage = true;
		
		bStatsPlayers = false;
		
		bStatsPlayersFine = false;
		
		cPrefix = '~';
		
		locale = null;
	}
	
	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	@Override
	public List<Class<?>> getDatabaseClasses()
	{
		List list = new ArrayList();
		
		list.add(DP.class);
		
		return list;
	}
	
	/**
	 * Gets the current SpoutManager.
	 * 
	 * @return a SpoutManager.
	 */
	public SpoutManager getSpoutManager()
	{
		return spout;
	}
	
	/**
	 * Returns the locale used by this plugin.
	 * 
	 * @return A locale used for translations.
	 */
	public Locale getLocale()
	{
		return locale;
	}
	
	/**
	 * Returns the char used as a prefix for displaynames.
	 * 
	 * @return a char for prefixes.
	 */
	public char getPrefix()
	{
		return cPrefix;
	}
	
	/**
	 * Returns whether or not spout is on the server.
	 * 
	 * @return true if spout is loaded, false otherwise.
	 */
	public boolean isSpoutEnabled()
	{
		return bUseSpout;
	}
	
	/**
	 * Check whether login message should be changed to use display name.
	 * 
	 * @return true to change, false otherwise.
	 */
	public boolean changeLogin()
	{
		return bChangeLogin;
	}
	
	/**
	 * Check whether kick/ban message should be changed to use display
	 * name.
	 * 
	 * @return true to change, false otherwise.
	 */
	public boolean changeKick()
	{
		return bChangeKick;
	}
	
	public boolean usePrefix()
	{
		return bUsePrefix;
	}
	
	public boolean useScoreboard()
	{
		return bUseScoreboard;
	}
	
	@Override
	public void onDisable()
	{
		log.info(dnc_long + info_dnc_disabled);
	}
	
	@Override
	public void onEnable()
	{
		loadConfig();
		
		PluginManager pm = getServer().getPluginManager();
		
		pSpout = pm.getPlugin("Spout");
		
		formatTranslations();
		
		setupDatabase();
		
		pm.registerEvents(playerlistener, this);
		
		getCommand("rename").setExecutor(executor);
		
		getCommand("reset").setExecutor(executor);
		
		getCommand("check").setExecutor(executor);
		
		if (pSpout == null)
		{
			this.bUseSpout = false;
			
			log.info(dnc_long + info_no_spout);
		}
		else
		{
			this.bUseSpout = true;
			
			log.info(dnc_long + info_spout);
		}
		
		log.info(dnc_long + info_dnc_enabled);
	}
	
	/**
	 * Returns the current plugin instance.
	 * 
	 * @return the DispNameChanger plugin.
	 */
	public static DispNameChanger getInstance()
	{
		return instance;
	}
	
	/*
	 * Formats translations strings with their respective arguments.
	 */
	private void formatTranslations()
	{
		Object[] spoutArgs = new Object[1];
		if (pSpout != null)
		{
			spoutArgs[0] = pSpout.getDescription().getVersion();
		}
		else
		{
			spoutArgs[0] = "unknown";
		}
		
		Object[] dncArgs =
		{ getDescription().getVersion(),
				getDescription().getAuthors().toString(),
				getDescription().getName() };
		
		MessageFormat formatter = new MessageFormat("");
		
		formatter.setLocale(locale);
		
		formatter.applyPattern(info_spout);
		
		info_spout = formatter.format(spoutArgs);
		
		formatter.applyPattern(info_dnc_enabled);
		
		info_dnc_enabled = formatter.format(dncArgs);
		
		formatter.applyPattern(info_dnc_disabled);
		
		info_dnc_disabled = formatter.format(dncArgs);
		
		formatter.applyPattern(info_db_make);
		
		info_db_make = formatter.format(dncArgs);
	}
	
	/*
	 * Load the configuration settings and localization files for DNC.
	 */
	private void loadConfig()
	{
		File fConfig = new File(this.getDataFolder() + File.separator
				+ "config.yml");
		
		if (!fConfig.exists())
		{
			this.saveDefaultConfig();
		}
		
		FileConfiguration conf = getConfig();
		
		bChangeLogin = conf.getBoolean("messages.login");
		
		bChangeKick = conf.getBoolean("messages.kick");
		
		bUsePrefix = conf.getBoolean("prefix.enabled");
		
		String sPrefix = conf.getString("prefix.character");
		
		if (sPrefix != null)
		{
			cPrefix = sPrefix.charAt(0);
		}
		
		bUseScoreboard = conf.getBoolean("scoreboard");
		
		bStatsEnabled = conf.getBoolean("stats.enabled");
		
		bStatsMessage = conf.getBoolean("stats.message");
		
		bStatsPlayers = conf.getBoolean("stats.player-count");
		
		bStatsPlayersFine = conf.getBoolean("stats.player-names");
		
		String sLocale = getConfig().getString("language");
		
		if (sLocale.equals("en_US"))
		{
			locale = Locale.ENGLISH;
			
			log.info(dnc_long + "English Translation Selected.");
		}
		
		if (locale == null)
		{
			log.severe(dnc_long + "Unknown Locale given: " + sLocale);
			
			log.severe(dnc_long + "Reverting to English!");
			
			locale = Locale.ENGLISH;
		}
		
		try
		{
			conf.save(fConfig);
		}
		catch (IOException e)
		{
			log.severe(e.getMessage());
		}
		
		loadTranslations();
	}
	
	/*
	 * Load Translations
	 */
	private void loadTranslations()
	{
		ResourceBundle dncStrings = ResourceBundle.getBundle(
				"translations/DNCStrings", Locale.ENGLISH);
		
		permission_spaces = dncStrings.getString("permission_spaces");
		permission_other = dncStrings.getString("permission_other");
		permission_use = dncStrings.getString("permission_use");
		permission_check = dncStrings.getString("permission_check");
		
		error_multi_match = dncStrings.getString("error_multi_match");
		error_bad_user = dncStrings.getString("error_bad_user");
		error_non_unique = dncStrings.getString("error_non_unique");
		error_max_length = dncStrings.getString("error_max_length");
		error_console = dncStrings.getString("error_console");
		error_console_rename = dncStrings.getString("error_console_rename");
		error_bad_args = dncStrings.getString("error_bad_args");
		
		info_check_multi = dncStrings.getString("info_check_multi");
		info_check_multi_list = dncStrings.getString("info_check_multi_list");
		info_check_single = dncStrings.getString("info_check_single");
		info_nick_conflict = dncStrings.getString("info_nick_conflict");
		info_nick_target = dncStrings.getString("info_nick_target");
		info_nick_caller = dncStrings.getString("info_nick_caller");
		info_player_join = dncStrings.getString("info_player_join");
		info_player_kick = dncStrings.getString("info_player_kick");
		info_player_quit = dncStrings.getString("info_player_quit");
		info_spout_target = dncStrings.getString("info_spout_target");
		info_spout_caller = dncStrings.getString("info_spout_caller");
		info_spout = dncStrings.getString("info_spout");
		info_no_spout = dncStrings.getString("info_no_spout");
		info_dnc_enabled = dncStrings.getString("info_dnc_enabled");
		info_dnc_disabled = dncStrings.getString("info_dnc_disabled");
		info_db_make = dncStrings.getString("info_db_make");
	}
	
	/*
	 * Tries to load the database, and if unable to find it, creates a new
	 * one.
	 */
	private void setupDatabase()
	{
		try
		{
			getDatabase().find(DP.class).findRowCount();
		}
		catch (PersistenceException ex)
		{
			log.info(dnc_long + info_db_make);
			
			installDDL();
		}
	}
}