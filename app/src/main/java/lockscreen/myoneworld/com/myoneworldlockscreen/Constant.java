package lockscreen.myoneworld.com.myoneworldlockscreen;

import android.os.Environment;

public class Constant {
    public static final String API_STATUS = "TEST";
    public static final String G_VERSION_LOGIN_LIVE = "https://api.myoneworld.tech/oauth/token";
    public static final String G_VERSION_LOGGED_IN_LIVE = "https://api.myoneworld.tech/api/user/info";
    public static final String G_VERSION_REGISTRATION_LIVE = "https://api.myoneworld.tech/api/mystory/registration";
    public static final String SEND_LOCATION_LIVE = "https://api.myoneworld.tech/api/mystory/location";
    public static final String MY_STORYA_DELETED_CONTENT = "https://api.myoneworld.tech/api/front/story?archived=1&showAll=1";
    public static final String MYPHONE_SHOP = "https://shop.myphone.com.ph";
    public static final String MY_LIFE = "http://mylifestyle.tech";
    public static final String SEND_COMMENT_LIVE = "https://api.myoneworld.tech/api/mystory/send_comment";
    public static final String GET_COMMENT_STORY_ID_LIVE = "https://api.myoneworld.tech/api/mystory/get_comment";
    public static final String ARTICLE_ANALYTICS = "https://brown.com.ph/api/analytics";

    public static final String G_VERSION_API_KEY = "KB1OD3PeCjInjdmJ13bGJQz3RfvceEItO3RKmLuS";
    public static final String MY_STORYA_API_CONTENT_LIVE = "https://api.myoneworld.tech/api/front/story?current_batch=true&showAll=1";
    public static final String MY_STORYA_SINGLE_CONTENT = "https://api.myoneworld.tech/api/front/story/";
    public static final String ANALYTICS_STORIES_LIVE = "https://api.myoneworld.tech/api/mystory/analytics";
    public static final String ANDROID_PATH = Environment.getExternalStorageDirectory().toString() + "/Android/data/";
    public static final String DEFAULT_BIRTHDAY = "1890-04-11";
    public static final String DEFAULT_COUNTRY = "Philippines";
    public static final String DEFAULT_CONTACT = "DEFAULT_CONTACT_ANDROID";
    public static final String DEFAULT_ADDRESS = "DEFAULT_ADDRESS_ANDROID";
    public static final String DEFAULT_EMAIL_ADDRESS = "DEFAULT_EMAIL_ADDRESS";
    public static final String DEFAULT_FIRST_NAME = "DEFAULT_FIRST_NAME";
    public static final String DEFAULT_LAST_NAME = "DEFAULT_LAST_NAME";
    public static final String DATA_USAGE = "Data charges may apply";
    public static final String PLEASE_WAIT = "Please Wait...";
    public static final long PERIODIC = 9000000; // 15mins total --> 15 mins minimum for jobScheduling
    public static final String CANT_PLAY_ERROR = "Cant Play Video";
    public static final String CANT_PLAY_ERROR_CLOUD = "Cant Load Video, Please Check Connection";


    public static final String CONSUMER_KEY = "F64atV1oWBXFAKhwn6tyktWMA";
    public static final String CONSUMER_SECRET = "FgOylOPUnzV9g063MbvLlA6sTTaHxq0kpPDLUK2CtGuaNcnbyd";


    public static final int JOB_SCHEDULE_ID = 11;

    public static final String STOP = "DISABLE LOCKSCREEN";
    public static final String START = "START LOCKSCREEN";


    public static String WEB_VIEW_SETTING = "TRUE";

    public static final String MISMATCH_PASSWORD = "Mismatch Password";
    public static final String ATLEAST_8_CHARACTERS = "Please Input at least 8 characters for Password";
    public static final String FIRST_NAME_REQUIRED = "First Name Field is Required";
    public static final String PHONE_NUMBER_REQUIRED = "Phone Number Field is Required";
    public static final String LAST_NAME_REQUIRED = "Last Name Field is Required";
    public static final String EMAIL_REQUIRED = "Email Field Address is Required";
    public static final String BIRTHDAY_REQUIRED = "Birthday Field is Required";
    public static final String ADDRESS_RQUIRED = "Address Field is Required";
    public static final String INVALID_EMAIL = "Invalid Email Address"; //@
    public static final String INVALID_DATE = "Invalid date format. Need date format(YYYY-MM-DD)";
    public static final String REGISTER_SUCCESS = "Registered Successful\nRedirecting to login page...";

    public static final String MSG_BOX_WARNING = "warning";
    public static final String MSG_BOX_SUCCESS = "success";
    public static final String MSG_BOX_ERROR = "ERROR";

    //--TEST API--///
    public static final String SEND_LOCATION_TEST= "http://192.168.1.149/myoneworld/public/api/mystory/location";
    public static final String G_VERSION_LOGIN_TEST = "http://192.168.1.149/myoneworld/public/oauth/token";

}
