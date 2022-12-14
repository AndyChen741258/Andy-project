package com.naer.pdfreader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Model.DescribeData;
import Model.DescribeList;

import static com.naer.pdfreader.DialogActivity.TAG;

public class DramaRecycleView extends Activity{

    private RecyclerView recyclerView;
    private Spinner search_my_or_other;
    private Spinner search_place;
    private Spinner search_type;
    private Button search;
    private String stu_spinner_word;
    private String place_spinner_word;
    public static String type_spinner_word;
    public static String people_spinner_word;

    private CardViewData mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DescribeData> list_describe;
    private ArrayList<String> list_id;
    DatabaseReference fire_recycler;
    DatabaseReference fire_see_time; //???????????????????????????????????????


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drama_recycle_view);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        search_my_or_other = findViewById(R.id.search_my_or_other);
        search_place = findViewById(R.id.search_place);
        search_type = findViewById(R.id.search_type);
        search = findViewById(R.id.btn_searchdescribe);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        search_place.setVisibility(View.INVISIBLE);
        search.setVisibility(View.INVISIBLE);
        search_type.setVisibility(View.INVISIBLE);

        list_describe = new ArrayList<DescribeData>();
        list_id = new ArrayList<String>();

        TTS.init(getApplicationContext());

        if(DescribeActivity.isWantToLearn == true){
            Toast.makeText(this, Boolean.toString(DescribeActivity.isWantToLearn), Toast.LENGTH_SHORT).show();
            search_my_or_other.setVisibility(View.INVISIBLE);
            search_place.setVisibility(View.INVISIBLE);
            search_type.setVisibility(View.INVISIBLE);
            search.setVisibility(View.INVISIBLE);

            list_id.clear();
            list_describe.clear();
            Toast.makeText(this, DescribeActivity.PlaceName.getText().toString(), Toast.LENGTH_SHORT).show();
            fire_recycler = FirebaseDatabase.getInstance().getReference().child("DescribeData_place")
                    .child(DescribeActivity.PlaceName.getText().toString()); //.child(DescribeActivity.choose_type_word);
            fire_recycler.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        //??????807,??????801
                        if(dataSnapshot1.child("student_number").getValue().toString().trim().contains("801")){
                            String kk = dataSnapshot1.getKey();
                            DescribeData describeData = dataSnapshot1.getValue(DescribeData.class);
                            list_id.add(kk);
                            list_describe.add(describeData);
                        }

                    }
                    mAdapter = new CardViewData(list_id, list_describe, DramaRecycleView.this);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DramaRecycleView.this, "Oops.....Something is wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

        //--------------------------????????????????????????????????????---------------------------------
        final String[] people_list = {"?????????","?????????????????????", "????????????????????????"};
        ArrayAdapter<String> people=new ArrayAdapter<String>(DramaRecycleView.this,
                android.R.layout.simple_spinner_dropdown_item,
                people_list);
        search_my_or_other.setAdapter(people);

        search_my_or_other.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//????????????Spinner????????????
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                people_spinner_word = search_my_or_other.getSelectedItem().toString();
                if(people_spinner_word.equals("????????????????????????")){
                    search_place.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);
                }else if(people_spinner_word.equals("?????????????????????")){
                        search_place.setVisibility(View.INVISIBLE);
                        search.setVisibility(View.INVISIBLE);
                        list_id.clear();
                        list_describe.clear();
                        fire_recycler = FirebaseDatabase.getInstance().getReference().child("??????" + Student.Name + "???").child("DescribeData");
                        fire_recycler.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                    for(DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                                        for(DataSnapshot dataSnapshot3: dataSnapshot2.getChildren()){
//                                            String kk = dataSnapshot3.getKey();
//                                            list_id.add(kk);
                                            for(DataSnapshot dataSnapshot4: dataSnapshot3.getChildren()){
                                                if(dataSnapshot4.getKey().equals("Content")){
                                                    String kk = dataSnapshot3.getKey();
                                                    list_id.add(kk);
                                                    DescribeData describeData = dataSnapshot4.getValue(DescribeData.class);
                                                    list_describe.add(describeData);

                                                }
                                            }
                                        }
                                    }
                                    mAdapter = new CardViewData(list_id, list_describe, DramaRecycleView.this);
                                    recyclerView.setAdapter(mAdapter);
                                    recyclerView.scrollToPosition(mAdapter.getItemCount()-1);

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(DramaRecycleView.this, "Oops.....Something is wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    //????????????????????????????????? (????????????)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf_sec = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                    String date = sdf.format(new java.util.Date());
                    String date_sec = sdf_sec.format(new java.util.Date());
                    fire_see_time = FirebaseDatabase.getInstance().getReference();
                    fire_see_time.child("??????" + Student.Name + "???").child("See_Self_Description_Time").child(date).child("see_time").child(date_sec).setValue("1");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//????????????
                people_spinner_word = search_my_or_other.getSelectedItem().toString();
            }
        });



        //--------------------------????????????????????? ????????????---------------------------------
        final String[] place_list = {"playground", "classroom","home","Other"};
        ArrayAdapter<String> place=new ArrayAdapter<String>(DramaRecycleView.this,
                android.R.layout.simple_spinner_dropdown_item,
                place_list);
        search_place.setAdapter(place);

        search_place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//????????????Spinner????????????
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                place_spinner_word = search_place.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//????????????
                place_spinner_word = search_place.getSelectedItem().toString();
            }
        });

        //--------------------------????????????????????? ????????????---------------------------------
        final String[] search_type_list = {"Vocabulary", "Phrase", "Sentence"};
        ArrayAdapter<String> typeList1=new ArrayAdapter<String>(DramaRecycleView.this,
                android.R.layout.simple_spinner_dropdown_item,
                search_type_list);
        search_type.setAdapter(typeList1);
        search_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//????????????Spinner????????????
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type_spinner_word = search_type.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//????????????
                type_spinner_word = search_type.getSelectedItem().toString();
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_id.clear();
                list_describe.clear();
                fire_recycler = FirebaseDatabase.getInstance().getReference().child("DescribeData_place").child(place_spinner_word);//.child(type_spinner_word);
                fire_recycler.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            //??????807,??????801
                            if(dataSnapshot1.child("student_number").getValue().toString().trim().contains("801")){
                                String kk = dataSnapshot1.getKey();
                                DescribeData describeData = dataSnapshot1.getValue(DescribeData.class);
                                list_id.add(kk);
                                list_describe.add(describeData);
                            }
                        }
                        mAdapter = new CardViewData(list_id, list_describe, DramaRecycleView.this);
                        recyclerView.setAdapter(mAdapter);
                        recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(DramaRecycleView.this, "Oops.....Something is wrong", Toast.LENGTH_SHORT).show();
                    }
                });
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf_sec = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String date = sdf.format(new java.util.Date());
                String date_sec = sdf_sec.format(new java.util.Date());
                fire_see_time = FirebaseDatabase.getInstance().getReference();
                fire_see_time.child("??????" + Student.Name + "???").child("See_Other_Description_Time").child(date).child(place_spinner_word).child("see_time").child(date_sec).setValue("1");
            }
        });


        //for???????????????????????????DescribeData????????????
//        for(int i = 0; i <= 200; i++){
//            fire_recycler = FirebaseDatabase.getInstance().getReference().child("??????" + i + "???").child("DescribeData");
//            fire_recycler.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                        DescribeData describeData = dataSnapshot1.getValue(DescribeData.class);
//                        list_describe.add(describeData);
//                    }
//                    mAdapter = new CardViewData(list_describe, DramaRecycleView.this);
//                    recyclerView.setAdapter(mAdapter);
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Toast.makeText(DramaRecycleView.this, "Oops.....Something is wrong", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
    }


    //??????????????? ???????????????????????????
    @Override
    public void onBackPressed() {
        MainActivity.isFromMain = false;
        DescribeActivity.isWantToLearn = false;
        DramaRecycleView.this.finish();
    }

}
