package com.naer.pdfreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lib.kingja.switchbutton.SwitchMultiButton;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.naer.pdfreader.DialogActivity.TAG;

public class CreatDrama extends Activity {
    private static int i;
    //????????????????????????
    private Spinner dramanumber;  //????????????
    public static ImageView drama1; //?????????????????????
    public static ImageView drama2;
    public static ImageView drama3;
    public static ImageView drama4;
    public static ImageView drama5; //?????????????????????
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
    private static Button button1;
    private static Button button2;
    private static Button button3;
    private static Button button4;
    private Button cancel;
    private Button finish_and_return;
    private int scoreview;
    private static int x;


    //????????????????????????????????????
    public static String spinner_drame_word = "Drama1";

    //creatdrama Button?????? ???????????????????????????
    public static int num;

    //?????????????????????Firebase
    private StorageReference fire_originalphoto;

    //??????????????????????????? ???????????????????????????
    private DatabaseReference fire_re_data_exist;

    //?????????????????????Firebase
    private StorageReference fire_final_four_frame;

    public static String download_url = "";

    public static boolean edit = false;
    public static String image_name;

//    private String b ="";

    public static boolean cantoload = false;
    private View view;
    private static boolean drama1_edit = false;
    private static boolean drama2_edit = false;
    private static boolean drama3_edit = false;
    private static boolean drama4_edit = false;
    private static boolean drama5_edit = false;
    private static boolean drama6_edit = false;
    private static boolean drama7_edit = false;
    private static boolean drama8_edit = false;
    private SwitchMultiButton  switchmultibutton;

    private final String[] Drama = new String[4];
    private DatabaseReference fire_dramaname;
    private Button delete;
    private ProgressDialog pDialog;
    private SimpleDateFormat sdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//???????????????
        this.getWindow().setFlags(0x80000000,0x80000000);
        setContentView(R.layout.activity_creat_drama);


        dramanumber = findViewById(R.id.dramanumber);
        drama1 = findViewById(R.id.drama1);
        drama2 = findViewById(R.id.drama2);
        drama3 = findViewById(R.id.drama3);
        drama4 = findViewById(R.id.drama4);
        creat1 = findViewById(R.id.creat1);
        creat2 = findViewById(R.id.creat2);
        creat3 = findViewById(R.id.creat3);
        creat4 = findViewById(R.id.creat4);
        drama5 = findViewById(R.id.drama5);
        drama6 = findViewById(R.id.drama6);
        drama7 = findViewById(R.id.drama7);
        drama8 = findViewById(R.id.drama8);
        creat5 = findViewById(R.id.creat5);
        creat6 = findViewById(R.id.creat6);
        creat7 = findViewById(R.id.creat7);
        creat8 = findViewById(R.id.creat8);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        cancel = findViewById(R.id.cancel);
        delete = findViewById(R.id.delete);
        finish_and_return = findViewById(R.id.finish_and_return);
        switchmultibutton = findViewById(R.id.switchmultibutton);

        //??????????????????(??????)
        sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

        pDialog = new ProgressDialog(CreatDrama.this);
        pDialog.setTitle("????????????");
        pDialog.setMessage("?????????... \n??????????????????");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        creat5.setVisibility(INVISIBLE);
        button1.setVisibility(INVISIBLE);



        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                creat5.setVisibility(VISIBLE);
                creat6.setVisibility(VISIBLE);
                button2.setVisibility(VISIBLE);
                drama5.setVisibility(VISIBLE);
                button1.setVisibility(INVISIBLE);
                scoreview=1;

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                creat6.setVisibility(VISIBLE);
                creat7.setVisibility(VISIBLE);
                button3.setVisibility(VISIBLE);
                drama6.setVisibility(VISIBLE);
                button2.setVisibility(INVISIBLE);
                scoreview=2;
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                creat7.setVisibility(VISIBLE);
                creat8.setVisibility(VISIBLE);
                button4.setVisibility(VISIBLE);
                drama7.setVisibility(VISIBLE);
                button3.setVisibility(INVISIBLE);
                scoreview=3;
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                creat8.setVisibility(VISIBLE);
                drama8.setVisibility(VISIBLE);
                button4.setVisibility(INVISIBLE);
                scoreview=4;
            }
        });


        try {

            ccc();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Drama[0] = "Drama1";
        Drama[1] = "Drama2";
        Drama[2] = "Drama3";
        Drama[3] = "Drama4";

        try {
            DatabaseReference fire_load_dramaname = FirebaseDatabase.getInstance().getReference();
            fire_load_dramaname.child("??????" + Student.Name + "???").child("DramaName")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int i = 0;
                            String[] sssttt = new String[(int) dataSnapshot.getChildrenCount()];
                            for (DataSnapshot each : dataSnapshot.getChildren()) {
                                sssttt[i] = each.getValue().toString();
                                Drama[i]=sssttt[i];
                                Log.v("??????st", Drama[i]);
                                Log.v("??????i", String.valueOf(i));
                                i++;
                            }
                            switchmultibutton
                                    .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ","                   "+Drama[3]+"                   ");
                            pDialog.dismiss();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

        switchmultibutton.setOnSwitchListener(onSwitchListener);
//        try {
//            final FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference db = database.getReference().child("??????" + Student.Name + "???").child("DramaName");
//            db.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    Log.v("getChildrenCount user", String.valueOf(dataSnapshot.getChildrenCount()));
//                    String test = String.valueOf(dataSnapshot.getValue());
//                    test = test.substring(0, test.length() - 1);
//                    Log.v("getValue()", test);
//                    String[] user_array = test.split(",");
//                    for (int i = 0; i < user_array.length-1; i++) {
//                        Drama[i]=user_array[i];
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }catch (Exception e) {
//            e.printStackTrace();
//        }


        //--------??????????????????----------
        final String[] list = {"?????????", "Drama_1", "Drama_2", "Drama_3"};

        ArrayAdapter<String> numberList=new ArrayAdapter<String>(CreatDrama.this,
                android.R.layout.simple_spinner_dropdown_item,
                list);
        dramanumber.setAdapter(numberList);
        dramanumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//????????????Spinner????????????
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_drame_word = dramanumber.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//????????????
                spinner_drame_word = dramanumber.getSelectedItem().toString();
            }
        });


//        switch (i){
//            case 4:
//                drama5.setVisibility(INVISIBLE);
//                drama6.setVisibility(INVISIBLE);
//                drama7.setVisibility(INVISIBLE);
//                drama8.setVisibility(INVISIBLE);
//                break;
//            case 5:
//                drama6.setVisibility(INVISIBLE);
//                drama7.setVisibility(INVISIBLE);
//                drama8.setVisibility(INVISIBLE);
//                break;
//            case 6:
//                drama7.setVisibility(INVISIBLE);
//                drama8.setVisibility(INVISIBLE);
//                break;
//            case 7:
//                drama8.setVisibility(INVISIBLE);
//                break;
//        }
//        drama1.setOnClickListener(re_edit1);
//        drama2.setOnClickListener(re_edit2);
//        drama3.setOnClickListener(re_edit3);
//        drama4.setOnClickListener(re_edit4);


        //--------????????????1234 Button???????????????????????????????????????----------

        drama1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.equals("?????????", spinner_drame_word) && !drama1_edit || x<1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //?????????
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //??????
                        BringImagePicker();
                    }
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama1");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    num = 1;
                    edit = false;
                    drama1_edit = true;

                } else if(drama1_edit && x>=1){
                    re_edit1();
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama1");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(CreatDrama.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                }

            }
        });


        drama2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("?????????", spinner_drame_word) && !drama2_edit || x<2){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //?????????
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //??????
                        BringImagePicker();
                    }
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama2");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    num = 2;
                    edit = false;
                    drama2_edit = true;
                }else if(drama2_edit && x>=2){
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama2");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    re_edit2();
                } else{
                    Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drama3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("?????????", spinner_drame_word) && !drama3_edit || x<3){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //?????????
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //??????
                        BringImagePicker();
                    }
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama3");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    num = 3;
                    edit = false;
                    drama3_edit = true;
                }else if(drama3_edit && x>=3){
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama3");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    re_edit3();
                } else{
                    Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drama4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("?????????", spinner_drame_word) && !drama4_edit || x<4){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //?????????
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                            drama4_edit = true;
                        }
                    } else {  //??????
                        BringImagePicker();
                        drama4_edit = true;
                    }
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama4");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    num = 4;
                    edit = false;
                }else if(drama4_edit && x>=4){
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama4");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    re_edit4();
                }else{
                    Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drama5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("?????????", spinner_drame_word) && !drama5_edit || x<5){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //?????????
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //??????
                        BringImagePicker();
                    }
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama5");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }

                    num = 5;
                    edit = false;
                    drama5_edit = true;
                }else if(drama5_edit && x>=5){
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama5");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    re_edit5();
                }else{
                    Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drama5.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                Toast.makeText(CreatDrama.this, "???????????????", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(CreatDrama.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("????????????5")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                drama5.setVisibility(INVISIBLE);
                                creat5.setVisibility(VISIBLE);
                                creat6.setVisibility(INVISIBLE);
                                creat7.setVisibility(INVISIBLE);
                                creat8.setVisibility(INVISIBLE);
                                button1.setVisibility(VISIBLE);
                                button2.setVisibility(INVISIBLE);
                                button3.setVisibility(INVISIBLE);
                                button4.setVisibility(INVISIBLE);
                            }
                        })
                        .setNegativeButton("??????",null).create()
                        .show();
                return true;
            }
        });


        drama6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("?????????", spinner_drame_word) && !drama6_edit || x<6){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //?????????
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //??????
                        BringImagePicker();
                    }
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama6");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }

                    num = 6;
                    edit = false;
                    drama6_edit = true;
                }else if(drama6_edit && x>=6){

                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama6");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    re_edit6();
                } else{
                    Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drama6.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                Toast.makeText(CreatDrama.this, "???????????????", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(CreatDrama.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("????????????6")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                drama6.setVisibility(INVISIBLE);
                                creat5.setVisibility(VISIBLE);
                                creat6.setVisibility(VISIBLE);
                                creat7.setVisibility(INVISIBLE);
                                creat8.setVisibility(INVISIBLE);
                                button1.setVisibility(INVISIBLE);
                                button2.setVisibility(VISIBLE);
                                button3.setVisibility(INVISIBLE);
                                button4.setVisibility(INVISIBLE);
                            }
                        })
                        .setNegativeButton("??????",null).create()
                        .show();
                return true;
            }
        });

        drama7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("?????????", spinner_drame_word) && !drama7_edit || x<7){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //?????????
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //??????
                        BringImagePicker();
                    }
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama7");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    num = 7;
                    edit = false;
                    drama7_edit = true;
                }else if(drama7_edit && x>=7){
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama7");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    re_edit7();
                } else{
                    Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drama7.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                Toast.makeText(CreatDrama.this, "???????????????", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(CreatDrama.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("????????????7")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                drama7.setVisibility(INVISIBLE);
                                creat5.setVisibility(VISIBLE);
                                creat6.setVisibility(VISIBLE);
                                creat7.setVisibility(VISIBLE);
                                creat8.setVisibility(INVISIBLE);
                                button1.setVisibility(INVISIBLE);
                                button2.setVisibility(INVISIBLE);
                                button3.setVisibility(VISIBLE);
                                button4.setVisibility(INVISIBLE);
                            }
                        })
                        .setNegativeButton("??????",null).create()
                        .show();
                return true;
            }
        });

        drama8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("?????????", spinner_drame_word) && !drama8_edit || x<8){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //?????????
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //??????
                        BringImagePicker();
                    }
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama8");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }

                    num = 8;
                    edit = false;
                    drama8_edit = true;
                }else if(drama8_edit && x>=8){
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("EditDrama").child("????????????");
                        fire_60sec_student_data.child(date).setValue("Drama8");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreatDrama.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    re_edit8();
                } else{
                    Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drama8.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                Toast.makeText(CreatDrama.this, "???????????????", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(CreatDrama.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("????????????8")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                drama8.setVisibility(INVISIBLE);
                                creat5.setVisibility(VISIBLE);
                                creat6.setVisibility(VISIBLE);
                                creat7.setVisibility(VISIBLE);
                                creat8.setVisibility(VISIBLE);
                                button1.setVisibility(INVISIBLE);
                                button2.setVisibility(INVISIBLE);
                                button3.setVisibility(INVISIBLE);
                                button4.setVisibility(VISIBLE);
                            }
                        })
                        .setNegativeButton("??????",null).create()
                        .show();
                return true;
            }
        });



        finish_and_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fire_final_four_frame = FirebaseStorage.getInstance().getReference().child(Student.Name).child("/Four-frame/").child(spinner_drame_word);
                try {
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), getBitmap(view), null,null));
                    fire_final_four_frame.putFile(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CreatDrama.this.finish();
            }
        });


    }



    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Toast.makeText(CreatDrama.this,"back",Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(CreatDrama.this)
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle("????????????")
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fire_final_four_frame = FirebaseStorage.getInstance().getReference().child(Student.Name).child("/Four-frame/").child(spinner_drame_word);
                            fire_dramaname = FirebaseDatabase.getInstance().getReference();
                            try {
                                Map<String,Object> childUpdates=new HashMap<>();
                                childUpdates.put("1",Drama[0]);
                                childUpdates.put("2",Drama[1]);
                                childUpdates.put("3",Drama[2]);
                                childUpdates.put("4",Drama[3]);
                                fire_dramaname.child("??????"+Student.Name+"???").child("DramaName").updateChildren(childUpdates);
                                for(int i=0;i<4;i++){
                                    if(Drama[i].equals("Playground")){
                                        fire_dramaname.child("Other").child("stu_drama").child("Playground").child(Student.Name).setValue("Playground");
                                    }else if(Drama[i].equals("Classroom")){
                                        fire_dramaname.child("Other").child("stu_drama").child("Classroom").child(Student.Name).setValue("Classroom");
                                    }else if(Drama[i].equals("Home")){
                                        fire_dramaname.child("Other").child("stu_drama").child("Home").child(Student.Name).setValue("Home");
                                    }else if(Drama[i].equals("Supermarket")){
                                        fire_dramaname.child("Other").child("stu_drama").child("Supermarket").child(Student.Name).setValue("Supermarket");
                                    }else if(Drama[i].equals("Park")){
                                        fire_dramaname.child("Other").child("stu_drama").child("Park").child(Student.Name).setValue("Park");
                                    } else if(Drama[i].equals("Other")){
                                        fire_dramaname.child("Other").child("stu_drama").child("Other").child(Student.Name).setValue("Other");
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), getBitmap(view), null,null));
                                fire_final_four_frame.putFile(uri);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            CreatDrama.this.finish();
                        }
                    })
                    .setNegativeButton("??????",null).create()
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    private SwitchMultiButton.OnSwitchListener onSwitchListener = new SwitchMultiButton.OnSwitchListener() {
        @Override
        public void onSwitch(int position, String tabText) {
            switch (position){
                case 0:
                    Log.v("tabtext",tabText);
//                    if(tabText == "                    Drama1                   "){
//                        AlertDialog.Builder editDialog = new AlertDialog.Builder(CreatDrama.this);
//                        editDialog.setTitle("??????????????????");
//                        editDialog.setIcon(R.drawable.ic_launcher);
//
//                        final EditText editText = new EditText(CreatDrama.this);
//                        editDialog.setView(editText);
//                        editDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
//                            // do something when the button is clicked
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                Drama[0] = editText.getText().toString();
//                                switchmultibutton
//                                        .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ");
//                            }
//                        });
//                        editDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
//                            // do something when the button is clicked
//                            public void onClick(DialogInterface arg0, int arg1) {
//                            }
//                        });
//                        editDialog.show();
//                    }

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder editDialog = new AlertDialog.Builder(CreatDrama.this);
                            editDialog.setTitle("??????????????????");
                            editDialog.setIcon(R.drawable.ic_launcher);

                            final EditText editText = new EditText(CreatDrama.this);
                            editDialog.setView(editText);
                            editDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                // do something when the button is clicked
                                public void onClick(DialogInterface arg0, int arg1) {
                                    String drama_name = editText.getText().toString().trim();
                                    drama_name = drama_name.toUpperCase().charAt(0)+drama_name.substring(1);
                                    Drama[0] = drama_name;
                                    switchmultibutton
                                            .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ","                   "+Drama[3]+"                   ");
                                }
                            });
                            editDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                // do something when the button is clicked
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                            editDialog.show();
                        }
                    });

                    spinner_drame_word="Drama1";
                    break;
                case 1:
//                    if(tabText == "                    Drama2                   "){
//                        AlertDialog.Builder editDialog = new AlertDialog.Builder(CreatDrama.this);
//                        editDialog.setTitle("??????????????????");
//                        editDialog.setIcon(R.drawable.ic_launcher);
//
//                        final EditText editText = new EditText(CreatDrama.this);
//                        editDialog.setView(editText);
//                        editDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
//                            // do something when the button is clicked
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                Drama[1] = editText.getText().toString();
//                                switchmultibutton
//                                        .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ");
//                            }
//                        });
//                        editDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
//                            // do something when the button is clicked
//                            public void onClick(DialogInterface arg0, int arg1) {
//                            }
//                        });
//                        editDialog.show();
//                    }
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder editDialog = new AlertDialog.Builder(CreatDrama.this);
                                editDialog.setTitle("??????????????????");
                                editDialog.setIcon(R.drawable.ic_launcher);

                                final EditText editText = new EditText(CreatDrama.this);
                                editDialog.setView(editText);
                                editDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        String drama_name = editText.getText().toString().trim();
                                        drama_name = drama_name.toUpperCase().charAt(0)+drama_name.substring(1);
                                        Drama[1] = drama_name;
                                        switchmultibutton
                                                .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ","                   "+Drama[3]+"                   ");
                                    }
                                });
                                editDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                });
                                editDialog.show();
                            }
                        });

                    spinner_drame_word="Drama2";
                    break;
                case 2:
//                    if(tabText == "                    Drama3                   "){
//                        AlertDialog.Builder editDialog = new AlertDialog.Builder(CreatDrama.this);
//                        editDialog.setTitle("??????????????????");
//                        editDialog.setIcon(R.drawable.ic_launcher);
//
//                        final EditText editText = new EditText(CreatDrama.this);
//                        editDialog.setView(editText);
//                        editDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
//                            // do something when the button is clicked
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                Drama[2] = editText.getText().toString();
//                                switchmultibutton
//                                        .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ");
//                            }
//                        });
//                        editDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
//                            // do something when the button is clicked
//                            public void onClick(DialogInterface arg0, int arg1) {
//                            }
//                        });
//                        editDialog.show();
//                    }
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder editDialog = new AlertDialog.Builder(CreatDrama.this);
                                editDialog.setTitle("??????????????????");
                                editDialog.setIcon(R.drawable.ic_launcher);

                                final EditText editText = new EditText(CreatDrama.this);
                                editDialog.setView(editText);
                                editDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        String drama_name = editText.getText().toString().trim();
                                        drama_name = drama_name.toUpperCase().charAt(0)+drama_name.substring(1);
                                        Drama[2] = drama_name;
                                        switchmultibutton
                                                .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ","                   "+Drama[3]+"                   ");
                                    }
                                });
                                editDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                });
                                editDialog.show();
                            }
                        });
                    spinner_drame_word="Drama3";
                    break;

                case 3:
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder editDialog = new AlertDialog.Builder(CreatDrama.this);
                            editDialog.setTitle("??????????????????");
                            editDialog.setIcon(R.drawable.ic_launcher);

                            final EditText editText = new EditText(CreatDrama.this);
                            editDialog.setView(editText);
                            editDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                // do something when the button is clicked
                                public void onClick(DialogInterface arg0, int arg1) {
                                    String drama_name = editText.getText().toString().trim();
                                    drama_name = drama_name.toUpperCase().charAt(0)+drama_name.substring(1);
                                    Drama[3] = drama_name;
                                    switchmultibutton
                                            .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ","                   "+Drama[3]+"                   ");
                                }
                            });
                            editDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                // do something when the button is clicked
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                            editDialog.show();
                        }
                    });

                    spinner_drame_word="Drama4";
            }
            Log.v("position", String.valueOf(position));
            try {
                ccc();
            }catch (Exception e){
                e.printStackTrace();
            }
            Toast.makeText(CreatDrama.this, tabText, Toast.LENGTH_SHORT).show();
        }
    };


    //--------CropImage????????????--------
    private void BringImagePicker() {
        CropImage.activity().
                setGuidelines(CropImageView.Guidelines.ON).
                setAspectRatio(12,10).
                start(CreatDrama.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, requestCode+":"+resultCode, Toast.LENGTH_SHORT).show();
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
//               //??????????????????local
//                try {
//                    MediaStore.Images.Media.insertImage(getContentResolver(),imageUri.toString(),null,null);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }

                //???????????????intent???????????????
                Intent intent = new Intent();
                intent.setClass(CreatDrama.this,EditDrama.class);
                intent.putExtra("ori_uri",imageUri.toString());//????????????????????????
                // ??????Activity
                startActivity(intent);

                //??????????????????FirebaseStorage
                fire_originalphoto = FirebaseStorage.getInstance().getReference()
                        .child(Student.Name + "/OriginalPhoto/" + spinner_drame_word + "/" + "ori_screen" + num + ".jpeg");

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
                        download_url = uri.toString();
                    }
                }).continueWithTask(new Continuation<Uri, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Uri> task) throws Exception {
//                        Intent intent = new Intent();
//                        intent.setClass(CreatDrama.this, EditDrama.class);
//                        startActivity(intent);
                        return null;
                    }
                });
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }


    //---------------????????????????????????------------------
    //???1?????????????????????
    private void re_edit1() {
        if(!TextUtils.equals("?????????", spinner_drame_word)){
            edit = true;
            num = 1;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
        }
    }
    //???2?????????????????????
    private  void re_edit2(){
        if(!TextUtils.equals("?????????", spinner_drame_word)){
            edit = true;
            num = 2;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
        }

    }

    //???3?????????????????????
    private  void re_edit3(){
        if(!TextUtils.equals("?????????", spinner_drame_word)){
            edit = true;
            num = 3;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    //???4?????????????????????
    private  void re_edit4(){
        if(!TextUtils.equals("?????????", spinner_drame_word)){
            edit = true;
            num = 4;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    //???5?????????????????????
    private  void re_edit5(){
        if(!TextUtils.equals("?????????", spinner_drame_word)){
            edit = true;
            num = 5;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    //???6?????????????????????
    private  void re_edit6(){
        if(!TextUtils.equals("?????????", spinner_drame_word)){
            edit = true;
            num = 6;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    //???7?????????????????????
    private  void re_edit7(){
        if(!TextUtils.equals("?????????", spinner_drame_word)){
            edit = true;
            num = 7;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    //???8?????????????????????
    private  void re_edit8(){
        if(!TextUtils.equals("?????????", spinner_drame_word)){
            edit = true;
            num = 8;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "?????????????????????", Toast.LENGTH_SHORT).show();
        }
    }



    private Bitmap getBitmap(View view) throws Exception {
        View screenView = getWindow().getDecorView();
        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache(); //????????????????????????
        Bitmap bitmap = screenView.getDrawingCache();
        if (bitmap != null) {
            //????????????????????????
            int outWidth = creat1.getWidth()+drama1.getWidth()+creat2.getWidth()+drama2.getWidth()+40;
            int outHeight =drama1.getHeight()+drama3.getHeight()+10;
            //????????????????????????????????????????????????(view??????????????????)
            int[] viewLocationArray = new int[2];
            creat1.getLocationOnScreen(viewLocationArray);
            //??????????????????????????????????????????
            bitmap = Bitmap.createBitmap(bitmap,
                    viewLocationArray[0], viewLocationArray[1],
                    outWidth, outHeight);
            Log.e(TAG, "OKKKK:" +Integer.toString(bitmap.getHeight()) +" ");
        }
        return bitmap;
    }

    public static void ccc(){
        DatabaseReference fire_check_edit_exist;
        fire_check_edit_exist = FirebaseDatabase.getInstance().getReference();
        drama1_edit = false;
        drama2_edit = false;
        drama3_edit = false;
        drama4_edit = false;
        drama5_edit = false;
        drama6_edit = false;
        drama7_edit = false;
        drama8_edit = false;

        fire_check_edit_exist.child("??????"+Student.Name+"???").child(spinner_drame_word).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                x=0;
                for (DataSnapshot each : dataSnapshot.getChildren()) {
                    if(each.getKey().equals("contextmenu") == false){
                        x++;
                    }
                }
                switch (x){
                    //??????(button1???creat5)?????????
                    case 0:
                        drama5.setVisibility(INVISIBLE);
                        drama6.setVisibility(INVISIBLE);
                        drama7.setVisibility(INVISIBLE);
                        drama8.setVisibility(INVISIBLE);
                        creat5.setVisibility(VISIBLE);
                        creat6.setVisibility(INVISIBLE);
                        creat7.setVisibility(INVISIBLE);
                        creat8.setVisibility(INVISIBLE);
                        button1.setVisibility(VISIBLE);
                        button2.setVisibility(INVISIBLE);
                        button3.setVisibility(INVISIBLE);
                        button4.setVisibility(INVISIBLE);
                        break;
                    case 1:
                        drama5.setVisibility(INVISIBLE);
                        drama6.setVisibility(INVISIBLE);
                        drama7.setVisibility(INVISIBLE);
                        drama8.setVisibility(INVISIBLE);
                        creat5.setVisibility(VISIBLE);
                        creat6.setVisibility(INVISIBLE);
                        creat7.setVisibility(INVISIBLE);
                        creat8.setVisibility(INVISIBLE);
                        button1.setVisibility(VISIBLE);
                        button2.setVisibility(INVISIBLE);
                        button3.setVisibility(INVISIBLE);
                        button4.setVisibility(INVISIBLE);
                        drama1_edit = true;
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
                        button1.setVisibility(VISIBLE);
                        button2.setVisibility(INVISIBLE);
                        button3.setVisibility(INVISIBLE);
                        button4.setVisibility(INVISIBLE);
                        drama1_edit = true;
                        drama2_edit = true;
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
                        button1.setVisibility(VISIBLE);
                        button2.setVisibility(INVISIBLE);
                        button3.setVisibility(INVISIBLE);
                        button4.setVisibility(INVISIBLE);
                        drama1_edit = true;
                        drama2_edit = true;
                        drama3_edit = true;
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
                        button1.setVisibility(VISIBLE);
                        button2.setVisibility(INVISIBLE);
                        button3.setVisibility(INVISIBLE);
                        button4.setVisibility(INVISIBLE);
                        drama1_edit = true;
                        drama2_edit = true;
                        drama3_edit = true;
                        drama4_edit = true;
                        break;
                    case 5:
                        drama6.setVisibility(INVISIBLE);
                        drama7.setVisibility(INVISIBLE);
                        drama8.setVisibility(INVISIBLE);
                        creat5.setVisibility(VISIBLE);
                        creat6.setVisibility(VISIBLE);
                        creat7.setVisibility(INVISIBLE);
                        creat8.setVisibility(INVISIBLE);
                        button1.setVisibility(INVISIBLE);
                        button2.setVisibility(VISIBLE);
                        button3.setVisibility(INVISIBLE);
                        button4.setVisibility(INVISIBLE);
                        drama1_edit = true;
                        drama2_edit = true;
                        drama3_edit = true;
                        drama4_edit = true;
                        drama5_edit = true;
                        break;
                    case 6:
                        drama7.setVisibility(INVISIBLE);
                        drama8.setVisibility(INVISIBLE);
                        creat5.setVisibility(VISIBLE);
                        creat6.setVisibility(VISIBLE);
                        creat7.setVisibility(VISIBLE);
                        creat8.setVisibility(INVISIBLE);
                        button1.setVisibility(INVISIBLE);
                        button2.setVisibility(INVISIBLE);
                        button3.setVisibility(VISIBLE);
                        button4.setVisibility(INVISIBLE);
                        drama1_edit = true;
                        drama2_edit = true;
                        drama3_edit = true;
                        drama4_edit = true;
                        drama5_edit = true;
                        drama6_edit = true;
                        break;
                    case 7:
                        drama8.setVisibility(INVISIBLE);
                        creat5.setVisibility(VISIBLE);
                        creat6.setVisibility(VISIBLE);
                        creat7.setVisibility(VISIBLE);
                        creat8.setVisibility(VISIBLE);
                        button1.setVisibility(INVISIBLE);
                        button2.setVisibility(INVISIBLE);
                        button3.setVisibility(INVISIBLE);
                        button4.setVisibility(VISIBLE);
                        drama1_edit = true;
                        drama2_edit = true;
                        drama3_edit = true;
                        drama4_edit = true;
                        drama5_edit = true;
                        drama6_edit = true;
                        drama7_edit = true;
                        break;
                    case 8:
                        creat5.setVisibility(VISIBLE);
                        creat6.setVisibility(VISIBLE);
                        creat7.setVisibility(VISIBLE);
                        creat8.setVisibility(VISIBLE);
                        button1.setVisibility(INVISIBLE);
                        button2.setVisibility(INVISIBLE);
                        button3.setVisibility(INVISIBLE);
                        button4.setVisibility(INVISIBLE);
                        drama1_edit = true;
                        drama2_edit = true;
                        drama3_edit = true;
                        drama4_edit = true;
                        drama5_edit = true;
                        drama6_edit = true;
                        drama7_edit = true;
                        drama8_edit = true;
                        break;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fire_check_edit_exist.child("??????"+Student.Name+"???").child(spinner_drame_word).child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama1.setVisibility(VISIBLE);
                    Glide.with(drama1.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama1);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "????????????", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "??????");
                    drama1.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "?????????");

            }
        });

        fire_check_edit_exist.child("??????"+Student.Name+"???").child(spinner_drame_word).child("2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama2.setVisibility(VISIBLE);
                    Glide.with(drama2.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama2);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "????????????", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "??????");
                    drama2.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "?????????");

            }
        });

        fire_check_edit_exist.child("??????"+Student.Name+"???").child(spinner_drame_word).child("3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama3.setVisibility(VISIBLE);
                    Glide.with(drama3.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama3);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "????????????", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "??????");
                    drama3.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "?????????");

            }
        });

        fire_check_edit_exist.child("??????"+Student.Name+"???").child(spinner_drame_word).child("4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama4.setVisibility(VISIBLE);
                    Glide.with(drama4.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama4);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "????????????", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "??????");
                    drama4.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "?????????");

            }
        });
        fire_check_edit_exist.child("??????"+Student.Name+"???").child(spinner_drame_word).child("5").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama5.setVisibility(VISIBLE);
                    Glide.with(drama5.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama5);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "????????????", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "??????");
                    drama5.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "?????????");

            }
        });
        fire_check_edit_exist.child("??????"+Student.Name+"???").child(spinner_drame_word).child("6").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama6.setVisibility(VISIBLE);
                    Glide.with(drama6.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama6);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "????????????", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "??????");
                    drama6.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "?????????");

            }
        });
        fire_check_edit_exist.child("??????"+Student.Name+"???").child(spinner_drame_word).child("7").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama7.setVisibility(VISIBLE);
                    Glide.with(drama7.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama7);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "????????????", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "??????");
                    drama7.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "?????????");

            }
        });
        fire_check_edit_exist.child("??????"+Student.Name+"???").child(spinner_drame_word).child("8").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama8.setVisibility(VISIBLE);
                    Glide.with(drama8.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama8);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "????????????", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "??????");
                    drama8.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "?????????");

            }
        });
    }



}
