package com.naer.pdfreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// sqlite database on the device
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private final static int _DBVersion = 1;
    private final static String _DBName = "vision-dialog.db";
    private final static String _TableName = "User";

    public MySQLiteOpenHelper(Context context) {
        super(context, _DBName, null, _DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL = "CREATE TABLE IF NOT EXISTS " + _TableName + "(" +
                "username VARCHAR(50), " +
                "password VARCHAR(50)," +
                "pretest Text, " +
                "pretestUploaded int, " +
                "posttest Text, " +
                "posttestUploaded int, " +
                "homework1 Text, " +
                "homework1Uploaded int, " +
                "homework2 Text, " +
                "homework2Uploaded int, " +
                "homework3 Text, " +
                "homework3Uploaded int, " +
                "homework4 Text, " +
                "homework4Uploaded int," +
                "homework5 Text, " +
                "homework5Uploaded int, " +
                "homework6 Text, " +
                "homework6Uploaded int" +
                ");";
        sqLiteDatabase.execSQL(SQL);

        final String createQuestionTableSQL = "CREATE TABLE IF NOT EXISTS `question` (\n" +
                "  `id` int(11) NOT NULL,\n" +
                "  `questionId` int(11) NOT NULL,\n" +
                "  `unit` varchar(15) NOT NULL,\n" +
                "  `question` text NOT NULL,\n" +
                "  `options` text NOT NULL,\n" +
                "  `translation` text NOT NULL\n" +
                ");";
        sqLiteDatabase.execSQL(createQuestionTableSQL);

        final String createAnswerTableSQL = "CREATE TABLE IF NOT EXISTS `answer` (" +
                "  `id` int(11) NOT NULL," +
                "  `questionId` int(11) NOT NULL," +
                "  `unit` int(11) NOT NULL," +
                "  `answer` text NOT NULL," +
                "  `options` text NOT NULL,\n" +
                "  `translation` text NOT NULL\n" +
                ");";
        sqLiteDatabase.execSQL(createAnswerTableSQL);

        final String createPatternTableSQL = "CREATE TABLE IF NOT EXISTS `pattern` (" +
                "  `unit` int(2) NOT NULL," +
                "  `category` text NOT NULL," +
                "  `text` text NOT NULL,\n" +
                "  `translation` text NOT NULL\n" +
                ");";
        sqLiteDatabase.execSQL(createPatternTableSQL);

        Cursor cursor = sqLiteDatabase.rawQuery("select * from User", null);
        if (!cursor.moveToFirst()) {
            initData(sqLiteDatabase);
        }
        cursor.close();
    }

    public void initData(SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", "");
        contentValues.put("password", "");
        contentValues.put("pretest", "");
        contentValues.put("pretestUploaded", 0);
        contentValues.put("posttest", "");
        contentValues.put("posttestUploaded", 0);
        contentValues.put("homework1", "");
        contentValues.put("homework1Uploaded", 0);
        contentValues.put("homework2", "");
        contentValues.put("homework2Uploaded", 0);
        contentValues.put("homework3", "");
        contentValues.put("homework3Uploaded", 0);
        contentValues.put("homework4", "");
        contentValues.put("homework4Uploaded", 0);
        contentValues.put("homework5", "");
        contentValues.put("homework5Uploaded", 0);
        contentValues.put("homework6", "");
        contentValues.put("homework6Uploaded", 0);
        sqLiteDatabase.insert(_TableName, null, contentValues);

        final String insertQuestionTableSQL = "INSERT INTO `question` (`id`,`questionId`, `unit`,`question`,`options`, `translation`) VALUES\n" +
                "(1, 1, '2', 'How do you go to school?', 'transportation', '你是如何上學的?')," +
                "(2, 2, '2', 'How does he go to school?', 'transportation', '他是如何上學的?')," +
                "(3, 3, '2', 'How does she go to school?', 'transportation', '她是如何上學的?')," +
                "(4, 4, '2', 'Do you ride a bike to school?', 'transportationVerb', '你是騎腳踏車上學的嗎?')," +
                "(5, 5, '2', 'Does he go to school by riding a bike?', 'transportationVing', '他是騎腳踏車上學的嗎?')," +
                "(6, 6, '2', 'Does she go to school by bike?', 'transportation', '她是騎腳踏車上學的嗎?')," +
                "(7, 7, '3', 'What do you do after class?', 'activities', '你下課時會做什麼?')," +
                "(8, 8, '3', 'Do you watch TV after finishing your homework?', 'activities', '你完成作業後會看電視嗎?')," +
                "(9, 9, '3', 'What does he do after school?', 'activitiesThird', '他放學時會做什麼?')," +
                "(10, 10, '3', 'What does she do in her free time?', 'activitiesThird', '她在空閒時會做什麼?')," +
                "(11, 11, '3', 'What do you like to do on the weekend?', 'activities', '你會在週末做什麼?')," +
                "(12, 12, '3', 'What do you prefer to do in your free time?', 'activitiesVing', '你在空閒時喜歡做什麼?')";
        sqLiteDatabase.execSQL(insertQuestionTableSQL);

        final String insertAnswerTableSQL = "INSERT INTO `answer` (`id`,`questionId`, `unit`,`answer`,`options`, `translation`) VALUES\n" +
                "(1, 1, '2', 'I go to school ...', 'transportation','我...去學校。')," +
                "(2, 2, '2', 'He goes to school ...', 'transportation','他...去學校。')," +
                "(3, 3, '2', 'She goes to school ...', 'transportation','她...去學校。')," +
                "(4, 4, '2', 'Yes, I do. I ... to school.', 'transportationVerb','對，我是這樣上學的。')," +
                "(5, 5, '2', 'Yes, He does. He ... to school.', 'transportationVing','對，他是這樣上學的。')," +
                "(6, 6, '2', 'Yes, She does. She ... to school.', 'transportationVerb','對，她是這樣上學的。')," +
                "(7, 7, '3', 'I ... after class.', 'activities','我下課時會...。')," +
                "(8, 8, '3', 'Yes, I do. I ... after finishing my homework.', 'activities','我完成作業後會...。')," +
                "(9, 9, '3', 'He ... after school.', 'activitiesThird','他下課時會...。')," +
                "(10, 10, '3', 'She ... in her free time.', 'activitiesThird','她空閒時會...。')," +
                "(11, 11, '3', 'I like to ... on the weekend.', 'activities','我週末喜歡...。')," +
                "(12, 12, '3', 'I spent my time ...', 'activitiesVing','我會花時間去...。')";
        sqLiteDatabase.execSQL(insertAnswerTableSQL);

        final String insertPatternTableSQL = "INSERT INTO `pattern` (`unit`,`category`, `text`, `translation`) VALUES\n" +
                "(2, 'transportation','on foot','走路'),\n" +
                "(2, 'transportation','by bicycle','騎腳踏車'),\n" +
                "(2, 'transportation','by bike','騎腳踏車'),\n" +
                "(2, 'transportation','by bus','搭公車'),\n" +
                "(2, 'transportation','by scooter','騎機車'),\n" +
                "(2, 'transportation','by taxi','搭計程車'),\n" +
                "(2, 'transportation','by car','搭車'),\n" +
                "(2, 'transportation','by train','搭火車'),\n" +
                "(2, 'transportation','by boat','搭船'),\n" +
                "(2, 'transportation','by plane','搭飛機'),\n" +
                "(2, 'transportation','by MRT','搭捷運'),\n" +
                "(2, 'transportation','by metro','搭地鐵'),\n" +
                "(2, 'transportation','by motorcycle','騎機車'),\n" +
                "(2, 'transportation','by THSR','搭高鐵'),\n" +

                "(2, 'transportationVerb','walk','走路'),\n" +
                "(2, 'transportationVerb','ride a bicycle','騎腳踏車'),\n" +
                "(2, 'transportationVerb','ride a bike','騎腳踏車'),\n" +
                "(2, 'transportationVerb','ride a motorcycle','騎機車'),\n" +
                "(2, 'transportationVerb','ride a scooter','騎機車'),\n" +
                "(2, 'transportationVerb','take a car','搭車'),\n" +
                "(2, 'transportationVerb','drive a car','開車'),\n" +
                "(2, 'transportationVerb','take a bus','搭公車'),\n" +
                "(2, 'transportationVerb','take the MRT','搭捷運'),\n" +
                "(2, 'transportationVerb','take the metro','搭地鐵'),\n" +
                "(2, 'transportationVerb','take a taxi','搭計程車'),\n" +
                "(2, 'transportationVerb','take a train','搭火車'),\n" +
                "(2, 'transportationVerb','take a boat','搭船'),\n" +
                "(2, 'transportationVerb','take a plane','搭飛機'),\n" +
                "(2, 'transportationVerb','take the THSR','搭高鐵'),\n" +

                "(2, 'transportationVing','walking','走路'),\n" +
                "(2, 'transportationVing','riding a bicycle','騎腳踏車'),\n" +
                "(2, 'transportationVing','riding a bike','騎腳踏車'),\n" +
                "(2, 'transportationVing','riding a motorcycle','騎機車'),\n" +
                "(2, 'transportationVing','riding a scooter','騎機車'),\n" +
                "(2, 'transportationVing','taking a car','搭車'),\n" +
                "(2, 'transportationVing','driving a car','開車'),\n" +
                "(2, 'transportationVing','taking a bus','搭公車'),\n" +
                "(2, 'transportationVing','taking the MRT','搭捷運'),\n" +
                "(2, 'transportationVing','taking the metro','搭地鐵'),\n" +
                "(2, 'transportationVing','taking a taxi','搭計程車'),\n" +
                "(2, 'transportationVing','taking a train','搭火車'),\n" +
                "(2, 'transportationVing','taking a boat','搭船'),\n" +
                "(2, 'transportationVing','taking a plane','搭飛機'),\n" +
                "(2, 'transportationVing','taking the THSR','搭高鐵'),\n" +

                "(3, 'activities','listen to music','聽音樂'),\n" +
                "(3, 'activities','play baseball','打棒球'),\n" +
                "(3, 'activities','play basketball','打籃球'),\n" +
                "(3, 'activities','play football','打美式足球'),\n" +
                "(3, 'activities','play table tennis','打桌球'),\n" +
                "(3, 'activities','ride a bike','騎單車'),\n" +
                "(3, 'activities','surf the Internet','上網'),\n" +
                "(3, 'activities','go jogging','慢跑'),\n" +
                "(3, 'activities','watch TV','看電視'),\n" +
                "(3, 'activities','read a book','看書'),\n" +
                "(3, 'activities','do exercise','做運動'),\n" +
                "(3, 'activities','play video games','玩電子遊戲'),\n" +
                "(3, 'activities','play hide and seek','玩躲貓貓'),\n" +
                "(3, 'activities','slide down the slide','滑溜滑梯'),\n" +
                "(3, 'activities','play on the swing','盪鞦韆'),\n" +
                "(3, 'activities','play on the seesaw','玩翹翹板'),\n" +
                "(3, 'activities','draw a picture','畫圖'),\n" +
                "(3, 'activities','walk the dog','遛狗'),\n" +
                "(3, 'activities','play in the playground','在遊樂場玩'),\n" +
                "(3, 'activities','learn English','學習英文'),\n" +
                "(3, 'activities','dance','跳舞'),\n" +
                "(3, 'activities','chat','聊天'),\n" +
                "(3, 'activities','play cards','打牌'),\n" +

                "(3, 'activitiesThird','listens to music','聽音樂'),\n" +
                "(3, 'activitiesThird','plays baseball','打棒球'),\n" +
                "(3, 'activitiesThird','plays basketball','打籃球'),\n" +
                "(3, 'activitiesThird','plays football','打美式足球'),\n" +
                "(3, 'activitiesThird','plays table tennis','打桌球'),\n" +
                "(3, 'activitiesThird','rides a bike','騎單車'),\n" +
                "(3, 'activitiesThird','surfs the Internet','上網'),\n" +
                "(3, 'activitiesThird','goes jogging','慢跑'),\n" +
                "(3, 'activitiesThird','watches TV','看電視'),\n" +
                "(3, 'activitiesThird','reads a book','看書'),\n" +
                "(3, 'activitiesThird','does exercise','做運動'),\n" +
                "(3, 'activitiesThird','plays video games','玩電子遊戲'),\n" +
                "(3, 'activitiesThird','plays hide and seek','玩躲貓貓'),\n" +
                "(3, 'activitiesThird','slides down the slide','滑溜滑梯'),\n" +
                "(3, 'activitiesThird','plays on the swing','盪鞦韆'),\n" +
                "(3, 'activitiesThird','plays on the seesaw','玩翹翹板'),\n" +
                "(3, 'activitiesThird','draws a picture','畫圖'),\n" +
                "(3, 'activitiesThird','walks the dog','遛狗'),\n" +
                "(3, 'activitiesThird','plays in the playground','在遊樂場玩'),\n" +
                "(3, 'activitiesThird','learns English','學習英文'),\n" +
                "(3, 'activitiesThird','dances','跳舞'),\n" +
                "(3, 'activitiesThird','chats','聊天'),\n" +
                "(3, 'activitiesThird','plays cards','打牌'),\n" +

                "(3, 'activitiesVing','listening to music','聽音樂'),\n" +
                "(3, 'activitiesVing','playing baseball','打棒球'),\n" +
                "(3, 'activitiesVing','playing basketball','打籃球'),\n" +
                "(3, 'activitiesVing','playing football','打美式足球'),\n" +
                "(3, 'activitiesVing','playing table tennis','打桌球'),\n" +
                "(3, 'activitiesVing','riding a bike','騎單車'),\n" +
                "(3, 'activitiesVing','surfing the Internet','上網'),\n" +
                "(3, 'activitiesVing','going jogging','慢跑'),\n" +
                "(3, 'activitiesVing','watching TV','看電視'),\n" +
                "(3, 'activitiesVing','reading a book','看書'),\n" +
                "(3, 'activitiesVing','doing exercise','做運動'),\n" +
                "(3, 'activitiesVing','playing video games','玩電子遊戲'),\n" +
                "(3, 'activitiesVing','playing hide and seek','玩躲貓貓'),\n" +
                "(3, 'activitiesVing','playing on the slide','滑溜滑梯'),\n" +
                "(3, 'activitiesVing','playing on the swing','盪鞦韆'),\n" +
                "(3, 'activitiesVing','playing on the seesaw','玩翹翹板'),\n" +
                "(3, 'activitiesVing','drawing a picture','畫圖'),\n" +
                "(3, 'activitiesVing','walking the dog','遛狗'),\n" +
                "(3, 'activitiesVing','playing in the playground','在遊樂場玩'),\n" +
                "(3, 'activitiesVing','learning English','學習英文'),\n" +
                "(3, 'activitiesVing','dancing','跳舞'),\n" +
                "(3, 'activitiesVing','chatting','聊天'),\n" +
                "(3, 'activitiesVing','playing cards','打牌')";
        sqLiteDatabase.execSQL(insertPatternTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String SQL = "DROP TABLE " + _TableName;
        sqLiteDatabase.execSQL(SQL);
    }
}
