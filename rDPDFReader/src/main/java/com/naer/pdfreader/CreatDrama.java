package com.naer.pdfreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lib.kingja.switchbutton.SwitchMultiButton;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.naer.pdfreader.DialogActivity.TAG;

public class CreatDrama extends Activity {
    private static int i;
    //創作劇本四格畫面
    private Spinner dramanumber;  //劇本編號
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
    private static Button button1;
    private static Button button2;
    private static Button button3;
    private static Button button4;
    private Button cancel;
    private Button finish_and_return;
    private int scoreview;


    //劇本編號所選擇的選項文字
    public static String spinner_drame_word = "Drama1";

    //creatdrama Button編號 根據編號將圖片傳回
    public static int num;

    //存放原始劇照的Firebase
    private StorageReference fire_originalphoto;

    //檢查是否有編輯資料 如果有才給重複編輯
    private DatabaseReference fire_re_data_exist;

    //存放四格截圖的Firebase
    private StorageReference fire_final_four_frame;

    public static String download_url = "";

    public static boolean edit = false;
    public static String image_name;

//    private String b ="";

    public static boolean cantoload = false;
    private View view;
    private boolean drama1_edit = false;
    private boolean drama2_edit = false;
    private boolean drama3_edit = false;
    private boolean drama4_edit = false;
    private boolean drama5_edit = false;
    private boolean drama6_edit = false;
    private boolean drama7_edit = false;
    private boolean drama8_edit = false;
    private SwitchMultiButton  switchmultibutton;

    private final String[] Drama = new String[4];
    private DatabaseReference fire_dramaname;
    private Button delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//隱藏標題列
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
            fire_load_dramaname.child("學生" + Student.Name + "號").child("DramaName")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int i = 0;
                            String[] sssttt = new String[(int) dataSnapshot.getChildrenCount()];
                            for (DataSnapshot each : dataSnapshot.getChildren()) {
                                sssttt[i] = each.getValue().toString();
                                Drama[i]=sssttt[i];
                                Log.v("檢查st", Drama[i]);
                                Log.v("檢查i", String.valueOf(i));
                                i++;
                            }
                                switchmultibutton
                                        .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ","                   "+Drama[3]+"                   ");
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
//            DatabaseReference db = database.getReference().child("學生" + Student.Name + "號").child("DramaName");
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


        //--------選擇劇本編號----------
        final String[] list = {"請選擇", "Drama_1", "Drama_2", "Drama_3"};

        ArrayAdapter<String> numberList=new ArrayAdapter<String>(CreatDrama.this,
                android.R.layout.simple_spinner_dropdown_item,
                list);
        dramanumber.setAdapter(numberList);
        dramanumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_drame_word = dramanumber.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
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


        //--------按下編輯1234 Button後進入拍照或是選擇照片模式----------

        drama1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.equals("請選擇", spinner_drame_word) && !drama1_edit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //照片庫
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //拍照
                        BringImagePicker();
                    }
                    Log.v("test", "false");
                    num = 1;
                    edit = false;
                    drama1_edit = true;
                } else if(drama1_edit){
                    Log.v("test","True");
                    re_edit1();
                }
                else{
                    Toast.makeText(CreatDrama.this, "請先選擇劇本編號", Toast.LENGTH_SHORT).show();
                }

            }
        });


        drama2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word) && !drama2_edit){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //照片庫
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //拍照
                        BringImagePicker();
                    }
                    num = 2;
                    edit = false;
                    drama2_edit = true;
                }else if(drama2_edit){
                    re_edit2();
                } else{
                    Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drama3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word) && !drama3_edit){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //照片庫
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //拍照
                        BringImagePicker();
                    }
                    num = 3;
                    edit = false;
                    drama3_edit = true;
                }else if(drama3_edit){
                    re_edit3();
                } else{
                    Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drama4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word) && !drama4_edit){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //照片庫
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //拍照
                        BringImagePicker();
                    }
                    num = 4;
                    edit = false;
                    drama4_edit = true;
                }else if(drama4_edit){
                    re_edit4();
                }else{
                    Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drama5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word) && !drama5_edit){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //照片庫
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //拍照
                        BringImagePicker();
                    }
                    num = 5;
                    edit = false;
                    drama5_edit = true;
                }else if(drama5_edit){
                    re_edit5();
                }else{
                    Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drama5.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                Toast.makeText(CreatDrama.this, "按下長按鈕", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(CreatDrama.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("隱藏情境5")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
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
                        .setNegativeButton("取消",null).create()
                        .show();
                return true;
            }
        });


        drama6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word) && !drama6_edit){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //照片庫
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //拍照
                        BringImagePicker();
                    }
                    num = 6;
                    edit = false;
                    drama6_edit = true;
                }else if(drama6_edit){
                    re_edit6();
                } else{
                    Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drama6.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                Toast.makeText(CreatDrama.this, "按下長按鈕", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(CreatDrama.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("隱藏情境6")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
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
                        .setNegativeButton("取消",null).create()
                        .show();
                return true;
            }
        });

        drama7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word) && !drama7_edit){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //照片庫
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //拍照
                        BringImagePicker();
                    }
                    num = 7;
                    edit = false;
                    drama7_edit = true;
                }else if(drama7_edit){
                    re_edit7();
                } else{
                    Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drama7.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                Toast.makeText(CreatDrama.this, "按下長按鈕", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(CreatDrama.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("隱藏情境7")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
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
                        .setNegativeButton("取消",null).create()
                        .show();
                return true;
            }
        });

        drama8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word) && !drama8_edit){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //照片庫
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //拍照
                        BringImagePicker();
                    }
                    num = 8;
                    edit = false;
                    drama8_edit = true;
                }else if(drama8_edit){
                    re_edit8();
                } else{
                    Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drama8.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                Toast.makeText(CreatDrama.this, "按下長按鈕", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(CreatDrama.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("隱藏情境8")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
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
                        .setNegativeButton("取消",null).create()
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
                    .setTitle("確認退出")
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
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
                                fire_dramaname.child("學生"+Student.Name+"號").child("DramaName").updateChildren(childUpdates);
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
                    .setNegativeButton("取消",null).create()
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
//                        editDialog.setTitle("編輯劇本名稱");
//                        editDialog.setIcon(R.drawable.ic_launcher);
//
//                        final EditText editText = new EditText(CreatDrama.this);
//                        editDialog.setView(editText);
//                        editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                            // do something when the button is clicked
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                Drama[0] = editText.getText().toString();
//                                switchmultibutton
//                                        .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ");
//                            }
//                        });
//                        editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
                            editDialog.setTitle("編輯劇本名稱");
                            editDialog.setIcon(R.drawable.ic_launcher);

                            final EditText editText = new EditText(CreatDrama.this);
                            editDialog.setView(editText);
                            editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                // do something when the button is clicked
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Drama[0] = editText.getText().toString().trim();
                                    switchmultibutton
                                            .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ","                   "+Drama[3]+"                   ");
                                }
                            });
                            editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
//                        editDialog.setTitle("編輯劇本名稱");
//                        editDialog.setIcon(R.drawable.ic_launcher);
//
//                        final EditText editText = new EditText(CreatDrama.this);
//                        editDialog.setView(editText);
//                        editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                            // do something when the button is clicked
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                Drama[1] = editText.getText().toString();
//                                switchmultibutton
//                                        .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ");
//                            }
//                        });
//                        editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
                                editDialog.setTitle("編輯劇本名稱");
                                editDialog.setIcon(R.drawable.ic_launcher);

                                final EditText editText = new EditText(CreatDrama.this);
                                editDialog.setView(editText);
                                editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Drama[1] = editText.getText().toString().trim();
                                        switchmultibutton
                                                .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ","                   "+Drama[3]+"                   ");
                                    }
                                });
                                editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
//                        editDialog.setTitle("編輯劇本名稱");
//                        editDialog.setIcon(R.drawable.ic_launcher);
//
//                        final EditText editText = new EditText(CreatDrama.this);
//                        editDialog.setView(editText);
//                        editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                            // do something when the button is clicked
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                Drama[2] = editText.getText().toString();
//                                switchmultibutton
//                                        .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ");
//                            }
//                        });
//                        editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
                                editDialog.setTitle("編輯劇本名稱");
                                editDialog.setIcon(R.drawable.ic_launcher);

                                final EditText editText = new EditText(CreatDrama.this);
                                editDialog.setView(editText);
                                editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Drama[2] = editText.getText().toString().trim();
                                        switchmultibutton
                                                .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ","                   "+Drama[3]+"                   ");
                                    }
                                });
                                editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
                            editDialog.setTitle("編輯劇本名稱");
                            editDialog.setIcon(R.drawable.ic_launcher);

                            final EditText editText = new EditText(CreatDrama.this);
                            editDialog.setView(editText);
                            editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                // do something when the button is clicked
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Drama[3] = editText.getText().toString().trim();
                                    switchmultibutton
                                            .setText("                   "+Drama[0]+"                   ","                   "+Drama[1]+"                   ","                   "+Drama[2]+"                   ","                   "+Drama[3]+"                   ");
                                }
                            });
                            editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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


    //--------CropImage剪裁圖片--------
    private void BringImagePicker() {
        CropImage.activity().
                setGuidelines(CropImageView.Guidelines.ON).
                setAspectRatio(6,3).
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
//               //試試會不會存local
//                try {
//                    MediaStore.Images.Media.insertImage(getContentResolver(),imageUri.toString(),null,null);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }

                //當拍完照後intent傳至編輯區
                Intent intent = new Intent();
                intent.setClass(CreatDrama.this,EditDrama.class);
                intent.putExtra("ori_uri",imageUri.toString());//可放所有基本類別
                // 切換Activity
                startActivity(intent);

                //存原始圖片進FirebaseStorage
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


    //---------------重新編輯狀態導入------------------
    //第1格照片重新編輯
    private void re_edit1() {
        if(!TextUtils.equals("請選擇", spinner_drame_word)){
            edit = true;
            num = 1;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
        }
    }
    //第2格照片重新編輯
    private  void re_edit2(){
        if(!TextUtils.equals("請選擇", spinner_drame_word)){
            edit = true;
            num = 2;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
        }

    }

    //第3格照片重新編輯
    private  void re_edit3(){
        if(!TextUtils.equals("請選擇", spinner_drame_word)){
            edit = true;
            num = 3;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
        }
    }

    //第4格照片重新編輯
    private  void re_edit4(){
        if(!TextUtils.equals("請選擇", spinner_drame_word)){
            edit = true;
            num = 4;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
        }
    }

    //第5格照片重新編輯
    private  void re_edit5(){
        if(!TextUtils.equals("請選擇", spinner_drame_word)){
            edit = true;
            num = 5;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
        }
    }

    //第6格照片重新編輯
    private  void re_edit6(){
        if(!TextUtils.equals("請選擇", spinner_drame_word)){
            edit = true;
            num = 6;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
        }
    }

    //第7格照片重新編輯
    private  void re_edit7(){
        if(!TextUtils.equals("請選擇", spinner_drame_word)){
            edit = true;
            num = 7;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
        }
    }

    //第8格照片重新編輯
    private  void re_edit8(){
        if(!TextUtils.equals("請選擇", spinner_drame_word)){
            edit = true;
            num = 8;
            image_name = drama1.toString();
            Intent intent = new Intent(CreatDrama.this, EditDrama.class);
            startActivity(intent);
        }else{
            Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
        }
    }



    private Bitmap getBitmap(View view) throws Exception {
        View screenView = getWindow().getDecorView();
        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache(); //獲取螢幕整張圖片
        Bitmap bitmap = screenView.getDrawingCache();
        if (bitmap != null) {
            //需要擷取的長和寬
            int outWidth = creat1.getWidth()+drama1.getWidth()+creat2.getWidth()+drama2.getWidth()+40;
            int outHeight =drama1.getHeight()+drama3.getHeight()+10;
            //獲取需要截圖部分的在螢幕上的座標(view的左上角座標)
            int[] viewLocationArray = new int[2];
            creat1.getLocationOnScreen(viewLocationArray);
            //從螢幕整張圖片中擷取指定區域
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

        fire_check_edit_exist.child("學生"+Student.Name+"號").child(spinner_drame_word).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double[] sssttt = new double[(int) dataSnapshot.getChildrenCount()];
                 i = sssttt.length;
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
                        button1.setVisibility(VISIBLE);
                        button2.setVisibility(INVISIBLE);
                        button3.setVisibility(INVISIBLE);
                        button4.setVisibility(INVISIBLE);
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
                        button4.setVisibility(INVISIBLE);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fire_check_edit_exist.child("學生"+Student.Name+"號").child(spinner_drame_word).child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama1.setVisibility(VISIBLE);
                    Glide.with(drama1.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama1);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama1.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });

        fire_check_edit_exist.child("學生"+Student.Name+"號").child(spinner_drame_word).child("2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama2.setVisibility(VISIBLE);
                    Glide.with(drama2.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama2);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama2.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });

        fire_check_edit_exist.child("學生"+Student.Name+"號").child(spinner_drame_word).child("3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama3.setVisibility(VISIBLE);
                    Glide.with(drama3.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama3);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama3.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });

        fire_check_edit_exist.child("學生"+Student.Name+"號").child(spinner_drame_word).child("4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama4.setVisibility(VISIBLE);
                    Glide.with(drama4.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama4);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama4.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });
        fire_check_edit_exist.child("學生"+Student.Name+"號").child(spinner_drame_word).child("5").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama5.setVisibility(VISIBLE);
                    Glide.with(drama5.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama5);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama5.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });
        fire_check_edit_exist.child("學生"+Student.Name+"號").child(spinner_drame_word).child("6").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama6.setVisibility(VISIBLE);
                    Glide.with(drama6.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama6);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama6.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });
        fire_check_edit_exist.child("學生"+Student.Name+"號").child(spinner_drame_word).child("7").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama7.setVisibility(VISIBLE);
                    Glide.with(drama7.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama7);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama7.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });
        fire_check_edit_exist.child("學生"+Student.Name+"號").child(spinner_drame_word).child("8").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama8.setVisibility(VISIBLE);
                    Glide.with(drama8.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama8);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
                    drama8.setImageResource(R.drawable.noimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });
    }



}
