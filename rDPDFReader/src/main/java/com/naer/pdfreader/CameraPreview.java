package com.naer.pdfreader;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;


public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;


    public CameraPreview(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    private static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();

        } catch (Exception e) {
            Log.d(TAG, "camera is not available");
        }
        return c;
    }


    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = getCameraInstance();

        try {
            mCamera.setPreviewDisplay(holder);
            initCamera();
            mCamera.startPreview();
            mCamera.cancelAutoFocus();

        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        mHolder.removeCallback(this);
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
       /* mCamera.autoFocus(new Camera.AutoFocusCallback() {

             public void onAutoFocus(boolean success, Camera camera) {

                 if(success){
                     initCamera();//实现相机的参数初始化
                     camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
                 }
             }
        });*/
    }
    //設定相機參數
    public void initCamera(){
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(1920, 1080);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//連續自動對焦
        mCamera.setParameters(parameters);
        mCamera.startPreview();
        mCamera.cancelAutoFocus();
    }


}