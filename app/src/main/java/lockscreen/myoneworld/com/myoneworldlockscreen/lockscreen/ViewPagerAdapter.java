package lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.ANDROID_PATH;
import lockscreen.myoneworld.com.myoneworldlockscreen.R;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> imagesPath;
    private boolean mIslistening;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private TextView listeningText;
    private Activity activity;
    public ViewPagerAdapter(Context context, ArrayList<String> imagePath, TextView listeningText
            , boolean mIslistening, SpeechRecognizer speechRecognizer, Intent mSpeechRecognizerIntent, Activity activity) {
        this.context = context;
        this.imagesPath = imagePath;
        this.listeningText = listeningText;
        this.mIslistening = mIslistening;
        this.mSpeechRecognizer = speechRecognizer;
        this.mSpeechRecognizerIntent = mSpeechRecognizerIntent;
        this.activity = activity;
    }

    public int getCount() {
        return imagesPath.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        try {
            ActivityLockscreen.fileID = position;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.lockscreen_background, null);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap bmImg = BitmapFactory.decodeFile(ANDROID_PATH + context.getPackageName() + "/mystory/" + imagesPath.get(position), options);
            BitmapDrawable background = new BitmapDrawable(bmImg);

            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            File path = new File(ANDROID_PATH + context.getPackageName() + "/mystory/" + imagesPath.get(position));
            if(path.exists())
                Picasso.with(context)
                        .load(path)
                        .centerCrop()
                        .fit()
                        .into(imageView);
            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);

//            imageView.setOnClickListener(v -> startListening(context,mSpeechRecognizer,listeningText,mIslistening,mSpeechRecognizerIntent));
            return view;
        }catch (Exception e){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.lockscreen_background, null);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap bmImg = BitmapFactory.decodeFile(ANDROID_PATH + context.getPackageName() + "/mystory/" + imagesPath.get(position), options);
            BitmapDrawable background = new BitmapDrawable(bmImg);

            ImageView imageView = view.findViewById(R.id.imageView);
            File path = new File(ANDROID_PATH + context.getPackageName() + "/mystory/" + imagesPath.get(position));
            if(path.exists())
                Picasso.with(context)
                        .load(path)
                        .fit()
                        .into(imageView);

            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;
        }
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
