package lockscreen.myoneworld.com.myoneworldlockscreen.editprofile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import lockscreen.myoneworld.com.myoneworldlockscreen.ApiClass;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.API_STATUS;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.EDIT_PROFILE_PIC_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.EDIT_PROFILE_PIC_TEST;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.EDIT_USER_PROFILE_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.EDIT_USER_PROFILE_TEST;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.ERROR_EDIT_PROFILE_MSG;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.G_VERSION_LOGGED_IN_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.G_VERSION_LOGGED_IN_TEST;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MSG_BOX_ERROR;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MSG_BOX_SUCCESS;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.SUCCESS_EDIT_PASSWORD_MSG;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.SUCCESS_EDIT_PROFILE_MSG;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.EDIT_PROFILE_TITLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.getValueString;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.save;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.editProfilePopUp;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.globalMessageBox;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.showChangePasswordPopUp;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.getCountryID;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.showPopUpProfilePicture;

public class EditProfileDAO {

    public void getUserProfile(Context context, String accessToken,boolean isChangePassword, boolean isChangeProfile, boolean fromEditButton) {
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Authorization", accessToken));
        api.getByUrlHeader(context, "LIVE".equalsIgnoreCase(API_STATUS) ? G_VERSION_LOGGED_IN_LIVE : G_VERSION_LOGGED_IN_TEST, headers.toArray(new Header[headers.size()])
                , rp,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject serverResp = new JSONObject(response.toString());
                            JSONObject user_information = serverResp.getJSONObject("user_information");
                            JSONObject countryDetails = user_information.getJSONObject("country");
                            String facebookKey = serverResp.getString("facebook_key").equals("null") ? " " : serverResp.getString("facebook_key");
                            String twitterKey = serverResp.getString("twitter_key").equals("null") ? " " : serverResp.getString("twitter_key");
                            String googleKey = serverResp.getString("google_key").equals("null") ? " " : serverResp.getString("google_key");
                            String decrypt = serverResp.getString("decrypt");
                            String email = serverResp.getString("email").contains("DEFAULT") ? "" : serverResp.getString("email");
                            String firsName = user_information.getString("first_name").contains("DEFAULT") ? "" : user_information.getString("first_name");
                            String lastName = user_information.getString("last_name").contains("DEFAULT") ? "" : user_information.getString("last_name");
                            String address = user_information.getString("address").contains("DEFAULT") ? "" : user_information.getString("address");
                            String mobileNumber = user_information.getString("mobile_number").contains("DEFAULT") ? "" : user_information.getString("mobile_number");
                            String userId = user_information.getString("user_id");
                            String birthday = user_information.getString("birthday");
                            String countryName = countryDetails.getString("name");
                            String dealer = user_information.getString("dealer");
                            String profilePicLink = null;
                            String galleryId = null;
                            if(user_information.has("gallery")) {
                                String gallery = user_information.getString("gallery");
                                if(!"null".equals(gallery)){
                                    JSONObject galleryData = new JSONObject(gallery);
                                    galleryId = galleryData.getString("id");
                                    if (galleryData.has("url_square")) {
                                        profilePicLink = galleryData.getString("url_square");
                                    }
                                }
                            }
                            EditProfileVO vo = new EditProfileVO();
                            vo.setImageProfileUrl(profilePicLink);
                            vo.setFirstName(firsName);
                            vo.setLastName(lastName);
                            vo.setAddress(address);
                            vo.setPhoneNumber(mobileNumber);
                            vo.setEmail(email);
                            vo.setUserProfileID(userId);
                            vo.setBirtday(birthday);
                            vo.setCountry(Integer.toString(getCountryID(countryName,context)));
                            vo.setFacebookKey(facebookKey);
                            vo.setGoogleKey(googleKey);
                            vo.setTwitterKey(twitterKey);
                            vo.setOldPassword(decrypt);
                            vo.setImageId(galleryId);
                            vo.setDealer(dealer);
                            if(isChangeProfile && !isChangePassword){
                                showPopUpProfilePicture(context, vo,new AlertDialog.Builder(context).create());
                            }
                            if(!isChangePassword && !isChangeProfile) {
                                if(!fromEditButton) {
                                    editProfilePopUp(context, new AlertDialog.Builder(context).create(), vo, fromEditButton);
                                }else {
                                    context.startActivity(new Intent(context, ActivityEditProfile.class)
                                            .putExtra("FIRST_NAME", vo.getFirstName())
                                            .putExtra("LAST_NAME", vo.getLastName())
                                            .putExtra("ADDRESS", vo.getAddress())
                                            .putExtra("PHONE_NUMBER", vo.getPhoneNumber())
                                            .putExtra("EMAIL_ADDRESS", vo.getEmail())
                                            .putExtra("USER_PROFILE_ID", vo.getUserProfileID())
                                            .putExtra("BIRTHDAY", vo.getBirthday())
                                            .putExtra("COUNTRY", vo.getCountry())
                                            .putExtra("DEALER", dealer)
                                            .putExtra("GALLERY_ID", vo.getImageId())
                                            .putExtra("FROM_EDIT_BUTTON", fromEditButton));
                                    ((Activity) context).finish();
                                }
                            }
                            if(isChangePassword && !isChangeProfile){
                                vo.setChangePassword(true);
                                showChangePasswordPopUp(context,vo,new AlertDialog.Builder(context).create());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            util.hideLoading();
                        }
                    }


                });

    }

    public void sendEditProfile(Context context,EditProfileVO vo,String accessToken) {
        ApiClass api = new ApiClass();
        JSONObject userProfileParam = new JSONObject();
        StringEntity se = null;
        try {
            userProfileParam.put("email", vo.getEmail());
            userProfileParam.put("first_name", vo.getFirstName());
            userProfileParam.put("last_name", vo.getLastName());
            userProfileParam.put("country_id", Integer.parseInt(vo.getCountry()) + 1);
            userProfileParam.put("birthday", vo.getBirthday().replaceAll("/", "-"));
            userProfileParam.put("address", vo.getAddress());
            userProfileParam.put("mobile_number", vo.getPhoneNumber());
            if(!"".equals(vo.getDealer()) || null != vo.getDealer()) {
                userProfileParam.put("dealer", Integer.parseInt(vo.getDealer()));
            }
            if(!"".equals(vo.getImageId())){
                userProfileParam.put("gallery_id", Integer.parseInt(vo.getImageId()));
            }
            if (vo.isChangePassword()) {
                userProfileParam.put("change_password", 1);
                userProfileParam.put("password", vo.getOldPassword());
                userProfileParam.put("new_password", vo.getNewPassword());
            }
            se = new StringEntity(userProfileParam.toString());
        } catch (Exception e) {
            globalMessageBox(context,e.getMessage(),MSG_BOX_ERROR.toUpperCase(),MSG_BOX_ERROR,new AlertDialog.Builder(context).create());
        }
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Authorization", accessToken));
        api.putByUrlHeader(context, "LIVE".equalsIgnoreCase(API_STATUS) ?
                        EDIT_USER_PROFILE_LIVE + getValueString("USER_ID", context)
                        : EDIT_USER_PROFILE_TEST + getValueString("USER_ID", context),
                headers.toArray(new Header[headers.size()]), se, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject serverResponse = new JSONObject(response.toString());
                            JSONObject data = serverResponse.getJSONObject("data");
                            if (null != data) {
                                String fullName = data.getString("first_name") + " " + data.getString("last_name");
                                String email = data.getString("email");
                                if(!vo.isChangePassword()) {
                                    globalMessageBox(context, SUCCESS_EDIT_PROFILE_MSG, EDIT_PROFILE_TITLE, MSG_BOX_SUCCESS,new AlertDialog.Builder(context).create());
                                }
                                else {
                                    globalMessageBox(context, SUCCESS_EDIT_PASSWORD_MSG, EDIT_PROFILE_TITLE, MSG_BOX_SUCCESS,new AlertDialog.Builder(context).create());
                                }
                                save("FULL_NAME", fullName, context);
                                save("EMAIL", email, context);
                            } else {
                                globalMessageBox(context, ERROR_EDIT_PROFILE_MSG, EDIT_PROFILE_TITLE, MSG_BOX_ERROR,new AlertDialog.Builder(context).create());
                            }
                        } catch (JSONException e) {

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        globalMessageBox(context,responseString,MSG_BOX_ERROR.toUpperCase(),MSG_BOX_ERROR,new AlertDialog.Builder(context).create());
//                                showNotifError(context, ((Activity) context).findViewById(R.id.notif_message), responseString);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        globalMessageBox(context,errorResponse.toString(),MSG_BOX_ERROR.toUpperCase(),MSG_BOX_ERROR,new AlertDialog.Builder(context).create());
                    }
                });


    }

    public void editProfilePic(Context context, String profilePicturePath, String accessToken, EditProfileVO vo){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        try {
            rp.add("folder", "profile");
            rp.add("sellerRequest", "1");
            rp.put("image[]", new File(profilePicturePath),"multipart/form-data","image.jg");
            client.post(context,
                    API_STATUS.equalsIgnoreCase("LIVE") ?
                            EDIT_PROFILE_PIC_LIVE : EDIT_PROFILE_PIC_TEST,
                    rp,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                if (null != response) {
                                    JSONObject serverResponse = new JSONObject(response.toString());
                                    if (serverResponse.has("message")) {
                                        vo.setUploadMessage(serverResponse.getString("message"));
                                        if (statusCode == 200) {
                                            JSONArray imageID = new JSONArray(serverResponse.getString("images"));
                                            vo.setImageId(imageID.getString(0));
                                            sendEditProfile(context, vo,accessToken);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,JSONArray errorResponse) {
                            globalMessageBox(context,errorResponse.toString(),MSG_BOX_ERROR.toUpperCase(),MSG_BOX_ERROR,new AlertDialog.Builder(context).create());
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers,String errorResponse, Throwable throwable) {
                            globalMessageBox(context,errorResponse,MSG_BOX_ERROR.toUpperCase(),MSG_BOX_ERROR,new AlertDialog.Builder(context).create());
                        }
                    });
        } catch (Exception e) {
            globalMessageBox(context,e.getMessage(),MSG_BOX_ERROR.toUpperCase(),MSG_BOX_ERROR,new AlertDialog.Builder(context).create());
        }
    }

}
