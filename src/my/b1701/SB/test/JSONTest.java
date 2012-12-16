package my.b1701.SB.test; 

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONTest {
	public static String jsonStr="{\"NearbyUsers\":[{ \"userid\":\"arpit\",\"latitude\":\"32.456\",\"longitude\":\"50.345\"},{\"userid\":\"rahul\",\"latitude\":\"33.456\",\"longitude\":\"51.345\"}]}";
	static JSONObject  jObj;

	public static JSONObject GetUsersJSONObj(){
		// try parse the string to a JSON object
        try {
            jObj = new JSONObject(jsonStr);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
	return jObj;
	}

}
