package lockscreen.myoneworld.com.myoneworldlockscreen.twittershareReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

public class TwitterReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
            // success
            System.out.println("share to twitter success");
            final Long tweetId = intent.getExtras().getLong(TweetUploadService.EXTRA_TWEET_ID);
        } else if (TweetUploadService.UPLOAD_FAILURE.equals(intent.getAction())) {
            // failure
            System.out.println("share to twitter fail");

        } else if (TweetUploadService.TWEET_COMPOSE_CANCEL.equals(intent.getAction())) {
            // cancel
            System.out.println("share to twitter cancel");
        }
    }
}
