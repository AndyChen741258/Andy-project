package com.naer.pdfreader;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.Snackbar;


import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.location.LocationRequest;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import Firebase.GetDataFromFirebase;
import Firebase.UploadStudentsBehavior;
import Model.StudentsSpeech;
import Others.diff_match_patch;
import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.ui.AIButton;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.logger.Logger;

import org.xml.sax.XMLReader;

public class DialogActivity extends Activity implements AIButton.AIButtonListener,OnMapReadyCallback {
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 1;
    public static final int LOCATION_UPDATE_MIN_TIME = 1000;
    public  int number;
    public static final String TAG = DialogActivity.class.getName();


    private AIButton aiButton;
    private TextView JudgeTextView;
    private TextView resultTextView;
    private TextView textResolt;
    private TextView textSpeech;
    private TextView textScore;
    private TextView PlaceName;
    private ImageView agentView;
    private ImageView tipview;
    private ImageView tipview1;
    private ImageView tipview2;

    //截圖
    private MediaProjection mMediaProjection;
    private ImageReader mImageReader;
    private VirtualDisplay mVirtualDisplay;
    private static Intent mResultData = null;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private CameraPreview mPreview;
    private Double longitude ;   //取得經度
    private Double latitude ;
    private SpeechRecognizer speechRecognizer;//內建語音辨識
    private List<String>SentencesList;//範例句子
    private static String CompareSentences = null;//設定比較的句子
    private static String PureSentences= null;//純句子


    public boolean textenable = false;

    public boolean tipenable = false;

    private LocationRequest locationRequest;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                longitude = location.getLongitude();   //取得經度
                latitude = location.getLatitude();    //取得緯度

                CheckLocation(latitude,longitude);//檢查地點是否在附近

                Logger.d(String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                Log.d("Number", String.valueOf(longitude) +" : "+ String.valueOf( latitude ));
                drawMarker(location);
              //  mLocationManager.removeUpdates(mLocationListener);
            } else {
                Logger.d("Place is null");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//隱藏標題列
        setContentView(com.naer.pdfreader.R.layout.activity_dialog);
        tipview = findViewById(com.naer.pdfreader.R.id.tipview);
        tipview1 = findViewById(com.naer.pdfreader.R.id.tipview1);
        tipview2 = findViewById(com.naer.pdfreader.R.id.tipview2);
        JudgeTextView = findViewById(com.naer.pdfreader.R.id.JudgeTextView);
        resultTextView = findViewById(com.naer.pdfreader.R.id.resultTextView);
        textResolt = findViewById(com.naer.pdfreader.R.id.textResult);
        textSpeech = findViewById(com.naer.pdfreader.R.id.textSpeech);
        textScore = findViewById(com.naer.pdfreader.R.id.textScore);
        PlaceName = findViewById(com.naer.pdfreader.R.id.PlaceName);
        aiButton = findViewById(com.naer.pdfreader.R.id.micButton);
        agentView = findViewById(com.naer.pdfreader.R.id.Agent);
        mMapView = findViewById(com.naer.pdfreader.R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隱藏狀態列

       // getCurrentLocation();

        initCamera();//預覽畫面

        createImageReader();

        ImportAgent();//加入代理人


      //  ImportTip2();

       // tipview2.setVisibility(View.INVISIBLE);
        JudgeTextView.setVisibility(View.INVISIBLE);

        Init_AIConfiguration();//初始化對話資料庫

        GetDataFromFirebase.DownSentencesKey();//下載範例句子

        GetDataFromFirebase.DownSentencesMap();//下載句子問題以及答案

        textResolt.setText("Welcome to this system, you can talk to me and scan items");
        textResolt.setVisibility(View.VISIBLE);

        TTS.init(getApplicationContext());

        new Handler().postDelayed(new Runnable(){
            public void run()
            {
                TTS.speak("Welcome to this system, you can talk to me and scan items");
               // textResolt.setVisibility(View.INVISIBLE);
            }
        }, 3500);


        InitSpeechRecognizer();//初始化聆聽者
        findViewById(com.naer.pdfreader.R.id.Speech).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetQuestions();
                //StartSpeechRecongizer();
            }
        });

        //ImageView的按鈕聆聽者
        tipview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textenable)
                {
                    tipenable = true;
                    CompareSentences = PureSentences;
                    StartSpeechRecongizer();
                }
               // String SpeechTTS =textSpeech.getText().toString();
               // TTS.speak(SpeechTTS);
            }
        });

        tipview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textenable)
                {
                    tipenable = false;
                    CompareSentences = textResolt.getText().toString().trim();
                    StartSpeechRecongizer();
                }
               // String ResoltTTS =textResolt.getText().toString();
              //  TTS.speak(ResoltTTS);
            }
        });

        //TextView的按鈕聆聽者
        textResolt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ResoltTTS =textResolt.getText().toString();
                TTS.speak(ResoltTTS);
               /* if(!textenable)
                {
                    tipenable = false;
                    CompareSentences = textResolt.getText().toString().trim();
                    StartSpeechRecongizer();
                }*/
            }
        });

        textSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SpeechTTS =textSpeech.getText().toString();
                TTS.speak(SpeechTTS);
              /*  if(!textenable)
                {
                    tipenable = true;
                    CompareSentences = PureSentences;
                    StartSpeechRecongizer();
                }*/
            }
        });
    }

    //設定隨機的範例問題
    private void SetQuestions()
    {
        textenable=false;
        SentencesList = GetDataFromFirebase.GetSentencesList();//從資料庫回傳，Lits跟陣列都是參考位置，意思說用 = 意思是參考他記憶體位子而不是複製。
        if(SentencesList !=null && SentencesList.size() !=0 && GetDataFromFirebase.SentencesMap !=null)
        {
            String Sentences = SentencesList.get((int) (Math.random()*SentencesList.size()));
            ImportTip();
            ImportTip1();
            tipview.setVisibility(View.VISIBLE);
            tipview1.setVisibility(View.VISIBLE);
            textResolt.setVisibility(View.VISIBLE);
            JudgeTextView.setVisibility(View.INVISIBLE);
            textResolt.setText(Sentences);//隨機撈取句子問題
            textSpeech.setVisibility(View.VISIBLE);
            textSpeech.setText(GetDataFromFirebase.SentencesMap.get(Sentences));
            PureSentences = (GetDataFromFirebase.SentencesMap.get(Sentences));//把沒有符號的句子存起來 提供比對，否則當答對後會加上正確率跟一堆符號
            TTS.speak(Sentences);
            SentencesList.remove(Sentences);
        }else
        {
            Toast.makeText(this, "You have already finish this sentences practice", Toast.LENGTH_SHORT).show();
            GetDataFromFirebase.DownSentencesKey();
        }
    }

    //初始化聆聽者
    private void InitSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);//建構值
        speechRecognizer.setRecognitionListener(new SpeechListener());// 設定聆聽者
    }

    //開啟內建語音辨識 用事件方式
    private void StartSpeechRecongizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en-US");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,3);
        speechRecognizer.startListening(intent);
    }

    //事件的內建語音辨識聆聽者
    private class SpeechListener implements RecognitionListener
    {
        @Override
        public void onReadyForSpeech(Bundle params) {
            if(tipenable == true){
                ImportMic();
            }
            else{
                ImportMic1();
            }
        }

        @Override
        public void onBeginningOfSpeech() {

            Log.d("Speech","Start...");
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.d("Speech","Received...");
        }

        @Override
        public void onEndOfSpeech() {
            ImportTip();
            ImportTip1();
            Log.d("Speech","End...");
        }

        @Override
        public void onError(int error) {
            ImportTip();
            ImportTip1();
        }

        @Override
        public void onResults(Bundle results) {
            ImportTip();
            ImportTip1();
           // tipview.setVisibility(View.VISIBLE);
           // tipview1.setVisibility(View.VISIBLE);
            textSpeech.setVisibility(View.VISIBLE);
            textScore.setVisibility(View.VISIBLE);
            String color = "green";//字體顏色
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            final String word =data.get(0).toString().trim();

            //if(textResolt !=null)
            if(CompareSentences !=null)
            {
                String response = word.replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                //String question = textResolt.getText().toString().replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                String question = CompareSentences.toString().replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                if(response.equals(question))
                {
                    JudgeTextView.setText(word);
                  //  textSpeech.setText(word);
                    int score = (int)(results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)[0]*100);
                    //textScore.setText("正確 : "+score+"/100");
                   // textSpeech.append(Html.fromHtml("<br>Correct: <font color=\""+ color +"\">" + score + "%</font>"));
                    JudgeTextView.setVisibility(View.VISIBLE);
                    JudgeTextView.append(Html.fromHtml("<br> <font color=\""+ color +"\">" + score + "%</font>"));
                    StudentsSpeech studentsSpeech = new StudentsSpeech(textResolt.getText().toString().trim(),word.trim(),score,"正確");
                    UploadStudentsBehavior.UploadSpeech(studentsSpeech);
                }else
                {
                    double len = question.length() > response.length() ? question.length() : response.length();//比較題目與答案字串長度
                    diff_match_patch diff_match_patch_obj = new diff_match_patch();//比對的Class
                    LinkedList<diff_match_patch.Diff> diff = diff_match_patch_obj.diff_lineMode(response, question, 3l);
                    Spanned recorrect_response = Html.fromHtml(diff_match_patch_obj.diff_prettyHtml(diff),null, new Html.TagHandler() {
                        int startTag;
                        int endTag;
                        @Override
                        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                            if (tag.equalsIgnoreCase("del")){
                                if(opening){
                                    startTag = output.length();
                                }else{
                                    endTag = output.length();
                                    output.setSpan(new StrikethroughSpan(),startTag,endTag, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }
                        }
                    });
                    JudgeTextView.setText(recorrect_response);
                 //   textSpeech.setText(recorrect_response);
                    double similarity = (int)((len - diff_match_patch_obj.diff_levenshtein(diff)) / len * 1000) / 10.0;
                    if(similarity >= 90.0){
                        color = "green";
                    }
                    else if(similarity >= 70.0){
                        color = "#FFA500";
                    }
                    else{
                        color = "red";
                    }
                   // textSpeech.append(Html.fromHtml("<br>Similarity: <font color=\""+ color +"\">" + similarity + "%</font>"));
                    JudgeTextView.setVisibility(View.VISIBLE);
                    JudgeTextView.append(Html.fromHtml("<br> <font color=\""+ color +"\">" + similarity + "%</font>"));
                    //StudentsSpeech studentsSpeech = new StudentsSpeech(textResolt.getText().toString().trim(),word.trim(),similarity,"錯誤");
                    StudentsSpeech studentsSpeech = new StudentsSpeech(CompareSentences.toString().trim(),word.trim(),similarity,"錯誤");
                    UploadStudentsBehavior.UploadSpeech(studentsSpeech);
                }
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }


    //檢查地點是否在範圍內，並改對話資料庫
    public void CheckLocation(Double latitude, Double longitude)
    {
        //Place 裡面的資料是目前的位置
        Place place = new Place();
        place.setLat(latitude);
        place.setLag(longitude);
        //初始化Key資料列表
        KeyList keyList = new KeyList();
        final List<KeyDatabase> keyDatabaseList = keyList.getKeyDatabaseList();
        //儲存未確認的地點，為了確認是否在未知地點
        List<Integer> Unidentified_List = new ArrayList<>();

        for(KeyDatabase keyDatabase : keyDatabaseList)
        {
            if(SearchMapService.check(place,keyDatabase.Latitude,keyDatabase.Longitude,keyDatabase.Radius)) {
                Unidentified_List.add(1);
                Config.ACCESS_TOKEN = keyDatabase.Key;
                Init_AIConfiguration();//更新對話資料庫
                String placeview = PlaceName.getText().toString();
                if(!placeview.equals(keyDatabase.Place)||placeview.isEmpty()) {
                     TTS.speak("You are in the "+ keyDatabase.Place +" now");
                    textResolt.setText("You are in the "+ keyDatabase.Place +" now");
                    textResolt.setVisibility(View.VISIBLE);
                    Place.GlobalPlace = keyDatabase.Place;//目前的地點
                    new Handler().postDelayed(new Runnable(){
                       public void run()
                        {
                            textResolt.setVisibility(View.INVISIBLE);
                        }
                    }, 5000);
               }
                PlaceName.setText((keyDatabase.Place));
            }
        }

        if(Unidentified_List.size() == 0)
        {
            PlaceName.setText("Unknown Location");
        }
    }

    public void Init_AIConfiguration()
    {
        final AIConfiguration config = new AIConfiguration(Config.ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        config.setRecognizerStartSound(getResources().openRawResourceFd(com.naer.pdfreader.R.raw.test_start));
        config.setRecognizerStopSound(getResources().openRawResourceFd(com.naer.pdfreader.R.raw.test_stop));
        config.setRecognizerCancelSound(getResources().openRawResourceFd(com.naer.pdfreader.R.raw.test_cancel));

        aiButton.initialize(config);
        aiButton.setResultsListener(this);


        TTS.init(getApplicationContext());
    }

    public static void setResultData(Intent mResultData) {
        DialogActivity.mResultData = mResultData;
    }

    //開啟預覽畫面
    private void initCamera() {
        CameraPreview mPreview = new CameraPreview(this);
        FrameLayout preview = findViewById(com.naer.pdfreader.R.id.camera_preview);
        preview.addView(mPreview);

    }

    //影像讀取
    private void createImageReader(){
        mImageReader = ImageReader.newInstance(2048, 1536,
                PixelFormat.RGBA_8888, 2);
    }

    //匯入代理人
    public void ImportAgent() {
        Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.standby).asGif()
                //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(agentView);
    }

    //匯入麥克風
    public void ImportMic() {
        Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.mic).asGif()
                //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(tipview);

    }

    public void ImportMic1() {
        Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.mic).asGif()
                //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(tipview1);

    }

    public void ImportTip() {
        Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.startspeak)
                //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(tipview);
    }
    public void ImportTip1(){
        Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.startspeak)
                //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(tipview1);
    }

    public void ImportTip2(){
        Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.tip).asGif()
                //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(tipview2);
    }

    @Override
    protected  void onStop(){
        super.onStop();
        mPreview = null;
        TTS.stop();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        InitSpeechRecognizer();
        if (mPreview == null)
        {
            initCamera();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        speechRecognizer.stopListening();
        speechRecognizer.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMapView.onPause();
        mPreview = null;
        // use this method to disconnect from speech recognition service
        // Not destroying the SpeechRecognition object in onPause method would block other apps from using SpeechRecognition service
        aiButton.pause();
        speechRecognizer.stopListening();
        speechRecognizer.destroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        InitSpeechRecognizer();
      //  if (mPreview == null)
       // {
         //   initCamera();
        //}

        mMapView.onResume();
        // use this method to reinit connection to recognition service
        aiButton.resume();

             new Handler().postDelayed(new Runnable(){
            public void run()
            {
                getCurrentLocation();
            }
        }, 5000);

    }


    //Dialog的回傳結果
    @Override
    public void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Result result = response.getResult();

                tipview2.setVisibility(View.INVISIBLE);
                tipview.setVisibility(View.INVISIBLE);
                tipview1.setVisibility(View.INVISIBLE);
                textSpeech.setVisibility(View.INVISIBLE);
                textScore.setVisibility(View.INVISIBLE);
                JudgeTextView.setVisibility(View.INVISIBLE);

                textenable = true;

                String question = result.getResolvedQuery();//說的字
                textSpeech.setText(question);

               // float score = result.getScore();//取準確度
               // textScore.setText(("準確度:" + String.format("%.4f", score-0.1)));
                //    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //    String date = sdf.format(new java.util.Date());

                  //  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("學生資料").child(Student.Name).child("精確度").child(date).child(question);
                  //  databaseReference.push().setValue(1);


                final String speech = result.getFulfillment().getSpeech();//回答的字
                TTS.speak(speech);
                textResolt.setText(speech);

                int speechnumble = speech.length();
                number=speechnumble;

                textSpeech.setVisibility(View.VISIBLE);
                if (speech.equals("")) {
                    textResolt.setVisibility(View.INVISIBLE); ;
                }
                else {
                    textResolt.setVisibility(View.VISIBLE);
                }

                ChangeAgent();
            }

        });
    }

    @Override
    public void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(DialogActivity.this,"請再說一次",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onCancelled() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onCancelled");
                resultTextView.setText("");
            }
        });
    }

    public void ChangeAgent(){
        switch(number) {
            case 0 :
                textResolt.setText("I can not understand,please say that again");
                TTS.speak("I can not understand,please say that again");
                textResolt.setVisibility(View.VISIBLE);
                textSpeech.setVisibility(View.INVISIBLE);
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.cantunderstand).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4000);
                break;
            case 1:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.thisis).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 6500);
                break;
            case 5:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 6:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 7:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 8:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 9:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 10:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 11:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 12:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 13:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 14:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 15:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 16:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 17:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 18:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 19:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 20:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 21:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 22:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 23:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 24:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 25:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat15).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 26:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 27:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 28:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 29:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 30:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 31:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 32:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 33:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 34:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 35:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 36:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 37:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 38:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 39:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 40:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 41:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 42:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 43:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 44:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 45:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat35).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 3500);
                break;
            case 46:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 47:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 48:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 49:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 50:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 51:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 52:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 53:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 54:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 55:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 56:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 57:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 58:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 59:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 60:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 61:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 62:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 63:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 64:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 65:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat55).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 4500);
                break;
            case 66:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 67:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 68:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 69:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 70:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 71:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 72:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 73:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 74:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 75:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 76:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 77:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 78:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 79:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 80:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 81:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 82:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 83:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 84:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 85:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat75).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 5000);
                break;
            case 86:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 87:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 88:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 89:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 90:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 91:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 92:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 93:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 94:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 95:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 96:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 97:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 98:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 99:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 100:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 101:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 102:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 103:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 104:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            case 105:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.chat95).asGif().dontAnimate()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                new Handler().postDelayed(new Runnable(){
                    public void run()
                    {
                        ImportAgent();
                    }
                }, 7000);
                break;
            default:
                Glide.with(DialogActivity.this).load(com.naer.pdfreader.R.drawable.teacher).asGif()
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(agentView);
                break;
        }
    }

    //截圖
    public void ScreenShotClick(final View view){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String date=sdf.format(new java.util.Date());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("學生資料").child("學生"+Student.Name+"號").child("截圖").child(date);
        databaseReference.push().setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DialogActivity.this,"上傳成功",Toast.LENGTH_LONG);
            }
        });

        Toast toast =Toast.makeText(this,"拍照成功",Toast.LENGTH_LONG);
        toast.show();
        photo();
    }

    public void PhotoClick(final View view){startActivity(new Intent(this,PhotoActivity.class));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1://QRcode
                if(resultCode==RESULT_OK)
                {
                    ImportTip2();
                  //  tipview2.setVisibility(View.VISIBLE);
                    number=99;
                    ChangeAgent();
                    new Handler().postDelayed(new Runnable(){
                        public void run()
                        {
                            // ZXing回傳的內容
                            String contents = data.getStringExtra("SCAN_RESULT");

                            tipview.setVisibility(View.INVISIBLE);
                            tipview1.setVisibility(View.INVISIBLE);
                            JudgeTextView.setVisibility(View.INVISIBLE);
                            textResolt.setText(contents);
                            TTS.speak(contents);
                            textSpeech.setVisibility(View.INVISIBLE);
                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                            String date=sdf.format(new java.util.Date());
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("學生資料").child("學生"+Student.Name+"號").child("QRcode").child(date).child(contents);
                            databaseReference.push().setValue(1);



                            if (contents.equals("")) {
                                textResolt.setVisibility(View.INVISIBLE); ;
                            }
                            else {
                                textResolt.setVisibility(View.VISIBLE);
                            } //execute the task
                        }
                    }, 2000);
                }
                else
                if(resultCode==RESULT_CANCELED)
                {
                    Toast.makeText(this, "取消掃描", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    //截圖
    public void photo() {
        // 延时以避免 mImageReader.acquireLatestImage() 返回 null
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //获取当前屏幕内容
                startVirtual();
            }
        }, 5);
        handler.postDelayed(new Runnable() {
            public void run() {
                //生成图片保存到本地
                startCapture();

            }
        }, 30);
    }

    public void startVirtual() {
        if (mMediaProjection != null) {
            virtualDisplay();
        } else {
            //mResultData是在Activity中用户授权后返回的结果
            mMediaProjection =getMediaProjectionManager().getMediaProjection(Activity.RESULT_OK, mResultData);
            virtualDisplay();
        }
    }

    private MediaProjectionManager getMediaProjectionManager() {

        return (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    private void startCapture() {

        Image image = mImageReader.acquireLatestImage();

        if (image == null) {
            photo();
        } else {
            doInBackground(image);
        }
    }

    private void virtualDisplay() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager WindowManager = (WindowManager) getApplication()
                .getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = WindowManager.getDefaultDisplay();
        defaultDisplay.getMetrics(metrics);
        int mScreenDensity = metrics.densityDpi;
        int mDisplayWidth = defaultDisplay.getWidth();
        int mDisplayHeight = defaultDisplay.getHeight();
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                mDisplayWidth, mDisplayHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
    }

    //儲存圖片
    protected void doInBackground(Image... params) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        if (params == null || params.length < 1 || params[0] == null) {

            return;
        }
        Image image = params[0];
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        //每个像素的间距
        int pixelStride = planes[0].getPixelStride();
        //总的间距
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        image.close();
        File fileImage = null;
        if (bitmap != null) {
            try {
                fileImage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + now + ".png");
                if (!fileImage.exists()) {
                    fileImage.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(fileImage);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(fileImage);
                media.setData(contentUri);
                sendBroadcast(media);
            } catch (IOException e) {
                e.printStackTrace();
                fileImage = null;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
    }

    //定位
    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled))
            Snackbar.make(mMapView, com.naer.pdfreader.R.string.error_location_provider, Snackbar.LENGTH_INDEFINITE).show();
        else {
            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null)
            drawMarker(location);
    }

    /* 在Google Map上放上目前位置的地標圖示。 */
    private void drawMarker(Location location) {
        if (mGoogleMap != null) {
            mGoogleMap.clear();
            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(gps)
                    .title("Current Position"));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 17));
        }
    }
    /*定時器
   Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            getCurrentLocation();
            handler.postDelayed(this, 1000);
        }
    };*/

    public void ScanClick(final View view){
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "SCAN_MODE");
        startActivityForResult(intent, 1);
    }
}

