package Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.naer.pdfreader.Student;

import java.text.SimpleDateFormat;

import Model.StudentsSpeech;

public class UploadStudentsBehavior {

    public static void UploadSpeech(StudentsSpeech studentsSpeech)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new java.util.Date());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("學生資料")
                .child("學生"+Student.Name+"號")
                .child("行為紀錄")
                .child("語音辨識")
                .child(date)
                .child(studentsSpeech.Correct)
                .child(studentsSpeech.Question);
        databaseReference.push().setValue(studentsSpeech);
    }

    public static void UploadStudentCheckedBehavior(StudentsSpeech studentsSpeech)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new java.util.Date());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("學生資料")
                .child("學生"+Student.Name+"號")
                .child("語音辨識")
                .child(date)
                .child(studentsSpeech.Correct)
                .child(studentsSpeech.Question);
        databaseReference.push().setValue(studentsSpeech);
    }
}
