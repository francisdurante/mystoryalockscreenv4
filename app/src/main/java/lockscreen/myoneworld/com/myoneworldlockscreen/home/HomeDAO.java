package lockscreen.myoneworld.com.myoneworldlockscreen.home;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

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
import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.Utility;
import lockscreen.myoneworld.com.myoneworldlockscreen.editprofile.EditProfileDAO;
import lockscreen.myoneworld.com.myoneworldlockscreen.login.ActivityLoginOptions;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.EXPIRED_LOG_IN;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.G_VERSION_LOGGED_IN_TEST;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.LOGIN_EXPIRED_MSG;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MSG_BOX_WARNING;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.G_VERSION_LOGGED_IN_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.API_STATUS;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.SEND_LOCATION_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.SEND_LOCATION_TEST;

public class HomeDAO {
    Context context;
    Activity activity;
    public static Location location;
    private static int locationStatus = 0;

    public HomeDAO(Context context, Activity activity){
        this.activity = activity;
        this.context = context;
    }


    public void checkIfValidLogin(String accessToken, DrawerLayout drawerLayout, TextView fullName, TextView email, ImageView profilePic){
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        rp.put("Authorization", accessToken);
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Authorization", accessToken));
        try{
            api.getByUrlHeader(context,"LIVE".equalsIgnoreCase(API_STATUS) ? G_VERSION_LOGGED_IN_LIVE : G_VERSION_LOGGED_IN_TEST,headers.toArray(new Header[headers.size()]),rp,new JsonHttpResponseHandler(){

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        String urlPictureSquare = "";
                        JSONObject serverResp = new JSONObject(response.toString());
                        JSONObject user_information = serverResp.getJSONObject("user_information");
                        String userId = user_information.getString("user_id");
                        if(user_information.has("gallery")) {
                            if(!"null".equalsIgnoreCase(user_information.getString("gallery")) ) {
                                JSONObject gallery = user_information.getJSONObject("gallery");
                                if (gallery.has("url_square")) {
                                    urlPictureSquare = user_information.getJSONObject("gallery").getString("url_square");
                                }
                            }
                            loadProfilePic(urlPictureSquare,profilePic);
                        }
                        if(!userId.equalsIgnoreCase(getValueString("USER_ID",context))){
                            Utility.globalMessageBox(context,LOGIN_EXPIRED_MSG,EXPIRED_LOG_IN,MSG_BOX_WARNING);
                        }else{
                            if(getValueString("FULL_NAME",context).contains("DEFAULT") || getValueString("EMAIL",context).contains("DEFAULT")){
                                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                EditProfileDAO dao = new EditProfileDAO();
                                dao.getUserProfile(context,getValueString("ACCESS_TOKEN",context));
                            }
                            else{
                                fullName.setText(getValueString("FULL_NAME",context));
                                email.setText(getValueString("EMAIL",context));
                            }
                        }
                    } catch (Exception e) {
                        Utility.globalMessageBox(context,e.getMessage(),EXPIRED_LOG_IN,MSG_BOX_WARNING);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Utility.globalMessageBox(context,LOGIN_EXPIRED_MSG,EXPIRED_LOG_IN,MSG_BOX_WARNING);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Utility.globalMessageBox(context,LOGIN_EXPIRED_MSG,EXPIRED_LOG_IN,MSG_BOX_WARNING);
                }
            });
        }catch (Exception e){
            Utility.globalMessageBox(context,e.getMessage(),EXPIRED_LOG_IN,MSG_BOX_WARNING);
        }
    }
    public static void sendLocation(String id, List<Address> addresses, Context context, String accessToken) {
        if(addresses != null) {
            generateJSONForLocation(addresses, context);
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient(true,80,443);
            RequestParams rp = new RequestParams();
            rp.add("id", id);
            rp.add("location", getValueString("UNSENT_LOCATION", context));
            List<Header> headers = new ArrayList<Header>();
            headers.add(new BasicHeader("Authorization", accessToken));

            asyncHttpClient.post(API_STATUS.equals("LIVE") ? SEND_LOCATION_LIVE : SEND_LOCATION_TEST, rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONObject serverResp = new JSONObject(response.toString());
                        String status = serverResp.getString("status");
                        if ("success".equals(status)) {
                            save("UNSENT_LOCATION", "", context);
                        }
                        else if("existing".equals(status))
                        {
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
                            sendLocation(getValueString("USER_ID",context),Utility.getCurrentLocation(context,location),context,getValueString("ACCESS_TOKEN",context));
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
                    Double.toString(addresses.get(0).getLatitude()),addresses.get(0).getSubAdminArea());
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

    private void loadProfilePic(String url, ImageView profilePic) {
        if("".equals(url)){
            url = "https://mystorya.com.ph";
        }
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .fit()
                .into(profilePic);
    }
}
