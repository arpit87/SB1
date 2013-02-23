package my.b1701.SB.HttpClient;

import android.util.Log;
import my.b1701.SB.Server.AddThisUserSrcDstCarPoolResponse;
import my.b1701.SB.Server.ServerConstants;
import my.b1701.SB.Server.ServerResponseBase;
import my.b1701.SB.Users.ThisUser;
import my.b1701.SB.Users.UserAttributes;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class AddThisUserScrDstCarPoolRequest extends SBHttpRequest {
    private final String TAG = "my.b1701.SB.HttpClient.AddThisUserSrcDstRequest";
    public static final String URL = ServerConstants.SERVER_ADDRESS + ServerConstants.REQUESTSERVICE + "/addCarpoolRequest/";

    HttpPost httpQueryAddRequest;
    JSONObject jsonobjAddRequest;
    HttpClient httpclient = new DefaultHttpClient();
    AddThisUserSrcDstCarPoolResponse addThisUserResponse;
    String jsonStr;

    public AddThisUserScrDstCarPoolRequest(String sourceAddress, String destinationAddress) {
        //we will post 2 requests here
        //1)addrequest to add source and destination
        //2) getUsersRequest to get users
        super();
        queryMethod = QueryMethod.Post;
        jsonobjAddRequest = GetServerAuthenticatedJSON();
        httpQueryAddRequest = new HttpPost(URL);
        try {
            populateEntityObject(sourceAddress, destinationAddress);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        StringEntity postEntityAddRequest = null;
        try {
            postEntityAddRequest = new StringEntity(jsonobjAddRequest.toString());
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
        }
        postEntityAddRequest.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        Log.d(TAG, "calling server:" + jsonobjAddRequest.toString());
        httpQueryAddRequest.setEntity(postEntityAddRequest);
    }

    private void populateEntityObject(String sourceAddress, String destinationAddress) throws JSONException {
        jsonobjAddRequest.put(UserAttributes.SHAREOFFERTYPE, ThisUser.getInstance().get_Take_Offer_Type());
        if (sourceAddress == null) {
            jsonobjAddRequest.put(UserAttributes.SRCLATITUDE, ThisUser.getInstance().getSourceGeoPoint().getLatitude());
            jsonobjAddRequest.put(UserAttributes.SRCLONGITUDE, ThisUser.getInstance().getSourceGeoPoint().getLongitude());
            jsonobjAddRequest.put(UserAttributes.SRCLOCALITY, ThisUser.getInstance().getSourceGeoPoint().getSubLocality());
            jsonobjAddRequest.put(UserAttributes.SRCADDRESS, ThisUser.getInstance().getSourceGeoPoint().getAddress());
        } else {
            jsonobjAddRequest.put(UserAttributes.SRCADDRESS, sourceAddress);
        }

        jsonobjAddRequest.put(UserAttributes.DSTADDRESS, destinationAddress);
        jsonobjAddRequest.put(UserAttributes.DATETIME, ThisUser.getInstance().getDateAndTimeOfRequest());
    }

    public ServerResponseBase execute() {
        try {
            response = httpclient.execute(httpQueryAddRequest);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        try {
            jsonStr = responseHandler.handleResponse(response);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        addThisUserResponse = new AddThisUserSrcDstCarPoolResponse(response, jsonStr);
        return addThisUserResponse;
    }
}
