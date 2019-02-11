package lockscreen.myoneworld.com.myoneworldlockscreen.articles;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.google.android.gms.analytics.Tracker;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.ARTICLE_POPUP_TITLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.CLOUD;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.COMIC_ARTICLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DATA_USAGE_MSG;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DATA_USAGE_TITLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.ERROR_PLYAING_VIDEO;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.FACEBOOK_PACKAGE_NAME;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GOOGLE_PACKAGE_NAME;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GOTHIC_BOLD_FONT_PATH;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GOTHIC_FONT_PATH;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.INSTAGRAM_PACKAGE_NAME;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.INTENT_SHARE_TITLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MOBILE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MYONEWORLD;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.NO_SHARING_APP;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.OK_BUTTON;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.SHARING_INTENT_TITLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.TWITTER_PACKAGE_NAME;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.VIDEO_ARTICLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.WIFI;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.filePath;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.freeMemory;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.getConnectionType;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.sendAnalytics;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.isNetworkAvailable;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.filePathComics;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.generateErrorLog;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.globalMessageBox;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.getCurrentTime;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.PLEASE_WAIT;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DATA_USAGE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.CANT_PLAY_ERROR_CLOUD;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.CANT_PLAY_ERROR;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MSG_BOX_WARNING;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.setFont;

import lockscreen.myoneworld.com.myoneworldlockscreen.AnalyticsApplication;
import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.Utility;

public class ActivityArticle extends AppCompatActivity {
    private Context mContext = this;
    public static String article_id;
    private VideoView videoView;
    private Tracker mTracker;
    private ProgressDialog mDialog;
    private ImageView initial;
    static ArrayList<String> imageUrl;
    static ArrayList<String> comicsPathArrayList;
    private ViewPager viewPager;
    private ImageButton textComment;
    private ImageButton shareButton;
    private ImageButton likeButton;
    private LinearLayout commentThings;
    private  LinearLayout likeAnimation;
    private boolean tempLikeStatus = false;
    private Animation rotate;
    private PopupWindow popWindow;
    private int videoStopped = 0;
    private int shared = 0;
    private TextView topMessage;
    private TextView midMessage;
    private TextView botMessage;
    RelativeLayout afterVideoLayout;
    public static boolean dataUsageConfirm = false;
    private AlertDialog popUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(shared != 1) {
            setContentView(R.layout.activity_article);
            rotate = AnimationUtils.loadAnimation(mContext, R.anim.rotation_fast);
            initial = findViewById(R.id.initial_page);
            BitmapDrawable initialBackground = new BitmapDrawable(filePath(article_id, mContext));
            initial.setBackground(initialBackground);
            initial.bringToFront();
            textComment = findViewById(R.id.text_comment);
            likeButton = findViewById(R.id.like);
            shareButton = findViewById(R.id.share);
            likeAnimation = findViewById(R.id.heart_anim_linear);
            textComment.setOnClickListener(this::onShowPopup);
            shareButton.setOnClickListener(v -> dialogShare());
            likeButton.setOnClickListener(v -> {
                clickLikeButton(tempLikeStatus);
                tempLikeStatus = !tempLikeStatus;
            });
//            bringToFrontLayout();
            AnalyticsApplication application = (AnalyticsApplication) getApplication();
            mTracker = application.getDefaultTracker();
            videoView = findViewById(R.id.flipper);
            if ("video_with_slide_show".equals(getValueString("article_kind_" + article_id, mContext))) {
                String[] articleType = {VIDEO_ARTICLE, COMIC_ARTICLE};

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(ARTICLE_POPUP_TITLE);
                builder.setIcon(mContext.getResources().getDrawable(R.drawable.m1w_logo));
                builder.setCancelable(false);
                builder.setItems(articleType, (dialog, which) -> {
                    if (VIDEO_ARTICLE.equals(articleType[which])) {
                        chooseOptionArticle("video");
                    } else if (COMIC_ARTICLE.equals(articleType[which])) {
                        chooseOptionArticle("slide_show");
                    }
                });
                builder.show();
            } else {
                chooseOptionArticle(getValueString("article_kind_" + article_id, mContext));
            }
            super.onCreate(savedInstanceState);
        }else{
            videoView.stopPlayback();
        }
    }

    private void dialogShare(){
        String message = "https://content.mystorya.tech/story/" + article_id +"?shared="+getValueString("USER_ID",mContext);
        List<Intent> targetShareIntents=new ArrayList<Intent>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        List<ResolveInfo> resInfos = getPackageManager().queryIntentActivities(shareIntent, 0);
        if(!resInfos.isEmpty()){
            for(ResolveInfo resInfo : resInfos){
                String packageName=resInfo.activityInfo.packageName;
                if(
                        packageName.contains(GOOGLE_PACKAGE_NAME) ||
                                packageName.contains(FACEBOOK_PACKAGE_NAME) ||
                                packageName.contains(INSTAGRAM_PACKAGE_NAME) ||
                                packageName.contains(TWITTER_PACKAGE_NAME)){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, message);
                    intent.putExtra(Intent.EXTRA_SUBJECT, MYONEWORLD);
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if(!targetShareIntents.isEmpty()){
                Intent chooserIntent=Intent.createChooser(targetShareIntents.remove(0), INTENT_SHARE_TITLE);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                startActivityForResult(chooserIntent,1010);
            }else{
                Utility.globalMessageBox(mContext,NO_SHARING_APP,SHARING_INTENT_TITLE,MSG_BOX_WARNING);
                finish();
            }
        }
    }
    private void clickLikeButton(boolean liked){
        //api for like
        if(!liked){
            likeButton.setImageResource(R.drawable.ic_like___colored);
            Animation like = AnimationUtils.loadAnimation(this,R.anim.zoomin);
            likeAnimation.setAnimation(like);
            likeAnimation.setVisibility(View.VISIBLE);
//            commentThings.setVisibility(View.GONE);
            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    likeAnimation.setVisibility(View.GONE);
                    likeAnimation.clearAnimation();
                }
            }.start();
        }else{
            likeButton.setImageResource(R.drawable.ic_heart);
        }
    }

    private void chooseOptionArticle(String type) {
        Utility util = new Utility();
        if ("video".equals(type)) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoView.getLayoutParams();
            params.width = metrics.widthPixels;
            params.height = metrics.heightPixels;
            params.leftMargin = 0;
            videoView.setLayoutParams(params);
            try {
               String path = "";
                if (!getValueString("DO_NOT_DOWNLOAD",mContext).equals("1")) { // downloaded video played
                    String finalPath = path;
                    videoView.setOnErrorListener((mp, what, extra) -> {
                        videoStopped = mp.getCurrentPosition();
                        videoView.pause();
                        util.showLoading(mContext);
                        videoView.setVideoPath(finalPath);
                        videoView.seekTo(videoStopped);
                        return true;
                    });
                    videoView.setOnCompletionListener(mp -> {
                        if (!getValueString("FULL_NAME",mContext).equals("")) {
                            bringToFrontLayout();
                            sendAnalytics(mContext,article_id);
                        } else {
                            finish();
                        }
                    });
                    if (!videoView.isPlaying()) {
                        path = Environment.getExternalStorageDirectory().toString() + "/Android/data/" + mContext.getPackageName() + "/mystory_articles/article_" + article_id + "/video_" + article_id + "_.mp4";
                        videoView.setVideoPath(path);
                        initial.setVisibility(View.GONE);
                        videoView.start();
                        util.hideLoading();
                    }
                    videoView.requestFocus();
                    videoView.setOnPreparedListener(mp -> {
                        mp.setLooping(false);
                        mp.start();
                    });
                } else {// cloud video played
                    if (!isNetworkAvailable(mContext)) { // error no connection
                        errorOnPlayingVideo(CLOUD);
                    }
                    else {
                        try { // cloud loading
                            util.showLoading(mContext);
                            if (!videoView.isPlaying()) {
                                if(!MOBILE.equalsIgnoreCase(getConnectionType(mContext))) {
                                    initialLoadingVideo(util);
                                }else{
                                    globalMessageBox(mContext);
                                }
                            } else {
                                videoView.pause();
                                util.showLoading(mContext);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("slide_show".equals(type)) {
            try {
                if ("1".equals(getValueString("DO_NOT_DOWNLOAD", mContext))) {
                    ArticleDAO articleDAO = new ArticleDAO();
                    viewPager = findViewById(R.id.view_pager_article);
                    articleDAO.getComicsTypeImage(article_id, mContext,initial,textComment,likeButton,
                            shareButton,likeAnimation,viewPager,this,commentThings,afterVideoLayout);
                }else{ // downloaded comics
                    filePathComics(article_id,mContext);
                    if (!comicsPathArrayList.isEmpty()) {
                        util.hideLoading();
                        initial.setVisibility(View.GONE);
                        viewPager = findViewById(R.id.view_pager_article);
                        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext, comicsPathArrayList,2);
                        viewPager.setAdapter(viewPagerAdapter);
                        viewPager.setCurrentItem(0);
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            boolean lastPageChange = false;

                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                int lastIdx = viewPagerAdapter.getCount() - 1;
                                if (position == lastIdx) {
                                    bringToFrontLayout();
                                    sendAnalytics(mContext,article_id);
                                }
                            }

                            @Override
                            public void onPageSelected(int position) {
                                viewPager.setCurrentItem(position);
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                int lastIdx = viewPagerAdapter.getCount() - 1;

                                int curItem = viewPager.getCurrentItem();
                                if (curItem == lastIdx && state == 1) {
                                    lastPageChange = true;
                                } else {
                                    lastPageChange = false;
                                }
                            }
                        });
                    }
                }
            } catch (Exception e) {
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                String s = writer.toString();
                generateErrorLog(mContext, "err_log_" + getCurrentTime(), s);
                finish();
                util.hideLoading();
            }
        }
    }
    private void errorOnPlayingVideo(final String setting) {
        videoView.setOnErrorListener((mp, what, extra) -> {
            String message = CANT_PLAY_ERROR;
            if(CLOUD.equals(setting))message = CANT_PLAY_ERROR_CLOUD;
            AlertDialog.Builder alertDialogBuilder =
                    new AlertDialog.Builder(ActivityArticle.this)
                            .setTitle(ERROR_PLYAING_VIDEO)
                            .setMessage(message)
                            .setPositiveButton(OK_BUTTON, (dialog, which) -> finish());
            AlertDialog alertDialog = alertDialogBuilder.show();
            return true;
        });
    }

    public void onShowPopup(View v) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate the custom popup layout
        final View inflatedView = layoutInflater.inflate(R.layout.comment_box_layout, null, false);
        ImageView loading = inflatedView.findViewById(R.id.loading_comment);
        ImageButton send = inflatedView.findViewById(R.id.send_comment);
        EditText comment = inflatedView.findViewById(R.id.writeComment);
        loading.startAnimation(rotate);
        ListView listView = (ListView) inflatedView.findViewById(R.id.commentsListView);
        send.setOnClickListener(v1 -> {
            ArticleDAO articleDAO = new ArticleDAO();
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            articleDAO.sendComment(article_id, getValueString("USER_ID", mContext), comment.getText().toString(), mContext,listView,loading,this);
            comment.setText("");
        });
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        int mDeviceHeight = size.y;
        setSimpleList(listView, loading);


        // set height depends on the device size
        popWindow = new PopupWindow(inflatedView, size.x - 50, mDeviceHeight > 1500 ? mDeviceHeight - 800 : mDeviceHeight - 600, true);
        // set a background drawable with rounders corners
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.comment_box_bg));
        // make it focusable to show the keyboard to enter in `EditText`
        popWindow.setFocusable(true);
        // make it outside touchable to dismiss the popup window
        popWindow.setOutsideTouchable(true);

        // show the popup at bottom of the screen and set some margin at bottom ie,
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0, 100);

//        popWindow.setOnDismissListener(() -> commentThings.setVisibility(View.VISIBLE));
    }
    @SuppressLint("ClickableViewAccessibility")
    void setSimpleList(ListView listView, ImageView loading) {
        ArticleDAO articleDAO = new ArticleDAO();
        articleDAO.getCommentByStoryId(article_id,mContext,listView,loading,this);
        listView.setOnTouchListener(new View.OnTouchListener() {
            float height;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                float height = event.getY();
                if (action == MotionEvent.ACTION_DOWN) {
                    this.height = height;
                } else if (action == MotionEvent.ACTION_UP) {
                    if (this.height + 300 < height) {
                        loading.setAnimation(rotate);
                        loading.setVisibility(View.VISIBLE);
                        setSimpleList(listView, loading);
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1010) {
            videoView.stopPlayback();
            if (resultCode == RESULT_OK) {
//                finish();
            }else if (resultCode == RESULT_CANCELED){
               shared = 1;
            }
        }
    }

    public void bringToFrontLayout(){
        afterVideoLayout = findViewById(R.id.after_video_layout);
        topMessage = findViewById(R.id.top_title_after_video);
        midMessage = findViewById(R.id.mid_message);
        botMessage = findViewById(R.id.bot_message);
        TextView footerMessage = findViewById(R.id.footer_message);
        Typeface font = setFont(mContext,GOTHIC_FONT_PATH);
        Typeface bold = setFont(mContext,GOTHIC_BOLD_FONT_PATH);
        afterVideoLayout.bringToFront();
        afterVideoLayout.setVisibility(View.VISIBLE);
        topMessage.setTypeface(bold);
        midMessage.setTypeface(font);
        botMessage.setTypeface(font);
        footerMessage.setTypeface(font);
        if(null != videoView) {
            videoView.stopPlayback();
            videoView.suspend();
        }
    }

    @Override
    protected void onDestroy() {
        freeMemory();
        videoView = null;
        initial = null;
        rotate = null;
        textComment = null;
        likeButton = null;
        shareButton = null;
        likeAnimation = null;
        afterVideoLayout = null;
        topMessage = null;
        midMessage = null;
        botMessage = null;
        finish();
        super.onDestroy();
    }

    private void initialLoadingVideo(Utility util){
        if ("".equals(getValueString("video_url_download_" + article_id, mContext))) {
            Uri uri = Uri.parse(getValueString("video_url_" + article_id, mContext));
            videoView.setVideoURI(uri);
            videoView.setOnCompletionListener(mp -> {
                if (!getValueString("FULL_NAME", mContext).equals("")) {
                    bringToFrontLayout();
                    sendAnalytics(mContext, article_id);
                } else {
                    finish();
                }
            });
            videoView.setOnErrorListener((mp, what, extra) -> {
                videoStopped = mp.getCurrentPosition();
                videoView.pause();
                util.showLoading(mContext);
                videoView.setVideoURI(uri);
                videoView.seekTo(videoStopped);
                return true;
            });
        } else {
            mDialog = new ProgressDialog(ActivityArticle.this, R.style.AppCompatAlertDialogStyle);
            String message1 = "";
            String path;
            if (!videoView.isPlaying()) {
                path = Environment.getExternalStorageDirectory().toString() + "/Android/data/" + mContext.getPackageName() + "/mystory_articles/article_" + article_id + "/video_" + article_id + "_.mp4";
                videoView.setVideoPath(path);
                initial.setVisibility(View.GONE);
                videoView.start();
                util.hideLoading();
                mDialog.dismiss();
                videoView.setOnCompletionListener(mp -> {
                    if (!getValueString("FULL_NAME", mContext).equals("")) {
                        bringToFrontLayout();
                        sendAnalytics(mContext, article_id);
                    } else {
                        finish();
                    }
                });
                videoView.setOnErrorListener((mp, what, extra) -> {
                    videoStopped = mp.getCurrentPosition();
                    videoView.pause();
                    util.showLoading(mContext);
                    videoView.setVideoPath(path);
                    videoView.seekTo(videoStopped);
                    util.showLoading(mContext);
                    return true;
                });
            }
        }
        videoView.requestFocus();
        videoView.setOnPreparedListener(mp -> {
            initial.setVisibility(View.GONE);
            mp.setLooping(false);
            videoView.start();
            util.hideLoading();
        });
    }
    public boolean globalMessageBox(Context context){
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
                initialLoadingVideo(new Utility());
                if(checked[0]) {
                    save("SHOW_POP_UP_DATA_USAGE", "1", context);
                }
                else {
                    save("SHOW_POP_UP_DATA_USAGE", "0", context);
                }
                response[0] = true;
                popUp.dismiss();

            });
            showMessageBox(false,context,inflatedView);
        }else{
            response[0] = true;
        }
        return response[0];
    }
    private void showMessageBox(boolean cancelable, Context context, View view){
        popUp = new AlertDialog.Builder(context).create();
        popUp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popUp.setView(view);
        popUp.setCanceledOnTouchOutside(cancelable);
        popUp.setCancelable(cancelable);
        popUp.show();
    }
}
