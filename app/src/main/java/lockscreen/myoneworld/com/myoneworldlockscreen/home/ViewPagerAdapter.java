package lockscreen.myoneworld.com.myoneworldlockscreen.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import lockscreen.myoneworld.com.myoneworldlockscreen.R;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private int[] guideImages = new int[]{
            R.drawable.tutorial_clock, // clock
            R.drawable.tutorial_unlock,//right
            R.drawable.tutorial_article, //left
            R.drawable.tutorial_mybarko,//up
            R.drawable.tutorial_mylifestyle, // down
    };
    public ViewPagerAdapter(Context context)
    {
        this.context = context;
    }

    public int getCount() {
        return guideImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_viewpager_holder, null);
        ImageView imageView = view.findViewById(R.id.images_guide);
        imageView.setImageResource(guideImages[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        android.support.v4.view.ViewPager vp = (android.support.v4.view.ViewPager) container;
        vp.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        android.support.v4.view.ViewPager vp = (android.support.v4.view.ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
