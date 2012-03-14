package me.captain.dnc;

import java.util.ResourceBundle;

public class DNCLocalization
{
	public String error_bad_args = "error_bad_args";
	public String error_bad_user = "error_bad_user";
	public String error_console = "error_console";
	public String error_console_rename = "error_console_rename";
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
	public String info_player_death = "info_player_death";
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
	
	private DispNameChanger plugin;
	
	public DNCLocalization()
	{
		plugin = DispNameChanger.getInstance();
	}
	
	/**
	 * Loads Translations from file.
	 */
	public void loadTranslations()
	{
		ResourceBundle dncStrings = ResourceBundle.getBundle(
				"translations/DNCStrings", plugin.getLocale());
		
		permission_spaces = dncStrings.getString("permission_spaces");
		permission_other = dncStrings.getString("permission_other");
		permission_use = dncStrings.getString("permission_use");
		permission_check = dncStrings.getString("permission_check");
		
		error_multi_match = dncStrings.getString("error_multi_match");
		error_bad_user = dncStrings.getString("error_bad_user");
		error_non_unique = dncStrings.getString("error_non_unique");
		error_console = dncStrings.getString("error_console");
		error_console_rename = dncStrings.getString("error_console_rename");
		error_bad_args = dncStrings.getString("error_bad_args");
		
		info_check_multi = dncStrings.getString("info_check_multi");
		info_check_multi_list = dncStrings.getString("info_check_multi_list");
		info_check_single = dncStrings.getString("info_check_single");
		info_nick_conflict = dncStrings.getString("info_nick_conflict");
		info_nick_target = dncStrings.getString("info_nick_target");
		info_nick_caller = dncStrings.getString("info_nick_caller");
		info_player_death = dncStrings.getString("info_player_death");
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
}
