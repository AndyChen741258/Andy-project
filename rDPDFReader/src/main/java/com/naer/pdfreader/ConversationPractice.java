package com.naer.pdfreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryParameters;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.client.utils.URIUtils;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import Model.DialogPracticBehavior;
import Model.SpeechData;
import Others.diff_match_patch;

import static com.naer.pdfreader.DialogActivity.TAG;

public class ConversationPractice extends Activity {

    private TextView drama_name;
    private TextView drama_four_num;
    private Button drama_take;
    private Button getanswer;
    private Button hint;
    private Button next_page;
    private ImageView drama_practice_photo;
    private ImageView drama_example_photo;
    private TextView practice_say_1;
    private TextView practice_say_2;
    private TextView cha_a;
    private TextView cha_b;
    private boolean questioning;
    StorageReference fire_ph;
    private int nnn = 1;
    boolean n = false;

    private String[] sssttt = {"a", "b", "c"};

    DatabaseReference fire_hint;
    private SpeechRecognizer speechRecognizer;//內建語音辨識
    private static String CompareSentences_practice = null;//設定比較的句子
    private int score;

    // Java V2
    private String uuid = UUID.randomUUID().toString();
    private SessionsClient sessionsClient;
    private SessionName session;

    private Context context;
    private PopupWindow popupWindow = null;
    private Button ok;
    private ImageView big_pic;

//    private String practice_id_key;
//    DatabaseReference fire_conversation_practice;

    //點選hint的次數
    public int hint_time1 = 0;
    public int hint_time2 = 0;
    public int hint_time3 = 0;
    public int hint_time4 = 0;

    //Storage存放照片
    private StorageReference fire_prctice_conversation_picture;
    private String prctice_conversation_download_url = "";



    private double longitude = 0.0;
    private double latitude = 0.0;
    private String options = ""; //for user's choose

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//隱藏標題列
        setContentView(R.layout.activity_conversation_practice);
        context = this;

        drama_name = findViewById(R.id.drama_name);
        drama_four_num = findViewById(R.id.drama_four_num);
        drama_take = findViewById(R.id.drama_take);
        getanswer = findViewById(R.id.getanswer);
        hint = findViewById(R.id.hint);
        drama_practice_photo = findViewById(R.id.drama_practice_photo);
        practice_say_1 = findViewById(R.id.practice_say_1);
        practice_say_2 = findViewById(R.id.practice_say_2);
        drama_example_photo = findViewById(R.id.drama_example_photo);
        next_page = findViewById(R.id.next_page);
        cha_a = findViewById(R.id.cha_a);
        cha_b = findViewById(R.id.cha_b);


        drama_example_photo.setOnClickListener(listener);
        drama_four_num.setText(Integer.toString(nnn));

        fire_ph = FirebaseStorage.getInstance().getReference().child(MainActivity.drama_word_stu).child("EditFinish")
                .child(MainActivity.drama_word_num).child("edit_screen"+ drama_four_num.getText().toString() +".jpeg");
        fire_ph.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri uri = task.getResult();
                Glide.with(drama_example_photo.getContext()).load(uri).into(drama_example_photo);
            }
        });


        practice_say_1.setVisibility(View.INVISIBLE);
        practice_say_2.setVisibility(View.INVISIBLE);
        cha_a.setVisibility(View.INVISIBLE);
        cha_b.setVisibility(View.INVISIBLE);

        drama_name.setText(MainActivity.drama_word_stu + " " + MainActivity.drama_word_num);

        Init_AIConfiguration();
        InitSpeechRecognizer();
        TTS.init(getApplicationContext());

        drama_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //照片庫
                    if (ContextCompat.checkSelfPermission(ConversationPractice.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ConversationPractice.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {  //拍照
                    BringImagePicker();
                }
            }
        });

        getanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSpeechRecongizer();
            }
        });

        if(MainActivity.way_word == "劇本對話練習" && nnn == 1){
            n = true;
            if(MainActivity.character_word == "B"){
                sendMessage_conversation(drama_name.getText().toString() + "_" + MainActivity.character_word);
            }
        }

//        next_page.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                nnn += 1;
//                recreate();
//                if(nnn == 4){
//                    finish();
//                }
//            }
//        });

//        initPopupWindow();

    }

    //複寫返回鍵
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提醒");
        builder.setMessage("回上頁將會清除目前作業資料，確定返回上一頁？");
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConversationPractice.this.finish();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void initPopupWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.big_picture, null);
        popupWindow = new PopupWindow(view);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(false);
        big_pic= view.findViewById(R.id.big_pic);
        fire_ph = FirebaseStorage.getInstance().getReference().child(MainActivity.drama_word_stu).child("EditFinish")
                .child(MainActivity.drama_word_num).child("edit_screen"+ drama_four_num.getText().toString() +".jpeg");
        fire_ph.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri uri = task.getResult();
                Glide.with(big_pic.getContext()).load(uri).into(big_pic);
            }
        });
        ok = view.findViewById(R.id.good);
        ok.setOnClickListener(listener);
    }

    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.drama_example_photo:
                    initPopupWindow();
                    popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
                    break;

                case R.id.good:
                    popupWindow.dismiss();
                    break;
            }
        }
    };

    public void getHint(final View view){

        LayoutInflater inflater = LayoutInflater.from(ConversationPractice.this);
        final View v = inflater.inflate(R.layout.give_the_hint, null);
        final TextView text_hint = v.findViewById(R.id.text_hint);
        final ListView listView_hint = v.findViewById(R.id.listview_hint);
        final AlertDialog hint = new AlertDialog.Builder(ConversationPractice.this)
                .setTitle("提示")
                .setView(v)
                .setPositiveButton("確定", null)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

         //每一次點選hint就會+1,在看當時的方框數字nnn為多少判斷要放在哪一格的次數計算裡

        if(nnn == 1){
            hint_time1 += 1;
            DatabaseReference behaviorhintReference = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("Content").child("first_hint");
            behaviorhintReference.setValue(hint_time1);
        }else if(nnn == 2){
            hint_time2 += 1;
            DatabaseReference behaviorhintReference = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("Content").child("second_hint");
            behaviorhintReference.setValue(hint_time2);
        }else if(nnn == 3){
            hint_time3 += 1;
            DatabaseReference behaviorhintReference = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("Content").child("third_hint");
            behaviorhintReference.setValue(hint_time3);
        }else if(nnn == 4){
            hint_time4 += 1;
            DatabaseReference behaviorhintReference = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("Content").child("fourth_hint");
            behaviorhintReference.setValue(hint_time4);
        }

        fire_hint = FirebaseDatabase.getInstance().getReference().child("Hint")
                .child(MainActivity.drama_word_stu+"_"+MainActivity.drama_word_num+"_"+MainActivity.character_word+"_"+drama_four_num.getText().toString());
        fire_hint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i =0;
                sssttt = new String [(int)dataSnapshot.getChildrenCount()];
                for(DataSnapshot each : dataSnapshot.getChildren()){
//                    text_hint_list.append(each.getValue().toString());
//                    text_hint_list.append("\n");
                    sssttt[i] = each.getValue().toString();
                    i++;
                }
                String[] shh = sssttt;
                ArrayAdapter list_hint = new ArrayAdapter(ConversationPractice.this, android.R.layout.simple_list_item_1, shh);
                listView_hint.setAdapter(list_hint);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    };

    //CropImage切割照片
    private void BringImagePicker() {
        CropImage.activity().
                setGuidelines(CropImageView.Guidelines.ON).
                setAspectRatio(1,1).
                start(ConversationPractice.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                drama_practice_photo.setImageURI(imageUri);

                String dd = System.currentTimeMillis() + ".jpeg";
                fire_prctice_conversation_picture = FirebaseStorage.getInstance().getReference().child("PracticeConversation").child("學生"+ Student.Name +"號/"+dd);
                //continueWithTask取得圖片的url
                fire_prctice_conversation_picture.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw  task.getException();
                        }
                        return fire_prctice_conversation_picture.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri uri = task.getResult();
                        prctice_conversation_download_url = uri.toString();
                    }
                }).continueWithTask(new Continuation<Uri, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Uri> task) throws Exception {
                        //存每次的圖片
                        if(nnn == 1){
                            String photo1 = prctice_conversation_download_url;
                            DatabaseReference behaviorphotoReference = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("Content").child("first_photo");
                            behaviorphotoReference.setValue(photo1);
                        }else if(nnn == 2){
                            String photo2 = prctice_conversation_download_url;
                            DatabaseReference behaviorphotoReference = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("Content").child("second_photo");
                            behaviorphotoReference.setValue(photo2);
                        }else if(nnn == 3){
                            String photo3 = prctice_conversation_download_url;
                            DatabaseReference behaviorphotoReference = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("Content").child("third_photo");
                            behaviorphotoReference.setValue(photo3);
                        }else if(nnn == 4){
                            String photo4 = prctice_conversation_download_url;
                            DatabaseReference behaviorphotoReference = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("Content").child("fourth_photo");
                            behaviorphotoReference.setValue(photo4);
                        }
                        return null;
                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    //初始化聆聽者
    private void InitSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);//建構值
        speechRecognizer.setRecognitionListener(new ConversationPractice.SpeechListener());// 設定聆聽者
    }

    //開啟內建語音辨識 用事件方式
    private void StartSpeechRecongizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en-US");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,3);
        speechRecognizer.startListening(intent);
    }

    public void Init_AIConfiguration() {
        try {
            InputStream stream = getResources().openRawResource(R.raw.engineerbuilding_hiowhp_ac400b4c08c1);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials)credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            session = SessionName.of(projectId, uuid); // uuid = UUID.randomUUID().toString()
        } catch (Exception e) {
            e.printStackTrace();
        }

        TTS.init(getApplicationContext());
    }

    private void sendMessage_conversation(String request) {
        if(MainActivity.way_word == "劇本對話練習" ){
            if(MainActivity.character_word == "A"){
                cha_b.setText("A: ");
                cha_b.setVisibility(View.VISIBLE);
                practice_say_2.setText(request);
                practice_say_2.setVisibility(View.VISIBLE);
                practice_say_1.setVisibility(View.INVISIBLE);


            }else if(MainActivity.character_word == "B"){
                if(n == true){
                    Log.d(TAG, "step2 "+ n);
                    practice_say_2.setVisibility(View.INVISIBLE);
                }else{
                    cha_b.setText("B: ");
                    cha_b.setVisibility(View.VISIBLE);
                    practice_say_2.setText(request);
                    practice_say_2.setVisibility(View.VISIBLE);

                }
            }
        }

        // Java V2
        QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder()
                .setText(request).setLanguageCode("en-US")).build();
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
                    List<com.google.cloud.dialogflow.v2.Context> contents = result.getOutputContextsList();
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

                    // set user text

//                        String[] diff = new String[3];
//                        if (questioning == false) {
//                            similarity = BestDiff.getFamiliarity(FixedActivity.this, 0, questionId, queryText, diff);
//                        } else {
//                            similarity = BestDiff.getFamiliarity(FixedActivity.this, 1, questionId, queryText, diff);
//                        }
//                        diffList.add(diff);
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

//                        addChat(speech, 1);
                }
            }
        };
        new RequestJavaV2Task.CallRequestJavaV2Task(ConversationPractice.this, session, sessionsClient, queryInput, queryParameters, taskCompletedListener).execute();

    }

    public void callbackV2(DetectIntentResponse response){
        if(response != null){
            if(MainActivity.way_word == "劇本對話練習" ){
                if(MainActivity.character_word == "A"){
                        String reply = response.getQueryResult().getFulfillmentText();
                        cha_a.setText("B: ");
                        cha_a.setVisibility(View.VISIBLE);
                        practice_say_1.setText(reply);
                        practice_say_1.setVisibility(View.VISIBLE);

                        DatabaseReference sayagainword = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("PracticeSay");
                        sayagainword.push().setValue("B: " + reply);

                        TTS.speak(practice_say_1.getText().toString());


                        next_page.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                recreate();
                                if(nnn == 4){
                                    new AlertDialog.Builder(ConversationPractice.this)
                                            .setIcon(R.drawable.ic_launcher)
                                            .setTitle("恭喜完成了同學"+MainActivity.drama_word_stu+"號的"+MainActivity.drama_word_num+"對話練習!")
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            }).setNegativeButton("cancel",null).create()
                                            .show();
                                }else{
                                    //清除照片 清除台詞B並隱藏 清除編號
                                    drama_practice_photo.equals("");
                                    drama_four_num.getText().toString().equals("");
                                    practice_say_1.setText("");
                                    practice_say_1.setVisibility(View.INVISIBLE);
                                    cha_a.setText("");
                                    cha_a.setVisibility(View.INVISIBLE);
                                    cha_b.setText("");
                                    cha_b.setVisibility(View.INVISIBLE);
                                    practice_say_2.setText("");
                                    practice_say_2.setVisibility(View.INVISIBLE);
                                    drama_practice_photo.setImageURI(Uri.EMPTY);

                                    //重新編號
                                    nnn += 1;
                                    drama_four_num.setText(Integer.toString(nnn));

                                    fire_ph = FirebaseStorage.getInstance().getReference().child(MainActivity.drama_word_stu).child("EditFinish")
                                            .child(MainActivity.drama_word_num).child("edit_screen"+ drama_four_num.getText().toString() +".jpeg");
                                    //重新抓提示圖片
                                    fire_ph.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            Uri uri = task.getResult();
                                            Glide.with(drama_example_photo.getContext()).load(uri).into(drama_example_photo);
                                        }
                                    });

                                    //放入dialogflow
//                                cha_a.setText("B: ");
//                                cha_a.setVisibility(View.VISIBLE);
//                                practice_say_1.setText(reply);
//                                practice_say_1.setVisibility(View.VISIBLE);
//                                TTS.speak(practice_say_1.getText().toString());
                                }
                            }
                        });


                }else if(MainActivity.character_word == "B"){
                    if(n == true){
                        Log.d(TAG, "step3 "+ n);
                        String reply = response.getQueryResult().getFulfillmentText();
                        cha_a.setText("A: ");
                        cha_a.setVisibility(View.VISIBLE);
                        practice_say_1.setText(reply);
                        practice_say_1.setVisibility(View.VISIBLE);

                        //讓系統沉睡3秒鐘再執行,不然系統不會執行TTS
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TTS.speak(practice_say_1.getText().toString());
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        n = false;
                        Log.d(TAG, "step4 "+ n);
                    }else{
                        String reply = response.getQueryResult().getFulfillmentText();
                        next_page.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                recreate();
                                if(nnn == 4){
                                    new AlertDialog.Builder(ConversationPractice.this)
                                            .setIcon(R.drawable.ic_launcher)
                                            .setTitle("恭喜完成了同學"+MainActivity.drama_word_stu+"號的"+MainActivity.drama_word_num+"對話練習!")
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            }).setNegativeButton("cancel",null).create()
                                            .show();
                                }else{
                                    //清除照片 清除台詞B並隱藏 清除編號
                                    drama_practice_photo.equals("");
                                    drama_four_num.getText().toString().equals("");
                                    cha_b.setText("");
                                    cha_b.setVisibility(View.INVISIBLE);
                                    practice_say_2.setText("");
                                    practice_say_2.setVisibility(View.INVISIBLE);
                                    drama_practice_photo.setImageURI(Uri.EMPTY);

                                    //重新編號
                                    nnn += 1;
                                    drama_four_num.setText(Integer.toString(nnn));

                                    fire_ph = FirebaseStorage.getInstance().getReference().child(MainActivity.drama_word_stu).child("EditFinish")
                                            .child(MainActivity.drama_word_num).child("edit_screen"+ drama_four_num.getText().toString() +".jpeg");
                                    //重新抓提示圖片
                                    fire_ph.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            Uri uri = task.getResult();
                                            Glide.with(drama_example_photo.getContext()).load(uri).into(drama_example_photo);
                                        }
                                    });

                                    //放入dialogflow
                                    cha_a.setText("A: ");
                                    cha_a.setVisibility(View.VISIBLE);
                                    practice_say_1.setText(reply);
                                    practice_say_1.setVisibility(View.VISIBLE);
                                    TTS.speak(practice_say_1.getText().toString());

                                    DatabaseReference sayagainword = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("PracticeSay");
                                    sayagainword.push().setValue("A: " + reply);
                                }
                            }
                        });
                    }

                }
            }
        }else{
            Log.d(TAG, "REPLY IS NULL");
        }

    }

    private Bitmap getBitmap(View view) throws Exception {
        View screenView = getWindow().getDecorView();
        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache(); //獲取螢幕整張圖片
        Bitmap bitmap = screenView.getDrawingCache();
        if (bitmap != null) {
            //需要擷取的長和寬
            int outWidth = screenView.getWidth();
            int outHeight =screenView.getHeight();
            //獲取需要截圖部分的在螢幕上的座標(view的左上角座標)
            int[] viewLocationArray = new int[2];
            screenView.getLocationOnScreen(viewLocationArray);
            //從螢幕整張圖片中擷取指定區域
            bitmap = Bitmap.createBitmap(bitmap,
                    viewLocationArray[0], viewLocationArray[1],
                    outWidth, outHeight);
            Log.e(TAG, "OKKKK:" +Integer.toString(bitmap.getHeight()) +" ");
        }
        return bitmap;
    }


    //事件的內建語音辨識聆聽者
    private class SpeechListener implements RecognitionListener
    {
        @Override
        public void onReadyForSpeech(Bundle params) {
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d("Speech","Start...");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.d("Speech","Received...");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d("Speech","End...");
        }

        @Override
        public void onError(int error) {
        }

        @Override
        public void onResults(Bundle results) {

            String color = "green";//字體顏色
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            final String word =data.get(0).toString().trim();

            sendMessage_conversation(word);

            if(nnn == 1){
                DatabaseReference behaviorwordReference = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("Content").child("first_word");
                behaviorwordReference.setValue(word);

                DatabaseReference sayagainword = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("PracticeSay");
                sayagainword.push().setValue(word);
            }else if(nnn == 2){
                DatabaseReference behaviorwordReference = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("Content").child("second_word");
                behaviorwordReference.setValue(word);

                DatabaseReference sayagainword = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("PracticeSay");
                sayagainword.push().setValue(word);
            }else if(nnn == 3){
                DatabaseReference behaviorwordReference = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("Content").child("third_word");
                behaviorwordReference.setValue(word);

                DatabaseReference sayagainword = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("PracticeSay");
                sayagainword.push().setValue(word);
            }else if(nnn == 4){
                DatabaseReference behaviorwordReference = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("Content").child("fourth_word");
                behaviorwordReference.setValue(word);

                DatabaseReference sayagainword = MainActivity.fire_conversation_practice.child(MainActivity.practice_id_key).child("PracticeSay");
                sayagainword.push().setValue(word);
            }

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }

    @Override
    protected  void onStop(){
        super.onStop();
        TTS.stop();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        InitSpeechRecognizer();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.stopListening();
        speechRecognizer.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        speechRecognizer.stopListening();
        speechRecognizer.destroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        InitSpeechRecognizer();
    }

}
