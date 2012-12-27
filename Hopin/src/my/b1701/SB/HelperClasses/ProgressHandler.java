package my.b1701.SB.HelperClasses;

import my.b1701.SB.Platform.Platform;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

public class ProgressHandler {
	ProgressBar progressBar = null;	
	private static ProgressDialog progressDialog = null;
	
	public static void showInfiniteProgressDialoge(final Activity underlying_activity,final String text1,final String text2)
	{
		Platform.getInstance().getHandler().post((new Runnable(){
			public void run() {	
				progressDialog = ProgressDialog.show(underlying_activity, text1, text2, true);				
			}}));
		
	}
	
	public static void dismissDialoge()
	{
		Platform.getInstance().getHandler().post((new Runnable(){
		public void run() {				
			progressDialog.dismiss();						
		}}));
	}
	

}
