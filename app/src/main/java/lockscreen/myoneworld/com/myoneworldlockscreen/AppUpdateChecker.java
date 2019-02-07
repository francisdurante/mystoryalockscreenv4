package lockscreen.myoneworld.com.myoneworldlockscreen;

import android.content.Context;
import android.os.AsyncTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import static lockscreen.myoneworld.com.myoneworldlockscreen.splashscreen.ActivitySplashScreen.currentVersion;
import static lockscreen.myoneworld.com.myoneworldlockscreen.home.ActivityHome.updateStatus;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.PLAY_STORE_URL_VERSION_CHECKER;

public class AppUpdateChecker extends AsyncTask<Context, String, String> {
    private Context[] context;
    @Override
    protected String doInBackground(Context... contexts) {
        this.context = contexts;
        String newVersion = null;

        try {
            Document document = Jsoup.connect(PLAY_STORE_URL_VERSION_CHECKER)
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
            if (document != null) {
                Elements element = document.getElementsContainingOwnText("Current Version");
                for (Element ele : element) {
                    if (ele.siblingElements() != null) {
                        Elements sibElemets = ele.siblingElements();
                        for (Element sibElemet : sibElemets) {
                            newVersion = sibElemet.text();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newVersion;
    }

    @Override

    protected void onPostExecute(String onlineVersion) {
        super.onPostExecute(onlineVersion);
        if (onlineVersion != null && !onlineVersion.isEmpty()) {
            String newOnlineVersion = onlineVersion.replaceAll("\\.","");
            String newCurrentVersion = currentVersion.replaceAll("\\.","");
            if(Integer.parseInt(newOnlineVersion) > Integer.parseInt(newCurrentVersion)){
                updateStatus = "outdated";
            }else{
                updateStatus = "updated";
            }
        }else{
            updateStatus = "nothing"; // no internet
        }
        save("VERSION_ONLINE",updateStatus,context[0]);
    }
}
