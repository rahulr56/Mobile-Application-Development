package com.falc0n.inclass11;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.falc0n.inclass11.LoginActivity.LOG;

/**
 * Created by fAlc0n on 11/12/16.
 */

public class GetMessagesFromFireBase {
    public static ArrayList<String> GetMessages(final ArrayList<String> msgList)
    {
        DatabaseReference databaseReference;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReferenceFromUrl("project/my-messenger-2bbab/database/data");
        databaseReference = firebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String msg = ds.getValue(String.class);
                    msgList.add(msg);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG,"Error in retrieving messages from Database!");
            }
        });
        return msgList;
    }

}
