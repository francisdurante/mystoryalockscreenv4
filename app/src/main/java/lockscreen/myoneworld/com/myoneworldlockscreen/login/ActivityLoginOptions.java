package lockscreen.myoneworld.com.myoneworldlockscreen.login;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import java.util.Arrays;

import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.home.ActivityHome;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.CONSUMER_KEY;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.CONSUMER_SECRET;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GOOGLE_BUTTON;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.TWITTER_BUTTON;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MYSTORYA_BUTTON;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;

public class ActivityLoginOptions extends AppCompatActivity {
    TwitterConfig config;
    Context mContext = this;
    LoginButton facebookLoginButton;
    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    int RC_SIGN_IN = 411;
    TwitterLoginButton twitterLoginButton;
    Typeface font;
    LinearLayout linearLayout;
    int registerBroadcast = 0;
    NetworkReceiver nr;
    Button signInMyStoryaButton;
    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!getValueString("FULL_NAME",mContext).equals("") &&
                !getValueString("USER_ID",mContext).equals("") &&
                !getValueString("EMAIL",mContext).equals("")){
            registerBroadcast = 1;
            super.onCreate(savedInstanceState);
            nr = null;
            startActivity(new Intent(mContext,ActivityHome.class));
            finish();
        }else {

            config = new TwitterConfig.Builder(this)
                    .logger(new DefaultLogger(Log.DEBUG))
                    .twitterAuthConfig(new TwitterAuthConfig(CONSUMER_KEY, CONSUMER_SECRET))
                    .debug(true)
                    .build();
            Twitter.initialize(config);

            super.onCreate(savedInstanceState);
            font = Typeface.createFromAsset(getAssets(), "font/Century_Gothic.ttf");
            setContentView(R.layout.activity_login_options);
            linearLayout = findViewById(R.id.linear_login_option);
            errorText = findViewById(R.id.login_text);
            twitterInit();
            facebookInit();
            googleInit();
            myStoryaLogin();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            nr = new NetworkReceiver(linearLayout,this,signInButton,facebookLoginButton,twitterLoginButton,signInMyStoryaButton,1);
            registerReceiver(nr, intentFilter);
        }

    }

    private void facebookInit() {
        LoginDAO loginDAO = new LoginDAO(mContext, this,errorText);
        LoginVO vo = new LoginVO();
        LoginManager.getInstance().logOut();
        FacebookSdk.sdkInitialize(mContext);
        facebookLoginButton = findViewById(R.id.facebook_login);
        facebookLoginButton.setHeight(40);
        facebookLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        facebookLoginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        if (Build.VERSION.SDK_INT == 19) {
            facebookLoginButton.setBackgroundColor(Color.parseColor("#3B5998"));
            facebookLoginButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_facebook), null, null, null);
        }
        facebookLoginButton.setTextSize(12f);
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setTypeface(font);
        loginDAO.facebookLogin(facebookLoginButton, callbackManager, vo);
    }

    private void googleInit() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTypeface(font);
                tv.setText(GOOGLE_BUTTON);
                tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_google_button));
                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                if (Build.VERSION.SDK_INT == 19) {
                    tv.setBackgroundColor(Color.parseColor("#DC4E41"));
                    tv.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_google_plus), null, null, null);
                    tv.setPadding(0, 0, 55, 0);
                }
                tv.setTextColor(Color.parseColor("#FFFFFF"));
                tv.setTextSize(12f);
                tv.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                break;
            }
        }
        signInButton.setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.sign_in_button:
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                    break;
            }
        });
    }

    public void twitterInit(){
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter);
        twitterLoginButton.setText(TWITTER_BUTTON);
        twitterLoginButton.setTypeface(font);
        twitterLoginButton.setTextSize(12f);
        twitterLoginButton.setBackground(getResources().getDrawable(R.drawable.ic_twitter_button));
        twitterLoginButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
        if(Build.VERSION.SDK_INT == 19){
            twitterLoginButton.setBackgroundColor(Color.parseColor("#55ACEE"));
            twitterLoginButton.setPadding(0,0,55,0);
            twitterLoginButton.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_twitter),null,null,null);
        }
        twitterLoginButton.setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                LoginDAO loginDAO = new LoginDAO(mContext,ActivityLoginOptions.this,errorText);
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
                loginDAO.twitterLogin(session,result);
            }

            @Override
            public void failure(TwitterException exception) {
//                setButtonEnable(true);
                Toast.makeText(mContext,exception.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void myStoryaLogin(){
        signInMyStoryaButton = findViewById(R.id.sign_in_mystorya);
        signInMyStoryaButton.setTypeface(font);
        signInMyStoryaButton.setTextSize(12f);
        signInMyStoryaButton.setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
        signInMyStoryaButton.setText(MYSTORYA_BUTTON);
        signInMyStoryaButton.setOnClickListener(v -> {
            startActivity(new Intent(mContext,ActivityLogin.class));
            finish();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            LoginDAO loginDAO = new LoginDAO(mContext, this,errorText);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            loginDAO.googleSignIn(task);
            mGoogleSignInClient.signOut();
        }
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(nr != null){
            unregisterReceiver(nr);
        }
    }
}