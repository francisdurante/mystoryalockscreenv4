package lockscreen.myoneworld.com.myoneworldlockscreen.notification;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import lockscreen.myoneworld.com.myoneworldlockscreen.ApiClass;
import lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences;
import lockscreen.myoneworld.com.myoneworldlockscreen.Utility;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.API_STATUS;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GET_USER_NOTIFICATION_MY_CRAZY_SALE_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GET_USER_NOTIFICATION_MY_CRAZY_SALE_TEST;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MSG_BOX_ERROR;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.getValueString;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.globalMessageBox;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.rePatternDate;

public class NotificationDAO {

    public void getAllUnreadNotificationInMyCrazySale(Context context, String accessToken) {
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        rp.add("showAll","1");
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Authorization", accessToken));
        api.getByUrlHeader(context, API_STATUS.equalsIgnoreCase("LIVE") ? GET_USER_NOTIFICATION_MY_CRAZY_SALE_LIVE : GET_USER_NOTIFICATION_MY_CRAZY_SALE_TEST,
                headers.toArray(new Header[headers.size()]),
                rp,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if(null != response){
                            try {
                                JSONObject serverResponse = new JSONObject(response.toString());
                                JSONArray notifications = serverResponse.getJSONArray("data");
                                for(int x = 0; x < notifications.length(); x++){
                                    String id = notifications.getJSONObject(x).getString("notification_id");
                                    String posted = getValueString("NOTIFICATION_"+id,context);
                                    if(notifications.getJSONObject(x).getInt("is_read") == 0 && !"1".equalsIgnoreCase(posted)){
                                        JSONObject notificationData = notifications.getJSONObject(x).getJSONObject("notification");
                                        String message = notificationData.getString("message");
                                        String datePosted = rePatternDate("MMM dd, yyyy HH:mm",notificationData.getString("created_at"));
                                        String notificationID = notificationData.getString("id");
                                        new Utility().immediateNotification(context,Integer.parseInt(notificationID),message + "\n" + datePosted,1);

                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }
                });
    }

    public void readNotification(Context context, String accessToken, String notifId){
        ApiClass api = new ApiClass();
        JSONObject userProfileParam = new JSONObject();
        StringEntity se = null;
        try {
            userProfileParam.put("is_read", 1);

            se = new StringEntity(userProfileParam.toString());
        } catch (Exception e) {
            globalMessageBox(context,e.getMessage(),MSG_BOX_ERROR.toUpperCase(),MSG_BOX_ERROR);
        }
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Authorization", accessToken));
        api.putByUrlHeader(context, API_STATUS.equalsIgnoreCase("LIVE") ? GET_USER_NOTIFICATION_MY_CRAZY_SALE_LIVE + notifId : GET_USER_NOTIFICATION_MY_CRAZY_SALE_TEST + notifId,
                headers.toArray(new Header[headers.size()]),
                se,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if(null != response){
                            try {
                                JSONObject serverResponse = new JSONObject(response.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
    }

}
