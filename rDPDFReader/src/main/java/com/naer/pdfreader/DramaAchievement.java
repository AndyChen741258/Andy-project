package com.naer.pdfreader;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.json.DupDetector;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.protobuf.ByteString;
import com.orhanobut.logger.Logger;

import org.xml.sax.XMLReader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Model.SpeechData;
import Model.StoreTheEditData;
import Others.diff_match_patch;
import io.grpc.netty.shaded.io.netty.handler.ssl.JdkApplicationProtocolNegotiator;
import lib.kingja.switchbutton.SwitchMultiButton;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.DIRECTORY_MUSIC;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.naer.pdfreader.DialogActivity.LOCATION_UPDATE_MIN_DISTANCE;
import static com.naer.pdfreader.DialogActivity.LOCATION_UPDATE_MIN_TIME;
import static com.naer.pdfreader.DialogActivity.TAG;

public class DramaAchievement extends Activity {

    private DramaAchievement context;
    private Spinner drama_stu_num;
    private Spinner drama_dramawork_num;
    private ImageView drama_four_image;
    private Button btn_speak;
    private Button searchdrama;
    private Button drama_voice_1_a;
    private Button drama_voice_1_b;
    private Button drama_voice_2_a;
    private Button drama_voice_2_b;
    private Button drama_voice_3_a;
    private Button drama_voice_3_b;
    private Button drama_voice_4_a;
    private Button drama_voice_4_b;


    private String drama_studentNumber_word;
    private String drama_dramaNumber_word;

    private StorageReference fire_callImage;
    private StorageReference fire_callVoice;
    private DatabaseReference fire_timeOfSee;
    private DatabaseReference fire_calltts;

    private MediaPlayer player;
    private String Link;

    private String id_kkk;//抓取此次存取觀看紀錄的key

    private boolean isPracticeTextbook = false;

    //紀錄每個錄音聽取的次數
    public int a1 = 0;
    public int b1 = 0;
    public int a2 = 0;
    public int b2 = 0;
    public int a3 = 0;
    public int b3 = 0;
    public int a4 = 0;
    public int b4 = 0;

    public static ImageView drama1; //創作第一格照片
    public static ImageView drama2;
    public static ImageView drama3;
    public static ImageView drama4;
    public static ImageView drama5; //創作第一格照片
    public static ImageView drama6;
    public static ImageView drama7;
    public static ImageView drama8;
    private TextView creat1;
    private TextView creat2;
    private TextView creat3;
    private TextView creat4;
    private static TextView creat5;
    private static TextView creat6;
    private static TextView creat7;
    private static TextView creat8;
    private SwitchMultiButton switchMultiButton;
    private String[] Drama = new String[4];
    private static int i;
    private boolean bol_btn_speak = false;
    private DatabaseReference fire_editdata;
    private int role_spinner;
    private Button getanswer;
    private TextView practice_say;
    private SpeechRecognizer speechRecognizer;
    private static String[] list2={"請選擇"};
    private int int_bol_speak;
    private Button practice_say_left;
    private Button practice_say_right;
    private int choose=0;
    public static int count;
    private StorageReference fire_dramarecord;
    private String pathword_a;
    private String pathword_b;
    private String pathword_3;
    private String pathword_4;
    private static String CompareSentences = null;//設定比較的句子
    private TextView showdescribescore;
    private String encourage;
    public static TextView PlaceName;
    public static String placeview;
    private NotificationManager mNotificationManager;
    private double longitude;
    private double latitude;
    private LocationManager mLocationManager;
    private int tts_count=0;
    private int listen_record_count=0;
    private int speak_count=0;
    private Spinner test_spinner;
    private Spinner test2_spinner;
    private String select_string;
    //第一次進聆聽錄音布林
    private boolean drama_record1;
    private boolean drama_record2;
    private boolean drama_record3;
    private boolean drama_record4;
    //學生完整記錄布林
    private boolean say1;
    private boolean say2;
    private boolean say3;
    private boolean say4;
    private boolean finish_drama1;
    private boolean finish_drama2;
    private boolean finish_drama3;
    private boolean finish_drama4;
    private boolean finish_drama5;
    private boolean finish_drama6;
    private boolean finish_drama7;
    private boolean finish_drama8;
    private int x;
    //完整練習次數
    private int all_finish=0;
    //口說準確率資料庫
    private DatabaseReference fire_speechdata;
    //時間倒數
    private int tt = 15;
    private Timer timer;
    private int first_listen_record_count=0;
    private int first_tts_count=0;
    private int first_speak_count=0;
    private int first_one_count=0;
    private SimpleDateFormat sdf;
    private String date;
    private boolean bol_60 = false;
    //分段練習紀錄
    private boolean one_speak = false;
    private boolean one_listen = false;;
    private int one_count = 0;
    //目前情境編號
    private int drama_number=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_drama_achievement);

        TTS.init(getApplicationContext());

        drama_stu_num = findViewById(R.id.drama_stu_num);
        switchMultiButton = findViewById(R.id.switchmultibutton);
//        drama_dramawork_num = findViewById(R.id.drama_dramawork_num);
//        drama_four_image = findViewById(R.id.drama_four_image);
        searchdrama = findViewById(R.id.btn_searchdrama);

        drama_voice_1_a = findViewById(R.id.drama_voice_1_a);
        drama_voice_1_b = findViewById(R.id.drama_voice_1_b);
        drama_voice_2_a = findViewById(R.id.drama_voice_2_a);
        drama_voice_2_b = findViewById(R.id.drama_voice_2_b);
        drama_voice_3_a = findViewById(R.id.drama_voice_3_a);
        drama_voice_3_b = findViewById(R.id.drama_voice_3_b);
        drama_voice_4_a = findViewById(R.id.drama_voice_4_a);
        drama_voice_4_b = findViewById(R.id.drama_voice_4_b);

        drama_voice_1_a.setVisibility(View.INVISIBLE);
        drama_voice_1_b.setVisibility(View.INVISIBLE);
        drama_voice_2_a.setVisibility(View.INVISIBLE);
        drama_voice_2_b.setVisibility(View.INVISIBLE);
        drama_voice_3_a.setVisibility(View.INVISIBLE);
        drama_voice_3_b.setVisibility(View.INVISIBLE);
        drama_voice_4_a.setVisibility(View.INVISIBLE);
        drama_voice_4_b.setVisibility(View.INVISIBLE);

        drama1 = findViewById(R.id.drama1);
        drama2 = findViewById(R.id.drama2);
        drama3 = findViewById(R.id.drama3);
        drama4 = findViewById(R.id.drama4);
        drama5 = findViewById(R.id.drama5);
        drama6 = findViewById(R.id.drama6);
        drama7 = findViewById(R.id.drama7);
        drama8 = findViewById(R.id.drama8);
        creat1 = findViewById(R.id.creat1);
        creat2 = findViewById(R.id.creat2);
        creat3 = findViewById(R.id.creat3);
        creat4 = findViewById(R.id.creat4);
        creat5 = findViewById(R.id.creat5);
        creat6 = findViewById(R.id.creat6);
        creat7 = findViewById(R.id.creat7);
        creat8 = findViewById(R.id.creat8);
        btn_speak = findViewById(R.id.btn_speak);
        getanswer = findViewById(R.id.getanswer);
        practice_say = findViewById(R.id.practice_say);
        practice_say_left = findViewById(R.id.practice_say_left);
        practice_say_right = findViewById(R.id.practice_say_right);
        showdescribescore = findViewById(R.id.showcorrect);
        PlaceName = findViewById(R.id.placechatbot);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        test_spinner = findViewById(R.id.test_spinner);
        test2_spinner = findViewById(R.id.test2_spinner);

        readInfo(Student.Name+"號學生觀看練習劇本行為紀錄");

        //取得現在時間
       sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
//        String date = sdf.format(new java.util.Date());
//        Calendar c = Calendar.getInstance();
//        c.setTime(new java.util.Date());
//        c.add(Calendar.SECOND, 5);// 今天+5秒
//        String date_test = sdf.format(c.getTime());
        bol_60 = false;
//        time();


        final String[] drama_string = {"Playground","Classroom","Home","Other"};
        ArrayAdapter<String> drama_list=new ArrayAdapter<String>(DramaAchievement.this,
                android.R.layout.simple_spinner_dropdown_item, drama_string);
        test_spinner.setAdapter(drama_list);
        test_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_string=test_spinner.getSelectedItem().toString();
                DatabaseReference fire_load_dramaname = FirebaseDatabase.getInstance().getReference()
                        .child("Other").child("stu_drama").child(select_string);
//                query.orderByValue().equalTo(select_string).addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        Updatevalue(dataSnapshot.getKey());
//                        Log.v("測試", String.valueOf(dataSnapshot.getChildrenCount()));
//                        x++;
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
                try{
                    fire_load_dramaname.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
//                                Updatevalue(dataSnapshot.getValue().toString());
                                String[] test=new String[(int) dataSnapshot.getChildrenCount()];
                                String get_test= dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().length()-1);
                                String[] test_array =get_test.split(",");
                                for (int i=0;i<test_array.length;i++){
                                    int x =test_array[i].trim().indexOf("=");
                                    test[i]=test_array[i].trim().substring(0,x);
                                }
                                test2_spinner.setVisibility(VISIBLE);
                                ArrayAdapter<String> test_list=new ArrayAdapter<String>(DramaAchievement.this,
                                        android.R.layout.simple_spinner_dropdown_item, test);
                                test2_spinner.setAdapter(test_list);
                                test2_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        finish_drama1 = false;
                                        finish_drama2 = false;
                                        finish_drama3 = false;
                                        finish_drama4 = false;
                                        finish_drama5 = false;
                                        finish_drama6 = false;
                                        finish_drama7 = false;
                                        finish_drama8 = false;
                                        Drama[0] = "Drama1";
                                        Drama[1] = "Drama2";
                                        Drama[2] = "Drama3";
                                        Drama[3] = "Drama4";
                                        drama_studentNumber_word = test2_spinner.getSelectedItem().toString();
                                        bol_btn_speak = false;
                                        try {
                                            DatabaseReference fire_load_dramaname = FirebaseDatabase.getInstance().getReference();
                                            fire_load_dramaname.child("學生" + drama_studentNumber_word + "號").child("DramaName")
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            int i = 0;
                                                            String[] sssttt = new String[(int) dataSnapshot.getChildrenCount()];
                                                            for (DataSnapshot each : dataSnapshot.getChildren()) {
                                                                sssttt[i] = each.getValue().toString();
                                                                Drama[i]=sssttt[i];
                                                                i++;
                                                            }
                                                            for(int x=0;x<Drama.length;x++){
                                                                if(Drama[x].equals(select_string)==true){
                                                                    drama_dramaNumber_word = "Drama"+(x+1);
                                                                    ccc();
                                                                    choose ();
                                                                }
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        }
                                                    });
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        drama_record1=false;
                                        drama_record2=false;
                                        drama_record3=false;
                                        drama_record4=false;
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時

                                    }
                                });
                            }else{
                                Toast.makeText(DramaAchievement.this,"無資料",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DramaAchievement.this, "取得學生劇本名稱錯誤", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時

            }


        });









        //初始化聆聽者
       InitSpeechRecognizer();



        //------------學生座號------------

//        final String[] stunum_list = {"111", "01", "4", "06", "10", "32", "36", "40", "43","21"};
//        ArrayAdapter<String> stu_numberList=new ArrayAdapter<String>(DramaAchievement.this,
//                android.R.layout.simple_spinner_dropdown_item, stunum_list);
//        drama_stu_num.setAdapter(stu_numberList);
//        drama_stu_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                drama_studentNumber_word = drama_stu_num.getSelectedItem().toString();
//                Drama[0] = "Drama1";
//                Drama[1] = "Drama2";
//                Drama[2] = "Drama3";
//                try {
//                    DatabaseReference fire_load_dramaname = FirebaseDatabase.getInstance().getReference();
//                    fire_load_dramaname.child("學生" + drama_studentNumber_word + "號").child("DramaName")
//                            .addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    int i = 0;
//                                    String[] sssttt = new String[(int) dataSnapshot.getChildrenCount()];
//                                    for (DataSnapshot each : dataSnapshot.getChildren()) {
//                                        sssttt[i] = each.getValue().toString();
//                                        Drama[i]=sssttt[i];
//                                        Log.v("檢查",Drama[0]);
//                                        i++;
//                                    }
//                                    switchMultiButton.setText(Drama[0],Drama[1],Drama[2]).setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
//                                        @Override
//                                        public void onSwitch(int position, String tabText) {
//                                            int i=position+1;
//                                            bol_btn_speak = false;
//                                            drama_dramaNumber_word = "Drama"+i;
//                                            Toast.makeText(DramaAchievement.this, tabText, Toast.LENGTH_SHORT).show();
//                                            ccc();
//                                            choose ();
//                                        }
//                                    });
//                                }
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                }
//                            });
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
//                drama_studentNumber_word = drama_stu_num.getSelectedItem().toString();
//            }
//        });




        //------------劇本編號------------
//        ArrayAdapter<String> dra_numberList=new ArrayAdapter<String>(DramaAchievement.this,
//                android.R.layout.simple_spinner_dropdown_item, Drama);
//        drama_dramawork_num.setAdapter(dra_numberList);
//        drama_dramawork_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                int i=0;
//                i= (int) drama_dramawork_num.getSelectedItemId()+1;
//                drama_dramaNumber_word = "Drama"+i;
//
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
//                drama_dramaNumber_word = drama_dramawork_num.getSelectedItem().toString();
//            }
//        });

        searchdrama.setOnClickListener(callAllInformation);
        drama_voice_1_a.setOnClickListener(calltheVoice1_a);
        drama_voice_1_b.setOnClickListener(calltheVoice1_b);
        drama_voice_2_a.setOnClickListener(calltheVoice2_a);
        drama_voice_2_b.setOnClickListener(calltheVoice2_b);
        drama_voice_3_a.setOnClickListener(calltheVoice3_a);
        drama_voice_3_b.setOnClickListener(calltheVoice3_b);
        drama_voice_4_a.setOnClickListener(calltheVoice4_a);
        drama_voice_4_b.setOnClickListener(calltheVoice4_b);
    }

    private View.OnClickListener callAllInformation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ccc();
//            fire_callImage = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child("/Four-frame/").child(drama_dramaNumber_word);
//            fire_callImage.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    Uri uri = task.getResult();
//                    Glide.with(drama_four_image.getContext()).load(uri.toString()).into(drama_four_image);
//                }
//            });

//            drama_voice_1_a.setVisibility(View.VISIBLE);
//            drama_voice_1_b.setVisibility(View.VISIBLE);
//            drama_voice_2_a.setVisibility(View.VISIBLE);
//            drama_voice_2_b.setVisibility(View.VISIBLE);
//            drama_voice_3_a.setVisibility(View.VISIBLE);
//            drama_voice_3_b.setVisibility(View.VISIBLE);
//            drama_voice_4_a.setVisibility(View.VISIBLE);
//            drama_voice_4_b.setVisibility(View.VISIBLE);
//            fire_timeOfSee = FirebaseDatabase.getInstance().getReference()
//                    .child("學生" + Student.Name + "號").child("See_Drama");
//
//            id_kkk = fire_timeOfSee.push().getKey();
//
//            a1 = 0;
//            b1 = 0;
//            a2 = 0;
//            b2 = 0;
//            a3 = 0;
//            b3 = 0;
//            a4 = 0;
//            b4 = 0;
//
//            TimeOfSee timeOfSee = new TimeOfSee(drama_studentNumber_word + "_" + drama_dramaNumber_word, a1, b1, a2, b2, a3, b3, a4, b4);
//            fire_timeOfSee.child(id_kkk).setValue(timeOfSee);
        }
    };

//    選擇練習劇本
    private void choose (){
//        drama1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fire_editdata = FirebaseDatabase.getInstance().getReference()
//                        .child("學生"+drama_studentNumber_word+"號").child(drama_dramaNumber_word).child("1");
//                fire_editdata.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        StoreTheEditData storeTheEditData = dataSnapshot.getValue(StoreTheEditData.class);
//                        if(storeTheEditData.getPlayerA_text().equals("") == false){
//                            int c1 = storeTheEditData.getPlayerA_text().indexOf(":");
//                            String s1 = storeTheEditData.getPlayerA_text().substring(0, c1);
//                            try {
//                                TTS.speak(storeTheEditData.getPlayerA_text());
//                                Toast.makeText(DramaAchievement.this,"角色:"+s1, Toast.LENGTH_SHORT).show();
//                                Thread.sleep(5000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                        if(storeTheEditData.getPlayerB_text().equals("") == false){
//                            int c2 = storeTheEditData.getPlayerB_text().indexOf(":");
//                            String s2 = storeTheEditData.getPlayerB_text().substring(0, c2);
//                            try {
//                                TTS.speak(storeTheEditData.getPlayerB_text());
//                                Toast.makeText(DramaAchievement.this,"角色:"+s2, Toast.LENGTH_SHORT).show();
//                                Thread.sleep(5000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        if(storeTheEditData.getPlayer3_text().equals("") == false){
//                            int c3 = storeTheEditData.getPlayer3_text().indexOf(":");
//                            String s3 = storeTheEditData.getPlayer3_text().substring(0, c3);
//                            try {
//                                TTS.speak(storeTheEditData.getPlayer3_text());
//                                Toast.makeText(DramaAchievement.this,"角色:"+s3, Toast.LENGTH_SHORT).show();
//                                Thread.sleep(5000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        if(storeTheEditData.getPlayer4_text().equals("") == false){
//                            int c4 = storeTheEditData.getPlayer4_text().indexOf(":");
//                            String s4 = storeTheEditData.getPlayer4_text().substring(0, c4);
//                            try {
//                                TTS.speak(storeTheEditData.getPlayer4_text());
//                                Toast.makeText(DramaAchievement.this,"角色:"+s4, Toast.LENGTH_SHORT).show();
//                                Thread.sleep(5000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        Toast.makeText(DramaAchievement.this,"播放完畢", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });

//        btn_speak.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (bol_btn_speak == false) {
//                    String drama_count[] = new String[i - 1];
//                    for (int x = 0; x < i - 1; x++) {
//                        drama_count[x] = "分境" + (x + 1);
//                    }
//                    LayoutInflater inflater = LayoutInflater.from(DramaAchievement.this);
//                    View view = inflater.inflate(R.layout.btn_speake, null);
//                    Spinner spinner = (view.findViewById(R.id.spinner));
//                    Spinner spinner2 = (view.findViewById(R.id.spinner2));
//                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(DramaAchievement.this);
//                    alertdialog.setTitle("選擇分境");
//                    alertdialog.setIcon(R.drawable.ic_launcher);
//                    ArrayAdapter<String> numberList = new ArrayAdapter<String>(DramaAchievement.this,
//                            android.R.layout.simple_spinner_dropdown_item,
//                            drama_count);
//                    spinner.setAdapter(numberList);
//                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            role_spinner = position + 1;
//                            fire_editdata = FirebaseDatabase.getInstance().getReference()
//                                    .child("學生" + drama_studentNumber_word + "號").child(drama_dramaNumber_word).child(String.valueOf(role_spinner));
//                            fire_editdata.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    StoreTheEditData storeTheEditData = dataSnapshot.getValue(StoreTheEditData.class);
//                                    String s1 = "", s2 = "", s3 = "", s4 = "";
//                                    int count = 0;
//                                    String[] list1 = new String[4];
//
//                                    if (storeTheEditData.getPlayerA_text().equals("") == false) {
//                                        int c1 = storeTheEditData.getPlayerA_text().indexOf(":");
//                                        s1 = storeTheEditData.getPlayerA_text().substring(0, c1);
//                                        count++;
//                                        list1[0] = s1;
//                                    }
//                                    if (storeTheEditData.getPlayerB_text().equals("") == false) {
//                                        int c2 = storeTheEditData.getPlayerB_text().indexOf(":");
//                                        s2 = storeTheEditData.getPlayerB_text().substring(0, c2);
//                                        count++;
//                                        list1[1] = s2;
//                                    }
//                                    if (storeTheEditData.getPlayer3_text().equals("") == false) {
//                                        int c3 = storeTheEditData.getPlayer3_text().indexOf(":");
//                                        s3 = storeTheEditData.getPlayer3_text().substring(0, c3);
//                                        count++;
//                                        list1[2] = s3;
//                                    }
//                                    if (storeTheEditData.getPlayer4_text().equals("") == false) {
//                                        int c4 = storeTheEditData.getPlayer4_text().indexOf(":");
//                                        s4 = storeTheEditData.getPlayer4_text().substring(0, c4);
//                                        count++;
//                                        list1[3] = s4;
//                                    }
//
//                                    list2 = new String[count];
//                                    for (int i = 0; i < count; i++) {
//                                        if (!list1[i].matches("")) {
//                                            list2[i] = list1[i];
//                                        }
//                                    }
//
//                                    ArrayAdapter<String> numberList2 = new ArrayAdapter<String>(DramaAchievement.this,
//                                            android.R.layout.simple_spinner_dropdown_item,
//                                            list2);
//                                    spinner2.setAdapter(numberList2);
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
//                        }
//                    });
//
//                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            int_bol_speak = position + 1;
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
//                        }
//                    });
//                    alertdialog.setView(view);
//                    alertdialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                        // do something when the button is clicked
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            bol_btn_speak = true;
//                        }
//                    });
//                    alertdialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        // do something when the button is clicked
//                        public void onClick(DialogInterface arg0, int arg1) {
//                        }
//                    });
//                    alertdialog.show();
//               }else{
////                    fire_editdata = FirebaseDatabase.getInstance().getReference()
////                            .child("學生"+drama_studentNumber_word+"號").child(drama_dramaNumber_word).child(String.valueOf(role_spinner));
////                    fire_editdata.addValueEventListener(new ValueEventListener() {
////                        @Override
////                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                            StoreTheEditData storeTheEditData = dataSnapshot.getValue(StoreTheEditData.class);
////
////                            if(storeTheEditData.getPlayerA_text().equals("") == false && int_bol_speak == 1){
////                                int c1 = storeTheEditData.getPlayerA_text().indexOf(":");
////                                String s1 = storeTheEditData.getPlayerA_text().substring(0, c1);
////                                TTS.speak(storeTheEditData.getPlayerA_text());
////                                Toast.makeText(DramaAchievement.this,"角色:"+s1, Toast.LENGTH_SHORT).show();
////                            }
////                            if(storeTheEditData.getPlayerB_text().equals("") == false && int_bol_speak == 2){
////                                int c2 = storeTheEditData.getPlayerB_text().indexOf(":");
////                                String s2 = storeTheEditData.getPlayerB_text().substring(0, c2);
////                                TTS.speak(storeTheEditData.getPlayerB_text());
////                                Toast.makeText(DramaAchievement.this,"角色:"+s2, Toast.LENGTH_SHORT).show();
////                            }
////                            if(storeTheEditData.getPlayer3_text().equals("") == false && int_bol_speak == 3){
////                                int c3 = storeTheEditData.getPlayer3_text().indexOf(":");
////                                String s3 = storeTheEditData.getPlayer3_text().substring(0, c3);
////                                TTS.speak(storeTheEditData.getPlayer3_text());
////                                Toast.makeText(DramaAchievement.this,"角色:"+s3, Toast.LENGTH_SHORT).show();
////                            }
////                            if(storeTheEditData.getPlayer4_text().equals("") == false && int_bol_speak == 4){
////                                int c4 = storeTheEditData.getPlayer4_text().indexOf(":");
////                                String s4 = storeTheEditData.getPlayer4_text().substring(0, c4);
////                                TTS.speak(storeTheEditData.getPlayer4_text());
////                                Toast.makeText(DramaAchievement.this,"角色:"+s4, Toast.LENGTH_SHORT).show();
////                            }
////                        }
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                        }
////                    });
//                    switch (int_bol_speak) {
//                        case 1:
//                            pathword_a = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + role_spinner + "_" + "A.amr";
//                            fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word+"/"+pathword_a);
//                            fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    Link = uri.toString();
//                                    Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
//                                    player = new MediaPlayer();
//                                    try {
//                                        player.setDataSource(Link);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    try {
//                                        player.prepare();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    player.start();
//                                    listen_record_count++;
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception exception) {
//                                    Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                            break;
//                        case 2:
//                            pathword_b = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + role_spinner + "_" + "B.amr";
//                            fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word+"/"+pathword_b);
//                            fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    Link = uri.toString();
//                                    Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
//                                    player = new MediaPlayer();
//                                    try {
//                                        player.setDataSource(Link);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    try {
//                                        player.prepare();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    player.start();
//                                    listen_record_count++;
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception exception) {
//                                    Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                            break;
//                        case 3:
//                            pathword_3 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + role_spinner + "_" + "3.amr";
//                            fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word+"/"+pathword_3);
//                            fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    Link = uri.toString();
//                                    Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
//                                    player = new MediaPlayer();
//                                    try {
//                                        player.setDataSource(Link);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    try {
//                                        player.prepare();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    player.start();
//                                    listen_record_count++;
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception exception) {
//                                    Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                            break;
//                        case 4:
//                            pathword_4 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + role_spinner + "_" + "4.amr";
//                            fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word+"/"+pathword_4);
//                            fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    Link = uri.toString();
//                                    Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
//                                    player = new MediaPlayer();
//                                    try {
//                                        player.setDataSource(Link);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    try {
//                                        player.prepare();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    player.start();
//                                    listen_record_count++;
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception exception) {
//                                    Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                            break;
//                    }
//                }
//            }
//        });
//
//        btn_speak.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                bol_btn_speak = false;
//                Toast.makeText(DramaAchievement.this, "重新選擇情境", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
    }

    private View.OnClickListener calltheVoice1_a = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            a1+=1;
            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("1").child("playerA_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_1_a");
                            a1Reference.setValue(a1);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_1_A.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(Link);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_1_a");
                                    a1Reference.setValue(a1);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }



        }
    };

    private View.OnClickListener calltheVoice1_b = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            b1 += 1;
            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("1").child("playerB_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_1_b");
                            a1Reference.setValue(b1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_1_B.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(Link);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_1_b");
                                    a1Reference.setValue(b1);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    private View.OnClickListener calltheVoice2_a = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            a2 += 1;
            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("2").child("playerA_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_2_a");
                            a1Reference.setValue(a2);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_2_A.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(Link);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_2_a");
                                    a1Reference.setValue(a2);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    private View.OnClickListener calltheVoice2_b = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            b2 += 1;
            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("2").child("playerB_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_2_b");
                            a1Reference.setValue(b2);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_2_B.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(Link);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_2_b");
                                    a1Reference.setValue(b2);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };


    private View.OnClickListener calltheVoice3_a = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            a3 += 1;

            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("3").child("playerA_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_3_a");
                            a1Reference.setValue(a3);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_3_A.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(Link);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_3_a");
                                    a1Reference.setValue(a3);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    private View.OnClickListener calltheVoice3_b = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            b3 += 1;

            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("3").child("playerB_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_3_b");
                            a1Reference.setValue(b3);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_3_B.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(Link);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_3_b");
                                    a1Reference.setValue(b3);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    private View.OnClickListener calltheVoice4_a = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            a4 +=1;

            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("4").child("playerA_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_4_a");
                            a1Reference.setValue(a4);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_4_A.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(Link);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_4_a");
                                    a1Reference.setValue(a4);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    private View.OnClickListener calltheVoice4_b = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            b4 += 1;

            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("4").child("playerB_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_4_b");
                            a1Reference.setValue(b4);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_4_B.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(Link);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_4_b");
                                    a1Reference.setValue(b4);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    public void ccc(){
        DatabaseReference fire_check_edit_exist;
        fire_check_edit_exist = FirebaseDatabase.getInstance().getReference();

        fire_check_edit_exist.child("學生"+drama_studentNumber_word+"號").child(drama_dramaNumber_word).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                x = 0;
                for (DataSnapshot each : dataSnapshot.getChildren()) {
                    if(each.getKey().equals("contextmenu") == false){
                        x++;
                    }
                }
                double[] sssttt = new double[(int) dataSnapshot.getChildrenCount()];
                i = sssttt.length;
                Log.v("測試", String.valueOf(x));
                switch (i-2){
                    case 1:
                        drama5.setVisibility(INVISIBLE);
                        drama6.setVisibility(INVISIBLE);
                        drama7.setVisibility(INVISIBLE);
                        drama8.setVisibility(INVISIBLE);
                        creat5.setVisibility(VISIBLE);
                        creat6.setVisibility(INVISIBLE);
                        creat7.setVisibility(INVISIBLE);
                        creat8.setVisibility(INVISIBLE);
                        break;
                    case 2:
                        drama5.setVisibility(INVISIBLE);
                        drama6.setVisibility(INVISIBLE);
                        drama7.setVisibility(INVISIBLE);
                        drama8.setVisibility(INVISIBLE);
                        creat5.setVisibility(VISIBLE);
                        creat6.setVisibility(INVISIBLE);
                        creat7.setVisibility(INVISIBLE);
                        creat8.setVisibility(INVISIBLE);
                        break;
                    case 3:
                        drama5.setVisibility(INVISIBLE);
                        drama6.setVisibility(INVISIBLE);
                        drama7.setVisibility(INVISIBLE);
                        drama8.setVisibility(INVISIBLE);
                        creat5.setVisibility(VISIBLE);
                        creat6.setVisibility(INVISIBLE);
                        creat7.setVisibility(INVISIBLE);
                        creat8.setVisibility(INVISIBLE);
                        break;
                    case 4:
                        drama5.setVisibility(INVISIBLE);
                        drama6.setVisibility(INVISIBLE);
                        drama7.setVisibility(INVISIBLE);
                        drama8.setVisibility(INVISIBLE);
                        creat5.setVisibility(VISIBLE);
                        creat6.setVisibility(INVISIBLE);
                        creat7.setVisibility(INVISIBLE);
                        creat8.setVisibility(INVISIBLE);
                        break;
                    case 5:
                        drama6.setVisibility(INVISIBLE);
                        drama7.setVisibility(INVISIBLE);
                        drama8.setVisibility(INVISIBLE);
                        creat5.setVisibility(VISIBLE);
                        creat6.setVisibility(VISIBLE);
                        creat7.setVisibility(INVISIBLE);
                        creat8.setVisibility(INVISIBLE);
                        break;
                    case 6:
                        drama7.setVisibility(INVISIBLE);
                        drama8.setVisibility(INVISIBLE);
                        creat5.setVisibility(VISIBLE);
                        creat6.setVisibility(VISIBLE);
                        creat7.setVisibility(VISIBLE);
                        creat8.setVisibility(INVISIBLE);
                        break;
                    case 7:
                        drama8.setVisibility(INVISIBLE);
                        creat5.setVisibility(VISIBLE);
                        creat6.setVisibility(VISIBLE);
                        creat7.setVisibility(VISIBLE);
                        creat8.setVisibility(VISIBLE);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fire_check_edit_exist.child("學生"+drama_studentNumber_word+"號").child(drama_dramaNumber_word).child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String[] s1 = new String[4];
                    drama1.setEnabled(true);
                    drama1.setVisibility(VISIBLE);
                    try {
                        drama1.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                drama_number=1;
                                one_listen = false;
                                one_speak = false;
                                drama_record1 = false;
                                drama_record2 = false;
                                drama_record3 = false;
                                drama_record4 = false;


                                StoreTheEditData storeTheEditData = dataSnapshot.getValue(StoreTheEditData.class);
                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                if(storeTheEditData.getPlayerA_text().equals("") == false){
                                    s1[count]=storeTheEditData.getPlayerA_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayerB_text().equals("") == false){
                                    s1[count]=storeTheEditData.getPlayerB_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayer3_text().equals("") == false ){
                                    s1[count]=storeTheEditData.getPlayer3_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayer4_text().equals("") == false ){
                                    s1[count]=storeTheEditData.getPlayer4_text();
                                    count++;
                                }

                                String[] s=new String[count];
                                for (int x=0;x<count;x++){
                                    s[x]=s1[x];
                                }

                                if(s[0].equals("")==false) {
                                    say1 = false;
                                    say2 = false;
                                    say3 = false;
                                    say4 = false;
                                    practice_say.setText("\t"+s[0]);
                                    choose=0;
                                    practice_say_right.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (choose < s.length-1) {
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                practice_say.setText("\t"+s[choose+1]);
                                                if (choose+1 == int_bol_speak-1){
                                                    practice_say.setTextColor(Color.rgb(255,0,0));
                                                }else{
                                                    practice_say.setTextColor(Color.rgb(0,0,0));
                                                }
                                                choose++;
                                                one_listen = false;
                                                one_speak = false;
                                            }else{
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24_black);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                Toast.makeText(DramaAchievement.this, "最後一句", Toast.LENGTH_SHORT).show();
                                            }
                                            drama_record1 = false;
                                            drama_record2 = false;
                                            drama_record3 = false;
                                            drama_record4 = false;
                                        }
                                    });
                                    practice_say_left.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (choose-1 < s.length && choose > 0 ) {
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                practice_say.setText("\t"+s[choose-1]);
                                                if (choose-1 == int_bol_speak-1){
                                                    practice_say.setTextColor(Color.rgb(255,0,0));
                                                }else{
                                                    practice_say.setTextColor(Color.rgb(0,0,0));
                                                }
                                                choose--;
                                                one_listen = false;
                                                one_speak = false;
                                            } else {
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                Toast.makeText(DramaAchievement.this, "第一句", Toast.LENGTH_SHORT).show();
                                            }
                                            drama_record1 = false;
                                            drama_record2 = false;
                                            drama_record3 = false;
                                            drama_record4 = false;
                                        }
                                    });

                                    btn_speak.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int x=0;
                                            for(int i=0;i<s.length;i++){
                                                if(practice_say.getText().toString().trim().equals(s[i].trim()) == true){
                                                    x=i;
                                                }
                                            }
                                            switch(x){
                                                case 0:
                                                    if(!drama_record1){
                                                        pathword_a = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 1 + "_" + "A.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_a);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record1 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                try {
                                                                    //上傳點擊行為與時間點
                                                                    String date = sdf.format(new java.util.Date());
                                                                    DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                                                            .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("DramaAchievement").child("聆聽錄音");
                                                                    fire_60sec_student_data.child(date).child("Drama Name").setValue(select_string);

                                                                    fire_60sec_student_data.child(date).child("Student number").setValue(drama_studentNumber_word);

                                                                    fire_60sec_student_data.child(date).child("Drama Number").setValue("Drama1");

                                                                    fire_60sec_student_data.child(date).child("Text").setValue(practice_say.getText().toString().trim());
                                                                }catch (Exception e){
                                                                    e.printStackTrace();
                                                                    Toast.makeText(DramaAchievement.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                                                                }
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }

                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        try {
                                                            //上傳點擊行為與時間點
                                                            String date = sdf.format(new java.util.Date());
                                                            DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                                                    .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("DramaAchievement").child("聆聽錄音");
                                                            fire_60sec_student_data.child(date).child("Drama Name").setValue(select_string);

                                                            fire_60sec_student_data.child(date).child("Student number").setValue(drama_studentNumber_word);

                                                            fire_60sec_student_data.child(date).child("Drama Number").setValue("Drama1");

                                                            fire_60sec_student_data.child(date).child("Text").setValue(practice_say.getText().toString().trim());
                                                        }catch (Exception e){
                                                            e.printStackTrace();
                                                            Toast.makeText(DramaAchievement.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                                                        }
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 1:
                                                    if(!drama_record2){
                                                        pathword_b = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 1 + "_" + "B.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_b);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record2 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                try {
                                                                    //上傳點擊行為與時間點
                                                                    String date = sdf.format(new java.util.Date());
                                                                    DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                                                            .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("DramaAchievement").child("聆聽錄音");
                                                                    fire_60sec_student_data.child(date).child("Drama Name").setValue(select_string);

                                                                    fire_60sec_student_data.child(date).child("Student number").setValue(drama_studentNumber_word);

                                                                    fire_60sec_student_data.child(date).child("Drama Number").setValue("Drama1");

                                                                    fire_60sec_student_data.child(date).child("Text").setValue(practice_say.getText().toString().trim());
                                                                }catch (Exception e){
                                                                    e.printStackTrace();
                                                                    Toast.makeText(DramaAchievement.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                                                                }
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        try {
                                                            //上傳點擊行為與時間點
                                                            String date = sdf.format(new java.util.Date());
                                                            DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                                                    .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("DramaAchievement").child("聆聽錄音");
                                                            fire_60sec_student_data.child(date).child("Drama Name").setValue(select_string);

                                                            fire_60sec_student_data.child(date).child("Student number").setValue(drama_studentNumber_word);

                                                            fire_60sec_student_data.child(date).child("Drama Number").setValue("Drama1");

                                                            fire_60sec_student_data.child(date).child("Text").setValue(practice_say.getText().toString().trim());
                                                        }catch (Exception e){
                                                            e.printStackTrace();
                                                            Toast.makeText(DramaAchievement.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                                                        }
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 2:
                                                    if(!drama_record3){
                                                        pathword_3 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 1 + "_" + "3.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_3);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record3 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                try {
                                                                    //上傳點擊行為與時間點
                                                                    String date = sdf.format(new java.util.Date());
                                                                    DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                                                            .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("DramaAchievement").child("聆聽錄音");
                                                                    fire_60sec_student_data.child(date).child("Drama Name").setValue(select_string);

                                                                    fire_60sec_student_data.child(date).child("Student number").setValue(drama_studentNumber_word);

                                                                    fire_60sec_student_data.child(date).child("Drama Number").setValue("Drama1");

                                                                    fire_60sec_student_data.child(date).child("Text").setValue(practice_say.getText().toString().trim());
                                                                }catch (Exception e){
                                                                    e.printStackTrace();
                                                                    Toast.makeText(DramaAchievement.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                                                                }
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        try {
                                                            //上傳點擊行為與時間點
                                                            String date = sdf.format(new java.util.Date());
                                                            DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                                                    .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("DramaAchievement").child("聆聽錄音");
                                                            fire_60sec_student_data.child(date).child("Drama Name").setValue(select_string);

                                                            fire_60sec_student_data.child(date).child("Student number").setValue(drama_studentNumber_word);

                                                            fire_60sec_student_data.child(date).child("Drama Number").setValue("Drama1");

                                                            fire_60sec_student_data.child(date).child("Text").setValue(practice_say.getText().toString().trim());
                                                        }catch (Exception e){
                                                            e.printStackTrace();
                                                            Toast.makeText(DramaAchievement.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                                                        }
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 3:
                                                    if(!drama_record4) {
                                                        pathword_4 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 1 + "_" + "4.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_4);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record4 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                try {
                                                                    //上傳點擊行為與時間點
                                                                    String date = sdf.format(new java.util.Date());
                                                                    DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                                                            .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("DramaAchievement").child("聆聽錄音");
                                                                    fire_60sec_student_data.child(date).child("Drama Name").setValue(select_string);

                                                                    fire_60sec_student_data.child(date).child("Student number").setValue(drama_studentNumber_word);

                                                                    fire_60sec_student_data.child(date).child("Drama Number").setValue("Drama1");

                                                                    fire_60sec_student_data.child(date).child("Text").setValue(practice_say.getText().toString().trim());
                                                                }catch (Exception e){
                                                                    e.printStackTrace();
                                                                    Toast.makeText(DramaAchievement.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                                                                }
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        try {
                                                            //上傳點擊行為與時間點
                                                            String date = sdf.format(new java.util.Date());
                                                            DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                                                    .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("DramaAchievement").child("聆聽錄音");
                                                            fire_60sec_student_data.child(date).child("Drama Name").setValue(select_string);

                                                            fire_60sec_student_data.child(date).child("Student number").setValue(drama_studentNumber_word);

                                                            fire_60sec_student_data.child(date).child("Drama Number").setValue("Drama1");

                                                            fire_60sec_student_data.child(date).child("Text").setValue(practice_say.getText().toString().trim());
                                                        }catch (Exception e){
                                                            e.printStackTrace();
                                                            Toast.makeText(DramaAchievement.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                                                        }
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                            }
                                            if(one_speak){
                                                one_count++;
                                                one_speak = false;
                                                one_listen = false;
                                            }
                                        }
                                    });

                                    getanswer.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(practice_say.getText().toString().equals("") == false){
                                                one_speak = true;
                                                int c2 = practice_say.getText().toString().indexOf(":");
                                                String s2 = practice_say.getText().toString().substring(c2+1,practice_say.length());
                                                CompareSentences = s2;
                                                StartSpeechRecongizer();
                                            }

                                            switch (choose) {
                                                case 0:
                                                    say1 = true;
                                                    break;
                                                case 1:
                                                    say2 = true;
                                                    break;
                                                case 2:
                                                    say3 = true;
                                                    break;
                                                case 3:
                                                    say4 = true;
                                                    break;
                                            }

                                            switch (s.length){
                                                case 2:
                                                    if(say1 ==true && say2 == true){
                                                        finish_drama1=true;
                                                    }
                                                    break;
                                                case 3:
                                                    if(say1 ==true && say2 == true && say3 == true){
                                                        finish_drama1=true;
                                                    }
                                                    break;
                                                case 4:
                                                    if(say1 ==true && say2 == true && say3 == true && say4 == true){
                                                        finish_drama1=true;
                                                    }
                                                    break;
                                            }

                                            if(one_listen){
                                                one_count++;
                                                one_speak = false;
                                                one_listen = false;
                                            }

                                        }
                                    });
                                }
                                count = 0;
                                Toast.makeText(DramaAchievement.this,"已選擇第一分鏡",Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    practice_say.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tts_count++;
                            int i=practice_say.getText().toString().indexOf(":");
                            String tts=practice_say.getText().toString().substring(i,practice_say.length());
                            TTS.speak(tts);
                        }
                    });
                    Glide.with(drama1.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama1);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama1.setImageResource(R.drawable.noimage);
                    drama1.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });

        fire_check_edit_exist.child("學生"+drama_studentNumber_word+"號").child(drama_dramaNumber_word).child("2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String[] s1 = new String[4];
                    drama2.setEnabled(true);
                    drama2.setVisibility(VISIBLE);
                    try {
                        drama2.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                drama_number=2;
                                one_listen = false;
                                one_speak = false;
                                drama_record1 = false;
                                drama_record2 = false;
                                drama_record3 = false;
                                drama_record4 = false;
                                StoreTheEditData storeTheEditData = dataSnapshot.getValue(StoreTheEditData.class);
                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                if(storeTheEditData.getPlayerA_text().equals("") == false){
                                    s1[count]=storeTheEditData.getPlayerA_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayerB_text().equals("") == false){
                                    s1[count]=storeTheEditData.getPlayerB_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayer3_text().equals("") == false ){
                                    s1[count]=storeTheEditData.getPlayer3_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayer4_text().equals("") == false ){
                                    s1[count]=storeTheEditData.getPlayer4_text();
                                    count++;
                                }

                                String[] s=new String[count];
                                for (int x=0;x<count;x++){
                                    s[x]=s1[x];
                                }

                                if(s[0].equals("")==false) {
                                    say1 = false;
                                    say2 = false;
                                    say3 = false;
                                    say4 = false;
                                    practice_say.setText("\t"+s[0]);
                                    choose=0;
                                    practice_say_right.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (choose < s.length-1) {
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                practice_say.setText("\t"+s[choose+1]);
                                                if (choose+1 == int_bol_speak-1){
                                                    practice_say.setTextColor(Color.rgb(255,0,0));
                                                }else{
                                                    practice_say.setTextColor(Color.rgb(0,0,0));
                                                }
                                                choose++;
                                                one_listen = false;
                                                one_speak = false;
                                            }else{
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24_black);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                Toast.makeText(DramaAchievement.this, "最後一句", Toast.LENGTH_SHORT).show();
                                            }
                                            drama_record1 = false;
                                            drama_record2 = false;
                                            drama_record3 = false;
                                            drama_record4 = false;
                                        }
                                    });
                                    practice_say_left.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (choose-1 < s.length && choose > 0 ) {
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                practice_say.setText("\t"+s[choose-1]);
                                                if (choose-1 == int_bol_speak-1){
                                                    practice_say.setTextColor(Color.rgb(255,0,0));
                                                }else{
                                                    practice_say.setTextColor(Color.rgb(0,0,0));
                                                }
                                                choose--;
                                                one_listen = false;
                                                one_speak = false;
                                            } else {
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                Toast.makeText(DramaAchievement.this, "第一句", Toast.LENGTH_SHORT).show();
                                            }
                                            drama_record1 = false;
                                            drama_record2 = false;
                                            drama_record3 = false;
                                            drama_record4 = false;
                                        }
                                    });

                                    btn_speak.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int x=0;
                                            for(int i=0;i<s.length;i++){
                                                if(practice_say.getText().toString().trim().equals(s[i].trim()) == true){
                                                    x=i;
                                                }
                                            }
                                            switch(x) {
                                                case 0:
                                                    if (!drama_record1){
                                                        pathword_a = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 2 + "_" + "A.amr";
                                                    fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_a);
                                                    fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            drama_record1 = true;
                                                            Link = uri.toString();
                                                            Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                            player = new MediaPlayer();
                                                            try {
                                                                player.setDataSource(Link);
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                            try {
                                                                player.prepare();
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                            player.start();
                                                            listen_record_count++;
                                                            one_listen = true;
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception exception) {
                                                            Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 1:
                                                    if(!drama_record2) {
                                                        pathword_b = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 2 + "_" + "B.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_b);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record2 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 2:
                                                    if(!drama_record3) {
                                                        pathword_3 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 2 + "_" + "3.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_3);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record3 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 3:
                                                    if(!drama_record4) {
                                                        pathword_4 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 2 + "_" + "4.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_4);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record4 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                            }
                                            if(one_speak){
                                                one_count++;
                                                one_speak = false;
                                                one_listen = false;
                                            }
                                        }
                                    });

                                    getanswer.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(practice_say.getText().toString().equals("") == false){
                                                one_speak=true;
                                                int c2 = practice_say.getText().toString().indexOf(":");
                                                String s2 = practice_say.getText().toString().substring(c2+1,practice_say.length());
                                                CompareSentences = s2;
                                                StartSpeechRecongizer();
                                            }

                                            switch (choose) {
                                                case 0:
                                                    say1 = true;
                                                    break;
                                                case 1:
                                                    say2 = true;
                                                    break;
                                                case 2:
                                                    say3 = true;
                                                    break;
                                                case 3:
                                                    say4 = true;
                                                    break;
                                            }

                                            switch (s.length){
                                                case 2:
                                                    if(say1 ==true && say2 == true){
                                                        finish_drama2=true;
                                                    }
                                                    break;
                                                case 3:
                                                    if(say1 ==true && say2 == true && say3 == true){
                                                        finish_drama2=true;
                                                    }
                                                    break;
                                                case 4:
                                                    if(say1 ==true && say2 == true && say3 == true && say4 == true){
                                                        finish_drama2=true;
                                                    }
                                                    break;
                                            }

                                            if(one_listen){
                                                one_count++;
                                                one_speak = false;
                                                one_listen = false;
                                            }
                                        }
                                    });


                                }
                                count = 0;
                                Toast.makeText(DramaAchievement.this,"已選擇第二分鏡",Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Glide.with(drama2.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama2);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama2.setImageResource(R.drawable.noimage);
                    drama2.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });

        fire_check_edit_exist.child("學生"+drama_studentNumber_word+"號").child(drama_dramaNumber_word).child("3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String[] s1 = new String[4];
                    drama3.setEnabled(true);
                    drama3.setVisibility(VISIBLE);
                    try {
                        drama3.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                drama_number=3;
                                one_listen = false;
                                one_speak = false;
                                drama_record1 = false;
                                drama_record2 = false;
                                drama_record3 = false;
                                drama_record4 = false;
                                StoreTheEditData storeTheEditData = dataSnapshot.getValue(StoreTheEditData.class);
                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                if(storeTheEditData.getPlayerA_text().equals("") == false){
                                    s1[count]=storeTheEditData.getPlayerA_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayerB_text().equals("") == false){
                                    s1[count]=storeTheEditData.getPlayerB_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayer3_text().equals("") == false ){
                                    s1[count]=storeTheEditData.getPlayer3_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayer4_text().equals("") == false ){
                                    s1[count]=storeTheEditData.getPlayer4_text();
                                    count++;
                                }

                                String[] s=new String[count];
                                for (int x=0;x<count;x++){
                                    s[x]=s1[x];
                                }

                                if(s[0].equals("")==false) {
                                    say1 = false;
                                    say2 = false;
                                    say3 = false;
                                    say4 = false;
                                    practice_say.setText("\t"+s[0]);
                                    choose=0;
                                    practice_say_right.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (choose < s.length-1) {
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                practice_say.setText("\t"+s[choose+1]);
                                                if (choose+1 == int_bol_speak-1){
                                                    practice_say.setTextColor(Color.rgb(255,0,0));
                                                }else{
                                                    practice_say.setTextColor(Color.rgb(0,0,0));
                                                }
                                                choose++;
                                                one_listen = false;
                                                one_speak = false;
                                            }else{
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24_black);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                Toast.makeText(DramaAchievement.this, "最後一句", Toast.LENGTH_SHORT).show();
                                            }
                                            drama_record1 = false;
                                            drama_record2 = false;
                                            drama_record3 = false;
                                            drama_record4 = false;
                                        }
                                    });
                                    practice_say_left.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (choose-1 < s.length && choose > 0 ) {
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                practice_say.setText("\t"+s[choose-1]);
                                                if (choose-1 == int_bol_speak-1){
                                                    practice_say.setTextColor(Color.rgb(255,0,0));
                                                }else{
                                                    practice_say.setTextColor(Color.rgb(0,0,0));
                                                }
                                                choose--;
                                                one_listen = false;
                                                one_speak = false;
                                            } else {
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                Toast.makeText(DramaAchievement.this, "第一句", Toast.LENGTH_SHORT).show();
                                            }
                                            drama_record1 = false;
                                            drama_record2 = false;
                                            drama_record3 = false;
                                            drama_record4 = false;
                                        }
                                    });

                                    btn_speak.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int x=0;
                                            for(int i=0;i<s.length;i++){
                                                if(practice_say.getText().toString().trim().equals(s[i].trim()) == true){
                                                    x=i;
                                                }
                                            }
                                            switch(x){
                                                case 0:
                                                    if(!drama_record1) {
                                                        pathword_a = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 3 + "_" + "A.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_a);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record1 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 1:
                                                    if(!drama_record2) {
                                                        pathword_b = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 3 + "_" + "B.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_b);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record2 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 2:
                                                    if(!drama_record3) {
                                                        pathword_3 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 3 + "_" + "3.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_3);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record3 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else {
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 3:
                                                    if(!drama_record4) {
                                                        pathword_4 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 3 + "_" + "4.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_4);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record4 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                            }
                                            if(one_speak){
                                                one_count++;
                                                one_speak = false;
                                                one_listen = false;
                                            }
                                        }
                                    });
                                    getanswer.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(practice_say.getText().toString().equals("") == false){
                                                one_speak = true;
                                                int c2 = practice_say.getText().toString().indexOf(":");
                                                String s2 = practice_say.getText().toString().substring(c2+1,practice_say.length());
                                                CompareSentences = s2;
                                                StartSpeechRecongizer();
                                            }

                                            switch (choose) {
                                                case 0:
                                                    say1 = true;
                                                    break;
                                                case 1:
                                                    say2 = true;
                                                    break;
                                                case 2:
                                                    say3 = true;
                                                    break;
                                                case 3:
                                                    say4 = true;
                                                    break;
                                            }

                                            switch (s.length){
                                                case 2:
                                                    if(say1 ==true && say2 == true){
                                                        finish_drama3=true;
                                                    }
                                                    break;
                                                case 3:
                                                    if(say1 ==true && say2 == true && say3 == true){
                                                        finish_drama3=true;
                                                    }
                                                    break;
                                                case 4:
                                                    if(say1 ==true && say2 == true && say3 == true && say4 == true){
                                                        finish_drama3=true;
                                                    }
                                                    break;
                                            }
                                            if(one_listen){
                                                one_count++;
                                                one_speak = false;
                                                one_listen = false;
                                            }
                                        }
                                    });

                                }

                                count = 0;
                                Toast.makeText(DramaAchievement.this,"已選擇第三分鏡",Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Glide.with(drama3.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama3);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama3.setImageResource(R.drawable.noimage);
                    drama3.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });

        fire_check_edit_exist.child("學生"+drama_studentNumber_word+"號").child(drama_dramaNumber_word).child("4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String[] s1 = new String[4];
                    drama4.setVisibility(VISIBLE);
                    drama4.setEnabled(true);
                    try{
                        drama4.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                drama_number=4;
                                one_listen = false;
                                one_speak = false;
                                drama_record1 = false;
                                drama_record2 = false;
                                drama_record3 = false;
                                drama_record4 = false;
                                StoreTheEditData storeTheEditData = dataSnapshot.getValue(StoreTheEditData.class);
                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                if(storeTheEditData.getPlayerA_text().equals("") == false){
                                    s1[count]=storeTheEditData.getPlayerA_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayerB_text().equals("") == false){
                                    s1[count]=storeTheEditData.getPlayerB_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayer3_text().equals("") == false ){
                                    s1[count]=storeTheEditData.getPlayer3_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayer4_text().equals("") == false ){
                                    s1[count]=storeTheEditData.getPlayer4_text();
                                    count++;
                                }

                                String[] s=new String[count];
                                for (int x=0;x<count;x++){
                                    s[x]=s1[x];
                                }

                                if(s[0].equals("")==false) {
                                    say1 = false;
                                    say2 = false;
                                    say3 = false;
                                    say4 = false;
                                    practice_say.setText("\t"+s[0]);
                                    choose=0;
                                    practice_say_right.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (choose < s.length-1) {
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                practice_say.setText("\t"+s[choose+1]);
                                                if (choose+1 == int_bol_speak-1){
                                                    practice_say.setTextColor(Color.rgb(255,0,0));
                                                }else{
                                                    practice_say.setTextColor(Color.rgb(0,0,0));
                                                }
                                                choose++;
                                                one_listen = false;
                                                one_speak = false;
                                            }else{
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24_black);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                Toast.makeText(DramaAchievement.this, "最後一句", Toast.LENGTH_SHORT).show();
                                            }
                                            drama_record1 = false;
                                            drama_record2 = false;
                                            drama_record3 = false;
                                            drama_record4 = false;
                                        }
                                    });
                                    practice_say_left.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (choose-1 < s.length && choose > 0 ) {
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                practice_say.setText("\t"+s[choose-1]);
                                                if (choose-1 == int_bol_speak-1){
                                                    practice_say.setTextColor(Color.rgb(255,0,0));
                                                }else{
                                                    practice_say.setTextColor(Color.rgb(0,0,0));
                                                }
                                                choose--;
                                                one_listen = false;
                                                one_speak = false;
                                            } else {
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                Toast.makeText(DramaAchievement.this, "第一句", Toast.LENGTH_SHORT).show();
                                            }
                                            drama_record1 = false;
                                            drama_record2 = false;
                                            drama_record3 = false;
                                            drama_record4 = false;
                                        }
                                    });

                                    btn_speak.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int x=0;
                                            for(int i=0;i<s.length;i++){
                                                if(practice_say.getText().toString().trim().equals(s[i].trim()) == true){
                                                    x=i;
                                                }
                                            }
                                            switch(x){
                                                case 0:
                                                    if(!drama_record1) {
                                                        pathword_a = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 4 + "_" + "A.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_a);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record1 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 1:
                                                    if(!drama_record2) {
                                                        pathword_b = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 4 + "_" + "B.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_b);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record2 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else {
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 2:
                                                    if(!drama_record3) {
                                                        pathword_3 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 4 + "_" + "3.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_3);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record3=true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 3:
                                                    if(!drama_record4) {
                                                        pathword_4 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 4 + "_" + "4.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_4);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record4 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                            }
                                            if(one_speak){
                                                one_count++;
                                                one_speak = false;
                                                one_listen = false;
                                            }
                                        }
                                    });
                                    getanswer.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(practice_say.getText().toString().equals("") == false){
                                                one_speak = true;
                                                int c2 = practice_say.getText().toString().indexOf(":");
                                                String s2 = practice_say.getText().toString().substring(c2+1,practice_say.length());
                                                CompareSentences = s2;
                                                StartSpeechRecongizer();
                                            }

                                            switch (choose) {
                                                case 0:
                                                    say1 = true;
                                                    break;
                                                case 1:
                                                    say2 = true;
                                                    break;
                                                case 2:
                                                    say3 = true;
                                                    break;
                                                case 3:
                                                    say4 = true;
                                                    break;
                                            }

                                            switch (s.length){
                                                case 2:
                                                    if(say1 ==true && say2 == true){
                                                        finish_drama4=true;
                                                    }
                                                    break;
                                                case 3:
                                                    if(say1 ==true && say2 == true && say3 == true){
                                                        finish_drama4=true;
                                                    }
                                                    break;
                                                case 4:
                                                    if(say1 ==true && say2 == true && say3 == true && say4 == true){
                                                        finish_drama4=true;
                                                    }
                                                    break;
                                            }

                                            if(x == 4 && finish_drama4 == true && finish_drama3
                                                    == true && finish_drama2 == true && finish_drama1 == true){
                                                all_finish++;
                                            }

                                            if(one_listen){
                                                one_count++;
                                                one_speak = false;
                                                one_listen = false;
                                            }

                                        }
                                    });

                                }

                                count = 0;
                                Toast.makeText(DramaAchievement.this,"已選擇第四分鏡",Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    Glide.with(drama4.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama4);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama4.setImageResource(R.drawable.noimage);
                    drama4.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });
        fire_check_edit_exist.child("學生"+drama_studentNumber_word+"號").child(drama_dramaNumber_word).child("5").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String[] s1 = new String[4];
                    drama5.setVisibility(VISIBLE);
                    creat5.setVisibility(VISIBLE);
                    drama5.setEnabled(true);
                     try{
                         drama5.setOnTouchListener(new View.OnTouchListener() {
                             @Override
                             public boolean onTouch(View v, MotionEvent event) {
                                 drama_number=5;
                                 one_listen = false;
                                 one_speak = false;
                                 drama_record1 = false;
                                 drama_record2 = false;
                                 drama_record3 = false;
                                 drama_record4 = false;
                                 StoreTheEditData storeTheEditData = dataSnapshot.getValue(StoreTheEditData.class);
                                 practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                 practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                 if(storeTheEditData.getPlayerA_text().equals("") == false){
                                     s1[count]=storeTheEditData.getPlayerA_text();
                                     count++;
                                 }
                                 if(storeTheEditData.getPlayerB_text().equals("") == false){
                                     s1[count]=storeTheEditData.getPlayerB_text();
                                     count++;
                                 }
                                 if(storeTheEditData.getPlayer3_text().equals("") == false ){
                                     s1[count]=storeTheEditData.getPlayer3_text();
                                     count++;
                                 }
                                 if(storeTheEditData.getPlayer4_text().equals("") == false ){
                                     s1[count]=storeTheEditData.getPlayer4_text();
                                     count++;
                                 }

                                 String[] s=new String[count];
                                 for (int x=0;x<count;x++){
                                     s[x]=s1[x];
                                 }

                                 if(s[0].equals("")==false) {
                                     say1 = false;
                                     say2 = false;
                                     say3 = false;
                                     say4 = false;
                                     practice_say.setText("\t"+s[0]);
                                     choose=0;
                                     practice_say_right.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (choose < s.length-1) {
                                                 practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                 practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                 practice_say.setText("\t"+s[choose+1]);
                                                 if (choose+1 == int_bol_speak-1){
                                                     practice_say.setTextColor(Color.rgb(255,0,0));
                                                 }else{
                                                     practice_say.setTextColor(Color.rgb(0,0,0));
                                                 }
                                                 choose++;
                                                 one_listen = false;
                                                 one_speak = false;
                                             }else{
                                                 practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24_black);
                                                 practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                 Toast.makeText(DramaAchievement.this, "最後一句", Toast.LENGTH_SHORT).show();
                                             }
                                             drama_record1 = false;
                                             drama_record2 = false;
                                             drama_record3 = false;
                                             drama_record4 = false;
                                         }
                                     });
                                     practice_say_left.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (choose-1 < s.length && choose > 0 ) {
                                                 practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                 practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                 practice_say.setText("\t"+s[choose-1]);
                                                 if (choose-1 == int_bol_speak-1){
                                                     practice_say.setTextColor(Color.rgb(255,0,0));
                                                 }else{
                                                     practice_say.setTextColor(Color.rgb(0,0,0));
                                                 }
                                                 choose--;
                                                 one_listen = false;
                                                 one_speak = false;
                                             } else {
                                                 practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                                 practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                 Toast.makeText(DramaAchievement.this, "第一句", Toast.LENGTH_SHORT).show();
                                             }
                                             drama_record1 = false;
                                             drama_record2 = false;
                                             drama_record3 = false;
                                             drama_record4 = false;
                                         }
                                     });
                                     btn_speak.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             int x=0;
                                             for(int i=0;i<s.length;i++){
                                                 if(practice_say.getText().toString().trim().equals(s[i].trim()) == true){
                                                     x=i;
                                                 }
                                             }
                                             switch(x) {
                                                 case 0:
                                                     if (!drama_record1) {
                                                         pathword_a = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 5 + "_" + "A.amr";
                                                         fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_a);
                                                         fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                             @Override
                                                             public void onSuccess(Uri uri) {
                                                                 drama_record1 = true;
                                                                 Link = uri.toString();
                                                                 Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                 player = new MediaPlayer();
                                                                 try {
                                                                     player.setDataSource(Link);
                                                                 } catch (IOException e) {
                                                                     e.printStackTrace();
                                                                 }
                                                                 try {
                                                                     player.prepare();
                                                                 } catch (IOException e) {
                                                                     e.printStackTrace();
                                                                 }
                                                                 player.start();
                                                                 listen_record_count++;
                                                                 one_listen = true;
                                                             }
                                                         }).addOnFailureListener(new OnFailureListener() {
                                                             @Override
                                                             public void onFailure(@NonNull Exception exception) {
                                                                 Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                             }
                                                         });
                                                     }else {
                                                         player.start();
                                                         listen_record_count++;
                                                         one_listen = true;
                                                     }
                                                     break;
                                                 case 1:
                                                     if(!drama_record2) {
                                                         pathword_b = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 5 + "_" + "B.amr";
                                                         fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_b);
                                                         fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                             @Override
                                                             public void onSuccess(Uri uri) {
                                                                 drama_record2=true;
                                                                 Link = uri.toString();
                                                                 Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                 player = new MediaPlayer();
                                                                 try {
                                                                     player.setDataSource(Link);
                                                                 } catch (IOException e) {
                                                                     e.printStackTrace();
                                                                 }
                                                                 try {
                                                                     player.prepare();
                                                                 } catch (IOException e) {
                                                                     e.printStackTrace();
                                                                 }
                                                                 player.start();
                                                                 listen_record_count++;
                                                                 one_listen = true;
                                                             }
                                                         }).addOnFailureListener(new OnFailureListener() {
                                                             @Override
                                                             public void onFailure(@NonNull Exception exception) {
                                                                 Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                             }
                                                         });
                                                     }else {
                                                         player.start();
                                                         listen_record_count++;
                                                         one_listen = true;
                                                     }
                                                     break;
                                                 case 2:
                                                     if(!drama_record3) {
                                                         pathword_3 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 5 + "_" + "3.amr";
                                                         fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_3);
                                                         fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                             @Override
                                                             public void onSuccess(Uri uri) {
                                                                 drama_record3 = true;
                                                                 Link = uri.toString();
                                                                 Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                 player = new MediaPlayer();
                                                                 try {
                                                                     player.setDataSource(Link);
                                                                 } catch (IOException e) {
                                                                     e.printStackTrace();
                                                                 }
                                                                 try {
                                                                     player.prepare();
                                                                 } catch (IOException e) {
                                                                     e.printStackTrace();
                                                                 }
                                                                 player.start();
                                                                 listen_record_count++;
                                                                 one_listen = true;
                                                             }
                                                         }).addOnFailureListener(new OnFailureListener() {
                                                             @Override
                                                             public void onFailure(@NonNull Exception exception) {
                                                                 Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                             }
                                                         });
                                                     }else{
                                                         player.start();
                                                         listen_record_count++;
                                                         one_listen = true;
                                                     }
                                                     break;
                                                 case 3:
                                                     if(!drama_record4) {
                                                         pathword_4 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 5 + "_" + "4.amr";
                                                         fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_4);
                                                         fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                             @Override
                                                             public void onSuccess(Uri uri) {
                                                                 drama_record4 = true;
                                                                 Link = uri.toString();
                                                                 Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                 player = new MediaPlayer();
                                                                 try {
                                                                     player.setDataSource(Link);
                                                                 } catch (IOException e) {
                                                                     e.printStackTrace();
                                                                 }
                                                                 try {
                                                                     player.prepare();
                                                                 } catch (IOException e) {
                                                                     e.printStackTrace();
                                                                 }
                                                                 player.start();
                                                                 listen_record_count++;
                                                                 one_listen = true;
                                                             }
                                                         }).addOnFailureListener(new OnFailureListener() {
                                                             @Override
                                                             public void onFailure(@NonNull Exception exception) {
                                                                 Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                             }
                                                         });
                                                     }else{
                                                         player.start();
                                                         listen_record_count++;
                                                         one_listen = true;
                                                     }
                                                     break;
                                             }
                                             if(one_speak){
                                                 one_count++;
                                                 one_speak = false;
                                                 one_listen = false;
                                             }
                                         }
                                     });
                                     getanswer.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             if(practice_say.getText().toString().equals("") == false){
                                                 one_speak = true;
                                                 int c2 = practice_say.getText().toString().indexOf(":");
                                                 String s2 = practice_say.getText().toString().substring(c2+1,practice_say.length());
                                                 CompareSentences = s2;
                                                 StartSpeechRecongizer();
                                             }

                                             switch (choose) {
                                                 case 0:
                                                     say1 = true;
                                                     break;
                                                 case 1:
                                                     say2 = true;
                                                     break;
                                                 case 2:
                                                     say3 = true;
                                                     break;
                                                 case 3:
                                                     say4 = true;
                                                     break;
                                             }

                                             switch (s.length){
                                                 case 2:
                                                     if(say1 ==true && say2 == true){
                                                         finish_drama5=true;
                                                     }
                                                     break;
                                                 case 3:
                                                     if(say1 ==true && say2 == true && say3 == true){
                                                         finish_drama5=true;
                                                     }
                                                     break;
                                                 case 4:
                                                     if(say1 ==true && say2 == true && say3 == true && say4 == true){
                                                         finish_drama5=true;
                                                     }
                                                     break;
                                             }

                                             if(x == 5 && finish_drama5 == true && finish_drama4 == true && finish_drama3
                                                     == true && finish_drama2 == true && finish_drama1 == true){
                                                 all_finish++;
                                             }

                                             if(one_listen){
                                                 one_count++;
                                                 one_speak = false;
                                                 one_listen = false;
                                             }
                                         }
                                     });

                                 }

                                 count = 0;
                                 Toast.makeText(DramaAchievement.this,"已選擇第五分鏡",Toast.LENGTH_SHORT).show();
                                 return false;
                             }
                         });
                     }catch (Exception e){
                        e.printStackTrace();
                     }
                    Glide.with(drama5.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama5);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama5.setImageResource(R.drawable.noimage);
                    drama5.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });
        fire_check_edit_exist.child("學生"+drama_studentNumber_word+"號").child(drama_dramaNumber_word).child("6").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String[] s1 = new String[4];
                    drama6.setVisibility(VISIBLE);
                    creat6.setVisibility(VISIBLE);
                    drama6.setEnabled(true);
                    try{
                        drama6.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                drama_number=6;
                                one_listen = false;
                                one_speak = false;
                                drama_record1 = false;
                                drama_record2 = false;
                                drama_record3 = false;
                                drama_record4 = false;
                                StoreTheEditData storeTheEditData = dataSnapshot.getValue(StoreTheEditData.class);
                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                if(storeTheEditData.getPlayerA_text().equals("") == false){
                                    s1[count]=storeTheEditData.getPlayerA_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayerB_text().equals("") == false){
                                    s1[count]=storeTheEditData.getPlayerB_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayer3_text().equals("") == false ){
                                    s1[count]=storeTheEditData.getPlayer3_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayer4_text().equals("") == false ){
                                    s1[count]=storeTheEditData.getPlayer4_text();
                                    count++;
                                }

                                String[] s=new String[count];
                                for (int x=0;x<count;x++){
                                    s[x]=s1[x];
                                }

                                if(s[0].equals("")==false) {
                                    say1 = false;
                                    say2 = false;
                                    say3 = false;
                                    say4 = false;
                                    practice_say.setText("\t"+s[0]);
                                    choose=0;
                                    practice_say_right.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (choose < s.length-1) {
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                practice_say.setText("\t"+s[choose+1]);
                                                if (choose+1 == int_bol_speak-1){
                                                    practice_say.setTextColor(Color.rgb(255,0,0));
                                                }else{
                                                    practice_say.setTextColor(Color.rgb(0,0,0));
                                                }
                                                choose++;
                                                one_listen = false;
                                                one_speak = false;
                                            }else{
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24_black);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                Toast.makeText(DramaAchievement.this, "最後一句", Toast.LENGTH_SHORT).show();
                                            }
                                            drama_record1 = false;
                                            drama_record2 = false;
                                            drama_record3 = false;
                                            drama_record4 = false;
                                        }
                                    });
                                    practice_say_left.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (choose-1 < s.length && choose > 0 ) {
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                practice_say.setText("\t"+s[choose-1]);
                                                if (choose-1 == int_bol_speak-1){
                                                    practice_say.setTextColor(Color.rgb(255,0,0));
                                                }else{
                                                    practice_say.setTextColor(Color.rgb(0,0,0));
                                                }
                                                choose--;
                                                one_listen = false;
                                                one_speak = false;
                                            } else {
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                Toast.makeText(DramaAchievement.this, "第一句", Toast.LENGTH_SHORT).show();
                                            }
                                            drama_record1 = false;
                                            drama_record2 = false;
                                            drama_record3 = false;
                                            drama_record4 = false;
                                        }
                                    });

                                    btn_speak.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int x=0;
                                            for(int i=0;i<s.length;i++){
                                                if(practice_say.getText().toString().trim().equals(s[i].trim()) == true){
                                                    x=i;
                                                }
                                            }
                                            switch(x){
                                                case 0:
                                                    if(!drama_record1) {
                                                        pathword_a = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 6 + "_" + "A.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_a);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record1 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else {
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 1:
                                                    if(!drama_record2) {
                                                        pathword_b = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 6 + "_" + "B.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_b);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record2 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else {
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 2:
                                                    if(!drama_record3) {
                                                        pathword_3 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 6 + "_" + "3.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_3);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record3 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 3:
                                                    if(!drama_record4) {
                                                        pathword_4 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 6 + "_" + "4.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_4);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record4 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                            }
                                            if(one_speak){
                                                one_count++;
                                                one_speak = false;
                                                one_listen = false;
                                            }
                                        }
                                    });
                                    getanswer.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(practice_say.getText().toString().equals("") == false){
                                                one_speak = true;
                                                int c2 = practice_say.getText().toString().indexOf(":");
                                                String s2 = practice_say.getText().toString().substring(c2+1,practice_say.length());
                                                CompareSentences = s2;
                                                StartSpeechRecongizer();
                                            }

                                            switch (choose) {
                                                case 0:
                                                    say1 = true;
                                                    break;
                                                case 1:
                                                    say2 = true;
                                                    break;
                                                case 2:
                                                    say3 = true;
                                                    break;
                                                case 3:
                                                    say4 = true;
                                                    break;
                                            }

                                            switch (s.length){
                                                case 2:
                                                    if(say1 ==true && say2 == true){
                                                        finish_drama6=true;
                                                    }
                                                    break;
                                                case 3:
                                                    if(say1 ==true && say2 == true && say3 == true){
                                                        finish_drama6=true;
                                                    }
                                                    break;
                                                case 4:
                                                    if(say1 ==true && say2 == true && say3 == true && say4 == true){
                                                        finish_drama6=true;
                                                    }
                                                    break;
                                            }

                                            if(x == 6 && finish_drama6 == true && finish_drama5 == true && finish_drama4 == true && finish_drama3
                                                    == true && finish_drama2 == true && finish_drama1 == true){
                                                all_finish++;
                                            }

                                            if(one_listen){
                                                one_count++;
                                                one_speak = false;
                                                one_listen = false;
                                            }

                                        }
                                    });
                                }
                                count = 0;
                                Toast.makeText(DramaAchievement.this,"已選擇第六分鏡",Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Glide.with(drama6.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama6);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama6.setImageResource(R.drawable.noimage);
                    drama6.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });
        fire_check_edit_exist.child("學生"+drama_studentNumber_word+"號").child(drama_dramaNumber_word).child("7").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String[] s1 = new String[4];
                    drama7.setVisibility(VISIBLE);
                    creat7.setVisibility(VISIBLE);
                    drama7.setEnabled(true);
                    try{
                        drama7.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                drama_number=7;
                                one_listen = false;
                                one_speak = false;
                                drama_record1 = false;
                                drama_record2 = false;
                                drama_record3 = false;
                                drama_record4 = false;
                                StoreTheEditData storeTheEditData = dataSnapshot.getValue(StoreTheEditData.class);
                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                if(storeTheEditData.getPlayerA_text().equals("") == false){
                                    s1[count]=storeTheEditData.getPlayerA_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayerB_text().equals("") == false){
                                    s1[count]=storeTheEditData.getPlayerB_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayer3_text().equals("") == false ){
                                    s1[count]=storeTheEditData.getPlayer3_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayer4_text().equals("") == false ){
                                    s1[count]=storeTheEditData.getPlayer4_text();
                                    count++;
                                }

                                String[] s=new String[count];
                                for (int x=0;x<count;x++){
                                    s[x]=s1[x];
                                }

                                if(s[0].equals("")==false) {
                                    say1 = false;
                                    say2 = false;
                                    say3 = false;
                                    say4 = false;
                                    practice_say.setText("\t"+s[0]);
                                    choose=0;
                                    practice_say_right.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (choose < s.length-1) {
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                practice_say.setText("\t"+s[choose+1]);
                                                if (choose+1 == int_bol_speak-1){
                                                    practice_say.setTextColor(Color.rgb(255,0,0));
                                                }else{
                                                    practice_say.setTextColor(Color.rgb(0,0,0));
                                                }
                                                choose++;
                                                one_listen = false;
                                                one_speak = false;
                                            }else{
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24_black);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                Toast.makeText(DramaAchievement.this, "最後一句", Toast.LENGTH_SHORT).show();
                                            }
                                            drama_record1 = false;
                                            drama_record2 = false;
                                            drama_record3 = false;
                                            drama_record4 = false;
                                        }
                                    });
                                    practice_say_left.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (choose-1 < s.length && choose > 0 ) {
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                practice_say.setText("\t"+s[choose-1]);
                                                if (choose-1 == int_bol_speak-1){
                                                    practice_say.setTextColor(Color.rgb(255,0,0));
                                                }else{
                                                    practice_say.setTextColor(Color.rgb(0,0,0));
                                                }
                                                choose--;
                                                one_listen = false;
                                                one_speak = false;
                                            } else {
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                Toast.makeText(DramaAchievement.this, "第一句", Toast.LENGTH_SHORT).show();
                                            }
                                            drama_record1 = false;
                                            drama_record2 = false;
                                            drama_record3 = false;
                                            drama_record4 = false;
                                        }
                                    });

                                    btn_speak.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int x=0;
                                            for(int i=0;i<s.length;i++){
                                                if(practice_say.getText().toString().trim().equals(s[i].trim()) == true){
                                                    x=i;
                                                }
                                            }
                                            switch(x){
                                                case 0:
                                                    if(!drama_record1) {
                                                        pathword_a = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 7 + "_" + "A.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_a);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record1 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else {
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 1:
                                                    if(!drama_record2) {
                                                        pathword_b = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 7 + "_" + "B.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_b);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record2 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 2:
                                                    if(!drama_record3) {
                                                        pathword_3 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 7 + "_" + "3.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_3);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record3 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else {
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 3:
                                                    if(!drama_record4) {
                                                        pathword_4 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 7 + "_" + "4.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_4);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record4=true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                            }
                                            if(one_speak){
                                                one_count++;
                                                one_speak = false;
                                                one_listen = false;
                                            }
                                        }
                                    });
                                    getanswer.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(practice_say.getText().toString().equals("") == false){
                                                one_speak = true;
                                                int c2 = practice_say.getText().toString().indexOf(":");
                                                String s2 = practice_say.getText().toString().substring(c2+1,practice_say.length());
                                                CompareSentences = s2;
                                                StartSpeechRecongizer();
                                            }

                                            switch (choose) {
                                                case 0:
                                                    say1 = true;
                                                    break;
                                                case 1:
                                                    say2 = true;
                                                    break;
                                                case 2:
                                                    say3 = true;
                                                    break;
                                                case 3:
                                                    say4 = true;
                                                    break;
                                            }

                                            switch (s.length){
                                                case 2:
                                                    if(say1 ==true && say2 == true){
                                                        finish_drama7=true;
                                                    }
                                                    break;
                                                case 3:
                                                    if(say1 ==true && say2 == true && say3 == true){
                                                        finish_drama7=true;
                                                    }
                                                    break;
                                                case 4:
                                                    if(say1 ==true && say2 == true && say3 == true && say4 == true){
                                                        finish_drama7=true;
                                                    }
                                                    break;
                                            }

                                            if(x == 7 && finish_drama7 == true && finish_drama6 == true && finish_drama5 == true && finish_drama4 == true && finish_drama3
                                                    == true && finish_drama2 == true && finish_drama1 == true){
                                                all_finish++;
                                            }

                                            if(one_listen){
                                                one_count++;
                                                one_speak = false;
                                                one_listen = false;
                                            }

                                        }
                                    });
                                }
                                count = 0;
                                Toast.makeText(DramaAchievement.this,"已選擇第七分鏡",Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Glide.with(drama7.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama7);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama7.setImageResource(R.drawable.noimage);
                    drama7.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });
        fire_check_edit_exist.child("學生"+drama_studentNumber_word+"號").child(drama_dramaNumber_word).child("8").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String[] s1 = new String[4];
                    drama8.setVisibility(VISIBLE);
                    creat8.setVisibility(VISIBLE);
                    drama8.setEnabled(true);
                    try{
                        drama8.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                drama_number=8;
                                one_listen = false;
                                one_speak = false;
                                drama_record1 = false;
                                drama_record2 = false;
                                drama_record3 = false;
                                drama_record4 = false;
                                StoreTheEditData storeTheEditData = dataSnapshot.getValue(StoreTheEditData.class);
                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                if(storeTheEditData.getPlayerA_text().equals("") == false){
                                    s1[count]=storeTheEditData.getPlayerA_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayerB_text().equals("") == false){
                                    s1[count]=storeTheEditData.getPlayerB_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayer3_text().equals("") == false ){
                                    s1[count]=storeTheEditData.getPlayer3_text();
                                    count++;
                                }
                                if(storeTheEditData.getPlayer4_text().equals("") == false ){
                                    s1[count]=storeTheEditData.getPlayer4_text();
                                    count++;
                                }

                                String[] s=new String[count];
                                for (int x=0;x<count;x++){
                                    s[x]=s1[x];
                                }

                                if(s[0].equals("")==false) {
                                    say1 = false;
                                    say2 = false;
                                    say3 = false;
                                    say4 = false;
                                    practice_say.setText("\t"+s[0]);
                                    choose=0;
                                    practice_say_right.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (choose < s.length-1) {
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                practice_say.setText("\t"+s[choose+1]);
                                                if (choose+1 == int_bol_speak-1){
                                                    practice_say.setTextColor(Color.rgb(255,0,0));
                                                }else{
                                                    practice_say.setTextColor(Color.rgb(0,0,0));
                                                }
                                                choose++;
                                                one_listen = false;
                                                one_speak = false;
                                            }else{
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24_black);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                Toast.makeText(DramaAchievement.this, "最後一句", Toast.LENGTH_SHORT).show();
                                            }
                                            drama_record1 = false;
                                            drama_record2 = false;
                                            drama_record3 = false;
                                            drama_record4 = false;
                                        }
                                    });
                                    practice_say_left.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (choose-1 < s.length && choose > 0 ) {
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24);
                                                practice_say.setText("\t"+s[choose-1]);
                                                if (choose-1 == int_bol_speak-1){
                                                    practice_say.setTextColor(Color.rgb(255,0,0));
                                                }else{
                                                    practice_say.setTextColor(Color.rgb(0,0,0));
                                                }
                                                choose--;
                                                one_listen = false;
                                                one_speak = false;
                                            } else {
                                                practice_say_left.setBackgroundResource(R.drawable.ic_baseline_chevron_left_24_black);
                                                practice_say_right.setBackgroundResource(R.drawable.ic_baseline_chevron_right_24);
                                                Toast.makeText(DramaAchievement.this, "第一句", Toast.LENGTH_SHORT).show();
                                            }
                                            drama_record1 = false;
                                            drama_record2 = false;
                                            drama_record3 = false;
                                            drama_record4 = false;
                                        }
                                    });

                                    btn_speak.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int x=0;
                                            for(int i=0;i<s.length;i++){
                                                if(practice_say.getText().toString().trim().equals(s[i].trim()) == true){
                                                    x=i;
                                                }
                                            }
                                            switch(x){
                                                case 0:
                                                    if(!drama_record1){
                                                        pathword_a = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 8 + "_" + "A.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_a);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record1 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 1:
                                                    if(!drama_record2) {
                                                        pathword_b = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 8 + "_" + "B.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_b);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record2 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 2:
                                                    if(!drama_record3) {
                                                        pathword_3 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 8 + "_" + "3.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_3);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record3 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                                case 3:
                                                    if(!drama_record4) {
                                                        pathword_4 = drama_studentNumber_word + "_" + drama_dramaNumber_word + "_" + 8 + "_" + "4.amr";
                                                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word + "/" + pathword_4);
                                                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                drama_record4 = true;
                                                                Link = uri.toString();
                                                                Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
                                                                player = new MediaPlayer();
                                                                try {
                                                                    player.setDataSource(Link);
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                try {
                                                                    player.prepare();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                player.start();
                                                                listen_record_count++;
                                                                one_listen = true;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(DramaAchievement.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else {
                                                        player.start();
                                                        listen_record_count++;
                                                        one_listen = true;
                                                    }
                                                    break;
                                            }
                                            if(one_speak){
                                                one_count++;
                                                one_speak = false;
                                                one_listen = false;
                                            }
                                        }
                                    });
                                    getanswer.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(practice_say.getText().toString().equals("") == false){
                                                one_speak = true;
                                                int c2 = practice_say.getText().toString().indexOf(":");
                                                String s2 = practice_say.getText().toString().substring(c2+1,practice_say.length());
                                                CompareSentences = s2;
                                                StartSpeechRecongizer();
                                            }

                                            switch (choose) {
                                                case 0:
                                                    say1 = true;
                                                    break;
                                                case 1:
                                                    say2 = true;
                                                    break;
                                                case 2:
                                                    say3 = true;
                                                    break;
                                                case 3:
                                                    say4 = true;
                                                    break;
                                            }

                                            switch (s.length){
                                                case 2:
                                                    if(say1 ==true && say2 == true){
                                                        finish_drama8=true;
                                                    }
                                                    break;
                                                case 3:
                                                    if(say1 ==true && say2 == true && say3 == true){
                                                        finish_drama8=true;
                                                    }
                                                    break;
                                                case 4:
                                                    if(say1 ==true && say2 == true && say3 == true && say4 == true){
                                                        finish_drama8=true;
                                                    }
                                                    break;
                                            }

                                            if(x == 8 && finish_drama8 == true && finish_drama7 == true && finish_drama6 == true && finish_drama5 == true && finish_drama4 == true && finish_drama3
                                                    == true && finish_drama2 == true && finish_drama1 == true){
                                                all_finish++;
                                            }

                                            if(one_listen){
                                                one_count++;
                                                one_speak = false;
                                                one_listen = false;
                                            }

                                        }
                                    });
                                }
                                count = 0;
                                Toast.makeText(DramaAchievement.this,"已選擇第八分鏡",Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Glide.with(drama8.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama8);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama8.setImageResource(R.drawable.noimage);
                    drama8.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });
    }

    //初始化聆聽者
    private void InitSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);//建構值
        speechRecognizer.setRecognitionListener(new DramaAchievement.SpeechListener());// 設定聆聽者
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

    private class SpeechListener implements RecognitionListener
    {
        @Override
        public void onReadyForSpeech(Bundle params) {
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
            Log.d("Speech","End...");
        }

        @Override
        public void onError(int error) {
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String color = "green";//字體顏色
            final String word = data.get(0).toString().trim();
//            practice_say.setText("\t"+word);

            if (CompareSentences != null) {
                speak_count++;
                String response = word.replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                //String question = textResolt.getText().toString().replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                String question = CompareSentences.replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                if (response.equals(question)) {
                    showdescribescore.setText(word);
                    //  textSpeech.setText(word);
                    //double score = (double) (results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)[0]*100);
                    int score = (int) (results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)[0] * 100);
                    //textScore.setText("正確 : "+score+"/100");
                    // textSpeech.append(Html.fromHtml("<br>Correct: <font color=\""+ color +"\">" + score + "%</font>"));
                    //showdescribescore.setVisibility(View.VISIBLE);
                    if (score >= 90) {
                        showdescribescore.append(Html.fromHtml("<br> <font color=\"" + color + "\">" + score + "%</font>"));
                        showdescribescore.append("\n你念的很棒哦! 繼續保持");
                    } else if (score >= 80) {
                        showdescribescore.append(Html.fromHtml("<br> <font color= #FFA500 >" + score + "%</font>"));
                        showdescribescore.append("\n很厲害欸! 快90%了，再試試看吧！");
                    }
                    try {
                        fire_speechdata = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("DramaAchievement");
                        fire_speechdata.child("SpeechData").child("Score").push().setValue(score);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(DramaAchievement.this, "儲存口說分數錯誤", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    double len = question.length() > response.length() ? question.length() : response.length();//比較題目與答案字串長度
                    diffMatchPatch diff_match_patch_obj = new diffMatchPatch();//比對的Class
                    LinkedList<diffMatchPatch.Diff> diff = diff_match_patch_obj.diff_lineMode(response, question, 31);
                    String htmlDiff = diff_match_patch_obj.diff_prettyHtml(diff);
                    htmlDiff = htmlDiff.replaceAll(" </u>", "</u> ");
                    htmlDiff = htmlDiff.replaceAll(" </del>", "</del> ");
                    htmlDiff = htmlDiff.replaceAll(" </span>", "</span> ");
                    Spanned recorrect_response = Html.fromHtml(htmlDiff, null, new Html.TagHandler() {
                        int startTag;
                        int endTag;

                        @Override
                        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                            if (tag.equalsIgnoreCase("del")) {
                                if (opening) {
                                    startTag = output.length();
                                } else {
                                    endTag = output.length();
                                    output.setSpan(new StrikethroughSpan(), startTag, endTag, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }
                        }
                    });
                    showdescribescore.setText(recorrect_response);
                    //   textSpeech.setText(recorrect_response);

                    //double similarity = (int)((len - diff_match_patch_obj.diff_levenshtein(diff)) / len * 1000) / 10.0;
                    int similarity = (int) ((len - diff_match_patch_obj.diff_levenshtein(diff)) / len * 1000) / 10;
                    if (similarity >= 90) {
                        color = "green";
                        encourage = "\n你念的很棒哦! 繼續保持";
                        //showdescribescore.append("\n你念的很棒哦! 繼續保持");
                    } else if (similarity >= 80 && similarity < 90) {
                        color = "#FFA500";
                        encourage = "\n很厲害欸! 快90%了，再試試看吧！";
                        //showdescribescore.append("\n 你念的不錯哦! 可以再更好,加油!");
                    } else if (similarity >= 70 && similarity < 80) {
                        color = "#FFA500";
                        encourage = "\n你念的不錯哦! 可以再更好,加油!";
                        //showdescribescore.append("\n 你念的不錯哦! 可以再更好,加油!");
                    } else if (similarity >= 60 && similarity < 70) {
                        color = "#FFA500";
                        encourage = "\n及格了! 你一定可以更好, 加油!";
                        //showdescribescore.append("\n 你念的不錯哦! 可以再更好,加油!");
                    } else {
                        color = "red";
                        encourage = "\n這句念得不是很正確哦！不要氣餒，再試試看！";
                        //showdescribescore.append("\n 這句念得不是很正確哦！不要氣餒，再試試看！");
                    }
                    // textSpeech.append(Html.fromHtml("<br>Similarity: <font color=\""+ color +"\">" + similarity + "%</font>"));
                    showdescribescore.setVisibility(View.VISIBLE);
                    showdescribescore.append(Html.fromHtml("<br> <font color=\"" + color + "\">" + similarity + "%</font>"));
                    showdescribescore.append(encourage);
                    Toast.makeText(DramaAchievement.this, showdescribescore.getText().toString(), Toast.LENGTH_SHORT).show();
                    try {
                        final String date = sdf.format(new java.util.Date());
                        final DatabaseReference fire_timeclick = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("Student data")
                                .child("點擊行為").child("DramaAchievement").child("口說練習");
                        fire_timeclick.child(date).child("Score").setValue(similarity);
                        fire_speechdata = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("DramaAchievement");
                        fire_speechdata.child("SpeechData").child("Score").push().setValue(similarity);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(DramaAchievement.this, "儲存口說分數錯誤", Toast.LENGTH_SHORT).show();
                    }
                }
                try {
                    final String date = sdf.format(new java.util.Date());
                    final DatabaseReference fire_timeclick = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("Student data")
                            .child("點擊行為").child("DramaAchievement").child("口說練習");
                    fire_timeclick.child(date).child("Drama Name").setValue(select_string);
                    fire_timeclick.child(date).child("Student number").setValue(drama_studentNumber_word);
                    fire_timeclick.child(date).child("Drama Number").setValue("Drama"+drama_number);
                    fire_timeclick.child(date).child("Correct Text").setValue(CompareSentences);
                    fire_timeclick.child(date).child("User say").setValue(word);
                    fire_speechdata = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("DramaAchievement");
                    fire_speechdata.child("SpeechData").child("Usersay").push().setValue(word);
                    fire_speechdata.child("SpeechData").child("correctText").push().setValue(CompareSentences);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(DramaAchievement.this, "儲存口說跟正確答案錯誤", Toast.LENGTH_SHORT).show();
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
    protected void onPause() {
        super.onPause();
        speechRecognizer.stopListening();
        speechRecognizer.destroy();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.stopListening();
        speechRecognizer.destroy();
    }

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
//                Init_AIConfiguration();//更新對話資料庫
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
                PlaceName.setText((keyDatabase.Place));

                //抓到PlaceName後，讀取相對應的資料塞進String陣列中，在Adapter進AutoCompleteTextview中
//                fire_vocabulary.child(PlaceName.getText().toString()).child("sentence").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        int i = 0;
//                        autoStrs= new String [(int)dataSnapshot.getChildrenCount()];   //若擺在for迴圈中會每一次都洗掉 抓不到值 null
//                        for(DataSnapshot each : dataSnapshot.getChildren()){
//                            autoStrs[i] = each.getValue().toString();
//                            i++;
//                        }
//                        String[] auto = autoStrs;
//                        adapter = new ArrayAdapter<String>(DescribeActivity.this, android.R.layout.simple_dropdown_item_1line,auto);
//                        studentdescribe.setAdapter(adapter);
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) { }
//                });
            }
        }

        if (Unidentified_List.size() == 0) {
            PlaceName.setText("Other");
        }
    }

    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            final Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm", now);
            if (location != null) {
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



    @Override
    protected void onResume() {
        super.onResume();
        InitSpeechRecognizer();
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void run() {
                getCurrentLocation();
            }
        }, 1000);
    }

    //定位
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled)){}
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
        if (location != null){
            //(Carol) drawMarker(location);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            writeInfo(Student.Name+"號學生觀看練習劇本行為紀錄","觀看劇本");
            bol_60 = true;
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
            bw.write("聆聽錄音次數:"+listen_record_count+"\n");
            bw.write("TTS次數:"+tts_count+"\n");
            bw.write("口說次數:"+speak_count+"\n");
            bw.write("完整練習次數:"+all_finish+"\n");
            bw.write("分段練習次數:"+one_count+"\n");

            try {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference db = database.getReference().child("學生"+Student.Name+"號").child("Student data").child("DramaAchievement");
                DatabaseReference db2 = database.getReference().child("Other").child("stu_data").child("DramaAchievement");
                db.child("Listen record").setValue(listen_record_count);
                db.child("TTS").setValue(tts_count);
                db.child("Speak count").setValue(speak_count);
                db.child("All Drama count").setValue(all_finish);
                db.child("One exercise count").setValue(one_count);
                int all_finish_coin = all_finish*10;
                int one_finish_coin = one_count*5;
                db2.child(Student.Name).setValue(all_finish_coin+speak_count+one_finish_coin);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(DramaAchievement.this, "儲存錯誤", Toast.LENGTH_SHORT).show();
            }

            bw.close();

            Toast.makeText(DramaAchievement.this, "行為紀錄紀錄成功", Toast.LENGTH_SHORT).show();
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
            listen_record_count= Integer.parseInt(response.substring(response.indexOf("聆聽錄音次數:")+7,response.indexOf("TTS次數:")-1));
            tts_count= Integer.parseInt(response.substring(response.indexOf("TTS次數:")+6,response.indexOf("口說次數:")-1));
            speak_count= Integer.parseInt(response.substring(response.indexOf("口說次數:")+5,response.indexOf("完整練習次數:")-1));
            all_finish= Integer.parseInt(response.substring(response.indexOf("完整練習次數:")+7,response.indexOf("分段練習次數:")-1));
            one_count = Integer.parseInt(response.substring(response.indexOf("分段練習次數:")+7,response.length()-1));
            br.close();
            Toast.makeText(DramaAchievement.this, "行為紀錄讀取成功", Toast.LENGTH_SHORT).show();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

//    public void time(){
//        //取得現在時間
//        tt = 15;
//        timer = new Timer();
//        final TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                if(!bol_60){
//                    if(tt==15){
//                        first_listen_record_count=listen_record_count;
//                        first_tts_count=tts_count;
//                        first_speak_count=speak_count;
//                        first_one_count=one_count;
//                        sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
//                        date = sdf.format(new java.util.Date());
//                    }
//                    tt--;
//                    if(tt<1){
//                        try{
//                            //上傳60秒內的點擊行為
//                            DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference();
//                            fire_60sec_student_data.child("學生"+Student.Name+"號").child("Student data")
//                                    .child("60").child("DramaAchievement").child(date).child("Listen record").setValue(listen_record_count-first_listen_record_count);
//                            fire_60sec_student_data.child("學生"+Student.Name+"號").child("Student data")
//                                    .child("60").child("DramaAchievement").child(date).child("Speak count").setValue(speak_count-first_speak_count);
//                            fire_60sec_student_data.child("學生"+Student.Name+"號").child("Student data")
//                                    .child("60").child("DramaAchievement").child(date).child("TTS").setValue(tts_count-first_tts_count);
//                            fire_60sec_student_data.child("學生"+Student.Name+"號").child("Student data")
//                                    .child("60").child("DramaAchievement").child(date).child("Drama name").setValue(select_string);
//                            fire_60sec_student_data.child("學生"+Student.Name+"號").child("Student data")
//                                    .child("60").child("DramaAchievement").child(date).child("Student number").setValue(drama_studentNumber_word);
//                            //分段練習劇本次數
//                            fire_60sec_student_data.child("學生"+Student.Name+"號").child("Student data")
//                                    .child("60").child("DramaAchievement").child(date).child("One exercise").setValue(one_count-first_one_count);
//                            tt=15;
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        };
//        timer.schedule(task,1000,1000);
//    }

}
