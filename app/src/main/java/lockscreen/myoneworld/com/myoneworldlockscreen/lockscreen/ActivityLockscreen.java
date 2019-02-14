package lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Browser;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.SWIPE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.freeMemory;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.sendAnalytics;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.setFont;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.getCurrentTime;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.generateErrorLog;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.disableLocksScreen;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.stopListening;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.fileMyStoryId;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.setActivityRunning;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.getFileInFolder;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.WEB_VIEW_SETTING;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MYPHONE_SHOP;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MY_LIFE_URL;
import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.Utility;
import lockscreen.myoneworld.com.myoneworldlockscreen.articles.ActivityArticle;
import lockscreen.myoneworld.com.myoneworldlockscreen.webviews.ActivityWebView;
import okhttp3.internal.Util;

public class ActivityLockscreen extends AppCompatActivity {
    String id;
    public static String article_id;
    public static int ads_count = -1;
    public static int totalSize = -1;
    private boolean isLockOpen;
    private boolean toUnlock;
    private TextView date = null;
    private TextView Time = null;
    private TextView am_pm = null;
    private Context mContext = this;
    ImageView lockscreen_bg = null;
    ViewFlipper flipper = null;
    private static String article_image_url;
    private static String article_story_id;
    private static String adsUrl;
    public static Bitmap bmImg;
    public static int status = 0;
    public static int current = 0;
    private ImageView image;
    private int xDelta;
    private int yDelta;
    private int unlockup;
    private int unlockdown;
    private int unlockleft;
    private int unlockright;
    private ViewGroup mainLayout;
    private View arrowLayout;
    BitmapDrawable background = null;
    private int unlockLogoWidth;
    private int unlockLogoHeigth;
    private ImageView right;
    private ImageView left;
    private ImageView up;
    private ImageView down;
    private ImageView showhide;
    private boolean showTime;
    ViewPager viewPager;
    ArrayList<String> imagesPath;
    private static int SR_CODE = 123;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private boolean mIslistening;
    AlertDialog.Builder articleDetails;
    AlertDialog articleDialog;
    TextView listeningText;
    TextToSpeech textToSpeech;
    TextView title;
    String[] extension;
    Animation rotate;
    Typeface font ;
    public static int fileID;
    ViewPagerAdapter viewPagerAdapter;

    public ActivityLockscreen(String id) {
        this.id = id;
        freeMemory();
    }
    public ActivityLockscreen() {freeMemory();}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        font = setFont(mContext, "font/Roboto-Light.ttf");
        try {
            super.onCreate(savedInstanceState);
            initialLoad();
        }catch (Exception e){
            generateErrorLogs(e, Utility.getCurrentTime());
            initialLoad();
            super.onCreate(savedInstanceState);
        }
    }
    private void init(){
        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            right = this.findViewById(R.id.right);
            left = this.findViewById(R.id.left);
            up = this.findViewById(R.id.up);
            down = this.findViewById(R.id.down);
            image = this.findViewById(R.id.image);
            showhide = this.findViewById(R.id.showhide);
            title = findViewById(R.id.title_lockscreen);
            listeningText = findViewById(R.id.listening);
            mainLayout = (LinearLayout) findViewById(R.id.main);
            image.setOnTouchListener(onTouchListener());
            image.startAnimation(rotate);
            arrowLayout = findViewById(R.id.arrow);
            Time = findViewById(R.id.time_hr);
            date = findViewById(R.id.time_date);
            am_pm = findViewById(R.id.time_a);
            Time.setTypeface(font);
            date.setTypeface(font);
            am_pm.setTypeface(font);
            title.setTypeface(font);
            Time.setVisibility(View.VISIBLE);
            date.setVisibility(View.VISIBLE);
            am_pm.setVisibility(View.VISIBLE);
            showTime = true;

            int height = getResources().getDisplayMetrics().heightPixels / 3;
            int marginTop = getResources().getDisplayMetrics().heightPixels - getResources().getDisplayMetrics().heightPixels / 3;
            int lockheight = getResources().getDisplayMetrics().heightPixels;
            int lockwidht = getResources().getDisplayMetrics().widthPixels;
            int lockdowner = (int) ((lockheight / 100) * 99.8);
            unlockup = (lockheight / 100) * 75;
            unlockdown = lockdowner;
            unlockleft = (lockheight / 100) * 15;
            unlockright = (lockwidht / 100) * 85;

            unlockLogoHeigth = image.getLayoutParams().height = ((lockheight / 100) * 13);
            unlockLogoWidth = image.getLayoutParams().width = ((lockheight / 100) * 13);

            right.getLayoutParams().height = ((lockheight / 100) * 7);
            right.getLayoutParams().width = ((lockheight / 100) * 7);
            left.getLayoutParams().height = ((lockheight / 100) * 7);
            left.getLayoutParams().width = ((lockheight / 100) * 7);
            up.getLayoutParams().height = ((lockheight / 100) * 7);
            up.getLayoutParams().width = ((lockheight / 100) * 7);
            down.getLayoutParams().height = ((lockheight / 100) * 7);
            down.getLayoutParams().width = ((lockheight / 100) * 7);

            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
            param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            param.setMargins(0, marginTop, 0, 0);
            mainLayout.setLayoutParams(
                    new android.widget.RelativeLayout.LayoutParams(param));
            arrowLayout.getLayoutParams().height = height;
            showhide.getLayoutParams().height = (lockheight / 100) * 74;

            String article_path = getValueString("downloaded_ads_" + ads_count, mContext);
            if (!article_path.equals("")) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                background = new BitmapDrawable(BitmapFactory.decodeFile(article_path, options));
            } else {
                ads_count = -1;
                if (!article_path.equals("")) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    background = new BitmapDrawable(BitmapFactory.decodeFile(article_path, options));
//                    lockscreen_bg.setBackgroundDrawable(background);
//                    lockscreen_bg.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            }
            lockscreen_bg.setBackgroundDrawable(background);
            lockscreen_bg.setScaleType(ImageView.ScaleType.FIT_XY);
        } catch (Exception e) {
            generateErrorLogs(e, getCurrentTime());
        }

//    textToSpeech = new TextToSpeech(mContext, status -> {
//        if (status != TextToSpeech.ERROR) {
//            textToSpeech.setLanguage(Locale.ENGLISH);
//            textToSpeech.setSpeechRate(0.8f);
//        }
//    });
//        initVoice();
    }

    private void generateErrorLogs(Exception e, String currentTime) {
        Writer writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String s = writer.toString();
        generateErrorLog(mContext, "err_log_" + currentTime, s);
    }

//    private void initVoice() {
//        if (SpeechRecognizer.isRecognitionAvailable(mContext)) {
//            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);
//            mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
//                    this.getPackageName());
//
//            mSpeechRecognizer.setRecognitionListener(
//                    new SpeechRecognitionListener(mContext,
//                            this,
//                            mSpeechRecognizer,
//                            listeningText,viewPager));
//        } else {
//            mSpeechRecognizer = null;
//        }
//    }



    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                try {
                    final int x = (int) event.getRawX();
                    final int y = (int) event.getRawY();
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:
                            LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams)
                                    view.getLayoutParams();
                            xDelta = x - lParams.leftMargin;
                            yDelta = y - lParams.topMargin;
                            right.setVisibility(View.VISIBLE);
                            left.setVisibility(View.VISIBLE);
                            up.setVisibility(View.VISIBLE);
                            down.setVisibility(View.VISIBLE);
                            break;

                        case MotionEvent.ACTION_UP:
                            LinearLayout.LayoutParams toCenter = new LinearLayout.LayoutParams(unlockLogoWidth, unlockLogoHeigth);
                            toCenter.gravity = Gravity.CENTER;
                            image.setLayoutParams(toCenter);
                            right.setVisibility(View.GONE);
                            left.setVisibility(View.GONE);
                            up.setVisibility(View.GONE);
                            down.setVisibility(View.GONE);
                            if (x + 100 >= unlockright && y - 10 > unlockup && y - 30 < unlockdown) {
                                disableLocksScreen(mContext);
                                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.dropd);
                                mp.start();
                                finish();
                                stopListening(mSpeechRecognizer,listeningText);
                            }
                            if (x <= unlockleft && y - 10 > unlockup && y - 30 < unlockdown) {
                                disableLocksScreen(mContext);
                                try {
//                                    LockscreenDAO lockscreenDAO = new LockscreenDAO(mContext);
                                    ActivityArticle.article_id = fileMyStoryId(viewPager);
                                    Intent articleActivity = new Intent(mContext, ActivityArticle.class);
                                    if ("".equals("article_opened" + article_id) && !"".equals(getValueString("id_platform",mContext))) {
//                                        lockscreenDAO.article_analytics(article_id, "0", "1", "0", PhoneInformation.getDeviceUniqueID());
                                        save("article_opened" + article_id, "opened",mContext);
                                    }
                                    stopListening(mSpeechRecognizer,listeningText);
                                    startActivity(articleActivity);
                                } catch (Exception e) {
                                    Writer writer = new StringWriter();
                                    e.printStackTrace(new PrintWriter(writer));
                                    String s = writer.toString();
                                    Utility.generateErrorLog(mContext, "err_log_" + Utility.getCurrentTime(), s);
                                    finish();
                                    stopListening(mSpeechRecognizer,listeningText);
                                }
                                finish();
                                stopListening(mSpeechRecognizer,listeningText);
                            }
                            if (y - 100 <= unlockup && x > unlockleft && x < unlockright) {
                                disableLocksScreen(mContext);
                                try {
                                    Intent i = getApplicationContext().getPackageManager().getLaunchIntentForPackage("g.mobile.mobile.mybarko.mybarko_g");
                                    getApplicationContext().startActivity(i);
                                    stopListening(mSpeechRecognizer,listeningText);
                                } catch (Exception e) {
                                    Writer writer = new StringWriter();
                                    e.printStackTrace(new PrintWriter(writer));
                                    String s = writer.toString();
                                    Utility.generateErrorLog(mContext, "err_log_" + Utility.getCurrentTime(), s);
                                    if (WEB_VIEW_SETTING.equals("FALSE")) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(MYPHONE_SHOP));
                                        intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
                                        startActivity(intent);
                                        stopListening(mSpeechRecognizer,listeningText);
                                    } else {
                                        ActivityWebView.url = MYPHONE_SHOP;
                                        startActivity(new Intent(mContext, ActivityWebView.class));
                                        finish();
                                        stopListening(mSpeechRecognizer,listeningText);
                                    }
                                }
                                finish();
                                stopListening(mSpeechRecognizer,listeningText);
                            }
                            if (y >= unlockdown && x > unlockleft && x < unlockright) {
                                disableLocksScreen(mContext);
                                if (WEB_VIEW_SETTING.equals("FALSE")) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(MY_LIFE_URL));
                                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
                                    startActivity(intent);
                                    finish();
                                    stopListening(mSpeechRecognizer,listeningText);
                                } else {
                                    ActivityWebView.url = MY_LIFE_URL;
                                    startActivity(new Intent(mContext, ActivityWebView.class));
                                    finish();
                                    stopListening(mSpeechRecognizer,listeningText);
                                }
                            }
                            mainLayout.invalidate();
                            stopListening(mSpeechRecognizer,listeningText);
                            break;

                        case MotionEvent.ACTION_MOVE:
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view
                                    .getLayoutParams();

                            layoutParams.leftMargin = x - xDelta;
                            layoutParams.topMargin = y - yDelta;
                            layoutParams.rightMargin = 0;
                            layoutParams.bottomMargin = 0;
                            view.setLayoutParams(layoutParams);
                            right.setVisibility(View.VISIBLE);
                            left.setVisibility(View.VISIBLE);
                            up.setVisibility(View.VISIBLE);
                            down.setVisibility(View.VISIBLE);
                            if (x + 100 >= unlockright && y - 10 > unlockup && y - 30 < unlockdown) {
                                right.setVisibility(View.GONE);
                            }
                            if (x - 100 <= unlockleft && y - 10 > unlockup && y - 30 < unlockdown) {
                                left.setVisibility(View.GONE);
                            }
                            if (y - 100 <= unlockup && x > unlockleft && x < unlockright) {
                                up.setVisibility(View.GONE);
                            }
                            if (y >= unlockdown && x > unlockleft && x < unlockright) {
                                down.setVisibility(View.GONE);
                            }
                            break;
                    }
                    mainLayout.invalidate();
                    return true;
                } catch (Exception e) {
                    Writer writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    String s = writer.toString();
                    Utility.generateErrorLog(mContext, "err_log_" + Utility.getCurrentTime(), s);
                    return false;
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        if (bmImg != null) {
            bmImg.recycle();
            bmImg = null;
        }
        background = null;
        lockscreen_bg = null;
        image = null;
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
            mSpeechRecognizer.cancel();
        }
        status = 0;
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        setActivityRunning(false);
        finish();
        super.onDestroy();
    }

    private void initialLoad() {
        rotate = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
        LockscreenDAO lockscreenDAO = new LockscreenDAO(mContext);
        lockscreenDAO.getArchivedStory();
        lockscreenDAO.newApiMyStoryaContent();
        getFileInFolder(mContext);
        imagesPath = new Utility().filePath(mContext);
        totalSize = new Utility().fileSize(mContext);
        freeMemory();
        setContentView(R.layout.activity_lockscreen);
        disableLocksScreen(mContext);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        init();
        if (totalSize >= 0) {
            RelativeLayout layout = findViewById(R.id.lockscreen_touch);
            layout.setBackground(null);
            PagerAdapter adapter = new InfinitePagerAdapter(new ViewPagerAdapter(mContext, imagesPath,
                    listeningText, mIslistening, mSpeechRecognizer, mSpeechRecognizerIntent, this));
            viewPager = findViewById(R.id.viewPager);
//            viewPagerAdapter = new ViewPagerAdapter(mContext, imagesPath,
//                    listeningText, mIslistening, mSpeechRecognizer, mSpeechRecognizerIntent, this);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(ads_count);
//                showTitle();
        } else {
            RelativeLayout layout = findViewById(R.id.lockscreen_touch);
            layout.setBackground(getResources().getDrawable(R.drawable.initial_load_mystorya));
        }
    }
    public void startListening1(View v) {
        if (!mIslistening && mSpeechRecognizer != null) {
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            Animation blink = AnimationUtils.loadAnimation(mContext, R.anim.blink_anim);
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

    @Override
    protected void onStart() {
        super.onStart();
        setActivityRunning(true);
    }

}
