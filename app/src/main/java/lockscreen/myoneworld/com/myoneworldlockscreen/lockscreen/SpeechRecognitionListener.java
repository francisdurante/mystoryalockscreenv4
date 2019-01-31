package lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;
import java.util.ArrayList;

import lockscreen.myoneworld.com.myoneworldlockscreen.articles.ActivityArticle;
import lockscreen.myoneworld.com.myoneworldlockscreen.webviews.ActivityWebView;

public class SpeechRecognitionListener implements RecognitionListener {
    private Context mContext;
    private Activity activity;
    private SpeechRecognizer mSpeechRecognizer;
    private TextView listeningText;
    private ViewPager viewPager;

    public SpeechRecognitionListener(Context context, Activity activity, SpeechRecognizer speechRecognizer,
    TextView textView, ViewPager viewPager){
        this.mContext = context;
        this.activity = activity;
        this.mSpeechRecognizer = speechRecognizer;
        this.listeningText = textView;
        this.viewPager = viewPager;
    }
    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onError(int error) {
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                Toast.makeText(mContext, "ERROR_AUDIO", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_CLIENT:
//                    Toast.makeText(mContext, "ERROR_CLIENT", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                Toast.makeText(mContext, "ERROR_RECOGNIZER_BUSY", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                Toast.makeText(mContext, "ERROR_INSUFFICIENT_PERMISSIONS", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                Toast.makeText(mContext, "ERROR_NETWORK_TIMEOUT", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                Toast.makeText(mContext, "ERROR_NETWORK", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_SERVER:
                Toast.makeText(mContext, "ERROR_SERVER", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                Toast.makeText(mContext, "ERROR_NO_MATCH", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                Toast.makeText(mContext, "ERROR_SPEECH_TIMEOUT", Toast.LENGTH_SHORT).show();
                break;
        }
        mSpeechRecognizer.cancel();
        listeningText.setVisibility(View.GONE);
        listeningText.clearAnimation();
    }

    @Override
    public void onResults(Bundle results) {
        mSpeechRecognizer.cancel();
        listeningText.setVisibility(View.GONE);
        listeningText.clearAnimation();
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches.contains("my phone unlock") || matches.contains("unlock")) {
            activity.finish();
            Log.d("Voice Command", "Unlocking");
            stopListening(mSpeechRecognizer,listeningText);
        } else if (matches.contains("show story")) {
            ActivityArticle.article_id = fileMyStoryId(viewPager);
            Intent articleActivity = new Intent(mContext, ActivityArticle.class);
            mContext.startActivity(articleActivity);
            activity.finish();
            Log.d("Voice Command", "show story");
            stopListening(mSpeechRecognizer,listeningText);
        }
        String forCommand = "";
        try {
            String fullCommand = "";
            for (int x = 0; x < matches.size(); x++) {
                if (matches.get(x).contains("my phone call ")) {
                    fullCommand = matches.get(x);
                    forCommand = "call1";
                    break;
                }
                if (matches.get(x).contains("my phone open ")) {
                    fullCommand = matches.get(x);
                    forCommand = "open1";
                    break;
                }
                if (matches.get(x).contains("call ")) {
                    fullCommand = matches.get(x);
                    forCommand = "call2";
                    break;
                }
                if (matches.get(x).contains("open ")) {
                    fullCommand = matches.get(x);
                    forCommand = "open2";
                    break;
                }
            }

            matches.clear();
            stopListening(mSpeechRecognizer,listeningText);
            String[] name = fullCommand.split("\\s+");

            switch (forCommand) {
                case "call1":
                    getVoiceCommandCall(forCommand, name);
                    stopListening(mSpeechRecognizer,listeningText);
                    break;
                case "call2":
                    getVoiceCommandCall(forCommand, name);
                    stopListening(mSpeechRecognizer,listeningText);
                    break;
                case "open1":
                    getVoiceCommandOpen(forCommand, name);
                    stopListening(mSpeechRecognizer,listeningText);
                    break;
                case "open2":
                    getVoiceCommandOpen(forCommand, name);
                    stopListening(mSpeechRecognizer,listeningText);
                    break;
                default:
                    stopListening(mSpeechRecognizer,listeningText);
                    break;

            }
        } catch (Exception ex) {
            stopListening(mSpeechRecognizer,listeningText);
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
    private void getVoiceCommandCall(String command, String[] fullCommand) {
        String nameToCall = "";
        if (command.equals("call1")) {
            try {
                if(fullCommand.length > 4){
                    for(int x = 0; x < fullCommand.length; x++){
                        if(x > 0)
                            nameToCall = nameToCall.concat(fullCommand[x]).concat(" ");
                    }
                }else{
                    nameToCall = fullCommand[3];
                }
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_DENIED) {
                    ArrayList contactNumbers = getPhoneNumber(nameToCall.trim(), mContext);
                    String[] contact = new String[contactNumbers.size()];
                    for(int i = 0; i < contactNumbers.size(); i++){
                        contact[i] = contactNumbers.get(i).toString();
                    }
                    if(contact.length > 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle(fullCommand[3].toUpperCase() + " have multiple contact");
                        builder.setItems(contact, (dialog, which) -> {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + contact[which]));
                            mContext.startActivity(intent);
                            stopListening(mSpeechRecognizer,listeningText);
                            activity.finish();
                        });
                        builder.show();
                    }else{
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + contact[0]));
                        mContext.startActivity(intent);
                        Log.d("Voice Command", "Call " + fullCommand[3]);
                        stopListening(mSpeechRecognizer,listeningText);
                        activity.finish();
                    }
                }
            }
            catch (Exception ex) {
                Toast.makeText(mContext, "Contact not found", Toast.LENGTH_SHORT).show();
                stopListening(mSpeechRecognizer,listeningText);
            }
        } else if ("call2".equals(command)) {
            try {
                if(fullCommand.length > 2){
                    for(int x = 0; x < fullCommand.length; x++){
                        if(x > 0)
                            nameToCall = nameToCall.concat(fullCommand[x]).concat(" ");
                    }
                }else{
                    nameToCall = fullCommand[1];
                }
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_DENIED) {
                    ArrayList contactNumbers = getPhoneNumber(nameToCall.trim(), mContext);
                    String[] contact = new String[contactNumbers.size()];
                    for(int i = 0; i < contactNumbers.size(); i++){
                        contact[i] = contactNumbers.get(i).toString();
                    }
                    if(contact.length > 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle(fullCommand[1].toUpperCase() + " have multiple contact");
                        builder.setCancelable(false);
                        builder.setItems(contact, (dialog, which) -> {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + contact[which]));
                            mContext.startActivity(intent);
                            Log.d("Voice Command", "Call " + fullCommand[1]);
                            stopListening(mSpeechRecognizer,listeningText);
                            activity.finish();
                        });
                        builder.show();
                    }else{
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + contact[0]));
                        mContext.startActivity(intent);
                        Log.d("Voice Command", "Call " + fullCommand[1]);
                        stopListening(mSpeechRecognizer,listeningText);
                        activity.finish();
                    }
                }

            } catch (Exception ex) {
                Toast.makeText(mContext, "Contact not found", Toast.LENGTH_SHORT).show();
                stopListening(mSpeechRecognizer,listeningText);
            }
        }
    }

    private void getVoiceCommandOpen(String command, String[] fullCommand) {
        int commandNumber = 3;
        if ("open2".equals(command)) {
            commandNumber = 1;
        }
        String commandOpen = "";
        final String appName;
        for (int x = 0; x < fullCommand.length; x++) {
            if (x >= commandNumber) {
                commandOpen = commandOpen.concat(fullCommand[x]);
            }
        }
        appName = commandOpen;
//        Utility.textToSpeechUtility(textToSpeech, "Opening " + appName);
        if ("mystory".equals(appName)) {
            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    try {
//                        ArticleActivity.article_id = fileMyStoryId();
//                        Intent articleActivity = new Intent(mContext, ArticleActivity.class);
//                        startActivity(articleActivity);
//                        finish();
//                        Log.d("Voice Command", "show story");
//                        stopListening();
                    } catch (Exception e) {
                        stopListening(mSpeechRecognizer,listeningText);
                    }
//                    Utility.textToSpeechDestroy(textToSpeech);
                }
            }.start();
        } else if ("mylifestyle".equals(appName)) {
            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    try {
                        ActivityWebView.url = MY_LIFE;
                        mContext.startActivity(new Intent(mContext, ActivityWebView.class));
                        activity.finish();
                        stopListening(mSpeechRecognizer,listeningText);
                    } catch (Exception e) {
                        stopListening(mSpeechRecognizer,listeningText);
                    }
//                    Utility.textToSpeechDestroy(textToSpeech);
                }
            }.start();
        } else if ("mycrazysale".equals(appName)) {
            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    try {
                        ActivityWebView.url = MYPHONE_SHOP;
                        mContext.startActivity(new Intent(mContext, ActivityWebView.class));
                        activity.finish();
                        stopListening(mSpeechRecognizer,listeningText);
                    } catch (Exception e) {
                        stopListening(mSpeechRecognizer,listeningText);
                    }
//                    Utility.textToSpeechDestroy(textToSpeech);
                }
            }.start();
        } else {
            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    try {
                        Intent i = mContext.getPackageManager().getLaunchIntentForPackage(getValueString(appName, mContext));
                        mContext.startActivity(i);
                        Log.d("Voice Command", "Open " + appName);
                        stopListening(mSpeechRecognizer,listeningText);
                        activity.finish();
                    } catch (Exception e) {
                        stopListening(mSpeechRecognizer,listeningText);
                    }
                }
            }.start();
        }
    }
}
