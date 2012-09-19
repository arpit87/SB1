package my.b1701.SB.HelperClasses;


public class ThisUserConfig extends ConfigBase{
	
	


	private static ThisUserConfig instance = null;
	
	
	public static final String FBCHECK = "fbcheck";
	public static final String FBACCESSTOKEN = "fb_access_token";
	public static final String FBACCESSEXPIRES = "fb_excess_expires";
	public static final String USERID_STR = "user_id";
	
	
	private ThisUserConfig(){super(Constants.USER_CONF_FILE);}
	
	public static ThisUserConfig getInstance()
	{
		if(instance == null)
			instance = new ThisUserConfig();
		return instance;
		
	}
}
