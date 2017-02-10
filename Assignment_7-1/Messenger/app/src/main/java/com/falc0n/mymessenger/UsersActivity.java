package com.falc0n.mymessenger;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.falc0n.mymessenger.ChatActivity.lastSeen;
import static com.falc0n.mymessenger.MainActivity.LOG;
import static com.falc0n.mymessenger.UsersRVAdapter.DELETE_CHAT_FLAG;
import static com.falc0n.mymessenger.UsersRVAdapter.VIEW_PROFILE_FLAG;

public class UsersActivity extends AppCompatActivity implements UsersRVAdapter.RVItemTouchListener, View.OnClickListener {
    ArrayList<User> userArrayList = new ArrayList<>();
    UsersRVAdapter usersRVAdapter;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    User currentUser = new User();
    ProgressDialog progressDialog;
    String userLastSeen;
    boolean flag = true;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        //Retrieve users from DB and display them
        //Should  be a Recycler view
        final AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
        builder.setTitle("Loading....")
                .setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewUsers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        getUserLastSeen();
        usersRVAdapter = new UsersRVAdapter(this, R.layout.user_item, userArrayList, this);

        recyclerView.setAdapter(usersRVAdapter);



        findViewById(R.id.imageButtonUsersLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                finish();
            }
        });
        findViewById(R.id.imageButtonCArProfilePic).setOnClickListener(this);
        findViewById(R.id.textViewUsersUserName).setOnClickListener(this);
        retrieveUsers();

        Log.d(LOG,"HELLO "+userLastSeen);
        for(User u : userArrayList)
        {
            if(checkForNewMessages(u.getUserId()))
            {
                u.setNewMessage(1);
            }else {
                u.setNewMessage(0);
            }
        }

        usersRVAdapter.notifyDataSetChanged();
        alertDialog.dismiss();
        flag = false;
    }

    private void getUserLastSeen() {
        Log.d(LOG,"HELLO in lastseen");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("messages/"+firebaseAuth.getCurrentUser().getUid());
        Log.d(LOG,"HELLO in lastseen DBRef"+databaseReference.getKey());
        Log.d(LOG,"HELLO in lastseen DBRef"+databaseReference.toString());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> tempMap = new HashMap<>();
                Log.d(LOG,"HELLO last seen key"+dataSnapshot.getKey().toString());
                Log.d(LOG,"HELLO  last seen child"+dataSnapshot.getChildren().toString());
                try {
                    Log.d(LOG, "HELLO last seen full" + dataSnapshot.getValue().toString());
                }catch (Exception e)
                {
                    auth.signOut();
                }
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    tempMap = (Map<String, String>) ds.getValue();
                    Log.d(LOG,"HELLO USer time"+ds.getValue().toString());
                    userLastSeen = tempMap.get("UPDATED_TIME");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void retrieveUsers() {
        //FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> tempMap = new HashMap<>();
                userArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(LOG, "Snapshot key is " + snapshot.getKey());
                    tempMap = (Map<String, String>) snapshot.getValue();
                    User user = new User();
                    user.setUserId(tempMap.get("userId"));
                    user.setFirstName(tempMap.get("firstName"));
                    user.setLastName(tempMap.get("lastName"));
                    user.setGender(tempMap.get("gender"));
                    user.setAvatarUrl(tempMap.get("avatarUrl"));
                    Log.d(LOG, "Adding user " + user.toString());
                    if (snapshot.getKey().matches(auth.getCurrentUser().getUid())) {
                        currentUser = user;
                        setProfileData(user);
                        continue;
                    }
                    userArrayList.add(user);
                }
                usersRVAdapter.notifyDataSetChanged();
              //  progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean checkForNewMessages(String userId) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String key= Utils.xor(userId,auth.getCurrentUser().getUid());
        DatabaseReference databaseMessageReference = firebaseDatabase.getReference().child("messages/"+auth.getCurrentUser().getUid()+"/"+key);
        final boolean[] flag = {false};
        databaseMessageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String messageTime;
                Map<String, String> tempMap = new HashMap<>();
                long count = dataSnapshot.getChildrenCount();
                Log.d(LOG,"Message Count is "+count);
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    tempMap = (Map<String, String>) ds.getValue();
                    Log.d(LOG,"snapshot : "+ds.toString());
                    messageTime = tempMap.get("time");
                    Log.d(LOG,"snapshot Message time is : "+messageTime);
                    if(checkDate(messageTime,userLastSeen)>0)
                    {
                        flag[0] = true;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return flag[0];
    }

    private int checkDate(String messageTime, String userLastSeen) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Log.d(LOG,"Message timing is "+messageTime);
        Log.d(LOG,"USER timing is "+userLastSeen);
        try {
            Date dateMessage = dateFormat.parse(messageTime);
            Date dateUser = dateFormat.parse(userLastSeen);
            return dateMessage.compareTo(dateUser);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -100;
    }

    private void setProfileData(User user) {
        ((TextView) findViewById(R.id.textViewUsersUserName)).setText(user.getFirstName() + " " + user.getLastName());
        ImageView imageView = (ImageView) findViewById(R.id.imageButtonCArProfilePic);
        if (!currentUser.getAvatarUrl().matches("NO_IMAGE")) {
            Picasso.with(UsersActivity.this).load(user.getAvatarUrl()).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.user);
            imageView.setColorFilter(Color.argb(0, 0, 0, 0));
            // imageView.setBackgroundTintList(new ColorStateList());
        }
    }

    @Override
    public void deletedItem(int position) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String key= Utils.xor(userArrayList.get(position).getUserId(),auth.getCurrentUser().getUid());
        DatabaseReference databaseMessageReference = firebaseDatabase.getReference().child("messages/"+auth.getCurrentUser().getUid()+"/"+key);
        databaseMessageReference.setValue(null);
    }

    @Override
    public void selectedItem(int position, int flag) {
        switch (flag) {
            case VIEW_PROFILE_FLAG:
                //Show Profile of user
                Log.d(LOG, "Selected user is " + userArrayList.get(position).getFirstName());
                Intent intent = new Intent(UsersActivity.this,EditProfile.class);
                intent.putExtra("USER",userArrayList.get(position));
                intent.putExtra("ACTIVITY_KEY","VIEW");
                startActivity(intent);

                break;
            case DELETE_CHAT_FLAG:
                Log.d(LOG, "Selected user to delete is " + userArrayList.get(position).getFirstName());
                //Delete Messages
                deletedItem(position);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButtonCArProfilePic:
            case R.id.textViewUsersUserName:
                Intent intent = new Intent(UsersActivity.this, EditProfile.class);
                intent.putExtra("ACTIVITY_KEY", "EDIT");
                intent.putExtra("USER", currentUser);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onDestroy() {
        try {
            lastSeen();
        } catch (Exception e)
        {
            //
        }
        super.onDestroy();
    }
}
