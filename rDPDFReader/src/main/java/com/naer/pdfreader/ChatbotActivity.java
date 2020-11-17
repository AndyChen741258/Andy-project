package com.naer.pdfreader;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import Firebase.GetDataFromFirebase;
import Firebase.UploadStudentsBehavior;

import com.orhanobut.logger.Logger;

import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.lang.reflect.Field;

import Firebase.GetDataFromFirebase;
import Interface.IDataDownloadCompleted;
import pl.droidsonroids.gif.GifImageView;

import static com.naer.pdfreader.DialogActivity.LOCATION_UPDATE_MIN_DISTANCE;
import static com.naer.pdfreader.DialogActivity.LOCATION_UPDATE_MIN_TIME;

public class ChatbotActivity extends Activity {

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
    private GifImageView loading;
    private SpeechRecognizer speechRecognizer;
    private boolean questioning;
    private double longitude = 0.0;
    private double latitude = 0.0;
    private String options = "";//for user's choose
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private List<String[]> diffList;
    private double similarity = 0;
    private static Field field;


    //限制錄音按鈕僅能按一下, 等按下停止時才可以再恢復可按狀態
    private boolean isPress = true;
    private boolean inputbool = false;
    private boolean inputbool_2 = false;
    private String queryText = "";
    private String inputText = "";
    private String outputText = "";
    private String outputText2 = "";
    private String uuid = UUID.randomUUID().toString();
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

        @Override
        public void onLocationChanged(Location location) {
            updateShow(location);
            final Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm", now);
            if (location != null) {
                longitude = location.getLongitude();   //取得經度
                latitude = location.getLatitude();    //取得緯度
                CheckLocation(latitude, longitude);//檢查地點是否在附近
                Logger.d(String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                Log.d("hooooo", String.valueOf(longitude) + " : " + String.valueOf(latitude));

//                sentence.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View view) {
//                        test1.setText(latitude.toString());
//                        test2.setText(longitude.toString());
//                    }
//                });

                //(Carol) drawMarker(location);
                //Update to Firebase
//                geoFire.setLocation(Student.Name + now, new GeoLocation(latitude, longitude),//定位點丟到firebase以學生座號+年月日時分
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//隱藏標題列
        setContentView(R.layout.activity_chatbot);
        context = this;
        btnShow = (Button) findViewById(R.id.btn_show);
        btnShow.setOnClickListener(listener);
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

        // init speechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new MyRecognizerListener());
        TTS.init(getApplicationContext());
        // Java V2
        initV2Chatbot();


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

        fire_welcome = FirebaseDatabase.getInstance().getReference();
        fire_welcome.child("playground").child("welcome").addValueEventListener(new ValueEventListener() {
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


    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_show:
                    popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
                    break;

                case R.id.btn_Conform:
                    popupWindow.dismiss();
                    break;

            }
        }
    };


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
    }

    public void startRecord(View view) {
//        int record_audio_permission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);        // 麥克風權限
//        int read_external_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE); // 讀檔權限
//        if (record_audio_permission != PackageManager.PERMISSION_GRANTED || read_external_storage != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
//        }
//        else{
        loading.setVisibility(View.VISIBLE);
        Log.d(TAG, "startRecord: ");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        speechRecognizer.startListening(intent);
//        }
    }

    private class MyRecognizerListener implements RecognitionListener {

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
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
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
            Log.d(TAG, "Recognizer onResults: " + queryText);
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
                        final String speech = result.getFulfillmentText();
                        Log.i(TAG, "V2  Speech: " + speech);
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
                                        questioning = true; // 改為問問題狀態
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
            final int diffListIndex = diffList.size() - 1;
            final String originalText = text; //翻譯前
            textView = new TextView(ChatbotActivity.this);
            textView.setTextSize(30);
            textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            textView.setBackground(getResources().getDrawable(R.drawable.text_send));
            textView.setLayoutParams(textParam);
            SpannableString content = new SpannableString(text);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            textView.setText(content);
            //textView.setText(text);
            //textView.setOnClickListener(new View.OnClickListener() {
            //Override
            //public void onClick(View view) {
//                    Intent intent = new Intent(FixedActivity.this, DiffActivity.class);
//                    intent.putExtra("diff", diffList.get(diffListIndex)[0]);
//                    intent.putExtra("questionPattern", diffList.get(diffListIndex)[1]);
//                    intent.putExtra("userPattern", diffList.get(diffListIndex)[2]);
//                    startActivity(intent);
//                    createDiffView(diffListIndex);
            // }
            // });
            parent = new LinearLayout(ChatbotActivity.this);
            parent.setLayoutParams(parentParam);
            parent.setGravity(Gravity.RIGHT);
            parent.addView(textView);
            linearLayout.addView(parent);
        } else if (type == 1) {
            String[] sentences = text.split("<br>");
            String fulfillmentText = "";
            for (int i = 0; i < sentences.length; i++) {
                String sentence = sentences[i];
                fulfillmentText += sentence + "\n";
                TTS.speak(sentence);
            }
            final String originalText = fulfillmentText.substring(0, fulfillmentText.length() - 1);
            textView = new TextView(ChatbotActivity.this);
            textView.setTextSize(30);
            textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            textView.setBackground(getResources().getDrawable(R.drawable.text_receive));
            textView.setLayoutParams(textParam);
            //textView.setText(originalText);
            SpannableString content = new SpannableString(originalText);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            textView.setText(content);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatbotActivity.this, R.style.AlertDialogStyle);
                    alertDialog.setTitle(getString(R.string.detail_dialog_title))
                            .setIcon(R.mipmap.good_icon)
                            .setMessage(getString(R.string.original) + ":\n" + originalText + "\n" + getString(R.string.translation) + ":\n" + getString(R.string.detail_dialog_translating))
                            .setNegativeButton(getString(R.string.Listen), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    try {
                                        field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                        field.setAccessible(true);
                                        field.set(dialog, false);
                                    } catch (NoSuchFieldException | IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    TTS.speak(originalText);
                                }
                            })
                            .setPositiveButton(getString(R.string.Read), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                        field.setAccessible(true);
                                        field.set(dialog, true);
                                    } catch (NoSuchFieldException | IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    alertDialogList = new ArrayList<AlertDialog>();
                                }
                            });

                    AlertDialog alertDialogFixed = alertDialog.create();
                    alertDialogFixed.setCanceledOnTouchOutside(false);
                    alertDialogFixed.show();

                    dialogList.add(originalText);
                    alertDialogList.add(alertDialogFixed);

                    //MicrosoftTranslate.translate(FixedActivity.this, dialogList.size() - 1);
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

    private void createDiffView(int diffListIndex) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View diffView = inflater.inflate(R.layout.activity_diff, null);

        answerTextView = diffView.findViewById(R.id.answerTextViewDiff);
        userTextView = diffView.findViewById(R.id.userTextView);
        wrongTextView = diffView.findViewById(R.id.wrongTextView);
        wrongWord = diffView.findViewById(R.id.wrongWord);
        wrongWordScrollView = diffView.findViewById(R.id.wrongWordScrollView);

        answerTextView.setText(this.diffList.get(diffListIndex)[1]);

        diffMatchPatch diff_matchPatch_obj = new diffMatchPatch();
        LinkedList<diffMatchPatch.Diff> diffListUser = diff_matchPatch_obj.diff_wordMode(this.diffList.get(diffListIndex)[2].replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase(), this.diffList.get(diffListIndex)[1].replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase());

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

        GetDiffWords getDiffWords = new GetDiffWords();
        final String[] words = new String[100];
        getDiffWords.getInsert(htmlDiff, words);
        for (int i = 0; i < words.length; i++) {
            if (words[i] == null || words[i] == "") {
                break;
            }
//            wrongWordScrollView.removeAllViews();
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 9));
            textView.setTextSize(22);
            textView.setText(words[i]);
            linearLayout.addView(textView);
            ImageButton imageButton = new ImageButton(context);
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.icon_voice_selector));
            imageButton.setBackgroundColor(getResources().getColor(R.color.transparent));
            imageButton.setOnClickListener(new TtsOnClickListener(words[i]));
            linearLayout.addView(imageButton);
            wrongWord.addView(linearLayout);
        }
        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
        alertDialogBuilder.setIcon(R.mipmap.correction_icon);
        alertDialogBuilder.setTitle("\t錯誤訂正");
        alertDialogBuilder.setView(diffView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();*/
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

    public void CheckLocation(Double latitude, Double longitude) {
        //Place 裡面的資料是目前的位置
        Place place = new Place();
        place.setLat(latitude);
        place.setLag(longitude);
        //初始化Key資料列表
        KeyList keyList = new KeyList();
        final List<KeyDatabase> keyDatabaseList = keyList.getKeyDatabaseList();
        //儲存未確認的地點，為了確認是否在未知地點
        List<Integer> Unidentified_List = new ArrayList<>();

        for (KeyDatabase keyDatabase : keyDatabaseList) {
            if (SearchMapService.check(place, keyDatabase.Latitude, keyDatabase.Longitude, keyDatabase.Radius)) {
                Unidentified_List.add(1);
                //Config.ACCESS_TOKEN = keyDatabase.Key;
//                Init_AIConfiguration();//更新對話資料庫
                placeview = PlaceName.getText().toString();
                if (!placeview.equals(keyDatabase.Place) || placeview.isEmpty()) {
                    // TTS.speak("You are in the "+ keyDatabase.Place +" now");
                    // textResolt.setText("You are in the "+ keyDatabase.Place +" now");
                    // textResolt.setVisibility(View.VISIBLE);
                    Place.GlobalPlace = keyDatabase.Place;//目前的地點
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            //reference.setVisibility(View.INVISIBLE);
                        }
                    }, 5000);
                }
                PlaceName.setText((keyDatabase.Place));

                //抓到PlaceName後，讀取相對應的資料塞進String陣列中，在Adapter進AutoCompleteTextview中
//                fire_vocabulary.child(PlaceName.getText().toString()).child("sentence").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        int i = 0;
//                        autoStrs= new String [(int)dataSnapshot.getChildrenCount()];   //若擺在for迴圈中會每一次都洗掉 抓不到值 null
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

    private void updateShow(Location location) {
        if (location != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("現在位置:\n");
            sb.append("經度:" + location.getLongitude() + "\n");
            sb.append("緯度:" + location.getLatitude() + "\n");
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

}

