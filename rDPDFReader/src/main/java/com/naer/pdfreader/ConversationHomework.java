package com.naer.pdfreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

import Model.DescribeClickTime;
import Model.DescribeData;

import static com.naer.pdfreader.DialogActivity.TAG;

public class ConversationHomework extends Activity {

    private Spinner homework_number;
    private Button homework_adddialog1;
    private Button homework_adddialog2;
    private Button homework_takephoto;
    private Button homework_finish;
    private ImageView homework_photo;
    private String spinner_homework_word;
    private TextView homework_say1;
    private TextView homework_say2;

    private StorageReference fire_conversation_pic; //????????????
    private StorageReference fire_hw_finish_pic; //??????????????????
    private String download_url = "";

    private MediaRecorder recorder;
    private MediaPlayer player;
    //?????? A ??????????????????
    private String hw_pathword_a;
    //?????? B ??????????????????
    private String hw_pathword_b;
    private StorageReference fire_conversation_record;
    private String hwrecord_download_url_a = "";
    private String hwrecord_download_url_b = "";
    private String Link;

    int timeCount;
    Uri cutViewuri;
    private StorageReference fire_call_check_voice;

    //?????????????????????????????????, ????????????????????????????????????????????????
    private boolean isPress = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//???????????????
        setContentView(R.layout.activity_conversation_homework);

        homework_number = findViewById(R.id.homework_number);
        homework_adddialog1 = findViewById(R.id.homework_adddialog1);
        homework_adddialog2 = findViewById(R.id.homework_adddialog2);
        homework_takephoto = findViewById(R.id.homework_takephoto);
        homework_finish = findViewById(R.id.homework_finish);
        homework_photo = findViewById(R.id.homework_photo);
        homework_say1 = findViewById(R.id.homework_say1);
        homework_say2 = findViewById(R.id.homework_say2);

        homework_say1.setOnTouchListener(hw_say1Listener);
        homework_say2.setOnTouchListener(hw_say2Listener);

        //--------??????????????????----------
        final String[] list = {"Homework_2_3", "Homework_3_2", "Homework_3_3", "Homework_4_2", "Homework_4_3"};
        ArrayAdapter<String> numberList=new ArrayAdapter<String>(ConversationHomework.this,
                android.R.layout.simple_spinner_dropdown_item,
                list);
        homework_number.setAdapter(numberList);
        homework_number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//????????????Spinner????????????
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_homework_word = homework_number.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//????????????
                spinner_homework_word = homework_number.getSelectedItem().toString();
            }
        });

        //??????
        homework_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //?????????
                    if (ContextCompat.checkSelfPermission(ConversationHomework.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ConversationHomework.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {  //??????
                    BringImagePicker();
                }
            }
        });

        homework_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dd = System.currentTimeMillis() + ".jpeg";
                fire_hw_finish_pic = FirebaseStorage.getInstance().getReference().child(Student.Name).child(spinner_homework_word + "/" + "finish_" + spinner_homework_word + ".jpeg");
                try {
                    cutViewuri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), getBitmap(view), null,null));
                    fire_hw_finish_pic.putFile(cutViewuri);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LayoutInflater inflater = LayoutInflater.from(ConversationHomework.this);
                final View v = inflater.inflate(R.layout.check_homework, null);
                final ImageView check_photo = (v.findViewById(R.id.check_photo));
                final Button check_voice_a = (v.findViewById(R.id.check_voice_a));
                final Button check_voice_b = (v.findViewById(R.id.check_voice_b));
                final TextView check_a_text = (v.findViewById(R.id.check_a_text));
                final TextView check_b_text = (v.findViewById(R.id.check_b_text));

                final AlertDialog check = new AlertDialog.Builder(ConversationHomework.this)
                        .setTitle("??????????????????????????????")
                        .setView(v)
                        .setPositiveButton("??????",null)
                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                check.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ConversationHomework.this.finish();
                            }
                        });

                Glide.with(check_photo.getContext()).load(cutViewuri).into(check_photo);

                check_voice_a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fire_call_check_voice = FirebaseStorage.getInstance().getReference().child(Student.Name).
                                child(spinner_homework_word).child(Student.Name +"_"+ spinner_homework_word+"_A.amr");
                        fire_call_check_voice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Link = uri.toString();
                                Toast.makeText(ConversationHomework.this, "??????A?????????", Toast.LENGTH_SHORT).show();
                                player = new MediaPlayer();
                                try {
                                    player.setDataSource(Link);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    player.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.start();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(ConversationHomework.this, "???????????????????????????!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                check_voice_b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fire_call_check_voice = FirebaseStorage.getInstance().getReference().child(Student.Name).
                                child(spinner_homework_word+"/"+ Student.Name +"_"+ spinner_homework_word+"_B.amr");
                        fire_call_check_voice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Link = uri.toString();
                                Toast.makeText(ConversationHomework.this, "??????B?????????", Toast.LENGTH_SHORT).show();
                                player = new MediaPlayer();
                                try {
                                    player.setDataSource(Link);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    player.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.start();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(ConversationHomework.this, "???????????????????????????!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

    }

    //CropImage????????????
    private void BringImagePicker() {
        CropImage.activity().
                setGuidelines(CropImageView.Guidelines.ON).
                setAspectRatio(4,3).
                start(ConversationHomework.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                homework_photo.setImageURI(imageUri);
                //Bitmap bitmap = decodeUriiAsBimap(this,imageUri);
                String dd = System.currentTimeMillis() + ".jpeg";
                fire_conversation_pic = FirebaseStorage.getInstance().getReference().child(Student.Name).child(spinner_homework_word + "/" + dd);

                //continueWithTask???????????????url
                fire_conversation_pic.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw  task.getException();
                        }
                        return fire_conversation_pic.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri uri = task.getResult();
                        download_url = uri.toString();
                    }
                }).continueWithTask(new Continuation<Uri, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Uri> task) throws Exception {
//                        String push_key = fire_describedata.push().getKey();
//
//                        //?????????Task??????????????????????????????????????????FireBase
//                        savedescribe.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                DescribeData describeData = new DescribeData(
//                                        "??????"+Student.Name+"???",
//                                        PlaceName.getText().toString(),
//                                        studentdescribe.getText().toString(),
//                                        download_url
//                                );
//                                DescribeClickTime describeClickTime = new DescribeClickTime(
//                                        vocabulary_clickTime, phrase_clickTime, sentence_clickTime, see_other_clickTime);
//
//                                fire_describedata.child(choose_type_word).child(push_key).child("Content").setValue(describeData); //??????????????????
//                                fire_describedata.child(choose_type_word).child(push_key).child("ClickTime").setValue(describeClickTime); //?????????????????????????????????
//                                Toast.makeText(DescribeActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                        return null;
                    }
                });
                Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
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

    public void hw_check_Click(final View view) {
        LayoutInflater inflater = LayoutInflater.from(ConversationHomework.this);
        final View v = inflater.inflate(R.layout.check_homework, null);
        final ImageView check_photo = (v.findViewById(R.id.check_photo));
        final Button check_voice_a = (v.findViewById(R.id.check_voice_a));
        final Button check_voice_b = (v.findViewById(R.id.check_voice_b));

        final AlertDialog check = new AlertDialog.Builder(ConversationHomework.this)
                .setTitle("??????????????????????????????")
                .setView(v)
                .setPositiveButton("??????",null)
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

        check.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConversationHomework.this.finish();
                    }
                });

        Glide.with(check_photo.getContext()).load(cutViewuri).into(check_photo);

        check_voice_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fire_call_check_voice = FirebaseStorage.getInstance().getReference().child(Student.Name).
                        child(spinner_homework_word+"/"+ Student.Name +"_"+ spinner_homework_word+"_A.amr");
                fire_call_check_voice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(ConversationHomework.this, Link, Toast.LENGTH_SHORT).show();
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(Link);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ConversationHomework.this, "???????????????????????????!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        check_voice_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fire_call_check_voice = FirebaseStorage.getInstance().getReference().child(Student.Name).
                        child(spinner_homework_word+"/"+ Student.Name +"_"+ spinner_homework_word+"_B.amr");
                fire_call_check_voice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(ConversationHomework.this, Link, Toast.LENGTH_SHORT).show();
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(Link);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ConversationHomework.this, "???????????????????????????!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void hw_lineA_Click(final View view) {
        findViewById(R.id.homework_adddialog1).setEnabled(false);

        LayoutInflater inflater = LayoutInflater.from(ConversationHomework.this);
        final View v = inflater.inflate(R.layout.builddrama, null);
        final EditText editText1 = (v.findViewById(R.id.editText1));
        final Button record1 = (v.findViewById(R.id.recordplayer1));
        final Button stop1 = (v.findViewById(R.id.stopplayer1));
        final Button play1 = (v.findViewById(R.id.playplayer1));
        final TextView showtime1 = (v.findViewById(R.id.show_time1));

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timeCount++;
                String str="????????????..." + showTimeCount((long)timeCount);
                showtime1.setText(str);
                handler.postDelayed(this, 1000);
            }
        };

        final AlertDialog drama = new AlertDialog.Builder(ConversationHomework.this)
                .setTitle("??????A")
                .setView(v)
                .setCancelable(false)
                .setPositiveButton("??????",null)
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        findViewById(R.id.homework_adddialog1).setEnabled(true);
                        dialog.dismiss();
                    }
                })
                .show();

        drama.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //??????????????????????????????
                        if(!(TextUtils.isEmpty(editText1.getText().toString()))){
                            homework_say1.setText(editText1.getText().toString());
                            homework_say1.setVisibility(View.VISIBLE);
                            findViewById(R.id.homework_adddialog1).setEnabled(true);
                            drama.dismiss();
                            Toast.makeText(getApplicationContext(), " A ???????????????", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "???????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        record1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPress == true){
                    recorder = new MediaRecorder();// new???MediaRecorder??????
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    // ??????MediaRecorder????????????????????????
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
                    // ??????MediaRecorder?????????????????????
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    // ??????MediaRecorder????????????????????????amr.

                    recorder.setOutputFile("/sdcard/" + hw_pathword_a);

                    // ??????????????????????????????????????????
                    try {
                        recorder.prepare();// ????????????
                        recorder.start();// ????????????
                        Toast.makeText(ConversationHomework.this, "?????????????????? A ??????", Toast.LENGTH_SHORT).show();

                        timeCount = 0;
                        handler.post(runnable);

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    isPress = false;
                }else{
                    Toast.makeText(ConversationHomework.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
        hw_pathword_a = Student.Name + "_" + spinner_homework_word + "_" + "A.amr";
        fire_conversation_record = FirebaseStorage.getInstance().getReference().child(Student.Name).child(spinner_homework_word + "/" + hw_pathword_a);
        //??????A??????????????????
        stop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isPress == false){
                    recorder.stop();// ????????????
                    Toast.makeText(ConversationHomework.this, "?????????????????? A ??????", Toast.LENGTH_SHORT).show();
                    handler.removeCallbacks(runnable);
                    showtime1.setText("????????????");

                    //??????????????????Firebase, ?????????:"??????/????????????1", ?????????"????????????_????????????1_1_A.amr"
                    //?????????continueWithTask???????????????url, ????????????????????????????????????
                    //?????????????????????????????????
                    Uri recoed_file_a = Uri.fromFile(new File("/sdcard/" + hw_pathword_a));
                    fire_conversation_record.putFile(recoed_file_a).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw  task.getException();
                            }
                            return fire_conversation_record.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri uri = task.getResult();
                            hwrecord_download_url_a = uri.toString();
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

                    isPress = true;
                }else{
                    Toast.makeText(ConversationHomework.this, "??????????????????", Toast.LENGTH_SHORT).show();
                }

            }
        });


        play1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fire_conversation_record.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(ConversationHomework.this, "???????????????", Toast.LENGTH_SHORT).show();
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(Link);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ConversationHomework.this, "????????????", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //??????B????????????
    public void hw_lineB_Click(final View view){
        findViewById(R.id.homework_adddialog2).setEnabled(false);

        LayoutInflater inflater = LayoutInflater.from(ConversationHomework.this);
        final View v = inflater.inflate(R.layout.builddrama2, null);
        final EditText editText2 =(v.findViewById(R.id.editText2));
        final Button record2 = (v.findViewById(R.id.recordplayer2));
        final Button stop2 = (v.findViewById(R.id.stopplayer2));
        final Button play2 = (v.findViewById(R.id.playplayer2));
        final TextView showtime2 = (v.findViewById(R.id.show_time2));

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timeCount++;
                String str="????????????..." + showTimeCount((long)timeCount);
                showtime2.setText(str);
                handler.postDelayed(this, 1000);
            }
        };


        final AlertDialog drama = new AlertDialog.Builder(ConversationHomework.this)
                .setTitle("?????? B ????????????")
                .setView(v)
                .setCancelable(false)
                .setPositiveButton("??????",null)
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        findViewById(R.id.homework_adddialog2).setEnabled(true);
                        dialog.dismiss();
                    }
                })
                .show();

        drama.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //??????????????????????????????
                        if(!(TextUtils.isEmpty(editText2.getText().toString()))){
                            homework_say2.setText(editText2.getText().toString());
                            homework_say2.setVisibility(View.VISIBLE);
                            findViewById(R.id.homework_adddialog2).setEnabled(true);
                            drama.dismiss();
                            Toast.makeText(getApplicationContext(), " B ???????????????", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "???????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //??????A??????????????????
        record2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPress == true){
                    recorder = new MediaRecorder();// new???MediaRecorder??????
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    // ??????MediaRecorder????????????????????????
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
                    // ??????MediaRecorder?????????????????????
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    // ??????MediaRecorder????????????????????????amr.

                    recorder.setOutputFile("/sdcard/" + hw_pathword_b);

                    // ??????????????????????????????????????????
                    try {
                        recorder.prepare();// ????????????
                        recorder.start();// ????????????
                        Toast.makeText(ConversationHomework.this, "?????????????????? B ??????", Toast.LENGTH_SHORT).show();

                        timeCount = 0;
                        handler.post(runnable);

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    isPress = false;
                }else{
                    Toast.makeText(ConversationHomework.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                }

            }
        });
        hw_pathword_b = Student.Name + "_" + spinner_homework_word + "_" + "B.amr";
        fire_conversation_record = FirebaseStorage.getInstance().getReference().child(Student.Name).child(spinner_homework_word+"/"+hw_pathword_b);

        stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPress == false){
                    recorder.stop();// ????????????
                    Toast.makeText(ConversationHomework.this, "?????????????????? B ??????", Toast.LENGTH_SHORT).show();

                    handler.removeCallbacks(runnable);
                    showtime2.setText("????????????");

                    //??????????????????Firebase, ?????????:"??????/????????????1", ?????????"????????????_????????????1_1_B.amr"
                    //?????????continueWithTask???????????????url, ????????????????????????????????????
                    //?????????????????????????????????
                    Uri recoed_file_b = Uri.fromFile(new File("/sdcard/" + hw_pathword_b));
                    fire_conversation_record.putFile(recoed_file_b).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw  task.getException();
                            }
                            return fire_conversation_record.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri uri = task.getResult();
                            hwrecord_download_url_b = uri.toString();
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

                    isPress = true;
                }else{
                    Toast.makeText(ConversationHomework.this, "??????????????????", Toast.LENGTH_SHORT).show();
                }

            }
        });

        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fire_conversation_record.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(Link);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ConversationHomework.this, "????????????", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private Bitmap getBitmap(View view) throws Exception {
        View screenView = getWindow().getDecorView();
        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache(); //????????????????????????
        Bitmap bitmap = screenView.getDrawingCache();
        if (bitmap != null) {
            //????????????????????????
            int outWidth = homework_photo.getWidth();
            int outHeight =homework_photo.getHeight();
            //????????????????????????????????????????????????(view??????????????????)
            int[] viewLocationArray = new int[2];
            homework_photo.getLocationOnScreen(viewLocationArray);
            //??????????????????????????????????????????
            bitmap = Bitmap.createBitmap(bitmap,
                    viewLocationArray[0], viewLocationArray[1],
                    outWidth, outHeight);
            Log.e(TAG, "OKKKK:" +Integer.toString(bitmap.getHeight()) +" ");
        }
        return bitmap;
    }

    //?????? A ?????????
    private View.OnTouchListener hw_say1Listener = new View.OnTouchListener() {
        private float x, y;    // ?????????????????????X,Y?????????
        private int mx, my; // ??????????????????X ,Y???????????????
        private int lastX, lastY; //??????????????????????????????

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Log.e("View", v.toString());
            switch (event.getAction()) {          //?????????????????????

                case MotionEvent.ACTION_DOWN:// ???????????????
                    x = event.getX();                  //?????????X?????????
                    y = event.getY();                  //?????????Y?????????
                    break;
                case MotionEvent.ACTION_MOVE:// ???????????????
                    //getX()????????????????????????(View)?????????
                    //getRawX()????????????????????????????????????????????????
                    mx = (int) (event.getRawX() - x);
                    my = (int) (event.getRawY() - y);
                    setRelativeViewLocation(homework_say1, mx, my, mx + v.getWidth(), my + v.getHeight());
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
//                    String ResoltTTS =say1.getText().toString();
//                    TTS.speak(ResoltTTS);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;

            }
            Log.e("address", String.valueOf(mx) + "~~" + String.valueOf(my)); // ??????????????????
            return true;
        }
        private void setRelativeViewLocation(View view, int left, int top, int right, int bottom) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(right - left, bottom - top);
            Log.e("params", params.width + "~~" +Integer.toString(right) + Integer.toString(left));
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params.setMargins(left, top, 0, 0);
            view.setLayoutParams(params);
        }
    };

    //?????? B ?????????
    private View.OnTouchListener hw_say2Listener = new View.OnTouchListener() {
        private float x, y;    // ?????????????????????X,Y?????????
        private int mx, my; // ??????????????????X ,Y???????????????

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Log.e("View", v.toString());
            switch (event.getAction()) {          //?????????????????????

                case MotionEvent.ACTION_DOWN:// ???????????????
                    x = event.getX();                  //?????????X?????????
                    y = event.getY();                  //?????????Y?????????
                    v.bringToFront();
                    break;

                case MotionEvent.ACTION_MOVE:// ???????????????
                    //getX()????????????????????????(View)?????????
                    //getRawX()????????????????????????????????????????????????
                    mx = (int) (event.getRawX() - x);
                    my = (int) (event.getRawY() - y);
                    setRelativeViewLocation(homework_say2, mx, my, mx + v.getWidth(), my + v.getHeight());
                    break;
                case MotionEvent.ACTION_UP:
//                    String SpeechTTS =say2.getText().toString();
//                    TTS.speak(SpeechTTS);
                    break;

            }
            Log.e("address", String.valueOf(mx) + "~~" + String.valueOf(my)); // ??????????????????

            return true;
        }

        private void setRelativeViewLocation(View view, int left, int top, int right, int bottom) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(right - left, bottom - top);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params.setMargins(left, top, 0, 0);
            view.setLayoutParams(params);
        }

    };


}
