package lockscreen.myoneworld.com.myoneworldlockscreen.articles;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.VideoView;
import com.google.android.gms.analytics.Tracker;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.*;

import lockscreen.myoneworld.com.myoneworldlockscreen.AnalyticsApplication;
import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.Utility;

public class ActivityArticle extends AppCompatActivity {
    public static int[] property;
    public static int imgCount = 0; //holder of number in folder
    static SharedPreferences spf;
    Context mContext = this;
    public static String article_id;
    VideoView videoView;
    Tracker mTracker;
    ProgressDialog mDialog;
    AlertDialog aDialog;
    ImageView initial;
    String[] imagesPath;
    static ArrayList<String> imageUrl;
    static ArrayList<String> comicsPathArrayList;
    ViewPager viewPager;
    Button textComment;
    ImageButton shareButton;
    ImageButton likeButton;
    LinearLayout commentThings;
    LinearLayout likeAnimation;
    boolean tempLikeStatus = false;
    String[] comicsPath;
    Animation rotate;
    private PopupWindow popWindow;
    private int videoStopped = 0;
    private int shared = 0;

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
            commentThings = findViewById(R.id.comment_things);
            likeAnimation = findViewById(R.id.heart_anim_linear);
            textComment.setOnClickListener(v -> {
                onShowPopup(v);
                commentThings.setVisibility(View.GONE);
            });
            shareButton.setOnClickListener(v -> dialogShare());
            likeButton.setOnClickListener(v -> {
                clickLikeButton(tempLikeStatus);
                tempLikeStatus = !tempLikeStatus;
            });
//            commentThings.setVisibility(View.VISIBLE);
            textComment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!textComment.getText().toString().equals("")) {
                        textComment.setBackgroundColor(Color.parseColor("#ffffff"));

                    } else {
                        textComment.setBackgroundColor(Color.parseColor("#60ffffff"));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            textComment.bringToFront();
            likeButton.bringToFront();
            shareButton.bringToFront();
            likeAnimation.bringToFront();
            AnalyticsApplication application = (AnalyticsApplication) getApplication();
            mTracker = application.getDefaultTracker();
            videoView = findViewById(R.id.flipper);
            if ("video_with_slide_show".equals(getValueString("article_kind_" + article_id, mContext))) {
                String[] articleType = {"Video Article", "Comic Article"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Choose Article Type");
                builder.setIcon(mContext.getResources().getDrawable(R.drawable.m1w_logo));
                builder.setCancelable(false);
                builder.setItems(articleType, (dialog, which) -> {
                    if ("Video Article".equals(articleType[which])) {
                        chooseOptionArticle("video");
                    } else if ("Comic Article".equals(articleType[which])) {
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
                        packageName.contains("com.google.android.apps.docs") ||
                                packageName.contains("com.facebook.katana") ||
                                packageName.contains("com.instagram.android") ||
                                packageName.contains("com.twitter.android")){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, message);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "my|storya");
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if(!targetShareIntents.isEmpty()){
                Intent chooserIntent=Intent.createChooser(targetShareIntents.remove(0), "Choose app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                startActivityForResult(chooserIntent,1010);
            }else{
                Toast.makeText(mContext,"No application applicable to share story.",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    private void clickLikeButton(boolean liked){
        //api for like
        if(liked){
            likeButton.setImageResource(R.drawable.ic_like___colored);
            Animation like = AnimationUtils.loadAnimation(this,R.anim.zoomin);
            likeAnimation.setAnimation(like);
            likeAnimation.setVisibility(View.VISIBLE);
            commentThings.setVisibility(View.GONE);
            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    likeAnimation.setVisibility(View.GONE);
                    likeAnimation.clearAnimation();
                    commentThings.setVisibility(View.VISIBLE);
                }
            }.start();
        }else{
            likeButton.setImageResource(R.drawable.ic_like_no_color);
        }
    }

    private void chooseOptionArticle(String type) {
        Utility util = new Utility();
        if ("video".equals(type)) {
            textComment.bringToFront();
            likeButton.bringToFront();
            shareButton.bringToFront();
            likeAnimation.bringToFront();
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoView.getLayoutParams();
            params.width = metrics.widthPixels;
            params.height = metrics.heightPixels;
            params.leftMargin = 0;
            videoView.setLayoutParams(params);
            try {
                if (!getValueString("DO_NOT_DOWNLOAD",mContext).equals("1")) { // downloaded video played
                    mDialog = new ProgressDialog(ActivityArticle.this, R.style.AppCompatAlertDialogStyle);
                    String message = "";
                    if ("MOBILE".equalsIgnoreCase(getConnectionType(mContext))) {
                        message = DATA_USAGE + PLEASE_WAIT;
                    } else {
                        message = PLEASE_WAIT;
                    }
                    String path;
                    mDialog.setMessage(message);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    if (!videoView.isPlaying()) {
                        path = Environment.getExternalStorageDirectory().toString() + "/Android/data/" + mContext.getPackageName() + "/mystory_articles/article_" + article_id + "/video_" + article_id + "_.mp4";
                        videoView.setVideoPath(path);
                        initial.setVisibility(View.GONE);
                        videoView.start();
                        util.hideLoading();
                        mDialog.dismiss();
                        videoView.setOnCompletionListener(mp -> {
                            if (!getValueString("FULL_NAME",mContext).equals("")) {
                                commentThings.setVisibility(View.VISIBLE);
                                videoView.stopPlayback();
                                sendAnalytics(mContext,article_id);
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
                            return true;
                        });
                    }
                } else {// cloud video played
                    if (!isNetworkAvailable(mContext)) { // error no connection
                        errorOnPlayingVideo("cloud");
                    }
                    else {
                        try { // cloud loading
                            String message = "";
                            if (!"MOBILE".equalsIgnoreCase(getConnectionType(mContext))) {
                                util.showLoading(mContext);
                            }else{
                                util.showLoading(mContext);
                                showPopUpShowDataUsage();
                            }
                            if (!videoView.isPlaying()) {
                                if ("".equals(getValueString("video_url_download_" + article_id,mContext))) {
                                    Uri uri = Uri.parse(getValueString("video_url_" + article_id,mContext));
                                    videoView.setVideoURI(uri);
                                    videoView.setOnCompletionListener(mp -> {
                                        if (!getValueString("FULL_NAME",mContext).equals("")) {
                                            commentThings.setVisibility(View.VISIBLE);
                                            sendAnalytics(mContext, article_id);
                                            videoView.stopPlayback();
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
                                    if ("MOBILE".equalsIgnoreCase(getConnectionType(mContext))) {
                                        message1 = DATA_USAGE + PLEASE_WAIT;
                                    } else {
                                        message1 = PLEASE_WAIT;
                                    }
                                    String path;
                                    mDialog.setMessage(message1);
                                    mDialog.setCanceledOnTouchOutside(false);
                                    mDialog.show();
                                    if (!videoView.isPlaying()) {
                                        path = Environment.getExternalStorageDirectory().toString() + "/Android/data/" + mContext.getPackageName() + "/mystory_articles/article_" + article_id + "/video_" + article_id + "_.mp4";
                                        videoView.setVideoPath(path);
                                        initial.setVisibility(View.GONE);
                                        videoView.start();
                                        util.hideLoading();
                                        mDialog.dismiss();
                                        videoView.setOnCompletionListener(mp -> {
                                            if (!getValueString("FULL_NAME",mContext).equals("")) {
                                                videoView.stopPlayback();
                                                commentThings.setVisibility(View.VISIBLE);
                                                sendAnalytics(mContext,article_id);
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
            videoView.requestFocus();
            videoView.setOnPreparedListener(mp -> {
                initial.setVisibility(View.GONE);
                mp.setLooping(false);
                videoView.start();
                util.hideLoading();
            });
        } else if ("slide_show".equals(type)) {
            try {
                if ("1".equals(getValueString("DO_NOT_DOWNLOAD", mContext))) {
                    ArticleDAO articleDAO = new ArticleDAO();
                    viewPager = findViewById(R.id.view_pager_article);
                    articleDAO.getComicsTypeImage(article_id, mContext,initial,textComment,likeButton,
                            shareButton,likeAnimation,viewPager,this,commentThings);
                }else{ // downloaded comics
                    filePathComics(article_id,mContext);
                    if (!comicsPathArrayList.isEmpty()) {
                        util.hideLoading();
                        initial.setVisibility(View.GONE);
                        textComment.bringToFront();
                        likeButton.bringToFront();
                        shareButton.bringToFront();
                        likeAnimation.bringToFront();
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
                                    commentThings.setVisibility(View.VISIBLE);
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
            if("cloud".equals(setting))message = CANT_PLAY_ERROR_CLOUD;
            AlertDialog.Builder alertDialogBuilder =
                    new AlertDialog.Builder(ActivityArticle.this)
                            .setTitle("Error in Playing Video")
                            .setMessage(message)
                            .setPositiveButton("OK", (dialog, which) -> finish());
            AlertDialog alertDialog = alertDialogBuilder.show();
            return true;
        });
    }

    public void onShowPopup(View v) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate the custom popup layout
        final View inflatedView = layoutInflater.inflate(R.layout.comment_box_layout, null, false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ImageView loading = inflatedView.findViewById(R.id.loading_comment);
        ImageButton send = inflatedView.findViewById(R.id.send_comment);
        EditText comment = inflatedView.findViewById(R.id.writeComment);
        loading.startAnimation(rotate);
        send.setOnClickListener(v1 -> {
            ArticleDAO articleDAO = new ArticleDAO();
            popWindow.dismiss();
            articleDAO.sendComment(article_id, getValueString("USER_ID", mContext), comment.getText().toString(), mContext);
        });
        ListView listView = (ListView) inflatedView.findViewById(R.id.commentsListView);
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        int mDeviceHeight = size.y;
        setSimpleList(listView, loading);


        // set height depends on the device size
        popWindow = new PopupWindow(inflatedView, size.x - 50, size.y - 800, true);
        // set a background drawable with rounders corners
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.comment_box_bg));
        // make it focusable to show the keyboard to enter in `EditText`
        popWindow.setFocusable(true);
        // make it outside touchable to dismiss the popup window
        popWindow.setOutsideTouchable(true);

        // show the popup at bottom of the screen and set some margin at bottom ie,
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0, 100);

        popWindow.setOnDismissListener(() -> commentThings.setVisibility(View.VISIBLE));
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

    private void showPopUpShowDataUsage(){
        if("MOBILE".equalsIgnoreCase(getConnectionType(mContext))){
            globalMessageBox(mContext,"Using Mobile Data Connection, may cause data charges","Data Usage",MSG_BOX_WARNING);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1010) {
            videoView.stopPlayback();
            if (resultCode == RESULT_OK) {
                finish();
            }else if (resultCode == RESULT_CANCELED){
               shared = 1;
            }
        }
    }
}
