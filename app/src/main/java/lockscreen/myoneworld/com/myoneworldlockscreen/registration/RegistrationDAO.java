package lockscreen.myoneworld.com.myoneworldlockscreen.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
import lockscreen.myoneworld.com.myoneworldlockscreen.ApiClass;
import lockscreen.myoneworld.com.myoneworldlockscreen.Utility;
import lockscreen.myoneworld.com.myoneworldlockscreen.login.ActivityLogin;
import lockscreen.myoneworld.com.myoneworldlockscreen.login.LoginDAO;
import lockscreen.myoneworld.com.myoneworldlockscreen.login.LoginVO;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.ERROR_OCCURED_SIGN_IN;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.FACEBOOK;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GOOGLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.G_VERSION_REGISTRATION_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DEFAULT_BIRTHDAY;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DEFAULT_COUNTRY;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DEFAULT_FIRST_NAME;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DEFAULT_LAST_NAME;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DEFAULT_ADDRESS;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DEFAULT_CONTACT;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.PLEASE_CHECK_CONNECTION;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.REGISTER_SUCCESS;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.TWITTER;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.showTopNotification;

public class RegistrationDAO {

    private TextView notifText;
    public RegistrationDAO(){}
    public RegistrationDAO(TextView textView){
        notifText = textView;
    }
    public void registration(RegistrationVO vo, Context context){
        Utility util = new Utility();
        util.showLoading(context);
        RequestParams rp = new RequestParams();
        ApiClass api = new ApiClass();
        rp.add("first_name", vo.getFirstName());
        rp.add("last_name", vo.getLastName());
        rp.add("email", vo.getEmail());
        rp.add("password", vo.getPassword());
        rp.add("birthday", vo.getBirthday());
        rp.add("country_name", vo.getCountry());
        rp.add("mobile_number", vo.getContact());
        rp.add("address", vo.getAddress());
        rp.put("applications[0]", 2);
        rp.put("applications[1]", 4);
        rp.put("applications[2]", 5);

        api.getByUrl(G_VERSION_REGISTRATION_LIVE,rp,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject serverResp = new JSONObject(response.toString());
                    vo.setRegistrationStatusMessage(serverResp.getString("status"));
                    if("success".equals(vo.getRegistrationStatusMessage())){
                        showTopNotification(context,notifText,REGISTER_SUCCESS);
                        new CountDownTimer(2000,1000){

                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                context.startActivity(new Intent(context,ActivityLogin.class));
                                ((Activity) context).finish();
                            }
                        }.start();
                    }else{
                        util.hideLoading();
                        showTopNotification(context,notifText,vo.getRegistrationStatusMessage());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if(null == errorResponse){
                    showTopNotification(context,notifText,PLEASE_CHECK_CONNECTION);
                    util.hideLoading();
                }else{
                    showTopNotification(context,notifText,ERROR_OCCURED_SIGN_IN);
                }
            }
        });
    }
    public void registration(RegistrationVO registerVO, LoginVO loginVO,Context context,Activity activity){
        RequestParams rp = new RequestParams();
        ApiClass api = new ApiClass();
        switch (loginVO.getLoginPlatform()){
            case FACEBOOK:
                rp.add("facebook_key",loginVO.getSocialId());
                break;
            case GOOGLE:
                rp.add("google_key",loginVO.getSocialId());
                break;
            case TWITTER:
                rp.add("twitter_key",loginVO.getSocialId());
                break;

        }

        String email = registerVO.getEmail();
        String birthday = registerVO.getBirthday().equals("") ? DEFAULT_BIRTHDAY : registerVO.getBirthday();
        String country = registerVO.getCountry().equals("") ? DEFAULT_COUNTRY : registerVO.getCountry();
        String mobileNumber = registerVO.getContact().equals("") ? DEFAULT_CONTACT+registerVO.getSocialID() : registerVO.getContact();
        String address = registerVO.getAddress().equals("") ? DEFAULT_ADDRESS : registerVO.getAddress();
        rp.add("first_name", "".equals(registerVO.getFirstName()) ? "" : DEFAULT_FIRST_NAME);
        rp.add("last_name", "".equals(registerVO.getLastName()) ? "" : DEFAULT_LAST_NAME);
        rp.add("password", registerVO.getPassword());
        rp.add("email", email);
        rp.add("birthday", birthday);
        rp.add("country_name", country);
        rp.add("mobile_number", mobileNumber);
        rp.add("address", address);
        rp.put("applications[0]", 2);
        rp.put("applications[1]", 4);
        rp.put("applications[2]", 5);

        api.getByUrl(G_VERSION_REGISTRATION_LIVE,rp,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    LoginDAO loginDAO = new LoginDAO(context,activity);
                    JSONObject serverResp = new JSONObject(response.toString());
                    registerVO.setRegistrationStatusMessage(serverResp.getString("status"));
                    if(null != loginVO.getLoginPlatform() || !"".equalsIgnoreCase(loginVO.getLoginPlatform())){
                        loginVO.setEmail(loginVO.getSocialId());
                        loginVO.setPassword("DEFAULT_PASSPORT");
                    }else {
                        loginVO.setEmail(email);
                        loginVO.setPassword(registerVO.getPassword());
                    }
                    loginDAO.login(loginVO);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }
}
