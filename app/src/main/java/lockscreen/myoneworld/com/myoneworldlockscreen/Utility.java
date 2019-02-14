package lockscreen.myoneworld.com.myoneworldlockscreen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.speech.SpeechRecognizer;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import com.facebook.share.model.ShareLinkContent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import lockscreen.myoneworld.com.myoneworldlockscreen.articles.ArticleDAO;
import lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen.LockscreenJobService;
import lockscreen.myoneworld.com.myoneworldlockscreen.notification.service.NotificationAlarmService;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.AUTO_START_MSG_TITLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.CANCEL_BUTTON;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.CONSUMER_KEY;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.CONSUMER_SECRET;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DATA_USAGE_MAY_APPLY_SETTING_TITLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DATA_USAGE_TITLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GOTHIC_FONT_PATH;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.HONOR;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.HONOR_AUTO_START;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.HONOR_AUTO_START_CLASS_NAME;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.LETV;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.LETV_AUTO_START;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.LETV_AUTO_START_CLASS_NAME;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MSG_BOX_SUCCESS;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.NEW_VERSION_TITLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.NO_THANKS_BUTTON;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.OK_BUTTON;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.OPPO;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.OPPO_AUTO_START;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.OPPO_AUTO_START_CLASS_NAME;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.PACKAGE_NAME;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.PLAY_STORE_URL_GENERAL;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.PLAY_STORE_URL_MARKET;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.UPDATE_NOW_BUTTON;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.VIVO;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.VIVO_AUTO_START;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.VIVO_AUTO_START_CLASS_NAME;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.XIAOMI;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.XIAOMI_AUTO_START;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.XIAOMI_AUTO_START_CLASS_NAME;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.ANDROID_PATH;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MSG_BOX_WARNING;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MSG_BOX_ERROR;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;

public class Utility {
    private static AlertDialog mDialog;
    private static ImageView loadingImage;
    static Geocoder geocoder;
    private static ArrayList<String> imagesPath;
    public static boolean isActivityRunning;

    public void showLoading(Context context) {
        Animation anim = AnimationUtils.loadAnimation(context,R.anim.blink_anim);
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView = layoutInflater.inflate(R.layout.loading_layout, null,false);
        loadingImage = inflatedView.findViewById(R.id.image_loading);
        loadingImage.startAnimation(anim);
        mDialog = new AlertDialog.Builder(context).create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setView(inflatedView);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();

    }
    public void hideLoading(){
        mDialog.dismiss();
        loadingImage.clearAnimation();
    }
    public static Typeface setFont(Context context,String path){
        return Typeface.createFromAsset(context.getAssets(), path);
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static void generateErrorLog(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Constant.ANDROID_PATH + context.getPackageName(), "err_log");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssMMddyyyy");
        return sdf.format(new Date());
    }
    public static String getCurrentTime(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    public static List<Address> getCurrentLocation(Context context, Location location) throws IOException {
        List<Address> addresses;
        if(location != null){
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        }else{
            addresses = null;
        }
        return addresses;
    }
    public static boolean isMyServiceRunning(Class<?> serviceClass,Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void stopJobService(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            JobScheduler scheduler = (JobScheduler) context.getSystemService(context.JOB_SCHEDULER_SERVICE);
            scheduler.cancel(Constant.JOB_SCHEDULE_ID);
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void startJobService(Context context) {
        ComponentName jobService = new ComponentName(context, LockscreenJobService.class);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("connection", getValueString("connection",context));
        JobInfo info = new JobInfo.Builder(Constant.JOB_SCHEDULE_ID, jobService)
                .setExtras(bundle)
                .setPersisted(true)
                .setPeriodic(Constant.PERIODIC)
                .build();

        JobScheduler scheduler = (JobScheduler) context.getSystemService(context.JOB_SCHEDULER_SERVICE);
        scheduler.schedule(info);
    }
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel name";
            String description = "Description";
            NotificationChannel channel = new NotificationChannel("NOTIFICATION_CHANNEL", name,NotificationManager.IMPORTANCE_LOW);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            channel.setSound(null,null);
            channel.enableVibration(false);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
    public ArrayList<String> filePath(Context context) {
        File myStory = new File(Constant.ANDROID_PATH + context.getPackageName() + "/mystory/");
        if (myStory.isDirectory()) {
            imagesPath = new ArrayList<String>(Arrays.asList(myStory.list()));

        }
        return imagesPath;
    }
    public int fileSize(Context context) {
        int size = 0;
        File myStory = new File(Constant.ANDROID_PATH + context.getPackageName() + "/mystory/");

        if (myStory.isDirectory()) {
            String[] children = myStory.list();
            size = children.length - 1;
        } else {
           size = -1;
        }
        return size;
    }
    public static void disableLocksScreen(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Activity.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(context.KEYGUARD_SERVICE);
        lock.disableKeyguard();
    }
    public static ArrayList getPhoneNumber(String name, Context context) {
        ArrayList ret ;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " like'%" + name + "'";
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        ret = new ArrayList();
        while(c.moveToNext()){
            String number = c.getString(0);
            ret.add(number);
        }
        c.close();
        if (ret == null)
            ret.add("unsaved");
        return ret;
    }
    public static void stopListening(SpeechRecognizer mSpeechRecognizer, TextView listeningText) {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.cancel();
            listeningText.clearAnimation();
            listeningText.setVisibility(View.GONE);
        }
    }
    public static void startListening(Context context, SpeechRecognizer mSpeechRecognizer, TextView listeningText, boolean mIslistening, Intent mSpeechRecognizerIntent) {
        if (!mIslistening && mSpeechRecognizer != null) {
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            Animation blink = AnimationUtils.loadAnimation(context, R.anim.blink_anim);
            listeningText.startAnimation(blink);
            listeningText.setVisibility(View.VISIBLE);
            new CountDownTimer(5000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    listeningText.clearAnimation();
                    listeningText.setVisibility(View.GONE);
                    mSpeechRecognizer.stopListening();
                    mSpeechRecognizer.cancel();
                }
            }.start();
        }
    }
    public static String fileMyStoryId(ViewPager viewPager) {
        String[] extension = null;
        int position = 0;
        if(imagesPath.size()< viewPager.getCurrentItem()){
            position =  viewPager.getCurrentItem() % imagesPath.size();
        }else{
            position = viewPager.getCurrentItem();
        }
        try {
            String fileName = imagesPath.get(position);
            extension = fileName.split("_");
        } catch (Exception e) {
            e.printStackTrace();
            extension[1] = "0";
        }
        return extension[1];
    }
    public static String fileMyStoryId(int position) {
        String[] extension = null;
        try {
            String fileName = imagesPath.get(position);
            extension = fileName.split("_");
        } catch (Exception e) {
            e.printStackTrace();
            extension[1] = "0";
        }
        return extension[1];
    }
    public static void getFileInFolder(Context context) {
        String path = Environment.getExternalStorageDirectory().toString();
        File directory = new File(path, "Android/data/" + context.getPackageName() + "/mystory");
        File[] files = directory.listFiles();
        if (directory.exists()) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].exists()) {
                    if ("".equals(getValueString("downloaded_ads_" + i, context))) {
                        save("downloaded_ads_" + i, directory + "/" + files[i].getName(),context);
                    }
                } else {
                    save("downloaded_ads_" + i, "",context);
                }
            }
        }
    }
    public static String getConnectionType(Context context) {
        String type = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            type = activeNetwork.getTypeName();
        }
        return type;
    }

    public static void deleteEditedContent(String story_id,Context context,int toDelete){ // 0-cover 1-video
        File main_folder_path = new File(ANDROID_PATH  + context.getApplicationContext().getPackageName() +"/mystory_articles/");
        File myStory = new File(ANDROID_PATH + context.getApplicationContext().getPackageName() + "/mystory/");
        File myStoryContent = new File(ANDROID_PATH + context.getApplicationContext().getPackageName() + "/mystory_articles/article_"+story_id+"/");
        File comicsArticleContent = new File(ANDROID_PATH + context.getApplicationContext().getPackageName() + "/mystory_articles/article_"+story_id+"/story_comics_"+story_id+"/");
        if (myStory.isDirectory() && toDelete == 0)
        {
            String[] children = myStory.list();
            for (int i = 0; i < children.length; i++)
            {
                if(children[i].contains("myStory_"+story_id+"_")) {
                    new File(myStory, children[i]).delete();
//                    Utility.save("image_url_" + story_id, "",mContext);
                }
            }
        }
        if(myStoryContent.isDirectory() && toDelete == 1){
            String[] children = myStoryContent.list();
            for (int i = 0; i < children.length; i++)
            {
                if(children[i].contains("video_"+story_id+"_")) {
                    new File(myStoryContent,children[i]).delete();
//                    Utility.save("video_url_" + story_id, "",mContext);
                }
            }
        }
        if(comicsArticleContent.isDirectory() && toDelete == 2){
            String[] children = comicsArticleContent.list();
            for (int i = 0; i < children.length; i++)
            {
                if(children[i].contains("myStorya_comics_type_"+story_id+"_")) {
                    new File(comicsArticleContent,children[i]).delete();
//                    Utility.save("video_url_" + story_id, "",mContext);
                }
            }
        }
//        if(main_folder_path.isDirectory()){
//            String[] children = main_folder_path.list();
//            for (int i = 0; i < children.length; i++)
//            {
//                if(children[i].contains("image_url_"+story_id)) {
//                    new File(main_folder_path,children[i]).delete();
//                }
//            }
//        }
    }
    public static String filePath(String id,Context context){
        String path="";
        String[] imagesPath = null;
        File myStory = new File(ANDROID_PATH + context.getPackageName() + "/mystory/");
        if (myStory.isDirectory()) {
            imagesPath = myStory.list();
            for (String anImagesPath : imagesPath) {
                if (anImagesPath.contains("_" + id + "_")) {
                    path = Constant.ANDROID_PATH + context.getPackageName() + "/mystory/" + anImagesPath;
                }
            }
        }
        return path;
    }
    public static void showProgressBar(Context context) {
        Animation anim = AnimationUtils.loadAnimation(context,R.anim.blink_anim);
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView = layoutInflater.inflate(R.layout.loading_layout, null,false);
        loadingImage = inflatedView.findViewById(R.id.image_loading);
        loadingImage.startAnimation(anim);
        mDialog = new AlertDialog.Builder(context).create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setView(inflatedView);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();

//        mDialog.setMessage(message);

    }
    public static void hideProgressBar() {
        mDialog.dismiss();
        loadingImage.clearAnimation();
    }
    public static ArrayList<String> filePathComics(String id,Context context){
        ArrayList<String> comicsPathArrayList = new ArrayList<String>();
        File myStory = new File(Constant.ANDROID_PATH + context.getPackageName() + "/mystory_articles/article_" + id + "/story_comics_" + id + "/");
        if (myStory.isDirectory()) {
            String[] comicsImage = myStory.list();
            String[] comicsPath = new String[comicsImage.length];
            for(int x = 0; x < comicsImage.length; x++) {
                comicsPath[x] = Constant.ANDROID_PATH + context.getPackageName() + "/mystory_articles/article_" + id + "/story_comics_" + id + "/" + comicsImage[x];
                comicsPathArrayList.add(comicsPath[x]);
            }
        }
        return comicsPathArrayList;
    }
    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "MM-dd-yy hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static String getDatePostedComputations(String datePosted) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yy hh:mm a");
        String currentDateAndTime = format.format(new Date());
        String datePostedReturn = "";
        Date datePostedVar = null;
        Date currentDateVar = null;
        try {
            datePostedVar = format.parse(datePosted);
            currentDateVar = format.parse(currentDateAndTime);

            // in milliseconds
            long diff =  currentDateVar.getTime() - datePostedVar.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if(diffMinutes > 1) {
                datePostedReturn = datePostedReturn + diffMinutes + "mins ago";
            }if(diffHours > 0){
                if(!datePostedReturn.equals("")) {
                    datePostedReturn =  diffHours + "hrs " + datePostedReturn;
                }else{
                    datePostedReturn = + diffHours + "hrs " + datePostedReturn ;
                }
            }if(diffDays > 0){
                if(!datePostedReturn.equals("")) {
                    datePostedReturn =  diffDays + "d " + datePostedReturn;
                }else{
                    datePostedReturn =  diffDays + "d " + datePostedReturn;
                }
            }else if(diffMinutes <= 1){
                datePostedReturn = "Just now";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datePostedReturn;
    }
    public static void sendAnalytics(Context co,String action, String articleId){
       ArticleDAO articleDAO = new ArticleDAO();
       articleDAO.sendAnalytics(getValueString("USER_ID",co),articleId,action,co);
    }
    public static boolean isActivityRunning() {
        return isActivityRunning;
    }

    public static void setActivityRunning(boolean activityRunning) {
        isActivityRunning = activityRunning;
    }
    public static void makeNotification(String type, String Message, Activity activity) {
        final LinearLayout notif = activity.findViewById(R.id.linear_notif);
        final TextView txtView = activity.findViewById(R.id.notif_message);
        notif.setVisibility(View.VISIBLE);
        txtView.setVisibility(View.VISIBLE);
        Animation myanim = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.bounce);
        notif.setAlpha(1);
        notif.startAnimation(myanim);
        if (type.equals("success")) {
            notif.setBackgroundColor(Color.parseColor("#ff00ff00"));
            txtView.setText(Message);
        } else if (type.equals("error")) {
            notif.setBackgroundColor(Color.parseColor("#ffff0000"));
            txtView.setText(Message);
        }
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                notif.setAlpha(0);
                notif.setVisibility(View.GONE);
                txtView.setVisibility(View.GONE);
            }
        }.start();
    }
    public static boolean isValidBirthday(String birthday) {
        boolean accepted = false;
        final SimpleDateFormat BIRTHDAY_FORMAT_PARSER = new SimpleDateFormat("Y/m/d");
        Calendar calendar = Calendar.getInstance();
        BIRTHDAY_FORMAT_PARSER.setLenient(false);
        try {
            calendar.setTime(BIRTHDAY_FORMAT_PARSER.parse(birthday));
            accepted = true;
        } catch (ParseException e) {
        }
        return accepted;
    }
    public static boolean globalMessageBox(Context context){
        final boolean[] checked = {false};
        final boolean[] response = {false};
        Typeface font = setFont(context,GOTHIC_FONT_PATH);
        if(!"1".equalsIgnoreCase(getValueString("SHOW_POP_UP_DATA_USAGE",context))) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View inflatedView = layoutInflater.inflate(R.layout.pop_up_layout, null,false);
            final TextView dataUsageMessage = inflatedView.findViewById(R.id.data_usage_message);
            final CheckBox showDataUsage = inflatedView.findViewById(R.id.show_data_usage);
            final Button cancel = inflatedView.findViewById(R.id.cancel_show_data);
            final Button ok = inflatedView.findViewById(R.id.ok_show_data);

            dataUsageMessage.setTypeface(font);
            showDataUsage.setTypeface(font);
            ok.setTypeface(font);
            cancel.setTypeface(font);

            showDataUsage.setOnCheckedChangeListener((buttonView, isChecked) -> checked[0] = isChecked);

            cancel.setOnClickListener(v -> {
                Activity activity = (Activity) context;
                activity.finish();
                response[0] = false;
            });
            ok.setOnClickListener(v -> {
                if(checked[0]) {
                    save("SHOW_POP_UP_DATA_USAGE", "1", context);
                }
                else {
                    save("SHOW_POP_UP_DATA_USAGE", "0", context);
                }
               response[0] = true;

                mDialog.dismiss();

            });
            showMessageBox(false,context,inflatedView);
        }else{
           response[0] = true;
        }
        return response[0];
    }
    public static void globalMessageBox(Context context, String Message, String Title, String type){

        Typeface font = setFont(context,GOTHIC_FONT_PATH);
        AlertDialog.Builder ab = new AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle);
        switch (type){
            case MSG_BOX_SUCCESS:
                break;
            case MSG_BOX_WARNING:
                ab.setIcon(R.drawable.ic_warning);
                break;
            case MSG_BOX_ERROR:
                ab.setIcon(R.drawable.ic_cancel);
                break;
        }
        if(Title.equalsIgnoreCase(DATA_USAGE_TITLE)){
            final boolean[] checked = {false};
            if(!"1".equalsIgnoreCase(getValueString("SHOW_POP_UP_DATA_USAGE",context))) {
                LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View inflatedView = layoutInflater.inflate(R.layout.pop_up_layout, null,false);
                final TextView dataUsageMessage = inflatedView.findViewById(R.id.data_usage_message);
                final CheckBox showDataUsage = inflatedView.findViewById(R.id.show_data_usage);
                final Button cancel = inflatedView.findViewById(R.id.cancel_show_data);
                final Button ok = inflatedView.findViewById(R.id.ok_show_data);

                dataUsageMessage.setTypeface(font);
                showDataUsage.setTypeface(font);
                ok.setTypeface(font);
                cancel.setTypeface(font);

                showDataUsage.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    checked[0] = isChecked;
                });

                cancel.setOnClickListener(v -> {
                    Activity activity = (Activity) context;
                    activity.finish();
                });
                ok.setOnClickListener(v -> {
                    if(checked[0])
                        save("SHOW_POP_UP_DATA_USAGE", "1", context);
                    else
                        save("SHOW_POP_UP_DATA_USAGE", "0", context);
                    mDialog.dismiss();
                });
                showMessageBox(false,context,inflatedView);
            }
        }else if(Title.equalsIgnoreCase(NEW_VERSION_TITLE)){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View inflatedView = layoutInflater.inflate(R.layout.message_box_layout, null,false);
            final TextView generalMessage = inflatedView.findViewById(R.id.message_box_message);
            final LinearLayout linearButtons = inflatedView.findViewById(R.id.linear_buttons);
            final Button cancelToSetting = inflatedView.findViewById(R.id.cancel_go_to_settings);
            final Button goToSettings = inflatedView.findViewById(R.id.go_to_settings);
            final TextView title = inflatedView.findViewById(R.id.message_box_title);
            Drawable img = null;
            if(MSG_BOX_WARNING.equalsIgnoreCase(type)){
                img = context.getResources().getDrawable(R.drawable.ic_warning);
                title.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
            }else if(MSG_BOX_ERROR.equalsIgnoreCase(type)){
                img = context.getResources().getDrawable(R.drawable.ic_cancel);
            }
            title.setText(Title);
            title.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
            title.setCompoundDrawablePadding(15);
            linearButtons.setVisibility(View.VISIBLE);
            goToSettings.setText(UPDATE_NOW_BUTTON);
            cancelToSetting.setText(NO_THANKS_BUTTON);
            cancelToSetting.setOnClickListener(v ->{ mDialog.dismiss(); ((Activity)context).finish();});
            goToSettings.setOnClickListener(v -> {
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_URL_MARKET)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_URL_GENERAL)));
                }
            });
            generalMessage.setTypeface(font);
            generalMessage.setText(Message);

            showMessageBox(false,context,inflatedView);

        }else if(Title.equalsIgnoreCase(AUTO_START_MSG_TITLE)) {
            if("".equalsIgnoreCase(getValueString("AUTO_START",context)) && checkManufacturer()) {
                LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View inflatedView = layoutInflater.inflate(R.layout.message_box_layout, null,false);
                final TextView generalMessage = inflatedView.findViewById(R.id.message_box_message);
                final LinearLayout linearButtons = inflatedView.findViewById(R.id.linear_buttons);
                final Button cancelToSetting = inflatedView.findViewById(R.id.cancel_go_to_settings);
                final Button goToSettings = inflatedView.findViewById(R.id.go_to_settings);
                final ImageView autoStartImage = inflatedView.findViewById(R.id.image_auto_start);
                final TextView title = inflatedView.findViewById(R.id.message_box_title);
                Drawable img = null;
                if(MSG_BOX_WARNING.equalsIgnoreCase(type)){
                    img = context.getResources().getDrawable(R.drawable.ic_warning);
                    title.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
                }else if(MSG_BOX_ERROR.equalsIgnoreCase(type)){
                   img = context.getResources().getDrawable(R.drawable.ic_cancel);
                }

                title.setText(Title);
                title.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
                title.setCompoundDrawablePadding(15);
                autoStartImage.setVisibility(View.VISIBLE);
                linearButtons.setVisibility(View.VISIBLE);
                cancelToSetting.setOnClickListener(v -> mDialog.dismiss());
                goToSettings.setOnClickListener(v -> {
                    addAutoStartup(context);
                    save("AUTO_START","1",context);
                    mDialog.dismiss();
                });
                generalMessage.setTypeface(font);
                generalMessage.setText(Message);

                showMessageBox(false,context,inflatedView);
            }
        }
        else {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View inflatedView = layoutInflater.inflate(R.layout.message_box_layout, null,false);
            final TextView generalMessage = inflatedView.findViewById(R.id.message_box_message);
            final TextView title = inflatedView.findViewById(R.id.message_box_title);

            Drawable img = null;
            if(MSG_BOX_WARNING.equalsIgnoreCase(type)){
                img = context.getResources().getDrawable(R.drawable.ic_warning);
                title.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
            }else if(MSG_BOX_ERROR.equalsIgnoreCase(type)){
                img = context.getResources().getDrawable(R.drawable.ic_cancel);
            }

            title.setText(Title);
            title.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
            title.setCompoundDrawablePadding(15);

            generalMessage.setText(Message);
            generalMessage.setTypeface(font);

            showMessageBox(true,context,inflatedView);
            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    mDialog.dismiss();
                }
            }.start();
        }
    }

    private static void addAutoStartup(Context context) {
        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if (XIAOMI.equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName(XIAOMI_AUTO_START, XIAOMI_AUTO_START_CLASS_NAME));
            } else if (OPPO.equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName(OPPO_AUTO_START, OPPO_AUTO_START_CLASS_NAME));
            } else if (VIVO.equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName(VIVO_AUTO_START, VIVO_AUTO_START_CLASS_NAME));
            } else if (LETV.equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName(LETV_AUTO_START, LETV_AUTO_START_CLASS_NAME));
            } else if (HONOR.equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName(HONOR_AUTO_START, HONOR_AUTO_START_CLASS_NAME));
            }else if("HUAWEI".equalsIgnoreCase(manufacturer)){
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                context.startActivity(intent);
            }
        } catch (Exception e) {
        }
    }
    public static void showLoginError(Context context,TextView errorText,String message){
        errorText.setVisibility(View.VISIBLE);
        errorText.setText(message);
        errorText.setTypeface(setFont(context,GOTHIC_FONT_PATH));
        errorText.setTextSize(15);
        errorText.startAnimation(AnimationUtils.loadAnimation(context,R.anim.shake));
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                errorText.clearAnimation();
                errorText.setVisibility(View.GONE);
            }
        }.start();

    }

    private static void showMessageBox(boolean cancelable, Context context, View view){
        mDialog = new AlertDialog.Builder(context).create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setView(view);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.setCancelable(cancelable);
        mDialog.show();
    }
    private static boolean checkManufacturer(){
        boolean autostartAvailable = false;
        String manufacturer = android.os.Build.MANUFACTURER;
        switch (manufacturer){
            case VIVO:
                autostartAvailable = true;
                break;
            case XIAOMI:
                autostartAvailable = true;
                break;
            case OPPO:
                autostartAvailable = true;
                break;
            case LETV:
                autostartAvailable = true;
                break;
            case HONOR:
                autostartAvailable = true;
                break;
        }
        return autostartAvailable;
    }

    public void scheduledNotification(Context context, String date, int id){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date fullDate = null;
        try {
            fullDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int year = fullDate.getYear() + 1900;
        int month = fullDate.getMonth();
        int day = fullDate.getDate();
        int hour = fullDate.getHours();
        int minute = fullDate.getMinutes();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(context, NotificationAlarmService.class)
                .putExtra("NOTIF_ID",id);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MINUTE, minute);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }
    public void immediateNotification(Context context, int id, String Message, int app) {// 1- my|crazysale 2-my|storya 3 - my|lifeStyle 4

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent =  new Intent(context, NotificationAlarmService.class)
                .putExtra("NOTIF_MESSAGE",Message)
                .putExtra("NOTIF_ID", id)
                .putExtra("APP",app);

        PendingIntent broadcast =  PendingIntent.getBroadcast(context, id, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 3);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }

    public static void dataChargesSettingMessageBox(Context context, String message, String Title, Switch[] sw, String type){
        Typeface font = setFont(context,GOTHIC_FONT_PATH);
        if(Title.equalsIgnoreCase(DATA_USAGE_MAY_APPLY_SETTING_TITLE)){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View inflatedView = layoutInflater.inflate(R.layout.message_box_layout, null,false);
            final TextView generalMessage = inflatedView.findViewById(R.id.message_box_message);
            final LinearLayout linearButtons = inflatedView.findViewById(R.id.linear_buttons);
            final Button cancelButton = inflatedView.findViewById(R.id.cancel_go_to_settings);
            final Button okayButton = inflatedView.findViewById(R.id.go_to_settings);
            final ImageView autoStartImage = inflatedView.findViewById(R.id.image_auto_start);
            final TextView title = inflatedView.findViewById(R.id.message_box_title);
            cancelButton.setText(CANCEL_BUTTON);
            okayButton.setText(OK_BUTTON);
            Drawable img = null;
            if(MSG_BOX_WARNING.equalsIgnoreCase(type)){
                img = context.getResources().getDrawable(R.drawable.ic_warning);
                title.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
            }else if(MSG_BOX_ERROR.equalsIgnoreCase(type)){
                img = context.getResources().getDrawable(R.drawable.ic_cancel);
            }

            title.setText(Title);
            title.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
            title.setCompoundDrawablePadding(15);
            linearButtons.setVisibility(View.VISIBLE);
            cancelButton.setOnClickListener(v -> {
                sw[0].setChecked(false);
                save("DO_NOT_DOWNLOAD", "1", context);
                save("WIFI_OR_DATA", "", context);
                save("WIFI_ONLY", "", context);
                mDialog.dismiss();
            });
            okayButton.setOnClickListener(v -> {
                sw[0].setChecked(true);
                sw[1].setChecked(false);
                sw[2].setChecked(false);
                save("WIFI_OR_DATA","1",context);
                save("DO_NOT_DOWNLOAD","",context);
                save("WIFI_ONLY","",context);
                mDialog.dismiss();
            });
            generalMessage.setTypeface(font);
            generalMessage.setText(message);

            showMessageBox(false,context,inflatedView);
        }
    }

    public static String getDataConsumption(Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = manager.getRunningAppProcesses();
        String total="";
        try {
            if(runningApps.size() > 0)
            {
                for (ActivityManager.RunningAppProcessInfo runningApp : runningApps) {
                    if (runningApp.processName.equals(PACKAGE_NAME)) {
                        int app = runningApp.uid;
                        float received = TrafficStats.getUidRxBytes(app);//received amount of each app
                        String totalMB = String.format("%.2f", received * 0.000001);
                        double totalGB = Double.parseDouble(totalMB ) / 1024;
                        total = String.format("%.2f",totalGB) + "GB";
                    }
                }
            }
        }catch (Exception e){
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            String s = writer.toString();
            generateErrorLog(context,"err_log_" + getCurrentTime(),s);
        }
        return total;
    }

    public ShareLinkContent facebookShare(String linkToShare){
       return  new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(linkToShare))
                .build();
    }
    public void twitterShare(String linkToShare, Context context, TwitterSession session){
        if(null == session){
//            System.out.println("test 12312344444");
//            TweetComposer.Builder builder = new TweetComposer.Builder(context)
//                    .text(linkToShare);
//            builder.show();
            authenticateUser(context,linkToShare);
        }else {
            System.out.println("test 123123");
            final Intent intent = new ComposerActivity.Builder(context)
                    .session(session)
                    .text(linkToShare)
                    .createIntent();
            context.startActivity(intent);
        }
    }
    private void authenticateUser(Context context,String linkToShare) {
        TwitterAuthClient client = new TwitterAuthClient();//init twitter auth client
        client.authorize(((Activity)context), new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                //if user is successfully authorized start sharing image
                Toast.makeText(context, "Login successful.", Toast.LENGTH_SHORT).show();
                new Utility().twitterShare(linkToShare,context,twitterSessionResult.data);
            }

            @Override
            public void failure(TwitterException e) {
                //if user failed to authorize then show toast
                Toast.makeText(context, "Failed to authenticate by Twitter. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
