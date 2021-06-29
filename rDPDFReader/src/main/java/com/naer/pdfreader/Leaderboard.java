package com.naer.pdfreader;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import static android.view.View.VISIBLE;

public class Leaderboard extends Activity implements SearchView.OnQueryTextListener {

    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private RecyclerView recyclerView4;
    private MyAdapter myAdapter;
    private MyAdapter myAdapter2;
    private MyAdapter myAdapter3;
    private MyAdapter myAdapter4;
    private ArrayList<Student> studentlist;
    private ArrayList<Student> studentlist2;
    private ArrayList<Student> studentlist3;
    private ArrayList<Student> studentlist4;
    private boolean nameAsce, marksAsce;
    private boolean filter;
    private boolean  marksAsce2;
    private boolean  marksAsce3;
    private boolean  marksAsce4;
    private TextView textView;
    private CardView cardView;
    private CardView cardView2;
    private CardView cardView3;
    private CardView cardView4;
    private static String[] namearray;
    private static String[] namearray2;
    private static String[] namearray3;
    private static String[] namearray4;
    private static int[] marks;
    private static int[] marks2;
    private static int[] marks3;
    private static int[] marks4;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//隱藏標題列
        setContentView(R.layout.activity_leaderboard);

        nameAsce = true;
        marksAsce = false;
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        textView = (TextView)findViewById(R.id.textview);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        recyclerView2 = (RecyclerView) findViewById(R.id.recycle2);
        recyclerView3 = (RecyclerView) findViewById(R.id.recycle3);
        recyclerView4 = (RecyclerView) findViewById(R.id.recycle4);
        studentlist = new ArrayList<>();
        studentlist2 = new ArrayList<>();
        studentlist3 = new ArrayList<>();
        studentlist4 = new ArrayList<>();
        cardView = (CardView) findViewById(R.id.card);
        cardView2 = (CardView) findViewById(R.id.card2);
        cardView3 = (CardView) findViewById(R.id.card3);
        cardView4 = (CardView) findViewById(R.id.card4);
        filter = true;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        recyclerView4.setLayoutManager(new LinearLayoutManager(this));

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Leaderboard.this);
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                final AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.imageview, null);
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface d) {
                        ImageView image = (ImageView) dialog.findViewById(R.id.goProDialogImage);
                        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.leaderboard);
                        float imageWidthInPX = (float)image.getWidth();

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                                Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
                        image.setLayoutParams(layoutParams);


                    }
                });
            }
        });


        try {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference db = database.getReference().child("Other").child("stu_data");
            myAdapter = new MyAdapter(this);
            myAdapter2 = new MyAdapter(this);
            myAdapter3 = new MyAdapter(this);
            myAdapter4 = new MyAdapter(this);

            db.child("Describe").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        int user = (int) dataSnapshot.getChildrenCount();
                        int i=0;
                        namearray=new String[user];
                        marks=new int[user];

                        for (DataSnapshot each : dataSnapshot.getChildren()) {
                            namearray[i]=each.getKey()+"號";
                            marks[i]= (int) each.getValue(Integer.class);
                            i++;
                        }
//                        for (DataSnapshot each : dataSnapshot.getChildren()) {
//                            if(each.getKey().substring(0,2).equals("學生")==true){
//                                namearray[i]=each.getKey();
//                                db.child(each.getKey()).child("Student data").child("Describe")
//                                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                if (dataSnapshot.exists()) {
//                                                    int x = 0;
//                                                    for (DataSnapshot each : dataSnapshot.getChildren()) {
//                                                        if (each.getKey().equals("Conversation") == true || each.getKey().equals("Take Picture") == true) {
//                                                            x= (int) each.getValue(Integer.class)+x;
//                                                        }
//
//                                                    }
//                                                    for (int y=0;y<namearray.length;y++){
//                                                        if (namearray[y] == each.getKey()){
//                                                            marks[y] = x;
//                                                            Log.v("測試",String.valueOf(marks[y]));
//                                                        }
//                                                    }
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            }
//                                        });

//                                db.child(each.getKey()).child("Student data").child("EditDrama")
//                                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                if (dataSnapshot.exists()) {
//                                                    int x = 0;
//                                                    for (DataSnapshot each : dataSnapshot.getChildren()) {
//                                                        if (each.getKey().equals("recordA") == true || each.getKey().equals("recordB") == true
//                                                                || each.getKey().equals("recordC") == true || each.getKey().equals("recordD") == true
//                                                                || each.getKey().equals("TTS") == true) {
//                                                            x= (int) each.getValue(Integer.class)+x;
//
//                                                        }
//                                                    }
//                                                    for (int y=0;y<namearray.length;y++){
//                                                        if (namearray[y] == each.getKey()){
//                                                            marks2[y] = x;
//                                                        }
//                                                    }
//
//                                                    for (int z = 0; z < namearray.length; z++) {
//                                                        String name = namearray[z];
//                                                        int mark = marks2[z];
//                                                        Student student = new Student();
//                                                        student.setName(name);
//                                                        student.setMarks(mark);
//                                                        studentlist2.add(student);
//                                                    }
//
//                                                    sortStudent2(false, false);
//
//                                                    for (Student student : studentlist2) {
//                                                        student.setRank(studentlist2.indexOf(student) + 1);
//                                                    }
//
//                                                    myAdapter2.getStudList1().addAll(studentlist2);
//                                                    recyclerView2.setAdapter(myAdapter2);
//                                                    myAdapter2.updateList();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            }
//                                        });
//                                i++;
//                            }
//                        }

                        for (int z = 0; z < namearray.length; z++) {
                            if (marks[z] != 0){
                                String name = namearray[z];
                                int mark = marks[z];
                                Student student = new Student();
                                student.setName(name);
                                student.setMarks(mark);
                                studentlist.add(student);
                            }
                        }

                        sortStudent(false, false);

                        for (Student student : studentlist) {
                            student.setRank(studentlist.indexOf(student) + 1);
                        }
                        myAdapter.getStudList1().addAll(studentlist);
                        recyclerView.setAdapter(myAdapter);
                        myAdapter.updateList();

                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            db.child("EditDrama").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        int user = (int) dataSnapshot.getChildrenCount();
                        int i=0;
                        namearray2=new String[user];
                        marks2=new int[user];

                        for (DataSnapshot each : dataSnapshot.getChildren()) {
                            namearray2[i]=each.getKey()+"號";
                            marks2[i]= (int) each.getValue(Integer.class);
                            i++;
                        }

                        for (int z = 0; z < namearray2.length; z++) {
                            if (marks2[z] != 0) {
                                String name = namearray2[z];
                                int mark = marks2[z];
                                Student student = new Student();
                                student.setName(name);
                                student.setMarks(mark);
                                studentlist2.add(student);
                            }
                        }

                        sortStudent2(false, false);

                        for (Student student : studentlist2) {
                            student.setRank(studentlist2.indexOf(student) + 1);
                        }
                        myAdapter2.getStudList1().addAll(studentlist2);
                        recyclerView2.setAdapter(myAdapter2);
                        myAdapter2.updateList();

                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            db.child("DramaAchievement").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        int user = (int) dataSnapshot.getChildrenCount();
                        int i=0;
                        namearray3=new String[user];
                        marks3=new int[user];

                        for (DataSnapshot each : dataSnapshot.getChildren()) {
                            namearray3[i]=each.getKey()+"號";
                            marks3[i]= (int) each.getValue(Integer.class);
                            i++;
                        }

                        for (int z = 0; z < namearray3.length; z++) {
                            if (marks3[z] != 0) {
                                String name = namearray3[z];
                                int mark = marks3[z];
                                Student student = new Student();
                                student.setName(name);
                                student.setMarks(mark);
                                studentlist3.add(student);
                            }

                            String name = namearray3[z];
                            int mark = (int) ((marks[z]*0.25)+(marks2[z]*0.4)+(marks3[z]*0.35));
                            Student student = new Student();
                            student.setName(name);
                            student.setMarks(mark);
                            studentlist4.add(student);
                        }

                        sortStudent3(false, false);
                        sortStudent4(false, false);

                        for (Student student : studentlist3) {
                            student.setRank(studentlist3.indexOf(student) + 1);
                        }

                        for (Student student : studentlist4) {
                            student.setRank(studentlist4.indexOf(student) + 1);
                        }
                        myAdapter3.getStudList1().addAll(studentlist3);
                        myAdapter4.getStudList1().addAll(studentlist4);
                        recyclerView3.setAdapter(myAdapter3);
                        recyclerView4.setAdapter(myAdapter4);
                        myAdapter3.updateList();
                        myAdapter4.updateList();

                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Leaderboard.this, "取得錯誤", Toast.LENGTH_SHORT).show();
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortStudent(false,marksAsce);
                myAdapter.getStudList1().clear();
                myAdapter.getStudList1().addAll(studentlist);
                myAdapter.updateList();
                marksAsce=!marksAsce;
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortStudent2(false,marksAsce2);
                myAdapter2.getStudList1().clear();
                myAdapter2.getStudList1().addAll(studentlist2);
                myAdapter2.updateList();
                marksAsce2=!marksAsce2;
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortStudent3(false,marksAsce3);
                myAdapter3.getStudList1().clear();
                myAdapter3.getStudList1().addAll(studentlist3);
                myAdapter3.updateList();
                marksAsce3=!marksAsce3;
            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortStudent4(false,marksAsce4);
                myAdapter4.getStudList1().clear();
                myAdapter4.getStudList1().addAll(studentlist4);
                myAdapter4.updateList();
                marksAsce4=!marksAsce4;
            }
        });


    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        myAdapter.getStudList1().clear();
        myAdapter2.getStudList1().clear();
        myAdapter3.getStudList1().clear();
        myAdapter4.getStudList1().clear();

        for (Student string : studentlist )
        {
            if (string.getName().toLowerCase().contains(userInput))
                myAdapter.getStudList1().add(string);
        }

        for (Student string : studentlist2 )
        {
            if (string.getName().toLowerCase().contains(userInput))
                myAdapter2.getStudList1().add(string);
        }

        for (Student string : studentlist3 )
        {
            if (string.getName().toLowerCase().contains(userInput))
                myAdapter3.getStudList1().add(string);
        }

        for (Student string : studentlist4 )
        {
            if (string.getName().toLowerCase().contains(userInput))
                myAdapter4.getStudList1().add(string);
        }


        if (myAdapter.getStudList1().isEmpty())
        {
            recyclerView.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.GONE);
            recyclerView3.setVisibility(View.GONE);
            recyclerView4.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            cardView2.setVisibility(View.GONE);
            cardView3.setVisibility(View.GONE);
            cardView4.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView2.setVisibility(View.VISIBLE);
            recyclerView3.setVisibility(View.VISIBLE);
            recyclerView4.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            cardView2.setVisibility(View.VISIBLE);
            cardView3.setVisibility(View.VISIBLE);
            cardView4.setVisibility(View.VISIBLE);
        }
        myAdapter.updateList();
        myAdapter2.updateList();
        myAdapter3.updateList();
        myAdapter4.updateList();
        return true;
    }

    private void sortStudent(final boolean sortOnName, final boolean Ascending){
        Collections.sort(studentlist, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                int result = 0;
                if(sortOnName){
                    if (Ascending)
                        result=o1.getName().compareTo(o2.getName());
                    else
                        result=o2.getName().compareTo(o1.getName());

                }
                else
                {
                    int marks1 = Ascending ? o1.getMarks() : o2.getMarks();
                    int marks2 = Ascending ?  o2.getMarks(): o1.getMarks();
                    if (marks1 > marks2)
                        result = 1;
                    else if (marks1 < marks2)
                        result = -1;
                    else if (marks1 == marks2){
                        if(!Ascending)
                            result = -1;
                        else
                            result = 1;
                    }
                }
                return result;
            }
        });


    }
    private void sortStudent2(final boolean sortOnName, final boolean Ascending){
        Collections.sort(studentlist2, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                int result = 0;
                if(sortOnName){
                    if (Ascending)
                        result=o1.getName().compareTo(o2.getName());
                    else
                        result=o2.getName().compareTo(o1.getName());

                }
                else
                {
                    int marks1 = Ascending  ? o1.getMarks() : o2.getMarks();
                    int marks2 = Ascending ?  o2.getMarks(): o1.getMarks();
                    if (marks1 > marks2)
                        result = 1;
                    else if (marks1 < marks2)
                        result = -1;
                    else if (marks1 == marks2){
                        if(!Ascending)
                            result = -1;
                        else
                            result = 1;
                    }
                }
                return result;
            }
        });

    }

    private void sortStudent3(final boolean sortOnName, final boolean Ascending){
        Collections.sort(studentlist3, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                int result = 0;
                if(sortOnName){
                    if (Ascending)
                        result=o1.getName().compareTo(o2.getName());
                    else
                        result=o2.getName().compareTo(o1.getName());

                }
                else
                {
                    int marks1 = Ascending  ? o1.getMarks() : o2.getMarks();
                    int marks2 = Ascending ?  o2.getMarks(): o1.getMarks();
                    if (marks1 > marks2)
                        result = 1;
                    else if (marks1 < marks2)
                        result = -1;
                    else if (marks1 == marks2){
                        if(!Ascending)
                            result = -1;
                        else
                            result = 1;
                    }
                }
                return result;
            }
        });


    }

    private void sortStudent4(final boolean sortOnName, final boolean Ascending){
        Collections.sort(studentlist4, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                int result = 0;
                if(sortOnName){
                    if (Ascending)
                        result=o1.getName().compareTo(o2.getName());
                    else
                        result=o2.getName().compareTo(o1.getName());

                }
                else
                {
                    int marks1 = Ascending  ? o1.getMarks() : o2.getMarks();
                    int marks2 = Ascending ?  o2.getMarks(): o1.getMarks();
                    if (marks1 > marks2)
                        result = 1;
                    else if (marks1 < marks2)
                        result = -1;
                    else if (marks1 == marks2){
                        if(!Ascending)
                            result = -1;
                        else
                            result = 1;
                    }
                }
                return result;
            }
        });


    }

}