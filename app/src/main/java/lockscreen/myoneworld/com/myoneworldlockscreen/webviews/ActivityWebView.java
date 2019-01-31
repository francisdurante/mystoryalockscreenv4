package lockscreen.myoneworld.com.myoneworldlockscreen.webviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import lockscreen.myoneworld.com.myoneworldlockscreen.R;

public class ActivityWebView extends AppCompatActivity {
    public static String url;
    WebView web;
    public ActivityWebView(){
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        web = findViewById(R.id.web_view);
        WebSettings settings = web.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        web.loadUrl(url);
        web.setWebViewClient(new MyBrowser());
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url){
            WebSettings settings = webView.getSettings();
            settings.setDomStorageEnabled(true);
            webView.loadUrl(url);
            return true;
        }
    }
    @Override
    public void onBackPressed() {
        if (web.canGoBack()) {
            web.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
