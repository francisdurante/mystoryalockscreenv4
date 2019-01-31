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
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.*;
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
                        String idForSetting = data.getJSONObject(x).getString("id");
                        String kindDownload = data.getJSONObject(x).getString("show");
                        String title = data.getJSONObject(x).getString("title");
                        save("my_storya_title_" + idForSetting, title, mContext);
                        if (!"1".equals(getValueString("DO_NOT_DOWNLOAD", mContext))) {
                            if ("video".equals(kindDownload)) {
                                if (getValueString("WIFI_ONLY", mContext).equals("1") &&
                                        getConnectionType(mContext).equalsIgnoreCase("WIFI")) {
                                    String videoUrlWifiOnly = data.getJSONObject(x).getString("video_url");
                                    if (!getValueString("video_url_download_" + idForSetting, mContext).equals(videoUrlWifiOnly)) {
                                        save("video_url_download_" + idForSetting, videoUrlWifiOnly, mContext);
                                        save("video_url_" + idForSetting, videoUrlWifiOnly, mContext);
                                        downloadVideo = new DownloadImageFromApi(2, idForSetting, mContext, 0).execute(videoUrlWifiOnly);
                                        Log.d("Video Download", "Download video with id " + idForSetting);
                                    }
                                } else if (getValueString("WIFI_OR_DATA", mContext).equals("1") &&
                                        getConnectionType(mContext).equalsIgnoreCase("WIFI") ||
                                        getConnectionType(mContext).equalsIgnoreCase("MOBILE")) { // wifi or data

                                    String videoUrlWifiOrData = data.getJSONObject(x).getString("video_url");
                                    if (!getValueString("video_url_download_" + idForSetting, mContext).equals(videoUrlWifiOrData)) {
                                        save("video_url_download_" + idForSetting, videoUrlWifiOrData, mContext);
                                        save("video_url_" + idForSetting, videoUrlWifiOrData, mContext);
                                        downloadVideo = new DownloadImageFromApi(2, idForSetting, mContext, 0).execute(videoUrlWifiOrData);
                                        Log.d("Video Download", "Download video with id " + idForSetting);
                                    }
                                }
                            } else if ("slide_show".equals(kindDownload)) {
                                String[] imageComicsUrl;
                                Log.d("downloading", "downloading article comic id " + idForSetting);
                                JSONObject imageData = new JSONObject(response.toString());
                                JSONArray data1 = imageData.getJSONArray("data");
                                JSONArray imageDataDownload = data1.getJSONObject(x).getJSONArray("slide_show_images");
                                imageComicsUrl = new String[imageDataDownload.length()];
                                for (int i = 0; i < imageDataDownload.length(); i++) {
                                    Log.d("comics url", imageDataDownload.getString(i));
                                    if (!imageDataDownload.getString(i).equals(getValueString("comic_article_download_" + idForSetting + "_" + i, mContext))) {
                                        imageComicsUrl[i] = imageDataDownload.getString(i);
                                        save("comic_article_download_" + idForSetting + "_" + i, imageComicsUrl[i], mContext);
                                        downloadVideo = new DownloadImageFromApi(3, idForSetting, mContext, 0, i).execute(imageComicsUrl[i]);
                                    }
                                }
                            }
                        }
                        if (!"".equals(data.getJSONObject(x).getString("image_url")) && "".equals(url)) {
                            String kind = data.getJSONObject(x).getString("show");
                            String video = data.getJSONObject(x).getString("video_url");
                            String id = data.getJSONObject(x).getString("id");
                            String imageUrl = data.getJSONObject(x).getString("image_url");
                            if (!"".equals(video) && !"null".equals(video) && kind.equals("slide_show")) {
                                save("article_kind_" + id, "video_with_slide_show", mContext);
                            } else {
                                save("article_kind_" + id, kind, mContext);
                            }
                            downloadArticle = new DownloadImageFromApi(1, id, mContext, 0).execute(imageUrl);
                            ///video things..
                            if (getValueString("DO_NOT_DOWNLOAD", mContext).equals("1")) { // cloud loading
                                String videoUrl = data.getJSONObject(x).getString("video_url");
                                save("video_url_" + id, videoUrl, mContext);
                            } else if (getValueString("WIFI_ONLY", mContext).equals("1") &&
                                    getConnectionType(mContext).equalsIgnoreCase("WIFI")) { //wifi only
                                String videoUrlWifiOnly = data.getJSONObject(x).getString("video_url");
                                if (!getValueString("video_url_download_" + id, mContext).equals(videoUrlWifiOnly)) {
                                    save("video_url_download_" + id, videoUrlWifiOnly, mContext);
                                    downloadVideo = new DownloadImageFromApi(2, id, mContext, 0).execute(videoUrlWifiOnly);
                                }
                                if ("slide_show".equals(kind)) {
                                    String[] imageComicsUrl;
                                    Log.d("downloading", "downloading article comic id " + id);
                                    JSONObject imageData = new JSONObject(response.toString());
                                    JSONArray data1 = imageData.getJSONArray("data");
                                    JSONArray imageDataDownload = data1.getJSONObject(x).getJSONArray("slide_show_images");
                                    imageComicsUrl = new String[imageDataDownload.length()];
                                    for (int i = 0; i < imageDataDownload.length(); i++) {
                                        Log.d("comics url", imageDataDownload.getString(i));
                                        if (!imageDataDownload.getString(i).equals(getValueString("comic_article_download_" + id + "_" + i, mContext))) {
                                            imageComicsUrl[i] = imageDataDownload.getString(i);
                                            save("comic_article_download_" + id + i, imageComicsUrl[i], mContext);
                                            downloadVideo = new DownloadImageFromApi(3, id, mContext, 0).execute(imageComicsUrl[i]);
                                        }
                                    }
                                }
                            } else if (getValueString("WIFI_OR_DATA", mContext).equals("1") &&
                                    getConnectionType(mContext).equalsIgnoreCase("WIFI") ||
                                    getConnectionType(mContext).equalsIgnoreCase("MOBILE")) { // wifi or data
                                String videoUrlWifiOrData = data.getJSONObject(x).getString("video_url");
                                if (!getValueString("video_url_download_" + id, mContext).equals(videoUrlWifiOrData)) {
                                    save("video_url_download_" + id, videoUrlWifiOrData, mContext);
                                    downloadVideo = new DownloadImageFromApi(2, id, mContext, 0).execute(videoUrlWifiOrData);
                                }
                                if ("slide_show".equals(kind)) {
                                    String[] imageComicsUrl;
                                    Log.d("downloading", "downloading article comic id " + idForSetting);
                                    JSONObject imageData = new JSONObject(response.toString());
                                    JSONArray data1 = imageData.getJSONArray("data");
                                    JSONArray imageDataDownload = data1.getJSONObject(x).getJSONArray("slide_show_images");
                                    imageComicsUrl = new String[imageDataDownload.length()];
                                    for (int i = 0; i < imageDataDownload.length(); i++) {
                                        Log.d("comics url", imageDataDownload.getString(i));
                                        if (!imageDataDownload.getString(i).equals(getValueString("comic_article_download_" + id + "_" + i, mContext))) {
                                            imageComicsUrl[i] = imageDataDownload.getString(i);
                                            save("comic_article_download_" + id + "_" + i, imageComicsUrl[i], mContext);
                                            downloadVideo = new DownloadImageFromApi(3, id, mContext, 0).execute(imageComicsUrl[i]);
                                        }
                                    }
                                }
                            }
                        }
                        if (!"".equals(url) && !url.equals(data.getJSONObject(x).getString("image_url"))) { //edit in admin panel
                            String kind = data.getJSONObject(x).getString("show");
                            String video = data.getJSONObject(x).getString("video_url");
                            String id = data.getJSONObject(x).getString("id");
                            String imageUrl = data.getJSONObject(x).getString("image_url");
                            if (!"".equals(video) && !"null".equals(video) && kind.equals("slide_show")) {
                                save("article_kind_" + id, "video_with_slide_show", mContext);
                            } else {
                                save("article_kind_" + id, kind, mContext);
                            }
                            downloadArticle = new DownloadImageFromApi(1, id, mContext, 1).execute(imageUrl);
                            //edited video
                            if (!"".equals(getValueString("video_url_" + id, mContext)) && !getValueString("video_url_" + id, mContext).equals(data.getJSONObject(x).getString("video_url"))) {
                                if (getValueString("DO_NOT_DOWNLOAD", mContext).equals("1")) { // cloud loading
                                    String videoUrl = data.getJSONObject(x).getString("video_url");
                                    save("video_url_" + id, videoUrl, mContext);
                                } else if (getValueString("WIFI_ONLY", mContext).equals("1") &&
                                        getConnectionType(mContext).equalsIgnoreCase("WIFI")) { //wifi only
                                    String videoUrlWifiOnly = data.getJSONObject(x).getString("video_url");
                                    if (!getValueString("video_url_" + id, mContext).equals(videoUrlWifiOnly)) {
                                        save("video_url_" + id, videoUrlWifiOnly, mContext);
                                        save("video_url_download_" + id, videoUrlWifiOnly, mContext);
                                        downloadVideo = new DownloadImageFromApi(2, id, mContext, 1).execute(videoUrlWifiOnly);
                                    }
                                } else if (getValueString("WIFI_OR_DATA", mContext).equals("1") &&
                                        getConnectionType(mContext).equalsIgnoreCase("WIFI") ||
                                        getConnectionType(mContext).equalsIgnoreCase("MOBILE")) { // wifi or data
                                    String videoUrlWifiOrData = data.getJSONObject(x).getString("video_url");
                                    if (!getValueString("video_url_" + id, mContext).equals(videoUrlWifiOrData)) {
                                        save("video_url_" + id, videoUrlWifiOrData, mContext);
                                        save("video_url_download_" + id, videoUrlWifiOrData, mContext);
                                        downloadVideo = new DownloadImageFromApi(2, id, mContext, 1).execute(videoUrlWifiOrData);
                                    }
                                }
                            }
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

