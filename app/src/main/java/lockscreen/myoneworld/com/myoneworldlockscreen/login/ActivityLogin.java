package lockscreen.myoneworld.com.myoneworldlockscreen.login;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.registration.ActivityRegister;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.FILL_REQUIRED;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.INVALID_EMAIL;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.generateErrorLog;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.showLoginError;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.getCurrentTime;

public class ActivityLogin extends AppCompatActivity {
    Context mContext = this;
    EditText userNameLogin;
    EditText passwordLogin;
    Button loginButton;
    LinearLayout linearLogin;
    NetworkReceiver nr;
    TextView signUpLink;
    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init(){

        userNameLogin = findViewById(R.id.email_address);
        passwordLogin = findViewById(R.id.password_login);
        loginButton = findViewById(R.id.login_button);
        userNameLogin.addTextChangedListener(onTextChange);
        passwordLogin.addTextChangedListener(onTextChange);
        linearLogin = findViewById(R.id.linear_login);
        signUpLink = findViewById(R.id.signup_text);
        errorText = findViewById(R.id.login_text);

        signUpLink.setOnClickListener(v -> {
            try {
                startActivity(new Intent(mContext, ActivityRegister.class));
            }catch (Exception e){
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                String s = writer.toString();
                generateErrorLog(mContext,"err_log_" + getCurrentTime(),s);
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        nr = new NetworkReceiver(linearLogin,this,loginButton,2);
        registerReceiver(nr, intentFilter);

        LoginDAO login = new LoginDAO(mContext,this,errorText);
        loginButton.setOnClickListener(v ->{
            LoginVO vo = new LoginVO();

            if(!"".equals(userNameLogin.getText().toString()) && !userNameLogin.getText().toString().contains("@")){
                showLoginError(mContext,errorText,INVALID_EMAIL);
            }
            else if(!"".equals(userNameLogin.getText().toString()) && !"".equals(passwordLogin.getText().toString())) {
                vo.setEmail(userNameLogin.getText().toString());
                vo.setPassword(passwordLogin.getText().toString());
                login.login(vo);
            }
            else{
                showLoginError(mContext,errorText,FILL_REQUIRED);
            }
        });
    }


    TextWatcher onTextChange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!userNameLogin.getText().toString().equals("")) {
                userNameLogin.setBackgroundColor(Color.parseColor("#ffffff"));
                userNameLogin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_new_email_outline_orange,0,0,0);

            }else {
                userNameLogin.setBackgroundColor(Color.parseColor("#60ffffff"));
                userNameLogin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_new_email_outline_white,0,0,0);
            }
            if(!passwordLogin.getText().toString().equals("")) {
                passwordLogin.setBackgroundColor(Color.parseColor("#ffffff"));
                passwordLogin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_orange,0,0,0);

            }else {
                passwordLogin.setBackgroundColor(Color.parseColor("#60ffffff"));
                passwordLogin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_white,0,0,0);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext,ActivityLoginOptions.class));
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if(nr != null)
            unregisterReceiver(nr);
        super.onDestroy();
    }
}
