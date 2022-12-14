package com.naer.pdfreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetDataShow extends Activity {

    private Spinner num_data;
    private String num_str;
    private TextView time_describe_text;
    private TextView time_describe;
    private ListView data_list;
    private TextView time_speech_text;
    private TextView time_speech;
    private TextView time_listen_tts_text;
    private TextView time_lis_tts;
    private TextView time_share_click_text;
    private TextView time_share_click;
    private TextView time_see_self_tts_text;
    private TextView time_see_self_tts;
    private TextView time_see_self_text;
    private TextView time_see_self;
    private TextView time_see_other_tts_text;
    private TextView time_see_other_tts;
    private TextView time_see_other_text;
    private TextView time_see_other;
    private Button clicktoget;

    private DatabaseReference fire_get_the_data;
    int describe_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data_show);

        clicktoget = findViewById(R.id.clicktoget);
        num_data = findViewById(R.id.num_data);
        time_describe_text = findViewById(R.id.time_describe_text);
        time_describe  = findViewById(R.id.time_describe);
        data_list  = findViewById(R.id.data_list);
        time_speech_text  = findViewById(R.id.time_speech_text);
        time_speech  = findViewById(R.id.time_speech);
        time_listen_tts_text  = findViewById(R.id.time_listen_tts_text);
        time_lis_tts  = findViewById(R.id.time_lis_tts);
        time_share_click_text  = findViewById(R.id.time_share_click_text);
        time_share_click  = findViewById(R.id.time_share_click);
        time_see_self_tts_text  = findViewById(R.id.time_see_self_tts_text);
        time_see_self_tts   = findViewById(R.id.time_see_self_tts);
        time_see_self_text   = findViewById(R.id.time_see_self_text);
        time_see_self   = findViewById(R.id.time_see_self);
        time_see_other_tts_text   = findViewById(R.id.time_see_other_tts_text);
        time_see_other_tts    = findViewById(R.id.time_see_other_tts);
        time_see_other_text    = findViewById(R.id.time_see_other_text);
        time_see_other    = findViewById(R.id.time_see_other);


        //??????????????????
        final String[] activity_final_list = {"??????1???", "??????2???", "??????3???", "??????4???", "??????5???",
                "??????6???", "??????7???", "??????8???", "??????9???", "??????10???", "??????11???", "??????12???",
                "??????31???", "??????32???", "??????33???", "??????34???", "??????35???", "??????36???", "??????37???",
                "??????38???", "??????39???", "??????40???", "??????41???", "??????42???", "??????43???", "??????44???"};
        ArrayAdapter<String> stu_num_List=new ArrayAdapter<String>(GetDataShow.this,
                android.R.layout.simple_spinner_dropdown_item,
                activity_final_list);
        num_data.setAdapter(stu_num_List);
        num_data.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//????????????Spinner????????????
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num_str = num_data.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//????????????
                num_str = num_data.getSelectedItem().toString();
            }
        });

        fire_get_the_data = FirebaseDatabase.getInstance().getReference();

        clicktoget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fire_get_the_data.child(num_str).child("DescribeData");
                fire_get_the_data.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                                if(dataSnapshot2.getKey().contains("-")){
                                    describe_time++;
                                }
                            }
                        }
                        time_describe.setText(Integer.toString(describe_time));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
