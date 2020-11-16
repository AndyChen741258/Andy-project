package com.naer.pdfreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DialogTitle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import Model.DescribeData;
import Model.StoreTheEditData;

import static com.naer.pdfreader.DialogActivity.TAG;

public class EditDrama extends Activity {

    private ImageView editimage;
    private Button adddialog1;
    private Button adddialog2;
    private Button finishedit;
    private Button student_question;
    private TextView say1; //隱藏的角色 A 對話框
    private TextView say2; //隱藏的角色 B 對話框

    private EditText editText1;
    private EditText editText2;

    //角色 A 錄音儲存檔名
    private String pathword_a = Student.Name + "_" + CreatDrama.spinner_drame_word + "_" + CreatDrama.num + "_" + "A.amr";
    //角色 B 錄音儲存檔名
    private String pathword_b = Student.Name + "_" + CreatDrama.spinner_drame_word + "_" + CreatDrama.num + "_" + "B.amr";

    //-------------錄音功能--------------
    private MediaRecorder recorder;
    private StorageReference fire_dramarecord;
    //限制錄音按鈕僅能按一下, 等按下停止時才可以再恢復可按狀態
    private boolean isPress = true;


    //-------------播放錄音--------------
    private MediaPlayer player;
    private String record_download_url_a = "";
    private String record_download_url_b = "";
    int playtimeA = 0;
    int playtimeB = 0;
    private String Link;

    int timeCount;

    private DatabaseReference fire_editdata;

    //存放編輯好的圖片截圖
    private StorageReference fire_finishphoto;

    private StorageReference fire_check_ori_exist;

    public static String download_url_editFinish = "";

    private String a = "";

    StoreTheEditData storeTheEditData;

    private String re_a_line_word = "";
    private String re_b_line_word = "";
    private View.OnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//隱藏標題列
        setContentView(R.layout.activity_edit_drama);

        editimage = findViewById(R.id.editimage);
        adddialog1 = findViewById(R.id.adddialog1);
        adddialog2 = findViewById(R.id.adddialog2);
        student_question = findViewById(R.id.student_question);
        student_question.setOnClickListener(listener);

        finishedit = findViewById(R.id.finishedit);
        say1 = findViewById(R.id.say1);
        say2 = findViewById(R.id.say2);
        say1.setVisibility(View.INVISIBLE);
        say2.setVisibility(View.INVISIBLE);

        //判斷是否是更新編輯
        //若是更新編輯 則執行撈回資料的方法
        //若非更新編輯, 也就是第一次編輯 則從Creat那邊將拍好的照片放過來進行編輯
        if(CreatDrama.edit == true){
            returnbackdata();
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
        }


        say1.setOnTouchListener(say1Listener);
        say2.setOnTouchListener(say2Listener);
        student_question.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                if (v.getId() == R.id.student_question) {
                    new AlertDialog.Builder(EditDrama.this)
                            .setTitle("建議")
                            .setMessage("選擇建議的親人名稱")
                            .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onDestroy();
                                }
                            })
                            .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onDestroy();
                                }
                            })
                            .show();

                }
            }
        });

        //編輯完照片按下finishedit按鈕 將截圖傳回CreatDrama (從Firebase撈)
        finishedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                        }
                    });


                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), getBitmap(view), null,null));

                    //將編輯好的照片截圖傳進Firebase
                    fire_finishphoto = FirebaseStorage.getInstance().getReference().child(Student.Name + "/EditFinish/"
                            + CreatDrama.spinner_drame_word + "/" + "edit_screen"+ CreatDrama.num + ".jpeg");
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
                                    .child("學生"+Student.Name+"號").child(CreatDrama.spinner_drame_word).child(Integer.toString(CreatDrama.num));

                            if(CreatDrama.edit == true){
                                fire_editdata.child("playerA_record").setValue(record_download_url_a);
                                fire_editdata.child("playerA_text").setValue(say1.getText().toString());
                                fire_editdata.child("playerA_x").setValue(say1.getX());
                                fire_editdata.child("playerA_y").setValue(say1.getY());
                                fire_editdata.child("playerB_record").setValue(record_download_url_b);
                                fire_editdata.child("playerB_text").setValue(say2.getText().toString());
                                fire_editdata.child("playerB_x").setValue(say2.getX());
                                fire_editdata.child("playerB_y").setValue(say2.getY());
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
                                storeTheEditData = new StoreTheEditData(CreatDrama.download_url.toString(), say1.getX(), say1.getY(),
                                        say2.getX(), say2.getY(), say1.getText().toString(), say2.getText().toString(),
                                        record_download_url_a,record_download_url_b, playtimeA, playtimeB, download_url_editFinish.toString());

                                fire_editdata.setValue(storeTheEditData).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                return null;
                            }
                            return null;
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
                CreatDrama.cantoload = true;
//                CreatDrama.ccc();



            }
        });
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
    public void buildLinesA_Click(final View view){
        findViewById(R.id.adddialog1).setEnabled(false);

        LayoutInflater inflater = LayoutInflater.from(EditDrama.this);
        final View v = inflater.inflate(R.layout.builddrama, null);
        editText1= (EditText) (v.findViewById(R.id.editText1));
        final Button record1 = (v.findViewById(R.id.recordplayer1));
        final Button stop1 = (v.findViewById(R.id.stopplayer1));
        final Button play1 = (v.findViewById(R.id.playplayer1));
        final TextView showtime1 = (v.findViewById(R.id.show_time1));

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
                .setTitle("角色A")
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
        if(CreatDrama.edit == true){
            editText1.setText(re_a_line_word);

        }

        drama.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判斷台詞欄位是否為空
                        if(!(TextUtils.isEmpty(editText1.getText().toString()))){
                            say1.setText(editText1.getText().toString());
                            say1.setVisibility(View.VISIBLE);
                            drama.dismiss();
                            Toast.makeText(getApplicationContext(), " A 對話已建立", Toast.LENGTH_SHORT).show();
                            findViewById(R.id.adddialog1).setEnabled(true);
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
                        Toast.makeText(EditDrama.this, "開始錄音角色 A 對話", Toast.LENGTH_SHORT).show();

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
        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(Student.Name).child(CreatDrama.spinner_drame_word+"/"+pathword_a);
        //角色A對話錄音停止
        stop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPress == false){
                    recorder.stop();// 停止燒錄
                    Toast.makeText(EditDrama.this, "停止錄音角色 A 對話", Toast.LENGTH_SHORT).show();
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
                playtimeA += 1;
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
    public void buildLinesB_Click(final View view){
        findViewById(R.id.adddialog2).setEnabled(false);

        LayoutInflater inflater = LayoutInflater.from(EditDrama.this);
        final View v = inflater.inflate(R.layout.builddrama2, null);
        editText2 = (EditText) (v.findViewById(R.id.editText2));
        final Button record2 = (v.findViewById(R.id.recordplayer2));
        final Button stop2 = (v.findViewById(R.id.stopplayer2));
        final Button play2 = (v.findViewById(R.id.playplayer2));
        final TextView showtime2 = (v.findViewById(R.id.show_time2));

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
                .setTitle("角色 B 對話編輯")
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

        if(CreatDrama.edit == true){
            editText2.setText(re_b_line_word);
        }

        drama.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判斷台詞欄位是否為空
                        if(!(TextUtils.isEmpty(editText2.getText().toString()))){
                            say2.setText(editText2.getText().toString());
                            say2.setVisibility(View.VISIBLE);
                            findViewById(R.id.adddialog2).setEnabled(true);
                            drama.dismiss();
                            Toast.makeText(getApplicationContext(), " B 對話已建立", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "請輸入完整", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //角色A對話錄音開始
        record2.setOnClickListener(new View.OnClickListener() {
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

                    recorder.setOutputFile("/sdcard/" + pathword_b);

                    // 設定錄製好的音訊檔案儲存路徑
                    try {
                        recorder.prepare();// 準備錄製
                        recorder.start();// 開始錄製
                        Toast.makeText(EditDrama.this, "開始錄音角色 B 對話", Toast.LENGTH_SHORT).show();

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
        fire_dramarecord = FirebaseStorage.getInstance().getReference().child(Student.Name).child(CreatDrama.spinner_drame_word+"/"+pathword_b);
        //角色A對話錄音停止
        stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPress == false){
                    recorder.stop();// 停止燒錄
                    Toast.makeText(EditDrama.this, "停止錄音角色 B 對話", Toast.LENGTH_SHORT).show();

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

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Log.e("View", v.toString());
            switch (event.getAction()) {          //判斷觸控的動作

                case MotionEvent.ACTION_DOWN:// 按下圖片時
                    x = event.getX();                  //觸控的X軸位置
                    y = event.getY();                  //觸控的Y軸位置
                    break;
                case MotionEvent.ACTION_MOVE:// 移動圖片時
                    //getX()：是獲取當前控件(View)的座標
                    //getRawX()：是獲取相對顯示螢幕左上角的座標
                    mx = (int) (event.getRawX() - x);
                    my = (int) (event.getRawY() - y);
                    setRelativeViewLocation(say1, mx, my, mx + v.getWidth(), my + v.getHeight());
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
//                    String ResoltTTS =say1.getText().toString();
//                    TTS.speak(ResoltTTS);
                    break;
                case MotionEvent.ACTION_CANCEL:
                break;

            }
            Log.e("address", String.valueOf(mx) + "~~" + String.valueOf(my)); // 記錄目前位置
            return true;
        }
        private void setRelativeViewLocation(View view, int left, int top, int right, int bottom) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(right - left, bottom - top);
            Log.e("params", params.width + "~~" +Integer.toString(right) + Integer.toString(left));
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

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Log.e("View", v.toString());
            switch (event.getAction()) {          //判斷觸控的動作

                case MotionEvent.ACTION_DOWN:// 按下圖片時
                    x = event.getX();                  //觸控的X軸位置
                    y = event.getY();                  //觸控的Y軸位置
                    v.bringToFront();
                    break;

                case MotionEvent.ACTION_MOVE:// 移動圖片時
                    //getX()：是獲取當前控件(View)的座標
                    //getRawX()：是獲取相對顯示螢幕左上角的座標
                    mx = (int) (event.getRawX() - x);
                    my = (int) (event.getRawY() - y);
                    setRelativeViewLocation(say2, mx, my, mx + v.getWidth(), my + v.getHeight());
                    break;
                case MotionEvent.ACTION_UP:
//                    String SpeechTTS =say2.getText().toString();
//                    TTS.speak(SpeechTTS);
                    break;

            }
            Log.e("address", String.valueOf(mx) + "~~" + String.valueOf(my)); // 記錄目前位置

            return true;
        }

        private void setRelativeViewLocation(View view, int left, int top, int right, int bottom) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(right - left, bottom - top);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params.setMargins(left, top, 0, 0);
            view.setLayoutParams(params);
        }

    };

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
        Toast.makeText(this, CreatDrama.spinner_drame_word + " & " +CreatDrama.num , Toast.LENGTH_SHORT).show();
        fire_editdata = FirebaseDatabase.getInstance().getReference()
                .child("學生"+Student.Name+"號").child(CreatDrama.spinner_drame_word).child(Integer.toString(CreatDrama.num));
        fire_editdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    StoreTheEditData storeTheEditData = dataSnapshot.getValue(StoreTheEditData.class);
                if(!isDestroy((Activity)editimage.getContext())){
                    Glide.with(editimage.getContext()).load(storeTheEditData.getOriginalPhotoUri()).into(editimage);
                }
                    say1.setVisibility(View.VISIBLE);
                    say2.setVisibility(View.VISIBLE);
                    say1.setText(storeTheEditData.getPlayerA_text());
                    say2.setText(storeTheEditData.getPlayerB_text());
                    say1.setX(storeTheEditData.getPlayerA_x());
                    say1.setY(storeTheEditData.getPlayerA_y());
                    say2.setX(storeTheEditData.getPlayerB_x());
                    say2.setY(storeTheEditData.getPlayerB_y());

                    re_a_line_word = storeTheEditData.getPlayerA_text();
                    re_b_line_word = storeTheEditData.getPlayerB_text();

                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditDrama.this, "Oops.....Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

        return;

    };
}
