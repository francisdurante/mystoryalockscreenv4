package lockscreen.myoneworld.com.myoneworldlockscreen.articles;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;
import lockscreen.myoneworld.com.myoneworldlockscreen.ApiClass;
import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.Utility;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.ANALYTICS_STORIES_TEST;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.API_STATUS;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DONE_VIEWED_ARTICLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GET_COMMENT_STORY_ID_TEST;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MY_STORYA_SINGLE_CONTENT;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.ANALYTICS_STORIES_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GET_COMMENT_STORY_ID_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.SEND_COMMENT_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.SEND_COMMENT_TEST;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.showProgressBar;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.hideProgressBar;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.parseDateToddMMyyyy;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.getDatePostedComputations;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;

public class ArticleDAO {
    RelativeLayout afterVideo;
    public void getComicsTypeImage(String id, Context context,
                                   ImageView initial,
                                   ImageButton textComment,
                                   ImageButton likeButton,
                                   ImageButton shareButton,
                                   LinearLayout likeAnimation,
                                   final ViewPager viewPager,
                                   Activity activity,
                                   LinearLayout commentThings,
                                   RelativeLayout afterArticleLayout
                                   ) {
        afterVideo = afterArticleLayout;
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        showProgressBar(context);
        api.getByUrl(MY_STORYA_SINGLE_CONTENT + id, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        ActivityArticle.imageUrl = new ArrayList<String>();
                        JSONObject serverResp = new JSONObject(response.toString());
                        JSONObject data = serverResp.getJSONObject("data");
                        JSONArray imageSlideShow = data.getJSONArray("slide_show_images");
                        for (int x = 0; x < imageSlideShow.length(); x++) {
                            ActivityArticle.imageUrl.add(imageSlideShow.get(x).toString());
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                if(!ActivityArticle.imageUrl.isEmpty()) {
                    hideProgressBar();
                    initial.setVisibility(View.GONE);
                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(context, ActivityArticle.imageUrl, 1);
                    viewPager.setAdapter(viewPagerAdapter);
                    int bookmark = Integer.parseInt("".equals(getValueString("bookmark_article_"+id,context)) ? "0" : getValueString("bookmark_article_"+id,context));
                    if(bookmark != 0)
                        viewPager.setCurrentItem(bookmark);
                    else
                        viewPager.setCurrentItem(0);

                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        boolean lastPageChange = false;

                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            int lastIdx = viewPagerAdapter.getCount() - 1;
                            if (position == lastIdx) {
                                save("bookmark_article_done_"+id,"DONE",context);
                                bringToFrontlayout();
                                sendAnalytics(getValueString("USER_ID", context),DONE_VIEWED_ARTICLE, ActivityArticle.article_id, context,getValueString("ACCESS_TOKEN",context));
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
                } else {
                    Toast.makeText(context, "Please check connection", Toast.LENGTH_LONG).show();
                    activity.finish();
                }
            }

        });
    }

    public void sendAnalytics(String user_id,String article_id,String action,Context context,String accessToken){
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        rp.add("id", user_id);
        rp.add("mystorya_id", article_id);
        rp.add("action",action);
        rp.add("Authorization", accessToken);
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Authorization", accessToken));

        api.getByUrlHeader(context,"LIVE".equals(API_STATUS) ? ANALYTICS_STORIES_LIVE : ANALYTICS_STORIES_TEST,headers.toArray(new Header[headers.size()]), rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject serverResp = new JSONObject(response.toString());
                    if ("success".equals(serverResp.getString("status"))) {
//                        Toast.makeText(context,serverResp.getString("message"),Toast.LENGTH_LONG).show();

                    } else if ("fail".equals(serverResp.getString("status")) && serverResp.has("message")) {
//                        Toast.makeText(context,serverResp.getString("message"),Toast.LENGTH_LONG).show();
                    }

                    Utility utility = new Utility();
                    utility.immediateNotification(context,Integer.parseInt(article_id),"You receive 10 points!",1,null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void sendComment(String storyId, String userId, String comment, Context context, ListView lv,ImageView iv,Activity activity,String accessToken){
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        rp.add("story_id",storyId);
        rp.add("user_id",userId);
        rp.add("comment",comment);
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Authorization", accessToken));

        api.getByUrlHeader(context,"LIVE".equalsIgnoreCase(API_STATUS) ? SEND_COMMENT_LIVE : SEND_COMMENT_TEST,headers.toArray(new Header[headers.size()]),rp,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject responseStatus = new JSONObject(response.toString());
                    getCommentByStoryId(storyId,context,lv,iv,activity,accessToken);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
    public void getCommentByStoryId(String storyId, Context context, ListView listView,ImageView loading,Activity activity,String accessToken){
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        rp.add("story_id",storyId);
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Authorization", accessToken));
        api.getByUrlHeader(context,"LIVE".equalsIgnoreCase(API_STATUS) ? GET_COMMENT_STORY_ID_LIVE : GET_COMMENT_STORY_ID_TEST,headers.toArray(new Header[headers.size()]) ,rp,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject commentResponse = new JSONObject(response.toString());
                    JSONArray commentDetail = commentResponse.getJSONArray("comments");
                    String[] _comments = new String[commentDetail.length()];
                    String[] _datePosted = new String[commentDetail.length()];
                    String[] _userCommented = new String[commentDetail.length()];
                    for(int x = 0; x < commentDetail.length(); x++){
                        _comments[x] = commentDetail.getJSONObject(x).getString("comment");
                        _datePosted[x] = parseDateToddMMyyyy(commentDetail.getJSONObject(x).getString("created_at"));
                        _userCommented[x] = commentDetail.getJSONObject(x).getString("first_name") + " " + commentDetail.getJSONObject(x).getString("last_name");

                    }
                    loading.clearAnimation();
                    loading.setVisibility(View.GONE);
                    ArrayList<Spanned> comment = new ArrayList<Spanned>();
                    if(_comments.length == 0){
//                        Toast.makeText(context,"No comment yet.",Toast.LENGTH_LONG).show();
                    }else {
                        for (int index = 0; index < _comments.length; index++) {
                            String commentContent = "<html><pre><b>" + _userCommented[index] + "</b> " +
                                    "<i><small>" + getDatePostedComputations(_datePosted[index]) + "</i></small></pre></html>" +
                                    "<br><br><html><pre><b>\t\t" +  Html.fromHtml(_comments[index]).toString() + "</b><pre></html>";

                            comment.add(Html.fromHtml(commentContent));
                        }

                        listView.setAdapter(new ArrayAdapter<Spanned>(activity,
                                R.layout.comment_list_item, android.R.id.text1, comment));


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
    private void bringToFrontlayout(){
        afterVideo.bringToFront();
        afterVideo.setVisibility(View.VISIBLE);
    }
}
