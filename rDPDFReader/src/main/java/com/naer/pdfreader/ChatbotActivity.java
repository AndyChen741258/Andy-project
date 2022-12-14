package com.naer.pdfreader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.util.Sleeper;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.Context;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryParameters;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.cloud.translate.Translate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;
import com.orhanobut.logger.Logger;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.UUID;

import pl.droidsonroids.gif.GifImageView;

import static android.view.View.VISIBLE;
import static com.naer.pdfreader.DialogActivity.LOCATION_UPDATE_MIN_DISTANCE;
import static com.naer.pdfreader.DialogActivity.LOCATION_UPDATE_MIN_TIME;

public class ChatbotActivity<MyBinder> extends Activity implements Animation.AnimationListener {

    private static final String LOG_TAG ="";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION =200;
    private ChatbotActivity context;
    private PopupWindow popupWindow = null;
    private Button btnConfirm, btnShow;
    private String TAG = "Louis";
    private Button btn_voice;
    private TextView voice_text;
    private TextView voice_text2;
    private TextView voice_text3;
    private TextView youspeak_text2;
    private TextView youspeak_text3;
    private TextView dialogflowspeak_text;
    private TextView dialogflowspeak_text2;
    private TextView dialogflowspeak_text3;
    private TextView dialogspeak_text2;
    private TextView dialogspeak_text3;
    private TextView dialogspeak_text;
    private TextView trans_correct;
    private TextView trans_speak;
    private GifImageView loading;
    private SpeechRecognizer speechRecognizer;
    private boolean questioning;
    private boolean bol_trans=false;
    private double longitude = 0.0;
    private double latitude = 0.0;
    private String options = "";//for user's choose
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private List<String[]> diffList;
    private String similarity;
    private static Field field;


    //?????????????????????????????????, ????????????????????????????????????????????????
    private boolean isPress = true;
    private boolean inputbool = false;
    private boolean inputbool_2 = false;
    private String queryText = "";
    private String inputText = "";
    private String outputText = "";
    private String outputText2 = "";
    private String uuid = UUID.randomUUID().toString();
    private String intentname;
    public static String placeview;
    private SessionsClient sessionsClient;
    private SessionName session;
    private final int MY_PERMISSIONS_REQUEST = 100;
    private Bundle bundle;
    private TextView textView;
    LinearLayout.LayoutParams textParam;
    private LinearLayout parent;
    LinearLayout.LayoutParams parentParam;
    public static List<AlertDialog> alertDialogList;
    public static List<String> dialogList;
    public static TextView answerTextView, userTextView, wrongTextView;
    public static LinearLayout wrongWord;
    public static ScrollView wrongWordScrollView;
    public static TextView PlaceName;
    private int questionId;
    private Random num;
    int a = 0;
    //    private String welcomeText;
    private DatabaseReference fire_welcome;
    private String[] sssttt = {"a", "b", "c"};
    private String[] st;
    private String welcome;
    private TextView gps_view;
    private String loc_msg;
    private LocationManager mLocationManager;
    private Button btn_recorder;



    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0x001) {
                gps_view.setText(loc_msg);
            }
            return false;
        }
    });

    private LocationListener mLocationListener = new LocationListener() {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onLocationChanged(Location location) {
            updateShow(location);
            final Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm", now);
            if (location != null) {
                last_location = PlaceName.getText().toString().trim();
                longitude = location.getLongitude();   //????????????
                latitude = location.getLatitude();    //????????????
                CheckLocation(latitude, longitude);//???????????????????????????
                Logger.d(String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                Log.d("hooooo", String.valueOf(longitude) + " : " + String.valueOf(latitude));

//                sentencesetOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View view) {
//                        test1.setText(latitude.toString());
//                        test2.setText(longitude.toString());
//                    }
//                });

                //(Carol) drawMarker(location);
                //Update to Firebase
//                geoFire.setLocation(Student.Name + now, new GeoLocation(latitude, longitude),//???????????????firebase???????????????+???????????????
//                       new GeoFire.CompletionListener(){
//                            public void onComplete(String key, DatabaseError error){
//                                //mGoogleMap.setMyLocationEnabled(true);
//                                //Add Marker
//                                if(mCurrent != null)
//                                    mCurrent.remove(); //remove old marker
//                                mCurrent = mGoogleMap.addMarker(new MarkerOptions()
//                                                        .position(new LatLng(latitude, longitude))
//                                                        .title("You"));
//                            }
//                        });


                //  mLocationManager.removeUpdates(mLocationListener);
            } else {
                Logger.d("Place is null");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };
    private TextView textSpeech;
    private String usertext;
    private Translate translate;
    private String translatedText;
    private String trn_originalText;
    private String[] anwerTextArray;
    private Button btn_start;
    private Button btn_stop;
    private Button btn_Notify;
    private long[] vibrate= {0,100,200,300};
    private NotificationChannel chnnel;
    private NotificationManager mNotificationManager;
    private TextView userTextView_left;
    public static LinearLayout wrongWord_left;
    private ScrollView wrongWordScrollView_left;
    private TextView trans_speak_left;
    private String[] anwerTextArray_left;
    private String str2;
    private TextView chinese;
    private String trans_chinese2;
    private ImageView takephoto;
    private ImageView takephoto_right;
    private Bitmap myBitmap;
    private byte[] mContent;
    private Animation animZoomIn;
    private Animation animZoomOut;
    private int pressed=0;
    private int pressed_right=0;
    private MediaPlayer MP;
    private Button btn_photo;
    private int btn_photo_pressed=0;
    private MediaRecorder recorder;
    private int timeCount;

    private String[] data_usersay=new String[1000];
    private String[] data_chatbotsay=new String[1000];
    private int usersaycount=0;
    private int chatbotcount=0;
    private int user=0;
    private int chatbot=0;
    private String[] user_array=new String[1000];
    private String[] chatbot_array=new String[1000];
    private String test="nul";
    private FileOutputStream outputStream;
    private final Date now = new Date();

    private int chatbot_tts_count=0;
    private int chatbot_text_tts_count=0;
    private int record_count=0;
    private int takephoto_count=0;
    private boolean bol_takephoto = false;
    private boolean bol_takephoto_right = false;
    private SimpleDateFormat sdf;
    private ProgressDialog pDialog;
    private String last_location;
    private Boolean bol_lastlocation = false;
    private Button btn_change;
    private boolean bol_english;
    private String dialog;
    private PowerManager pm;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//???????????????
        setContentView(R.layout.activity_chatbot);
        context = this;
//        btnShow = (Button) findViewById(R.id.btn_show);
//        btnShow.setOnClickListener(listener);
//        initPopupWindow();


        dialogList = new ArrayList<String>();
        alertDialogList = new ArrayList<AlertDialog>();
        diffList = new ArrayList<String[]>();

        voice_text = findViewById(R.id.voice_text);
        voice_text2 = findViewById(R.id.voice_text2);
        voice_text3 = findViewById(R.id.voice_text3);
        youspeak_text2 = findViewById(R.id.youspeak_text2);
        youspeak_text3 = findViewById(R.id.youspeak_text3);
        dialogflowspeak_text = findViewById(R.id.dialogspeak_text);
        dialogflowspeak_text2 = findViewById(R.id.dialogflowspeak_text2);
        dialogflowspeak_text3 = findViewById(R.id.dialogflowspeak_text3);
        dialogspeak_text3 = findViewById(R.id.dialogspeak_text3);
        dialogspeak_text2 = findViewById(R.id.dialogspeak_text2);
        dialogspeak_text = findViewById(R.id.dialogspeak_text);
        loading = findViewById(R.id.loading);
        btn_voice = findViewById(R.id.btn_voice);
        btn_change = findViewById(R.id.btn_change);
        bol_english = true;
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bol_english){
                    bol_english = false;
                    Toast.makeText(context,"????????????", Toast.LENGTH_SHORT).show();
                    btn_change.setBackgroundResource(R.drawable.chinese);
                }else{
                    bol_english = true;
                    Toast.makeText(context,"????????????", Toast.LENGTH_SHORT).show();
                    btn_change.setBackgroundResource(R.drawable.english);
                }
            }
        });
//        btn_photo = findViewById(R.id.btn_photo);

//        //??????class????????????
//        if(Student.Name.trim().equals("") || Student.Name.trim()==null){
//            alertdialog_name();
//        }
        // init speechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new MyRecognizerListener());
        TTS.init(getApplicationContext());
        // Java V2
        initV2Chatbot();

        handler.post(runnable_message);
        pm = (PowerManager) getSystemService(android.content.Context.POWER_SERVICE);


        voice_text2.setVisibility(View.INVISIBLE);
        voice_text3.setVisibility(View.INVISIBLE);
        dialogspeak_text2.setVisibility(View.INVISIBLE);
        dialogspeak_text3.setVisibility(View.INVISIBLE);

        linearLayout = findViewById(R.id.linerlayout);
        scrollView = findViewById(R.id.scrollView);

        mLocationManager = (LocationManager) getSystemService(android.content.Context.LOCATION_SERVICE);
        PlaceName = findViewById(R.id.placechatbot);

        gps_view = findViewById(R.id.gps_view);

        textParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parentParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParam.setMargins(0, 0, 0, 15);

        locationUpdate();

        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        chnnel=new  NotificationChannel("ID","notification_text_a",NotificationManager.IMPORTANCE_HIGH);
        mNotificationManager.createNotificationChannel(chnnel);

        
        btn_Notify = findViewById(R.id.btn_Notify);
//        btn_recorder=findViewById(R.id.btn_recorder);

        // load the animation
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out);

        // set animation listener
        animZoomIn.setAnimationListener(this);
        animZoomOut.setAnimationListener(this);

//        RecordView recordView = (RecordView) findViewById(R.id.record_view);
//        final  RecordButton recordButton = (RecordButton) findViewById(R.id.record_button);
//
//        //IMPORTANT
//        recordButton.setRecordView(recordView);

        getchildcount();//??????????????????
        Log.v("test_user", String.valueOf(user));

        //??????????????????(??????)
        sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

        readInfo(Student.Name+"????????????????????????????????????");

        pDialog = new ProgressDialog(ChatbotActivity.this);
        pDialog.setTitle("????????????");
        pDialog.setMessage("?????????... \n??????????????????");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();



//        storageReference = FirebaseDatabase.getInstance().getReference().child("playground").child("sentence");

//        storageReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                 sentence = dataSnapshot.child("1").getValue().toString();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference getContactsRef = database.getReference().child("playground").child("sentence");


//        Query queryRef = getContactsRef;
//        queryRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.v("Get",dataSnapshot.toString());
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



//        btn_Notify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context,ChatbotActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                PendingIntent  PI = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
//
//                Notification.Builder builder = new Notification.Builder(context);
//                builder.setSmallIcon(R.drawable.ic_launcher)
//                        .setChannelId("ID")
//                        .setContentTitle("????????????")
//                        .setContentText("??????????????????:"+PlaceName.getText().toString())
//                        .setContentIntent(PI);
//
//                Notification notification = builder.build();
//                mNotificationManager.notify(0,notification);
//
//            }
//        });
//        btn_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (btn_photo_pressed){
//                    case 0:
//                        takephoto.setVisibility(View.VISIBLE);
//                        btn_photo_pressed=1;
//                        break;
//                    case 1:
//                        takephoto.setVisibility(View.INVISIBLE);
//                        btn_photo_pressed=0;
//                        break;
//                }
//            }
//        });

//        takephoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (pressed){
//                    case 0:
//                        takephoto.startAnimation(animZoomIn);
//                        pressed=1;
//                        break;
//                    case 1:
//                        takephoto.startAnimation(animZoomOut);
//                        pressed=0;
//                        break;
//                }
//            }
//        });
//
//
//        takephoto.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //?????????
//                    if (ContextCompat.checkSelfPermission(ChatbotActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(ChatbotActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                    } else {
//                        BringImagePicker();
//                    }
//                } else {  //??????
//                    BringImagePicker();
//                }
//                return false;
//            }
//        });

//        recordView.setOnRecordListener(new OnRecordListener() {
//            @Override
//            public void onStart() {
//                //Start Recording..
//                // ????????????
//                try {
//                    MR = new MediaRecorder();
//                    MR.setAudioSource(MediaRecorder.AudioSource.MIC);
//                    MR.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
//                    MR.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//                    // ?????????????????????????????????????????????????????????????????????????????????
//                    RF = File.createTempFile("raw", ".amr", Environment.getExternalStorageDirectory());
//                    MR.setOutputFile(RF.getAbsolutePath());
//                    MR.prepare();
//                    MR.start();
//                    Toast.makeText(context,RF.getAbsolutePath().toString(),Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    Toast.makeText(context,"FileIOException", Toast.LENGTH_SHORT).show();
//                    MR.stop();
//                    MR.release();
//                    e.printStackTrace();
//                }
//
//                Log.d("RecordView", "onStart");
//            }
//
//            @Override
//            public void onCancel() {
//                //On Swipe To Cancel
//                Log.d("RecordView", "onCancel");
//
//            }
//
//            @Override
//            public void onFinish(long recordTime) {
//                //Stop Recording..
//                String time = getHumanTimeText(recordTime);
//                if (MR != null) {
//                    MR.stop();
//                    MR.release();
//                    MR = null;
//                }
//                Log.d("RecordView", "onFinish");
//                Log.d("RecordTime", time);
//            }
//
//            @Override
//            public void onLessThanSecond() {
//                //When the record time is less than One Second
//                Log.d("RecordView", "onLessThanSecond");
//            }
//        });
//
//        btn_recorder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                Uri uri = Uri.fromFile(RF.getAbsoluteFile());
//                MP = MediaPlayer.create(context, uri);
//                MP.start();
//                MP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer MP) {
//                        MP.release();
//                    }
//                });
//                }catch (Exception e){
//                    Toast.makeText(context,"????????????", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//        recordButton.setOnRecordClickListener(new OnRecordClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ChatbotActivity.this, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show();
//                Log.d("RecordButton","RECORD BUTTON CLICKED");
//            }
//        });
//
//        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
//            @Override
//            public void onAnimationEnd() {
//                Log.d("RecordView", "Basket Animation Finished");
//            }
//        });
//
//        recordView.setCancelBounds(8);
//
//        recordView.setSoundEnabled(false);
//
//        recordView.setSmallMicColor(Color.parseColor("#c2185b"));
//
//        recordView.setSlideToCancelText("Slide To Cancel");
//
//        //prevent recording under one Second (it's false by default)
//        recordView.setLessThanSecondAllowed(false);
//
//        //change slide To Cancel Text Color
//        recordView.setSlideToCancelTextColor(Color.parseColor("#ff0000"));
//        //change slide To Cancel Arrow Color
//        recordView.setSlideToCancelArrowColor(Color.parseColor("#ff0000"));
//        //change Counter Time (Chronometer) color
//        recordView.setCounterTimeColor(Color.parseColor("#ff0000"));

//        btn_recorder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
//                int color = getResources().getColor(R.color.colorPrimaryDark);
//                int requestCode = 0;
//                AndroidAudioRecorder.with(ChatbotActivity.this)
//                        // Required
//                        .setFilePath(filePath)
//                        .setColor(color)
//                        .setRequestCode(requestCode)
//
//                        // Optional
//                        .setSource(AudioSource.MIC)
//                        .setChannel(AudioChannel.STEREO)
//                        .setSampleRate(AudioSampleRate.HZ_48000)
//                        .setAutoStart(true)
//                        .setKeepDisplayOn(true)
//
//                        // Start recording
//                        .record();
//            }
//        });


    }



//    private String getHumanTimeText(long milliseconds) {
//        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(milliseconds), TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
//    }





    private void BringImagePicker() {
        CropImage.activity().
                setGuidelines(CropImageView.Guidelines.ON).
                setAspectRatio(12,12).
                start(ChatbotActivity.this);
    }


//    public void random() {
//        Random x = new Random();
//        a=x.nextInt(12);
//       if("playground" == placeview) {
//            String [] playground = new String[] {"1","2","3","4","5"};
//            welcomeText = playground[a];
//        }
//        else {
//          welcomeText = ("GPS loading");
//            String [] hello = new String[] {"Hello,What kind of balls are there?","Hey,What plants are here?","Hi,How is the weather today?", "Who are in the family?","What did you see in supermarket?"};
//            welcomeText = hello[a];
//        }


//    private void initPopupWindow() {
//
//        View view = LayoutInflater.from(context).inflate(R.layout.activity_chatbot, null);
//        popupWindow = new PopupWindow(view);
//        int  width = getWindowManager().getDefaultDisplay().getWidth();
//        popupWindow.setWidth(width*3/4);
//        int height = getWindowManager().getDefaultDisplay().getHeight();
//        popupWindow.setHeight(height*3/4);
//        popupWindow.setFocusable(false);
//        btnConfirm = (Button) view.findViewById(R.id.btn_Conform);
//        btnConfirm.setOnClickListener(listener);
//
//
//
//    }



//    public View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//
//            switch (view.getId()) {
//                case R.id.btn_show:
//                    popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
//                    break;
//
//                case R.id.btn_Conform:
//                    popupWindow.dismiss();
//                    break;
//
//            }
//        }
//    };


    @Override
    protected void onDestroy() {
        speechRecognizer.stopListening();
        speechRecognizer.destroy();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final String pathword_img = "Image_" + Student.Name + "_" + trn_originalText + ".jpg";
        final String pathword_right_img = "Right_Image_" + Student.Name + "_" + usertext + ".jpg";
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if(bol_takephoto==true && bol_takephoto_right ==false){
                    Uri imageUri=result.getUri();
                    Log.v("????????????", imageUri.toString());
                    takephoto.setImageURI(imageUri);
                    File publicPicFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File customCreateFolder = new File(publicPicFolder, "MyPicFolder");
                    customCreateFolder.mkdirs();
                    File save_image = new File(customCreateFolder,pathword_img);
                    FileOutputStream fos;
                    takephoto.setDrawingCacheEnabled(true);
                    Bitmap bmp = takephoto.getDrawingCache();
                    try{
                        save_image.createNewFile();
                        fos = new FileOutputStream(save_image);
                        bmp.compress(Bitmap.CompressFormat.JPEG,100,fos);
                        fos.flush();
                        fos.close();
                        try {
                            //??????????????????????????????
                            String date = sdf.format(new java.util.Date());
                            DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                    .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("Chatbot").child("???????????????????????????");
                            fire_60sec_student_data.child(date).child("Dialog Text").setValue(userTextView_left.getText().toString());
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(ChatbotActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                        Log.v("????????????",getFilesDir().getPath());
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                if(bol_takephoto==false && bol_takephoto_right == true){
                    Uri imageUri=result.getUri();
                    Log.v("????????????", imageUri.toString());
                    takephoto_right.setImageURI(imageUri);
                    File publicPicFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File customCreateFolder = new File(publicPicFolder, "MyPicFolder");
                    customCreateFolder.mkdirs();
                    File save_image = new File(customCreateFolder,pathword_right_img);
                    FileOutputStream fos;
                    takephoto_right.setDrawingCacheEnabled(true);
                    Bitmap bmp = takephoto_right.getDrawingCache();
                    try{
                        save_image.createNewFile();
                        fos = new FileOutputStream(save_image);
                        bmp.compress(Bitmap.CompressFormat.JPEG,100,fos);
                        fos.flush();
                        fos.close();
                        try {
                            //??????????????????????????????
                            String date = sdf.format(new java.util.Date());
                            DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                    .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("Chatbot").child("");
                            fire_60sec_student_data.child(date).child("Dialog Text").setValue(answerTextView.getText().toString());
                            fire_60sec_student_data.child(date).child("User say text").setValue(usertext);
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(ChatbotActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                        Log.v("????????????",getFilesDir().getPath());
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }


//                try {
//                    MediaStore.Images.Media.insertImage(getContentResolver(),imageUri.toString(), "null",null);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://")));

            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

//        if (requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//                // Great! User has recorded and saved the audio file
//            } else if (resultCode == RESULT_CANCELED) {
//                // Oops! User has canceled the recording
//            }
//        }
    }




    public void startRecord(View view) {
//        int record_audio_permission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);        // ???????????????
//        int read_external_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE); // ????????????
//        if (record_audio_permission != PackageManager.PERMISSION_GRANTED || read_external_storage != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
//        }
//        else{
        loading.setVisibility(View.VISIBLE);
        Log.d(TAG, "startRecord: ");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        if(bol_english){
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        }else{
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh-TW");
        }
        speechRecognizer.startListening(intent);
//        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation
        // check for zoom in animation
        if (animation == animZoomIn) {
        }else if(animation == animZoomOut){

        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    class MyRecognizerListener implements RecognitionListener {

        @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {
            Log.d(TAG, "Recognizer Error Code: " + i);
            if (i == 8) {
                speechRecognizer.cancel();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                if(bol_english){
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                }else{
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh-TW");
                }
                speechRecognizer.startListening(intent);
            }
            if (i == 7) {
                loading.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onResults(Bundle bundle) {
            // process user's text
            loading.setVisibility(View.INVISIBLE);
            ArrayList resultList = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            int score = (int) (bundle.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)[0] * 100);
            queryText = resultList.get(0).toString();
//            try{
//                final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference db=database.getReference().child("??????" + Student.Name + "???");
//                db.child("Chatbot data").child("Usersay").child(String.valueOf(user_speak)).setValue(queryText.replace(",","__"));
//            }catch (Exception e){
//                e.printStackTrace();
//                Toast.makeText(ChatbotActivity.this,"???????????????????????????",Toast.LENGTH_SHORT).show();
//            }
            Log.d(TAG, "Recognizer onResults: " + queryText);

            if(queryText != null){
                usersaycount++;
                data_usersay[usersaycount]=queryText.replaceAll(",","@");
            }
           /* if(inputbool==false){
                inputText = "";
                youspeak_text2.setText("");
                dialogflowspeak_text2.setText("");
                inputbool=true;
            }else {
                voice_text2.setVisibility(View.VISIBLE);
                youspeak_text2.setText("You");
                youspeak_text2.setTextSize(50);
                dialogflowspeak_text2.setText("Dialog");
                dialogspeak_text2.setVisibility(View.VISIBLE);
                dialogspeak_text2.setText(dialogspeak_text.getText().toString());
                if (inputbool_2 == false){
                    inputbool_2=true;
                    outputText = dialogspeak_text2.getText().toString();

                }else{
                    youspeak_text3.setText("You");
                    youspeak_text3.setTextSize(50);
                    dialogflowspeak_text3.setText("Dialog");
                    dialogspeak_text3.setVisibility(View.VISIBLE);
                    dialogspeak_text3.setText(outputText);
                    dialogspeak_text3.setTextColor(0xFFFFFFFF);
                    voice_text3.setVisibility(View.VISIBLE);
                    voice_text3.setText(outputText2);
                    voice_text3.setTextColor(0xFFFFFFFF);
                }
            }
            voice_text.setText(queryText);
            voice_text2.setText(inputText);
            voice_text2.setTextColor(0xFFFFFFFF);
            outputText2 = voice_text2.getText().toString();
            inputText=queryText;*/

            // set user text


            // Java V2
            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(queryText).setLanguageCode("en-US")).build();
            Struct struct_params = Struct.newBuilder().putFields("longitude", Value.newBuilder().setStringValue(Double.toString(longitude)).build())
                    .putFields("latitude", Value.newBuilder().setStringValue(Double.toString(latitude)).build())
                    .putFields("options", Value.newBuilder().setStringValue(options).build()).build();
            QueryParameters queryParameters = QueryParameters.newBuilder().setPayload(struct_params).build();
            RequestJavaV2Task.TaskCompletedListener taskCompletedListener = new RequestJavaV2Task.TaskCompletedListener() {
                @Override
                public void onTaskCompleted(DetectIntentResponse response) {
                    if (response != null) {
                        // process aiResponse here
                        final QueryResult result = response.getQueryResult();
                        final com.google.cloud.dialogflow.v2.Intent intent = result.getIntent();
                        intentname = intent.getDisplayName();
                        if (intent != null) {
                            Log.i(TAG, "Intent name: " + intentname);
                        }
                        final String speech = result.getFulfillmentText();
                        Log.i(TAG, "V2  Speech: " + speech);
                        if(speech != null){
                            chatbotcount++;
                            data_chatbotsay[chatbotcount]=speech;
                        }
                        dialogflowspeak_text.setText(speech);
                        TTS.speak(dialogflowspeak_text.getText().toString());
                        List<Context> contents = result.getOutputContextsList();
                        if (contents.size() > 0) {
                            for (int i = 0; i < contents.size(); i++) {
//                                Log.d(TAG, "onPostExecute: " + contents.get(i).getName());
                                String[] div = contents.get(i).getName().split("/");
                                String[] contentPattern = div[div.length - 1].split("-");
                                if (contentPattern.length == 2) {
//                                    if (contentPattern[1].equals("fixedPattern")) {
                                    if (contentPattern[1].equals("pattern")) {
                                        questioning = true; // ?????????????????????
                                        break;
                                    } else {
                                        questioning = false;
                                    }
                                }
                            }
                        } else {
                            questioning = false;
                        }

                        addChat(queryText, 0);

                        // set user text
                        String[] diff = new String[3];
                        diffList.add(diff);
//                        addChat(queryText + "\n" + getResources().getString(R.string.your_score_title) + ": " + similarity, 0);
                        // get options
                        try {
                            final Map<String, Value> payload = result.getWebhookPayload().getFieldsMap();
                            options = "[";
                            for (int i = 0; i < payload.get("options").getListValue().getValuesList().size(); i++) {
                                if (i != 0) {
                                    options += ",";
                                }
                                options += payload.get("options").getListValue().getValues(i);
                            }
                            options = options.replaceAll("string_value: ", "");
                            options += "]";
                        } catch (Exception e) {
                            options = "";
                        }

                        addChat(speech, 1);

//                        try{
//                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                            DatabaseReference db=database.getReference().child("??????" + Student.Name + "???");
//                            db.child("Chatbot data").child("Usersay").child(String.valueOf(user+1)).setValue(queryText.replace(",","__"));
//                            db.child("Chatbot data").child("Chatbotsay").child(String.valueOf(chatbot+1)).setValue(response.getQueryResult()
//                                    .getFulfillmentText().replace(",","__"));
//                        }catch (Exception e){
//                            e.printStackTrace();
//                            Toast.makeText(ChatbotActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
//                        }
                    }
                }
            };
            new RequestJavaV2Task.CallRequestJavaV2Task(ChatbotActivity.this, session, sessionsClient, queryInput, queryParameters, taskCompletedListener).execute();
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    }

    //show text on screen
    private void addChat(String text, int type) {
        // type 0 user, 1 dialog, 2 actively notify
        if (type == 0) {
            String[] sentences = text.split("<br>");
            String fulfillmentText = "";
            for (int i = 0; i < sentences.length; i++) {
                String sentence = sentences[i];
                fulfillmentText += sentence + "\n";
//                TTS.speak(sentence);
            }
            final int diffListIndex = diffList.size()-1;
            final String originalText_right = fulfillmentText.substring(0, fulfillmentText.length() - 1);
//            final String originalText = text; //?????????
            textView = new TextView(ChatbotActivity.this);
            textView.setTextSize(30);
            textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            textView.setBackground(getResources().getDrawable(R.drawable.text_send));
            textView.setLayoutParams(textParam);
            SpannableString content = new SpannableString(text);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            textView.setText(content);
            final String img_pathword_right = "Right_Image_" + Student.Name + "_" + content + ".jpg";
            try{
                final File f_photo=new File("/sdcard/Pictures/MyPicFolder/" + img_pathword_right);
                if(f_photo.exists())
                {
                    Drawable drawable = getResources().getDrawable(R.drawable.picture);
                    drawable.setBounds(0, 0, 40,40);
                    textView.setCompoundDrawables(null, null,drawable, null);
                }
                Log.v("??????uri",f_photo.toString());
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(ChatbotActivity.this,"?????????????????????",Toast.LENGTH_SHORT).show();
            }
            //textView.setText(text);
            textView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(FixedActivity.this, DiffActivity.class);
//                    intent.putExtra("diff", diffList.get(diffListIndex)[0]);
//                    intent.putExtra("questionPattern", diffList.get(diffListIndex)[1]);
//                    intent.putExtra("userPattern", diffList.get(diffListIndex)[2]);
//                    startActivity(intent);
                    usertext = content.toString();
                    createDiffView(diffListIndex);
                }
             });
            parent = new LinearLayout(ChatbotActivity.this);
            parent.setLayoutParams(parentParam);
            parent.setGravity(Gravity.RIGHT);
            parent.addView(textView);
            linearLayout.addView(parent);
        } else if (type == 1) {
            String[] sentences = text.split("<br>");
            Boolean loadpicture=false;
            Boolean loadvoice=false;
            String fulfillmentText = "";
            for (int i = 0; i < sentences.length; i++) {
                String sentence = sentences[i];
                fulfillmentText += sentence + "\n";
//                TTS.speak(sentence);
            }
            final String originalText = fulfillmentText.substring(0, fulfillmentText.length() - 1);
            textView = new TextView(ChatbotActivity.this);
            textView.setTextSize(30);
            textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            textView.setBackground(getResources().getDrawable(R.drawable.text_receive));
            textView.setLayoutParams(textParam);
            //textView.setText(originalText);
            SpannableString content = new SpannableString(originalText);
            final String pathword = Student.Name + "_" + content + ".amr";
            final String img_pathword = "Image_" + Student.Name + "_" + content + ".jpg";
            try{
                final File f_photo=new File("/sdcard/Pictures/MyPicFolder/" + img_pathword);
                if(f_photo.exists())
                {
                    Drawable drawable2 = getResources().getDrawable(R.drawable.picture);
                    drawable2.setBounds(0, 0, 40,40);
                    textView.setCompoundDrawables(null, null,drawable2, null);
                    loadpicture=true;
                }
                Log.v("??????uri",f_photo.toString());
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(ChatbotActivity.this,"?????????????????????",Toast.LENGTH_SHORT).show();
            }
            try{
                final File f=new File("/sdcard/" + pathword);
                if(f.exists())
                {
                    Drawable drawable1 = getResources().getDrawable(R.drawable.advertising);
                    drawable1.setBounds(0, 0, 40,40);
                    textView.setCompoundDrawables(null, null,drawable1, null);
                    loadvoice=true;
                }
                Log.v("??????uri",f.toString());
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(ChatbotActivity.this,"?????????????????????",Toast.LENGTH_SHORT).show();
            }
            if(loadpicture==true && loadvoice==true){
                Drawable drawable3 = getResources().getDrawable(R.drawable.photo_and_speaker);
                drawable3.setBounds(0, 0, 80,40);
                textView.setCompoundDrawables(null, null,drawable3, null);
            }
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            textView.setText(content);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatbotActivity.this, R.style.AlertDialogStyle);
                    trn_originalText = originalText;
                    creatview_left();
//                    alertDialog.setTitle(getString(R.string.detail_dialog_title))
//                            .setIcon(R.mipmap.good_icon)
//                            .setMessage(getString(R.string.original) + ":\n" + originalText + "\n" + getString(R.string.translation) + ":\n" + getString(R.string.detail_dialog_translating))
//                            .setNegativeButton(getString(R.string.Listen), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int i) {
//                                    try {
//                                        field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
//                                        field.setAccessible(true);
//                                        field.set(dialog, false);
//                                    } catch (NoSuchFieldException | IllegalAccessException e) {
//                                        e.printStackTrace();
//                                    }
//                                    TTS.speak(originalText);
//                                }
//                            })

//                            .setPositiveButton(getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    try {
//                                        field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
//                                        field.setAccessible(true);
//                                        field.set(dialog, true);
//                                    } catch (NoSuchFieldException | IllegalAccessException e) {
//                                        e.printStackTrace();
//                                    }
//                                    alertDialogList = new ArrayList<AlertDialog>();
//                                }
//                            })
//                            .setMessage(getString(R.string.original) + ":\n" + originalText + "\n" + getString(R.string.translation) + ":\n" + translatedText)
                            ;
//                    AlertDialog alertDialogFixed = alertDialog.create();
//                    alertDialogFixed.setCanceledOnTouchOutside(false);
//                    alertDialogFixed.show();

                    dialogList.add(originalText);
//                    alertDialogList.add(alertDialogFixed);
                    trans();

//                    MicrosoftTranslate.translate(FixedActivity.this, dialogList.size() - 1);
                }
            });
            parent = new LinearLayout(ChatbotActivity.this);
            parent.setLayoutParams(parentParam);
            parent.setGravity(Gravity.LEFT);
            parent.addView(textView);
            linearLayout.addView(parent);
            //keep log
            //keepDialog(queryText, fulfillmentText, similarity);
        }
        // scroll down
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, scrollView.getBottom());
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                pDialog.dismiss();
            }
        });
    }


    private void initV2Chatbot() {
        try {
            InputStream stream = getResources().openRawResource(R.raw.engineerbuilding_hiowhp_bf82041d2f2a);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            session = SessionName.of(projectId, uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        TTS.stop();
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

    private void creatview_left () {
        LayoutInflater inflater = LayoutInflater.from(context);
        View diffView = inflater.inflate(R.layout.activity_diff_left, null);

        userTextView_left = diffView.findViewById(R.id.userTextView_left);
        wrongWord_left = diffView.findViewById(R.id.wrongWord_left);
        wrongWordScrollView_left = diffView.findViewById(R.id.wrongWordScrollView_left);
        trans_speak_left = diffView.findViewById(R.id.trans_speak_left);

        final Button record = (diffView.findViewById(R.id.recordplayer));
        final Button stop = (diffView.findViewById(R.id.stopplayer));
        final Button play = (diffView.findViewById(R.id.playplayer));
        final Button btn_tts_chatbot_left = (diffView.findViewById(R.id.btn_tts_chatbot_left));
        final TextView showtime = (diffView.findViewById(R.id.show_time));
        takephoto = (diffView.findViewById(R.id.takephoto));
        final String pathword = Student.Name + "_" + trn_originalText + ".amr";
        final String img_pathword = "Image_" + Student.Name + "_" + trn_originalText + ".jpg";

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timeCount++;
                String str="????????????..." + showTimeCount((long)timeCount);
                showtime.setText(str);
                handler.postDelayed(this, 1000);
            }
        };
        
        userTextView_left.setText("\t"+trn_originalText);
        userTextView_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTS.speak(userTextView_left.getText().toString());
            }
        });
        btn_tts_chatbot_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatbot_tts_count++;
                TTS.speak(userTextView_left.getText().toString());
                try {
                    //??????????????????????????????
                    String date = sdf.format(new java.util.Date());
                    DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                            .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("Chatbot").child("?????????????????????TTS");
                    fire_60sec_student_data.child(date).child("Dialog Text").setValue(userTextView_left.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ChatbotActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.v("left_user:",userTextView_left.getText().toString());
        anwerTextArray_left = userTextView_left.getText().toString().replace(",","").replace(".","").split("\\s+");
        Log.v("word_left:",Arrays.toString(anwerTextArray_left));

        try{
            final File f_photo=new File("/sdcard/Pictures/MyPicFolder/" + img_pathword);
            if(f_photo.exists())
            {
                takephoto.setImageURI(Uri.fromFile(f_photo));
            }
            Log.v("??????uri",f_photo.toString());
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(ChatbotActivity.this,"?????????????????????",Toast.LENGTH_SHORT).show();
        }


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPress == true){
                    recorder = new MediaRecorder();// new???MediaRecorder??????
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    // ??????MediaRecorder????????????????????????
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                    // ??????MediaRecorder?????????????????????
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    // ??????MediaRecorder????????????????????????amr.

                    recorder.setOutputFile("/sdcard/" + pathword);

                    // ??????????????????????????????????????????
                    try {
                        recorder.prepare();// ????????????
                        recorder.start();// ????????????
                        Toast.makeText(ChatbotActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                        timeCount = 0;
                        handler.post(runnable);

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isPress = false;
                }else{
                    Toast.makeText(ChatbotActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                }

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPress){
                    recorder.stop();// ????????????
                    Toast.makeText(ChatbotActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    handler.removeCallbacks(runnable);
                    showtime.setText("????????????");

                    // recorder.reset(); // ????????????MediaRecorder.
                    recorder.release(); // ?????????????????????????????????
                    recorder = null;
                    isPress = true;
                    record_count++;
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("Chatbot").child("?????????????????????");
                        fire_60sec_student_data.child(date).child("Dialog Text").setValue(trn_originalText);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(ChatbotActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChatbotActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                }

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                playtimeA += 1;
                try {
                Uri uri = Uri.fromFile(new File("/sdcard/" + pathword));
                MP = MediaPlayer.create(context, uri);
                MP.start();
                MP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer MP) {
                        try {
                            //??????????????????????????????
                            String date = sdf.format(new java.util.Date());
                            DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                    .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("Chatbot").child("???????????????????????????");
                            fire_60sec_student_data.child(date).child("Dialog Text").setValue(trn_originalText);
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(ChatbotActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                        MP.release();
                    }
                });
                }catch (Exception e){
                    Toast.makeText(context,"????????????", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pressed){
                    case 0:
                        takephoto.startAnimation(animZoomIn);
                        pressed=1;
                        break;
                    case 1:
                        takephoto.startAnimation(animZoomOut);
                        pressed=0;
                        break;
                }
            }
        });


        takephoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bol_takephoto = true;
                bol_takephoto_right = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //?????????
                    if (ContextCompat.checkSelfPermission(ChatbotActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ChatbotActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {  //??????
                    BringImagePicker();
                }
                takephoto_count++;
                return false;
            }
        });

        


        GetDiffWords getDiffWords = new GetDiffWords();
        final String[] words = new String[100];
        for (int i = 1; i < anwerTextArray_left.length; i++) {
//            if (words[i] == null || words[i] == "") {
//                break;
//            }
//            wrongWordScrollView.removeAllViews();
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 9));
            textView.setTextSize(30);
            textView.setText(anwerTextArray_left[i]+"\n"+"\n");
            linearLayout.addView(textView);
            ImageButton imageButton = new ImageButton(context);
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.chatbot_player));
            imageButton.setBackgroundColor(getResources().getColor(R.color.transparent));
            imageButton.setPaddingRelative(0,0,55,0);

            linearLayout.addView(imageButton);

//            imageButton.setOnClickListener(new TtsOnClickListener(anwerTextArray_left[i]));
            int finalI = i;


            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TTS.speak(anwerTextArray_left[finalI]);
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("Chatbot").child("???????????????????????????TTS");
                        fire_60sec_student_data.child(date).child("Dialog Single Text").setValue(anwerTextArray_left[finalI]);
                        fire_60sec_student_data.child(date).child("Dialog Text").setValue(userTextView_left.getText().toString());
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(ChatbotActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    chatbot_text_tts_count++;
                }
            });
            wrongWord_left.addView(linearLayout);
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
        alertDialogBuilder.setIcon(R.mipmap.correction_icon);
        alertDialogBuilder.setTitle("\t????????????");
        alertDialogBuilder.setView(diffView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(1000, 1150);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createDiffView(int diffListIndex) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View diffView = inflater.inflate(R.layout.activity_diff, null);

        answerTextView = diffView.findViewById(R.id.answerTextViewDiff);
        userTextView = diffView.findViewById(R.id.userTextView);


        wrongTextView = diffView.findViewById(R.id.wrongTextView);
        wrongWord = diffView.findViewById(R.id.wrongWord);
        wrongWordScrollView = diffView.findViewById(R.id.wrongWordScrollView);
        trans_correct = diffView.findViewById(R.id.trans_correct);
        takephoto_right = diffView.findViewById(R.id.takephoto_right);
//        trans_speak = diffView.findViewById(R.id.trans_speak);
        chinese = diffView.findViewById(R.id.chinese);

        final Button btn_tts_chatbot = diffView.findViewById(R.id.btn_tts_chatbot);

        btn_tts_chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTS.speak(answerTextView.getText().toString());
                try {
                    //??????????????????????????????
                    String date = sdf.format(new java.util.Date());
                    DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                            .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("Chatbot").child("??????????????????TTS");
                    fire_60sec_student_data.child(date).child("Dialog Text").setValue(answerTextView.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ChatbotActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(intentname == null){
            Toast.makeText(ChatbotActivity.this,"????????????????????????", Toast.LENGTH_SHORT).show();
        }else{
            answerTextView.setText("\t"+intentname);
        }
        anwerTextArray = answerTextView.getText().toString().replace(",", "").replace(".", "").split("\\s+");
        Log.v("wordss:", Arrays.toString(anwerTextArray));



        String intentname_text;

        if (intentname == null || intentname.equals("") == true) {
            trans_correct.setText("");
            intentname_text = "";
        } else {
            trans_correct();
            intentname_text = intentname;
        }

        diffMatchPatch diff_matchPatch_obj = new diffMatchPatch();
        LinkedList<diffMatchPatch.Diff> diffListUser = diff_matchPatch_obj.diff_wordMode(usertext.replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase(), intentname_text.replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase());

        // change tag <u> to real del line
        String htmlDiff = diff_matchPatch_obj.diff_prettyHtml(diffListUser);
        htmlDiff = htmlDiff.replaceAll(" </u>", "</u> ");
        htmlDiff = htmlDiff.replaceAll(" </del>", "</del> ");
        htmlDiff = htmlDiff.replaceAll(" </span>", "</span> ");

        Spanned recorrect_response = Html.fromHtml(htmlDiff, null, new Html.TagHandler() {
            int startTag;
            int endTag;

            @Override
            public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                if (tag.equalsIgnoreCase("del")) {
                    if (opening) {
                        startTag = output.length();
                    } else {
                        endTag = output.length();
                        output.setSpan(new StrikethroughSpan(), startTag, endTag, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        });
        userTextView.setText(recorrect_response);

        //????????????dialog???????????????
        String img_pathword_right = "Right_Image_" + Student.Name + "_" + usertext + ".jpg";;
        try{

            final File f_photo=new File("/sdcard/Pictures/MyPicFolder/" + img_pathword_right);
            if(f_photo.exists())
            {
                takephoto_right.setImageURI(Uri.fromFile(f_photo));
            }
            Log.v("??????uri",f_photo.toString());
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(ChatbotActivity.this,"?????????????????????",Toast.LENGTH_SHORT).show();
        }

        takephoto_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pressed_right){
                    case 0:
                        takephoto_right.startAnimation(animZoomIn);
                        pressed_right=1;
                        break;
                    case 1:
                        takephoto_right.startAnimation(animZoomOut);
                        pressed_right=0;
                        break;
                }
            }
        });

        takephoto_right.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bol_takephoto = false;
                bol_takephoto_right = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //?????????
                    if (ContextCompat.checkSelfPermission(ChatbotActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ChatbotActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {  //??????
                    BringImagePicker();
                }
                takephoto_count++;
                return false;
            }
        });
//        trans_speak();

        GetDiffWords getDiffWords = new GetDiffWords();
        final String[] words = new String[100];
        getDiffWords.getInsert(htmlDiff, words);
        for (int i = 1; i < anwerTextArray.length; i++) {
            int finalI = i;
//            if (words[i] == null || words[i] == "") {
//                break;
//            }
//            wrongWordScrollView.removeAllViews();
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 9));
            textView.setTextSize(30);
            textView.setText(anwerTextArray[i]+"\n"+"\n");
            linearLayout.addView(textView);
            ImageButton imageButton = new ImageButton(context);
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.chatbot_player));
            imageButton.setBackgroundColor(getResources().getColor(R.color.transparent));
            imageButton.setPaddingRelative(0,0,50,0);
            linearLayout.addView(imageButton);


            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TTS.speak(anwerTextArray[finalI]);
                    try {
                        //??????????????????????????????
                        String date = sdf.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("??????"+Student.Name+"???").child("Student data").child("????????????").child("Chatbot").child("????????????????????????TTS");
                        fire_60sec_student_data.child(date).child("Dialog Single Text").setValue(anwerTextArray[finalI]);
                        fire_60sec_student_data.child(date).child("Dialog Text").setValue(answerTextView.getText().toString());
                        fire_60sec_student_data.child(date).child("User say text").setValue(usertext);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(ChatbotActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            imageButton.setOnClickListener(new TtsOnClickListener(anwerTextArray[i]));

            wrongWord.addView(linearLayout);
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
        alertDialogBuilder.setIcon(R.mipmap.correction_icon);
        alertDialogBuilder.setTitle("\t????????????");
        alertDialogBuilder.setView(diffView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        String str = usertext;
        ArrayList list=new ArrayList();
        for (int i = 0; i < str.length(); i++) {
            String test = str.substring(i, i + 1);
            if (test.matches("[\\u4E00-\\u9FA5]+")) {
                list.add(test);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Object s : list)
        {
            sb.append(s);
        }
        str2=sb.toString();
        if (str2 != null){
            trans_chinese();
        }
        if(str2 !="") {
            chinese.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TTS.speak(trans_chinese2);
                }
            });
        }
        Log.v("test_chinese2",str2);
    }

    private class TtsOnClickListener implements View.OnClickListener {
        String text;

        public TtsOnClickListener(String text) {
            this.text = text;
        }

        @Override
        public void onClick(View v) {
            TTS.speak(this.text);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {
        }
        //(Carol) Snackbar.make(mMapView, com.naer.pdfreader.R.string.error_location_provider, Snackbar.LENGTH_INDEFINITE).show();
        else {
            if (isNetworkEnabled) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            //(Carol) drawMarker(location);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CheckLocation(Double latitude, Double longitude) {
        String[] home=new String[]{"Who is at home last night?","Who did you have dinner today?",
                "Did you go to the convenience stores last night?","Do you like convenience stores?","How many dishes did you have last night?"};
        String[] other=new String[]{"What's is weather now?","How was the weather last night?","You can tell me the weather.","You can ask me the convenience store."};
        String[] classroom =new String[]{"What did you eat yesterday morning?","What main food did you have?","What's your favorite side dish at this breakfast shop?"
        ,"Did you have your breakfast?"};
        String[] playground =new String[]{"How was the weather last afternoon?","What's is weather now?","Did you played basketball yesterday?"
        ,"Did you jogged yesterday?"};
        //Place ?????????????????????????????????
        Place place = new Place();
        place.setLat(latitude);
        place.setLag(longitude);
        //?????????Key????????????
        KeyList keyList = new KeyList();
        final List<KeyDatabase> keyDatabaseList = keyList.getKeyDatabaseList();
        //????????????????????????????????????????????????????????????
        List<Integer> Unidentified_List = new ArrayList<>();

        for (KeyDatabase keyDatabase : keyDatabaseList) {
            if (SearchMapService.check(place, keyDatabase.Latitude, keyDatabase.Longitude, keyDatabase.Radius)) {
                Unidentified_List.add(1);
                //Config.ACCESS_TOKEN = keyDatabase.Key;
//                Init_AIConfiguration();//?????????????????????
                placeview = PlaceName.getText().toString();
                if (!placeview.equals(keyDatabase.Place) || placeview.isEmpty()) {
                    // TTS.speak("You are in the "+ keyDatabase.Place +" now");
                    // textResolt.setText("You are in the "+ keyDatabase.Place +" now");
                    // textResolt.setVisibility(View.VISIBLE);
                    Place.GlobalPlace = keyDatabase.Place;//???????????????
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            //reference.setVisibility(View.INVISIBLE);
                        }
                    }, 5000);
                }
                Random x;
                int i;
                PlaceName.setText((keyDatabase.Place));

                switch (PlaceName.getText().toString().trim()){
                    case "home":
                        x = new Random();
                        i = x.nextInt(home.length);
                        dialog = home[i];
                        break;
                    case "classroom":
                        x = new Random();
                        i = x.nextInt(classroom.length);
                        dialog = classroom[i];
                        break;
                    case "playground":
                        x = new Random();
                        i = x.nextInt(playground.length);
                        dialog = playground[i];
                        break;
                }
                //?????????????????????
                if(!last_location.trim().equals(PlaceName.getText().toString().trim())){
                    notfy_message();
                }
                //??????PlaceName????????????????????????????????????String???????????????Adapter???AutoCompleteTextview???
//                fire_vocabulary.child(PlaceName.getText().toString()).child("sentence").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        int i = 0;
//                        autoStrs= new String [(int)dataSnapshot.getChildrenCount()];   //?????????for?????????????????????????????? ???????????? null
//                        for(DataSnapshot each : dataSnapshot.getChildren()){
//                            autoStrs[i] = each.getValue().toString();
//                            i++;
//                        }
//                        String[] auto = autoStrs;
//                        adapter = new ArrayAdapter<String>(DescribeActivity.this, android.R.layout.simple_dropdown_item_1line,auto);
//                        studentdescribe.setAdapter(adapter);
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) { }
//                });
            }
        }
        if (Unidentified_List.size() == 0) {
            PlaceName.setText("Other");
        }
    }

    private Runnable runnable_message = new Runnable() {
        @Override
        public void run() {
            handler_message.postDelayed(this,  7200000);
            handler_message.sendEmptyMessage(1);
        }
    };

    private Handler handler_message = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1 && !pm.isInteractive()){
                Random x;
                int i;
                java.util.Date date = new java.util.Date();
                SimpleDateFormat df = new SimpleDateFormat("HH");
                String str = df.format(date);

                final int a = Integer.parseInt(str);
                if (a > 8 && a <= 12){
                    String[] morning=new String[]{"Who is at home now?","Who did you have breakfast today?",
                            "Did you go to the convenience stores last night?","What's is weather now?","What's your favorite side dish at this breakfast shop?"};
                    x = new Random();
                    i = x.nextInt(morning.length);
                    dialog = morning[i];
                    //?????????????????????
                    notfy_message();
                }else if(a > 13 && a <= 18){
                    String[] afternoon =new String[]{"What did you eat last lunch?","Who is at home now?","You can tell me the weather."
                            ,"Did you have your lunch?"};
                    x = new Random();
                    i = x.nextInt(afternoon.length);
                    dialog = afternoon[i];
                    //?????????????????????
                    notfy_message();
                }else if(a > 18 && a <= 21){
                    String[] night =new String[]{"How was the weather last night?","You can tell me Convenience stores","Did you have dinner?"
                            ,"Who is at home now?"};
                    x = new Random();
                    i = x.nextInt(night.length);
                    dialog = night[i];
                    //?????????????????????
                    notfy_message();
                }
            }
        }
    };

    private void notfy_message(){
        java.util.Date date = new java.util.Date();
        SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String str_date = df_date.format(date);
        //??????PlaceName????????????????????????????????????String???????????????Adapter???AutoCompleteTextview???
//                fire_vocabulary.child(PlaceName.getText().toString()).child("sentence").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        int i = 0;
//                        autoStrs= new String [(int)dataSnapshot.getChildrenCount()];   //?????????for?????????????????????????????? ???????????? null
//                        for(DataSnapshot each : dataSnapshot.getChildren()){
//                            autoStrs[i] = each.getValue().toString();
//                            i++;
//                        }
//                        String[] auto = autoStrs;
//                        adapter = new ArrayAdapter<String>(DescribeActivity.this, android.R.layout.simple_dropdown_item_1line,auto);
//                        studentdescribe.setAdapter(adapter);
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) { }
//                });
        Intent intent = new Intent(this,ChatbotActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent  PI = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.ic_launcher)
                    .setChannelId("ID")
                    .setContentTitle(dialog)
                    .setSubText("????????????"+str_date)
                    .setContentText("??????????????????:"+PlaceName.getText().toString())
                    .setContentIntent(PI);
        }
        //????????????????????????
        Notification notification = builder.build();
        mNotificationManager.notify(0,notification);
        if (!bol_lastlocation){
            addChat(dialog,1);
            bol_lastlocation=true;
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private void updateShow(Location location) {
        if (location != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("????????????:\n");
            sb.append("??????:" + location.getLongitude() + "\n");
            sb.append("??????:" + location.getLatitude() + "\n");

            loc_msg = sb.toString();
        } else loc_msg = "";
            handler.sendEmptyMessage(0x001);
    }

    public void locationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        updateShow(location);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,8,mLocationListener);

    }

    @Override
    public void onResume() {
        super.onResume();
        locationUpdate();
//        InitSpeechRecognizer();
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void run() {
                getCurrentLocation();
            }
        }, 1000);
    }

    private void trans(){
    TranslateAPI translateAPI = new TranslateAPI(
            Language.AUTO_DETECT,
            Language.CHINESE_TRADITIONAL,
            trn_originalText
    );
    translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
        @Override
        public void onSuccess(String s) {
            trans_speak_left.setText("\t"+s);
            translatedText=s;
            if(ChatbotActivity.alertDialogList.size() > 0){
                ChatbotActivity.alertDialogList.get(ChatbotActivity.alertDialogList.size() - 1).setMessage(getString(R.string.original) + ":\n" + trn_originalText + "\n" + getString(R.string.translation) + ":\n" +translatedText);
            }
        }

        @Override
        public void onFailure(String s) {

        }
    });
    }

    private void trans_correct(){
        TranslateAPI translateAPI = new TranslateAPI(
                Language.AUTO_DETECT,
                Language.CHINESE_TRADITIONAL,intentname

        );
        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
            @Override
            public void onSuccess(String s) {
                trans_correct.setText("\t"+s);
            }

            @Override
            public void onFailure(String s) {

            }
        });
    }

//    private void trans_speak(){
//        TranslateAPI translateAPI = new TranslateAPI(
//                Language.AUTO_DETECT,
//                Language.CHINESE_TRADITIONAL,usertext
//
//        );
//        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
//            @Override
//            public void onSuccess(String s) {
//                trans_speak.setText(s);
//            }
//
//            @Override
//            public void onFailure(String s) {
//
//            }
//        });
//    }

    private void trans_chinese(){
        TranslateAPI translateAPI = new TranslateAPI(
                Language.AUTO_DETECT,
                Language.ENGLISH,str2

        );
        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
            @Override
            public void onSuccess(String s) {
                chinese.setText(str2+"("+s+")");
                trans_chinese2=s;
            }

            @Override
            public void onFailure(String s) {

            }
        });
    }



    private void getchildcount(){
      if(user==0) {
          try {
              final FirebaseDatabase database = FirebaseDatabase.getInstance();
              DatabaseReference db = database.getReference().child("??????" + Student.Name + "???").child("Chatbot data").child("Usersay");
              db.addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      Log.v("getChildrenCount user", String.valueOf(dataSnapshot.getChildrenCount()));
                      user = (int) dataSnapshot.getChildrenCount();
                      test = String.valueOf(dataSnapshot.getValue());
                      test = test.substring(0,test.length()-1);
                      Log.v("getValue()",test);
                      user_array = test.split(",");
                      user_time();
                  }
                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              });

          } catch (Exception e) {
              e.printStackTrace();
              Toast.makeText(ChatbotActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
          }
      }
      if(chatbot==0) {
          try {
              final FirebaseDatabase database = FirebaseDatabase.getInstance();
              DatabaseReference db = database.getReference().child("??????" + Student.Name + "???").child("Chatbot data").child("Chatbotsay");
              db.addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      String test;
                      Log.v("getChildrenCount chat", String.valueOf(dataSnapshot.getChildrenCount()));
                      chatbot = (int) dataSnapshot.getChildrenCount();
                      test = String.valueOf(dataSnapshot.getValue());
                      test = test.substring(0,test.length()-1);
                      Log.v("getValue() chat",test);
                      chatbot_array = test.split(",");
                      chatbot_time();
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              });
          } catch (Exception e) {
              e.printStackTrace();
              Toast.makeText(ChatbotActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
          }
      }
     }

    private void user_time(){
        try {
            for (int i = 0; i < 1; i++) {
                Thread.sleep(500);
                Log.v("text_user", String.valueOf(user));
            }
        }catch(Exception e) {

        }
    }
    private void chatbot_time(){
        try {
            for (int i = 0; i < 1; i++) {
                Thread.sleep(500);
                Log.v("text_user", String.valueOf(user));
                loaddialogdata();
            }
        }catch(Exception e) {

        }
    }


    private void loaddialogdata(){
        if(user != 0){
            Log.v("????????????",test);
            Log.v("????????????", String.valueOf(user));
            for (int i=1;i<=user;i++) {
                addChat(user_array[i].replace("__",",").trim(),0);
                addChat(chatbot_array[i].replace("__",",").trim(),1);
//                if (i%2!=0){
//                    Log.v("test_z", String.valueOf(i-((i-1)/2)));
//                    addChat(user_array[i-((i-1)/2)],0);
//                    addChat(user_array[1],0);
//            }
//                    Log.v("array chat",chatbot_array[1]);
//                    addChat(chatbot_array[1],0);
//
//            }
//                if(i%2==0){
//                    Log.v("test_y", String.valueOf(i/2));
//                    addChat(chatbot_array[i/2],1);
//                    Log.v("chatbot_array[1]",chatbot_array[1]);
//                    addChat(chatbot_array[1],1);
                }
            pDialog.dismiss();
        }else{
            Log.v("????????????","True");
            fire_welcome = FirebaseDatabase.getInstance().getReference();
            fire_welcome.child("playground").child("welcome").addListenerForSingleValueEvent(new ValueEventListener() {
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
                    Log.v("Get", st[a]);
                    welcome = st[a];
                    addChat(welcome, 1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            handler_message.removeCallbacks(runnable_message);
            int test_user = user;
            int test_chatbot = chatbot;
            pDialog = new ProgressDialog(ChatbotActivity.this);
            pDialog.setTitle("????????????");
            pDialog.setMessage("?????????... \n??????????????????");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference db=database.getReference().child("??????" + Student.Name + "???").child("Chatbot data");
            try{
                HashMap map_user = new HashMap();
                HashMap map_chatbot = new HashMap();
                if(usersaycount == chatbotcount){
                    for(int i=0;i<usersaycount;i++){
                        map_user.put(String.valueOf(i+test_user+1),data_usersay[i+1].replace(",","__"));
                        map_chatbot.put(String.valueOf(i+test_chatbot+1),data_chatbotsay[i+1].replace(",","__"));
                    }
                }else{
                    for(int i=0;i<usersaycount;i++){
                        map_user.put(String.valueOf(i+test_user+1),data_usersay[i+1].replace(",","__"));
                    }
                    for(int i=0;i<chatbotcount;i++){
                        map_chatbot.put(String.valueOf(i+test_chatbot+1),data_chatbotsay[i+1].replace(",","__"));
                    }
                }

                db.child("Usersay").updateChildren(map_user);
                db.child("Chatbotsay").updateChildren(map_chatbot).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        pDialog.dismiss();
                        ChatbotActivity.this.finish();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(ChatbotActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }



    public void writeInfo(String fileName, String strWrite) {
        try {

            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String savePath = fullPath + File.separator + "/" + fileName + ".txt";

            File file = new File(savePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(strWrite+"\n");
            bw.write("????????????:"+chatbot_text_tts_count+"\n");
            bw.write("TTS??????:"+chatbot_tts_count+"\n");
            bw.write("????????????:"+record_count+"\n");
            bw.write("????????????:"+takephoto_count+"\n");

            bw.close();
            Toast.makeText(ChatbotActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String readInfo(String fileName){

        BufferedReader br = null;
        String response = null;
        try {
            StringBuffer output = new StringBuffer();
            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String savePath = fullPath + File.separator + "/"+fileName+".txt";

            br = new BufferedReader(new FileReader(savePath));
            String line = "";
            while ((line = br.readLine()) != null) {
                output.append(line +"\n");

            }
            response = output.toString();
            chatbot_text_tts_count= Integer.parseInt(response.substring(response.indexOf("????????????:")+5,response.indexOf("TTS??????:")-1));
            chatbot_tts_count= Integer.parseInt(response.substring(response.indexOf("TTS??????:")+6,response.indexOf("????????????:")-1));
            record_count= Integer.parseInt(response.substring(response.indexOf("????????????:")+5,response.indexOf("????????????:")-1));
            takephoto_count= Integer.parseInt(response.substring(response.indexOf("????????????:")+5,response.length()-1));
            br.close();
            Toast.makeText(ChatbotActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    private void alertdialog_name(){
        AlertDialog.Builder editDialog = new AlertDialog.Builder(ChatbotActivity.this);
        editDialog.setTitle("??????????????????+??????");
        editDialog.setIcon(R.drawable.ic_launcher);

        final EditText editText = new EditText(ChatbotActivity.this);
        editDialog.setView(editText);

        editDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                Student.Name = editText.getText().toString().trim();
            }
        });
        editDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(ChatbotActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        editDialog.show();

    }
}

