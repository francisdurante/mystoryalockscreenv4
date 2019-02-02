package lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;

public class DownloadImageFromApi extends AsyncTask<String, Integer, String> {
    private int ft;
    private Bitmap bitmap;
    private BitmapDrawable bitmapDrawable;
    private int fileNameNumber = 0;
    static SharedPreferences spf;
    private String filePath;
    private SimpleDateFormat sdf_min;
    private SimpleDateFormat sdf;
    private SimpleDateFormat ms;
    private Calendar c = Calendar.getInstance();
    private String articleId;
    Context mContext;
    private String pathCover;
    private String pathVideo;
    private String pathComics;
    private int edited;
    private int count;

    public DownloadImageFromApi(ImageView image, int folderType, String article, Context context)
    {
        this.ft = folderType;
        this.articleId = article;
        mContext = context;
    }
    DownloadImageFromApi(int folderType, String article, Context context, int edit) // 1- edited 0-not
    {
        this.ft = folderType;
        this.articleId = article;
        mContext = context;
        this.edited = edit;
    }
    DownloadImageFromApi(int folderType, String article, Context context, int edit, int count) // 1- edited 0-not
    {
        this.ft = folderType;
        this.articleId = article;
        mContext = context;
        this.edited = edit;
        this.count = count;
    }
    DownloadImageFromApi()
    {}
    @Override
    protected String doInBackground(String... url) {
        String response = "fail";
        try {
            if (ft == 1) {
                String fileName = "myStory_" + articleId + "_date.jpg";
//
                pathCover = url[0];
                try {
                    URL urlForImg = new URL(pathCover);
                    URLConnection urlConnection = urlForImg.openConnection();
                    urlConnection.connect();
                    File new_folder = new File(Environment.getExternalStorageDirectory(), "Android/data/" + mContext.getPackageName() + "/mystory/");
                    if (!new_folder.exists()) {
                        new_folder.mkdirs();
                    }
                    File input_file = new File(new_folder, fileName);
                    InputStream srt = new java.net.URL(pathCover).openStream();
                    ArrayList<String> fileNameCheck = new ArrayList<String>(Arrays.asList(new_folder.list()));
                    if (!input_file.exists()) {
                        if (edited == 0) {
                            boolean existing = false;
                            for(int x = 0; x < fileNameCheck.size(); x++){
                                if(fileNameCheck.get(x).contains("myStory_" + articleId + "_")){
                                    existing = true;
                                }
                            }
                            if (!existing) {
                                fileName = "myStory_" + articleId + "_" + getCurrentTime("MM_dd_yyyy_HH_mm_ss") + ".jpg";
                                saveDownloadImage(new_folder, srt,fileName);
                            }
                        } else if (edited == 1) {
                            deleteEditedContent(articleId, mContext, 0);
                            fileName = "myStory_" + articleId + "_" + getCurrentTime("MM_dd_yyyy_HH_mm_ss") + ".jpg";
                            saveDownloadImage(new_folder, srt,fileName);
                        } else if (edited == 2) {
                            deleteEditedContent(articleId, mContext, 0);
                            fileName = "myStorya_comics_type_" + articleId + "_" + getCurrentTime("MM_dd_yyyy_HH_mm_ss") + ".jpg";
                            saveDownloadImage(input_file,srt,fileName);
                        }
                        response = "success";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //return null;
            } else if (ft == 2) {
                int count;
                String fileName = "article_content_" + fileNameNumber + ".jpg";
                pathVideo = url[0];
                try {
                    URL urlForImg = new URL(pathVideo);
                    URLConnection urlConnection = urlForImg.openConnection();
                    urlConnection.connect();
                    File new_article_folder = new File(Environment.getExternalStorageDirectory(), "Android/data/" + mContext.getPackageName() + "/mystory_articles/article_" + articleId + "/");
                    if (!new_article_folder.exists()) {
                        new_article_folder.mkdirs();
                    }
                    File input_file = new File(new_article_folder, fileName);
                    InputStream srt = new java.net.URL(pathVideo).openStream();

                    if (!input_file.exists()) {
                        fileName = "video_" + articleId + "_" + ".mp4";
                        if (edited == 0) {
                            Log.d("Video Downloading", "doInBackground: downloading video id " + articleId);
                            input_file = new File(new_article_folder, fileName);
                            OutputStream outputStream = new FileOutputStream(input_file);
                            byte data[] = new byte[2048];
                            long total = 0;
                            while ((count = srt.read(data)) != -1) {
                                total += count;
                                outputStream.write(data, 0, count);
                            }
                            filePath = input_file.getPath();
                            srt.close();
                            outputStream.close();
                            freeMemory();
                        } else if (edited == 1) {
                            Log.d("Video Downloading", "doInBackground: downloading video id " + articleId);
                            deleteEditedContent(articleId, mContext, 1);
                            input_file = new File(new_article_folder, fileName);
                            OutputStream outputStream = new FileOutputStream(input_file);
                            byte data[] = new byte[2048];
                            long total = 0;
                            while ((count = srt.read(data)) != -1) {
                                total += count;
                                outputStream.write(data, 0, count);
                            }
                            filePath = input_file.getPath();
                            srt.close();
                            outputStream.close();
                            freeMemory();
                        }
                        response = "success";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (ft == 3) { // comics article download
                String fileName = "myStorya_comics_type_" + articleId + "_date.jpg";
                pathComics = url[0];
                try {
                    URL urlForImg = new URL(pathComics);
                    URLConnection urlConnection = urlForImg.openConnection();
                    urlConnection.connect();
                    File new_folder = new File(Environment.getExternalStorageDirectory(), "Android/data/" + mContext.getPackageName() + "/mystory_articles/article_" + articleId + "/story_comics_" + articleId + "/");
                    if (!new_folder.exists()) {
                        new_folder.mkdirs();
                    }
                    File input_file = new File(new_folder, fileName);
                    InputStream srt = new java.net.URL(pathComics).openStream();

                    if (!input_file.exists()) {
                        if (edited == 0) {
                            fileName = "myStorya_comics_type_" + articleId + "_" + getCurrentTime("MM_dd_yyyy_HH_mm_ss") + ".jpg";
                            input_file = new File(new_folder, fileName);
                            OutputStream outputStream = new FileOutputStream(input_file);
                            bitmap = BitmapFactory.decodeStream(srt);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
                            bitmapDrawable = new BitmapDrawable(bitmap);
                            filePath = input_file.getPath();
                            srt.close();
                            outputStream.close();
                            freeMemory();
                        } else if (edited == 1) {
                            deleteEditedContent(articleId, mContext, 3);
                            fileName = "myStorya_comics_type_" + articleId + "_" + getCurrentTime("MM_dd_yyyy_HH_mm_ss") + ".jpg";
                            input_file = new File(new_folder, fileName);
                            OutputStream outputStream = new FileOutputStream(input_file);
                            bitmap = BitmapFactory.decodeStream(srt);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
                            bitmapDrawable = new BitmapDrawable(bitmap);
                            filePath = input_file.getPath();
                            srt.close();
                            outputStream.close();
                            freeMemory();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  response;
    }
    @Override
    protected void onPostExecute(String response) {
        if("success".equals(response)) {
            if (ft == 1) {
                save("image_url_" + articleId, pathCover, mContext);
                Log.d("url_downloaded_cover", pathCover);
                ActivityLockscreen.status = 1;
            }
            if (ft == 2) {
                save("video_url_" + articleId, pathVideo, mContext);
                ActivityLockscreen.status = 1;
            }
            if (ft == 3) {
                Log.d("url_downloaded", pathComics);
                Log.d("Comic Downloaded", "Comic Downloaded id " + articleId);
                ActivityLockscreen.status = 1;
            }
            if (!"1".equals(getValueString("DO_NOT_DOWNLOAD", mContext))) {
                save("video_url_download_", pathVideo, mContext);
            }
        }
        super.onPostExecute(response);

    }


    private void saveDownloadImage(File new_folder, InputStream srt,String fileName) throws IOException {
        String name = fileName;
        File input_file;
        input_file = new File(new_folder, name);
        OutputStream outputStream = new FileOutputStream(input_file);
        bitmap = BitmapFactory.decodeStream(srt);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        bitmapDrawable = new BitmapDrawable(bitmap);
        filePath = input_file.getPath();
        srt.close();
        outputStream.close();
        freeMemory();
    }
}
