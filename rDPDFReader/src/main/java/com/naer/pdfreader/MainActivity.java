package com.naer.pdfreader;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.radaee.pdf.Global;
import com.radaee.reader.PDFCurlViewAct;
import com.radaee.reader.PDFLayoutView;
import com.radaee.reader.PDFNavAct;
import com.radaee.reader.PDFViewAct;

public class MainActivity extends Activity implements OnClickListener
{
	private Button m_btn_file;
	private Button m_btn_asset;
	private Button m_btn_sdcard;
	private Button m_btn_http;
    private Button m_btn_gl;
    private Button m_btn_565;
    private Button m_btn_4444;
    private Button m_btn_curl;
    private Button m_btn_about;
    private EditText m_ID_edit;
    private EditText m_Pwd_edit;
    private  EditText Name;
    private Spinner spinner_std;
	private Spinner spinner_work;
	public String Link;
	public static String stu="學生"+Student.Name+"號";
	//private String stu="學生30號";
	private String spinner_std_word;
	private String spinner_work_word;
	public static String StudentSelectedName;
	public static String HomeworkSelected;
	public static final String TAG = "pname";


	private StorageReference mStorageRef;
	private DatabaseReference mDatabase;

	MediaProjectionManager mediaProjectionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        //plz set this line to Activity in AndroidManifes.xml:
        //    android:configChanges="orientation|keyboardHidden|screenSize"
        //otherwise, APP shall destroy this Activity and re-create a new Activity when rotate.
		RelativeLayout layout = (RelativeLayout)LayoutInflater.from(this).inflate(com.naer.pdfreader.R.layout.activity_main, null);
		Name = layout.findViewById(com.naer.pdfreader.R.id.editName);

		m_btn_file = layout.findViewById(com.naer.pdfreader.R.id.btn_file);
		m_btn_asset = layout.findViewById(com.naer.pdfreader.R.id.btn_asset);
		m_btn_sdcard = (Button)layout.findViewById(com.naer.pdfreader.R.id.btn_sdcard);
		m_btn_http = (Button)layout.findViewById(com.naer.pdfreader.R.id.btn_http);
        m_btn_gl = (Button)layout.findViewById(com.naer.pdfreader.R.id.btn_gl);
        m_btn_565 = (Button)layout.findViewById(com.naer.pdfreader.R.id.btn_565);
        m_btn_4444 = (Button)layout.findViewById(com.naer.pdfreader.R.id.btn_4444);
        m_btn_curl = (Button)layout.findViewById(com.naer.pdfreader.R.id.btn_curl);
        m_btn_about = (Button)layout.findViewById(com.naer.pdfreader.R.id.btn_about);
        m_ID_edit=(EditText)layout.findViewById(com.naer.pdfreader.R.id.Pwdedit);
		m_btn_file.setOnClickListener(this);
		m_btn_asset.setOnClickListener(this);
		m_btn_sdcard.setOnClickListener(this);
		m_btn_http.setOnClickListener(this);
        m_btn_gl.setOnClickListener(this);
        m_btn_565.setOnClickListener(this);
        m_btn_4444.setOnClickListener(this);
        m_btn_curl.setOnClickListener(this);
        m_btn_about.setOnClickListener(this);
		spinner_std= (Spinner)layout.findViewById(com.naer.pdfreader.R.id.spinner_std);
		spinner_work= (Spinner)layout.findViewById(com.naer.pdfreader.R.id.spinner_work);
		Log.i(TAG, "onCreate: license "+this.getPackageName());

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Global.Init(this);
		mDatabase = FirebaseDatabase.getInstance().getReference();	//初始化FirebaseDatabase
		mStorageRef = FirebaseStorage.getInstance().getReference();	//初始化FirebaseStorage

		final String[] lunch = {"老師","學生1號", "學生2號", "學生3號", "學生4號", "學生5號", "學生6號", "學生7號"
				, "學生8號", "學生9號", "學生10號", "學生11號", "學生12號", "學生13號"
				, "學生14號", "學生15號", "學生16號", "學生17號", "學生18號", "學生19號"
				, "學生20號", "學生21號", "學生22號", "學生23號", "學生24號", "學生25號"
				, "學生26號", "學生27號", "學生28號", "學生29號", "學生30號"};
		final String[] lunch2 = {"第一課", "第二課", "第三課",
				"作業一", "作業二", "作業三", "作業四"};



		ArrayAdapter<String> lunchList=new ArrayAdapter<String>(MainActivity.this,
				android.R.layout.simple_spinner_dropdown_item,
				lunch);
		spinner_std.setAdapter(lunchList);
		spinner_std.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				spinner_std_word = spinner_std.getSelectedItem().toString();
				Toast.makeText(MainActivity.this, spinner_std_word, Toast.LENGTH_SHORT).show();
				StudentSelectedName = lunch[position];//判斷選則其他同學名稱
				mStorageRef = FirebaseStorage.getInstance().getReference();
				mStorageRef.child(spinner_std_word+"/"+spinner_work_word+".pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
					@Override
					public void onSuccess(Uri uri) {
						Link=uri.toString();
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception exception) {
						Link="";

					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
				spinner_std_word = spinner_std.getSelectedItem().toString();
			}
		});
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		ArrayAdapter<String> lunchList2=new ArrayAdapter<String>(MainActivity.this,
				android.R.layout.simple_spinner_dropdown_item,
				lunch2);
		spinner_work.setAdapter(lunchList2);
		spinner_work.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(MainActivity.this, "查看" + lunch2[position], Toast.LENGTH_SHORT).show();
				spinner_work_word = spinner_work.getSelectedItem().toString();
				HomeworkSelected = spinner_work.getSelectedItem().toString();
				mStorageRef = FirebaseStorage.getInstance().getReference();
				mStorageRef.child(spinner_std_word+"/"+spinner_work_word+".pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
					@Override
					public void onSuccess(Uri uri) {
						Link=uri.toString();
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception exception) {
						Link="";

					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
				spinner_work_word = spinner_work.getSelectedItem().toString();
			}
		});

		setContentView(layout);
	}


    @SuppressLint("InlinedApi")
	@Override
    protected void onDestroy()
    {
    	Global.RemoveTmp();
    	super.onDestroy();
    }
	@Override
	public void onClick(View arg0) {
		//spinner_std_word = spinner_std.getSelectedItem().toString();
		//spinner_work_word = spinner_work.getSelectedItem().toString();

        if(!TextUtils.isEmpty(Name.getText().toString())){

			if (arg0 == m_btn_file) {
				Student.Name =  Name.getText().toString();
				//用廣播的方式把資料廣播到另一個模組，才能取得學生名稱
				Intent broadcastIntent = new Intent("StudentsName");
				broadcastIntent.putExtra("ShareStudentsName","");
				broadcastIntent.putExtra("Topic","");
				broadcastIntent.putExtra("name",Student.Name);
				broadcastIntent.putExtra("StudentSelectedName",StudentSelectedName);
				sendBroadcast(broadcastIntent);
			
			/*String input=m_ID_edit.getText().toString();
			String input2=m_Pwd_edit.getText().toString();*/
				PDFLayoutView.WordNum = stu;
				PDFLayoutView.WordNum_btn = false;
				Intent intent = new Intent(this, PDFNavAct.class);
				startActivity(intent);
			} else if (arg0 == m_btn_asset) {
				Intent intent = new Intent(this, PDFViewAct.class);
				intent.putExtra("PDFAsset", "test.PDF");
				intent.putExtra("PDFPswd", "");//password
				PDFLayoutView.WordNum = stu;
				PDFLayoutView.WordNum_btn = false;
				startActivity(intent);
			} else if (arg0 == m_btn_sdcard) {
				Intent intent = new Intent(this, PDFViewAct.class);
				String pdf_path = "/sdcard/test.pdf";
				File file = new File(pdf_path);
				PDFLayoutView.WordNum_btn = false;
				PDFLayoutView.WordNum = stu;
				if (file.exists()) {
					intent.putExtra("PDFPath", pdf_path);
					intent.putExtra("PDFPswd", "");//password
					startActivity(intent);
				} else {
					Toast.makeText(this, "file not exists:" + pdf_path, Toast.LENGTH_SHORT).show();
				}
				//TODO 查看分享
			} else if (arg0 == m_btn_http) {
				spinner_std_word = spinner_std.getSelectedItem().toString();
				spinner_work_word = spinner_work.getSelectedItem().toString();
				Intent intent = new Intent(this, PDFViewAct.class);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());
				PDFLayoutView.WordNum = spinner_std_word;
				PDFLayoutView.WordNum_btn = true;
				//PDFLayoutView.WordNum2 = stu;
				stu =Name.getText().toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date = sdf.format(new java.util.Date());
				String http_link = Link;
				String strtime = formatter.format(curDate);
				//上傳到Firebase 紀錄閱讀次數
				mDatabase.child("學生資料").child("學生"+stu+"號").child("觀看分享").child(date).child(spinner_std_word).child("閱讀次數").child(spinner_work_word).push().setValue(1);
				//把查看分享資料廣播過去
				Intent broadcastIntent = new Intent("StudentsName");
				broadcastIntent.putExtra("ShareStudentsName",spinner_std_word);
				broadcastIntent.putExtra("Topic",spinner_work_word);
				broadcastIntent.putExtra("name",Name.getText().toString());
				broadcastIntent.putExtra("StudentSelectedName",StudentSelectedName);
				sendBroadcast(broadcastIntent);
				intent.putExtra("PDFHttp", Link);
				//intent.putExtra("PDFHttp", Link);
				intent.putExtra("PDFPswd", "");//password
				startActivity(intent);
			} else if (arg0 == m_btn_gl) {
				Intent intent = new Intent(this, PDFGLViewAct.class);
				PDFLayoutView.WordNum = stu;
				PDFLayoutView.WordNum_btn = false;
				startActivity(intent);
			} else if (arg0 == m_btn_565) {
				Intent intent = new Intent(this, PDFViewAct.class);
				PDFLayoutView.WordNum = stu;
				PDFLayoutView.WordNum_btn = false;
				intent.putExtra("PDFAsset", "test.PDF");
				intent.putExtra("PDFPswd", "");//password
				intent.putExtra("BMPFormat", "RGB_565");
				startActivity(intent);
			} else if (arg0 == m_btn_4444) {
				Intent intent = new Intent(this, PDFViewAct.class);
				intent.putExtra("PDFAsset", "test.PDF");
				intent.putExtra("PDFPswd", "");//password
				intent.putExtra("BMPFormat", "ARGB_4444");
				startActivity(intent);
			} else if (arg0 == m_btn_curl) {
				Intent intent = new Intent(this, PDFCurlViewAct.class);
				intent.putExtra("PDFAsset", "test.PDF");
				intent.putExtra("PDFPswd", "");//password
				startActivity(intent);
			} else if (arg0 == m_btn_about) {
				Intent intent = new Intent(this, AboutActivity.class);
				startActivity(intent);
			}
		}else {
            Toast.makeText(this,"請輸入姓名",Toast.LENGTH_SHORT).show();
        }
	}

	/*	else if( arg0 == m_btn_dialog)
		{
			Intent intent = new Intent(this, DialogActivity.class);
			startActivity(intent);
		}*/


	public void buttonSampleClick(final View view) {
	    if(!TextUtils.isEmpty(Name.getText().toString())){
			Student.Name = Name.getText().toString();//設定學生名稱
            requestCapturePermission();//詢問截圖權限
        }else {
	        Toast.makeText(this,"請輸入姓名",Toast.LENGTH_SHORT).show();
        }
	}

	//權限視窗
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void requestCapturePermission() {
		mediaProjectionManager = (MediaProjectionManager)
				getSystemService(Context.MEDIA_PROJECTION_SERVICE);
		startActivityForResult(
				mediaProjectionManager.createScreenCaptureIntent(),
				10);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 10:
				if (resultCode == RESULT_OK && data != null) {
					DialogActivity.setResultData(data);
					startActivity(new Intent(this,DialogActivity.class));
				}
				break;
		}
	}
}
