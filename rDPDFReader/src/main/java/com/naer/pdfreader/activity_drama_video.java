package com.naer.pdfreader;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import pl.droidsonroids.gif.GifImageView;

public class activity_drama_video extends Activity {
;
    private ImageView videoPreview;
    private VideoView videoPlay;
    private String TAG = "123";
    private static int CAMERA_PERMISSION_CODE = 100;
    private static int STORAGE_PERMISSION_CODE = 101;
    private static int VIDEO_RECORD_CODE = 102;
    private static int VIDEO_STORAGE_CODE = 103;
    private static int PICTURE_CAPTURE_CODE = 104;
    private static int PICTURE_STORAGE_CODE = 105;

    private AutoCompleteTextView studentdrama;
    private Button toolbar_record;
    private Button toolbar_stop;
    private Button toolbar_play;
    private Uri videoPath;
    private Uri imagePath;



    //
    private DatabaseReference fire_speechdata;
    private DatabaseReference fire_process_write_listen;
    private DatabaseReference fire_listen_tts_time;
    private MediaRecorder recorder;
    private DatabaseReference fire_translation_time;
    private GifImageView loading;
    private SpeechRecognizer speechRecognizer;
    private Bitmap capturedImage;
    public static String choose_type_word;
    private String hw_drama_pathword;
    private boolean hw_isPress = true;
    ArrayAdapter<String> adapter;
    private StorageReference fire_hw_drama_record;
    private Context context;
    private String hw_drama_download_url = "";
    private SimpleDateFormat sdf_now;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar_record = findViewById(R.id.recordvideo);
        toolbar_stop = findViewById(R.id.stoplayerofvideorec);
        toolbar_play = findViewById(R.id.displayvideorec);
        setContentView(R.layout.activity_drama_video);
        initLevel0();
        initLevel1();
        initLevel2();


    }

    private void initLevel0() {
        //getSupportActionBar().hide();
        if (isCameraPresentInPhone()) {
            Toast.makeText(this, "Camera detected and ready to serve", Toast.LENGTH_LONG).show();
            getCameraPermission();
            getStoragePermission();
        } else {
            Toast.makeText(this, "Sorry! No camera is detected", Toast.LENGTH_LONG).show();
        }
    }

    private void initLevel1(){
        videoPreview = findViewById(R.id.video_preview);
        videoPlay = findViewById(R.id.video_play);
        loading = findViewById(R.id.loading);


    }




    private void initLevel2(){
        Picasso.get().load(R.mipmap.video_icon).into(videoPreview);
    }



    @Override
    protected void onPause() {
        Toast.makeText(this, "Pause", Toast.LENGTH_LONG).show();
        super.onPause();
    }


    private boolean isCameraPresentInPhone(){
        if(getPackageManager().hasSystemFeature((PackageManager.FEATURE_CAMERA_ANY))){
            return true;
        }
        else{
            return false;
        }
    }
    public void startRecord(View view) {

        loading.setVisibility(View.VISIBLE);
        Log.d(TAG, "startRecord: ");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizer.startListening(intent);
//        }
    }
    private void getCameraPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);

        }
    }

    private void getStoragePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private void recordVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        startActivityForResult(intent, VIDEO_RECORD_CODE);
    }

    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICTURE_CAPTURE_CODE);
    }


    private void playingVideo(){
        videoPreview.setVisibility(View.GONE);
        videoPlay.setVisibility(View.VISIBLE);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoPlay);
        videoPlay.setMediaController(mediaController);
        videoPlay.setVideoURI(videoPath);
        videoPlay.requestFocus();
        videoPlay.start();
    }

    private void viewImage(){
        videoPreview.setVisibility(View.VISIBLE);
        videoPlay.setVisibility(View.GONE);
        videoPreview.setImageBitmap(capturedImage);
    }

    public void buttonCaptureImage(View v){
        takePhoto();
    }

    public void buttonRecordVideo(View v){
        recordVideo();
    }

    public void buttonSelectVideo(View V){
        selectVideo();
    }

    private void selectVideo(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, VIDEO_STORAGE_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == VIDEO_STORAGE_CODE){
            if(data.getData()!=null){
                videoPath = data.getData();
                Toast.makeText(this, "Video is selected, Playing now", Toast.LENGTH_LONG).show();
                playingVideo();
            }
            else{
                Toast.makeText(this, "No Video is selected", Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode == PICTURE_CAPTURE_CODE){
            imagePath = data.getData();
            capturedImage = (Bitmap) data.getExtras().get("data");
            Toast.makeText(this, "Image is captured and available at: " + imagePath, Toast.LENGTH_LONG).show();
            viewImage();
        }

        if(requestCode == VIDEO_RECORD_CODE){
            if(resultCode == RESULT_OK){
                videoPath = data.getData();
                Toast.makeText(this, "Video is recorded and available at: " + videoPath, Toast.LENGTH_LONG).show();
                playingVideo();
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Video recording is cancelled", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Video recording: Error.", Toast.LENGTH_LONG).show();
            }
        }
        toolbar_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hw_isPress == true) {
                    MediaRecorder recorder = new MediaRecorder();// new???MediaRecorder??????
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    // ??????MediaRecorder????????????????????????
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                    // ??????MediaRecorder?????????????????????
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    // ??????MediaRecorder????????????????????????amr.

//                    hw_describe_pathword = Student.Name + "_" + hw1_list_word + ".amr";

                     hw_drama_pathword = Student.Name + "_" + studentdrama.getText().toString().trim() + ".amr";
                    recorder.setOutputFile("/sdcard/" + hw_drama_pathword);

                    // ??????????????????????????????????????????
                    try {
                        recorder.prepare();// ????????????
                        recorder.start();// ????????????
                        Toast.makeText(activity_drama_video.this, "????????????", Toast.LENGTH_SHORT).show();

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    hw_isPress = false;
                } else {
                    Toast.makeText(activity_drama_video.this, "?????????~???", Toast.LENGTH_SHORT).show();
                }
            }
        });


        toolbar_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hw_isPress == false) {
//                    hw_describe_pathword = Student.Name + "_" + hw1_list_word + ".amr";
                    hw_drama_pathword = Student.Name + "_" + studentdrama.getText().toString().trim() + ".amr";
                    fire_hw_drama_record = FirebaseStorage.getInstance().getReference()
                            .child(Student.Name).child("Describe Record").child(studentdrama.getText().toString().trim() + "/" + hw_drama_pathword);

                    recorder.stop();// ????????????
                    Toast.makeText(activity_drama_video.this, "????????????", Toast.LENGTH_SHORT).show();

                    try {
                        //??????????????????????????????
                        String date = sdf_now.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("Describe").child("????????????");
                        fire_60sec_student_data.child(date).child("Describe Text").setValue(studentdrama.getText().toString().trim());
                        fire_60sec_student_data.child(date).child("choose type").setValue(choose_type_word);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(activity_drama_video.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }


                    //??????????????????Firebase, ?????????:"??????/Homework_1_1", ?????????"????????????_Homework_1_1.amr"
                    //?????????continueWithTask???????????????url, ????????????????????????????????????
                    //?????????????????????????????????
                    Uri hw_describe_recoed_file = Uri.fromFile(new File("/sdcard/" + hw_drama_pathword));
                    fire_hw_drama_record.putFile(hw_describe_recoed_file).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return fire_hw_drama_record.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri uri = task.getResult();
                            hw_drama_download_url = uri.toString();
                        }
                    }).continueWithTask(new Continuation<Uri, Task<Void>>() {
                        @Override
                        public Task<Void> then(@NonNull Task<Uri> task) throws Exception {
                            return null;
                        }
                    });
                    // recorder.reset(); // ????????????MediaRecorder.
                    recorder.release(); // ?????????????????????????????????
                    recorder = null;

                    hw_isPress = true;
                } else {
                    Toast.makeText(activity_drama_video.this, "??????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toolbar_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = Uri.fromFile(new File("/sdcard/" + hw_drama_pathword));
                    MediaPlayer MP = MediaPlayer.create(activity_drama_video.this, uri);
                    MP.start();
                    MP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer MP) {
                            try {
                                //??????????????????????????????
                                String date = sdf_now.format(new java.util.Date());
                                DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                        .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("Describe").child("????????????");
                                fire_60sec_student_data.child(date).child("Describe Text").setValue(studentdrama.getText().toString().trim());
                                fire_60sec_student_data.child(date).child("choose type").setValue(choose_type_word);
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(activity_drama_video.this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                            }
                            MP.release();
                        }
                    });
                }catch (Exception e){

                    Toast.makeText(context,"????????????", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

    }
    });}}