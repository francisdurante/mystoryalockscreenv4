package lockscreen.myoneworld.com.myoneworldlockscreen.registration;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.CallbackManager;

import java.util.Calendar;

import lockscreen.myoneworld.com.myoneworldlockscreen.R;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GOTHIC_FONT_PATH;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.isValidBirthday;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.setFont;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.FIRST_NAME_REQUIRED;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.LAST_NAME_REQUIRED;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.PHONE_NUMBER_REQUIRED;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.EMAIL_REQUIRED;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.ATLEAST_8_CHARACTERS;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.INVALID_DATE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.INVALID_EMAIL;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MISMATCH_PASSWORD;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DEFAULT_COUNTRY;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DEFAULT_ADDRESS;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DEFAULT_BIRTHDAY;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.showTopNotification;

public class ActivityRegister extends AppCompatActivity {

    String[] countries;
    TextView loggedIn ;
    private EditText firstName = null;
    private EditText lastName = null;
    private EditText email = null;
    private EditText password = null;
    private EditText reTypePassword = null;
    private EditText phoneNumber = null;
    private EditText address = null;
    private EditText birthday = null;
    private ImageButton birthdayIcon = null;
    private Button submit = null;
    private TextView signupHeader = null;
    private TextView textNotif = null;
    Context mContext = this;
    private Spinner spinnerCountry;
    static SharedPreferences spf;
    CallbackManager callbackManager;
    Typeface font;
    DatePickerDialog.OnDateSetListener mDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        font = setFont(mContext,GOTHIC_FONT_PATH);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init(){
        mContext = this;
        birthday = findViewById(R.id.birthdate);
        address = findViewById(R.id.address);
        spinnerCountry = findViewById(R.id.country);
        callbackManager = CallbackManager.Factory.create();
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        reTypePassword = findViewById(R.id.repassword);
        submit = this.findViewById(R.id.registration);
        phoneNumber = findViewById(R.id.phone_number);
        signupHeader = findViewById(R.id.text_registration);
        textNotif = findViewById(R.id.notif_message);
        birthdayIcon = findViewById(R.id.birthday_icon);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.right_enter);
        firstName.startAnimation(myanim);
        lastName.startAnimation(myanim);
        email.startAnimation(myanim);
        password.startAnimation(myanim);
        reTypePassword.startAnimation(myanim);
        submit.startAnimation(myanim);
        phoneNumber.startAnimation(myanim);
        birthday.startAnimation(myanim);
        address.startAnimation(myanim);
        spinnerCountry.startAnimation(myanim);
        firstName.setText("");
        lastName.setText("");
        email.setText("");

        firstName.setTypeface(font);
        lastName.setTypeface(font);
        email.setTypeface(font);
        password.setTypeface(font);
        reTypePassword.setTypeface(font);
        birthday.setTypeface(font);
        phoneNumber.setTypeface(font);
        submit.setTypeface(font);
        signupHeader.setTypeface(font);

        birthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!birthday.getText().toString().equals("")){
                    birthday.setBackgroundColor(Color.parseColor("#ffffff"));
                }else{
                    birthday.setBackgroundColor(Color.parseColor("#60ffffff"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!firstName.getText().toString().equals("")){
                    firstName.setBackgroundColor(Color.parseColor("#ffffff"));
                }else{
                    firstName.setBackgroundColor(Color.parseColor("#60ffffff"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!lastName.getText().toString().equals("")){
                    lastName.setBackgroundColor(Color.parseColor("#ffffff"));
                }else{
                    lastName.setBackgroundColor(Color.parseColor("#60ffffff"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!email.getText().toString().equals("")){
                    email.setBackgroundColor(Color.parseColor("#ffffff"));
                }else{
                    email.setBackgroundColor(Color.parseColor("#60ffffff"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!password.getText().toString().equals("")){
                    password.setBackgroundColor(Color.parseColor("#ffffff"));
                }else{
                    password.setBackgroundColor(Color.parseColor("#60ffffff"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        reTypePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!reTypePassword.getText().toString().equals("")){
                    reTypePassword.setBackgroundColor(Color.parseColor("#ffffff"));
                }else{
                    reTypePassword.setBackgroundColor(Color.parseColor("#60ffffff"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!phoneNumber.getText().toString().equals("")){
                    phoneNumber.setBackgroundColor(Color.parseColor("#ffffff"));
                }else{
                    phoneNumber.setBackgroundColor(Color.parseColor("#60ffffff"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            birthdayIcon.setOnClickListener(v -> {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ActivityRegister.this,
                        R.style.AppCompatAlertDialogStyle,
                        mDatePicker,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            });

            mDatePicker = (view, year, month, dayOfMonth) -> {
                month =+ 1;
                birthday.setText(year+"/"+month+"/"+dayOfMonth);
            };
        }
        spinnerCountry.setVisibility(View.INVISIBLE);
        address.setVisibility(View.INVISIBLE);
        submit.setOnClickListener(v -> {
            if (firstName.getText().toString().equals("")) {
                showTopNotification(mContext,textNotif,FIRST_NAME_REQUIRED);
            } else if (lastName.getText().toString().equals("")) {
                showTopNotification(mContext,textNotif,LAST_NAME_REQUIRED);
            } else if (email.getText().toString().equals("")) {
                showTopNotification(mContext,textNotif,EMAIL_REQUIRED);
            } else if (password.getText().length() < 8) {
                showTopNotification(mContext,textNotif,ATLEAST_8_CHARACTERS);
            } else if (!birthday.getText().toString().equals("") && !isValidBirthday(birthday.getText().toString())) {
                showTopNotification(mContext,textNotif,INVALID_DATE);
            } else if (!password.getText().toString().equals(reTypePassword.getText().toString())) {
                showTopNotification(mContext,textNotif,MISMATCH_PASSWORD);
            } else if (!email.getText().toString().contains("@")) {
                showTopNotification(mContext,textNotif,INVALID_EMAIL);
            } else if (phoneNumber.getText().toString().equals("")) {
                showTopNotification(mContext,textNotif,PHONE_NUMBER_REQUIRED);
            } else {
//                makeNotification("success",PLEASE_WAIT,ActivityRegister.this);
                RegistrationDAO registrationDAO = new RegistrationDAO(textNotif);
                RegistrationVO vo = new RegistrationVO();
                vo.setFirstName(firstName.getText().toString());
                vo.setLastName(lastName.getText().toString());
                vo.setEmail(email.getText().toString());
                vo.setPassword(password.getText().toString());
                vo.setContact(phoneNumber.getText().toString()); //(YYYY/MM/DD)
                vo.setBirthday(birthday.getText().toString().equals("") ? DEFAULT_BIRTHDAY : birthday.getText().toString());
                vo.setCountry(DEFAULT_COUNTRY);
                vo.setAddress(DEFAULT_ADDRESS);
                registrationDAO.registration(vo,mContext);
            }
        });
    }
}
