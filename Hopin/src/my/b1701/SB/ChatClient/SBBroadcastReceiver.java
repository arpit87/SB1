package my.b1701.SB.ChatClient;

import my.b1701.SB.R;
import my.b1701.SB.ChatService.SBChatService;
import my.b1701.SB.HelperClasses.ProgressHandler;
import my.b1701.SB.Server.GetMatchingNearbyUsersResponse;
import my.b1701.SB.Users.CurrentNearbyUsers;
import my.b1701.SB.Users.NearbyUser;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class SBBroadcastReceiver extends BroadcastReceiver{

	    /** Broadcast intent type. */
	    public static String SBCHAT_CONNECTION_CLOSED = "SBConnectionClosed";
	    public static String SBLOGIN_TO_CHAT = "SBLoginToChatServer";

	   
	    @Override
	    public void onReceive(final Context context, final Intent intent) {
	    	ChatWindow thisChatWindow = (ChatWindow)context;
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
			//context.stopService(new Intent(context, SBChatService.class));
		    }
		}
		else if(intentAction.equals(GetMatchingNearbyUsersResponse.NEARBY_USER_UPDATED))
		{
			//this is done to handle if chat comes in from a user who is not yet visible to this user
			//then getnearbyuser called which fires intent after updation and then we proceed in chat
			//updation is in new list and not in current list so we need to call update currenttonew
			thisChatWindow.unregisterReceiver(this);
			if(CurrentNearbyUsers.getInstance().usersHaveChanged())				
				CurrentNearbyUsers.getInstance().updateCurrentToNew();
			NearbyUser thisNearbyUser= CurrentNearbyUsers.getInstance().getNearbyUserWithFBID(thisChatWindow.getParticipantFBID());
			if(thisNearbyUser == null)
			{
				//this can happen if user get a chat after long time n other user has moved out till then
				Toast.makeText(context, "This user has moved out of request area,chat will close",
					    Toast.LENGTH_SHORT).show();
				thisChatWindow.finish();
			}
			else
				thisChatWindow.getParticipantInfoFromFBID(thisChatWindow.getParticipantFBID());			
			ProgressHandler.dismissDialoge();
		}
	    }
	}



