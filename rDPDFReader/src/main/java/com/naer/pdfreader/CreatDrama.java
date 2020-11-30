package com.naer.pdfreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.naer.pdfreader.DialogActivity.TAG;

public class CreatDrama extends Activity {
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
    private Button creat1;
    private Button creat2;
    private Button creat3;
    private Button creat4;
    private Button creat5;
    private Button creat6;
    private Button creat7;
    private Button creat8;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button cancel;
    private Button finish_and_return;
    private int scoreview;


    //劇本編號所選擇的選項文字
    public static String spinner_drame_word;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//隱藏標題列
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
        finish_and_return = findViewById(R.id.finish_and_return);

        drama1.setVisibility(INVISIBLE);
        drama2.setVisibility(INVISIBLE);
        drama3.setVisibility(INVISIBLE);
        drama4.setVisibility(INVISIBLE);

        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                creat5.setVisibility(VISIBLE);
                button2.setVisibility(VISIBLE);
                button1.setVisibility(INVISIBLE);
                scoreview=1;

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                creat6.setVisibility(VISIBLE);
                button3.setVisibility(VISIBLE);
                button2.setVisibility(INVISIBLE);
                scoreview=2;
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                creat7.setVisibility(VISIBLE);
                button4.setVisibility(VISIBLE);
                button3.setVisibility(INVISIBLE);
                scoreview=3;
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                creat8.setVisibility(VISIBLE);
                button4.setVisibility(INVISIBLE);
                scoreview=4;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (scoreview){
                    case 4:
                        creat8.setVisibility(INVISIBLE);
                        button4.setVisibility(VISIBLE);
                        scoreview=3;
                        break;
                    case 3:
                        creat7.setVisibility(INVISIBLE);
                        button4.setVisibility(INVISIBLE);
                        button3.setVisibility(VISIBLE);
                        scoreview=2;
                        break;
                    case 2:
                        creat6.setVisibility(INVISIBLE);
                        button3.setVisibility(INVISIBLE);
                        button2.setVisibility(VISIBLE);
                        scoreview=1;
                        break;
                    case 1:
                        creat5.setVisibility(INVISIBLE);
                        button2.setVisibility(INVISIBLE);
                        button1.setVisibility(VISIBLE);
                        break;
                }
            }
        });










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

//        drama1.setOnClickListener(re_edit1);
//        drama2.setOnClickListener(re_edit2);
//        drama3.setOnClickListener(re_edit3);
//        drama4.setOnClickListener(re_edit4);


        //--------按下編輯1234 Button後進入拍照或是選擇照片模式----------
        creat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word)){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //照片庫
                        if (ContextCompat.checkSelfPermission(CreatDrama.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreatDrama.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else {
                            BringImagePicker();
                        }
                    } else {  //拍照
                        BringImagePicker();
                    }
                    num = 1;
                    edit = false;
                }else{
                    Toast.makeText(CreatDrama.this, "請先選擇劇本編號", Toast.LENGTH_SHORT).show();
                }

            }
        });

        creat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word)){
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
                }else{
                    Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
                }
            }
        });

        creat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word)){
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
                }else{
                    Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
                }
            }
        });

        creat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word)){
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
                }else{
                    Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
                }
            }
        });

        creat5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word)){
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
                }else{
                    Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
                }
            }
        });

        creat6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word)){
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
                }else{
                    Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
                }
            }
        });

        creat7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word)){
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
                }else{
                    Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
                }
            }
        });

        creat8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("請選擇", spinner_drame_word)){
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
                }else{
                    Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
                }
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
//    private View.OnClickListener re_edit1 = new View.OnClickListener(){
//        @Override
//        public void onClick(View view) {
//            if(!TextUtils.equals("請選擇", spinner_drame_word)){
//                edit = true;
//                num = 1;
//                image_name = drama1.toString();
//                Intent intent = new Intent(CreatDrama.this, EditDrama.class);
//                startActivity(intent);
//            }else{
//                Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
//
//    //第2格照片重新編輯
//    private View.OnClickListener re_edit2 = new View.OnClickListener(){
//        @Override
//        public void onClick(View view) {
//            if(!TextUtils.equals("請選擇", spinner_drame_word)){
//                edit = true;
//                num = 2;
//                image_name = drama1.toString();
//                Intent intent = new Intent(CreatDrama.this, EditDrama.class);
//                startActivity(intent);
//            }else{
//                Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
//
//    //第3格照片重新編輯
//    private View.OnClickListener re_edit3 = new View.OnClickListener(){
//        @Override
//        public void onClick(View view) {
//            if(!TextUtils.equals("請選擇", spinner_drame_word)){
//                edit = true;
//                num = 3;
//                image_name = drama1.toString();
//                Intent intent = new Intent(CreatDrama.this, EditDrama.class);
//                startActivity(intent);
//            }else{
//                Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
//            }
//
//
//        }
//    };
//
//    //第4格照片重新編輯
//    private View.OnClickListener re_edit4 = new View.OnClickListener(){
//        @Override
//        public void onClick(View view) {
//            if(!TextUtils.equals("請選擇", spinner_drame_word)){
//                edit = true;
//                num = 4;
//                image_name = drama1.toString();
//                Intent intent = new Intent(CreatDrama.this, EditDrama.class);
//                startActivity(intent);
//            }else{
//                Toast.makeText(CreatDrama.this, "請選擇劇本編號", Toast.LENGTH_SHORT).show();
//            }
//        }
//    };

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

        fire_check_edit_exist.child("學生"+Student.Name+"號").child(spinner_drame_word).child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    drama1.setVisibility(VISIBLE);
                    Glide.with(drama1.getContext()).load(dataSnapshot.child("editFinishPhotoUri").getValue().toString()).into(drama1);
                }else if(!dataSnapshot.exists()){
                    //Toast.makeText(CreatDrama.this, "開始創作", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "開始");
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
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "有問題");

            }
        });
    }



}
