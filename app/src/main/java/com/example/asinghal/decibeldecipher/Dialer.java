package com.example.asinghal.decibeldecipher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class Dialer extends AppCompatActivity {
    private static final String TAG = "Audio Analyzer >>> ";
    private static Button startRecord;
    private static TextView resultText;
    private static TextView tipText;
    private static SpeechRecognizer sr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);

        // Request audio recording permission
        String permission = Manifest.permission.RECORD_AUDIO;
        if (checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{permission}, 101);
        }

        Typeface typeface = Typeface.createFromAsset(getAssets(),"FontAwesome.otf");
        startRecord = (Button)findViewById(R.id.startRecord);
        startRecord.setTypeface(typeface);
        startRecord.setOnTouchListener(rClickListener);
        resultText = (TextView)findViewById(R.id.resultText);
        tipText = (TextView)findViewById(R.id.tip);
        tipText.setText(R.string.tip_string);
    }

    private View.OnTouchListener rClickListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Drawable background = (Drawable)getResources().getDrawable(R.drawable.record_button_pressed);
                startRecord.setBackground(background);
                tipText.setText(R.string.listening);
                resultText.setText("");
                sr = SpeechRecognizer.createSpeechRecognizer(getBaseContext());
                sr.setRecognitionListener(new listener());
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
                sr.startListening(intent);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP){
                Drawable background = getResources().getDrawable(R.drawable.record_button);
                tipText.setText(R.string.tip_string);
                startRecord.setBackground(background);
                sr.stopListening();
            }
            return true;
        }
    };

    class listener implements RecognitionListener{
        public void onBeginningOfSpeech(){
            Log.d(TAG,"onBeginningOfSpeech");
        }
        public void onReadyForSpeech(Bundle bundle){
            Log.d(TAG,"onReadyForSpeech");
        }
        public void onRmsChanged(float rmsDb){
            Log.d(TAG,"onRmsChanged");
        }
        public void onBufferReceived(byte[] buffer){
            Log.d(TAG,"onBufferReceived");
        }
        public void onEndOfSpeech(){
            Log.d(TAG,"onEndOfSpeech");
        }
        public void onError(int error){
            Log.d(TAG,"***Error***" + error);
        }
        public void onResults(Bundle results){
            Log.d(TAG,"onResults");
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String speech = data.get(0).toString() + ".";
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<speech.length(); i++){
                if(i==0){
                    sb.append(Character.toUpperCase(speech.charAt(i)));
                }
                else{
                    sb.append(Character.toLowerCase(speech.charAt(i)));
                }
            }
            resultText.setText(sb.toString());
        }
        public void onPartialResults(Bundle partialResults){

        }
        public void onEvent(int eventType, Bundle params){

        }

    }
}
