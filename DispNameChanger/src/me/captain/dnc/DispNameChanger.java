package me.captain.dnc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
	private static DispNameChanger instance;
	
	private final Logger log = Bukkit.getLogger();
	
	private final DispNameCE executor;
	
	private final DNCLocalization localization;
	
	private final DPL playerlistener;
	
	private ChatColor ccPrefix;
	
	private HashMap<String, Integer> hCommands;
	
	private Locale locale;
	
	private Plugin pSpout;
	
	private Properties pVersion;
	
	private String sPrefixFull;
	
	private String sPrefixShort;
	
	private TreeMap<String, Player> tmPlayers;
	
	private boolean bBroadcastAll;
	
	private boolean bChangeDeath;
	
	private boolean bChangeKick;
	
	private boolean bChangeLogin;
	
	private boolean bGlobalAnnounce;
	
	private boolean bSaveOnQuit;
	
	private boolean bSaveOnRename;
	
	private boolean bUsePrefix;
	
	private boolean bUsePrefixColor;
	
	private boolean bUseSpout;
	
	private boolean bUseScoreboard;
	
	// private boolean bStatsEnabled;
	
	// private boolean bStatsMessage;
	
	// private boolean bStatsPlayers;
	
	// private boolean bStatsPlayersFine;
	
	private char cPrefix;
	
	/**
	 * Constructs a new DispNameChanger.
	 */
	public DispNameChanger()
	{
		super();
		
		if (instance == null)
		{
			instance = this;
		}
		
		localization = new DNCLocalization();
		
		executor = new DispNameCE();
		
		playerlistener = new DPL();
		
		pVersion = new Properties();
		
		hCommands = new HashMap<String, Integer>();
		
		tmPlayers = new TreeMap<String, Player>();
		
		bBroadcastAll = true;
		
		bUsePrefix = false;
		
		bChangeDeath = true;
		
		bChangeLogin = true;
		
		bChangeKick = true;
		
		bGlobalAnnounce = true;
		
		bSaveOnQuit = false;
		
		bSaveOnRename = true;
		
		// bStatsEnabled = true;
		
		// bStatsMessage = true;
		
		// bStatsPlayers = false;
		
		// bStatsPlayersFine = false;
		
		cPrefix = '+';
		
		ccPrefix = ChatColor.YELLOW;
		
		locale = null;
	}
	
	/**
	 * Check whether death message should be changed to use display name.
	 * 
	 * @return True to change, false otherwise.
	 */
	public boolean changeDeath()
	{
		return bChangeDeath;
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
	 * Checks whether nick changes are broadcast to all or admins only.
	 * 
	 * @return True if everyone should receive broadcast, false otherwise.
	 */
	public boolean isBroadcastAll()
	{
		return bBroadcastAll;
	}
	
	/**
	 * Check to see if should save DisplayName on quit.
	 * 
	 * @return True to save, false otherwise.
	 */
	public boolean isSaveOnQuit()
	{
		return bSaveOnQuit;
	}
	
	/**
	 * Check to see if should save Displayname on rename.
	 * 
	 * @return True to save, false otherwise.
	 */
	public boolean isSaveOnRename()
	{
		return bSaveOnRename;
	}
	
	@Override
	public List<Class<?>> getDatabaseClasses()
	{
		List<Class<?>> list = new ArrayList<Class<?>>();
		
		list.add(DP.class);
		
		return list;
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
	 * Gets the localization class used for strings.
	 * 
	 * @return the current DNCLocalization object.
	 */
	public DNCLocalization getLocalization()
	{
		return localization;
	}
	
	/**
	 * Returns the char used as a prefix for display names.
	 * 
	 * @return a char for prefixes.
	 */
	public char getPrefix()
	{
		return cPrefix;
	}
	
	/**
	 * Gets the full prefix (color & char).
	 * 
	 * @return a string with the prefix and color.
	 */
	public String getPrefixFull()
	{
		return sPrefixFull;
	}
	
	/**
	 * Gets the short prefix ( char only ).
	 * 
	 * @return String with the char.
	 */
	public String getPrefixShort()
	{
		return sPrefixShort;
	}
	
	/**
	 * Returns the color to be used the prefix. Default is yellow.
	 * 
	 * @return The color to use with the prefix.
	 */
	public ChatColor getPrefixColor()
	{
		return ccPrefix;
	}
	
	/**
	 * Returns a map of commands and number of name replacements allowed for that command.
	 * 
	 * @return map of commands + max replacement.
	 */
	public HashMap<String, Integer> getCommandList()
	{
		return hCommands;
	}
	
	/**
	 * Returns a list of players ordered by login name.
	 * 
	 * @return A tree with a list of ordered players on the server.
	 */
	public TreeMap<String, Player> getOrderedPlayers()
	{
		return tmPlayers;
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
	 * Returns whether or not to globally announce name changes.
	 * 
	 * @return true to announce, false otherwise.
	 */
	public boolean useGlobalAnnounce()
	{
		return bGlobalAnnounce;
	}
	
	@Override
	public void onDisable()
	{
		log.info(DNCStrings.dnc_long
				+ localization.getString(DNCStrings.INFO_DNC_DISABLED));
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
		
		for (DNCCommands cmd : DNCCommands.values())
		{
			getCommand(cmd.getName()).setExecutor(executor);
		}
		
		if (pSpout == null)
		{
			this.bUseSpout = false;
			
			log.info(DNCStrings.dnc_long
					+ localization.getString(DNCStrings.INFO_NO_SPOUT));
		}
		else
		{
			this.bUseSpout = true;
			
			log.info(DNCStrings.dnc_long + localization.getString(DNCStrings.INFO_SPOUT));
		}
		
		log.info(DNCStrings.dnc_long
				+ localization.getString(DNCStrings.INFO_DNC_COMMANDS));
		
		if(!isSaveOnQuit() && !isSaveOnRename())
		{
			log.severe(DNCStrings.dnc_long + localization.getString(DNCStrings.INFO_SAVE_DISABLED));
		}
		
		log.info(DNCStrings.dnc_long
				+ localization.getString(DNCStrings.INFO_DNC_ENABLED));
	}
	
	/**
	 * Checks whether the prefix function of the plugin is enabled.
	 * 
	 * @return true to use prefix, false otherwise.
	 */
	public boolean usePrefix()
	{
		return bUsePrefix;
	}
	
	/**
	 * Checks whether prefix should be colored or not.
	 * 
	 * @return True if it should be colored, false otherwise.
	 */
	public boolean usePrefixColor()
	{
		return bUsePrefixColor;
	}
	
	/**
	 * Checks whether to use the scoreboard function of the plugin.
	 * 
	 * @return True if scoreboard integration enabled, false otherwise.
	 */
	public boolean useScoreboard()
	{
		return bUseScoreboard;
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
	
	/**
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
		
		List<String> authors = getDescription().getAuthors();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		
		for (String s : authors)
		{
			sb.append(s).append(", ");
		}
		
		sb.delete(sb.lastIndexOf(","), sb.length());
		
		sb.append("]");
		
		Object[] dncArgs =
		{ getDescription().getVersion(), sb.toString(),
				getDescription().getName() };
		
		Object[] dncCommands =
		{ hCommands.size(), hCommands.toString() };
		
		MessageFormat formatter = new MessageFormat("");
		
		formatter.setLocale(locale);
		
		formatter.applyPattern(localization.getString(DNCStrings.INFO_SPOUT));
		
		localization.setString(DNCStrings.INFO_SPOUT,
				formatter.format(spoutArgs));
		
		formatter.applyPattern(localization
				.getString(DNCStrings.INFO_DNC_ENABLED));
		
		localization.setString(DNCStrings.INFO_DNC_ENABLED,
				formatter.format(dncArgs));
		
		formatter.applyPattern(localization
				.getString(DNCStrings.INFO_DNC_DISABLED));
		
		localization.setString(DNCStrings.INFO_DNC_DISABLED,
				formatter.format(dncArgs));
		
		formatter
				.applyPattern(localization.getString(DNCStrings.INFO_DB_MAKE));
		
		localization.setString(DNCStrings.INFO_DB_MAKE,
				formatter.format(dncArgs));
		
		formatter.applyPattern(localization
				.getString(DNCStrings.INFO_DNC_COMMANDS));
		
		localization.setString(DNCStrings.INFO_DNC_COMMANDS,
				formatter.format(dncCommands));
	}
	
	/**
	 * Load the configuration settings and localization files for DNC.
	 */
	private void loadConfig()
	{
		File fConfig = new File(this.getDataFolder() + File.separator
				+ "config.yml");
		
		File fProperties = new File(this.getDataFolder() + File.separator
				+ "VERSION");
		
		if (!fConfig.exists())
		{
			this.saveDefaultConfig();
		}
		
		FileConfiguration conf = getConfig();
		
		if (!fProperties.exists())
		{
			log.info(DNCStrings.dnc_long
					+ "Unable to find Config Version info. Copying Defaults from Default Config.");
			
			conf.options().copyDefaults(true);
		}
		else
		{
			try
			{
				pVersion.load(new FileInputStream(fProperties));
			}
			catch (FileNotFoundException e)
			{
				log.throwing("DispNameChanger", "loadConfig", e);
			}
			catch (IOException e)
			{
				log.throwing("DispNameChanger", "loadConfig", e);
			}
			
			if (pVersion.containsKey("VERSION"))
			{
				if (!pVersion.getProperty("VERSION").equals(
						this.getDescription().getVersion()))
				{
					log.info(DNCStrings.dnc_long
							+ "Different Config Version Detected: "
							+ pVersion.getProperty("VERSION")
							+ " Copying Defaults from Default Config.");
					
					conf.options().copyDefaults(true);
				}
			}
			else
			{
				log.info(DNCStrings.dnc_long
						+ "Unable to find Config Version info. Copying defaults from Default Config.");
				
				conf.options().copyDefaults(true);
			}
		}
		
		pVersion.put("VERSION", this.getDescription().getVersion());
		
		try
		{
			pVersion.store(new FileWriter(fProperties), null);
		}
		catch (IOException e)
		{
			log.throwing("DispNameChanger", "loadConfig", e);
		}
		
		bChangeDeath = conf.getBoolean("messages.death");
		
		bChangeLogin = conf.getBoolean("messages.login");
		
		bChangeKick = conf.getBoolean("messages.kick");
		
		bGlobalAnnounce = conf.getBoolean("global-announce.enabled");
		
		String sAnnounce = conf.getString("global-announce.target");
		
		if (sAnnounce == null)
		{
			bBroadcastAll = true;
		}
		else if (sAnnounce.equalsIgnoreCase("all"))
		{
			bBroadcastAll = true;
		}
		else if (sAnnounce.equalsIgnoreCase("admin"))
		{
			bBroadcastAll = false;
		}
		
		bUsePrefix = conf.getBoolean("prefix.enabled");
		
		String scPrefix = conf.getString("prefix.character");
		
		if (scPrefix != null)
		{
			cPrefix = scPrefix.charAt(0);
		}
		
		bUsePrefixColor = conf.getBoolean("prefix.color.enabled");
		
		String sPrefixColor = conf.getString("prefix.color.code");
		
		if (sPrefixColor != null)
		{
			ccPrefix = ChatColor.getByChar(sPrefixColor);
			
			if (ccPrefix == null)
			{
				ccPrefix = ChatColor.YELLOW;
			}
		}
		else
		{
			ccPrefix = ChatColor.YELLOW;
		}
		
		if (usePrefixColor())
		{
			sPrefixFull = ccPrefix + Character.toString(cPrefix)
					+ ChatColor.RESET;
			
			sPrefixShort = ccPrefix + Character.toString(cPrefix);
		}
		
		bUseScoreboard = conf.getBoolean("scoreboard");
		
		bSaveOnQuit = conf.getBoolean("save.quit");
		
		bSaveOnRename = conf.getBoolean("save.rename");
		
		// bStatsEnabled = conf.getBoolean("stats.enabled");
		
		// bStatsMessage = conf.getBoolean("stats.message");
		
		// bStatsPlayers = conf.getBoolean("stats.player-count");
		
		// bStatsPlayersFine = conf.getBoolean("stats.player-names");
		
		String sLocale = getConfig().getString("language");
		
		if (sLocale.equals("en_US"))
		{
			locale = Locale.ENGLISH;
			
			log.info(DNCStrings.dnc_long + "English Translation Selected.");
		}
		else if (sLocale.equals("fr"))
		{
			locale = Locale.FRENCH;
			
			log.info(DNCStrings.dnc_long + "French Translation Selected.");
		}
		
		if (locale == null)
		{
			log.severe(DNCStrings.dnc_long + "Unknown Locale given: " + sLocale);
			
			log.severe(DNCStrings.dnc_long + "Reverting to English!");
			
			locale = Locale.ENGLISH;
		}
		
		ConfigurationSection section = conf
				.getConfigurationSection("commands");
		
		if (section != null)
		{
			Set<String> commands = section.getKeys(false);
			
			for (String s : commands)
			{
				if (conf.isInt("commands." + s))
				{
					int iCount = conf.getInt("commands." + s);
					
					if (iCount < 0)
					{
						hCommands.put(s, new Integer(0));
						
						conf.set("commands." + s, 0);
					}
					else
					{
						hCommands.put(s, new Integer(iCount));
					}
				}
				else
				{
					hCommands.put(s, new Integer(0));
					
					conf.set("commands." + s, 0);
				}
			}
		}
		
		try
		{
			conf.save(fConfig);
		}
		catch (IOException e)
		{
			log.severe(e.getMessage());
		}
		
		localization.loadTranslations();
	}
	
	/**
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
			log.info(DNCStrings.dnc_long
					+ localization.getString(DNCStrings.INFO_DB_MAKE));
			
			installDDL();
		}
	}
}