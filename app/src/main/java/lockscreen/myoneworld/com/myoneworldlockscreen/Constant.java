package lockscreen.myoneworld.com.myoneworldlockscreen;

import android.os.Environment;

public class Constant {
    public static final String API_STATUS = "LIVE";
    public static final String G_VERSION_LOGIN_LIVE = "https://api.myoneworld.tech/oauth/token";
    public static final String G_VERSION_LOGGED_IN_LIVE = "https://api.myoneworld.tech/api/user/info";
    public static final String G_VERSION_REGISTRATION_LIVE = "https://api.myoneworld.tech/api/mystory/registration";
    public static final String SEND_LOCATION_LIVE = "https://api.myoneworld.tech/api/mystory/location";
    public static final String MY_STORYA_DELETED_CONTENT = "https://api.myoneworld.tech/api/front/story?archived=1&showAll=1";
    public static final String MYPHONE_SHOP = "https://shop.myphone.com.ph";
    public static final String MY_LIFE_URL = "https://mylifestyle.tech";
    public static final String MY_STORYA_URL = "https://mystorya.tech";
    public static final String SEND_COMMENT_LIVE = "https://api.myoneworld.tech/api/mystory/send_comment";
    public static final String GET_COMMENT_STORY_ID_LIVE = "https://api.myoneworld.tech/api/mystory/get_comment";
    public static final String ARTICLE_ANALYTICS = "https://brown.com.ph/api/analytics";
    public static final String PLAY_STORE_URL_VERSION_CHECKER = "https://play.google.com/store/apps/details?id=com.lockscreen.brown.brownlockscreen&hl=en";
    public static final String PLAY_STORE_URL_MARKET = "market://details?id=com.lockscreen.brown.brownlockscreen";
    public static final String PLAY_STORE_URL_GENERAL = "https://play.google.com/store/apps/details?id=com.lockscreen.brown.brownlockscreen";
    public static final String G_VERSION_API_KEY = "KB1OD3PeCjInjdmJ13bGJQz3RfvceEItO3RKmLuS";
    public static final String MY_STORYA_API_CONTENT_LIVE = "https://api.myoneworld.tech/api/front/story?current_batch=true&showAll=1";
    public static final String MY_STORYA_SINGLE_CONTENT = "https://api.myoneworld.tech/api/front/story/";
    public static final String ANALYTICS_STORIES_LIVE = "https://api.myoneworld.tech/api/mystory/analytics";
    public static final String EDIT_USER_PROFILE_LIVE = "https://api.myoneworld.tech/api/user/";
    public static final String EDIT_PROFILE_PIC_LIVE = "https://api.myoneworld.tech/api/mystory/edit/profile_pic";
    public static final String GET_USER_WALLET_LIVE = "https://api.myoneworld.tech/api/mystory/getwallet";
    public static final String GET_USER_NOTIFICATION_MY_CRAZY_SALE_LIVE = "https://api.myoneworld.tech/api/notification_users/";
    public static final String GET_UNREAD_NOTIFICATION_LIVE = "https://api.myoneworld.tech/api/notification_unread_count";


    public static final String ANDROID_PATH = Environment.getExternalStorageDirectory().toString() + "/Android/data/";
    public static final String DEFAULT_BIRTHDAY = "1890/04/11";
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
    public static final String DO_NOT_SHOW_AGAIN = "Do not show again.";
    public static final String ENABLE_AUTO_START_MSG = "Please enable autostart on my|ONEworld navigation app.";
    public static final String AUTO_START_MSG_TITLE = "Autostart application";
    public static final String CHANGE_PROFILE_PIC_TITLE = "Choose option";
    public static final String EDIT_PROFILE_TITLE = "Edit Profile";
    public static final String SUCCESS_EDIT_PROFILE_MSG = "Your profile was successfully updated.";
    public static final String ERROR_EDIT_PROFILE_MSG = "Something went wrong in updating profile.";
    public static final String EXPIRED_LOG_IN = "Expired Login";
    public static final String LOGGING_OUT_TITLE = "Logging Out";
    public static final String LOGGING_OUT_MESSAGE = "Do you want to logout my|storya?";
    public static final String LOGIN_EXPIRED_MSG = "Your session is invalid or expired.\nPlease log in again";
    public static final String NEW_VERSION_MSG = "New Version is now available in Google Play Store. Please update to continue using the lockscreen.";
    public static final String NEW_VERSION_TITLE = "Application Update";
    public static final String DATA_USAGE_MSG = "Using Mobile Data Connection, may cause data charges";
    public static final String DATA_USAGE_TITLE = "Data Usage";
    public static final String PLEASE_CHECK_CONNECTION = "Please check connection.";
    public static final String ERROR_OCCURED_SIGN_IN = "Error occured while signing in.";
    public static final String LOGOUT = "Log out";
    public static final String LOGOUT_MSG = "Do you want to logout my|storya?";
    public static final String DISABLE_LOCKSCREEN_MSG = "Please disable my|storya lockscreen first to access this settings";
    public static final String DISABLE_LOCKSCREEN_TITLE = "Download settings";
    public static final String ENABLE_WIFI_AND_DATA = "Enabling Mobile data or Wifi setting may apply data charges.\n";
    public static final String ERROR_PLYAING_VIDEO = "Error in Playing Video";
    public static final String DATA_USAGE_MAY_APPLY_SETTING_TITLE = "Data Charges May Apply";
    public static final String EDIT_PROFILE_MESSAGE = "Please fill in the required information.";
    public static final String INCORRECT_PASSWORD = "Incorrect email address or password.";
    public static final String EMAIL_ALREADY_REGISTERED = "This email address was already registered using different social or manual login.";
    public static final String ERROR_OCCURED = "ERROR OCCURED.";

    public static final String CONSUMER_KEY = "F64atV1oWBXFAKhwn6tyktWMA";
    public static final String CONSUMER_SECRET = "FgOylOPUnzV9g063MbvLlA6sTTaHxq0kpPDLUK2CtGuaNcnbyd";


    static final int JOB_SCHEDULE_ID = 11;

    public static final String STOP = "DISABLE LOCKSCREEN";
    public static final String START = "START LOCKSCREEN";


    public static String WEB_VIEW_SETTING = "FALSE";

    public static final String MISMATCH_PASSWORD = "Password mismatch";
    public static final String OLD_PASSWORD_NOT_MATCH = "Your current password is incorrect";
    public static final String ATLEAST_8_CHARACTERS = "Please Input at least 8 characters for Password";
    public static final String FIRST_NAME_REQUIRED = "First Name Field is Required";
    public static final String PHONE_NUMBER_REQUIRED = "Phone Number Field is Required";
    public static final String LAST_NAME_REQUIRED = "Last Name Field is Required";
    public static final String EMAIL_REQUIRED = "Email Field Address is Required";
    public static final String BIRTHDAY_REQUIRED = "Birthday Field is Required";
    public static final String ADDRESS_RQUIRED = "Address Field is Required";
    public static final String INVALID_EMAIL = "Invalid Email Address"; //@
    public static final String INVALID_DATE = "Invalid date format. Need date format(YYYY-MM-DD)";
    public static final String REGISTER_SUCCESS = "Registration Successful!\nYou will now be redirected to login page.";
    public static final String FILL_REQUIRED = "Please fill all the required fields.";
    public static final String NO_PHOTO = "No photo is selected or capture. Please try again.";

    public static final String MSG_BOX_WARNING = "warning";
    public static final String MSG_BOX_SUCCESS = "success";
    public static final String MSG_BOX_ERROR = "error";

    public static final String FACEBOOK = "FACEBOOK";
    public static final String TWITTER = "TWITTER";
    public static final String GOOGLE = "GOOGLE";

    public static final String SETTING_TEXT = "Settings";
    public static final String CLOUD = "cloud";
    public static final String MOBILE = "MOBILE";
    public static final String WIFI = "WIFI";

    //--TEST API--///
    public static final String SEND_LOCATION_TEST= "http://192.168.1.149/myoneworld/public/api/mystory/location";
    public static final String G_VERSION_LOGIN_TEST = "http://192.168.1.149/myoneworld/public/oauth/token";
    public static final String ANALYTICS_STORIES_TEST = "http://192.168.1.149/myoneworld/public/api/mystory/analytics";
    public static final String GET_COMMENT_STORY_ID_TEST = "http://192.168.1.149/myoneworld/public/api/mystory/get_comment";
    public static final String SEND_COMMENT_TEST = "http://192.168.1.149/myoneworld/public/api/mystory/send_comment";
    public static final String EDIT_USER_PROFILE_TEST = "http://192.168.1.149/myoneworld/public/api/user/";
    public static final String G_VERSION_LOGGED_IN_TEST = "http://192.168.1.149/myoneworld/public/api/user/info";
    public static final String EDIT_PROFILE_PIC_TEST = "http://192.168.1.149/myoneworld/public/api/mystory/edit/profile_pic";
    public static final String GET_USER_WALLET_TEST = "http://192.168.1.149/myoneworld/public/api/mystory/getwallet";
    public static final String GET_USER_NOTIFICATION_MY_CRAZY_SALE_TEST = "http://192.168.1.149/myoneworld/public/api/notification_users/";
    public static final String GET_UNREAD_NOTIFICATION_TEST = "http://192.168.1.149/myoneworld/public/api/notification_unread_count";



    static final String UPDATE_NOW_BUTTON = "UPDATE NOW";
    static final String NO_THANKS_BUTTON = "NO THANKS";
    public static final String TWITTER_BUTTON = "Login with Twitter";
    public static final String MYSTORYA_BUTTON = "Login with My|OneWorld";
    public static final String GOOGLE_BUTTON = "Login with Google";
    public static final String YES_BUTTON = "YES";
    public static final String NO_BUTTON = "NO";
    public static final String PLUS_BUTTON = "+";
    public static final String NEGATIVE_BUTTON = "-";
    public static final String OK_BUTTON = "OK";
    public static final String CANCEL_BUTTON = "CANCEL";
    public static final String MYONEWORLD = "my|ONEworld";
    public static final String INTENT_SHARE_TITLE = "Choose app to share";
    public static final String NO_SHARING_APP = "No application applicable to share story.";
    public static final String SHARING_INTENT_TITLE = "Sharing Content";

    public static final String VIDEO_ARTICLE = "Video Article";
    public static final String COMIC_ARTICLE = "Comic Article";
    public static final String ARTICLE_POPUP_TITLE = "Choose Article Type";

    public static final String PACKAGE_NAME = "com.lockscreen.brown.brownlockscreen";
    static final String XIAOMI_AUTO_START = "com.miui.securitycenter";
    static final String OPPO_AUTO_START = "com.coloros.safecenter";
    static final String VIVO_AUTO_START = "com.vivo.permissionmanager";
    static final String LETV_AUTO_START = "com.letv.android.letvsafe";
    static final String HONOR_AUTO_START = "com.huawei.systemmanager";

    public static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
    public static final String GOOGLE_PACKAGE_NAME = "com.google.android.apps.docs";
    public static final String TWITTER_PACKAGE_NAME = "com.twitter.android";
    public static final String INSTAGRAM_PACKAGE_NAME = "com.instagram.android";
    static final String XIAOMI_AUTO_START_CLASS_NAME = "com.miui.permcenter.autostart.AutoStartManagementActivity";
    static final String OPPO_AUTO_START_CLASS_NAME = "com.coloros.safecenter.permission.startup.StartupAppListActivity";
    static final String VIVO_AUTO_START_CLASS_NAME = "com.vivo.permissionmanager.activity.BgStartUpManagerActivity";
    static final String LETV_AUTO_START_CLASS_NAME = "com.letv.android.letvsafe.AutobootManageActivity";
    static final String HONOR_AUTO_START_CLASS_NAME = "com.huawei.systemmanager.optimize.process.ProtectActivity";

    static final String XIAOMI = "xiaomi";
    static final String OPPO = "oppo";
    static final String VIVO = "vivo";
    static final String LETV = "Letv";
    static final String HONOR = "honor";
    static final String HUAWEI = "HUAWEI";

    public static final String GOTHIC_FONT_PATH = "font/Century_Gothic.ttf";
    public static final String GOTHIC_BOLD_FONT_PATH = "font/Gothicbold.TTF";
    public static final String ROBOTO_FONT_PATH = "font/Roboto-Light.ttf";
    public static final String ARBERKLEY_FONT_PATH = "font/ARBERKLEY.ttf";

    public static final String DONE_VIEWED_ARTICLE = "DONE_VIEWED_ARTICLE";
    public static final String SWIPE = "SWIPE";


    public static final int GALLERY = 10001;
    public static final int CAMERA = 10002;
    public static final int REQUEST_CODE_CAMERA = 10003;
    public static final int REQUEST_CODE_READ_STORAGE = 10004;

    public static final String PHP_CURRENCY_WALLET = "PHP WALLET\n";
    public static final String RAFFLE_POINTS_WALLET = "RAFFLE POINTS\n";

    public static final String PHP = "PHP";
    public static final String POINTS = "POINTS";

    public static final String PHP_SIGN = "₱";
}
