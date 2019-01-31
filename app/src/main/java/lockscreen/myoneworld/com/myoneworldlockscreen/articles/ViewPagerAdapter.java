package lockscreen.myoneworld.com.myoneworldlockscreen.articles;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import lockscreen.myoneworld.com.myoneworldlockscreen.R;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    ArrayList<String> arrayList;
    private int type;

    public ViewPagerAdapter(Context context, ArrayList<String> arrayList, int type) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = arrayList;
        this.type = type;
    }


    @Override
    public int getCount() {
        if(arrayList != null){
            return arrayList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.article_comics_type, null);
        if(type == 1) {
            ImageView imageView = view.findViewById(R.id.comics_type);
            Picasso.with(context)
                    .load(arrayList.get(position))
                    .placeholder(R.drawable.default_image_thumbnail)
                    .into(imageView);
            container.addView(view);
        }else if(type == 2){
            ImageView imageView = view.findViewById(R.id.comics_type);
            Picasso.with(context)
                    .load(new File(arrayList.get(position)))
                    .placeholder(R.drawable.default_image_thumbnail)
                    .into(imageView);
            container.addView(view);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
