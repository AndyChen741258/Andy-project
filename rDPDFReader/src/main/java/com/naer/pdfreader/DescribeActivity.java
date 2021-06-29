package com.naer.pdfreader;

import android.app.Activity;
import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
/*import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.animation.ImageMatrixProperty;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.client.util.Sleeper;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonElement;
import com.orhanobut.logger.Logger;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.slf4j.event.SubstituteLoggingEvent;
import org.xml.sax.XMLReader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import Firebase.GetDataFromFirebase;
import Firebase.UploadStudentsBehavior;
import Interface.IDataDownloadCompleted;
import Model.DescribeClickTime;
import Model.DescribeData;
import Model.SpeechData;
import Model.StudentsSpeech;
import Others.diff_match_patch;
import ai.api.AIServiceContext;
import ai.api.AIServiceContextBuilder;
import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIEvent;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.ResponseMessage;
import ai.api.model.Result;
import ai.api.ui.AIButton;
import lib.kingja.switchbutton.SwitchMultiButton;


import static android.view.View.INVISIBLE;
import static android.view.View.generateViewId;
import static com.naer.pdfreader.DialogActivity.LOCATION_UPDATE_MIN_DISTANCE;
import static com.naer.pdfreader.DialogActivity.LOCATION_UPDATE_MIN_TIME;
import static com.naer.pdfreader.DialogActivity.TAG;
import static com.naer.pdfreader.DramaAchievement.count;

import com.radaee.Broadcast.MyBroadcast;
import com.radaee.Model.StudentsNote;

public class DescribeActivity extends Activity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    //描述情境操作介面
    private Context context;
    private Button vocabulary;
    private Button phrase;
    private Button sentence;
    private TextView showdescribescore;
    private ImageView photo;
    private Button recordvoice;
    private Button takephoto;
    private Button savedescribe;
    private Button inputClear;
    private String testAI;
    private ListView listview;
    private Button example_speach_tts;
    private Button select_alltext;

    //--------學生選擇紀錄學習的類型:單字/片語/句子------------
    private TextView choose_which;
    private Spinner choose_type;
    public static String choose_type_word;

    //--------紀錄點擊次數------------
    int vocabulary_clickTime = 0;
    int phrase_clickTime = 0;
    int sentence_clickTime = 0;
    int see_other_clickTime = 0;

    //進入觀看有兩個途徑 1. 每當按下儲存紀錄時 2. 在描述時想看別人如何描述可以點選觀看
    //這個變數要判斷是否為第二種情況時進入, 如果是, 則在觀看的地方出現指定的其他人的資料
    public static boolean isWantToLearn = false;


    private String[] sssttt = {"a", "b", "c"};
    private String wordoflist;

    //--------自動提示TextView------------
    private AutoCompleteTextView studentdescribe;
    private String[] autoStrs = {"a", "b", "c"};
    private GetDataFromFirebase getDataFromFirebase;
    private List<String> SentencesList;
    ArrayAdapter<String> adapter;


    public String Link;

    //-------口說準確度------------
    private SpeechRecognizer speechRecognizer;//內建語音辨識
    public boolean textenable = false;
    public boolean tipenable = false;
    private static String CompareSentences = null;//設定比較的句子
    private static String PureSentences = null;//純句子
    private String encourage;

    //-------定義FireBase------------
    //情境描述資料庫
    private DatabaseReference fire_describedata;
    //情境描述口說準確率資料庫
    private DatabaseReference fire_speechdata;
    //Storage存放情境照片
    private StorageReference fire_picture;
    //單字資料
    private DatabaseReference fire_vocabulary;


    public int number;
    private String download_url = "";


    //-----------地點偵測-----------
    private Double longitude;   //取得經度
    private Double latitude;    //取得緯度
    public static TextView PlaceName;
    public static String placeview;


    // Java V2
    private String uuid = UUID.randomUUID().toString();
    private SessionsClient sessionsClient;
    private SessionName session;

    //描述修改過程, TTS紀錄
    private DatabaseReference fire_process_write_listen;
    private DatabaseReference fire_listen_tts_time;

    private DatabaseReference fire_translation_time;

    //要做作業時的錄音工具
    private boolean isShowOrNot = false; //點一下出現toolbar 點一下消失
    private Button toolbar_show;
    private Spinner week1_hw_record;
    private Button toolbar_record;
    private Button toolbar_stop;
    private Button toolbar_play;
    private String hw1_list_word;

    private MediaRecorder recorder;
    private MediaPlayer player;
    private String hw_describe_pathword;  //描述作業錄音儲存檔名
    private StorageReference fire_hw_describe_record;
    private String hw_describe_download_url = "";
    private boolean hw_isPress = true; //限制錄音按鈕僅能按一下, 等按下停止時才可以再恢復可按狀態
    private String hw_describe_Link;

    //創作戲劇時的觀看資料行為
    private DatabaseReference fire_creatdrama_behavior;
    int creat_vocabulary = 0;
    int creat_phrase = 0;
    int creat_sentence = 0;
    int creat_see_other = 0;

    //觀看範例影片
    private Button video;
    private Uri vidUri;
    private int stopPosition=0;
    private boolean stop = false;


    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            final Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm", now);
            if (location != null) {
                int firstMarker = 0;
                longitude = location.getLongitude();   //取得經度
                latitude = location.getLatitude();    //取得緯度
                CheckLocation(latitude, longitude);//檢查地點是否在附近
                Logger.d(String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                Log.d("hooooo", String.valueOf(longitude) + " : " + String.valueOf(latitude));


//                sentence.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View view) {
//                        test1.setText(latitude.toString());
//                        test2.setText(longitude.toString());
//                    }
//                });

                //(Carol) drawMarker(location);
                //Update to Firebase
//                geoFire.setLocation(Student.Name + now, new GeoLocation(latitude, longitude),//定位點丟到firebase以學生座號+年月日時分
//                       new GeoFire.CompletionListener(){
//                            public void onComplete(String key, DatabaseError error){
//                                //mGoogleMap.setMyLocationEnabled(true);
//                                //Add Marker
//                                if(mCurrent != null)
//                                    mCurrent.remove(); //remove old marker
//                                mCurrent = mGoogleMap.addMarker(new MarkerOptions()
//                                                        .position(new LatLng(latitude, longitude))
//                                                        .title("You"));
//                            }
//                        });


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
    private int photo_count = 0;
    private int showdescribescore_count = 0;
    private int tts_count = 0;
    private double toLng = 0;
    private double tplat = 0;
    private GoogleMap mMap;
    private MapFragment mapFragment;
    private TextView place_tell;
    private Button addmarker;
    //地圖自訂定位點
    private boolean firstmarker = false;
    private boolean bol_correctmarker = false;
    //地圖定位點總數
    private int marker_count;
    private int int_marker=0;
    private String[] marker_left = new String[25];
    private String[] marker_right = new String[25];
    private String[] marker_name = new String[25];
    private String[] marker_time = new String[25];
    private String situation_word;
    private VideoView video_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//隱藏標題列
        setContentView(R.layout.activity_describe);
        vocabulary = findViewById(R.id.vocabulary);
        phrase = findViewById(R.id.phrase);
        sentence = findViewById(R.id.sentence);
        studentdescribe = findViewById(R.id.studentdescribe);
        showdescribescore = findViewById(R.id.showdescribescore);
        photo = findViewById(R.id.photo);
        recordvoice = findViewById(R.id.recordvoice);
        takephoto = findViewById(R.id.takephoto);
        savedescribe = findViewById(R.id.savedescribe);
        inputClear = findViewById(R.id.inputClear);
        select_alltext = findViewById(R.id.select_alltext);

        listview = findViewById(R.id.listview);
        example_speach_tts = findViewById(R.id.example_speech_tts);

        choose_which = findViewById(R.id.choose_which);
        choose_type = findViewById(R.id.choose_type);

        place_tell = findViewById(R.id.place_tell);

        addmarker = findViewById(R.id.addmarker);
        video = findViewById(R.id.video);

        //--------GPS顯示地點及經緯度--------
        PlaceName = findViewById(R.id.place);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //--------錄音作業--------
        toolbar_show = findViewById(R.id.toolbar_show); //按下才顯示下面的錄音按鈕
        week1_hw_record = findViewById(R.id.week1_hw_record);
        toolbar_record = findViewById(R.id.toolbar_record);
        toolbar_stop = findViewById(R.id.toolbar_stop);
        toolbar_play = findViewById(R.id.toolbar_play);
        //初始化隱藏
//        toolbar_show.setVisibility(INVISIBLE);
        week1_hw_record.setVisibility(INVISIBLE);
        toolbar_record.setVisibility(INVISIBLE);
        toolbar_stop.setVisibility(INVISIBLE);
        toolbar_play.setVisibility(INVISIBLE);

        toolbar_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowOrNot == false) {
                    Log.v("測試","執行");
                    week1_hw_record.setVisibility(View.VISIBLE);
                    toolbar_record.setVisibility(View.VISIBLE);
                    toolbar_stop.setVisibility(View.VISIBLE);
                    toolbar_play.setVisibility(View.VISIBLE); //設置顯示
                    isShowOrNot = true;
                } else {
                    week1_hw_record.setVisibility(INVISIBLE);
                    toolbar_record.setVisibility(INVISIBLE);
                    toolbar_stop.setVisibility(INVISIBLE);
                    toolbar_play.setVisibility(INVISIBLE); //設置隱藏
                    isShowOrNot = false;
                }
            }
        });

        readInfo(Student.Name + "號學生描述情境行為紀錄");

        //地圖
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this::onMapReady);

        //影片
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video();
            }
        });

        


        addmarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder spinnerDialog = new AlertDialog.Builder(DescribeActivity.this);
                spinnerDialog.setTitle("選擇定位點");
                spinnerDialog.setIcon(R.drawable.ic_launcher);
                //情境選單
                final String[] stunum_list = new String[] {"playground", "classroom", "home"};
                final Spinner situation_num = new Spinner(DescribeActivity.this);
                ArrayAdapter<String> stu_numberList=new ArrayAdapter<String>(DescribeActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, stunum_list);
                situation_num.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                situation_num.setAdapter(stu_numberList);
                situation_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        situation_word = situation_num.getSelectedItem().toString();
                        
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
                        situation_word = situation_num.getSelectedItem().toString();
                    }
                });


                spinnerDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        LatLng sydney = new LatLng(latitude, longitude);
                        //取得現在時間(日期)
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String date = sdf.format(new java.util.Date());
                        mMap.addMarker(new MarkerOptions()
                                .position(sydney)
                                .title(situation_word)
                                .snippet("日期:"+date+"\n"+"座號:"+Student.Name));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18.0f));
                        marker_left [int_marker]= String.valueOf(latitude);
                        marker_right [int_marker]= String.valueOf(longitude);
                        marker_name [int_marker]= situation_word;
                        marker_time [int_marker] = date;
                        int_marker++;
                    }
                });
                spinnerDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                spinnerDialog.setView(situation_num);
                spinnerDialog.show();
            }
        });


        //--------錄音作業選單--------
        final String[] activity1_list = {"Homework_1_1", "Homework_1_2", "Homework_1_3", "Homework_2_1", "Homework_2_2", "Homework_3_1", "Homework_4_1"};
        ArrayAdapter<String> hwList = new ArrayAdapter<String>(DescribeActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                activity1_list);
        week1_hw_record.setAdapter(hwList);
        week1_hw_record.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hw1_list_word = week1_hw_record.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
                hw1_list_word = week1_hw_record.getSelectedItem().toString();
            }
        });


        //--------選擇紀錄類型 單字 片語 句子----------
        final String[] choose_list = {"Vocabulary", "Phrase", "Sentence"};
        ArrayAdapter<String> typeList = new ArrayAdapter<String>(DescribeActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                choose_list);
        choose_type.setAdapter(typeList);
        choose_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose_type_word = choose_type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
                choose_type_word = choose_type.getSelectedItem().toString();
            }
        });

        //--------自動提示功能
        studentdescribe = (AutoCompleteTextView) findViewById(R.id.studentdescribe);

        //new ArrayAdapter物件並將autoStr字串陣列傳入studentdescribe中(初始化)(後面第481行在塞新的值)
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, autoStrs);
        studentdescribe.setAdapter(adapter);

        TTS.init(getApplicationContext());
        initDescribeData();
        Init_AIConfiguration();//初始化對話資料庫

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());

        fire_process_write_listen = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("Describe_process").child(date);
        studentdescribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fire_process_write_listen.child(PlaceName.getText().toString()).child(studentdescribe.getText().toString().replaceAll("[,|.|!|?|']", "").trim()).push().setValue("fix");
            }
        });

        //戲劇觀看紀錄行為firebase初始化
        fire_creatdrama_behavior = FirebaseDatabase.getInstance().getReference()
                .child("學生" + Student.Name + "號").child("CreateDrama_Behavior").child(date);


        select_alltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentdescribe.selectAll();
                //紀錄按下全選的內容 = 翻譯內容
                fire_translation_time = FirebaseDatabase.getInstance().getReference();
                fire_translation_time.child("學生" + Student.Name + "號").child("Describe_Translate").child(date)
                        .push().setValue(studentdescribe.getText().toString().replaceAll("[,|.|!|?|']", "").trim());
            }
        });

        vocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vocabulary_clickTime++;

                //創作戲劇時觀看的資料次數
                creat_vocabulary++;
                fire_creatdrama_behavior.child("Vocabulary").child("ClickTime").setValue(creat_vocabulary);

                fire_vocabulary.child(PlaceName.getText().toString()).child("vocabulary").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int i = 0;
                        sssttt = new String[(int) dataSnapshot.getChildrenCount()];
                        for (DataSnapshot each : dataSnapshot.getChildren()) {
                            sssttt[i] = each.getValue().toString();
                            i++;
                        }
                        String[] st = sssttt;
                        ArrayAdapter list_ref = new ArrayAdapter(DescribeActivity.this, android.R.layout.simple_list_item_1, st);
                        listview.setAdapter(list_ref);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                int index = studentdescribe.getSelectionStart();
                                Editable sss = studentdescribe.getText();
                                sss.insert(index, st[i] + " ");

                                //創作戲劇時觀看的資料內容
                                fire_creatdrama_behavior.child("Vocabulary").push().setValue(st[i]);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        phrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phrase_clickTime++;

                //創作戲劇時觀看的資料次數
                creat_phrase++;
                fire_creatdrama_behavior.child("Phrase").child("ClickTime").setValue(creat_phrase);

                fire_vocabulary.child(PlaceName.getText().toString()).child("phrase").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int i = 0;
                        sssttt = new String[(int) dataSnapshot.getChildrenCount()];
                        for (DataSnapshot each : dataSnapshot.getChildren()) {
                            sssttt[i] = each.getValue().toString();
                            i++;
                        }
                        String[] st = sssttt;
                        ArrayAdapter list_ref = new ArrayAdapter(DescribeActivity.this, android.R.layout.simple_list_item_1, st);
                        listview.setAdapter(list_ref);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                int index = studentdescribe.getSelectionStart();
                                Editable sss = studentdescribe.getText();
                                sss.insert(index, st[i] + " ");

                                //創作戲劇時觀看的資料內容
                                fire_creatdrama_behavior.child("Phrase").push().setValue(st[i]);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        sentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentence_clickTime++;

                //創作戲劇時觀看的資料次數
                creat_sentence++;
                fire_creatdrama_behavior.child("Sentence").child("ClickTime").setValue(creat_sentence);

                fire_vocabulary.child(PlaceName.getText().toString()).child("sentence").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int i = 0;
                        sssttt = new String[(int) dataSnapshot.getChildrenCount()];
                        for (DataSnapshot each : dataSnapshot.getChildren()) {
                            sssttt[i] = each.getValue().toString();
                            i++;
                        }
                        String[] st = sssttt;
                        ArrayAdapter list_ref = new ArrayAdapter(DescribeActivity.this, android.R.layout.simple_list_item_1, st);
                        listview.setAdapter(list_ref);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                int index = studentdescribe.getSelectionStart();
                                Editable sss = studentdescribe.getText();
                                sss.insert(index, st[i] + " ");
//                                studentdescribe.append(st[i]+" ");

                                //創作戲劇時觀看的資料內容
                                fire_creatdrama_behavior.child("Sentence").push().setValue(st[i]);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        //拍情境照片
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //照片庫
                    if (ContextCompat.checkSelfPermission(DescribeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(DescribeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {  //拍照
                    BringImagePicker();
                }
            }
        });

        //錄音口說準確率
        recordvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textenable) {
                    tipenable = false;
                    showdescribescore_count++;
                    CompareSentences = studentdescribe.getText().toString().trim();
                    StartSpeechRecongizer();
                }
                // String ResoltTTS =textResolt.getText().toString();
                //  TTS.speak(ResoltTTS);
            }
        });

        example_speach_tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts_count++;
                TTS.speak(studentdescribe.getText().toString());
                //fire_process_write_listen.child(studentdescribe.getText().toString().replaceAll("[,|.|!|?|']", "").trim()).push().setValue("TTS");
                fire_listen_tts_time.child(choose_type_word).child("TTStime")
                        .push().setValue(studentdescribe.getText().toString().replaceAll("[,|.|!|?|']", "").trim());
            }
        });

        inputClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentdescribe.setText("");
            }
        });

        //作業錄音部分
        toolbar_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hw_isPress == true) {
                    recorder = new MediaRecorder();// new出MediaRecorder物件
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    // 設定MediaRecorder的音訊源為麥克風
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
                    // 設定MediaRecorder錄製的音訊格式
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    // 設定MediaRecorder錄製音訊的編碼為amr.

                    hw_describe_pathword = Student.Name + "_" + hw1_list_word + ".amr";
                    recorder.setOutputFile("/sdcard/" + hw_describe_pathword);

                    // 設定錄製好的音訊檔案儲存路徑
                    try {
                        recorder.prepare();// 準備錄製
                        recorder.start();// 開始錄製
                        Toast.makeText(DescribeActivity.this, "開始錄音" + hw1_list_word, Toast.LENGTH_SHORT).show();

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    hw_isPress = false;
                } else {
                    Toast.makeText(DescribeActivity.this, "你已經正在錄音囉！", Toast.LENGTH_SHORT).show();
                }
            }
        });


        toolbar_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hw_isPress == false) {
                    hw_describe_pathword = Student.Name + "_" + hw1_list_word + ".amr";
                    fire_hw_describe_record = FirebaseStorage.getInstance().getReference()
                            .child(Student.Name).child(hw1_list_word + "/" + hw_describe_pathword);

                    recorder.stop();// 停止燒錄
                    Toast.makeText(DescribeActivity.this, "停止錄音" + hw1_list_word, Toast.LENGTH_SHORT).show();

                    //上傳錄音檔至Firebase, 路徑為:"座號/Homework_1_1", 檔名為"學生座號_Homework_1_1.amr"
                    //並使用continueWithTask接回錄音檔url, 當按下播放鍵可以聽見錄音
                    //加上記錄聽錄音的次數???
                    Uri hw_describe_recoed_file = Uri.fromFile(new File("/sdcard/" + hw_describe_pathword));
                    fire_hw_describe_record.putFile(hw_describe_recoed_file).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return fire_hw_describe_record.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri uri = task.getResult();
                            hw_describe_download_url = uri.toString();
                        }
                    }).continueWithTask(new Continuation<Uri, Task<Void>>() {
                        @Override
                        public Task<Void> then(@NonNull Task<Uri> task) throws Exception {

                            return null;
                        }
                    });
                    // recorder.reset(); // 重新啟動MediaRecorder.
                    recorder.release(); // 燒錄完成一定要釋放資源
                    recorder = null;

                    hw_isPress = true;
                } else {
                    Toast.makeText(DescribeActivity.this, "要先錄音哦！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toolbar_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fire_hw_describe_record = FirebaseStorage.getInstance().getReference()
                        .child(Student.Name).child(hw1_list_word + "/" + hw_describe_pathword);
                fire_hw_describe_record.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        hw_describe_Link = uri.toString();
                        Toast.makeText(DescribeActivity.this, "播放錄音" + hw1_list_word, Toast.LENGTH_SHORT).show();
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(hw_describe_Link);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DescribeActivity.this, "尚未錄音", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void sentenceClick(final View view) {
        see_other_clickTime++;

        creat_see_other++;
        fire_creatdrama_behavior.child("see_other").child("ClickTime").setValue(creat_vocabulary);

        isWantToLearn = true;
        Intent intent = new Intent(this, DramaRecycleView.class);
        startActivity(intent);
    }

    //初始化描述情境資料庫
    private void initDescribeData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new java.util.Date());

        fire_describedata = FirebaseDatabase.getInstance().getReference()
                .child("學生" + Student.Name + "號").child("DescribeData").child(date);

//        fire_speechdata = FirebaseDatabase.getInstance().getReference()
//                .child("學生"+Student.Name+"號").child("SpeechData").child(date);

        fire_speechdata = FirebaseDatabase.getInstance().getReference()
                .child("學生" + Student.Name + "號").child("DescribeData").child(date);

        fire_listen_tts_time = FirebaseDatabase.getInstance().getReference()
                .child("學生" + Student.Name + "號").child("DescribeData").child(date);

        fire_vocabulary = FirebaseDatabase.getInstance().getReference();


    }

    //CropImage切割照片
    private void BringImagePicker() {
        CropImage.activity().
                setGuidelines(CropImageView.Guidelines.ON).
                setAspectRatio(1, 1).
                start(DescribeActivity.this);

    }

//讀格式
//    public String getImageExt(Uri uri) {
//        ContentResolver contentResolver = getContentResolver();
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                photo_count++;
                photo.setBackground(null);
                photo.setImageURI(imageUri);
                Bitmap bitmap = decodeUriiAsBimap(this, imageUri);
                String dd = System.currentTimeMillis() + ".jpeg";
                fire_picture = FirebaseStorage.getInstance().getReference().child("DescribeImage/" + dd);

                //continueWithTask取得圖片的url
                fire_picture.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return fire_picture.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri uri = task.getResult();
                        download_url = uri.toString();
                    }
                }).continueWithTask(new Continuation<Uri, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Uri> task) throws Exception {
                        String push_key = fire_describedata.push().getKey();

                        //這一步Task當按下儲存鈕時才存所有資料進FireBase
                        savedescribe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DescribeData describeData = new DescribeData(
                                        "學生" + Student.Name + "號",
                                        PlaceName.getText().toString(),
                                        studentdescribe.getText().toString(),
                                        download_url
                                );
                                DescribeClickTime describeClickTime = new DescribeClickTime(
                                        vocabulary_clickTime, phrase_clickTime, sentence_clickTime, see_other_clickTime);

                                fire_describedata.child(choose_type_word).child(push_key).child("Content").setValue(describeData); //紀錄描述內容
                                fire_describedata.child(choose_type_word).child(push_key).child("ClickTime").setValue(describeClickTime); //記錄過程中各個點擊次數
                                Toast.makeText(DescribeActivity.this, "情境描述已儲存", Toast.LENGTH_SHORT).show();

                                //儲存完就將此頁面關閉 並開啟觀看介面
//                                   isWantToLearn = false;
                                Intent intent = new Intent();
                                intent.setClass(DescribeActivity.this, DramaRecycleView.class);
                                startActivity(intent);
                                DescribeActivity.this.finish();
                                writeInfo(Student.Name+"號學生描述情境行為紀錄","描述情境");

                            }
                        });
                        return null;
                    }
                });
                Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private Bitmap decodeUriiAsBimap(DescribeActivity describeActivity, Uri imageUri) {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(describeActivity.getContentResolver().openInputStream(imageUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    //檢查地點是否在範圍內，並改對話資料庫
    public void CheckLocation(Double latitude, Double longitude) {
        //Place 裡面的資料是目前的位置
        Place place = new Place();
        place.setLat(latitude);
        place.setLag(longitude);
        //初始化Key資料列表
        KeyList keyList = new KeyList();
        final List<KeyDatabase> keyDatabaseList = keyList.getKeyDatabaseList();
        //儲存未確認的地點，為了確認是否在未知地點
        List<Integer> Unidentified_List = new ArrayList<>();

        for (KeyDatabase keyDatabase : keyDatabaseList) {
            if (SearchMapService.check(place, keyDatabase.Latitude, keyDatabase.Longitude, keyDatabase.Radius)) {
                Unidentified_List.add(1);
                //Config.ACCESS_TOKEN = keyDatabase.Key;
                Init_AIConfiguration();//更新對話資料庫
                placeview = PlaceName.getText().toString();
                if (!placeview.equals(keyDatabase.Place) || placeview.isEmpty()) {
                    // TTS.speak("You are in the "+ keyDatabase.Place +" now");
                    // textResolt.setText("You are in the "+ keyDatabase.Place +" now");
                    // textResolt.setVisibility(View.VISIBLE);
                    Place.GlobalPlace = keyDatabase.Place;//目前的地點
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            //reference.setVisibility(View.INVISIBLE);
                        }
                    }, 5000);
                }
                if (!bol_correctmarker) {
                    PlaceName.setText((keyDatabase.Place));
                }


                //抓到PlaceName後，讀取相對應的資料塞進String陣列中，在Adapter進AutoCompleteTextview中
                fire_vocabulary.child(PlaceName.getText().toString()).child("sentence").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int i = 0;
                        autoStrs = new String[(int) dataSnapshot.getChildrenCount()];   //若擺在for迴圈中會每一次都洗掉 抓不到值 null
                        for (DataSnapshot each : dataSnapshot.getChildren()) {
                            autoStrs[i] = each.getValue().toString();
                            i++;
                        }
                        String[] auto = autoStrs;
                        adapter = new ArrayAdapter<String>(DescribeActivity.this, android.R.layout.simple_dropdown_item_1line, auto);
                        studentdescribe.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }

        if (Unidentified_List.size() == 0 && bol_correctmarker == false) {
            PlaceName.setText("Other");
        }

        if (PlaceName.getText().toString().equals("") == false && firstmarker == false) {
            LatLng sydney = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18.0f));
            firstmarker = true;
        }

    }

    //定位
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {
        }
        //(Carol) Snackbar.make(mMapView, com.naer.pdfreader.R.string.error_location_provider, Snackbar.LENGTH_INDEFINITE).show();
        else {
            if (isNetworkEnabled) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            //(Carol) drawMarker(location);
        }

    }


    //初始化聆聽者
    private void InitSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);//建構值
        speechRecognizer.setRecognitionListener(new DescribeActivity.SpeechListener());// 設定聆聽者
    }

    //開啟內建語音辨識 用事件方式
    private void StartSpeechRecongizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        speechRecognizer.startListening(intent);
    }

    //初始化AIButton
    public void Init_AIConfiguration() {
//        final AIConfiguration config = new AIConfiguration(
//                Config.ACCESS_TOKEN,
//                AIConfiguration.SupportedLanguages.English,
//                AIConfiguration.RecognitionEngine.System);
//
//        final AIDataService aiDataService = new AIDataService(this, config);
//        final AIRequest aiRequest = new AIRequest();
//        aiRequest.setQuery("What thing can I do here?");
//
//        TTS.init(getApplicationContext());
//
//        final AsyncTask<AIRequest, Void, AIResponse> task = new AsyncTask<AIRequest, Void, AIResponse>() {
//            AIError aiError;
//
//            @Override
//            protected AIResponse doInBackground(final AIRequest... requests) {
//                final AIRequest request = requests[0];
//
//                try {
//                    final AIResponse response = aiDataService.request(aiRequest);
//                    return response;
//                } catch (AIServiceException e) {
//                    aiError = new AIError(e);
//                    return null;
//                }
//            }
//
//            @Override
//            protected void onPostExecute(final AIResponse aiResponse){
//                if (aiResponse != null) {
//                    onResult(aiResponse);
//                } else {
//                    onError(aiError);
//                }
//            }
//        };
//        task.execute(aiRequest);

        try {
            InputStream stream = getResources().openRawResource(R.raw.newagent_cqnfce_1fbb0504b0f8);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            session = SessionName.of(projectId, uuid); // uuid = UUID.randomUUID().toString()
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


//    private void sendMessage() {
//            // Java V2
//            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder()
//                    .setText("What thing can I do here?").setLanguageCode("en-US")).build();
//            new RequestJavaV2Task(DescribeActivity.this,session, sessionsClient, queryInput).execute();
//
//    }


//    @Override
//    public void onResult(DetectIntentResponse response) {
//
//        //Result result = response.getQueryResult().getFulfillmentText();
//        //String speech = result.getFulfillment().getSpeech();
//
//        String result = response.getQueryResult().getFulfillmentText();
//
//        if(!PlaceName.getText().toString().isEmpty()){
//            int messageCount = response.getQueryResult().getFulfillmentMessagesCount();
//            autoStrs = new String[messageCount];
////            String c = Integer.toString(messageCount);
//            Toast.makeText(this, Integer.toString(messageCount), Toast.LENGTH_SHORT).show();
//            for(int i = 0; i < messageCount; i++ ){
//
//                ResponseMessage.ResponseSpeech responseMessage = (ResponseMessage.ResponseSpeech) response.getQueryResult().getFulfillmentMessagesOrBuilder(i);
//                Log.e(TAG, "responseMessage: " + responseMessage.getSpeech());
////                autoStrs[i] = result.getFulfillment().getMessages().get(i).toString();
//                autoStrs[i] = responseMessage.getSpeech().toString().replaceAll("\\p{Punct}", ""); //去除每個字串中的括號[]
//                Toast.makeText(this, autoStrs[i], Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        String[] auto = autoStrs;
//        adapter = new ArrayAdapter<String>(DescribeActivity.this, android.R.layout.simple_dropdown_item_1line, auto);
//        studentdescribe.setAdapter(adapter);
////        String parameterString = "";
////        if(result.getParameters() != null && !result.getParameters().isEmpty()){
////            for(final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()){
////                parameterString += "(" + entry.getKey() + ", " + entry.getValue();
//////                Toast.makeText(this, parameterString, Toast.LENGTH_SHORT).show();
////            }
////        }
//
//
//
//        tttttt.setText(result);
////        Toast.makeText(this, speech, Toast.LENGTH_SHORT).show();
//
//    }

    public void callbackV2(DetectIntentResponse response) {
        if (response != null) {
            String reply = response.getQueryResult().getFulfillmentText();

        } else {
            Log.d(TAG, "REPLY IS NULL");
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                final TextView textView = new TextView(DescribeActivity.this);
                textView.setText("確認選擇定位點"+"\t"+marker.getTitle()+"\t"+"?");
                textView.setTextSize(30);
                new AlertDialog.Builder(DescribeActivity.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("確認位置")
                        .setView(textView)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int c1 = marker.getTitle().indexOf("_");
                                String s1 = marker.getTitle().substring(c1+1);
                                PlaceName.setText(s1);
                                bol_correctmarker = true;
                            }
                        })
                        .setNegativeButton("取消", null).create()
                        .show();
            }
        });

        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                final TextView textView = new TextView(DescribeActivity.this);
                textView.setText("確認刪除定位點"+"\t"+marker.getTitle()+"\t"+"?");
                textView.setTextSize(30);
                new AlertDialog.Builder(DescribeActivity.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("刪除位置")
                        .setView(textView)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bol_correctmarker = false;
                                marker.remove();
                            }
                        })
                        .setNegativeButton("取消", null).create()
                        .show();
            }
        });

        try {
            DatabaseReference fire_marker = FirebaseDatabase.getInstance().getReference();
            fire_marker.child("Other").child("marker_gps").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        marker_count = (int) dataSnapshot.child("location_name").getChildrenCount();
                        for (int i=0;i<marker_count;i++){
                            double location_left = (double) dataSnapshot.child("location_left").child(String.valueOf(i+1)).getValue();
                            double location_right = (double) dataSnapshot.child("location_right").child(String.valueOf(i+1)).getValue();
                            String location_name = (String) dataSnapshot.child("location_name").child(String.valueOf(i+1)).getValue();
                            String location_number = (String) dataSnapshot.child("location_number").child(String.valueOf(i+1)).getValue();
                            String location_time = (String) dataSnapshot.child("location_time").child(String.valueOf(i+1)).getValue();
                            LatLng sydney = new LatLng(location_left,location_right);
                            mMap.addMarker(new MarkerOptions().position(sydney)
                                    .title(location_name)
                                    .snippet("日期:"+location_time+"\n"+"座號:"+location_number)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                        }
                    } else {
                        Log.e(TAG, "onDataChange: No data");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(DescribeActivity.this, "取得Marker錯誤", Toast.LENGTH_SHORT).show();
        }

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.marker_infowindow, null);
                v.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
                TextView tvSubTitle = (TextView) v.findViewById(R.id.tv_subtitle);

                tvTitle.setText(marker.getTitle());
                tvSubTitle.setText(marker.getSnippet());
                return v;
            }
        });


//        LatLng sydney = new LatLng(121.18772685182198,24.967161421443464);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,18.0f));
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "位置資訊\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "現在目前位置", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    //事件的內建語音辨識聆聽者
    private class SpeechListener implements RecognitionListener
    {
        @Override
        public void onReadyForSpeech(Bundle params) {
            if(tipenable == true){
                //ImportMic();
            }
            else{
                //ImportMic1();
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
            //ImportTip();
            //ImportTip1();
            Log.d("Speech","End...");
        }

        @Override
        public void onError(int error) {
            //ImportTip();
            //ImportTip1();
        }

        @Override
        public void onResults(Bundle results) {
            //ImportTip();
           // ImportTip1();
            // tipview.setVisibility(View.VISIBLE);
            //tipview1.setVisibility(View.VISIBLE);
            //textSpeech.setVisibility(View.VISIBLE);
            //textScore.setVisibility(View.VISIBLE);
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
                    showdescribescore.setText(word);
                    //  textSpeech.setText(word);
                    //double score = (double) (results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)[0]*100);
                    int score = (int) (results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)[0]*100);
                    //textScore.setText("正確 : "+score+"/100");
                    // textSpeech.append(Html.fromHtml("<br>Correct: <font color=\""+ color +"\">" + score + "%</font>"));
                    //showdescribescore.setVisibility(View.VISIBLE);
                    if(score >= 90){
                        showdescribescore.append(Html.fromHtml("<br> <font color=\""+ color +"\">" + score + "%</font>"));
                        showdescribescore.append("\n你念的很棒哦! 繼續保持");
                    }else if(score >= 80){
                        showdescribescore.append(Html.fromHtml("<br> <font color= #FFA500 >" + score + "%</font>"));
                        showdescribescore.append("\n很厲害欸! 快90%了，再試試看吧！");
                    }else if(score >= 70){
                        showdescribescore.append(Html.fromHtml("<br> <font color= #FFA500 >" + score + "%</font>"));
                        showdescribescore.append("\n你念的不錯哦! 可以再更好,加油!");
                    }


                    //紀錄Firebase口說的紀錄資料
                    String describeText = studentdescribe.getText().toString();
                    SpeechData speechData = new SpeechData(describeText,word.trim(),(double)score);
                    fire_speechdata.child(choose_type_word).child("SpeechData")
                            .child(studentdescribe.getText().toString().replaceAll("[,|.|!|?|']", "").trim())
                            .push().setValue(speechData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(DescribeActivity.this, "成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(DescribeActivity.this, "失敗", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //StudentsSpeech studentsSpeech = new StudentsSpeech(studentdescribe.getText().toString().trim(),word.trim(),score,"正確");
                    //UploadStudentsBehavior.UploadSpeech(studentsSpeech);
                }else{
                    double len = question.length() > response.length() ? question.length() : response.length();//比較題目與答案字串長度
                    diff_match_patch diff_match_patch_obj = new diff_match_patch();//比對的Class
                    LinkedList<diff_match_patch.Diff> diff = diff_match_patch_obj.diff_lineMode(response, question, 31);
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
                    showdescribescore.setText(recorrect_response);
                    //   textSpeech.setText(recorrect_response);

                    //double similarity = (int)((len - diff_match_patch_obj.diff_levenshtein(diff)) / len * 1000) / 10.0;
                    int similarity = (int)((len - diff_match_patch_obj.diff_levenshtein(diff)) / len * 1000) / 10;
                    if(similarity >= 90){
                        color = "green";
                        encourage = "\n你念的很棒哦! 繼續保持";
                        //showdescribescore.append("\n你念的很棒哦! 繼續保持");
                    }
                    else if(similarity >= 80 && similarity < 90){
                        color = "#FFA500";
                        encourage = "\n很厲害欸! 快90%了，再試試看吧！";
                        //showdescribescore.append("\n 你念的不錯哦! 可以再更好,加油!");
                    }
                    else if(similarity >= 70 && similarity < 80){
                        color = "#FFA500";
                        encourage = "\n你念的不錯哦! 可以再更好,加油!";
                        //showdescribescore.append("\n 你念的不錯哦! 可以再更好,加油!");
                    }
                    else if(similarity >= 60 && similarity < 70){
                        color = "#FFA500";
                        encourage = "\n及格了! 你一定可以更好, 加油!";
                        //showdescribescore.append("\n 你念的不錯哦! 可以再更好,加油!");
                    }
                    else{
                        color = "red";
                        encourage = "\n這句念得不是很正確哦！不要氣餒，再試試看！";
                        //showdescribescore.append("\n 這句念得不是很正確哦！不要氣餒，再試試看！");
                    }
                    // textSpeech.append(Html.fromHtml("<br>Similarity: <font color=\""+ color +"\">" + similarity + "%</font>"));
                    showdescribescore.setVisibility(View.VISIBLE);
                    showdescribescore.append(Html.fromHtml("<br> <font color=\""+ color +"\">" + similarity + "%</font>"));
                    showdescribescore.append(encourage);
                    Toast.makeText(DescribeActivity.this, showdescribescore.getText().toString(), Toast.LENGTH_SHORT).show();
                    //StudentsSpeech studentsSpeech = new StudentsSpeech(textResolt.getText().toString().trim(),word.trim(),similarity,"錯誤");
                    String describeText = studentdescribe.getText().toString();
                    SpeechData speechData = new SpeechData(describeText,word.trim(),(double)similarity);
                    fire_speechdata.child(choose_type_word).child("SpeechData")
                            .child(studentdescribe.getText().toString().replaceAll("[,|.|!|?|']", "").trim())
                            .push().setValue(speechData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(DescribeActivity.this, "成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(DescribeActivity.this, "失敗", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //StudentsSpeech studentsSpeech = new StudentsSpeech(CompareSentences.toString().trim(),word.trim(),similarity,"錯誤");
                    //UploadStudentsBehavior.UploadSpeech(studentsSpeech);
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

    @Override
    protected  void onStop(){
        super.onStop();
        TTS.stop();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        InitSpeechRecognizer();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.stopListening();
        speechRecognizer.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // use this method to disconnect from speech recognition service
        // Not destroying the SpeechRecognition object in onPause method would block other apps from using SpeechRecognition service

        speechRecognizer.stopListening();
        speechRecognizer.destroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        InitSpeechRecognizer();
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void run() {
                getCurrentLocation();
            }
        }, 1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                final DatabaseReference fire_marker = FirebaseDatabase.getInstance().getReference();
                for(int i=1;i<=int_marker;i++){
                    fire_marker.child("Other").child("marker_gps").child("location_left").child(String.valueOf(marker_count+i)).setValue(Double.parseDouble(marker_left[i-1]));
                    fire_marker.child("Other").child("marker_gps").child("location_right").child(String.valueOf(marker_count+i)).setValue(Double.parseDouble(marker_right[i-1]));
                    fire_marker.child("Other").child("marker_gps").child("location_name").child(String.valueOf(marker_count+i)).setValue(marker_name[i-1]);
                    fire_marker.child("Other").child("marker_gps").child("location_number").child(String.valueOf(marker_count+i)).setValue(Student.Name);
                    fire_marker.child("Other").child("marker_gps").child("location_time").child(String.valueOf(marker_count+i)).setValue(marker_time[i-1]);
                }
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(DescribeActivity.this, "儲存Marker錯誤", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void writeInfo(String fileName, String strWrite) {
        try {

            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String savePath = fullPath + File.separator + "/" + fileName + ".txt";

            File file = new File(savePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(strWrite+"\n");
            bw.write("拍照次數:"+photo_count+"\n");
            bw.write("TTS次數:"+tts_count+"\n");
            bw.write("對話次數:"+showdescribescore_count+"\n");
            bw.write("觀看劇本次數:"+see_other_clickTime+"\n");

            try {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference db = database.getReference().child("學生"+Student.Name+"號").child("Student data").child("Describe");
                DatabaseReference db2 = database.getReference().child("Other").child("stu_data").child("Describe");
                db.child("Take Picture").setValue(photo_count);
                db.child("TTS").setValue(tts_count);
                db.child("Conversation").setValue(showdescribescore_count);
                db.child("SeeDrama").setValue(see_other_clickTime);
                db2.child(Student.Name).setValue(photo_count+showdescribescore_count);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(DescribeActivity.this, "儲存錯誤", Toast.LENGTH_SHORT).show();
            }


            bw.close();
            Toast.makeText(DescribeActivity.this, "行為紀錄紀錄成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readInfo(String fileName){

        BufferedReader br = null;
        String response = null;
        try {
            StringBuffer output = new StringBuffer();
            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String savePath = fullPath + File.separator + "/"+fileName+".txt";

            br = new BufferedReader(new FileReader(savePath));
            String line = "";
            while ((line = br.readLine()) != null) {
                output.append(line +"\n");

            }
            response = output.toString();
            photo_count= Integer.parseInt(response.substring(response.indexOf("拍照次數:")+5,response.indexOf("TTS次數:")-1));
            tts_count= Integer.parseInt(response.substring(response.indexOf("TTS次數:")+6,response.indexOf("對話次數:")-1));
            showdescribescore_count= Integer.parseInt(response.substring(response.indexOf("對話次數:")+5,response.indexOf("觀看劇本次數:")-1));
            see_other_clickTime= Integer.parseInt(response.substring(response.indexOf("觀看劇本次數:")+7,response.length()-1));
            br.close();
            Toast.makeText(DescribeActivity.this, "行為紀錄讀取成功", Toast.LENGTH_SHORT).show();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

//    public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
//
//        private Activity context;
//
//        public CustomInfoWindowAdapter(Activity context){
//            this.context = context;
//        }
//
//        @Override
//        public View getInfoWindow(Marker marker) {
//            return null;
//        }
//
//        @Override
//        public View getInfoContents(Marker marker) {
//            View view = context.getLayoutInflater().inflate(R.layout.custominfowindow, null);
//
//            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
//            TextView tvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);
//
//            tvTitle.setText(marker.getTitle());
//            tvSubTitle.setText(marker.getSnippet());
//
//            return view;
//        }
//    }
    
    private void video() {
        LayoutInflater inflater = LayoutInflater.from(DescribeActivity.this);
        View diffView = inflater.inflate(R.layout.video, null);
        video_view =diffView.findViewById(R.id.videoView);
        TextView video_imageview = diffView.findViewById(R.id.video_imageview);
        final String[] list = {"Step1-新增定位點", "Step2-拍攝照片", "Step3-建立語句", "Step4-聆聽語句發音","Step4-口說練習與儲存分享"};
        final Spinner spinner= diffView.findViewById(R.id.video_spinner);

        video_imageview.setVisibility(View.INVISIBLE);
        ArrayAdapter<String> numberList=new ArrayAdapter<String>(DescribeActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                list);
        spinner.setAdapter(numberList);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stop=false;
                switch (position) {
                    case 0:
                        final String describe_1 = "describe_1.mp4";
                        final File f_1=new File("/sdcard/Playlists/" + describe_1);
                        if(f_1.exists()){
                            vidUri = Uri.parse(f_1.getPath());
                        }
                        break;
                    case 1:
                        final String describe_2 = "describe_2.mp4";
                        final File f_2=new File("/sdcard/Playlists/" + describe_2);
                        if(f_2.exists()){
                            vidUri = Uri.parse(f_2.getPath());
                        }
                        break;
                    case 2:
                        final String describe_3 = "describe_3.mp4";
                        final File f_3=new File("/sdcard/Playlists/" + describe_3);
                        if(f_3.exists()){
                            vidUri = Uri.parse(f_3.getPath());
                        }
                        break;
                    case 3:
                        final String describe_4 = "describe_4.mp4";
                        final File f_4=new File("/sdcard/Playlists/" + describe_4);
                        if(f_4.exists()){
                            vidUri = Uri.parse(f_4.getPath());
                        }
                        break;
                    case 4:
                        final String describe_5 = "describe_5.mp4";
                        final File f_5=new File("/sdcard/Playlists/" + describe_5);
                        if(f_5.exists()){
                            vidUri = Uri.parse(f_5.getPath());
                        }
                        break;
                }
                final MediaController vidControl = new MediaController(DescribeActivity.this);
                vidControl.setAnchorView(video_view);
                video_view.setMediaController(vidControl);
                video_view.setVideoURI(vidUri);
                video_view.start();

                video_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!stop){
                            stopPosition = video_view.getCurrentPosition(); //stopPosition is an int
                            video_view.pause();
                            video_imageview.setVisibility(View.VISIBLE);
                            stop = true;
                            Toast.makeText(DescribeActivity.this, "暫停", Toast.LENGTH_SHORT).show();
                        }else{
                            stop = false;
                            video_view.seekTo(stopPosition);
                            video_imageview.setVisibility(View.INVISIBLE);
                            video_view.start(); //Or use resume() if it doesn't work. I'm not sure
                            Toast.makeText(DescribeActivity.this, "撥放", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                        video_view.setVideoURI(vidUri);
                        video_view.start();
                    }
                });

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
            }
        });
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DescribeActivity.this, R.style.AlertDialogStyle);
        alertDialogBuilder.setIcon(R.mipmap.photo_icon);
        alertDialogBuilder.setTitle("\t教學影片");
        alertDialogBuilder.setView(diffView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(1500, 1500);
    }
}
