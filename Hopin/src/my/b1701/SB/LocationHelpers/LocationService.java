package my.b1701.SB.LocationHelpers;

/*
 * we are using loacl service in same process same thread assuming it wont do heavy operation but only
 * check if user has moved out of range and cancel its request or update map
 * dont do any intensive task here. Also we are not using alarm manager but timertask, so if android kill
 * our process we could land in problem
 */

import java.util.Timer;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.BroadcastReceivers.KillLocationServiceReceiver;
import my.b1701.SB.HelperClasses.ThisAppConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service{
	
	private static final String TAG ="LocationService";

	private NotificationManager mNM;
    private Timer timer;
    private CheckAndDeleteUserOutOfReqArea CheckAndDeleteUserTask;
    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.request_active;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
    	LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }
    

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Loc service started,Received start id " + startId + ": " + intent);
        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
        CheckAndDeleteUserTask = new CheckAndDeleteUserOutOfReqArea();
        timer = new Timer();
        timer.scheduleAtFixedRate(CheckAndDeleteUserTask, 2*60*1000, ThisAppConfig.getInstance().getLong(ThisAppConfig.USERPOSCHECKFREQ));
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);
        CheckAndDeleteUserTask.cancel();  
        timer.cancel();
        // Tell the user we stopped.
        ToastTracker.showToast("Location servce tracking users stopped");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.request_active);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.searchingbuddies, text,System.currentTimeMillis());

         //The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(this, KillLocationServiceReceiver.class), PendingIntent.FLAG_CANCEL_CURRENT);
     // Hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;

         //Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.request_cancel),
                       text, contentIntent);
        notification.number += 1;
        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }
	
	

}
