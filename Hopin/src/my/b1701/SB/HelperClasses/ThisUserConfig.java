package my.b1701.SB.HelperClasses;


public class ThisUserConfig extends ConfigBase{
	

	private static ThisUserConfig instance = null;
	
	
	public static final String FBLOGGEDIN = "fbloggedin";
	public static final String FBINFOSENTTOSERVER = "fbinfosent";
	public static final String FBACCESSTOKEN = "fb_access_token";
	public static final String FBACCESSEXPIRES = "fb_excess_expires";
	public static final String USERID = "user_id";
    public static final String USERNAME = "username";
	public static final String FBPICURL = "fb_pic_url";
	public static final String FBUSERNAME = "fb_username";
	public static final String FB_FIRSTNAME = "fb_firstname";
	public static final String FB_LASTNAME = "fb_lastname";
	public static final String FBUID = "fb_user_uid";
	public static final String FBPICFILENAME = "user_fb_pic.bmp";
	public static final String PASSWORD = "password";
	public static final String CHATPASSWORD = "password";
	public static final String CHATUSERID = "chat_userid";
	public static final String IsOfferMode = "offer_mode";
	
	
	private ThisUserConfig(){super(Constants.USER_CONF_FILE);}
	
	public static ThisUserConfig getInstance()
	{
		if(instance == null)
			instance = new ThisUserConfig();
		return instance;
		
	}
}
