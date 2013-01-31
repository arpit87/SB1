package my.b1701.SB.Service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import my.b1701.SB.HttpClient.GetMatchingNearbyUsersRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;

public class GetNearByUsersService extends IntentService {

    private static final String TAG = "my.b1701.SB.Service.GetNearByUsersService";

    public GetNearByUsersService() {
        super("GetNearByUsersService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Fetching nearby users..");
        SBHttpRequest getNearbyUsersRequest = new GetMatchingNearbyUsersRequest();
        SBHttpClient.getInstance().executeRequest(getNearbyUsersRequest);
    }
}
