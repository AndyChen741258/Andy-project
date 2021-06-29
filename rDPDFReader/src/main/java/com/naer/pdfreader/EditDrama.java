package com.naer.pdfreader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DialogTitle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.nsd.NsdManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import Model.DescribeData;
import Model.StoreTheEditData;
import ai.api.model.ResponseMessage;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.naer.pdfreader.CreatDrama.spinner_drame_word;
import static com.naer.pdfreader.DialogActivity.TAG;

public class EditDrama extends Activity {

    private ImageView editimage;
    private Button adddialog1;
    private Button adddialog2;
    private Button finishedit;
    private Button student_question;
    private TextView say1; //隱藏的角色 A 對話框
    private TextView say2; //隱藏的角色 B 對話框
    private TextView say3;
    private TextView say4;
    private TextView say5;
    private TextView say6;
    private EditText editText1;
    private EditText editText2;

    //角色 A 錄音儲存檔名
    private String pathword_a = Student.Name + "_" + spinner_drame_word + "_" + CreatDrama.num + "_" + "A.amr";
    //角色 B 錄音儲存檔名
    private String pathword_b = Student.Name + "_" + spinner_drame_word + "_" + CreatDrama.num + "_" + "B.amr";
    private String pathword_3 = Student.Name + "_" + spinner_drame_word + "_" + CreatDrama.num + "_" + "3.amr";
    private String pathword_4 = Student.Name + "_" + spinner_drame_word + "_" + CreatDrama.num + "_" + "4.amr";
    private String pathword_5 = Student.Name + "_" + spinner_drame_word + "_" + CreatDrama.num + "_" + "5.amr";
    private String pathword_6 = Student.Name + "_" + spinner_drame_word + "_" + CreatDrama.num + "_" + "6.amr";

    //-------------錄音功能--------------
    private MediaRecorder recorder;
    private StorageReference fire_dramarecord;
    //限制錄音按鈕僅能按一下, 等按下停止時才可以再恢復可按狀態
    private boolean isPress = true;


    //-------------播放錄音--------------
    private MediaPlayer player;
    private String record_download_url_a = "";
    private String record_download_url_b = "";
    private String record_download_url_3 = "";
    private String record_download_url_4 = "";
    private String record_download_url_5 = "";
    private String record_download_url_6 = "";
    int playtimeA ;
    int playtimeB = 0;
    int playtime3 = 0;
    int playtime4 = 0;
    int playtime5 = 0;
    int playtime6 = 0;
    private String Link;

    int timeCount;

    private DatabaseReference fire_editdata;

    //存放編輯好的圖片截圖
    private StorageReference fire_finishphoto;

    private StorageReference fire_check_ori_exist;

    public static String download_url_editFinish = "";

    StoreTheEditData storeTheEditData;

    private String re_a_line_word = "";
    private String re_b_line_word = "";
    private String re_3_line_word = "";
    private String re_4_line_word = "";
    private String re_5_line_word = "";
    private String re_6_line_word = "";
    private String info=null;
    private String info1=null;
    private String info2=null;
    private String info3=null;
    private String info4=null;
    private EditText editText3;
    private EditText editText4;
    private float say1_X;
    private float say1_Y;
    private float say2_X;
    private float say2_Y;
    private float say3_X;
    private float say3_Y;
    private float say4_X;
    private float say4_Y;

    private ImageView imageView;
    private int bubble=0;
    private int choose=0;
    private String re_a_line;
    private boolean re_a=false;
    private EditText re_b_line;
    private boolean re_b=false;
    private EditText re_3_line;
    private boolean re_3=false;
    private EditText re_4_line;
    private boolean re_4=false;
    private String username;
    private MenuItem option4;
    private MenuItem option5;
    private MenuItem option6;
    private MenuItem option7;
    private MenuItem option8;
    private String textOut;
    private boolean textOut_bol=false;
    private String textOut_5;
    private boolean textOut_bol_5=false;
    private String textOut_6;
    private boolean textOut_bol_6=false;
    private String textOut_7;
    private boolean textOut_bol_7=false;
    private String textOut_8;
    private boolean textOut_bol_8=false;
    private long lastPressTime;
    private static final long DOUBLE_PRESS_INTERVAL = 250;
    private View view;
    private float x_touch;
    private float y_touch;
    private float[] lastTouchDownXY = new float[2];
    private DatabaseReference fire_contextmenu;
    private DatabaseReference fire_load_contextmenu;
    private Button btn_tts_edit;
    private String edit_name;
    private String[] list=new String[8];
    private String spinner_item;
    private String role_spinner;
    private Button btn_speak;
    private boolean bol_btn_speak = false;
    private Button play1;
    private Button play2;
    private Button play3;
    private Button play4;
    private int tts_count=0;
    private int stoptimeA=0;
    private int stoptimeB=0;
    private int stoptime3=0;
    private int stoptime4=0;
    private SimpleDateFormat sdf;




    @SuppressLint({"ClickableViewAccessibility", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//隱藏標題列
        setContentView(R.layout.activity_edit_drama);

        editimage = findViewById(R.id.editimage);
        adddialog1 = findViewById(R.id.adddialog1);
        adddialog2 = findViewById(R.id.adddialog2);
        finishedit = findViewById(R.id.finishedit);
        btn_speak = findViewById(R.id.btn_speak);
        say1 = findViewById(R.id.say1);
        say2 = findViewById(R.id.say2);
        say3 = findViewById(R.id.say3);
        say4 = findViewById(R.id.say4);
        say5 = findViewById(R.id.say5);
        say6 = findViewById(R.id.say6);
        say1.setVisibility(View.INVISIBLE);
        say2.setVisibility(View.INVISIBLE);
        say3.setVisibility(View.INVISIBLE);
        say4.setVisibility(View.INVISIBLE);
        say5.setVisibility(View.INVISIBLE);
        say6.setVisibility(View.INVISIBLE);

        imageView = findViewById(R.id.editimage);
        TTS.init(getApplicationContext());

        list[0]="Father";
        list[1]="Brother";
        list[2]="Sister";

        readInfo(Student.Name+"號學生創建劇本行為紀錄");

        //取得現在時間
        sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");




//        registerForContextMenu(say1);
//        registerForContextMenu(say2);
//        registerForContextMenu(say3);
//        registerForContextMenu(say4);



        /// 新增泡泡框
        loadicon();
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                x_touch = lastTouchDownXY[0];
                y_touch = lastTouchDownXY[1];
                Log.e("Location", String.valueOf(x_touch));
                Log.e("Location", String.valueOf(y_touch));
                switch (bubble){
                    case 0:
                        Log.v("第一個X位置", String.valueOf(x_touch));
                        Log.v("第一個Y位置", String.valueOf(y_touch));
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(say1.getWidth(), say1.getHeight());
                        params.setMargins((int) x_touch, (int) y_touch, 0, 0);
                        if(params.width<100){
                            params.width=400;
                        }
                        say1.setLayoutParams(params);
                        Drawable drawable = getResources().getDrawable(R.drawable.line1);
                        drawable.setBounds(0, 0, 40,40);
                        say1.setCompoundDrawables(drawable,null,null, null);
                        say1.setVisibility(VISIBLE);
                        loadicon();
                        bubble++;
                        break;
                    case 1:
                        Log.v("第二個X位置", String.valueOf(x_touch));
                        Log.v("第二個Y位置", String.valueOf(y_touch));
                        RelativeLayout.LayoutParams params2= new RelativeLayout.LayoutParams(say2.getWidth(), say2.getHeight());
                        params2.setMargins((int) x_touch, (int) y_touch, 0, 0);
                        if(params2.width<100){
                            params2.width=400;
                        }
                        say2.setLayoutParams(params2);
                        Drawable drawable2 = getResources().getDrawable(R.drawable.line2);
                        drawable2.setBounds(0, 0, 40,40);
                        say2.setCompoundDrawables(drawable2, null,null, null);
                        say2.setVisibility(VISIBLE);
                        loadicon();
                        bubble++;
                        break;
                    case 2:
                        new AlertDialog.Builder(EditDrama.this)
                            .setIcon(R.drawable.ic_launcher)
                            .setTitle("新增對話框?")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.v("第四個的寬3", String.valueOf(say3.getWidth()));
                                    Log.v("第四個的長3", String.valueOf(say3.getHeight()));
                                    RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(say3.getWidth(), say3.getHeight());
                                    params3.setMargins((int) x_touch, (int) y_touch, 0, 0);
                                    if(params3.width<100){
                                        params3.width=400;
                                    }
                                    say3.setLayoutParams(params3);
                                    Drawable drawable3 = getResources().getDrawable(R.drawable.line3);
                                    drawable3.setBounds(0, 0, 40,40);
                                    say3.setCompoundDrawables(drawable3, null,null, null);
                                    say3.setVisibility(VISIBLE);
                                    loadicon();
                                    bubble++;
                                }
                            })
                            .setNegativeButton("取消",null).create()
                            .show();
                        Log.v("第三個X位置", String.valueOf(x_touch));
                        Log.v("第三個Y位置", String.valueOf(y_touch));

                        break;
                    case 3:
                        new AlertDialog.Builder(EditDrama.this)
                                .setIcon(R.drawable.ic_launcher)
                                .setTitle("新增對話框?")
                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.v("第四個X位置", String.valueOf(x_touch));
                                        Log.v("第四個Y位置", String.valueOf(y_touch));
                                        Log.v("第四個的寬", String.valueOf(say4.getWidth()));
                                        Log.v("第四個的長", String.valueOf(say4.getHeight()));
                                        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(say4.getWidth(), say4.getHeight());
                                        if(params4.width<100){
                                            params4.width=400;
                                        }
                                        params4.setMargins((int) x_touch, (int) y_touch, 0, 0);
                                        say4.setLayoutParams(params4);
                                        Drawable drawable4 = getResources().getDrawable(R.drawable.line4);
                                        drawable4.setBounds(0, 0, 40,40);
                                        say4.setCompoundDrawables(drawable4, null,null, null);
                                        say4.setVisibility(VISIBLE);
                                        loadicon();
                                        bubble++;
                                    }
                                })
                                .setNegativeButton("取消",null).create()
                                .show();
                        break;
                }
                return true;
            }
        });
        imageView.setOnTouchListener(imageTouch);
        say1.setOnTouchListener(say1Listener);
        say2.setOnTouchListener(say2Listener);
        say3.setOnTouchListener(say3Listener);
        say4.setOnTouchListener(say4Listener);
//        imageView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                switch (bubble){
//                    case 2:
//                        new AlertDialog.Builder(EditDrama.this)
//                                .setIcon(R.drawable.ic_launcher)
//                                .setTitle("隱藏"+info1)
//                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        say2.setVisibility(INVISIBLE);
//                                    }
//                                })
//                                .setNegativeButton("取消",null).create()
//                                .show();
//                        bubble--;
//                        break;
//                    case 1:
//                        new AlertDialog.Builder(EditDrama.this)
//                                .setIcon(R.drawable.ic_launcher)
//                                .setTitle("隱藏"+info1)
//                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        say1.setVisibility(INVISIBLE);
//                                    }
//                                })
//                                .setNegativeButton("取消",null).create()
//                                .show();
//                        bubble--;
//                        break;
//                }
//                return true;
//            }
//        });





        //判斷是否是更新編輯
        //若是更新編輯 則執行撈回資料的方法
        //若非更新編輯, 也就是第一次編輯 則從Creat那邊將拍好的照片放過來進行編輯

        if(CreatDrama.edit){
            returnbackdata();
            if (say1.getText().toString()==""){
                say1.setVisibility(INVISIBLE);
            }

            if(say2.getText().toString()==""){
                say2.setVisibility(INVISIBLE);
            }

//            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(400, say2.getHeight());
//            params2.setMargins((int) say2_X, (int) say2_Y, 0, 0);
//            say2.setLayoutParams(params2);


            if(say3.getText().toString()==""){
                say3.setVisibility(INVISIBLE);
            }

//            RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(400, say3.getHeight());
//            params3.setMargins((int) say3_X, (int) say3_Y, 0, 0);
//            say3.setLayoutParams(params3);


            if (say4.getText().toString()=="") {
                say4.setVisibility(INVISIBLE);
            }

//            RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(400, say4.getHeight());
//            params4.setMargins((int) say4_X, (int) say4_Y, 0, 0);
//            say4.setLayoutParams(params4);

        }else{
            //方法一 撈回Firebase圖片(較久)
//            fire_check_ori_exist = FirebaseStorage.getInstance().getReference()
//                    .child(Student.Name + "/OriginalPhoto/" + CreatDrama.spinner_drame_word + "/" + "ori_screen" + CreatDrama.num + ".jpeg");
//            if(!fire_check_ori_exist.equals(null)){
//                fire_check_ori_exist.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        Uri uri = task.getResult();
//                        a = uri.toString();
//                        Glide.with(editimage.getContext()).load(a).into(editimage);
//                    }
//                });
//            }
            //-------------------------------------------------------------------------
            //方法二 從Creat那邊將拍好的照片直接Intent過來
            Intent intent = this.getIntent();
            //取得傳遞過來的資料
            String ooo = intent.getStringExtra("ori_uri");
            editimage.setImageURI(Uri.parse(ooo));
            Log.v("測試1",Uri.parse(ooo).toString());
        }


//        say5.setOnTouchListener(say5Listener);
//        say6.setOnTouchListener(say6Listener);

//        say1.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                buildLinesA_Click();
//                return false;
//            }
//        });


        adddialog1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildLinesA_Click();
            }
        });

        adddialog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildLinesB_Click();
            }
        });

        try {
            fire_load_contextmenu = FirebaseDatabase.getInstance().getReference();
            fire_load_contextmenu.child("學生" + Student.Name + "號").child(spinner_drame_word).child("contextmenu")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int i = 0;
                            String[] sssttt = new String[(int) dataSnapshot.getChildrenCount()];
                            for (DataSnapshot each : dataSnapshot.getChildren()) {
                                sssttt[i] = each.getValue().toString();
                                Log.v("GET sssttt", sssttt[i]);
                                Log.v("GET i", String.valueOf(i));
                                i++;
                            }
                            Log.v("GET i", String.valueOf(i));
                            switch (i-1){
                                case 0:
                                    textOut_bol = true;
                                    textOut=sssttt[i-1];
                                    list[3]=sssttt[i-1];
                                    break;
                                case 1:
                                    textOut_bol = true;
                                    textOut_bol_5 = true;
                                    textOut=sssttt[i-2];
                                    textOut_5=sssttt[i-1];
                                    list[3]=sssttt[i-2];
                                    list[4]=sssttt[i-1];
                                    break;
                                case 2:
                                    textOut_bol = true;
                                    textOut_bol_5 = true;
                                    textOut_bol_6 = true;
                                    textOut=sssttt[i-3];
                                    textOut_5=sssttt[i-2];
                                    textOut_6=sssttt[i-1];
                                    list[3]=sssttt[i-3];
                                    list[4]=sssttt[i-2];
                                    list[5]=sssttt[i-1];
                                    break;
                                case 3:
                                    textOut_bol = true;
                                    textOut_bol_5 = true;
                                    textOut_bol_6 = true;
                                    textOut_bol_7 = true;
                                    textOut=sssttt[i-4];
                                    textOut_5=sssttt[i-3];
                                    textOut_6=sssttt[i-2];
                                    textOut_7=sssttt[i-1];
                                    list[3]=sssttt[i-4];
                                    list[4]=sssttt[i-3];
                                    list[5]=sssttt[i-2];
                                    list[6]=sssttt[i-1];
                                    break;
                                case 4:
                                    textOut_bol = true;
                                    textOut_bol_5 = true;
                                    textOut_bol_6 = true;
                                    textOut_bol_7 = true;
                                    textOut_bol_8 = true;
                                    textOut=sssttt[i-5];
                                    textOut_5=sssttt[i-4];
                                    textOut_6=sssttt[i-3];
                                    textOut_7=sssttt[i-2];
                                    textOut_8=sssttt[i-1];
                                    list[3]=sssttt[i-5];
                                    list[4]=sssttt[i-4];
                                    list[5]=sssttt[i-3];
                                    list[6]=sssttt[i-2];
                                    list[7]=sssttt[i-1];
                                    break;
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //照片庫
                    if (ContextCompat.checkSelfPermission(EditDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {  //拍照
                    BringImagePicker();
                }
            }
        });





//        btn_speak.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (bol_btn_speak == false){
//                    String s1="",s2="",s3="",s4="";
//                    Spinner spinner=new Spinner(EditDrama.this);
//                    String[] list1 = new String[4];
//                    int count=0;
//
//                    if(say1.getText().toString() != ""){
//                        int c1 = say1.getText().toString().indexOf(":");
//                        s1 = say1.getText().toString().substring(0,c1);
//                        count++;
//                        list1[0]=s1;
//                    }
//                    if(say2.getText().toString() != ""){
//                        int c2 = say2.getText().toString().indexOf(":");
//                        s2 = say2.getText().toString().substring(0,c2);
//                        count++;
//                        list1[1]=s2;
//                    }
//                    if(say3.getText().toString() != ""){
//                        int c3 = say3.getText().toString().indexOf(":");
//                        s3 = say3.getText().toString().substring(0,c3);
//                        count++;
//                        list1[2]=s3;
//                    }
//                    if(say4.getText().toString() != ""){
//                        int c4 = say4.getText().toString().indexOf(":");
//                        s4 = say4.getText().toString().substring(0,c4);
//                        count++;
//                        list1[3]=s4;
//                    }
//
//                    String[] list2 = new String[count];
//                    for(int i=0;i<count;i++){
//                        if(!list1[i].matches("")){
//                            list2[i]=list1[i];
//                        }
//                    }
//
//
//                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(EditDrama.this);
//                    alertdialog.setTitle("選擇對話");
//                    alertdialog .setIcon(R.drawable.ic_launcher);
//                    ArrayAdapter<String> numberList=new ArrayAdapter<String>(EditDrama.this,
//                            android.R.layout.simple_spinner_dropdown_item,
//                            list2);
//                    spinner.setAdapter(numberList);
//                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            role_spinner=spinner.getSelectedItem().toString();
//                        }
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
//                        }
//                    });
//                    alertdialog .setView(spinner);
//                    alertdialog .setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                        // do something when the button is clicked
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            role_spinner=spinner.getSelectedItem().toString();
//                            bol_btn_speak = true;
//                        }
//                    });
//                    alertdialog .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        // do something when the button is clicked
//                        public void onClick(DialogInterface arg0, int arg1) {
//                        }
//                    });
//                    alertdialog .show();
//                    Toast.makeText(EditDrama.this,"請先選擇角色", Toast.LENGTH_SHORT).show();
//                }else{
//                    String s1="",s2="",s3="",s4="";
//                    if(say1.getText().toString() != ""){
//                        int c1 = say1.getText().toString().indexOf(":");
//                        s1 = say1.getText().toString().substring(0,c1);
//                        if(role_spinner.equals(s1) == false){
//                            try {
//                                TTS.speak(say1.getText().toString());
//                                Toast.makeText(EditDrama.this,"角色:"+s1, Toast.LENGTH_SHORT).show();
//                                Thread.sleep(5000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//
//
//                    if(say2.getText().toString() != ""){
//                        int c2 = say2.getText().toString().indexOf(":");
//                        s2 = say2.getText().toString().substring(0,c2);
//                        if(role_spinner.equals(s2) == false){
//                            try {
//                                TTS.speak(say2.getText().toString());
//                                Toast.makeText(EditDrama.this,"角色:"+s2, Toast.LENGTH_SHORT).show();
//                                Thread.sleep(5000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    if(say3.getText().toString() != ""){
//                        int c3 = say3.getText().toString().indexOf(":");
//                        s3 = say3.getText().toString().substring(0,c3);
//                        if(role_spinner.equals(s3) == false){
//                            try {
//                                TTS.speak(say3.getText().toString());
//                                Toast.makeText(EditDrama.this,"角色:"+s3, Toast.LENGTH_SHORT).show();
//                                Thread.sleep(5000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    if(say4.getText().toString() != ""){
//                        int c4 = say4.getText().toString().indexOf(":");
//                        s4 = say4.getText().toString().substring(0,c4);
//                        if(role_spinner.equals(s4) == false){
//                            try {
//                                TTS.speak(say4.getText().toString());
//                                Toast.makeText(EditDrama.this,"角色:"+s4, Toast.LENGTH_SHORT).show();
//                                Thread.sleep(5000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//                    Toast.makeText(EditDrama.this,"播放結束", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

//        btn_speak.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                String s1="",s2="",s3="",s4="";
//                Spinner spinner=new Spinner(EditDrama.this);
//                String[] list1 = new String[4];
//                int count=0;
//
//                if(say1.getText().toString() != ""){
//                    int c1 = say1.getText().toString().indexOf(":");
//                    s1 = say1.getText().toString().substring(0,c1);
//                    count++;
//                    list1[0]=s1;
//                }
//                if(say2.getText().toString() != ""){
//                    int c2 = say2.getText().toString().indexOf(":");
//                    s2 = say2.getText().toString().substring(0,c2);
//                    count++;
//                    list1[1]=s2;
//                }
//                if(say3.getText().toString() != ""){
//                    int c3 = say3.getText().toString().indexOf(":");
//                    s3 = say3.getText().toString().substring(0,c3);
//                    count++;
//                    list1[2]=s3;
//                }
//                if(say4.getText().toString() != ""){
//                    int c4 = say4.getText().toString().indexOf(":");
//                    s4 = say4.getText().toString().substring(0,c4);
//                    count++;
//                    list1[3]=s4;
//                }
//
//                String[] list2 = new String[count];
//                for(int i=0;i<count;i++){
//                    if(!list1[i].matches("")){
//                        list2[i]=list1[i];
//                    }
//                }
//
//
//                AlertDialog.Builder alertdialog = new AlertDialog.Builder(EditDrama.this);
//                alertdialog.setTitle("選擇對話");
//                alertdialog .setIcon(R.drawable.ic_launcher);
//                ArrayAdapter<String> numberList=new ArrayAdapter<String>(EditDrama.this,
//                        android.R.layout.simple_spinner_dropdown_item,
//                        list2);
//                spinner.setAdapter(numberList);
//                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        role_spinner=spinner.getSelectedItem().toString();
//                    }
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
//                    }
//                });
//                alertdialog .setView(spinner);
//                alertdialog .setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                    // do something when the button is clicked
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        role_spinner=spinner.getSelectedItem().toString();
//                        bol_btn_speak = true;
//                    }
//                });
//                alertdialog .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    // do something when the button is clicked
//                    public void onClick(DialogInterface arg0, int arg1) {
//                    }
//                });
//                alertdialog .show();
//                Toast.makeText(EditDrama.this,"請選擇角色", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });




        //編輯完照片按下finishedit按鈕 將截圖傳回CreatDrama (從Firebase撈)
//        finishedit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
//                        }
//                    });
//
//
//                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), getBitmap(view), null,null));
//
//                    //將編輯好的照片截圖傳進Firebase
//                    fire_finishphoto = FirebaseStorage.getInstance().getReference().child(Student.Name + "/EditFinish/"
//                            + CreatDrama.spinner_drame_word + "/" + "edit_screen"+ CreatDrama.num + ".jpeg");
//                    fire_finishphoto.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                        @Override
//                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                            if(!task.isSuccessful()){
//                                throw  task.getException();
//                            }
//                            return fire_finishphoto.getDownloadUrl();
//                        }
//                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            Uri uri = task.getResult();
//                            download_url_editFinish = uri.toString();
//                        }
//                    }).continueWithTask(new Continuation<Uri, Task<Void>>() {
//                        @Override
//                        public Task<Void> then(@NonNull Task<Uri> task) throws Exception {
//
//                            fire_editdata = FirebaseDatabase.getInstance().getReference()
//                                    .child("學生"+Student.Name+"號").child(CreatDrama.spinner_drame_word).child(Integer.toString(CreatDrama.num));
//
//                            if(CreatDrama.edit == true){
//                                fire_editdata.child("playerA_record").setValue(record_download_url_a);
//                                fire_editdata.child("playerA_text").setValue(say1.getText().toString());
//                                fire_editdata.child("playerA_x").setValue(say1.getX());
//                                fire_editdata.child("playerA_y").setValue(say1.getY());
//                                fire_editdata.child("playerB_record").setValue(record_download_url_b);
//                                fire_editdata.child("playerB_text").setValue(say2.getText().toString());
//                                fire_editdata.child("playerB_x").setValue(say2.getX());
//                                fire_editdata.child("playerB_y").setValue(say2.getY());
//                                fire_editdata.child("player3_record").setValue(record_download_url_3);
//                                fire_editdata.child("player3_text").setValue(say3.getText().toString());
//                                fire_editdata.child("player3_x").setValue(say3.getX());
//                                fire_editdata.child("player3_y").setValue(say3.getY());
//                                fire_editdata.child("player4_record").setValue(record_download_url_4);
//                                fire_editdata.child("player4_text").setValue(say4.getText().toString());
//                                fire_editdata.child("player4_x").setValue(say4.getX());
//                                fire_editdata.child("player4_y").setValue(say4.getY());
//                                fire_editdata.child("player5_record").setValue(record_download_url_5);
//                                fire_editdata.child("player5_text").setValue(say5.getText().toString());
//                                fire_editdata.child("player5_x").setValue(say5.getX());
//                                fire_editdata.child("player5_y").setValue(say5.getY());
//                                fire_editdata.child("player6_record").setValue(record_download_url_6);
//                                fire_editdata.child("player6_text").setValue(say6.getText().toString());
//                                fire_editdata.child("player6_x").setValue(say6.getX());
//                                fire_editdata.child("player6_y").setValue(say6.getY());
//                                fire_editdata.child("bubble").setValue(bubble);
//                                fire_editdata.child("editFinishPhotoUri").setValue(download_url_editFinish).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//
//                                            Toast.makeText(EditDrama.this, "編輯資料上傳成功", Toast.LENGTH_SHORT).show();
//                                            CreatDrama.ccc();
//                                            //CreatDrama.num = 0;
//                                            new Thread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    try {
//                                                        Thread.sleep(2000);
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
//                                                            }
//                                                        });
//                                                    } catch (InterruptedException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                            }).start();
//                                            EditDrama.this.finish();
//                                        }else{
//                                            Toast.makeText(EditDrama.this, "失敗", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                            }else{
//                                //StoreTheEditData 傳入 編輯過的每一項資料
//                                storeTheEditData = new StoreTheEditData(CreatDrama.download_url.toString(), say1.getX(), say1.getY(), say2.getX(), say2.getY(),say3.getX(), say3.getY(), say4.getX(), say4.getY(),say5.getX(), say5.getY(), say6.getX(), say6.getY(),
//                                        say1.getText().toString(), say2.getText().toString(),record_download_url_a,record_download_url_b,say3.getText().toString(), say4.getText().toString(),record_download_url_3,record_download_url_4, say5.getText().toString(), say6.getText().toString(),record_download_url_5,record_download_url_6,
//                                        playtimeA, playtimeB, playtime3, playtime4, playtime5, playtime6,download_url_editFinish.toString());
//
//                                fire_editdata.setValue(storeTheEditData).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//
//                                            Toast.makeText(EditDrama.this, "編輯資料上傳成功", Toast.LENGTH_SHORT).show();
//                                            CreatDrama. ccc();
//                                            //CreatDrama.num = 0;
//                                            new Thread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    try {
//                                                        Thread.sleep(2000);
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
//                                                            }
//                                                        });
//                                                    } catch (InterruptedException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                            }).start();
//                                            EditDrama.this.finish();
//                                        }else{
//                                            Toast.makeText(EditDrama.this, "失敗", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                                return null;
//                            }
//                            return null;
//                        }
//                    });
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                CreatDrama.cantoload = true;
////                CreatDrama.ccc();
//
//            }
//        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v==say1 && say1.getText().toString() != ""){
            int c = say1.getText().toString().indexOf(":");
            edit_name = say1.getText().toString().substring(0,c);
            getMenuInflater().inflate(R.menu.example_menu_edit,menu);
        }else if(v==say2 && say2.getText().toString() != "") {
            int c = say2.getText().toString().indexOf(":");
            edit_name = say2.getText().toString().substring(0,c);
            getMenuInflater().inflate(R.menu.example_menu_edit, menu);
        }else if(v==say3 && say3.getText().toString() != ""){
            int c = say3.getText().toString().indexOf(":");
            edit_name = say3.getText().toString().substring(0,c);
            getMenuInflater().inflate(R.menu.example_menu_edit,menu);
        } else if(v==say4 && say4.getText().toString() != ""){
            int c = say4.getText().toString().indexOf(":");
            edit_name = say4.getText().toString().substring(0,c);
            getMenuInflater().inflate(R.menu.example_menu_edit,menu);
        }else {
            getMenuInflater().inflate(R.menu.example_menu,menu);
            option4 = menu.findItem(R.id.option_4);

            if (textOut_bol){
                option4.setTitle(textOut);
                menu.add(0,5,0,"自訂角色");
                option5 = menu.findItem(5);
                if (textOut_bol_5){
                    option5.setTitle(textOut_5);
                    menu.add(0,6,0,"自訂角色");
                    option6 = menu.findItem(6);
                    if(textOut_bol_6){
                        option6.setTitle(textOut_6);
                        menu.add(0,7,0,"自訂角色");
                        option7 = menu.findItem(7);
                        if(textOut_bol_7){
                            option7.setTitle(textOut_7);
                            menu.add(0,8,0,"自訂角色");
                            option8 = menu.findItem(8);
                            if(textOut_bol_8){
                                option8.setTitle(textOut_8);
                            }
                        }
                    }
                }
            }
        }



    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_1:
                Toast.makeText(this,"Op1 selected",Toast.LENGTH_SHORT).show();
                info= (String) item.getTitle();
                Log.v("info",info);
                switch (choose){
                    case 1:
                        info1=info;
                        info=null;
                        buildLinesA_Click();
                        break;
                    case 2:
                        info2=info;
                        info=null;
                        buildLinesB_Click();
                        break;
                    case 3:
                        info3=info;
                        info=null;
                        buildLines3_Click();
                        break;
                    case 4:
                        info4=info;
                        info=null;
                        buildLines4_Click();
                        break;
                }
                return true;
//                info= (String) item.getTitle();
//                Log.v("info",info);
//                switch (choose){
//                    case 1:
//                        new AlertDialog.Builder(EditDrama.this)
//                            .setIcon(R.drawable.ic_launcher)
//                            .setTitle("隱藏"+info1)
//                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    say1.setVisibility(INVISIBLE);
//                                }
//                            })
//                            .setNegativeButton("取消",null).create()
//                            .show();
//                        bubble=0;
//                        break;
//                    case 2:
//                        new AlertDialog.Builder(EditDrama.this)
//                                .setIcon(R.drawable.ic_launcher)
//                                .setTitle("隱藏"+info2)
//                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        say2.setVisibility(INVISIBLE);
//                                        say1.setVisibility(VISIBLE);
//                                    }
//                                })
//                                .setNegativeButton("取消",null).create()
//                                .show();
//                        bubble=1;
//                        break;
//                    case 3:
//                        new AlertDialog.Builder(EditDrama.this)
//                                .setIcon(R.drawable.ic_launcher)
//                                .setTitle("隱藏"+info3)
//                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        say3.setVisibility(INVISIBLE);
//                                        say2.setVisibility(VISIBLE);
//                                        say1.setVisibility(VISIBLE);
//                                    }
//                                })
//                                .setNegativeButton("取消",null).create()
//                                .show();
//                        bubble=2;
//                        break;
//                    case 4:
//                        new AlertDialog.Builder(EditDrama.this)
//                                .setIcon(R.drawable.ic_launcher)
//                                .setTitle("隱藏"+info4)
//                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        say4.setVisibility(INVISIBLE);
//                                        say3.setVisibility(VISIBLE);
//                                        say2.setVisibility(VISIBLE);
//                                        say1.setVisibility(VISIBLE);
//                                    }
//                                })
//                                .setNegativeButton("取消",null).create()
//                                .show();
//                        bubble=3;
//                        break;
//                }
            case R.id.option_2:
                Toast.makeText(this,"Op2 selected",Toast.LENGTH_SHORT).show();
                info= (String) item.getTitle();
                Log.v("info",info);
                switch (choose){
                    case 1:
                        info1=info;
                        info=null;
                        buildLinesA_Click();
                        break;
                    case 2:
                        info2=info;
                        info=null;
                        buildLinesB_Click();
                        break;
                    case 3:
                        info3=info;
                        info=null;
                        buildLines3_Click();
                        break;
                    case 4:
                        info4=info;
                        info=null;
                        buildLines4_Click();
                        break;
                }
                return true;
            case R.id.option_3:
                Toast.makeText(this,"Op3 selected",Toast.LENGTH_SHORT).show();
                info= (String) item.getTitle();
                Log.v("info",info);
                switch (choose){
                    case 1:
                        info1=info;
                        info=null;
                        buildLinesA_Click();
                        break;
                    case 2:
                        info2=info;
                        info=null;
                        buildLinesB_Click();
                        break;
                    case 3:
                        info3=info;
                        info=null;
                        buildLines3_Click();
                        break;
                    case 4:
                        info4=info;
                        info=null;
                        buildLines4_Click();
                        break;
                }
                return true;
            case R.id.option_4:
                Toast.makeText(this,"Op4 selected",Toast.LENGTH_SHORT).show();
                if(!textOut_bol) {
                    AlertDialog.Builder editDialog = new AlertDialog.Builder(EditDrama.this);
                    editDialog.setTitle("自訂角色");
                    editDialog.setIcon(R.drawable.ic_launcher);

                    final EditText editText = new EditText(EditDrama.this);
                    editDialog.setView(editText);

                    editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                            textOut = editText.getText().toString();
                            textOut_bol = true;
                            switch (choose) {
                                case 1:
                                    info1=textOut;
                                    buildLinesA_Click();
                                    break;
                                case 2:
                                    info2=textOut;
                                    buildLinesB_Click();
                                    break;
                                case 3:
                                    info3=textOut;
                                    buildLines3_Click();
                                    break;
                                case 4:
                                    info4=textOut;
                                    buildLines4_Click();
                                    break;
                            }
                            Log.v("textOut", textOut);
                        }
                    });
                    editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
                    editDialog.show();
                }else if(textOut_bol){
                    switch (choose) {
                        case 1:
                            info1=textOut;
                            buildLinesA_Click();
                            break;
                        case 2:
                            info2=textOut;
                            buildLinesB_Click();
                            break;
                        case 3:
                            info3=textOut;
                            buildLines3_Click();
                            break;
                        case 4:
                            info4=textOut;
                            buildLines4_Click();
                            break;
                    }
                }
                return true;
            case 5:
                if(!textOut_bol_5) {
                    AlertDialog.Builder editDialog = new AlertDialog.Builder(EditDrama.this);
                    editDialog.setTitle("自訂角色");
                    editDialog.setIcon(R.drawable.ic_launcher);

                    final EditText editText = new EditText(EditDrama.this);
                    editDialog.setView(editText);

                    editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                            textOut_5 = editText.getText().toString();
                            textOut_bol_5 = true;
                            switch (choose) {
                                case 1:
                                    info1=textOut_5;
                                    buildLinesA_Click();
                                    break;
                                case 2:
                                    info2=textOut_5;
                                    buildLinesB_Click();
                                    break;
                                case 3:
                                    info3=textOut_5;
                                    buildLines3_Click();
                                    break;
                                case 4:
                                    info4=textOut_5;
                                    buildLines4_Click();
                                    break;
                            }
                            Log.v("textOut_5", textOut_5);
                        }
                    });
                    editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
                    editDialog.show();
                }else if(textOut_bol_5){
                    switch (choose) {
                        case 1:
                            info1=textOut_5;
                            buildLinesA_Click();
                            break;
                        case 2:
                            info2=textOut_5;
                            buildLinesB_Click();
                            break;
                        case 3:
                            info3=textOut_5;
                            buildLines3_Click();
                            break;
                        case 4:
                            info4=textOut_5;
                            buildLines4_Click();
                            break;
                    }
                }
                return true;
            case 6:
                if(!textOut_bol_6) {
                    AlertDialog.Builder editDialog = new AlertDialog.Builder(EditDrama.this);
                    editDialog.setTitle("自訂角色");
                    editDialog.setIcon(R.drawable.ic_launcher);

                    final EditText editText = new EditText(EditDrama.this);
                    editDialog.setView(editText);

                    editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                            textOut_6 = editText.getText().toString();
                            textOut_bol_6 = true;
                            switch (choose) {
                                case 1:
                                    info1=textOut_6;
                                    buildLinesA_Click();
                                    break;
                                case 2:
                                    info2=textOut_6;
                                    buildLinesB_Click();
                                    break;
                                case 3:
                                    info3=textOut_6;
                                    buildLines3_Click();
                                    break;
                                case 4:
                                    info4=textOut_6;
                                    buildLines4_Click();
                                    break;
                            }
                            Log.v("textOut_6", textOut_6);
                        }
                    });
                    editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
                    editDialog.show();
                }else if(textOut_bol_6){
                    switch (choose) {
                        case 1:
                            info1=textOut_6;
                            buildLinesA_Click();
                            break;
                        case 2:
                            info2=textOut_6;
                            buildLinesB_Click();
                            break;
                        case 3:
                            info3=textOut_6;
                            buildLines3_Click();
                            break;
                        case 4:
                            info4=textOut_6;
                            buildLines4_Click();
                            break;
                    }
                }
                return true;
            case 7:
                if(!textOut_bol_7) {
                    AlertDialog.Builder editDialog = new AlertDialog.Builder(EditDrama.this);
                    editDialog.setTitle("自訂角色");
                    editDialog.setIcon(R.drawable.ic_launcher);

                    final EditText editText = new EditText(EditDrama.this);
                    editDialog.setView(editText);

                    editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                            textOut_7 = editText.getText().toString();
                            textOut_bol_7 = true;
                            switch (choose) {
                                case 1:
                                    info1=textOut_7;
                                    buildLinesA_Click();
                                    break;
                                case 2:
                                    info2=textOut_7;
                                    buildLinesB_Click();
                                    break;
                                case 3:
                                    info3=textOut_7;
                                    buildLines3_Click();
                                    break;
                                case 4:
                                    info4=textOut_7;
                                    buildLines4_Click();
                                    break;
                            }
                            Log.v("textOut_7", textOut_7);
                        }
                    });
                    editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
                    editDialog.show();
                }else if(textOut_bol_7){
                    switch (choose) {
                        case 1:
                            info1=textOut_7;
                            buildLinesA_Click();
                            break;
                        case 2:
                            info2=textOut_7;
                            buildLinesB_Click();
                            break;
                        case 3:
                            info3=textOut_7;
                            buildLines3_Click();
                            break;
                        case 4:
                            info4=textOut_7;
                            buildLines4_Click();
                            break;
                    }
                }
                return true;
            case 8:
                if(!textOut_bol_8) {
                    AlertDialog.Builder editDialog = new AlertDialog.Builder(EditDrama.this);
                    editDialog.setTitle("自訂角色");
                    editDialog.setIcon(R.drawable.ic_launcher);

                    final EditText editText = new EditText(EditDrama.this);
                    editDialog.setView(editText);

                    editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                            textOut_8 = editText.getText().toString();
                            textOut_bol_8 = true;
                            switch (choose) {
                                case 1:
                                    info1=textOut_8;
                                    buildLinesA_Click();
                                    break;
                                case 2:
                                    info2=textOut_8;
                                    buildLinesB_Click();
                                    break;
                                case 3:
                                    info3=textOut_8;
                                    buildLines3_Click();
                                    break;
                                case 4:
                                    info4=textOut_8;
                                    buildLines4_Click();
                                    break;
                            }
                            Log.v("textOut_8", textOut_8);
                        }
                    });
                    editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
                    editDialog.show();
                }else if(textOut_bol_8){
                    switch (choose) {
                        case 1:
                            info1=textOut_8;
                            buildLinesA_Click();
                            break;
                        case 2:
                            info2=textOut_8;
                            buildLinesB_Click();
                            break;
                        case 3:
                            info3=textOut_8;
                            buildLines3_Click();
                            break;
                        case 4:
                            info4=textOut_8;
                            buildLines4_Click();
                            break;
                    }
                }
                return true;

            case R.id.option_edit_0:
                AlertDialog.Builder editDialog = new AlertDialog.Builder(EditDrama.this);
                editDialog.setTitle("新增角色");
                editDialog.setIcon(R.drawable.ic_launcher);

                final EditText editText = new EditText(EditDrama.this);
                editDialog.setView(editText);
                editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        switch (choose) {
                            case 1:
                                info1 = editText.getText().toString();
                                info = null;
                                buildLinesA_Click();
                                break;
                            case 2:
                                info2 = editText.getText().toString();
                                info = null;
                                buildLinesB_Click();
                                break;
                            case 3:
                                info3 = editText.getText().toString();
                                info = null;
                                buildLines3_Click();
                                break;
                            case 4:
                                info4 = editText.getText().toString();
                                info = null;
                                buildLines4_Click();
                                break;
                        }
                    }
                });
                editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                editDialog.show();
                return  true;
            case R.id.option_edit_1:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditDrama.this);
                alertDialog.setTitle("編輯角色名稱");
                alertDialog.setIcon(R.drawable.ic_launcher);
                Spinner spinner = new Spinner(EditDrama.this);
                String[] s = {""};
                int x=0;

                for(int i=0;i<list.length;i++){
                    if (list[i] == null){
                        x=i;
                        break;
                    }else{
                        x=8;
                    }
                }

                final String[] list1 = new String[x];
                for(int i=0;i<list.length;i++){
                    if (list[i] != null){
                        list1[i]=list[i];
                    }else {
                        break;
                    }
                }

                ArrayAdapter<String> numberList=new ArrayAdapter<String>(EditDrama.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        list1);
                spinner.setAdapter(numberList);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinner_item=spinner.getSelectedItem().toString();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
                    }
                });
                alertDialog.setView(spinner);
                alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        s[0] = spinner_item;
                        switch (choose) {
                            case 1:
                                info1 = s[0];
                                info = null;
                                buildLinesA_Click();
                                break;
                            case 2:
                                info2 = s[0];
                                info = null;
                                buildLinesB_Click();
                                break;
                            case 3:
                                info3 = s[0];
                                info = null;
                                buildLines3_Click();
                                break;
                            case 4:
                                info4 = s[0];
                                info = null;
                                buildLines4_Click();
                                break;
                        }
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                alertDialog.show();
                return true;
            case R.id.option_edit_2:
                switch (choose) {
                    case 1:
                        info1=edit_name;
                        buildLinesA_Click();
                        break;
                    case 2:
                        info2=edit_name;
                        buildLinesB_Click();
                        break;
                    case 3:
                        info3=edit_name;
                        buildLines3_Click();
                        break;
                    case 4:
                        info4=edit_name;
                        buildLines4_Click();
                        break;
                }
                return true;

            case R.id.option_edit_3:
                switch (choose) {
                    case 1:
                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(Student.Name).child(spinner_drame_word+"/"+pathword_a);
                        playtimeA = playtimeA + 1;;
                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Link = uri.toString();
                                Toast.makeText(EditDrama.this, Link, Toast.LENGTH_SHORT).show();
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
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(EditDrama.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 2:
                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(Student.Name).child(spinner_drame_word+"/"+pathword_b);
                        playtimeB += 1;
                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Link = uri.toString();
                                Toast.makeText(EditDrama.this, Link, Toast.LENGTH_SHORT).show();
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
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(EditDrama.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 3:
                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(Student.Name).child(spinner_drame_word+"/"+pathword_3);
                        playtime3 += 1;
                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Link = uri.toString();
                                Toast.makeText(EditDrama.this, Link, Toast.LENGTH_SHORT).show();
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
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(EditDrama.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 4:
                        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(Student.Name).child(spinner_drame_word+"/"+pathword_4);
                        playtime4 += 1;
                        fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Link = uri.toString();
                                Toast.makeText(EditDrama.this, Link, Toast.LENGTH_SHORT).show();
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
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(EditDrama.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


    //複寫android本身的返回鍵
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

    }



    public String showTimeCount(long time) {
        String s = null;
        if(time<=59){
            s ="00:";
            return time<10 ? s+"0"+String.valueOf(time) : s+String.valueOf(time);
        }else{
            return (time%60 <10 ? s+"0"+String.valueOf(time) : s+String.valueOf(time))+":"+(time/60<10 ? s+"0"+String.valueOf(time) : s+String.valueOf(time));
        }
    }


    //pop up window 編輯劇本 編輯台詞及錄音 並可播放錄音確認錄成功與否
    //建立A角色台詞
    public void buildLinesA_Click(){
        findViewById(R.id.adddialog1).setEnabled(false);

        LayoutInflater inflater = LayoutInflater.from(EditDrama.this);

        final View v = inflater.inflate(R.layout.builddrama, null);
        editText1= (EditText) (v.findViewById(R.id.editText1));
        final Button record1 = (v.findViewById(R.id.recordplayer1));
        final Button stop1 = (v.findViewById(R.id.stopplayer1));
        play1 = (v.findViewById(R.id.playplayer1));
        final TextView showtime1 = (v.findViewById(R.id.show_time1));
        final Button btn_tts1=(v.findViewById(R.id.btn_tts1));

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timeCount++;
                String str="正在錄音..." + showTimeCount((long)timeCount);
                showtime1.setText(str);
                handler.postDelayed(this, 1000);
            }
        };

        final AlertDialog drama = new AlertDialog.Builder(EditDrama.this)
                .setTitle(info1)
                .setView(v)
                .setCancelable(false)
                .setPositiveButton("確定",null)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        findViewById(R.id.adddialog1).setEnabled(true);
                        dialog.dismiss();
                    }
                })
                .show();
//        if (re_a){
//            editText1.setText(re_a_line);
//        }else{
//            editText1.setText("");
//        }
        drama.getWindow().setDimAmount(0.05f);
        drama.show();



        if(say1.getText().toString() != ""){
            int c = say1.getText().toString().indexOf(":");
            String s = say1.getText().toString().substring(c+1,say1.length());
            editText1.setText(s);
        }

        try {
            btn_tts1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tts_count++;
                    TTS.speak(editText1.getText().toString());
                    if(!editText1.getText().toString().equals("")){
                        try {
                            //上傳點擊行為與時間點
                            String date = sdf.format(new java.util.Date());
                            DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                    .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("EditDrama").child("聆聽TTS");

                            fire_60sec_student_data.child(date).child("Dialog Text").setValue(editText1.getText().toString().trim());
                            fire_60sec_student_data.child(date).child("Dialog Number").setValue("Say1");
                            fire_60sec_student_data.child(date).child("Dialog Role Name").setValue(info1.trim());
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(EditDrama.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "請輸入句子", Toast.LENGTH_SHORT).show();
        }






//        if(CreatDrama.edit == true){
//            editText1.setText(re_a_line_word);}


        drama.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判斷台詞欄位是否為空
                        if(!(TextUtils.isEmpty(editText1.getText().toString()))){
                            SpannableStringBuilder spb = new SpannableStringBuilder();
                            String lyrics1 = info1.trim();
                            String lyrics2 = ":";
                            String lyrics3 = editText1.getText().toString().trim();
                            spb.append(lyrics1);
                            spb.setSpan(new ForegroundColorSpan(Color.RED),0,lyrics1.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spb.append(lyrics2);
                            spb.append(lyrics3);
                            say1.setText(spb);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) say1.getLayoutParams();
                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            if(params.width<100){
                                params.width = 400;
                            }
                            say1.setLayoutParams(params);
                            Drawable drawable = getResources().getDrawable(R.drawable.line1);
                            drawable.setBounds(0, 0, 40,40);
                            say1.setCompoundDrawables(drawable, null,null, null);
//                            re_a=true;
//                            re_a_line=editText1.getText().toString();
                            drama.dismiss();
                            Toast.makeText(getApplicationContext(), info1+"對話已建立", Toast.LENGTH_SHORT).show();
                            findViewById(R.id.adddialog1).setEnabled(true);
                            loadicon();
                        }else {
                            Toast.makeText(getApplicationContext(), "請輸入完整", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        //角色A對話錄音開始
        record1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPress == true){
                    recorder = new MediaRecorder();// new出MediaRecorder物件
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    // 設定MediaRecorder的音訊源為麥克風
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
                    // 設定MediaRecorder錄製的音訊格式
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    // 設定MediaRecorder錄製音訊的編碼為amr.

                    recorder.setOutputFile("/sdcard/" + pathword_a);

                    // 設定錄製好的音訊檔案儲存路徑
                    try {
                        recorder.prepare();// 準備錄製
                        recorder.start();// 開始錄製
                        Toast.makeText(EditDrama.this, "開始錄音"+info1+"對話", Toast.LENGTH_SHORT).show();

                        timeCount = 0;
                        handler.post(runnable);

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    isPress = false;
                }else{
                    Toast.makeText(EditDrama.this, "你已經正在錄音囉！", Toast.LENGTH_SHORT).show();
                }

            }
        });
        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(Student.Name).child(spinner_drame_word+"/"+pathword_a);
        //角色A對話錄音停止
        stop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPress == false){
                    stoptimeA++;
                    recorder.stop();// 停止燒錄
                    Toast.makeText(EditDrama.this, "停止錄音"+info1+"對話", Toast.LENGTH_SHORT).show();
                    handler.removeCallbacks(runnable);
                    showtime1.setText("停止錄音");


                    //上傳錄音檔至Firebase, 路徑為:"座號/劇本創作1", 檔名為"學生座號_劇本創作1_1_A.amr"
                    //並使用continueWithTask接回錄音檔url, 當按下播放鍵可以聽見錄音
                    //加上記錄聽錄音的次數???
                    Uri recoed_file_a = Uri.fromFile(new File("/sdcard/" + pathword_a));
                    fire_dramarecord.putFile(recoed_file_a).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw  task.getException();
                            }
                            return fire_dramarecord.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri uri = task.getResult();
                            record_download_url_a = uri.toString();
                            try {
                                //上傳點擊行為與時間點
                                String date = sdf.format(new java.util.Date());
                                DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                        .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("EditDrama").child("對話錄音");

                                if(!editText1.getText().toString().equals("")){
                                    fire_60sec_student_data.child(date).child("Dialog Text").setValue(editText1.getText().toString().trim());
                                }
                                fire_60sec_student_data.child(date).child("Dialog Number").setValue("Say1");
                                fire_60sec_student_data.child(date).child("Dialog Role Name").setValue(info1.trim());
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(EditDrama.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                            }
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

                    isPress = true;
                }else{
                    Toast.makeText(EditDrama.this, "要先錄音哦！", Toast.LENGTH_SHORT).show();
                }

            }
        });

        play1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playtimeA = playtimeA + 1;
                fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(EditDrama.this, Link, Toast.LENGTH_SHORT).show();
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
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EditDrama.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //建立B角色台詞
    public void buildLinesB_Click(){
        findViewById(R.id.adddialog2).setEnabled(false);

        LayoutInflater inflater = LayoutInflater.from(EditDrama.this);
        final View v = inflater.inflate(R.layout.builddrama2, null);
        editText2 = (EditText) (v.findViewById(R.id.editText2));
        final Button record2 = (v.findViewById(R.id.recordplayer2));
        final Button stop2 = (v.findViewById(R.id.stopplayer2));
        play2 = (v.findViewById(R.id.playplayer2));
        final TextView showtime2 = (v.findViewById(R.id.show_time2));
        final Button btn_tts2 = (v.findViewById(R.id.btn_tts2));

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timeCount++;
                String str="正在錄音..." + showTimeCount((long)timeCount);
                showtime2.setText(str);
                handler.postDelayed(this, 1000);
            }
        };


        final AlertDialog drama = new AlertDialog.Builder(EditDrama.this)
                .setTitle(info2)
                .setView(v)
                .setCancelable(false)
                .setPositiveButton("確定",null)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        findViewById(R.id.adddialog2).setEnabled(true);
                        dialog.dismiss();

                    }

                })
                .show();
//        if (re_b == true ){
//            editText2.setText(re_b_line.getText().toString());
//        }else{
//            editText2.setText("");
//        }
        drama.getWindow().setDimAmount(0.05f);
        drama.show();

        if(say2.getText().toString() != ""){
            int c = say2.getText().toString().indexOf(":");
            String s = say2.getText().toString().substring(c+1,say2.length());
            editText2.setText(s);
        }

        try {
            btn_tts2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tts_count++;
                    TTS.speak(editText2.getText().toString());
                    if(!editText2.getText().toString().equals("")){
                        try {
                            //上傳點擊行為與時間點
                            String date = sdf.format(new java.util.Date());
                            DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                    .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("EditDrama").child("聆聽TTS");

                            fire_60sec_student_data.child(date).child("Dialog Text").setValue(editText2.getText().toString().trim());
                            fire_60sec_student_data.child(date).child("Dialog Number").setValue("Say2");
                            fire_60sec_student_data.child(date).child("Dialog Role Name").setValue(info2.trim());
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(EditDrama.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "請輸入句子", Toast.LENGTH_SHORT).show();
        }


//        if(CreatDrama.edit == true){
//            editText2.setText(re_b_line_word);
//        }



        drama.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判斷台詞欄位是否為空
                        if(!(TextUtils.isEmpty(editText2.getText().toString()))){
                            SpannableStringBuilder spb = new SpannableStringBuilder();
                            String lyrics1 = info2.trim();
                            String lyrics2 = ":";
                            String lyrics3 = editText2.getText().toString().trim();
                            spb.append(lyrics1);
                            spb.setSpan(new ForegroundColorSpan(Color.RED),0,lyrics1.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spb.append(lyrics2);
                            spb.append(lyrics3);
                            say2.setText(spb);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) say2.getLayoutParams();
                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            if(params.width<100){
                                params.width = 400;
                            }
                            say2.setLayoutParams(params);
                            Drawable drawable2 = getResources().getDrawable(R.drawable.line2);
                            drawable2.setBounds(0, 0, 40,40);
                            say2.setCompoundDrawables(drawable2, null,null, null);
//                            re_b=true;
//                            re_b_line=editText2;
                            findViewById(R.id.adddialog2).setEnabled(true);
                            drama.dismiss();
                            Toast.makeText(getApplicationContext(), info2+"對話已建立", Toast.LENGTH_SHORT).show();
                            loadicon();
                        }else {
                            Toast.makeText(getApplicationContext(), "請輸入完整", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //角色A對話錄音開始
        record2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPress){
                    recorder = new MediaRecorder();// new出MediaRecorder物件
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    // 設定MediaRecorder的音訊源為麥克風
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
                    // 設定MediaRecorder錄製的音訊格式
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    // 設定MediaRecorder錄製音訊的編碼為amr.

                    recorder.setOutputFile("/sdcard/" + pathword_b);

                    // 設定錄製好的音訊檔案儲存路徑
                    try {
                        recorder.prepare();// 準備錄製
                        recorder.start();// 開始錄製
                        Toast.makeText(EditDrama.this, "開始錄音"+info2+"對話", Toast.LENGTH_SHORT).show();

                        timeCount = 0;
                        handler.post(runnable);

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    isPress = false;
                }else{
                    Toast.makeText(EditDrama.this, "你已經正在錄音囉！", Toast.LENGTH_SHORT).show();
                }

            }
        });
        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(Student.Name).child(spinner_drame_word+"/"+pathword_b);
        //角色A對話錄音停止
        stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPress == false){
                    stoptimeB++;
                    recorder.stop();// 停止燒錄
                    Toast.makeText(EditDrama.this, "停止錄音"+info2+"對話", Toast.LENGTH_SHORT).show();

                    handler.removeCallbacks(runnable);
                    showtime2.setText("停止錄音");

                    //上傳錄音檔至Firebase, 路徑為:"座號/劇本創作1", 檔名為"學生座號_劇本創作1_1_B.amr"
                    //並使用continueWithTask接回錄音檔url, 當按下播放鍵可以聽見錄音
                    //加上記錄聽錄音的次數???
                    Uri recoed_file_b = Uri.fromFile(new File("/sdcard/" + pathword_b));
                    fire_dramarecord.putFile(recoed_file_b).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw  task.getException();
                            }
                            return fire_dramarecord.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri uri = task.getResult();
                            record_download_url_b = uri.toString();
                            try {
                                //上傳點擊行為與時間點
                                String date = sdf.format(new java.util.Date());
                                DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                        .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("EditDrama").child("對話錄音");

                                if(!editText2.getText().toString().equals("")){
                                    fire_60sec_student_data.child(date).child("Dialog Text").setValue(editText2.getText().toString().trim());
                                }
                                fire_60sec_student_data.child(date).child("Dialog Number").setValue("Say2");
                                fire_60sec_student_data.child(date).child("Dialog Role Name").setValue(info2.trim());
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(EditDrama.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                            }
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

                    isPress = true;
                }else{
                    Toast.makeText(EditDrama.this, "要先錄音哦！", Toast.LENGTH_SHORT).show();
                }

            }
        });


        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playtimeB += 1;
                fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
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
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EditDrama.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void buildLines3_Click(){
//        findViewById(R.id.adddialog2).setEnabled(false);

        LayoutInflater inflater = LayoutInflater.from(EditDrama.this);
        final View v = inflater.inflate(R.layout.builddrama3, null);
        editText3 = (EditText) (v.findViewById(R.id.editText3));
        final Button record3 = (v.findViewById(R.id.recordplayer3));
        final Button stop3 = (v.findViewById(R.id.stopplayer3));
        play3 = (v.findViewById(R.id.playplayer3));
        final TextView showtime3 = (v.findViewById(R.id.show_time3));
        final Button btn_tts3 = (v.findViewById(R.id.btn_tts3));

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timeCount++;
                String str="正在錄音..." + showTimeCount((long)timeCount);
                showtime3.setText(str);
                handler.postDelayed(this, 1000);
            }
        };


        final AlertDialog drama = new AlertDialog.Builder(EditDrama.this)
                .setTitle(info3)
                .setView(v)
                .setCancelable(false)
                .setPositiveButton("確定",null)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        findViewById(R.id.adddialog2).setEnabled(true);
                        dialog.dismiss();

                    }

                })
                .show();
//        if (re_3 == true ){
//            editText3.setText(re_3_line.getText().toString());
//        }else{
//            editText3.setText("");
//        }
        drama.getWindow().setDimAmount(0.05f);
        drama.show();
        if(say3.getText().toString() != ""){
            int c = say3.getText().toString().indexOf(":");
            String s = say3.getText().toString().substring(c+1,say3.length());
            editText3.setText(s);
        }

        try {
            btn_tts3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tts_count++;
                    TTS.speak(editText3.getText().toString());
                    if(!editText3.getText().toString().equals("")){
                        try {
                            //上傳點擊行為與時間點
                            String date = sdf.format(new java.util.Date());
                            DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                    .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("EditDrama").child("聆聽TTS");

                            fire_60sec_student_data.child(date).child("Dialog Text").setValue(editText3.getText().toString().trim());
                            fire_60sec_student_data.child(date).child("Dialog Number").setValue("Say3");
                            fire_60sec_student_data.child(date).child("Dialog Role Name").setValue(info3.trim());
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(EditDrama.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "請輸入句子", Toast.LENGTH_SHORT).show();
        }


//        if(CreatDrama.edit == true){
//            editText3.setText(re_3_line_word);
//        }



        drama.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判斷台詞欄位是否為空
                        if(!(TextUtils.isEmpty(editText3.getText().toString()))){
                            SpannableStringBuilder spb = new SpannableStringBuilder();
                            String lyrics1 = info3.trim();
                            String lyrics2 = ":";
                            String lyrics3 = editText3.getText().toString().trim();
                            spb.append(lyrics1);
                            spb.setSpan(new ForegroundColorSpan(Color.RED),0,lyrics1.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spb.append(lyrics2);
                            spb.append(lyrics3);
                            say3.setText(spb);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) say3.getLayoutParams();
                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            if(params.width<100){
                                params.width = 400;
                            }
//                            re_3=true;
//                            re_3_line=editText3;
                            say3.setLayoutParams(params);
                            Drawable drawable3 = getResources().getDrawable(R.drawable.line3);
                            drawable3.setBounds(0, 0, 40,40);
                            say3.setCompoundDrawables(drawable3, null,null, null);
//                            findViewById(R.id.adddialog2).setEnabled(true);
                            drama.dismiss();
                            Toast.makeText(getApplicationContext(), info3+"對話已建立", Toast.LENGTH_SHORT).show();
                            loadicon();
                        }else {
                            Toast.makeText(getApplicationContext(), "請輸入完整", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //角色A對話錄音開始
        record3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPress == true){
                    recorder = new MediaRecorder();// new出MediaRecorder物件
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    // 設定MediaRecorder的音訊源為麥克風
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
                    // 設定MediaRecorder錄製的音訊格式
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    // 設定MediaRecorder錄製音訊的編碼為amr.

                    recorder.setOutputFile("/sdcard/" + pathword_3);

                    // 設定錄製好的音訊檔案儲存路徑
                    try {
                        recorder.prepare();// 準備錄製
                        recorder.start();// 開始錄製
                        Toast.makeText(EditDrama.this, "開始錄音"+info3+"對話", Toast.LENGTH_SHORT).show();

                        timeCount = 0;
                        handler.post(runnable);

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    isPress = false;
                }else{
                    Toast.makeText(EditDrama.this, "你已經正在錄音囉！", Toast.LENGTH_SHORT).show();
                }

            }
        });
        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(Student.Name).child(spinner_drame_word+"/"+pathword_3);
        //角色A對話錄音停止
        stop3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPress == false){
                    stoptime3++;
                    recorder.stop();// 停止燒錄
                    Toast.makeText(EditDrama.this, "停止錄音"+info3+"對話", Toast.LENGTH_SHORT).show();

                    handler.removeCallbacks(runnable);
                    showtime3.setText("停止錄音");

                    //上傳錄音檔至Firebase, 路徑為:"座號/劇本創作1", 檔名為"學生座號_劇本創作1_1_B.amr"
                    //並使用continueWithTask接回錄音檔url, 當按下播放鍵可以聽見錄音
                    //加上記錄聽錄音的次數???
                    Uri recoed_file_3 = Uri.fromFile(new File("/sdcard/" + pathword_3));
                    fire_dramarecord.putFile(recoed_file_3).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw  task.getException();
                            }
                            return fire_dramarecord.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri uri = task.getResult();
                            record_download_url_3 = uri.toString();
                            try {
                                //上傳點擊行為與時間點
                                String date = sdf.format(new java.util.Date());
                                DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                        .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("EditDrama").child("對話錄音");

                                if(!editText3.getText().toString().equals("")){
                                    fire_60sec_student_data.child(date).child("Dialog Text").setValue(editText3.getText().toString().trim());
                                }
                                fire_60sec_student_data.child(date).child("Dialog Number").setValue("Say3");
                                fire_60sec_student_data.child(date).child("Dialog Role Name").setValue(info3.trim());
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(EditDrama.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                            }
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

                    isPress = true;
                }else{
                    Toast.makeText(EditDrama.this, "要先錄音哦！", Toast.LENGTH_SHORT).show();
                }

            }
        });

        play3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playtime3 += 1;
                fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
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
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EditDrama.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void buildLines4_Click(){
//        findViewById(R.id.adddialog2).setEnabled(false);

        LayoutInflater inflater = LayoutInflater.from(EditDrama.this);
        final View v = inflater.inflate(R.layout.builddrama4, null);
        editText4 = (EditText) (v.findViewById(R.id.editText4));
        final Button record4 = (v.findViewById(R.id.recordplayer4));
        final Button stop4 = (v.findViewById(R.id.stopplayer4));
        play4 = (v.findViewById(R.id.playplayer4));
        final TextView showtime4 = (v.findViewById(R.id.show_time4));
        final Button btn_tts4 = (v.findViewById(R.id.btn_tts4));

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timeCount++;
                String str="正在錄音..." + showTimeCount((long)timeCount);
                showtime4.setText(str);
                handler.postDelayed(this, 1000);
            }
        };


        final AlertDialog drama = new AlertDialog.Builder(EditDrama.this)
                .setTitle(info4)
                .setView(v)
                .setCancelable(false)
                .setPositiveButton("確定",null)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        findViewById(R.id.adddialog2).setEnabled(true);
                        dialog.dismiss();

                    }

                })
                .show();
//        if (re_4 == true ){
//            editText4.setText(re_4_line.getText().toString());
//        }else{
//            editText4.setText("");
//        }
        drama.getWindow().setDimAmount(0.05f);
        drama.show();

        if(say4.getText().toString() != ""){
            int c = say4.getText().toString().indexOf(":");
            String s = say4.getText().toString().substring(c+1,say4.length());
            editText4.setText(s);
        }

        try {
            btn_tts4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tts_count++;
                    TTS.speak(editText4.getText().toString());
                    if(!editText4.getText().toString().equals("")){
                        try {
                            //上傳點擊行為與時間點
                            String date = sdf.format(new java.util.Date());
                            DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                    .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("EditDrama").child("聆聽TTS");

                            fire_60sec_student_data.child(date).child("Dialog Text").setValue(editText4.getText().toString().trim());
                            fire_60sec_student_data.child(date).child("Dialog Number").setValue("Say4");
                            fire_60sec_student_data.child(date).child("Dialog Role Name").setValue(info4.trim());
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(EditDrama.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "請輸入句子", Toast.LENGTH_SHORT).show();
        }


//        if(CreatDrama.edit == true){
//            editText4.setText(re_4_line_word);
//        }



        drama.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判斷台詞欄位是否為空
                        if(!(TextUtils.isEmpty(editText4.getText().toString()))){
                            SpannableStringBuilder spb = new SpannableStringBuilder();
                            String lyrics1 = info4.trim();
                            String lyrics2 = ":";
                            String lyrics3 = editText4.getText().toString().trim();
                            spb.append(lyrics1);
                            spb.setSpan(new ForegroundColorSpan(Color.RED),0,lyrics1.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spb.append(lyrics2);
                            spb.append(lyrics3);
                            say4.setText(spb);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) say4.getLayoutParams();
                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            if(params.width<100){
                                params.width = 400;
                            }
                            say4.setLayoutParams(params);
                            Drawable drawable4 = getResources().getDrawable(R.drawable.line4);
                            drawable4.setBounds(0, 0, 40,40);
                            say4.setCompoundDrawables(drawable4, null,null, null);
//                            re_4=true;
//                            re_4_line=editText4;
//                            findViewById(R.id.adddialog2).setEnabled(true);
                            drama.dismiss();
                            loadicon();
                            Toast.makeText(getApplicationContext(), info4+"對話已建立", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "請輸入完整", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //角色A對話錄音開始
        record4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPress == true){
                    recorder = new MediaRecorder();// new出MediaRecorder物件
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    // 設定MediaRecorder的音訊源為麥克風
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
                    // 設定MediaRecorder錄製的音訊格式
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    // 設定MediaRecorder錄製音訊的編碼為amr.

                    recorder.setOutputFile("/sdcard/" + pathword_4);

                    // 設定錄製好的音訊檔案儲存路徑
                    try {
                        recorder.prepare();// 準備錄製
                        recorder.start();// 開始錄製
                        Toast.makeText(EditDrama.this, "開始錄音"+info4+"對話", Toast.LENGTH_SHORT).show();

                        timeCount = 0;
                        handler.post(runnable);

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    isPress = false;
                }else{
                    Toast.makeText(EditDrama.this, "你已經正在錄音囉！", Toast.LENGTH_SHORT).show();
                }

            }
        });
        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(Student.Name).child(spinner_drame_word+"/"+pathword_4);
        //角色A對話錄音停止
        stop4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPress == false){
                    stoptime4++;
                    recorder.stop();// 停止燒錄
                    Toast.makeText(EditDrama.this, "停止錄音"+info4+"對話", Toast.LENGTH_SHORT).show();

                    handler.removeCallbacks(runnable);
                    showtime4.setText("停止錄音");

                    //上傳錄音檔至Firebase, 路徑為:"座號/劇本創作1", 檔名為"學生座號_劇本創作1_1_B.amr"
                    //並使用continueWithTask接回錄音檔url, 當按下播放鍵可以聽見錄音
                    //加上記錄聽錄音的次數???
                    Uri recoed_file_4 = Uri.fromFile(new File("/sdcard/" + pathword_4));
                    fire_dramarecord.putFile(recoed_file_4).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw  task.getException();
                            }
                            return fire_dramarecord.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri uri = task.getResult();
                            record_download_url_4 = uri.toString();
                            try {
                                //上傳點擊行為與時間點
                                String date = sdf.format(new java.util.Date());
                                DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                        .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("EditDrama").child("對話錄音");

                                if(!editText4.getText().toString().equals("")){
                                    fire_60sec_student_data.child(date).child("Dialog Text").setValue(editText4.getText().toString().trim());
                                }
                                fire_60sec_student_data.child(date).child("Dialog Number").setValue("Say4");
                                fire_60sec_student_data.child(date).child("Dialog Role Name").setValue(info4.trim());
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(EditDrama.this, "上傳時間點擊紀錄失敗", Toast.LENGTH_SHORT).show();
                            }
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

                    isPress = true;
                }else{
                    Toast.makeText(EditDrama.this, "要先錄音哦！", Toast.LENGTH_SHORT).show();
                }

            }
        });


        play4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playtime4 += 1;
                fire_dramarecord.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
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
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EditDrama.this, "檔案載入失敗", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    //系統截圖(擷取 editimage 的 View)
    private Bitmap getBitmap(View view) throws Exception {
        View screenView = getWindow().getDecorView();
        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache(); //獲取螢幕整張圖片
        Bitmap bitmap = screenView.getDrawingCache();
        if (bitmap != null) {
            //需要擷取的長和寬
            int outWidth = editimage.getWidth();
            int outHeight = editimage.getHeight(); //獲取需要截圖部分的在螢幕上的座標(view的左上角座標)
            int[] viewLocationArray = new int[2];
            editimage.getLocationOnScreen(viewLocationArray); //從螢幕整張圖片中擷取指定區域
            bitmap = Bitmap.createBitmap(bitmap,
                    viewLocationArray[0], viewLocationArray[1],
                    outWidth, outHeight);
        }
        return bitmap;
    }

    //拖曳 A 對話框
    private View.OnTouchListener say1Listener = new View.OnTouchListener() {
        private float x, y;    // 原本圖片存在的X,Y軸位置
        private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
        private int lastX, lastY; //记录移动的最后的位置
        private boolean first_touch = true;


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Log.e("View", v.toString());
//            if(say1.getText().toString() == null){
//                info1=null;
//            }

           gestureDetector.onTouchEvent(event);
            switch (event.getAction()) {          //判斷觸控的動作
                case MotionEvent.ACTION_DOWN:// 按下圖片時
                    x = event.getX();                  //觸控的X軸位置
                    y = event.getY();                  //觸控的Y軸位置
                    registerForContextMenu(say1);
//                    if (say1.getVisibility() != VISIBLE) {
//                        say1.setX(x);
//                        say1.setY(y);
//                    }
                return false;
                case MotionEvent.ACTION_MOVE:// 移動圖片時
                    // getX()：是獲取當前控件(View)的座標
                    //getRawX()：是獲取相對顯示螢幕左上角的座標
                    mx = (int) (event.getRawX() - x);
                    my = (int) (event.getRawY() - y);
                    setRelativeViewLocation(say1, mx, my, mx + v.getWidth(), my + v.getHeight());
//                    lastX = (int) event.getRawX();
//                    lastY = (int) event.getRawY();
                    first_touch = false;
                    return false;
                case MotionEvent.ACTION_UP:
                    choose = 1;
                    x = event.getX();
                    y = event.getY();
                    Log.e("say1手置放開的座標", String.valueOf(x) + " " + String.valueOf(y));
////                    String ResoltTTS =say1.getText().toString();
////                    TTS.speak(ResoltTTS);
////                    if (info1 == null){
////                        registerForContextMenu(say1);
////                        new Thread() {
////                            public void run() {
////                                try {
////                                    Thread.sleep(5000);
////                                } catch (InterruptedException e) {
////                                    // TODO Auto-generated catch block
////                                    e.printStackTrace();
////                                }
////                                info1=info;
////                                info=null;
////                            }
////                        }.start();
////                    }
////                    else if(say1.getText().toString() != null)
////                    {
////                        buildLinesA_Click();
////                    }
////                    else {
////                        buildLinesA_Click();
////                    }
                    break;
////                case MotionEvent.ACTION_POINTER_DOWN:
////                    return false;
////                case MotionEvent.ACTION_POINTER_UP:
////                    new AlertDialog.Builder(EditDrama.this)
////                            .setIcon(R.drawable.ic_launcher)
////                            .setTitle("隱藏"+info1)
////                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
////                                @Override
////                                public void onClick(DialogInterface dialog, int which) {
////                                    say1.setVisibility(INVISIBLE);
////                                }
////                            })
////                            .setNegativeButton("取消",null).create()
////                            .show();
////                    bubble=0;
////                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            Log.e("address", String.valueOf(mx) + "~~" + String.valueOf(my)); // 記錄目前位置
            return gestureDetector.onTouchEvent(event);
        }
        final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//                Log.v("Double", "double");
//                new AlertDialog.Builder(EditDrama.this)
//                        .setIcon(R.drawable.ic_launcher)
//                        .setTitle("隱藏"+info1)
//                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                say1.setVisibility(INVISIBLE);
//                            }
//                        })
//                        .setNegativeButton("取消",null).create()
//                        .show();
//                bubble=0;
//                return super.onDoubleTap(e);
//            }
            //            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                registerForContextMenu(say1);
//                return super.onFling(e1, e2, velocityX, velocityY);
//            }

//            @Override
//            public void onLongPress(MotionEvent event) {
//                info1=info;
//                info=null;
//                buildLinesA_Click();
//                super.onLongPress(event);
//            }
        });
        private void setRelativeViewLocation(View view, int left, int top, int right, int bottom) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(right - left, bottom - top);
            Log.e("第一個泡泡框移動", params.width + "~~" +Integer.toString(right) + Integer.toString(left));
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params.setMargins(left, top, 0, 0);
            view.setLayoutParams(params);
        }
    };


    //拖曳 B 對話框
    private View.OnTouchListener say2Listener = new View.OnTouchListener() {
        private float x, y;    // 原本圖片存在的X,Y軸位置
        private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
        private boolean first_touch = true;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Log.e("View", v.toString());
//            if(say2.getText().toString() == null){
//                info2=null;
//            }
            gestureDetector.onTouchEvent(event);
            switch (event.getAction()) {          //判斷觸控的動作

                case MotionEvent.ACTION_DOWN:// 按下圖片時
                    x = event.getX();                  //觸控的X軸位置
                    y = event.getY();                  //觸控的Y軸位置
//                    v.bringToFront();
                    registerForContextMenu(say2);
//                    if (first_touch){
//                        say2.setX(x);
//                        say2.setY(y);
//                    }
                    return false;
                case MotionEvent.ACTION_MOVE:// 移動圖片時
                    //getX()：是獲取當前控件(View)的座標
                    //getRawX()：是獲取相對顯示螢幕左上角的座標
                    mx = (int) (event.getRawX() - x);
                    my = (int) (event.getRawY() - y);
                    setRelativeViewLocation(say2, mx, my, mx + v.getWidth(), my + v.getHeight());
//                    first_touch=false;
                    return false;
                case MotionEvent.ACTION_UP:
                    choose=2;
//                    String SpeechTTS =say2.getText().toString();
//                    TTS.speak(SpeechTTS);
//                    if (info2 == null ){
//                        registerForContextMenu(say2);
//                        new Thread() {
//                            public void run() {
//                                try {
//                                    Thread.sleep(5000);
//                                } catch (InterruptedException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//                                info2=info;
//                                info=null;
//                            }
//                        }.start();
//                        touch=false;
//                    }
////                    else if(say2.getText().toString() != null)
////                    {
////                        buildLinesB_Click();
////                    }
////                    else {
////                        buildLinesB_Click();
////                    }
                   break;
//                case MotionEvent.ACTION_POINTER_DOWN:
//                    return false;
//                case MotionEvent.ACTION_POINTER_UP:
//                    new AlertDialog.Builder(EditDrama.this)
//                                .setIcon(R.drawable.ic_launcher)
//                                .setTitle("隱藏"+info2)
//                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        say2.setVisibility(INVISIBLE);
//                                        say1.setVisibility(VISIBLE);
//                                    }
//                                })
//                                .setNegativeButton("取消",null).create()
//                                .show();
//                        bubble=1;
//                        break;
                case MotionEvent.ACTION_CANCEL:
                break;
            }
            Log.e("address", String.valueOf(mx) + "~~" + String.valueOf(my)); // 記錄目前位置
            return gestureDetector.onTouchEvent(event);
        }
    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {

//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            registerForContextMenu(say2);
//            return super.onFling(e1, e2, velocityX, velocityY);
//        }

//        @Override
//        public void onLongPress(MotionEvent event) {
//            info2=info;
//            info=null;
//            buildLinesB_Click();
//            super.onLongPress(event);
//        }

    });

        private void setRelativeViewLocation(View view, int left, int top, int right, int bottom) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(right - left, bottom - top);
            Log.e("第二個泡泡框移動", params.width + "~~" +Integer.toString(right) + Integer.toString(left));
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params.setMargins(left, top, 0, 0);
            view.setLayoutParams(params);
        }

    };

    //拖曳 3 對話框
    private View.OnTouchListener say3Listener = new View.OnTouchListener() {
        private float x, y;    // 原本圖片存在的X,Y軸位置
        private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
        private boolean first_touch = true;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Log.e("View", v.toString());
//            if(say2.getText().toString() == null){
//                info2=null;
//            }
            gestureDetector.onTouchEvent(event);
            switch (event.getAction()) {          //判斷觸控的動作

                case MotionEvent.ACTION_DOWN:// 按下圖片時
                    x = event.getX();                  //觸控的X軸位置
                    y = event.getY();                  //觸控的Y軸位置
//                    v.bringToFront();
                    registerForContextMenu(say3);
//                    if (first_touch){
//                        say3.setX(x);
//                        say3.setY(y);
//                    }
                    return false;
                case MotionEvent.ACTION_MOVE:// 移動圖片時
                    //getX()：是獲取當前控件(View)的座標
                    //getRawX()：是獲取相對顯示螢幕左上角的座標
                    mx = (int) (event.getRawX() - x);
                    my = (int) (event.getRawY() - y);
                    setRelativeViewLocation(say3, mx, my, mx + v.getWidth(), my + v.getHeight());
//                    first_touch=false;
                    return false;
                case MotionEvent.ACTION_UP:
                    choose=3;
//                    String SpeechTTS =say2.getText().toString();
//                    TTS.speak(SpeechTTS);
//                    if (info2 == null ){
//                        registerForContextMenu(say2);
//                        new Thread() {
//                            public void run() {
//                                try {
//                                    Thread.sleep(5000);
//                                } catch (InterruptedException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//                                info2=info;
//                                info=null;
//                            }
//                        }.start();
//                        touch=false;
//                    }
////                    else if(say2.getText().toString() != null)
////                    {
////                        buildLinesB_Click();
////                    }
////                    else {
////                        buildLinesB_Click();
////                    }
                    break;
//                case MotionEvent.ACTION_POINTER_DOWN:
//                    return false;
//                case MotionEvent.ACTION_POINTER_UP:
//                    new AlertDialog.Builder(EditDrama.this)
//                            .setIcon(R.drawable.ic_launcher)
//                            .setTitle("隱藏"+info3)
//                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    say3.setVisibility(INVISIBLE);
//                                    say2.setVisibility(VISIBLE);
//                                    say1.setVisibility(VISIBLE);
//                                }
//                            })
//                            .setNegativeButton("取消",null).create()
//                            .show();
//                    bubble=2;
//                    break;
            }
            Log.e("address", String.valueOf(mx) + "~~" + String.valueOf(my)); // 記錄目前位置
            return gestureDetector.onTouchEvent(event);
        }
        final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                registerForContextMenu(say3);
//                return super.onFling(e1, e2, velocityX, velocityY);
//            }
//
//            @Override
//            public void onLongPress(MotionEvent event) {
//                info3=info;
//                info=null;
//                buildLines3_Click();
//                super.onLongPress(event);
//            }

        });

        private void setRelativeViewLocation(View view, int left, int top, int right, int bottom) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(right - left, bottom - top);
            Log.e("第三個泡泡框移動", params.width + "~~" +Integer.toString(right) + Integer.toString(left));
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params.setMargins(left, top, 0, 0);
            view.setLayoutParams(params);
        }

    };

    //拖曳 4 對話框
    private View.OnTouchListener say4Listener = new View.OnTouchListener() {
        private float x, y;    // 原本圖片存在的X,Y軸位置
        private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
        private boolean first_touch = true;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Log.e("View", v.toString());
//            if(say2.getText().toString() == null){
//                info2=null;
//            }
            gestureDetector.onTouchEvent(event);
            switch (event.getAction()) {          //判斷觸控的動作

                case MotionEvent.ACTION_DOWN:// 按下圖片時
                    x = event.getX();                  //觸控的X軸位置
                    y = event.getY();                  //觸控的Y軸位置
//                    v.bringToFront();
                    registerForContextMenu(say4);
//                    if (first_touch){
//                        say4.setX(x);
//                        say4.setY(y);
//                    }
                    return false;
                case MotionEvent.ACTION_MOVE:// 移動圖片時
                    //getX()：是獲取當前控件(View)的座標
                    //getRawX()：是獲取相對顯示螢幕左上角的座標
                    mx = (int) (event.getRawX() - x);
                    my = (int) (event.getRawY() - y);
                    setRelativeViewLocation(say4, mx, my, mx + v.getWidth(), my + v.getHeight());
//                    first_touch=false;
                    return false;
                case MotionEvent.ACTION_UP:
                    choose=4;
//                    String SpeechTTS =say2.getText().toString();
//                    TTS.speak(SpeechTTS);
//                    if (info2 == null ){
//                        registerForContextMenu(say2);
//                        new Thread() {
//                            public void run() {
//                                try {
//                                    Thread.sleep(5000);
//                                } catch (InterruptedException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//                                info2=info;
//                                info=null;
//                            }
//                        }.start();
//                        touch=false;
//                    }
////                    else if(say2.getText().toString() != null)
////                    {
////                        buildLinesB_Click();
////                    }
////                    else {
////                        buildLinesB_Click();
////                    }
                    break;
//                case MotionEvent.ACTION_POINTER_DOWN:
//                    return false;
//                case MotionEvent.ACTION_POINTER_UP:
//                    new AlertDialog.Builder(EditDrama.this)
//                            .setIcon(R.drawable.ic_launcher)
//                            .setTitle("隱藏"+info4)
//                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    say4.setVisibility(INVISIBLE);
//                                    say3.setVisibility(VISIBLE);
//                                    say2.setVisibility(VISIBLE);
//                                    say1.setVisibility(VISIBLE);
//                                }
//                            })
//                            .setNegativeButton("取消",null).create()
//                            .show();
//                    bubble=3;
//                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            Log.e("address", String.valueOf(mx) + "~~" + String.valueOf(my)); // 記錄目前位置
            return gestureDetector.onTouchEvent(event);
        }
        final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                registerForContextMenu(say4);
//                return super.onFling(e1, e2, velocityX, velocityY);
//            }
//
//            @Override
//            public void onLongPress(MotionEvent event) {
//                info4=info;
//                info=null;
//                buildLines4_Click();
//                super.onLongPress(event);
//            }

        });

        private void setRelativeViewLocation(View view, int left, int top, int right, int bottom) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(right - left, bottom - top);
            Log.e("第四個泡泡框移動", params.width + "~~" +Integer.toString(right) + Integer.toString(left));
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params.setMargins(left, top, 0, 0);
            view.setLayoutParams(params);
        }

    };

    private View.OnTouchListener imageTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                lastTouchDownXY[0] = event.getX();
                lastTouchDownXY[1] = event.getY();
            }
            return false;

        }
    };

//    private View.OnTouchListener imageTouch = new View.OnTouchListener() {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getAction()) {          //判斷觸控的動作
//                case MotionEvent.ACTION_DOWN:// 按下圖片時
//                    x_touch = event.getX();                  //觸控的X軸位置
//                    y_touch = event.getY();                  //觸控的Y軸位置
//                    break;
//                case MotionEvent.ACTION_MOVE:// 移動圖片時
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//                    break;
//            }
//            return false;
//        }
//    };






    //判断Activity是否Destroy
    public static boolean isDestroy(Activity activity) {
        if (activity == null || activity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }

//    private ArrayList<StoreTheEditData> list_editdata;
//    String push_key = fire_editdata.getKey();
    public void returnbackdata(){
        Toast.makeText(this, spinner_drame_word + " & 情境" +CreatDrama.num+"進行編輯" , Toast.LENGTH_SHORT).show();

        fire_editdata = FirebaseDatabase.getInstance().getReference()
                .child("學生"+Student.Name+"號").child(spinner_drame_word).child(String.valueOf(CreatDrama.num));

        fire_editdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StoreTheEditData storeTheEditData = dataSnapshot.getValue(StoreTheEditData.class);
                if(!isDestroy((Activity)editimage.getContext())){
                    Glide
                            .with(editimage.getContext())
                            .load(dataSnapshot.child("originalPhotoUri").getValue().toString())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(editimage);
                }
                playtimeA= storeTheEditData.getPlayerA_playtime();
                playtimeB= storeTheEditData.getPlayerB_playtime();
                playtime3= storeTheEditData.getPlayer3_playtime();
                playtime4= storeTheEditData.getPlayer4_playtime();

                say1_X = storeTheEditData.getPlayerA_x();
                say1_Y = storeTheEditData.getPlayerA_y();
                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp1.setMargins(Math.round(say1_X),Math.round(say1_Y), 0, 0);
                say1.setLayoutParams(lp1);

                say2_X = storeTheEditData.getPlayerB_x();
                say2_Y = storeTheEditData.getPlayerB_y();
                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp2.setMargins(Math.round(say2_X),Math.round(say2_Y), 0, 0);
                say2.setLayoutParams(lp2);

                say3_X = storeTheEditData.getPlayer3_x();
                say3_Y = storeTheEditData.getPlayer3_y();
                RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp3.setMargins(Math.round(say3_X),Math.round(say3_Y), 0, 0);
                say3.setLayoutParams(lp3);

                say4_X = storeTheEditData.getPlayer4_x();
                say4_Y = storeTheEditData.getPlayer4_y();
                RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp4.setMargins(Math.round(say4_X),Math.round(say4_Y), 0, 0);
                say4.setLayoutParams(lp4);

                if(storeTheEditData.getPlayerA_text().equals("") == false){
                    say1.setVisibility(VISIBLE);
                    SpannableStringBuilder spb = new SpannableStringBuilder();
                    int i=storeTheEditData.getPlayerA_text().indexOf(":");
                    String lyrics1 = storeTheEditData.getPlayerA_text().substring(0,i).trim();
                    String lyrics2 = ":";
                    String lyrics3 = storeTheEditData.getPlayerA_text().substring(i+1).trim();
                    spb.append(lyrics1);
                    spb.setSpan(new ForegroundColorSpan(Color.RED),0,lyrics1.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spb.append(lyrics2);
                    spb.append(lyrics3);
                    say1.setText(spb);
                    Drawable drawable = getResources().getDrawable(R.drawable.line1);
                    drawable.setBounds(0, 0, 40,40);
                    say1.setCompoundDrawables(drawable, null,null, null);
                }
                if(storeTheEditData.getPlayerB_text().equals("") == false){
                    say2.setVisibility(VISIBLE);
                    SpannableStringBuilder spb = new SpannableStringBuilder();
                    int i=storeTheEditData.getPlayerB_text().indexOf(":");
                    String lyrics1 = storeTheEditData.getPlayerB_text().substring(0,i).trim();
                    String lyrics2 = ":";
                    String lyrics3 = storeTheEditData.getPlayerB_text().substring(i+1).trim();
                    spb.append(lyrics1);
                    spb.setSpan(new ForegroundColorSpan(Color.RED),0,lyrics1.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spb.append(lyrics2);
                    spb.append(lyrics3);
                    say2.setText(spb);
                    Drawable drawable2 = getResources().getDrawable(R.drawable.line2);
                    drawable2.setBounds(0, 0, 40,40);
                    say2.setCompoundDrawables(drawable2, null,null, null);
                }
                if(storeTheEditData.getPlayer3_text().equals("") == false){
                    say3.setVisibility(VISIBLE);
                    SpannableStringBuilder spb = new SpannableStringBuilder();
                    int i=storeTheEditData.getPlayer3_text().indexOf(":");
                    String lyrics1 = storeTheEditData.getPlayer3_text().substring(0,i).trim();
                    String lyrics2 = ":";
                    String lyrics3 = storeTheEditData.getPlayer3_text().substring(i+1).trim();
                    spb.append(lyrics1);
                    spb.setSpan(new ForegroundColorSpan(Color.RED),0,lyrics1.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spb.append(lyrics2);
                    spb.append(lyrics3);
                    say3.setText(spb);
                    Drawable drawable3 = getResources().getDrawable(R.drawable.line3);
                    drawable3.setBounds(0, 0, 40,40);
                    say3.setCompoundDrawables(drawable3, null,null, null);
                }
                if(storeTheEditData.getPlayer4_text().equals("") == false){
                    say4.setVisibility(VISIBLE);
                    SpannableStringBuilder spb = new SpannableStringBuilder();
                    int i=storeTheEditData.getPlayer4_text().indexOf(":");
                    String lyrics1 = storeTheEditData.getPlayer4_text().substring(0,i).trim();
                    String lyrics2 = ":";
                    String lyrics3 = storeTheEditData.getPlayer4_text().substring(i+1).trim();
                    spb.append(lyrics1);
                    spb.setSpan(new ForegroundColorSpan(Color.RED),0,lyrics1.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spb.append(lyrics2);
                    spb.append(lyrics3);
                    say4.setText(spb);
                    Drawable drawable4 = getResources().getDrawable(R.drawable.line4);
                    drawable4.setBounds(0, 0, 40,40);
                    say4.setCompoundDrawables(drawable4, null,null, null);
                }

                loadicon();

//                say1.setVisibility(VISIBLE);
//                say2.setVisibility(VISIBLE);
//                say3.setVisibility(VISIBLE);
//                say4.setVisibility(VISIBLE);
//                say1.setText(storeTheEditData.getPlayerA_text());
//                say2.setText(storeTheEditData.getPlayerB_text());
//                say3.setText(storeTheEditData.getPlayer3_text());
//                say4.setText(storeTheEditData.getPlayer4_text());



                re_a_line_word = storeTheEditData.getPlayerA_text();
                re_b_line_word = storeTheEditData.getPlayerB_text();
                re_3_line_word = storeTheEditData.getPlayer3_text();
                re_4_line_word = storeTheEditData.getPlayer4_text();


                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditDrama.this, "Oops.....Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

        return;

    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Toast.makeText(EditDrama.this,"back",Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(EditDrama.this)
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle("編輯完畢?")
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.loadingPanel).setVisibility(VISIBLE);
                                    }
                                });

                                
                                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), getBitmap(view), null,null));

                                //將編輯好的照片截圖傳進Firebase
                                fire_finishphoto = FirebaseStorage.getInstance().getReference().child(Student.Name + "/EditFinish/"
                                        + spinner_drame_word + "/" + "edit_screen"+ CreatDrama.num + ".jpeg");
                                fire_finishphoto.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if(!task.isSuccessful()){
                                            throw  task.getException();
                                        }
                                        return fire_finishphoto.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        Uri uri = task.getResult();
                                        download_url_editFinish = uri.toString();
                                    }
                                }).continueWithTask(new Continuation<Uri, Task<Void>>() {
                                    @Override
                                    public Task<Void> then(@NonNull Task<Uri> task) throws Exception {

                                        fire_editdata = FirebaseDatabase.getInstance().getReference()
                                                .child("學生"+Student.Name+"號").child(spinner_drame_word).child(Integer.toString(CreatDrama.num));

                                        fire_contextmenu = FirebaseDatabase.getInstance().getReference()
                                                .child("學生"+Student.Name+"號").child(spinner_drame_word);

                                        try {
                                            fire_contextmenu.child("contextmenu").child("4").setValue(textOut);
                                            fire_contextmenu.child("contextmenu").child("5").setValue(textOut_5);
                                            fire_contextmenu.child("contextmenu").child("6").setValue(textOut_6);
                                            fire_contextmenu.child("contextmenu").child("7").setValue(textOut_7);
                                            fire_contextmenu.child("contextmenu").child("8").setValue(textOut_8);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        if(CreatDrama.edit){
                                            fire_editdata.child("playerA_playtime").setValue(playtimeA);
                                            fire_editdata.child("playerA_record").setValue(record_download_url_a);
                                            fire_editdata.child("playerA_text").setValue(say1.getText().toString());
                                            fire_editdata.child("playerA_x").setValue(say1.getX());
                                            fire_editdata.child("playerA_y").setValue(say1.getY());
                                            fire_editdata.child("playerB_playtime").setValue(playtimeB);
                                            fire_editdata.child("playerB_record").setValue(record_download_url_b);
                                            fire_editdata.child("playerB_text").setValue(say2.getText().toString());
                                            fire_editdata.child("playerB_x").setValue(say2.getX());
                                            fire_editdata.child("playerB_y").setValue(say2.getY());
                                            fire_editdata.child("player3_playtime").setValue(playtime3);
                                            fire_editdata.child("player3_record").setValue(record_download_url_3);
                                            fire_editdata.child("player3_text").setValue(say3.getText().toString());
                                            fire_editdata.child("player3_x").setValue(say3.getX());
                                            fire_editdata.child("player3_y").setValue(say3.getY());
                                            fire_editdata.child("player4_playtime").setValue(playtime4);
                                            fire_editdata.child("player4_record").setValue(record_download_url_4);
                                            fire_editdata.child("player4_text").setValue(say4.getText().toString());
                                            fire_editdata.child("player4_x").setValue(say4.getX());
                                            fire_editdata.child("player4_y").setValue(say4.getY());
                                            fire_editdata.child("player5_playtime").setValue(playtime5);
                                            fire_editdata.child("player5_record").setValue(record_download_url_5);
                                            fire_editdata.child("player5_text").setValue(say5.getText().toString());
                                            fire_editdata.child("player5_x").setValue(say5.getX());
                                            fire_editdata.child("player5_y").setValue(say5.getY());
                                            fire_editdata.child("player6_playtime").setValue(playtime6);
                                            fire_editdata.child("player6_record").setValue(record_download_url_6);
                                            fire_editdata.child("player6_text").setValue(say6.getText().toString());
                                            fire_editdata.child("player6_x").setValue(say6.getX());
                                            fire_editdata.child("player6_y").setValue(say6.getY());
                                            fire_editdata.child("editFinishPhotoUri").setValue(download_url_editFinish).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        Toast.makeText(EditDrama.this, "編輯資料上傳成功", Toast.LENGTH_SHORT).show();
                                                        CreatDrama.ccc();
                                                        //CreatDrama.num = 0;
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    Thread.sleep(2000);
                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                                                        }
                                                                    });
                                                                } catch (InterruptedException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }).start();
                                                        EditDrama.this.finish();
                                                    }else{
                                                        Toast.makeText(EditDrama.this, "失敗", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            //StoreTheEditData 傳入 編輯過的每一項資料
                                            storeTheEditData = new StoreTheEditData(CreatDrama.download_url.toString(), say1.getX(), say1.getY(), say2.getX(), say2.getY(),say3.getX(), say3.getY(), say4.getX(), say4.getY(),say5.getX(), say5.getY(), say6.getX(), say6.getY(),
                                                    say1.getText().toString(), say2.getText().toString(),record_download_url_a,record_download_url_b,say3.getText().toString(), say4.getText().toString(),record_download_url_3,record_download_url_4, say5.getText().toString(), say6.getText().toString(),record_download_url_5,record_download_url_6,
                                                    playtimeA, playtimeB, playtime3, playtime4, playtime5, playtime6,download_url_editFinish.toString());

                                            fire_editdata.setValue(storeTheEditData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        Toast.makeText(EditDrama.this, "編輯資料上傳成功", Toast.LENGTH_SHORT).show();
                                                        CreatDrama. ccc();
                                                        //CreatDrama.num = 0;
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    Thread.sleep(2000);
                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                                                        }
                                                                    });
                                                                } catch (InterruptedException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }).start();
                                                        EditDrama.this.finish();
                                                    }else{
                                                        Toast.makeText(EditDrama.this, "失敗", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            return null;
                                        }
                                        return null;
                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CreatDrama.cantoload = true;
                        }
                    })
                    .setNegativeButton("取消",null).create()
                    .show();
            writeInfo(Student.Name+"號學生創建劇本行為紀錄","創建劇本");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void registerForContextMenu(View view) {
        if (view == say1  ) {
            view.setOnCreateContextMenuListener(this);
        }
        if (view == say2 ) {
            view.setOnCreateContextMenuListener(this);
        }
        if (view == say3 ) {
            view.setOnCreateContextMenuListener(this);
        }
        if (view == say4 ) {
            view.setOnCreateContextMenuListener(this);
        }
    }

    private void loadicon(){
        final String pathword_a = Student.Name + "_" + spinner_drame_word + "_" + CreatDrama.num + "_" + "A.amr";
        final String pathword_b = Student.Name + "_" + spinner_drame_word + "_" + CreatDrama.num + "_" + "B.amr";
        final String pathword_3 = Student.Name + "_" + spinner_drame_word + "_" + CreatDrama.num + "_" + "3.amr";
        final String pathword_4 = Student.Name + "_" + spinner_drame_word + "_" + CreatDrama.num + "_" + "4.amr";
        final File f_a=new File("/sdcard/" + pathword_a);
        final File f_b=new File("/sdcard/" + pathword_b);
        final File f_3=new File("/sdcard/" + pathword_3);
        final File f_4=new File("/sdcard/" + pathword_4);
        if(f_a.exists())
        {
            Drawable drawable = getResources().getDrawable(R.drawable.line1);
            Drawable drawable1 = getResources().getDrawable(R.drawable.advertising);
            drawable.setBounds(0, 0, 40,40);
            drawable1.setBounds(0, 0, 40,40);
            say1.setCompoundDrawables(drawable, null,drawable1, null);
        }
        if(f_b.exists())
        {
            Drawable drawable = getResources().getDrawable(R.drawable.line2);
            Drawable drawable1 = getResources().getDrawable(R.drawable.advertising);
            drawable.setBounds(0, 0, 40,40);
            drawable1.setBounds(0, 0, 40,40);
            say2.setCompoundDrawables(drawable, null,drawable1, null);
        }
        if(f_3.exists())
        {
            Drawable drawable = getResources().getDrawable(R.drawable.line3);
            Drawable drawable1 = getResources().getDrawable(R.drawable.advertising);
            drawable.setBounds(0, 0, 40,40);
            drawable1.setBounds(0, 0, 40,40);
            say3.setCompoundDrawables(drawable, null,drawable1, null);
        }
        if(f_4.exists())
        {
            Drawable drawable = getResources().getDrawable(R.drawable.line4);
            Drawable drawable1 = getResources().getDrawable(R.drawable.advertising);
            drawable.setBounds(0, 0, 40,40);
            drawable1.setBounds(0, 0, 40,40);
            say4.setCompoundDrawables(drawable, null,drawable1, null);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        TTS.stop();
    }

    private void BringImagePicker() {
        CropImage.activity().
                setGuidelines(CropImageView.Guidelines.ON).
                setAspectRatio(12,10).
                start(EditDrama.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Intent intent = new Intent();
                Uri imageUri=result.getUri();
                editimage.setImageURI(imageUri);
                intent.putExtra("ori_uri",imageUri.toString());//可放所有基本類別
                StorageReference fire_originalphoto = FirebaseStorage.getInstance().getReference()
                        .child(Student.Name + "/OriginalPhoto/" + CreatDrama.spinner_drame_word + "/" + "ori_screen" + CreatDrama.num + ".jpeg");

                fire_originalphoto.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw  task.getException();
                        }
                        return fire_originalphoto.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri uri = task.getResult();
                        CreatDrama.download_url = uri.toString();
                    }
                }).continueWithTask(new Continuation<Uri, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Uri> task) throws Exception {
                        return null;
                    }
                });
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
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
            bw.write("聆聽A次數:"+playtimeA+"\n");
            bw.write("聆聽B次數:"+playtimeB+"\n");
            bw.write("聆聽C次數:"+playtime3+"\n");
            bw.write("聆聽D次數:"+playtime4+"\n");
            bw.write("A錄音次數:"+stoptimeA+"\n");
            bw.write("B錄音次數:"+stoptimeB+"\n");
            bw.write("C錄音次數:"+stoptime3+"\n");
            bw.write("D錄音次數:"+stoptime4+"\n");
            bw.write("TTS次數:"+tts_count+"\n");

            try {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference db = database.getReference().child("學生"+Student.Name+"號").child("Student data").child("EditDrama");
                DatabaseReference db2 = database.getReference().child("Other").child("stu_data").child("EditDrama");
                db.child("listenA").setValue(playtimeA);
                db.child("listenB").setValue(playtimeB);
                db.child("listenC").setValue(playtime3);
                db.child("listenD").setValue(playtime4);
                db.child("recordA").setValue(stoptimeA);
                db.child("recordB").setValue(stoptimeB);
                db.child("recordC").setValue(stoptime3);
                db.child("recordD").setValue(stoptime4);
                db.child("TTS").setValue(tts_count);
                db2.child(Student.Name).setValue(tts_count+stoptimeA+stoptimeB+stoptime3+stoptime4);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(EditDrama.this, "儲存錯誤", Toast.LENGTH_SHORT).show();
            }

            bw.close();
            Toast.makeText(EditDrama.this, "行為紀錄紀錄成功", Toast.LENGTH_SHORT).show();
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
            stoptimeA= Integer.parseInt(response.substring(response.indexOf("A錄音次數:")+6,response.indexOf("B錄音次數:")-1));
            stoptimeB= Integer.parseInt(response.substring(response.indexOf("B錄音次數:")+6,response.indexOf("C錄音次數:")-1));
            stoptime3= Integer.parseInt(response.substring(response.indexOf("C錄音次數:")+6,response.indexOf("D錄音次數:")-1));
            stoptime4= Integer.parseInt(response.substring(response.indexOf("D錄音次數:")+6,response.indexOf("TTS次數:")-1));
            tts_count= Integer.parseInt(response.substring(response.indexOf("TTS次數:")+6,response.length()-1));
            br.close();
            Toast.makeText(EditDrama.this, "行為紀錄讀取成功", Toast.LENGTH_SHORT).show();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

}
