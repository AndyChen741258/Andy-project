package com.naer.pdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import lib.kingja.switchbutton.SwitchMultiButton;

public class ChoosePracticrPart extends Activity {

    private TextView way_text;
    private Spinner practice_way;
    private TextView drama_text;
    private Spinner practice_drama;
    private TextView character_text;
    private Spinner practice_character;
    private SwitchMultiButton switchMultiButton2;


    //選單中獲取資料以利後面對話流程的進行
    public static String way_word;
    public static String drama_word;
    public static String character_word;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_practicr_part);

//        way_text = findViewById(R.id.way_text);
//        practice_way = findViewById(R.id.practice_way);
        drama_text = findViewById(R.id.drama_text);
//        practice_drama = findViewById(R.id.practice_drama);
        character_text = findViewById(R.id.character_text);
        practice_character = findViewById(R.id.practice_character);
        switchMultiButton2 = findViewById(R.id.switchmultibutton2);

        drama_text.setVisibility(View.INVISIBLE);
        practice_drama.setVisibility(View.INVISIBLE);
        character_text.setVisibility(View.INVISIBLE);
        practice_character.setVisibility(View.INVISIBLE);

        //--------選擇練習模式----------
        switchMultiButton2.setText("課本對話練習", "劇本對話練習").setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                way_word = tabText;
                character_word = tabText;
                drama_text.setVisibility(View.VISIBLE);
                practice_drama.setVisibility(View.VISIBLE);
            }
        });
//        final String[] way_list = {"課本對話練習", "劇本對話練習"};
//        ArrayAdapter<String> way =new ArrayAdapter<String>(ChoosePracticrPart.this,
//                android.R.layout.simple_spinner_dropdown_item,
//                way_list);
//        practice_way.setAdapter(way);
//        practice_way.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                way_word = practice_way.getSelectedItem().toString();
//                drama_text.setVisibility(View.VISIBLE);
//                practice_drama.setVisibility(View.VISIBLE);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
//                way_word = practice_way.getSelectedItem().toString();
//            }
//        });

        //--------選擇劇本內容----------
//        final String[] drama_list = {"課本對話練習", "劇本對話練習"};
//        ArrayAdapter<String> drama =new ArrayAdapter<String>(ChoosePracticrPart.this,
//                android.R.layout.simple_spinner_dropdown_item,
//                drama_list);
//        practice_drama.setAdapter(drama);
//        practice_drama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                drama_word = practice_drama.getSelectedItem().toString();
//                character_text.setVisibility(View.VISIBLE);
//                practice_character.setVisibility(View.VISIBLE);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
//                drama_word = practice_drama.getSelectedItem().toString();
//            }
//        });

        //--------選擇腳色----------
//        final String[] character_list = {"課本對話練習", "劇本對話練習"};
//        ArrayAdapter<String> character =new ArrayAdapter<String>(ChoosePracticrPart.this,
//                android.R.layout.simple_spinner_dropdown_item,
//                character_list);
//        practice_character.setAdapter(character);
//        practice_character.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//添加事件Spinner事件監聽
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                character_word = practice_character.getSelectedItem().toString();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {//沒有選時
//                character_word = practice_character.getSelectedItem().toString();
//            }
//        });
    }
}
