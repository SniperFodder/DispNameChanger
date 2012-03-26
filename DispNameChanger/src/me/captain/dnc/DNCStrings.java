package me.captain.dnc;

public enum DNCStrings
{
	/*
	 * Error Strings
	 */
	/**
	 * For too many arguments.
	 */
	ERROR_BAD_ARGS,
	
	/**
	 * For when user does not exist or is not online.
	 */
	ERROR_BAD_USER,
	
	/**
	 * For when trying to rename the console.
	 */
	ERROR_CONSOLE_RENAME,
	
	/**
	 * For when multiple people match a target.
	 */
	ERROR_MULTI_MATCH,
	
	/**
	 * For when a name is non-unique.
	 */
	ERROR_NON_UNIQUE,
	
	/*
	 * Info Strings
	 */
	/**
	 * Statement about more than 1 user match.
	 */
	INFO_CHECK_MULTI,
	
	/**
	 * Actual X is Y string for multi-match.
	 */
	INFO_CHECK_MULTI_LIST,
	
	/**
	 * X is Y for single match
	 */
	INFO_CHECK_SINGLE,
	
	/**
	 * Statement about creating database.
	 */
	INFO_DB_MAKE,
	
	/**
	 * Statement about disabling DNC.
	 */
	INFO_DNC_DISABLED,
	
	/**
	 * Statement about enabling DNC.
	 */
	INFO_DNC_ENABLED,
	
	/**
	 * Statement to caller about name change.
	 */
	INFO_NICK_CALLER,
	 
	/**
	 * Name was reset because non-unique.
	 */
	INFO_NICK_CONFLICT,
	
	/**
	 * Statement to target about name change.
	 */
	INFO_NICK_TARGET,
	
	/**
	 * No Spout found statement.
	 */
	INFO_NO_SPOUT,
	
	/**
	 * Death message with nick.
	 */
	INFO_PLAYER_DEATH,
	
	/**
	 * Join message with nick.
	 */
	INFO_PLAYER_JOIN,
	
	/**
	 * Kick message with nick.
	 */
	INFO_PLAYER_KICK,
	
	/**
	 * Quit message with nick.
	 */
	INFO_PLAYER_QUIT,
	
	/**
	 * Spout found statement.
	 */
	INFO_SPOUT,
	
	/**
	 * Spout Achievement message for caller about new name.
	 */
	INFO_SPOUT_CALLER,
	 
	 /**
	 * Spout Achievement message for target about new name.
	 */
	INFO_SPOUT_TARGET,
	
	/*
	 * Permission errors.
	 */
	/**
	 * Error about checking names.
	 */
	PERMISSION_CHECK,
	
	/**
	 * Error about using Bold color code.
	 */
	PERMISSION_COLOR_BOLD,
	
	/**
	 * Error about using Italic Color Code.
	 */
	PERMISSION_COLOR_ITALIC,
	
	/**
	 * Error about using Magic Color Code.
	 */
	PERMISSION_COLOR_MAGIC,
	
	/**
	 * Error about using Strikethrough color code.
	 */
	PERMISSION_COLOR_STRIKETHROUGH,
	
	/**
	 * Error about using Underline color code.
	 */
	PERMISSION_COLOR_UNDERLINE,
	
	/**
	 * Error about listing display names.
	 */
	PERMISSION_LIST,
	
	/**
	 * Error about changing other player's names.
	 */
	PERMISSION_OTHER,
	
	/**
	 * Error about using spaces in names.
	 */
	PERMISSION_SPACES,
	
	/**
	 * Error about changing names.
	 */
	PERMISSION_USE;
}
