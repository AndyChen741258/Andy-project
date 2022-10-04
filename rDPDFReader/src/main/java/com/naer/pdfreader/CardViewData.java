package com.naer.pdfreader;

//import android.support.v7.widget.RecyclerView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Model.DescribeData;
import Model.DescribeList;
import Model.StoreTheEditData;

public class CardViewData extends RecyclerView.Adapter<CardViewData.ViewHolder>{
    private DramaRecycleView context;
    private ArrayList<String> list_de_id; //存KEY值
    private ArrayList<DescribeData> list_describe;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private DatabaseReference fire_sharedata; //分享區
    private DatabaseReference fire_share_time; //分享次數/個數記錄
    private DatabaseReference fire_see_other_tts; //觀看時聆聽tts的次數及內容記錄
    private SimpleDateFormat sdf_now = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String date = sdf.format(new java.util.Date());

    // data is passed into the constructor
    public CardViewData(ArrayList<String> list_de_id, ArrayList<DescribeData> list_describe, Context context){
        this.list_de_id = list_de_id;
        this.list_describe = list_describe;
        this.mInflater = LayoutInflater.from(context);

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String id_num = list_de_id.get(position);
        DescribeData describeData = list_describe.get(position);
        holder.title_location.setText(describeData.getLocation());
        holder.num.setText(describeData.getStudent_number());
        holder.contenet_describe.setText(describeData.getDescribe_text());
        Glide.with(holder.phUrl.getContext()).load(describeData.getImgUrl()).into(holder.phUrl);

        //分享情境描述
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //上傳點擊行為與時間點
                String date = sdf_now.format(new java.util.Date());
                fire_sharedata = FirebaseDatabase.getInstance().getReference();
                //上傳到分享區
                fire_sharedata.child("DescribeData_place").child(describeData.getLocation()).child(id_num).setValue(describeData);
                Toast.makeText(view.getContext(), "已上傳至分享區", Toast.LENGTH_SHORT).show();
                fire_share_time = FirebaseDatabase.getInstance().getReference();
                //記錄下分享的內容及個數
                fire_share_time.child("學生"+Student.Name+"號").child("Share_Time").child(describeData.getLocation()).child(date).setValue(describeData.getDescribe_text());
            }
        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return list_describe.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView phUrl;
        TextView num;
        TextView title_location;
        TextView contenet_describe;
        Button share;
        Button listen_ttstts;
        Button listen_record;

        ViewHolder(View itemView) {
            super(itemView);
            phUrl = itemView.findViewById(R.id.cardphoto);
            num = itemView.findViewById(R.id.row_photo_stunum);
            title_location = itemView.findViewById(R.id.row_photo_title);
            contenet_describe = itemView.findViewById(R.id.row_photo_content);
            share = itemView.findViewById(R.id.row_btn_share);
            listen_ttstts = itemView.findViewById(R.id.listen_ttstts);
            listen_record = itemView.findViewById(R.id.listen_record);


            //TTS.init(getApplicationContext());

            if(DramaRecycleView.people_spinner_word.equals("其他人的情境探索")){
                share.setVisibility(View.INVISIBLE);

            }else if(DramaRecycleView.people_spinner_word.equals("自己的情境探索")){
                if(MainActivity.isFromMain == true){
                    share.setVisibility(View.INVISIBLE);
                }else{
                    share.setVisibility(View.VISIBLE);
                }
            }

            String string=num.getText().toString().trim().substring(2,num.getText().toString().trim().length()-1);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child(string)
                    .child("Describe Record")
                    .child(contenet_describe.getText().toString().trim()+ "/" + string+"_"+contenet_describe.getText().toString().trim()+".amr");
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    listen_record.setVisibility(View.VISIBLE);
                    listen_record.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                //上傳點擊行為與時間點
                                String date = sdf_now.format(new java.util.Date());
                                DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                        .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("Describe").child("聆聽情境錄音");
                                fire_60sec_student_data.child(date).child("Describe Text").setValue(contenet_describe.getText().toString().trim());
                                fire_60sec_student_data.child(date).child("Student number").setValue(num.getText().toString().trim());
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(itemView.getContext(), "上傳聆聽情境錄音紀錄失敗", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(itemView.getContext(),"撥放錄音", Toast.LENGTH_SHORT).show();
                            MediaPlayer player = new MediaPlayer();
                            try {
                                player.setDataSource(uri.toString());
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
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });


            listen_ttstts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TTS.speak(contenet_describe.getText().toString());
                    try {
                        //上傳點擊行為與時間點
                        String date = sdf_now.format(new java.util.Date());
                        DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("Describe").child("聆聽情境TTS");
                        if(DramaRecycleView.people_spinner_word.equals("自己的情境探索")){
                            fire_60sec_student_data.child("觀看自己的").child(date).child("Describe Text").setValue(contenet_describe.getText().toString().trim());
                        }else{
                            if(num.getText().toString().trim().substring(2, num.getText().toString().length() - 1).trim().equals(Student.Name.trim())){
                                fire_60sec_student_data.child("觀看自己的").child(date).child("Describe Text").setValue(contenet_describe.getText().toString().trim());
                            }else{
                                fire_60sec_student_data.child("觀看同學的").child(date).child("Describe Text").setValue(contenet_describe.getText().toString().trim());
                                fire_60sec_student_data.child("觀看同學的").child(date).child("Student number").setValue(num.getText().toString().trim());
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(itemView.getContext(), "上傳聆聽情境TTS紀錄失敗", Toast.LENGTH_SHORT).show();
                    }

                    if(DramaRecycleView.people_spinner_word.equals("其他人的情境探索")){
                        //紀錄: 觀看其他人的描述時 看哪個地點哪個學生的紀錄 tts聽了幾個幾次
                        fire_see_other_tts = FirebaseDatabase.getInstance().getReference();
                        fire_see_other_tts.child("學生"+Student.Name+"號").child("See_Other_Description_Time").child(date)
                                .child(title_location.getText().toString()).child("TTS").child(num.getText().toString()).push().setValue(contenet_describe.getText().toString().replaceAll("[,|.|!|?|']", "").trim());
                    }else if(DramaRecycleView.people_spinner_word.equals("自己的情境探索")){
                        fire_see_other_tts = FirebaseDatabase.getInstance().getReference();
                        fire_see_other_tts.child("學生"+Student.Name+"號").child("See_Self_Description_Time").child(date)
                                .child("TTS").push().setValue(contenet_describe.getText().toString().replaceAll("[,|.|!|?|']", "").trim());
                    }
                }
            });

            listen_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String string=num.getText().toString().trim().substring(2,num.getText().toString().trim().length()-1);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                            .child(string)
                            .child("Describe Record")
                            .child(contenet_describe.getText().toString().trim()+ "/" + string+"_"+contenet_describe.getText().toString().trim()+".amr");
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            try {
                                //上傳點擊行為與時間點
                                String date = sdf_now.format(new java.util.Date());
                                DatabaseReference fire_60sec_student_data = FirebaseDatabase.getInstance().getReference()
                                        .child("學生"+Student.Name+"號").child("Student data").child("點擊行為").child("Describe").child("聆聽情境錄音");
                                if(DramaRecycleView.people_spinner_word.equals("自己的情境探索")){
                                    fire_60sec_student_data.child("觀看自己的").child(date).child("Describe Text").setValue(contenet_describe.getText().toString().trim());
                                }else{
                                    if(num.getText().toString().trim().substring(2, num.getText().toString().length() - 1).trim().equals(Student.Name.trim())){
                                        fire_60sec_student_data.child("觀看自己的").child(date).child("Describe Text").setValue(contenet_describe.getText().toString().trim());
                                    }else{
                                        fire_60sec_student_data.child("觀看同學的").child(date).child("Describe Text").setValue(contenet_describe.getText().toString().trim());
                                        fire_60sec_student_data.child("觀看同學的").child(date).child("Student number").setValue(num.getText().toString().trim());
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(itemView.getContext(), "上傳聆聽情境錄音紀錄失敗", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(itemView.getContext(),"撥放錄音", Toast.LENGTH_SHORT).show();
                            MediaPlayer player = new MediaPlayer();
                            try {
                                player.setDataSource(uri.toString());
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
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(itemView.getContext(),"沒有錄音", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            if(DescribeActivity.isWantToLearn == true){
                share.setVisibility(View.INVISIBLE);
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }
    }

    // convenience method for getting data at click position
//    String getItem(int id) {
//        return list_describe.get(id);
//    }

     //allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

     //parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
///=================



    //    private List<String> mData;
//
//    CardViewData(List<String> data) {
//        mData = data;
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder{
//        // 宣告元件
//        private TextView txtItem;
//
//        ViewHolder(View itemView) {
//            super(itemView);
//            txtItem = (TextView) itemView.findViewById(R.id.txtItem);
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        // 連結項目布局檔list_item
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.cardview, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        // 設置txtItem要顯示的內容
//        holder.txtItem.setText(mData.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return mData.size();
//    }
    }

