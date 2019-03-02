package lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import cz.msebera.android.httpclient.Header;
import lockscreen.myoneworld.com.myoneworldlockscreen.ApiClass;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.generateErrorLog;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.getConnectionType;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.getCurrentTime;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MY_STORYA_DELETED_CONTENT;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.ANDROID_PATH;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.ARTICLE_ANALYTICS;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MY_STORYA_API_CONTENT_LIVE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;

public class LockscreenDAO {

    Context mContext;
    AsyncTask downloadArticle;
    AsyncTask downloadVideo;

    public LockscreenDAO(Context context) {
        this.mContext = context;
    }

    public void getArchivedStory() {
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        api.getByUrl(MY_STORYA_DELETED_CONTENT, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject serverResp = new JSONObject(response.toString());
                    JSONArray data = serverResp.getJSONArray("data");
                    for (int x = 0; x < data.length(); x++) {
                        String id = data.getJSONObject(x).getString("id");
                        deleteMyStoryFolderContent(id);
                    }
                } catch (JSONException e) {
                    Writer writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    String s = writer.toString();
                    generateErrorLog(mContext, "err_log_" + getCurrentTime(), s);
                    e.printStackTrace();
                }
            }
        });
    }

    private void deleteMyStoryFolderContent(String story_id) {
        File main_folder_path = new File(ANDROID_PATH + mContext.getPackageName() + "/mystory_articles/");
        File myStory = new File(ANDROID_PATH + mContext.getPackageName() + "/mystory/");
        File myStoryContent = new File(ANDROID_PATH + mContext.getPackageName() + "/mystory_articles/article_" + story_id + "/");
        File comicsArticleContent = new File(ANDROID_PATH + mContext.getPackageName() + "/mystory_articles/article_" + story_id + "/story_comics_" + story_id + "/");
        if (myStory.isDirectory()) {
            String[] children = myStory.list();
            for (int i = 0; i < children.length; i++) {
                if (children[i].contains("myStory_" + story_id + "_")) {
                    new File(myStory, children[i]).delete();
                    save("image_url_" + story_id, "", mContext);
                }
            }
        }
        if (myStoryContent.isDirectory()) {
            String[] children = myStoryContent.list();
            for (int i = 0; i < children.length; i++) {
                if (children[i].contains("video_" + story_id)) {
                    new File(myStoryContent, children[i]).delete();
                    save("video_url_" + story_id, "", mContext);
                }
            }
        }
        if (comicsArticleContent.isDirectory()) {
            String[] children = comicsArticleContent.list();
            for (int i = 0; i < children.length; i++) {
                if (children[i].contains("myStorya_comics_type_" + story_id + "_")) {
                    new File(comicsArticleContent, children[i]).delete();
                    save("comic_article_download_" + story_id + "_" + i, "", mContext);
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

    void newApiMyStoryaContent() {
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        String statusArticle;
        api.getByUrl(MY_STORYA_API_CONTENT_LIVE, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject serverResp = new JSONObject(response.toString());
                    JSONArray data = serverResp.getJSONArray("data");
                    for (int x = 0; x < data.length(); x++) {
                        String url = getValueString("image_url_" + data.getJSONObject(x).getString("id"), mContext);
                        String storyId = data.getJSONObject(x).getString("id");
                        String kindDownload = data.getJSONObject(x).getString("show");
                        String lockscreenCoverUrl = data.getJSONObject(x).getString("image_url");
                        String videoUrl = data.getJSONObject(x).getString("video_url");
                        String title = data.getJSONObject(x).getString("title");
                        save("my_storya_title_" + storyId, title, mContext);

                        //downloading article cover
                        if("".equals(getValueString("LOCKSCREEN_COVER_"+storyId,mContext))
                                && "".equals(getValueString("DOWNLOADING_LOCKSCREEN_COVER_ID_"+storyId,mContext))) {
                            downloadArticle = new DownloadImageFromApi(1, storyId, mContext, 0).execute(lockscreenCoverUrl);
                            System.out.println("downloading cover " + storyId);
                        }
                        if(!"".equals(getValueString("LOCKSCREEN_COVER_"+storyId,mContext))
                                && !lockscreenCoverUrl.equals(getValueString("LOCKSCREEN_COVER_"+storyId,mContext))){
                            downloadArticle = new DownloadImageFromApi(1, storyId, mContext, 1).execute(lockscreenCoverUrl);
                            System.out.println("EDITED LOCKSCREEN COVER");
                        }

                        if (!"".equals(videoUrl) && !"null".equals(videoUrl) && kindDownload.equals("slide_show")) {
                            save("ARTICLE_KIND_" + storyId, "video_with_slide_show", mContext);
                        }else {
                            save("ARTICLE_KIND_" + storyId, kindDownload, mContext);
                        }
                        if("video".equals(kindDownload)){
                            save("VIDEO_URL_ID_"+storyId,videoUrl,mContext);
                        }
                    }
                } catch (JSONException e) {
                    Writer writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    String s = writer.toString();
                    e.printStackTrace();
                }
            }
        });
    }
    public static void article_analytics(String articleID, String articleViewed, String articleOpened, String articleRead, String idPlatform) {
        ApiClass api = new ApiClass();
        RequestParams rp = new RequestParams();
        rp.add("article_id", articleID);
        rp.add("article_viewed", articleViewed);
        rp.add("article_opened", articleOpened);
        rp.add("article_readed", articleRead);
        rp.add("id", idPlatform);

        api.getByUrl(ARTICLE_ANALYTICS, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    JSONObject serverResp = new JSONObject(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

