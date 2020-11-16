package com.naer.pdfreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.io.IOException;

import Model.TimeOfSee;

public class DramaAchievement extends Activity {

    private Spinner drama_stu_num;
    private Spinner drama_dramawork_num;
    private ImageView drama_four_image;
    private Button searchdrama;
    private Button drama_voice_1_a;
    private Button drama_voice_1_b;
    private Button drama_voice_2_a;
    private Button drama_voice_2_b;
    private Button drama_voice_3_a;
    private Button drama_voice_3_b;
    private Button drama_voice_4_a;
    private Button drama_voice_4_b;


    private String drama_studentNumber_word;
    private String drama_dramaNumber_word;

    private StorageReference fire_callImage;
    private StorageReference fire_callVoice;
    private DatabaseReference fire_timeOfSee;
    private DatabaseReference fire_calltts;

    private MediaPlayer player;
    private String Link;

    private String id_kkk;//抓取此次存取觀看紀錄的key

    private boolean isPracticeTextbook = false;

    //紀錄每個錄音聽取的次數
    public int a1 = 0;
    public int b1 = 0;
    public int a2 = 0;
    public int b2 = 0;
    public int a3 = 0;
    public int b3 = 0;
    public int a4 = 0;
    public int b4 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_drama_achievement);

        TTS.init(getApplicationContext());

        drama_stu_num = findViewById(R.id.drama_stu_num);
        drama_dramawork_num = findViewById(R.id.drama_dramawork_num);
        drama_four_image = findViewById(R.id.drama_four_image);
        searchdrama = findViewById(R.id.btn_searchdrama);

        drama_voice_1_a = findViewById(R.id.drama_voice_1_a);
        drama_voice_1_b = findViewById(R.id.drama_voice_1_b);
        drama_voice_2_a = findViewById(R.id.drama_voice_2_a);
        drama_voice_2_b = findViewById(R.id.drama_voice_2_b);
        drama_voice_3_a = findViewById(R.id.drama_voice_3_a);
        drama_voice_3_b = findViewById(R.id.drama_voice_3_b);
        drama_voice_4_a = findViewById(R.id.drama_voice_4_a);
        drama_voice_4_b = findViewById(R.id.drama_voice_4_b);

        drama_voice_1_a.setVisibility(View.INVISIBLE);
        drama_voice_1_b.setVisibility(View.INVISIBLE);
        drama_voice_2_a.setVisibility(View.INVISIBLE);
        drama_voice_2_b.setVisibility(View.INVISIBLE);
        drama_voice_3_a.setVisibility(View.INVISIBLE);
        drama_voice_3_b.setVisibility(View.INVISIBLE);
        drama_voice_4_a.setVisibility(View.INVISIBLE);
        drama_voice_4_b.setVisibility(View.INVISIBLE);


        //------------學生座號------------
        final String[] stunum_list = {"111", "01", "4", "06", "10", "32", "36", "40", "43"};
        ArrayAdapter<String> stu_numberList=new ArrayAdapter<String>(DramaAchievement.this,
                android.R.layout.simple_spinner_dropdown_item, stunum_list);
        drama_stu_num.setAdapter(stu_numberList);
        drama_stu_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                drama_studentNumber_word = drama_stu_num.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
                drama_studentNumber_word = drama_stu_num.getSelectedItem().toString();
            }
        });


        //------------劇本編號------------
        final String[] dramanum_list = {"Drama_1", "Drama_2", "Drama_3"};
        ArrayAdapter<String> dra_numberList=new ArrayAdapter<String>(DramaAchievement.this,
                android.R.layout.simple_spinner_dropdown_item, dramanum_list);
        drama_dramawork_num.setAdapter(dra_numberList);
        drama_dramawork_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                drama_dramaNumber_word = drama_dramawork_num.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
                drama_dramaNumber_word = drama_dramawork_num.getSelectedItem().toString();
            }
        });

        searchdrama.setOnClickListener(callAllInformation);
        drama_voice_1_a.setOnClickListener(calltheVoice1_a);
        drama_voice_1_b.setOnClickListener(calltheVoice1_b);
        drama_voice_2_a.setOnClickListener(calltheVoice2_a);
        drama_voice_2_b.setOnClickListener(calltheVoice2_b);
        drama_voice_3_a.setOnClickListener(calltheVoice3_a);
        drama_voice_3_b.setOnClickListener(calltheVoice3_b);
        drama_voice_4_a.setOnClickListener(calltheVoice4_a);
        drama_voice_4_b.setOnClickListener(calltheVoice4_b);
    }

    private View.OnClickListener callAllInformation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            fire_callImage = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child("/Four-frame/").child(drama_dramaNumber_word);
            fire_callImage.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri uri = task.getResult();
                    Glide.with(drama_four_image.getContext()).load(uri.toString()).into(drama_four_image);
                }
            });

            drama_voice_1_a.setVisibility(View.VISIBLE);
            drama_voice_1_b.setVisibility(View.VISIBLE);
            drama_voice_2_a.setVisibility(View.VISIBLE);
            drama_voice_2_b.setVisibility(View.VISIBLE);
            drama_voice_3_a.setVisibility(View.VISIBLE);
            drama_voice_3_b.setVisibility(View.VISIBLE);
            drama_voice_4_a.setVisibility(View.VISIBLE);
            drama_voice_4_b.setVisibility(View.VISIBLE);
            fire_timeOfSee = FirebaseDatabase.getInstance().getReference()
                    .child("學生" + Student.Name + "號").child("See_Drama");

            id_kkk = fire_timeOfSee.push().getKey();

            a1 = 0;
            b1 = 0;
            a2 = 0;
            b2 = 0;
            a3 = 0;
            b3 = 0;
            a4 = 0;
            b4 = 0;

            TimeOfSee timeOfSee = new TimeOfSee(drama_studentNumber_word + "_" + drama_dramaNumber_word, a1, b1, a2, b2, a3, b3, a4, b4);
            fire_timeOfSee.child(id_kkk).setValue(timeOfSee);

        }
    };

    private View.OnClickListener calltheVoice1_a = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            a1+=1;
            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("1").child("playerA_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_1_a");
                            a1Reference.setValue(a1);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_1_A.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
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
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_1_a");
                                    a1Reference.setValue(a1);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }



        }
    };

    private View.OnClickListener calltheVoice1_b = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            b1 += 1;
            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("1").child("playerB_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_1_b");
                            a1Reference.setValue(b1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_1_B.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
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
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_1_b");
                                    a1Reference.setValue(b1);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    private View.OnClickListener calltheVoice2_a = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            a2 += 1;
            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("2").child("playerA_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_2_a");
                            a1Reference.setValue(a2);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_2_A.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
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
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_2_a");
                                    a1Reference.setValue(a2);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    private View.OnClickListener calltheVoice2_b = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            b2 += 1;

            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("2").child("playerB_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_2_b");
                            a1Reference.setValue(b2);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_2_B.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
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
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_2_b");
                                    a1Reference.setValue(b2);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };


    private View.OnClickListener calltheVoice3_a = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            a3 += 1;

            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("3").child("playerA_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_3_a");
                            a1Reference.setValue(a3);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_3_A.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
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
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_3_a");
                                    a1Reference.setValue(a3);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    private View.OnClickListener calltheVoice3_b = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            b3 += 1;

            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("3").child("playerB_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_3_b");
                            a1Reference.setValue(b3);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_3_B.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
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
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_3_b");
                                    a1Reference.setValue(b3);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    private View.OnClickListener calltheVoice4_a = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            a4 +=1;

            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("4").child("playerA_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_4_a");
                            a1Reference.setValue(a4);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_4_A.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
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
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_4_a");
                                    a1Reference.setValue(a4);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    private View.OnClickListener calltheVoice4_b = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            b4 += 1;

            if(drama_studentNumber_word == "111" && (drama_dramaNumber_word == "Drama_1" || drama_dramaNumber_word == "Drama_2")){
                fire_calltts = FirebaseDatabase.getInstance().getReference().child("學生"+drama_studentNumber_word+"號").
                        child(drama_dramaNumber_word).child("4").child("playerB_text");
                fire_calltts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence_firebase = dataSnapshot.getValue().toString();
                        Toast.makeText(DramaAchievement.this, sentence_firebase, Toast.LENGTH_SHORT).show();
                        TTS.speak(sentence_firebase);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            DatabaseReference a1Reference = fire_timeOfSee.child("listen_4_b");
                            a1Reference.setValue(b4);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }else{
                fire_callVoice = FirebaseStorage.getInstance().getReference().child(drama_studentNumber_word).child(drama_dramaNumber_word).
                        child(drama_studentNumber_word + "_" + drama_dramaNumber_word + "_4_B.amr");
                fire_callVoice.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        Toast.makeText(DramaAchievement.this, Link, Toast.LENGTH_SHORT).show();
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
                        fire_timeOfSee = FirebaseDatabase.getInstance().getReference().child("學生" + Student.Name + "號").child("See_Drama").child(id_kkk);
                        fire_timeOfSee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                                    DatabaseReference a1Reference = fire_timeOfSee.child("listen_4_b");
                                    a1Reference.setValue(b4);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DramaAchievement.this, "此對話並沒有錄音喔!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };
}
