package Firebase;

import android.icu.text.LocaleDisplayNames;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naer.pdfreader.DescribeActivity;
import com.naer.pdfreader.DialogActivity;
import com.naer.pdfreader.Place;
import com.naer.pdfreader.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Interface.IDataDownloadCompleted;

public class GetDataFromFirebase {
    private static IDataDownloadCompleted iDataDownloadCompleted;//Callback
    private static List<String> SentencesKeyList = new ArrayList<>();
    public static Map<String,String> SentencesMap = new HashMap<>();

    public GetDataFromFirebase(IDataDownloadCompleted iDataDownloadCompleted) { //初始化Callback
        this.iDataDownloadCompleted = iDataDownloadCompleted;
    }

    public static void DownSentencesKey()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(DescribeActivity.placeview).child("vocabulary");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    SentencesKeyList.add(ds.getKey().toString());
                    Log.d("SentencesKey",ds.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void Call()
    {
        iDataDownloadCompleted.DownloadCompleted();//呼叫Callback
    }

    public static void DownSentencesMap()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(DescribeActivity.placeview).child("vocabulary");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    SentencesMap.put(ds.getKey().toString(),ds.getValue().toString());
                    Log.d("SentencesMap",ds.getKey().toString()+":"+ds.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static List<String> GetSentencesList()
    {
        return  SentencesKeyList;
    }

    public static void ClearList()
    {
        SentencesKeyList.clear();
    }

    public static Map<String,String> GetSentencesMap()
    {
        return  SentencesMap;
    }
}
