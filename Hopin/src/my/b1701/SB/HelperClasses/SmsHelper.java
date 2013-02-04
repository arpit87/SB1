package my.b1701.SB.HelperClasses;

public class SmsHelper{
	
	private static void SmsHelper(){};
	private static SmsHelper instance = new SmsHelper();
	
	public static SmsHelper getInstance()
	{
		return instance;
	}
	
	public void sendSms(String text,String toUserID)
	{
		SMS thisSms = new SMS(text,toUserID);
		//TODO
		//api call to server to send sms
	}
	
	private class SMS{
		String sms_text="";
		String touserID;
		
		SMS(String txt,String userid)
		{
			sms_text=txt;
			touserID=userid;
		}
	}
}
