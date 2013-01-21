package my.b1701.SB.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import my.b1701.SB.HelperClasses.ThisAppConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.HttpClient.DeleteUserRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.ThisUser;

public class CheckAndDeleteUserRequestService extends IntentService{

    public static final String TAG = "my.b1701.SB.service.CheckAndDeleteUserRequestService";

    public CheckAndDeleteUserRequestService() {
        super("CheckAndDelteUserRequestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SBGeoPoint currGeoPoint = ThisUser.getInstance().getSourceGeoPoint();
        SBGeoPoint shareReqGeoPoint = ThisUser.getInstance().getShareReqGeoPoint();

        Log.i(TAG, "Checking if user out of request area");
        ToastTracker.showToast("checking if user out of req area");
        double deleteReqDist = ThisAppConfig.getInstance().getLong(ThisAppConfig.USERCUTOFFDIST);
        try {
            if (shareReqGeoPoint.distanceFromSBGeoPoint(currGeoPoint) > deleteReqDist) {
                Log.i(TAG,"user out of req area sending delete req to server");
                SBHttpRequest request = new DeleteUserRequest();
                SBHttpClient.getInstance().executeRequest(request);
                Log.i(TAG,"got delete user response ,processing");
            } else {
                Context context = Platform.getInstance().getContext();
                Intent getNearByUsersIntent = new Intent(context, GetNearByUsersService.class);
                context.startService(getNearByUsersIntent);
            }
        } catch (RuntimeException e){
            e.printStackTrace();
            //TODO cancel request?
        }
    }
}
