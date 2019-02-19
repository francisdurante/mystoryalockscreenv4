package lockscreen.myoneworld.com.myoneworldlockscreen.editprofile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;

import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.home.ActivityHome;
import lockscreen.myoneworld.com.myoneworldlockscreen.registration.ActivityRegister;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DEFAULT_BIRTHDAY;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GOTHIC_FONT_PATH;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.getValueString;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.editProfilePopUp;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.setFont;

public class ActivityEditProfile extends AppCompatActivity {
    private Context mContext = this;
    String[] countries;
    private Spinner spinnerCountry;
    private TextView tvFirstName;
    private TextView tvLastName;
    private TextView tvAddress;
    private TextView tvPhoneNumber;
    private TextView tvEmail;
    private TextView tvBirthday;
    private Button submitEdit;

    private String firstName;
    private String lastName;
    private String birthday;
    private String email;
    private String country;
    private String phoneNumber;
    private String address;
    private String userProfileID;
    private Typeface font;
    private ArrayAdapter<String> adapter;
    private String dealer;
    private DatePickerDialog.OnDateSetListener mDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        font = setFont(mContext,GOTHIC_FONT_PATH);
        super.onCreate(savedInstanceState);
        editProfilePopUp(mContext);
        setContentView(R.layout.activity_edit_profile);
        spinnerCountry = findViewById(R.id.country_edit);
        spinnerCountryItems();
    }

    public void spinnerCountryItems() {
        try {
            spinnerCountry.setOnItemSelectedListener(listener);
            InputStream is = getAssets().open("country.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String countryJson = new String(buffer, "UTF-8");

            JSONArray country = new JSONArray(countryJson);
            countries = new String[country.length()];
            for (int i = 0; i < country.length(); i++) {
                countries[i] = (country.getJSONObject(i).getString("name"));
            }
            adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_dropdown_item_1line, countries);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinnerCountry.setAdapter(adapter);
            init();
            spinnerCountry.setSelection(Integer.parseInt(this.country));
        }catch (Exception e){
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            String s = writer.toString();
        }
    }

    private void init(){
        Intent intent = getIntent();
        firstName = intent.getExtras().getString("FIRST_NAME");
        lastName = intent.getExtras().getString("LAST_NAME");
        address = intent.getExtras().getString("ADDRESS");
        phoneNumber = intent.getExtras().getString("PHONE_NUMBER");
        email = intent.getExtras().getString("EMAIL_ADDRESS");
        userProfileID = intent.getExtras().getString("USER_PROFILE_ID");
        birthday = intent.getExtras().getString("BIRTHDAY");
        country = intent.getExtras().getString("COUNTRY");
        dealer = intent.getExtras().getString("DEALER");

        tvFirstName = findViewById(R.id.first_name_edit);
        tvLastName = findViewById(R.id.last_name_edit);
        tvAddress = findViewById(R.id.address_edit);
        tvBirthday = findViewById(R.id.birth_date_edit);
        tvEmail = findViewById(R.id.email_edit);
        tvPhoneNumber = findViewById(R.id.phone_number_edit);
        submitEdit = findViewById(R.id.edit_submit);

        tvFirstName.addTextChangedListener(textWatcher);
        tvLastName.addTextChangedListener(textWatcher);
        tvAddress.addTextChangedListener(textWatcher);
        tvBirthday.addTextChangedListener(textWatcher);
        tvEmail.addTextChangedListener(textWatcher);
        tvPhoneNumber.addTextChangedListener(textWatcher);

        tvFirstName.setTypeface(font);
        tvLastName.setTypeface(font);
        tvAddress.setTypeface(font);
        tvBirthday.setTypeface(font);
        tvEmail.setTypeface(font);
        tvPhoneNumber.setTypeface(font);

        tvFirstName.setText(firstName);
        tvLastName.setText(lastName);
        tvAddress.setText(address);
        tvBirthday.setText(birthday);
        tvEmail.setText(email);
        tvPhoneNumber.setText(phoneNumber);

        tvBirthday.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);

            DatePickerDialog dialog = new DatePickerDialog(ActivityEditProfile.this,
                    R.style.AppCompatAlertDialogStyle,
                    mDatePicker,
                    year, month, day);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.show();
        });

        mDatePicker = (view, year, month, dayOfMonth) -> {
            month = month + 1;
            tvBirthday.setText(year+"-"+month+"-"+dayOfMonth);
        };

        submitEdit.setOnClickListener(v -> {
            if(!"".equals(tvFirstName.getText().toString()) &&
                    tvLastName.getText().toString().length() > 0 &&
                    tvEmail.getText().toString().length() > 0 &&
                    tvEmail.getText().toString().contains("@") &&
                    !tvBirthday.getText().toString().equals(DEFAULT_BIRTHDAY) &&
                    tvAddress.getText().toString().length() > 0 &&
                    tvPhoneNumber.getText().toString().length() > 0) {
                EditProfileVO vo = new EditProfileVO();
                EditProfileDAO dao = new EditProfileDAO();
                vo.setFirstName(tvFirstName.getText().toString());
                vo.setLastName(tvLastName.getText().toString());
                vo.setBirtday(tvBirthday.getText().toString());
                vo.setEmail(tvEmail.getText().toString());
                vo.setCountry(Integer.toString(adapter.getPosition(spinnerCountry.getSelectedItem().toString())));
                vo.setPhoneNumber(tvPhoneNumber.getText().toString());
                vo.setAddress(tvAddress.getText().toString());
                vo.setDealer(dealer);

                dao.sendEditProfile(mContext,vo,getValueString("ACCESS_TOKEN",mContext));
            }else{
                //invalid email
            }
        });
    }



    private AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(tvFirstName.getText().length() > 0){
                tvFirstName.setBackgroundColor(Color.parseColor("#ffffff"));
            }else{
                tvFirstName.setBackgroundColor(Color.parseColor("#60ffffff"));
            }
            if(tvLastName.getText().length() > 0){
                tvLastName.setBackgroundColor(Color.parseColor("#ffffff"));
            }else{
                tvLastName.setBackgroundColor(Color.parseColor("#60ffffff"));
            }
            if(tvAddress.getText().length() > 0){
                tvAddress.setBackgroundColor(Color.parseColor("#ffffff"));
            }else{
                tvAddress.setBackgroundColor(Color.parseColor("#60ffffff"));
            }
            if(tvBirthday.getText().length() > 0){
                tvBirthday.setBackgroundColor(Color.parseColor("#ffffff"));
            }else{
                tvBirthday.setBackgroundColor(Color.parseColor("#60ffffff"));
            }
            if(tvEmail.getText().length() > 0){
                tvEmail.setBackgroundColor(Color.parseColor("#ffffff"));
            }else{
                tvEmail.setBackgroundColor(Color.parseColor("#60ffffff"));
            }
            if(tvPhoneNumber.getText().length() > 0){
                tvPhoneNumber.setBackgroundColor(Color.parseColor("#ffffff"));
            }else{
                tvPhoneNumber.setBackgroundColor(Color.parseColor("#60ffffff"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(mContext,ActivityHome.class));
        finish();
    }
}
