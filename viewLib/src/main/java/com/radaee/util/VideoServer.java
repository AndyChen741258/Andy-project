package com.radaee.util;

import com.radaee.viewlib.R;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VideoServer extends Activity implements SurfaceHolder.Callback {

	TextView testView;
	 
	 Camera camera;
	 SurfaceView surfaceView;
	 SurfaceHolder surfaceHolder;
	 
	 private final String tag = "VideoServer";
	 
	 Button start, stop;
	 
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.photomain);
	        //final Button start=(Button)layout.findViewById(R.id.opphbut);
	        start = (Button)findViewById(R.id.opphbut);
	        start.setOnClickListener(new Button.OnClickListener()
	        {
	   public void onClick(View arg0) {
	    start_camera();
	   }
	        });
	        stop = (Button)findViewById(R.id.opclbut);
	        stop.setOnClickListener(new Button.OnClickListener()
	        {
	   public void onClick(View arg0) {
	    stop_camera();
	   }
	        });
	       
	        surfaceView = (SurfaceView)findViewById(R.id.surfaceView1);
	        surfaceHolder = surfaceView.getHolder();
	        surfaceHolder.addCallback(this);
	        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    }
	   
	    private void start_camera()
	    {
	     try{
	      camera = Camera.open();
	     }catch(RuntimeException e){
	      Log.e(tag, "init_camera: " + e);
	      return;
	     }
	     Camera.Parameters param;
	     param = camera.getParameters();
	     //modify parameter
	     param.setPreviewFrameRate(20);
	     param.setPreviewSize(176, 144);
	     camera.setParameters(param);
	     try {
	   camera.setPreviewDisplay(surfaceHolder);
	   camera.startPreview();
	  } catch (Exception e) {
	   Log.e(tag, "init_camera: " + e);
	   return;
	  }
	    }
	   
	    private void stop_camera()
	    {
	     camera.stopPreview();
	     camera.release();
	    }

	 public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	  // TODO Auto-generated method stub
	 }

	 public void surfaceCreated(SurfaceHolder holder) {
	  // TODO Auto-generated method stub
	 }

	 public void surfaceDestroyed(SurfaceHolder holder) {
	  // TODO Auto-generated method stub 
	 }

}

