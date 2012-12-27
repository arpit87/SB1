package my.b1701.SB.ChatService;

import my.b1701.SB.HelperClasses.ConfigBase;
import my.b1701.SB.HelperClasses.Constants;
import my.b1701.SB.HelperClasses.ThisAppConfig;

public class ChatConfig  extends ConfigBase{
		
		private static ChatConfig instance = null;
			
		
		public static final String ACCOUNT_USERNAME = "account_username";
	    /** Preference key for account password. */
	    public static final String ACCOUNT_PASSWORD = "account_password";
	    /** Preference key for status (available, busy, away, ...). */
	    public static final String STATUS_KEY = "status";
	    /** Preference key for status message. */
	    public static final String STATUS_TEXT_KEY = "status_text";
	    /** Preference key for connection resource . */
	    public static final String NOTIFICATION_VIBRATE_KEY = "notification_vibrate";
	    /** Preference key for notification sound. */
	    public static final String NOTIFICATION_SOUND_KEY = "notification_sound";
	    /** Preference key for smack debugging. */
	    public static final String CHAT_HISTORY_KEY = "settings_chat_history_path";
		
		private ChatConfig(){super(Constants.CHAT_CONF_FILE);}
		
		public static ChatConfig getInstance()
		{
			if(instance == null)
				instance = new ChatConfig();
			return instance;
			
		}
	}
