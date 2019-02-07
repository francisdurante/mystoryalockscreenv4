package lockscreen.myoneworld.com.myoneworldlockscreen.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;
import lockscreen.myoneworld.com.myoneworldlockscreen.ApiClass;
import lockscreen.myoneworld.com.myoneworldlockscreen.Utility;
import lockscreen.myoneworld.com.myoneworldlockscreen.home.ActivityHome;
import lockscreen.myoneworld.com.myoneworldlockscreen.home.HomeVO;
import lockscreen.myoneworld.com.myoneworldlockscreen.registration.RegistrationDAO;
import lockscreen.myoneworld.com.myoneworldlockscreen.registration.RegistrationVO;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.G_VERSION_API_KEY;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.G_VERSION_LOGIN_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.G_VERSION_LOGGED_IN_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DEFAULT_EMAIL_ADDRESS;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.showLoginError;

public class LoginDAO {
    private Context mContext;
    private Activity activity;
    private TextView errorText;

    public LoginDAO(Context context, Activity activity) {
        mContext = context;
        this.activity = activity;
    }
    public LoginDAO(Context context, Activity activity,TextView errorText) {
        mContext = context;
        this.activity = activity;
        this.errorText = errorText;
    }

    public void login(LoginVO vo) {
        Utility loading = new Utility();
        loading.showLoading(mContext);
        ApiClass api = new ApiClass();
        RequestParams param = new RequestParams();
        param.add("username", vo.getEmail());
        param.add("password", vo.getPassword());
        param.put("client_secret", G_VERSION_API_KEY);
        param.put("client_id", 1);
        param.put("grant_type", "password");
        try {
            api.postByUrl(G_VERSION_LOGIN_LIVE, param, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    HomeVO vo = new HomeVO();
                    try {
                        JSONObject serverResp = new JSONObject(response.toString());
                        String accessToken = serverResp.getString("token_type") + " " + serverResp.getString("access_token");
                        userInformationGVersion(accessToken, vo,loading);
                        loading.hideLoading();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    if (null != errorResponse) {
                        try {
                            JSONObject error = new JSONObject(errorResponse.toString());
                            if (error.has("message")) {
                                showLoginError(mContext,errorText,error.getString("message"));
                            } else {
                                showLoginError(mContext,errorText,error.getString("error"));
                            }
                            loading.hideLoading();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.hideLoading();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            loading.hideLoading();
        }
    }

    private void userInformationGVersion(String accessToken, HomeVO vo, Utility util) {
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        rp.put("Authorization", accessToken);
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Authorization", accessToken));
        api.getByUrlHeader(mContext, G_VERSION_LOGGED_IN_LIVE, headers.toArray(new Header[headers.size()])
                , rp,
                new JsonHttpResponseHandler() {
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

                            save("FULL_NAME", vo.getFullName(), mContext);
                            save("USER_ID", vo.getUserID(), mContext);
                            save("EMAIL", vo.getEmail(), mContext);
                            save("ACCESS_TOKEN",accessToken,mContext);

                            util.hideLoading();
                            mContext.startActivity(new Intent(mContext, ActivityHome.class));
                            activity.finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            util.hideLoading();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        if (null != errorResponse) {
                            JSONObject error = null;
                            try {
                                error = new JSONObject(errorResponse.toString());
                                if (error.has("message")) {
                                    Toast.makeText(mContext, error.getString("message"), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(mContext, error.getString("error"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void facebookLogin(LoginButton facebookButton, CallbackManager callbackManager, LoginVO vo) {
        facebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        (object, response) -> {
                            try {
                                RegistrationVO rVO = new RegistrationVO();
                                RegistrationDAO registrationDAO = new RegistrationDAO();
                                vo.setSocialId(object.getString("id"));
                                vo.setFirstName(object.getString("first_name"));
                                vo.setLastName(object.getString("last_name"));
                                vo.setLoginPlatform("FACEBOOK");
                                if(object.has("email")){
                                    vo.setEmail(object.getString("email"));
                                    rVO.setPassword(vo.getEmail() + vo.getSocialId());
                                }else{
                                    vo.setEmail(DEFAULT_EMAIL_ADDRESS + vo.getSocialId());
                                    rVO.setPassword(DEFAULT_EMAIL_ADDRESS + vo.getSocialId());
                                }
                                rVO.setSocialID(vo.getSocialId());
                                rVO.setFirstName(vo.getFirstName());
                                rVO.setLastName(vo.getLastName());
                                rVO.setEmail(vo.getEmail());
                                rVO.setBirthday("");
                                rVO.setAddress("");
                                rVO.setContact("");
                                rVO.setCountry("");
                                registrationDAO.registration(rVO,vo,mContext,activity);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void googleSignIn(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            RegistrationDAO registrationDAO = new RegistrationDAO();
            RegistrationVO Rvo = new RegistrationVO();
            LoginVO vo = new LoginVO();
            assert account != null;

            vo.setSocialId(account.getId());
            vo.setFirstName(account.getGivenName());
            vo.setLastName(account.getFamilyName());
            vo.setEmail(account.getEmail());
            vo.setLoginPlatform("GOOGLE");

            Rvo.setPassword(vo.getEmail() + vo.getSocialId());
            Rvo.setEmail(vo.getEmail());
            Rvo.setSocialID(vo.getSocialId());
            Rvo.setFirstName(vo.getFirstName());
            Rvo.setLastName(vo.getLastName());

            Rvo.setBirthday("");
            Rvo.setAddress("");
            Rvo.setContact("");
            Rvo.setCountry("");

            registrationDAO.registration(Rvo,vo,mContext,activity);
        } catch (ApiException e) {
            switch (e.getStatusCode()) {
                case GoogleSignInStatusCodes.CANCELED:
//                    setButtonEnable(true);
                    break;
                case GoogleSignInStatusCodes.NETWORK_ERROR:
//                    setButtonEnable(true);
                    Toast.makeText(mContext,"NETWORK_ERROR",Toast.LENGTH_SHORT).show();
                    break;
                case GoogleSignInStatusCodes.SIGN_IN_CANCELLED:
//                    setButtonEnable(true);
                    break;
                case GoogleSignInStatusCodes.ERROR:
//                    setButtonEnable(true);
                    Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    void twitterLogin(TwitterSession session, Result<TwitterSession> result){
        LoginVO vo = new LoginVO();
        final TwitterSession twitterSession = result.data;
        TwitterAuthClient twitterAuthClient = new TwitterAuthClient();
        twitterAuthClient.requestEmail(twitterSession, new com.twitter.sdk.android.core.Callback<String>() {

            @Override
            public void success(Result<String> result) {
                String email = "";
                RegistrationDAO registrationDAO = new RegistrationDAO();
                RegistrationVO rVo = new RegistrationVO();
                email = result.data;
                vo.setEmail(email);
                vo.setSocialId(Long.toString(session.getId()));
                vo.setLoginPlatform("TWITTER");
                String password = "".equals(vo.getEmail()) ? DEFAULT_EMAIL_ADDRESS + vo.getSocialId() : vo.getEmail()+vo.getSocialId();
                String emailToRegister = "".equals(vo.getEmail()) ? DEFAULT_EMAIL_ADDRESS + vo.getSocialId() : vo.getEmail();
                rVo.setEmail(emailToRegister);
                rVo.setPassword(password);
                rVo.setSocialID(vo.getSocialId());
                rVo.setBirthday("");
                rVo.setAddress("");
                rVo.setContact("");
                rVo.setCountry("");
                registrationDAO.registration(rVo,vo,mContext,activity);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(mContext,exception.getMessage(),Toast.LENGTH_SHORT).show();
                TwitterCore.getInstance().getSessionManager().clearActiveSession();
            }
        });

    }

    public void checkIfValidLogin(String accessToken, HomeVO vo){
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        rp.put("Authorization", accessToken);
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Authorization", accessToken));
        try{
            api.getByUrlHeader(mContext,G_VERSION_LOGGED_IN_LIVE,headers.toArray(new Header[headers.size()]),rp,new JsonHttpResponseHandler(){

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

                        save("FULL_NAME", vo.getFullName(), mContext);
                        save("USER_ID", vo.getUserID(), mContext);
                        save("EMAIL", vo.getEmail(), mContext);
                        mContext.startActivity(new Intent(mContext,ActivityHome.class));
                        activity.finish();

                    } catch (JSONException e) {
                        save("FULL_NAME", "", mContext);
                        save("USER_ID", "", mContext);
                        save("EMAIL", "", mContext);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                }
            });
        }catch (Exception e){

        }
    }
}
