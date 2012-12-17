package my.b1701.SB.HelperClasses;

import my.b1701.SB.Platform.Platform;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogBuilder {
	
	private static AlertDialog alertDialog;
	public static void showOKDialog(final Context context,final String title,final String message)
	{
		Platform.getInstance().getHandler().post((new Runnable(){			

			public void run() {				
				alertDialog = new AlertDialog.Builder(context).create(); 
	    		  alertDialog.setTitle(title);
	    		  alertDialog.setMessage(message);
	    		  alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	                dialog.cancel();
	    	           }
	    	       });
	    		  alertDialog.show();			
			}}));
	}
	
}
