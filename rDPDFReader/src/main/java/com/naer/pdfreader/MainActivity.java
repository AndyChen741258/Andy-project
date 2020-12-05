package com.naer.pdfreader;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.radaee.pdf.Global;
import com.radaee.reader.PDFCurlViewAct;
import com.radaee.reader.PDFLayoutView;
import com.radaee.reader.PDFNavAct;
import com.radaee.reader.PDFViewAct;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

import Model.DialogPracticBehavior;

public class MainActivity extends Activity implements OnClickListener {
	private Button m_btn_file;
	private Button m_btn_asset;
	private Button m_btn_sdcard;
	private Button m_btn_http;
	private Button m_btn_gl;
	private Button m_btn_565;
	private Button m_btn_4444;
	private Button m_btn_curl;
	private Button m_btn_about;
	private Button btn_describe;
	private Button btn_creatdrama;
	private Button btn_seedrama;
	private Button btn_hw;
	private Button btn_review;
//	private Button btn_conversation;

	private EditText m_ID_edit;
	private EditText m_Pwd_edit;
	private EditText Name;
	private Spinner spinner_std;
	private Spinner spinner_work;
	public String Link;
	public static String stu = "學生" + Student.Name + "號";
	//private String stu="學生30號";
	private String spinner_std_word;
	private String spinner_work_word;
	public static String StudentSelectedName;
	public static String HomeworkSelected;
	public static final String TAG = "pname";


	private StorageReference mStorageRef;
	private DatabaseReference mDatabase;


	//當按下對話練習時, 跳出視窗做選擇,選擇的文字
	public static String way_word;
	public static String drama_word_stu;
	public static String drama_word_num;
	public static String character_word;
	StorageReference fire_ph;
	public static String practice_id_key;
	public static DatabaseReference fire_conversation_practice;

	public static boolean isFromMain = false;
	FloatingActionMenu floatingActionMenu;
	FloatingActionButton chatbot;


	//private GeofencingClient geofencingClient;//////////////

	MediaProjectionManager mediaProjectionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//plz set this line to Activity in AndroidManifes.xml:
		//    android:configChanges="orientation|keyboardHidden|screenSize"
		//otherwise, APP shall destroy this Activity and re-create a new Activity when rotate.
		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(com.naer.pdfreader.R.layout.activity_main, null);
		Name = layout.findViewById(com.naer.pdfreader.R.id.editName);
		Name.setInputType(EditorInfo.TYPE_CLASS_PHONE);


		floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
		m_btn_file = layout.findViewById(com.naer.pdfreader.R.id.btn_file);
		m_btn_asset = layout.findViewById(com.naer.pdfreader.R.id.btn_asset);
		m_btn_sdcard = (Button) layout.findViewById(com.naer.pdfreader.R.id.btn_sdcard);
		m_btn_http = (Button) layout.findViewById(com.naer.pdfreader.R.id.btn_http);
		m_btn_gl = (Button) layout.findViewById(com.naer.pdfreader.R.id.btn_gl);
		m_btn_565 = (Button) layout.findViewById(com.naer.pdfreader.R.id.btn_565);
		m_btn_4444 = (Button) layout.findViewById(com.naer.pdfreader.R.id.btn_4444);
		m_btn_curl = (Button) layout.findViewById(com.naer.pdfreader.R.id.btn_curl);
		m_btn_about = (Button) layout.findViewById(com.naer.pdfreader.R.id.btn_about);
		m_ID_edit = (EditText) layout.findViewById(com.naer.pdfreader.R.id.Pwdedit);
		m_btn_file.setOnClickListener(this);
		m_btn_asset.setOnClickListener(this);
		m_btn_sdcard.setOnClickListener(this);
		m_btn_http.setOnClickListener(this);
		m_btn_gl.setOnClickListener(this);
		m_btn_565.setOnClickListener(this);
		m_btn_4444.setOnClickListener(this);
		m_btn_curl.setOnClickListener(this);
		m_btn_about.setOnClickListener(this);

		btn_describe = layout.findViewById(R.id.btn_describe);
		btn_creatdrama = layout.findViewById(R.id.btn_creatdrama);
		btn_seedrama = layout.findViewById(R.id.btn_seedrama);
		btn_hw = layout.findViewById(R.id.btn_hw);
		btn_review = layout.findViewById(R.id.btn_review);
//		btn_conversation = layout.findViewById(R.id.btn_conversation);

		spinner_std = (Spinner) layout.findViewById(com.naer.pdfreader.R.id.spinner_std);
		spinner_work = (Spinner) layout.findViewById(com.naer.pdfreader.R.id.spinner_work);
		Log.i(TAG, "onCreate: license " + this.getPackageName());

		btn_describe.setOnClickListener(this);
		btn_creatdrama.setOnClickListener(this);
		btn_seedrama.setOnClickListener(this);
		btn_hw.setOnClickListener(this);
		btn_review.setOnClickListener(this);
		chatbot = (FloatingActionButton) layout.findViewById(R.id.fab_item1);
		chatbot.setOnClickListener(this);
//		chatbot.setOnClickListener(new View.OnClickListener(){
//			@Override
//			public void onClick(View view){
//				Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
//			}
//		});


//		btn_conversation.setOnClickListener(this);


		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Global.Init(this);
		FirebaseApp.initializeApp(getApplicationContext());

		mDatabase = FirebaseDatabase.getInstance().getReference();    //初始化FirebaseDatabase
		mStorageRef = FirebaseStorage.getInstance().getReference();    //初始化FirebaseStorage

		final String[] lunch = {"學生1號", "學生3號", "學生4號", "學生5號", "學生6號", "學生7號"
				, "學生8號", "學生10號", "學生11號", "學生12號", "學生13號"
				, "學生14號", "學生15號", "學生16號", "學生31號", "學生32號", "學生34號"
				, "學生35號", "學生36號", "學生37號", "學生38號", "學生39號", "學生40號"
				, "學生41號", "學生43號", "學生44號", "學生111號"};
		//final String[] lunch2 = {"第一課", "第二課", "第三課",
		//	"作業一", "作業二", "作業三", "作業四"};
		final String[] lunch2 = {"L5-Dialog", "L5-practice", "第一次作業", "第二次作業 ", "第三次作業", "戲劇成果1",
				"戲劇成果2", "戲劇成果3"};


		ArrayAdapter<String> lunchList = new ArrayAdapter<String>(MainActivity.this,
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
				mStorageRef.child(spinner_std_word + "/" + spinner_work_word + ".pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
					@Override
					public void onSuccess(Uri uri) {
						Link = uri.toString();
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception exception) {
						Link = "";

					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
				spinner_std_word = spinner_std.getSelectedItem().toString();
			}
		});
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		ArrayAdapter<String> lunchList2 = new ArrayAdapter<String>(MainActivity.this,
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
				mStorageRef.child(spinner_std_word + "/" + spinner_work_word + ".pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
					@Override
					public void onSuccess(Uri uri) {
						Link = uri.toString();
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception exception) {
						Link = "";

					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
				spinner_work_word = spinner_work.getSelectedItem().toString();
			}
		});

		setContentView(layout);

		//geofencingClient = LocationServices.getGeofencingClient(this); ////////////////
	}


	@SuppressLint("InlinedApi")
	@Override
	protected void onDestroy() {
		Global.RemoveTmp();
		super.onDestroy();
	}

	@Override
	public void onClick(View arg0) {
		//spinner_std_word = spinner_std.getSelectedItem().toString();
		//spinner_work_word = spinner_work.getSelectedItem().toString();

		if (!TextUtils.isEmpty(Name.getText().toString())) {

			if (arg0 == m_btn_file) {
				Student.Name = Name.getText().toString();
				//用廣播的方式把資料廣播到另一個模組，才能取得學生名稱
				Intent broadcastIntent = new Intent("StudentsName");
				broadcastIntent.putExtra("ShareStudentsName", "");
				broadcastIntent.putExtra("Topic", "");
				broadcastIntent.putExtra("name", Student.Name);
				broadcastIntent.putExtra("StudentSelectedName", StudentSelectedName);
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
				stu = Name.getText().toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date = sdf.format(new java.util.Date());
				String http_link = Link;
				String strtime = formatter.format(curDate);
				//上傳到Firebase 紀錄閱讀次數
				mDatabase.child("學生資料").child("學生" + stu + "號").child("觀看分享").child(date).child(spinner_std_word).child("閱讀次數").child(spinner_work_word).push().setValue(1);
				//把查看分享資料廣播過去
				Intent broadcastIntent = new Intent("StudentsName");
				broadcastIntent.putExtra("ShareStudentsName", spinner_std_word);
				broadcastIntent.putExtra("Topic", spinner_work_word);
				broadcastIntent.putExtra("name", Name.getText().toString());
				broadcastIntent.putExtra("StudentSelectedName", StudentSelectedName);
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
			} else if (arg0 == btn_describe) {
				Student.Name = Name.getText().toString();
				Intent intent = new Intent(this, DescribeActivity.class);
				startActivity(intent);
			} else if (arg0 == btn_creatdrama) {
				Student.Name = Name.getText().toString();
				Intent intent = new Intent(this, CreatDrama.class);
				startActivity(intent);
			} else if (arg0 == btn_seedrama) {
				Student.Name = Name.getText().toString();
				Intent intent = new Intent(this, DramaAchievement.class);
				startActivity(intent);
			} else if (arg0 == btn_hw) {
				Student.Name = Name.getText().toString();
				Intent intent = new Intent(this, ConversationHomework.class);
				startActivity(intent);
			} else if (arg0 == btn_review) {
				Student.Name = Name.getText().toString();
				isFromMain = true;
				Intent intent = new Intent(this, DramaRecycleView.class);
				startActivity(intent);
			}
			else if (arg0 == chatbot) {
				Student.Name = Name.getText().toString();
				Intent intent = new Intent(this, ChatbotActivity.class);
				startActivity(intent);
			}
		} else {
			Toast.makeText(this, "請輸入座號", Toast.LENGTH_SHORT).show();
		}
	}

//	private void viewTask() {
//
//		Dialog dialog = new Dialog(this);
//		dialog.setContentView(R.layout.activity_chatbot);
//
//		ScrollView view_web = dialog.findViewById(R.id.scrollView);
//		//TextView view_head = dialog.findViewById(R.id.map_txt_head);
//
////		btn_continue.setOnClickListener((View y) -> {
////			dialog.hide();
////			hideSystemUI();
////		});
//
//		//view_head.setText("What is my task ?");
////		view_web.getSettings().setJavaScriptEnabled(true);
////		view_web.getSettings().setAppCacheEnabled(false);
////		view_web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
////		view_web.loadUrl(active_learning_stat_url);
//
//		//dialog
//		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//		dialog.show();
//
//		dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//				| View.SYSTEM_UI_FLAG_FULLSCREEN
//				| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//
//	}

	//按下對話後,進入選單選擇內容腳色等
	public void conversationClick(final View view) {
		if (!TextUtils.isEmpty(Name.getText().toString())) {
			Student.Name = Name.getText().toString();//設定學生名稱
			LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			final View v = inflater.inflate(R.layout.activity_choose_practicr_part, null);
			final TextView way_text = v.findViewById(R.id.way_text);
			final Spinner practice_way = v.findViewById(R.id.practice_way);
			final TextView drama_text = v.findViewById(R.id.drama_text);
			final Spinner practice_drama_stu = v.findViewById(R.id.practice_drama_stu);
			final Spinner practice_drama_num = v.findViewById(R.id.practice_drama_num);
			final TextView character_text = v.findViewById(R.id.character_text);
			final Spinner practice_character = v.findViewById(R.id.practice_character);
			final ImageView choose_drama_photo = v.findViewById(R.id.choose_drama_photo);

			drama_text.setVisibility(View.INVISIBLE);
			practice_drama_num.setVisibility(View.INVISIBLE);
			practice_drama_stu.setVisibility(View.INVISIBLE);
			character_text.setVisibility(View.INVISIBLE);
			practice_character.setVisibility(View.INVISIBLE);
			choose_drama_photo.setVisibility(View.INVISIBLE);


			final AlertDialog conversation_choose = new AlertDialog.Builder(MainActivity.this)
					.setTitle("對話練習選單")
					.setView(v)
					.setPositiveButton("確定", null)
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.show();

			//--------選擇練習模式----------
			final String[] way_list = {"課本對話練習", "劇本對話練習"};
			ArrayAdapter<String> way = new ArrayAdapter<String>(MainActivity.this,
					android.R.layout.simple_spinner_dropdown_item,
					way_list);
			practice_way.setAdapter(way);
			practice_way.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					way_word = practice_way.getSelectedItem().toString();
					if (way_word.equals("劇本對話練習")) {
						drama_text.setVisibility(View.VISIBLE);
						practice_drama_stu.setVisibility(View.VISIBLE);
						practice_drama_num.setVisibility(View.VISIBLE);
						choose_drama_photo.setVisibility(View.VISIBLE);
					} else {
						drama_text.setVisibility(View.INVISIBLE);
						practice_drama_stu.setVisibility(View.INVISIBLE);
						practice_drama_num.setVisibility(View.INVISIBLE);
						choose_drama_photo.setVisibility(View.INVISIBLE);

					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
					way_word = practice_way.getSelectedItem().toString();
				}
			});

			//--------選擇劇本內容----------
			final String[] drama_list1 = {"Student Number", "111", "01", "4", "06", "10", "32", "36", "40", "43"};
			ArrayAdapter<String> drama1 = new ArrayAdapter<String>(MainActivity.this,
					android.R.layout.simple_spinner_dropdown_item,
					drama_list1);
			practice_drama_stu.setAdapter(drama1);
			practice_drama_stu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					drama_word_stu = practice_drama_stu.getSelectedItem().toString();
					character_text.setVisibility(View.VISIBLE);
					practice_character.setVisibility(View.VISIBLE);
				}

				@Override
				public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
					drama_word_stu = practice_drama_stu.getSelectedItem().toString();
				}
			});

			final String[] drama_list2 = {"Drama_1", "Drama_2", "Drama_3"};
			ArrayAdapter<String> drama2 = new ArrayAdapter<String>(MainActivity.this,
					android.R.layout.simple_spinner_dropdown_item,
					drama_list2);
			practice_drama_num.setAdapter(drama2);
			practice_drama_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					drama_word_num = practice_drama_num.getSelectedItem().toString();
					character_text.setVisibility(View.VISIBLE);
					practice_character.setVisibility(View.VISIBLE);
					if ((!drama_word_stu.equals("Student Number")) && way_word.equals("劇本對話練習")) {
						fire_ph = FirebaseStorage.getInstance().getReference().child(drama_word_stu).child("Four-frame").child(drama_word_num);
						fire_ph.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
							@Override
							public void onComplete(@NonNull Task<Uri> task) {
								Uri uri = task.getResult();
								Glide.with(choose_drama_photo.getContext()).load(uri).into(choose_drama_photo);
								choose_drama_photo.setVisibility(View.VISIBLE);
							}
						});
					} else {
						Toast.makeText(MainActivity.this, "你沒有選擇同學的座號哦!", Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
					drama_word_num = practice_drama_num.getSelectedItem().toString();
				}
			});

			//--------選擇腳色----------
			final String[] character_list = {"A", "B"};
			ArrayAdapter<String> character = new ArrayAdapter<String>(MainActivity.this,
					android.R.layout.simple_spinner_dropdown_item,
					character_list);
			practice_character.setAdapter(character);
			practice_character.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					character_word = practice_character.getSelectedItem().toString();
				}

				@Override
				public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
					character_word = practice_character.getSelectedItem().toString();
				}
			});

			conversation_choose.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if ((!drama_word_stu.equals("Student Number")) && way_word.equals("劇本對話練習")) {
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, ConversationPractice.class);
						startActivity(intent);

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String date = sdf.format(new java.util.Date());
						fire_conversation_practice = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("ConversationPractice");
						practice_id_key = fire_conversation_practice.push().getKey();
						DialogPracticBehavior dialogPracticBehavior = new DialogPracticBehavior(date, way_word, drama_word_stu + "_" + drama_word_num,
								character_word, "null", "null", 0, "null", "null", 0,
								"null", "null", 0, "null", "null", 0);
						fire_conversation_practice.child(practice_id_key).child("Content").setValue(dialogPracticBehavior);
					} else {
						Toast.makeText(MainActivity.this, "你沒有選擇要練習誰的哦!", Toast.LENGTH_SHORT).show();
					}


				}
			});

		} else {
			Toast.makeText(this, "請輸入姓名", Toast.LENGTH_SHORT).show();
		}

	}


	public void buttonSampleClick(final View view) {
		if (!TextUtils.isEmpty(Name.getText().toString())) {
			Student.Name = Name.getText().toString();//設定學生名稱
			requestCapturePermission();//詢問截圖權限
		} else {
			Toast.makeText(this, "請輸入姓名", Toast.LENGTH_SHORT).show();
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
					startActivity(new Intent(this, DialogActivity.class));
				}
				break;
		}
	}
}


