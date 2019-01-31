package lockscreen.myoneworld.com.myoneworldlockscreen;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import cz.msebera.android.httpclient.Header;

public class ApiClass {
    private AsyncHttpClient client = new AsyncHttpClient(true,80,443);

    public void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);

    }
    public void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);

    }
    public void getByUrlHeader(Context context,String url, Header[] header, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(context,url, header, params, responseHandler);
    }
}
