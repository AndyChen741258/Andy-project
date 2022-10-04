package com.naer.pdfreader;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class activity_drama_video extends AppCompatActivity {
    private ImageView videoPreview;
    private VideoView videoPlay;

    private static int CAMERA_PERMISSION_CODE = 100;
    private static int STORAGE_PERMISSION_CODE = 101;
    private static int VIDEO_RECORD_CODE = 102;
    private static int VIDEO_STORAGE_CODE = 103;
    private Uri videoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLevel0();
        initLevel1();
        initLevel2();
    }

    private void initLevel0() {
        getSupportActionBar().hide();
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
    }

    private void initLevel2(){
        Picasso.get().load(R.mipmap.video_icon).into(videoPreview);
    }

    private boolean isCameraPresentInPhone(){
        if(getPackageManager().hasSystemFeature((PackageManager.FEATURE_CAMERA_ANY))){
            return true;
        }
        else{
            return false;
        }
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


    public void buttonRecordVideo(View v){
        recordVideo();
    }

    public void buttonSelectVideo(View V){
        selectVideo();
    }

    @SuppressLint("IntentReset")
    private void selectVideo(){
        @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, VIDEO_STORAGE_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == VIDEO_STORAGE_CODE){
            assert data != null;
            if(data.getData()!=null){
                videoPath = data.getData();
                Toast.makeText(this, "Video is selected, Playing now", Toast.LENGTH_LONG).show();
                playingVideo();
            }
            else{
                Toast.makeText(this, "No Video is selected", Toast.LENGTH_LONG).show();
            }
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
    }

}