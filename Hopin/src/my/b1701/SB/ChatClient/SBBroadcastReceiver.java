package my.b1701.SB.ChatClient;

import my.b1701.SB.R;
import my.b1701.SB.ChatService.SBChatService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class SBBroadcastReceiver extends BroadcastReceiver{

	    /** Broadcast intent type. */
	    public static String SBCHAT_CONNECTION_CLOSED = "SBConnectionClosed";
	    public static String SBLOGIN_TO_CHAT = "SBLoginToChatServer";

	   
	    @Override
	    public void onReceive(final Context context, final Intent intent) {
		String intentAction = intent.getAction();
		if (intentAction.equals(SBCHAT_CONNECTION_CLOSED)) {
		    CharSequence message = intent.getCharSequenceExtra("message");
		    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		    if (context instanceof Activity) {
			Activity act = (Activity) context;
			act.finish();
			// The service will be unbinded in the destroy of the activity.
		    }
		} else if (intentAction.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
		    if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
			Toast.makeText(context, context.getString(R.string.NetworkConnectivityLost),
			    Toast.LENGTH_SHORT).show();
			context.stopService(new Intent(context, SBChatService.class));
		    }
		}
	    }
	}



