package com.naer.pdfreader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;

import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BestDiff {
    public static double getFamiliarity(Context context, int type, int questionId, String text, String[] diffString) {
        double familiarity = 0;
        // db init
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
        SQLiteDatabase sqliteDatabase = mySQLiteOpenHelper.getWritableDatabase();
        if (type == 0) { // compare answer diff
            String bestStr = "";
            Cursor cursor = sqliteDatabase.rawQuery("select * from answer Where questionId = " + questionId + ";", null);
            if (cursor.moveToFirst()) {
                do {
                    String[] optionList = cursor.getString(4).split(";");
                    List<String[]> optionsList = new ArrayList();
                    for (int i = 0; i < optionList.length; i++) {
                        Log.d("BestDiff", "optionsList: " + optionList[i]);
                        if (!optionList[i].equals("")) {
                            Cursor cursorOptions = sqliteDatabase.rawQuery("select * from pattern Where category = '" + optionList[i] + "';", null);
                            int length = cursorOptions.getCount();
                            String[] option = new String[length];
                            for (int j = 0; j < length; j++) {
                                cursorOptions.moveToPosition(j);
                                option[j] = cursorOptions.getString(2);
                            }
                            optionsList.add(option);
                        }
                    }

                    if (optionsList.size() == 0) {
                        String questionPattern = cursor.getString(3).replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                        String userSentence = text.replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                        diffMatchPatch diff_matchPatch_obj = new diffMatchPatch();
                        LinkedList<diffMatchPatch.Diff> diff = diff_matchPatch_obj.diff_wordMode(userSentence, questionPattern);
                        //change tag <u> to real del line
                        String htmlDiff = diff_matchPatch_obj.diff_prettyHtml(diff);
                        htmlDiff = htmlDiff.replaceAll(" </u>", "</u> ");
                        htmlDiff = htmlDiff.replaceAll(" </del>", "</del> ");
                        htmlDiff = htmlDiff.replaceAll(" </span>", "</span> ");
                        Log.d("BestDiff", "onResults: " + htmlDiff);
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
                        GetDiffWords getDiffWords = new GetDiffWords();
                        String[] words = new String[100];
                        getDiffWords.getInsert(htmlDiff, words);
                        int insertedNum = 0;
                        for (int k = 0; k < words.length; k++) {
                            if (words[k] == null) {
                                break;
                            }
                            insertedNum++;
                        }
                        // similarity
                        String[] deletedWords = new String[100];
                        getDiffWords.getDelete(htmlDiff, deletedWords);
                        int deletedNum = 0;
                        for (int k = 0; k < deletedWords.length; k++) {
                            if (deletedWords[k] == null) {
                                break;
                            }
                            deletedNum++;
                        }
                        double len = questionPattern.split(" ").length;
                        double similarity = 100;
                        if (insertedNum > deletedNum) {
                            similarity = (int) ((1 - (insertedNum / len)) * 10000.0) / 100.0;
                        } else {
                            similarity = (int) ((1 - (deletedNum / len)) * 10000.0) / 100.0;
                        }
                        if (similarity < 0) {
                            similarity = 0;
                        }
                        if (questionPattern.equals(userSentence)) {
                            similarity = 100;
                        }
                        if (similarity >= familiarity) {
                            familiarity = similarity;
                            diffString[0] = htmlDiff;
                            diffString[1] = questionPattern;
                            diffString[2] = text;
                        }
                        Log.d("similarity", "getFamiliarity: " + similarity);
                    } else if (optionsList.size() == 1) {
                        for (int j = 0; j < optionsList.get(0).length; j++) {
                            String questionPattern = cursor.getString(3).replaceFirst("\\.\\.\\.", optionsList.get(0)[j]).replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                            String userSentence = text.replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                            diffMatchPatch diff_matchPatch_obj = new diffMatchPatch();
                            LinkedList<diffMatchPatch.Diff> diff = diff_matchPatch_obj.diff_wordMode(userSentence, questionPattern);
                            //change tag <u> to real del line
                            String htmlDiff = diff_matchPatch_obj.diff_prettyHtml(diff);
                            htmlDiff = htmlDiff.replaceAll(" </u>", "</u> ");
                            htmlDiff = htmlDiff.replaceAll(" </del>", "</del> ");
                            htmlDiff = htmlDiff.replaceAll(" </span>", "</span> ");
                            Log.d("BestDiff", "onResults: " + htmlDiff);
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
                            GetDiffWords getDiffWords = new GetDiffWords();
                            String[] words = new String[100];
                            getDiffWords.getInsert(htmlDiff, words);
                            int insertedNum = 0;
                            for (int k = 0; k < words.length; k++) {
                                if (words[k] == null) {
                                    break;
                                }
                                insertedNum++;
                            }
                            //similarity
                            String[] deletedWords = new String[100];
                            getDiffWords.getDelete(htmlDiff, deletedWords);
                            int deletedNum = 0;
                            for (int k = 0; k < deletedWords.length; k++) {
                                if (deletedWords[k] == null) {
                                    break;
                                }
                                deletedNum++;
                            }
                            double len = questionPattern.split(" ").length;
                            double similarity = 100;
                            if (insertedNum > deletedNum) {
                                similarity = (int) ((1 - (insertedNum / len)) * 10000.0) / 100.0;
                            } else {
                                similarity = (int) ((1 - (deletedNum / len)) * 10000.0) / 100.0;
                            }
                            if (similarity < 0) {
                                similarity = 0;
                            }
                            if (questionPattern.equals(userSentence)) {
                                similarity = 100;
                            }
                            if (similarity >= familiarity) {
                                familiarity = similarity;
                                diffString[0] = htmlDiff;
                                diffString[1] = questionPattern;
                                diffString[2] = text;
                            }
                            Log.d("BestDiff", "insertedNum:" + insertedNum + "  deletedNum:" + deletedNum);
                            Log.d("BestDiff", "getFamiliarity: " + similarity);
                        }
                    } else if (optionsList.size() == 2) {
                        for (int i = 0; i < optionsList.get(0).length; i++) {
                            for (int j = 0; j < optionsList.get(1).length; j++) {
                                String questionPattern = cursor.getString(3).replaceFirst("\\.\\.\\.", optionsList.get(0)[i]).replaceFirst("\\.\\.\\.", optionsList.get(1)[j]).replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                                String userSentence = text.replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                                Log.d("BestDiff", questionPattern + " / " + userSentence);
                                diffMatchPatch diff_matchPatch_obj = new diffMatchPatch();
                                LinkedList<diffMatchPatch.Diff> diff = diff_matchPatch_obj.diff_wordMode(userSentence, questionPattern);
                                //change tag <u> to real del line
                                String htmlDiff = diff_matchPatch_obj.diff_prettyHtml(diff);
                                htmlDiff = htmlDiff.replaceAll(" </u>", "</u> ");
                                htmlDiff = htmlDiff.replaceAll(" </del>", "</del> ");
                                htmlDiff = htmlDiff.replaceAll(" </span>", "</span> ");
                                Log.d("BestDiff", "onResults: " + htmlDiff);
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
                                GetDiffWords getDiffWords = new GetDiffWords();
                                String[] words = new String[100];
                                getDiffWords.getInsert(htmlDiff, words);
                                int insertedNum = 0;
                                for (int k = 0; k < words.length; k++) {
                                    if (words[k] == null) {
                                        break;
                                    }
                                    insertedNum++;
                                }

                                // similarity
                                String[] deletedWords = new String[100];
                                getDiffWords.getDelete(htmlDiff, deletedWords);
                                int deletedNum = 0;
                                for (int k = 0; k < deletedWords.length; k++) {
                                    if (deletedWords[k] == null) {
                                        break;
                                    }
                                    deletedNum++;
                                }

                                double len = questionPattern.split(" ").length;
                                double similarity = 100;
                                if (insertedNum > deletedNum) {
                                    similarity = (int) ((1 - (insertedNum / len)) * 10000.0) / 100.0;
                                } else {
                                    similarity = (int) ((1 - (deletedNum / len)) * 10000.0) / 100.0;
                                }
                                if (similarity < 0) {
                                    similarity = 0;
                                }
                                if (questionPattern.equals(userSentence)) {
                                    similarity = 100;
                                }
                                if (similarity >= familiarity) {
                                    familiarity = similarity;
                                    diffString[0] = htmlDiff;
                                    diffString[1] = questionPattern;
                                    diffString[2] = text;
                                }
                                Log.d("similarity", "getFamiliarity: " + similarity);
                            }
                        }
                    }
                } while (cursor.moveToNext());
            }
        } else if (type == 1) { // compare question diff
            String bestStr = "";
            Cursor cursor = sqliteDatabase.rawQuery("select * from question Where id = " + questionId + ";", null);
            if (cursor.moveToFirst()) {
                String[] optionList = cursor.getString(4).split(";");
                List<String[]> optionsList = new ArrayList();
                for (int i = 0; i < optionList.length; i++) {
                    if (!optionList[i].equals("")) {
                        Cursor cursorOptions = sqliteDatabase.rawQuery("select * from pattern Where category = '" + optionList[i] + "';", null);
                        int length = cursorOptions.getCount();
                        String[] option = new String[length];
                        for (int j = 0; j < length; j++) {
                            cursorOptions.moveToPosition(j);
                            option[j] = cursorOptions.getString(2);
                        }
                        optionsList.add(option);
                    }
                }
                if (optionsList.size() == 0) {
                    String questionPattern = cursor.getString(3).replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                    String userSentence = text.replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                    diffMatchPatch diff_matchPatch_obj = new diffMatchPatch();
                    LinkedList<diffMatchPatch.Diff> diff = diff_matchPatch_obj.diff_wordMode(userSentence, questionPattern);
                    // change tag <u> to real del line
                    String htmlDiff = diff_matchPatch_obj.diff_prettyHtml(diff);
                    htmlDiff = htmlDiff.replaceAll(" </u>", "</u> ");
                    htmlDiff = htmlDiff.replaceAll(" </del>", "</del> ");
                    htmlDiff = htmlDiff.replaceAll(" </span>", "</span> ");
                    Log.d("BestDiff", "onResults: " + htmlDiff);
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
                    GetDiffWords getDiffWords = new GetDiffWords();
                    String[] words = new String[100];
                    getDiffWords.getInsert(htmlDiff, words);
                    int insertedNum = 0;
                    for (int k = 0; k < words.length; k++) {
                        if (words[k] == null) {
                            break;
                        }
                        insertedNum++;
                    }
                    // similarity
                    String[] deletedWords = new String[100];
                    getDiffWords.getDelete(htmlDiff, deletedWords);
                    int deletedNum = 0;
                    for (int k = 0; k < deletedWords.length; k++) {
                        if (deletedWords[k] == null) {
                            break;
                        }
                        deletedNum++;
                    }
                    double len = questionPattern.split(" ").length;
                    double similarity = 100;
                    if (insertedNum > deletedNum) {
                        similarity = (int) ((1 - (insertedNum / len)) * 10000.0) / 100.0;
                    } else {
                        similarity = (int) ((1 - (deletedNum / len)) * 10000.0) / 100.0;
                    }
                    if (similarity < 0) {
                        similarity = 0;
                    }
                    if (questionPattern.equals(userSentence)) {
                        similarity = 100;
                    }
                    if (similarity >= familiarity) {
                        familiarity = similarity;
                        diffString[0] = htmlDiff;
                        diffString[1] = questionPattern;
                        diffString[2] = text;
                    }
                    Log.d("similarity", "getFamiliarity: " + similarity);
                } else if (optionsList.size() == 1) {
                    for (int j = 0; j < optionsList.get(0).length; j++) {
                        String questionPattern = cursor.getString(3).replaceFirst("\\.\\.\\.", optionsList.get(0)[j]).replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                        String userSentence = text.replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                        diffMatchPatch diff_matchPatch_obj = new diffMatchPatch();
                        LinkedList<diffMatchPatch.Diff> diff = diff_matchPatch_obj.diff_wordMode(userSentence, questionPattern);
                        // change tag <u> to real del line
                        String htmlDiff = diff_matchPatch_obj.diff_prettyHtml(diff);
                        htmlDiff = htmlDiff.replaceAll(" </u>", "</u> ");
                        htmlDiff = htmlDiff.replaceAll(" </del>", "</del> ");
                        htmlDiff = htmlDiff.replaceAll(" </span>", "</span> ");
                        Log.d("BestDiff", "onResults: " + htmlDiff);
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
                        GetDiffWords getDiffWords = new GetDiffWords();
                        String[] words = new String[100];
                        getDiffWords.getInsert(htmlDiff, words);
                        int insertedNum = 0;
                        for (int k = 0; k < words.length; k++) {
                            if (words[k] == null) {
                                break;
                            }
                            insertedNum++;
                        }
                        // similarity
                        String[] deletedWords = new String[100];
                        getDiffWords.getDelete(htmlDiff, deletedWords);
                        int deletedNum = 0;
                        for (int k = 0; k < deletedWords.length; k++) {
                            if (deletedWords[k] == null) {
                                break;
                            }
                            deletedNum++;
                        }
                        double len = questionPattern.split(" ").length;
                        double similarity = 100;
                        if (insertedNum > deletedNum) {
                            similarity = (int) ((1 - (insertedNum / len)) * 10000.0) / 100.0;
                        } else {
                            similarity = (int) ((1 - (deletedNum / len)) * 10000.0) / 100.0;
                        }
                        if (similarity < 0) {
                            similarity = 0;
                        }
                        if (questionPattern.equals(userSentence)) {
                            similarity = 100;
                        }
                        if (similarity >= familiarity) {
                            familiarity = similarity;
                            diffString[0] = htmlDiff;
                            diffString[1] = questionPattern;
                            diffString[2] = text;
                        }
                        Log.d("BestDiff", "insertedNum:" + insertedNum + "  deletedNum:" + deletedNum);
                        Log.d("BestDiff", "getFamiliarity: " + similarity);
                    }
                } else if (optionsList.size() == 2) {
                    for (int i = 0; i < optionsList.get(0).length; i++) {
                        for (int j = 0; j < optionsList.get(1).length; j++) {
                            String questionPattern = cursor.getString(3).replaceFirst("\\.\\.\\.", optionsList.get(0)[i]).replaceFirst("\\.\\.\\.", optionsList.get(1)[j]).replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                            String userSentence = text.replaceAll("[,|.|!|?|']", "").trim().replaceAll(" +", " ").toLowerCase();
                            Log.d("BestDiff", questionPattern + " / " + userSentence);
                            diffMatchPatch diff_matchPatch_obj = new diffMatchPatch();
                            LinkedList<diffMatchPatch.Diff> diff = diff_matchPatch_obj.diff_wordMode(userSentence, questionPattern);
                            //change tag <u> to real del line
                            String htmlDiff = diff_matchPatch_obj.diff_prettyHtml(diff);
                            htmlDiff = htmlDiff.replaceAll(" </u>", "</u> ");
                            htmlDiff = htmlDiff.replaceAll(" </del>", "</del> ");
                            htmlDiff = htmlDiff.replaceAll(" </span>", "</span> ");
                            Log.d("BestDiff", "onResults: " + htmlDiff);
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
                            GetDiffWords getDiffWords = new GetDiffWords();
                            String[] words = new String[100];
                            getDiffWords.getInsert(htmlDiff, words);
                            int insertedNum = 0;
                            for (int k = 0; k < words.length; k++) {
                                if (words[k] == null) {
                                    break;
                                }
                                insertedNum++;
                            }

                            // similarity
                            String[] deletedWords = new String[100];
                            getDiffWords.getDelete(htmlDiff, deletedWords);
                            int deletedNum = 0;
                            for (int k = 0; k < deletedWords.length; k++) {
                                if (deletedWords[k] == null) {
                                    break;
                                }
                                deletedNum++;
                            }

                            double len = questionPattern.split(" ").length;
                            double similarity = 100;
                            if (insertedNum > deletedNum) {
                                similarity = (int) ((1 - (insertedNum / len)) * 10000.0) / 100.0;
                            } else {
                                similarity = (int) ((1 - (deletedNum / len)) * 10000.0) / 100.0;
                            }
                            if (similarity < 0) {
                                similarity = 0;
                            }
                            if (questionPattern.equals(userSentence)) {
                                similarity = 100;
                            }
                            if (similarity >= familiarity) {
                                familiarity = similarity;
                                diffString[0] = htmlDiff;
                                diffString[1] = questionPattern;
                                diffString[2] = text;
                            }
                            Log.d("similarity", "getFamiliarity: " + similarity);
                        }
                    }
                }
            }
        }
        return familiarity;
    }
}
