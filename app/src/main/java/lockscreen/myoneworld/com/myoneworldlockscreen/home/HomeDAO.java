package lockscreen.myoneworld.com.myoneworldlockscreen.home;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;
import lockscreen.myoneworld.com.myoneworldlockscreen.ApiClass;
import lockscreen.myoneworld.com.myoneworldlockscreen.Utility;
import lockscreen.myoneworld.com.myoneworldlockscreen.login.ActivityLoginOptions;

import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.*;

public class HomeDAO {
    Context context;
    Activity activity;
    private static Location location;
    private static int locationStatus = 0;

    public HomeDAO(Context context, Activity activity){
        this.activity = activity;
        this.context = context;
    }


    public void checkIfValidLogin(String accessToken, HomeVO vo){
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        rp.put("Authorization", accessToken);
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Authorization", accessToken));
        try{
            api.getByUrlHeader(context,G_VERSION_LOGGED_IN_LIVE,headers.toArray(new Header[headers.size()]),rp,new JsonHttpResponseHandler(){

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONObject serverResp = new JSONObject(response.toString());
                        JSONObject user_information = serverResp.getJSONObject("user_information");
                        String email = serverResp.getString("email");
                        String fullName = user_information.getString("full_name");
                        String userId = user_information.getString("user_id");

                        vo.setFullName(fullName);
                        vo.setUserID(userId);
                        vo.setEmail(email);

                        save("FULL_NAME", vo.getFullName(), context);
                        save("USER_ID", vo.getUserID(), context);
                        save("EMAIL", vo.getEmail(), context);


                    } catch (JSONException e) {
                        save("FULL_NAME", "", context);
                        save("USER_ID", "", context);
                        save("EMAIL", "", context);
                        context.startActivity(new Intent(context,ActivityLoginOptions.class));
                        activity.finish();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    save("FULL_NAME", "", context);
                    save("USER_ID", "", context);
                    save("EMAIL", "", context);
                    context.startActivity(new Intent(context,ActivityLoginOptions.class));
                    activity.finish();
                }
            });
        }catch (Exception e){

        }
    }
    public static void sendLocation(String id, List<Address> addresses, Context context) {
        if(addresses != null) {
            generateJSONForLocation(addresses, context);
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient(true,80,443);
            RequestParams rp = new RequestParams();
            rp.add("id", id);
            rp.add("location", getValueString("UNSENT_LOCATION", context));

            asyncHttpClient.post(SEND_LOCATION_LIVE , rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONObject serverResp = new JSONObject(response.toString());
                        String status = serverResp.getString("status");
                        if ("success".equals(status)) {
                            Log.d("Send Location", "success");
                            save("UNSENT_LOCATION", "", context);
                        }
                        else if("existing".equals(status))
                        {
                            Log.d("Send Location", status);
                            save("UNSENT_LOCATION", "", context);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public boolean getUseSynchronousMode() {
                    return false;
                }

                @Override
                public void setUseSynchronousMode(boolean useSynchronousMode) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    generateJSONForLocation(addresses, context);
                }
            });
        }else{
            locationStatus = 1; // null get Current location
        }
    }
    public void sendLocationTimer() {
        Timer ayncTimer = new Timer();
        TimerTask timerTaskAsync = new TimerTask() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_DENIED) {
                    if(location != null){
                        try {
                            sendLocation(getValueString("user_id",context),Utility.getCurrentLocation(context,location),context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        ayncTimer.schedule(timerTaskAsync, 0, 5000);
    }
    public static void generateJSONForLocation(List<Address> addresses, Context context) {
        try {
            Locations locationObj = new Locations(addresses.get(0).getAddressLine(0),
                    Double.toString(addresses.get(0).getLongitude()),
                    Double.toString(addresses.get(0).getLatitude()));
            Gson parser = new Gson();
            String json = parser.toJson(locationObj);

            JSONArray unSendLocation = new JSONArray(getValueString("UNSENT_LOCATION", context).equals("") ?
                    "[]" :
                    getValueString("UNSENT_LOCATION", context));
            JSONObject locations = new JSONObject(json);
            if (unSendLocation.length() == 0) {
                unSendLocation.put(0, locations);
            } else {
                for (int x = 0; x < unSendLocation.length(); x++) {
                    String currentLocation = unSendLocation.getString(x);
                    if (currentLocation.equals("")) {
                        unSendLocation.put(x, locations);
                        break;
                    }else {
                        if (!unSendLocation.equals(locations)) {
                            unSendLocation.put(x + 1, locations);
                            break;
                        }
                    }

                }
            }
            save("UNSENT_LOCATION", unSendLocation.toString(), context);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
}
