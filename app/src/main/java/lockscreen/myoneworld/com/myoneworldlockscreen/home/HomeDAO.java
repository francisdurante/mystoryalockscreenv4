package lockscreen.myoneworld.com.myoneworldlockscreen.home;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;
import lockscreen.myoneworld.com.myoneworldlockscreen.ApiClass;
import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.Utility;
import lockscreen.myoneworld.com.myoneworldlockscreen.editprofile.EditProfileDAO;
import lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen.LockscreenService;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.EXPIRED_LOG_IN;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GET_USER_WALLET_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GET_USER_WALLET_TEST;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GOTHIC_FONT_PATH;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.G_VERSION_LOGGED_IN_TEST;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.LOGIN_EXPIRED_MSG;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MSG_BOX_ERROR;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MSG_BOX_WARNING;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.PHP;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.PHP_CURRENCY_WALLET;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.PHP_SIGN;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.PLEASE_CHECK_CONNECTION;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.POINTS;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.RAFFLE_POINTS_WALLET;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.START;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.STOP;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.WAIT_FOR_A_WHILE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.G_VERSION_LOGGED_IN_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.API_STATUS;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.SEND_LOCATION_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.SEND_LOCATION_TEST;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.globalMessageBox;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.isMyServiceRunning;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.loadProfilePic;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.setFont;
import static lockscreen.myoneworld.com.myoneworldlockscreen.notification.NotificationDAO.getCountUnread;

public class HomeDAO {
    Context context;
    Activity activity;
    public static Location location;
    private static int locationStatus = 0;
    public HomeDAO(){}
    public HomeDAO(Context context, Activity activity){
        this.activity = activity;
        this.context = context;
    }


    public void checkIfValidLogin(String accessToken, DrawerLayout drawerLayout, TextView fullName, TextView email, ImageView profilePic, Button lockscreen){
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        rp.put("Authorization", accessToken);
        List<Header> headers = new ArrayList<Header>();
        lockscreen.setText(WAIT_FOR_A_WHILE);
        lockscreen.setEnabled(false);
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
                            loadProfilePic(context,urlPictureSquare,profilePic,R.drawable.com_facebook_profile_picture_blank_square);
                        }
                        if(!userId.equalsIgnoreCase(getValueString("USER_ID",context))){
                            Utility.globalMessageBox(context,LOGIN_EXPIRED_MSG,EXPIRED_LOG_IN,MSG_BOX_WARNING,new AlertDialog.Builder(context).create());
                        }else{
                            if(user_information.getString("full_name").contains("DEFAULT")
                                    || serverResp.getString("email").contains("DEFAULT")
                                    || user_information.getString("address").contains("DEFAULT")){
                                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                EditProfileDAO dao = new EditProfileDAO();
                                dao.getUserProfile(context,getValueString("ACCESS_TOKEN",context),false,false,false);
                            }
                            else{
                                fullName.setText(getValueString("FULL_NAME",context));
                                email.setText(getValueString("EMAIL",context));
                            }
                        }
                        if (isMyServiceRunning(LockscreenService.class, context)) {
                            lockscreen.setText(STOP);

                        } else {
                            lockscreen.setText(START);
                        }
                        lockscreen.setEnabled(true);
                    } catch (Exception e) {
                        Utility.globalMessageBox(context,e.getMessage(),EXPIRED_LOG_IN,MSG_BOX_WARNING,new AlertDialog.Builder(context).create());

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Utility.globalMessageBox(context,LOGIN_EXPIRED_MSG,EXPIRED_LOG_IN,MSG_BOX_WARNING,new AlertDialog.Builder(context).create());

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Utility.globalMessageBox(context,LOGIN_EXPIRED_MSG,EXPIRED_LOG_IN,MSG_BOX_WARNING,new AlertDialog.Builder(context).create());

                }
            });
        }catch (Exception e){
            Utility.globalMessageBox(context,e.getMessage(),EXPIRED_LOG_IN,MSG_BOX_WARNING,new AlertDialog.Builder(context).create());
            lockscreen.setText(PLEASE_CHECK_CONNECTION);
            lockscreen.setEnabled(false);
        }
    }
    public static void sendLocation(String id, List<Address> addresses, Context context, String accessToken) {
        if(addresses != null) {
            generateJSONForLocation(addresses, context);
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient(true,80,443);
            RequestParams rp = new RequestParams();
            rp.put("id", id);
            rp.put("location", getValueString("UNSENT_LOCATION", context));
            List<Header> headers = new ArrayList<Header>();
            headers.add(new BasicHeader("Authorization", accessToken));

            asyncHttpClient.post(context,API_STATUS.equals("LIVE") ? SEND_LOCATION_LIVE : SEND_LOCATION_TEST,headers.toArray(new Header[headers.size()]), rp,"multipart/form-data", new JsonHttpResponseHandler() {
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
    public void checkNotification(Context context) {
        Timer ayncTimer = new Timer();
        TimerTask timerTaskAsync = new TimerTask() {
            @Override
            public void run() {
                getCountUnread(context,getValueString("ACCESS_TOKEN",context));
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

    public void getUserWallet(Context context, String accessToken, String currency, TextView phpWallet,TextView rafflePoints,View view, Utility util, AlertDialog mDialog) {
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        rp.add("id", getValueString("USER_ID", context));
        rp.add("currency", currency);
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Authorization", accessToken));
//        if(currency.equalsIgnoreCase(POINTS)) {
//            util.showLoading(context);
//        }
        api.getByUrlHeader(context, API_STATUS.equalsIgnoreCase("LIVE") ? GET_USER_WALLET_LIVE : GET_USER_WALLET_TEST,
                headers.toArray(new Header[headers.size()]),
                rp,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if(null != response){
                            try{
                                JSONObject serverResponse = new JSONObject(response.toString());
                                Double points = serverResponse.getDouble("amount");

                                DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                                decimalFormat.setGroupingUsed(true);
                                decimalFormat.setGroupingSize(3);

                                if(PHP.equals(currency)){
                                    phpWallet.setTypeface(setFont(context, GOTHIC_FONT_PATH));
                                    phpWallet.setText(PHP_CURRENCY_WALLET + decimalFormat.format(points));
                                    getUserWallet(context,accessToken,POINTS,phpWallet,rafflePoints,view,util,mDialog);
                                }else{
                                    rafflePoints.setTypeface(setFont(context, GOTHIC_FONT_PATH));
                                    rafflePoints.setText(RAFFLE_POINTS_WALLET + decimalFormat.format(points) + " " + POINTS);
                                    new Utility().showMessageBox(true,view,mDialog);
//                                    util.hideLoading();
                                }
                            }catch (JSONException e){
                                globalMessageBox(context,e.getMessage(),MSG_BOX_ERROR.toUpperCase(),MSG_BOX_ERROR,new AlertDialog.Builder(context).create());
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        globalMessageBox(context,responseString,MSG_BOX_ERROR.toUpperCase(),MSG_BOX_ERROR,mDialog);
                    }
                });
    }

}
