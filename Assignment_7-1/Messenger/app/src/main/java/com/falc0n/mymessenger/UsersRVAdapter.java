package com.falc0n.mymessenger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.falc0n.mymessenger.MainActivity.LOG;

/**
 * Created by fAlc0n on 11/17/16.
 */

public class UsersRVAdapter extends RecyclerView.Adapter<UsersRVAdapter.ViewHolder> {
    Context mContext;
    int mResource;
    ArrayList<User> userArrayList;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    RVItemTouchListener rvItemTouchListener;
    public final static int DELETE_CHAT_FLAG = 1000;
    public final static int VIEW_PROFILE_FLAG = 1001;
    public UsersRVAdapter(Context mContext, int mResource, ArrayList<User> userArrayList, RVItemTouchListener rvItemTouchListener) {
        this.mContext = mContext;
        this.mResource = mResource;
        this.userArrayList = userArrayList;
        this.rvItemTouchListener = rvItemTouchListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mResource,parent,false);
        return new UsersRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(LOG,"In bindview holder");
        User user = userArrayList.get(position);
        if(user.getNewMessage() != 0 )
        {
            holder.linearLayout.setBackgroundColor(Color.RED);
        }
        Log.d(LOG,"setting user Dp to "+user.getAvatarUrl());
        Picasso.with(mContext).load(user.getAvatarUrl()).into(holder.imageViewProfilePic);
        Log.d(LOG,"Finished setting user DP");
        holder.textViewUserName.setText(user.getFirstName()+" "+user.getLastName());
    }

    @Override
    public int getItemCount() {
        Log.d(LOG,"Count is "+userArrayList.size());
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageViewProfilePic;
        private TextView textViewUserName , textViewNumMessages;
        private LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            imageViewProfilePic = (ImageView) itemView.findViewById(R.id.imageButtonLIUserProfilePic);
            textViewUserName = (TextView) itemView.findViewById(R.id.textViewLIUsersUserName);
            textViewNumMessages = (TextView) itemView.findViewById(R.id.textViewLIUsersMessageCount);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.LLUserLayout);
            imageViewProfilePic.setOnClickListener(this);
            linearLayout.setOnClickListener(this);
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    final String[] options = {"View Profile","Delete Chat"};

                    alertDialog.setTitle("Select an action")
                            .setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d(LOG,"Selected item is "+i+" which is  "+options[i]);
                                    if(i == 1) {
                                        rvItemTouchListener.selectedItem(getAdapterPosition(), DELETE_CHAT_FLAG);

                                    }else if(i == 0)
                                    {
                                        rvItemTouchListener.selectedItem(getAdapterPosition(),VIEW_PROFILE_FLAG);
                                    }
                                }
                            })
                            .create()
                            .show();
                    return true;
                }
            });
        }

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.imageButtonLIUserProfilePic:
                    //show the profile of other user
                    Intent intent1 = new Intent(mContext,EditProfile.class);
                    intent1.putExtra("ACTIVITY_KEY","VIEW");
                    intent1.putExtra("USER",userArrayList.get(getAdapterPosition()));
                    mContext.startActivity(intent1);
                    break;
                case R.id.LLUserLayout:
                    //open chat between 2 users
                    FirebaseAuth auth1 = FirebaseAuth.getInstance();

                    User user = userArrayList.get(getAdapterPosition());
        /*            Log.d(LOG,"selected user is "+user.getFirstName());
                    Log.d(LOG,"selected user userId "+user.getUserId());
                    Log.d(LOG,"Current userID is "+auth1.getCurrentUser().getUid());
                    Log.d(LOG,"XOR of user is "+(Utils.xor(user.getUserId(),auth1.getCurrentUser().getUid())));
                    Log.d(LOG,"XOR of user is "+(Utils.xor(auth1.getCurrentUser().getUid(),user.getUserId())));
*/
                    String key = Utils.xor(auth1.getCurrentUser().getUid(),user.getUserId());
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                    DatabaseReference currUserReference = firebaseDatabase.getReference("messages/"+auth1.getCurrentUser().getUid()+"/"+key);
                    DatabaseReference otherReference = firebaseDatabase.getReference("messages/"+user.getUserId()+"/"+key);

                    currUserReference.push();
                    otherReference.push();

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Map<String,Object> tempMap = new HashMap<>();
                    tempMap.put("UPDATED_TIME",dateFormat.format(date));

                    currUserReference.updateChildren(tempMap);
                    otherReference.updateChildren(tempMap);

                    Intent intent = new Intent(mContext,ChatActivity.class);
                    intent.putExtra("USER_KEY",user);
                    mContext.startActivity(intent);
                    break;
            }

        }
    };
    interface RVItemTouchListener
    {
        void deletedItem(int position);
        void selectedItem(int position, int flag);
    }
}
