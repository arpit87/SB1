package my.b1701.SB.HelperClasses;

import java.util.concurrent.atomic.AtomicBoolean;

import my.b1701.SB.Platform.Platform;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

/**
 * 
 * @author arpit87
 *This class is thread safe,call from anywhere to show and dismiss dialog
 */
public class ProgressHandler {
	ProgressBar progressBar = null;	
	private static ProgressDialog progressDialog = null;
	private static AtomicBoolean isshowing = new AtomicBoolean(false);
	public static void showInfiniteProgressDialoge(final Activity underlying_activity,final String title,final String message)
	{
		if(!isshowing.getAndSet(true))
		{
			Platform.getInstance().getHandler().post((new Runnable(){
				public void run() {						
					progressDialog = ProgressDialog.show(underlying_activity, title, message, true);	
					progressDialog.setCancelable(true);
				}}));
		}
		else
		{
			if(progressDialog!=null)
			{
				progressDialog.setTitle(title);
				progressDialog.setMessage(message);
			}
		}
	}
	
	public static void dismissDialoge()
	{
		if(isshowing.getAndSet(false))
		{
			Platform.getInstance().getHandler().post((new Runnable(){
			public void run() {
				progressDialog.dismiss();						
			}}));
		}
	}
	

}
