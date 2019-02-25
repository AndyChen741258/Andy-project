package Firebase;

import android.icu.text.LocaleDisplayNames;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetDataFromFirebase {
    private static List<String> SentencesKeyList = new ArrayList<>();
    public static Map<String,String> SentencesMap = new HashMap<>();

    public static void DownSentencesKey()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DefaultExample");
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

    public static void DownSentencesMap()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DefaultExample");
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

    public static Map<String,String> GetSentencesMap()
    {
        return  SentencesMap;
    }
}
