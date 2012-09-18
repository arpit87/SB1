package my.b1701.SB.HelperClasses;


public class ThisUserConfig extends ConfigBase{
	
	private static ThisUserConfig instance = null;
	
	
	public static String FBCHECK = "fbcheck";
	public static String FBACCESSTOKEN = "fb_access_token";
	public static String FBACCESSEXPIRES = "fb_excess_expires";
	
	
	private ThisUserConfig(){super(Constants.USER_CONF_FILE);}
	
	public static ThisUserConfig getInstance()
	{
		if(instance == null)
			instance = new ThisUserConfig();
		return instance;
		
	}
}
