package lockscreen.myoneworld.com.myoneworldlockscreen.briefing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GOTHIC_FONT_PATH;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;

import lockscreen.myoneworld.com.myoneworldlockscreen.Constant;
import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.login.ActivityLoginOptions;

public class ActivityBriefing extends AppCompatActivity {
    Context mContext = this;
    Button getStarted;
    TextView message;
    Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        font = Typeface.createFromAsset(getAssets(), GOTHIC_FONT_PATH);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_briefing);
        getStarted = findViewById(R.id.get_started);
        message = findViewById(R.id.txt_briefer);

        message.setTypeface(font);
        getStarted.setTypeface(font);
        if(getValueString("BRIEFING_LOCKSCREEN",mContext).equals("1")){
            startActivity(new Intent(mContext,ActivityLoginOptions.class));
            finish();
        }
        else {
            getStarted.setOnClickListener(v -> {
                save("BRIEFING_LOCKSCREEN","1",mContext);
                startActivity(new Intent(mContext,ActivityLoginOptions.class));
                finish();
            });
        }
    }
}
