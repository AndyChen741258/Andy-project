package com.naer.pdfreader;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Builder;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionMenu;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.Random;

import Model.DialogPracticBehavior;
import lib.kingja.switchbutton.SwitchMultiButton;

public class MainActivity extends Activity implements OnClickListener {
	private MainActivity context;
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
	private Button btn_conversation;
	private Button drama_video_btn;



	private EditText m_ID_edit;
	private EditText m_Pwd_edit;
	private EditText Name;
	private Spinner spinner_std;
	private Spinner spinner_work;
	public String Link;
	public static String stu = "??????" + Student.Name + "???";
	//private String stu="??????30???";
	private String spinner_std_word;
	private String spinner_work_word;
	public static String StudentSelectedName;
	public static String HomeworkSelected;
	public static final String TAG = "pname";


	private StorageReference mStorageRef;
	private DatabaseReference mDatabase;


	//????????????????????????, ?????????????????????,???????????????
	public static String way_word;
	public static String drama_word_stu;
	public static String drama_word_num;
	public static String character_word;
	StorageReference fire_ph;
	public static String practice_id_key;
	public static DatabaseReference fire_conversation_practice;

	public static boolean isFromMain = false;
	FloatingActionMenu floatingActionMenu;
	FloatingActionButton fab_chatbot;
	FloatingActionButton fab_rank;


	//private GeofencingClient geofencingClient;//////////////

	MediaProjectionManager mediaProjectionManager;
	private DatabaseReference fire_welcome;
	private String[] sssttt = {"a", "b", "c"};
	private String[] st;
	private int a;
	private String welcome;
	private int score=0;
	private int lastScore=0;
	private int best1=0;
	private int best2=0;
	private int best3=0;
	private NotificationManager mNotificationManager;
	private NotificationChannel chnnel;
	private String string = "?????????????????????...?????????";
//	private PowerManager pm;


	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//??????
		String control = "https://firebasestorage.googleapis.com/v0/b/carolvpen.appspot.com/o/apk%2Fcontral_update.xml?alt=media&token=89aaf366-d33f-4088-9e45-300ba4b95fa6";
		//??????
		String test = "https://firebasestorage.googleapis.com/v0/b/carolvpen.appspot.com/o/apk%2Fupdate-changelog.xml?alt=media";
		AppUpdater appUpdater = new AppUpdater(this)
				.setDisplay(Display.SNACKBAR)
				.setDisplay(Display.DIALOG)
				.setDisplay(Display.NOTIFICATION)
				.setUpdateFrom(UpdateFrom.XML)
				.setUpdateXML(test)
				.setTitleOnUpdateAvailable("????????????")
				.setContentOnUpdateAvailable("????????????????????????????????????Update????????????")
				.setTitleOnUpdateNotAvailable("????????????????????????????????????")
				.setContentOnUpdateNotAvailable("????????????????????????????????????????????????!")
				.setButtonUpdate("?????????????")
				.setButtonDismiss("????????????")
				.setButtonDoNotShowAgain("????????????")
				.setIcon(R.drawable.ic_baseline_cloud_download_24) // Notification icon
				.setCancelable(false)
				.showEvery(3); // Dialog could not be dismissable
		appUpdater.start();

		AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
				.setUpdateFrom(UpdateFrom.XML)
				.setUpdateXML("https://firebasestorage.googleapis.com/v0/b/carolvpen.appspot.com/o/apk%2Fupdate-changelog.xml?alt=media")
				.withListener(new AppUpdaterUtils.UpdateListener() {
					@Override
					public void onSuccess(Update update, Boolean isUpdateAvailable) {
						Log.v("Latest Version", update.getLatestVersion());
						Log.v("Latest Version Code", String.valueOf(update.getLatestVersionCode()));
						Log.v("URL", String.valueOf(update.getUrlToDownload()));
						Log.v("Is update available?", Boolean.toString(isUpdateAvailable));
					}

					@Override
					public void onFailed(AppUpdaterError error) {
						Log.v("AppUpdater Error", "Something went wrong");
					}
				});
		appUpdaterUtils.start();
		//plz set this line to Activity in AndroidManifes.xml:
		//    android:configChanges="orientation|keyboardHidden|screenSize"
		//otherwise, APP shall destroy this Activity and re-create a new Activity when rotate.
		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(com.naer.pdfreader.R.layout.activity_main, null);
		Name = layout.findViewById(com.naer.pdfreader.R.id.editName);
		Name.setInputType(EditorInfo.TYPE_CLASS_PHONE);

//		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
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
		btn_conversation = layout.findViewById(R.id.btn_conversation);
		drama_video_btn=layout.findViewById(R.id.drama_video_btn);


		spinner_std = (Spinner) layout.findViewById(com.naer.pdfreader.R.id.spinner_std);
		spinner_work = (Spinner) layout.findViewById(com.naer.pdfreader.R.id.spinner_work);
		Log.i(TAG, "onCreate: license " + this.getPackageName());

		btn_describe.setOnClickListener(this);
		btn_creatdrama.setOnClickListener(this);
		btn_seedrama.setOnClickListener(this);
		btn_hw.setOnClickListener(this);
		btn_review.setOnClickListener(this);
		fab_chatbot= (FloatingActionButton) layout.findViewById(R.id.fab_item1);
		fab_rank = (FloatingActionButton) layout.findViewById(R.id.fab_item2);
		fab_chatbot.setOnClickListener(this);
		fab_rank.setOnClickListener(this);
		drama_video_btn.setOnClickListener(this);
//		chatbot.setOnClickListener(new View.OnClickListener(){
//			@Override
//			public void onClick(View view){
//				Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
//			}
//		});


		btn_conversation.setOnClickListener(this);


		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Global.Init(this);
		FirebaseApp.initializeApp(getApplicationContext());

		mDatabase = FirebaseDatabase.getInstance().getReference();    //?????????FirebaseDatabase
		mStorageRef = FirebaseStorage.getInstance().getReference();    //?????????FirebaseStorage

		final String[] lunch = {"??????1???", "??????3???", "??????4???", "??????5???", "??????6???", "??????7???"
				, "??????8???", "??????10???", "??????11???", "??????12???", "??????13???"
				, "??????14???", "??????15???", "??????16???", "??????31???", "??????32???", "??????34???"
				, "??????35???", "??????36???", "??????37???", "??????38???", "??????39???", "??????40???"
				, "??????41???", "??????43???", "??????44???", "??????111???"};
		//final String[] lunch2 = {"?????????", "?????????", "?????????",
		//	"?????????", "?????????", "?????????", "?????????"};
		final String[] lunch2 = {"L5-Dialog", "L5-practice", "???????????????", "??????????????? ", "???????????????", "????????????1",
				"????????????2", "????????????3"};

		mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		chnnel=new NotificationChannel("ID","notification_text_a",NotificationManager.IMPORTANCE_HIGH);
		mNotificationManager.createNotificationChannel(chnnel);
		Name.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					Student.Name=Name.getText().toString();
//					handler.post(runnable);
				}
				return false;
			}
		});


		ArrayAdapter<String> lunchList = new ArrayAdapter<String>(MainActivity.this,
				android.R.layout.simple_spinner_dropdown_item,
				lunch);
		spinner_std.setAdapter(lunchList);
		spinner_std.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//????????????Spinner????????????
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				spinner_std_word = spinner_std.getSelectedItem().toString();
//				Toast.makeText(MainActivity.this, spinner_std_word, Toast.LENGTH_SHORT).show();
				StudentSelectedName = lunch[position];//??????????????????????????????
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
			public void onNothingSelected(AdapterView<?> adapterView) {//????????????
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
//				Toast.makeText(MainActivity.this, "??????" + lunch2[position], Toast.LENGTH_SHORT).show();
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

		fire_welcome = FirebaseDatabase.getInstance().getReference();
		fire_welcome.child("Other").child("welcome").addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				int i = 0;
				sssttt = new String[(int) dataSnapshot.getChildrenCount()];
				for (DataSnapshot each : dataSnapshot.getChildren()) {
					sssttt[i] = each.getValue().toString();
//                            Log.v("GET",sssttt[i]);
					i++;
				}
				st = sssttt;

				Random x = new Random();
				a = x.nextInt(st.length);
				Log.v("Get_main", st[a]);
				welcome = st[a];
				fab_chatbot.setLabelText(welcome);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			}
		});



		//geofencingClient = LocationServices.getGeofencingClient(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//?????????????????????????????????????????????????????????
		if(keyCode == KeyEvent.KEYCODE_BACK){
			new AlertDialog.Builder(MainActivity.this)
					.setIcon(R.drawable.ic_launcher)
					.setTitle("?????????????")
					.setPositiveButton("??????", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
//							handler.removeCallbacks(runnable);
						}
					});

		};
		return super.onKeyDown(keyCode, event);
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			handler.postDelayed(this,  21600000);
			handler.sendEmptyMessage(1);
		}
	};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
//			if(msg.what == 1 && !pm.isInteractive())
			if(msg.what == 1){
				java.util.Date date = new java.util.Date();
				SimpleDateFormat df = new SimpleDateFormat("HH");
				SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
				String str = df.format(date);
				String str_date = df_date.format(date);
				final int a = Integer.parseInt(str);
				Intent intent = new Intent(MainActivity.this,ChatbotActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				PendingIntent PI = PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
				final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
				firebaseDatabase.child("??????" + Name.getText().toString()+ "???").child("Chatbot data").child("Chatbotsay").orderByKey().limitToLast(1)
						.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						if(dataSnapshot.getValue().toString().equals("") == false){
							string = dataSnapshot.getValue().toString().trim().replaceAll("__",",");
							int i = string.indexOf("=");
							string = string.substring(i+1,string.length()-1);
							//??????
							if (a > 8 && a <= 12 && !string.equals("?????????????????????...?????????")) {
								Notification.Builder builder = new Notification.Builder(MainActivity.this);
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
									builder.setSmallIcon(R.drawable.ic_launcher)
											.setChannelId("ID")
											.setContentIntent(PI)
											.setAutoCancel(true)
											.setContentTitle("????????????")
											.setSubText("????????????:\t"+str_date)
											.setContentText("???????????????:\t"+string);
								}
								Notification notification = builder.build();
								mNotificationManager.notify(0,notification);
							}
							//??????
							else if (a > 12 && a <= 13 && !string.equals("?????????????????????...?????????")) {
								Notification.Builder builder = new Notification.Builder(MainActivity.this);
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
									builder.setSmallIcon(R.drawable.ic_launcher)
											.setChannelId("ID")
											.setContentIntent(PI)
											.setAutoCancel(true)
											.setContentTitle("????????????")
											.setSubText("????????????:\t"+str_date)
											.setContentText("???????????????:\t"+string);
								}
								Notification notification = builder.build();
								mNotificationManager.notify(0,notification);
							}
							//??????
							else if (a > 13 && a <= 18 && !string.equals("?????????????????????...?????????")) {
//					String[] welcome = new String[]{"What do you eat in the afternoon?",
//							"What did you do in the afternoon?",
//							"How's the weather in the afternoon?",};
//					Random x = new Random();
//					int welcome_number = x.nextInt(welcome.length);
								Notification.Builder builder = new Notification.Builder(MainActivity.this);
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
									builder.setSmallIcon(R.drawable.ic_launcher)
											.setChannelId("ID")
											.setContentIntent(PI)
											.setAutoCancel(true)
											.setContentTitle("????????????")
											.setSubText("????????????:\t"+str_date)
											.setContentText("???????????????:\t"+string);
								}
								Notification notification = builder.build();
								mNotificationManager.notify(0,notification);
							}
							//??????
							else if (a > 18 && a <= 24 && !string.equals("?????????????????????...?????????")) {
//					Log.v("??????","??????");
//					String[] welcome = new String[]{"What do you eat in the night?",
//							"What did you do in the night?",
//							"How's the weather in the night?",};
//					Random x = new Random();
//					int welcome_number = x.nextInt(welcome.length);
								Notification.Builder builder = new Notification.Builder(MainActivity.this);
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
									builder.setSmallIcon(R.drawable.ic_launcher)
											.setChannelId("ID")
											.setContentIntent(PI)
											.setAutoCancel(true)
											.setContentTitle("????????????")
											.setSubText("????????????:\t"+str_date)
											.setContentText("???????????????:\t"+string);
								}
								Notification notification = builder.build();
								mNotificationManager.notify(0,notification);
							}else{
								Notification.Builder builder = new Notification.Builder(MainActivity.this);
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
									builder.setSmallIcon(R.drawable.ic_launcher)
											.setChannelId("ID")
											.setContentIntent(PI)
											.setAutoCancel(true)
											.setContentTitle("????????????")
											.setSubText("????????????:\t"+str_date)
											.setContentText("What do you do?");
								}
								Notification notification = builder.build();
								mNotificationManager.notify(0,notification);
							}
						}
					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {
						string = "?????????????????????...?????????";
					}
				});

			}
			super.handleMessage(msg);
		}
	};



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
				//??????????????????????????????????????????????????????????????????????????????
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
				//TODO ????????????
			} else if (arg0 == m_btn_http) {
				spinner_std_word = spinner_std.getSelectedItem().toString();
				spinner_work_word = spinner_work.getSelectedItem().toString();
				Intent intent = new Intent(this, PDFViewAct.class);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy???MM???dd???HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());
				PDFLayoutView.WordNum = spinner_std_word;
				PDFLayoutView.WordNum_btn = true;
				//PDFLayoutView.WordNum2 = stu;
				stu = Name.getText().toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date = sdf.format(new java.util.Date());
				String http_link = Link;
				String strtime = formatter.format(curDate);
				//?????????Firebase ??????????????????
				mDatabase.child("????????????").child("??????" + stu + "???").child("????????????").child(date).child(spinner_std_word).child("????????????").child(spinner_work_word).push().setValue(1);
				//?????????????????????????????????
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
				Intent intent = new Intent(this, Leaderboard.class);
				startActivity(intent);
//				Student.Name = Name.getText().toString();
//				Intent intent = new Intent(this, ConversationHomework.class);
//				startActivity(intent);
			} else if (arg0 == btn_review) {
				Student.Name = Name.getText().toString();
				isFromMain = true;
				Intent intent = new Intent(this, DramaRecycleView.class);
				startActivity(intent);
			} else if (arg0 == btn_conversation) {
				Student.Name = Name.getText().toString();
				Intent intent = new Intent(this, ChatbotActivity.class);
				startActivity(intent);
			} else if (arg0 == fab_chatbot) {
				Student.Name = Name.getText().toString();
				Intent intent = new Intent(this, ChatbotActivity.class);
				startActivity(intent);
			} else if (arg0 == fab_rank) {
				Student.Name = Name.getText().toString();
				Intent intent = new Intent(this, Leaderboard.class);
				startActivity(intent);
			} else if (arg0 == drama_video_btn) {
				Student.Name = Name.getText().toString();
				Intent intent = new Intent(this, activity_drama_video.class);
				startActivity(intent);
			} else {
				Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
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

		//???????????????,?????????????????????????????????
//	public void conversationClick(final View view) {
//		if (!TextUtils.isEmpty(Name.getText().toString())) {
//			Student.Name = Name.getText().toString();//??????????????????
//			LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
//			final View v = inflater.inflate(R.layout.activity_choose_practicr_part, null);
////			final TextView way_text = v.findViewById(R.id.way_text);
////			final Spinner practice_way = v.findViewById(R.id.practice_way);
////			final Spinner practice_drama_num = v.findViewById(R.id.practice_drama_num);
//			final SwitchMultiButton switchMultiButton2 = v.findViewById(R.id.switchmultibutton2);
//			final SwitchMultiButton switchMultiButton3 = v.findViewById(R.id.switchmultibutton3);
//			final TextView drama_text = v.findViewById(R.id.drama_text);
//			final Spinner practice_drama_stu = v.findViewById(R.id.practice_drama_stu);
//			final TextView character_text = v.findViewById(R.id.character_text);
//			final Spinner practice_character = v.findViewById(R.id.practice_character);
//			final ImageView choose_drama_photo = v.findViewById(R.id.choose_drama_photo);
//
//			drama_text.setVisibility(View.INVISIBLE);
//			switchMultiButton3.setVisibility(View.INVISIBLE);
//			practice_drama_stu.setVisibility(View.INVISIBLE);
//			character_text.setVisibility(View.INVISIBLE);
//			practice_character.setVisibility(View.INVISIBLE);
//			choose_drama_photo.setVisibility(View.INVISIBLE);
//
//
//			final AlertDialog conversation_choose = new AlertDialog.Builder(MainActivity.this)
//					.setTitle("??????????????????")
//					.setView(v)
//					.setPositiveButton("??????", null)
//					.setNegativeButton("??????", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//						}
//					})
//					.show();
//
//			//--------??????????????????----------
//			switchMultiButton2.setText("??????????????????", "??????????????????").setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
//				@Override
//				public void onSwitch(int position, String tabText) {
//					way_word = tabText;
//					if (way_word.equals("??????????????????")) {
//						drama_text.setVisibility(View.VISIBLE);
//						practice_drama_stu.setVisibility(View.VISIBLE);
//						switchMultiButton3.setVisibility(View.VISIBLE);
//						choose_drama_photo.setVisibility(View.VISIBLE);
//					} else {
//						drama_text.setVisibility(View.INVISIBLE);
//						practice_drama_stu.setVisibility(View.INVISIBLE);
//						switchMultiButton3.setVisibility(View.INVISIBLE);
//						choose_drama_photo.setVisibility(View.INVISIBLE);
//
//					}
//					Toast.makeText(MainActivity.this, tabText, Toast.LENGTH_SHORT).show();
//				}
//			});
////			ArrayAdapter<String> way = new ArrayAdapter<String>(MainActivity.this,
////					android.R.layout.simple_spinner_dropdown_item,
////					way_list);
////			practice_way.setAdapter(way);
////			practice_way.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//????????????Spinner????????????
////				@Override
////				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////					way_word = practice_way.getSelectedItem().toString();
////					if (way_word.equals("??????????????????")) {
////						drama_text.setVisibility(View.VISIBLE);
////						practice_drama_stu.setVisibility(View.VISIBLE);
////						practice_drama_num.setVisibility(View.VISIBLE);
////						choose_drama_photo.setVisibility(View.VISIBLE);
////					} else {
////						drama_text.setVisibility(View.INVISIBLE);
////						practice_drama_stu.setVisibility(View.INVISIBLE);
////						practice_drama_num.setVisibility(View.INVISIBLE);
////						choose_drama_photo.setVisibility(View.INVISIBLE);
////
////					}
////
////				}
////
////				@Override
////				public void onNothingSelected(AdapterView<?> adapterView) {//????????????
////					way_word = practice_way.getSelectedItem().toString();
////				}
////			});
//
//			//--------??????????????????----------
//			final String[] drama_list1 = {"Student Number", "111", "01", "4", "06", "10", "32", "36", "40", "43"};
//			ArrayAdapter<String> drama1 = new ArrayAdapter<String>(MainActivity.this,
//					android.R.layout.simple_spinner_dropdown_item,
//					drama_list1);
//			practice_drama_stu.setAdapter(drama1);
//			practice_drama_stu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//????????????Spinner????????????
//				@Override
//				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//					drama_word_stu = practice_drama_stu.getSelectedItem().toString();
//					character_text.setVisibility(View.VISIBLE);
//					practice_character.setVisibility(View.VISIBLE);
//				}
//
//				@Override
//				public void onNothingSelected(AdapterView<?> adapterView) {//????????????
//					drama_word_stu = practice_drama_stu.getSelectedItem().toString();
//				}
//			});
//
//			switchMultiButton3.setText("Drama_1","Drama_2","Drama_3").setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
//				@Override
//				public void onSwitch(int position, String tabText) {
//					drama_word_num = tabText;
//					character_text.setVisibility(View.VISIBLE);
//					practice_character.setVisibility(View.VISIBLE);
//					if ((!drama_word_stu.equals("Student Number")) && way_word.equals("??????????????????")) {
//						fire_ph = FirebaseStorage.getInstance().getReference().child(drama_word_stu).child("Four-frame").child(drama_word_num);
//						fire_ph.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//							@Override
//							public void onComplete(@NonNull Task<Uri> task) {
//								Uri uri = task.getResult();
//								Glide.with(choose_drama_photo.getContext()).load(uri).into(choose_drama_photo);
//								choose_drama_photo.setVisibility(View.VISIBLE);
//							}
//						});
//					} else {
//						Toast.makeText(MainActivity.this, "?????????????????????????????????!", Toast.LENGTH_SHORT).show();
//					}
//					Toast.makeText(MainActivity.this, tabText, Toast.LENGTH_SHORT).show();
//				}
//			});
//
////			final String[] drama_list2 = {"Drama_1", "Drama_2", "Drama_3"};
////			ArrayAdapter<String> drama2 = new ArrayAdapter<String>(MainActivity.this,
////					android.R.layout.simple_spinner_dropdown_item,
////					drama_list2);
////			practice_drama_num.setAdapter(drama2);
////			practice_drama_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//????????????Spinner????????????
////				@Override
////				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////					drama_word_num = practice_drama_num.getSelectedItem().toString();
////					character_text.setVisibility(View.VISIBLE);
////					practice_character.setVisibility(View.VISIBLE);
////					if ((!drama_word_stu.equals("Student Number")) && way_word.equals("??????????????????")) {
////						fire_ph = FirebaseStorage.getInstance().getReference().child(drama_word_stu).child("Four-frame").child(drama_word_num);
////						fire_ph.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
////							@Override
////							public void onComplete(@NonNull Task<Uri> task) {
////								Uri uri = task.getResult();
////								Glide.with(choose_drama_photo.getContext()).load(uri).into(choose_drama_photo);
////								choose_drama_photo.setVisibility(View.VISIBLE);
////							}
////						});
////					} else {
////						Toast.makeText(MainActivity.this, "?????????????????????????????????!", Toast.LENGTH_SHORT).show();
////					}
////				}
////
////				@Override
////				public void onNothingSelected(AdapterView<?> adapterView) {//????????????
////					drama_word_num = practice_drama_num.getSelectedItem().toString();
////				}
////			});
//
//			//--------????????????----------
//			final String[] character_list = {"A", "B"};
//			ArrayAdapter<String> character = new ArrayAdapter<String>(MainActivity.this,
//					android.R.layout.simple_spinner_dropdown_item,
//					character_list);
//			practice_character.setAdapter(character);
//			practice_character.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//????????????Spinner????????????
//				@Override
//				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//					character_word = practice_character.getSelectedItem().toString();
//				}
//
//				@Override
//				public void onNothingSelected(AdapterView<?> adapterView) {//????????????
//					character_word = practice_character.getSelectedItem().toString();
//				}
//			});
//
//			conversation_choose.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if ((!drama_word_stu.equals("Student Number")) && way_word.equals("??????????????????")) {
//						Intent intent = new Intent();
//						intent.setClass(MainActivity.this, ConversationPractice.class);
//						startActivity(intent);
//
//						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//						String date = sdf.format(new java.util.Date());
//						fire_conversation_practice = FirebaseDatabase.getInstance().getReference().child("??????" + Student.Name + "???").child("ConversationPractice");
//						practice_id_key = fire_conversation_practice.push().getKey();
//						DialogPracticBehavior dialogPracticBehavior = new DialogPracticBehavior(date, way_word, drama_word_stu + "_" + drama_word_num,
//								character_word, "null", "null", 0, "null", "null", 0,
//								"null", "null", 0, "null", "null", 0);
//						fire_conversation_practice.child(practice_id_key).child("Content").setValue(dialogPracticBehavior);
//					} else {
//						Toast.makeText(MainActivity.this, "?????????????????????????????????!", Toast.LENGTH_SHORT).show();
//					}
//
//
//				}
//			});
//
//		} else {
//			Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
//		}
//
//	}

	}

	public void buttonSampleClick(final View view) {
		if (!TextUtils.isEmpty(Name.getText().toString())) {
			Student.Name = Name.getText().toString();//??????????????????
			requestCapturePermission();//??????????????????
		} else {
			Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
		}
	}

	//????????????
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


