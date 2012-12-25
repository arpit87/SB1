package my.b1701.SB.HelperClasses;

public class ThisAppConfig extends ConfigBase{
	
	private static ThisAppConfig instance = null;
	public static final String USERCUTOFFDIST = "user_cutoff_dist";
	public static final String GPSFREQ = "gps_freq";
	public static final String NETWORKFREQ = "network_freq";
	public static final String USERPOSCHECKFREQ = "user_pos_chk_freq";
	public static final String APPUUID = "uuid";
	
	
	private ThisAppConfig(){super(Constants.APP_CONF_FILE);}
	
	public static ThisAppConfig getInstance()
	{
		if(instance == null)
			instance = new ThisAppConfig();
		return instance;
		
	}
}
