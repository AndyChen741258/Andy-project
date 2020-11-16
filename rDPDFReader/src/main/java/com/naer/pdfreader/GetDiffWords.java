package com.naer.pdfreader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetDiffWords {
    public static void getInsert(String htmlString, String[] words){

        // select tag del and the word
        htmlString = htmlString.replaceAll("<del>.*?</del>", "");
        htmlString = htmlString.replaceAll("<span>|</span>", "");

        // get the string in tag
        String str_words = "";
        Pattern cover_pattern = Pattern.compile("<u>.*?</u>");
        Matcher cover_matcher = cover_pattern.matcher(htmlString);
        while(cover_matcher.find()){
            str_words += cover_matcher.group();
        }
        str_words = str_words.replaceAll("<u>|</u>", " ").trim().replaceAll(" +", " ");

        String[] src_words = str_words.split(" ");
        if(src_words[0] == null && str_words.length() != 0){
            words[0] = str_words;
        }
        else{
            System.arraycopy( src_words, 0, words, 0, src_words.length);
        }

    }

    public static void getDelete(String htmlString, String[] words){

        // select tag del and the word
        htmlString = htmlString.replaceAll("<u>.*?</u>", "");
        htmlString = htmlString.replaceAll("<span>|</span>", "");

        // get the string in tag del
        String str_words = "";
        Pattern cover_pattern = Pattern.compile("<del>.*?</del>");
        Matcher cover_matcher = cover_pattern.matcher(htmlString);
        while(cover_matcher.find()){
            str_words += cover_matcher.group();
        }
        str_words = str_words.replaceAll("<del>|</del>", " ").trim().replaceAll(" +", " ");

        String[] src_words = str_words.split(" ");
        if(src_words[0] == null && str_words.length() != 0){
            words[0] = str_words;
        }
        else{
            System.arraycopy( src_words, 0, words, 0, src_words.length);
        }

    }
}
