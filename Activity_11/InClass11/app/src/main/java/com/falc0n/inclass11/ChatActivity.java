package com.falc0n.inclass11;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.falc0n.inclass11.LoginActivity.LOG;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener , MessageAdapter.ItemTouchHelper{
    ArrayList<ChatMessage> messagesList = new ArrayList<>();
    final static int SELECT_PICTURE = 1000;
    static String USER_NAME = "X";
    EditText messageEditText;
    RecyclerView listViewMainMessage;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String userId = firebaseAuth.getCurrentUser().getUid();
    DatabaseReference reference = firebaseDatabase.getReference("messages");
    MessageAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG, "In onCreate of ChatActivity");
        setContentView(R.layout.activity_chat);
        getUserName();
        ((TextView)findViewById(R.id.textViewUserNameCA)).setText(USER_NAME);
        DatabaseReference childReference = firebaseDatabase.getReference(userId);
                messageEditText = (EditText) findViewById(R.id.editTextSendMessage);
        retrieveMessages();
        reference.getDatabase().getReference(userId);
        listViewMainMessage = (RecyclerView) findViewById(R.id.recyclerViewChat);
        messagesAdapter = new MessageAdapter(messagesList, R.layout.recycleritem, ChatActivity.this,this);
        listViewMainMessage.setAdapter(messagesAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        listViewMainMessage.setLayoutManager(layoutManager);
        findViewById(R.id.imageButtonSend).setOnClickListener(this);
        findViewById(R.id.imageButtonGallery).setOnClickListener(this);
        findViewById(R.id.imageButtonLogOutCA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG,"Clicked LOGOUT");
                firebaseAuth.signOut();
                finish();
            }
        });
    }
    private void getUserName()
    {
        reference = reference.getDatabase().getReference(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                USER_NAME = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void retrieveMessages() {
        reference = reference.getDatabase().getReference("messages");
        DatabaseReference ds = reference.child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(LOG,"Data set changed!!!");
                ArrayList<ChatMessage> tempList = new ArrayList<>();
                messagesList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String, String> tempMap = new HashMap<String, String>();
                    tempMap = (HashMap<String, String>) ds.getValue();

                    Log.d(LOG,"RETRIVAL HASH MAP "+tempMap.toString());
                    Log.d(LOG,"RETRIVAL Value : "+ds.getValue());
                    ChatMessage msgToInsert = new ChatMessage();
                    Log.d(LOG,"URL IS URL--"+tempMap.get("url")+"---");
                    if(tempMap.get("url").matches("NO_IMAGE"))
                    {
                        msgToInsert.setTime(tempMap.get("time"));
                        msgToInsert.setUser(tempMap.get("user"));
                        msgToInsert.setText(tempMap.get("text"));
                        msgToInsert.setUrl("NO_IMAGE");
                    }else
                    {
                        msgToInsert.setText("");
                        msgToInsert.setUrl(tempMap.get("url"));
                        msgToInsert.setTime(tempMap.get("time"));
                        msgToInsert.setUser(tempMap.get("user"));
                    }
                    messagesList.add(msgToInsert);
                }

                Log.d(LOG,"Size of list after insertion :"+messagesList.size());
                for(int i=0;i<messagesList.size();i++)
                {
                    Log.d(LOG,"MESSAGE LIST is "+messagesList.get(i));
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    String message;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButtonSend:
                ChatMessage chatMessage =  new ChatMessage();
                message = ((EditText) findViewById(R.id.editTextSendMessage)).getText().toString().trim();
                if (message == null || message.matches("")) {
                    Toast.makeText(ChatActivity.this, "Empty message, discarding!", Toast.LENGTH_SHORT).show();
                    return;
                }
                chatMessage.setText(message);
                chatMessage.setUrl("NO_IMAGE");
                String currDate= new SimpleDateFormat("dd/MM/YYYY HH:mm:ss").format(new Date());
                chatMessage.setUser(USER_NAME);
                chatMessage.setTime(currDate);
                Log.d(LOG,"Inserting "+chatMessage.toString());
                InsertDataIntoFireBase(chatMessage);
                message = null;
                break;
            case R.id.imageButtonGallery:
                selectImage();
                break;
        }
    }

    private void InsertDataIntoFireBase(ChatMessage msg) {
        String ref = "messages";

        reference = reference.getDatabase().getReference(ref);
        Log.d(LOG,"Pushing to FB "+msg.toString());
        HashMap<String,Object> map = new HashMap<>();
        map.put("user",msg.getUser());
        map.put("time",msg.getTime());
        map.put("text",msg.getText());
        map.put("url",msg.getUrl());
        map.put("commentList",msg.getCommentList());
        reference.push();
        reference.child(messagesList.size()+1+"").setValue(map);
        ((EditText) findViewById(R.id.editTextSendMessage)).setText("");
    }

    private void selectImage() {
        Intent intent = new Intent();
        Log.d(LOG, "Setting intent type to images");
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Log.d(LOG, "Starting intent");
        Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT, Uri.parse(
                "content://media/internal/images/media"));
        startActivityForResult(intent1, SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            Log.d(LOG, "Result received");
            Uri selectedImageUri = data.getData();
            Log.d(LOG, "Result URI : " + selectedImageUri.toString());
            Log.d(LOG, "Get fireBase STORAGE ref");
            String random= new SimpleDateFormat("ddMMYYYYHHmmssSSS").format(new Date());

            Log.d(LOG,"Date string is "+random);
            String filePath = "messages"+random+".jpeg";
            StorageReference fireBaseStorageRef = FirebaseStorage.getInstance().getReference(filePath);
            Log.d(LOG, "Uploading....");
            message = messageEditText.getText().toString();
            final String[] imageUrl = new String[1];
            //InputStream inputStream = new FileInputStream(new File(getPath(selectedImageUri)));
            UploadTask uploadTask = fireBaseStorageRef.putFile(selectedImageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(LOG, "Upload SUCCESS. Url is "+taskSnapshot.getDownloadUrl());
                    //messagesList.add(taskSnapshot.getDownloadUrl().toString());
                    //messagesAdapter.notifyDataSetChanged();
                    imageUrl[0] = taskSnapshot.getDownloadUrl().toString();
                    setImageURL(taskSnapshot.getDownloadUrl().toString());
                    Toast.makeText(ChatActivity.this, "File uploaded successfully!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(LOG, "Upload FAILED");
                    e.printStackTrace();
                    Toast.makeText(ChatActivity.this, "Failed to upload image! try again!", Toast.LENGTH_LONG).show();
                }
            });
            messageEditText.setText("");
        } else {
            Log.d(LOG, "Result  : UNKNOWN");
        }
    }
    public void setImageURL(String url)
    {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUrl(url);
        chatMessage.setUser(USER_NAME);
        String currDate= new SimpleDateFormat("dd/MM/YYYY HH:mm:ss").format(new Date());
        chatMessage.setTime(currDate);
        chatMessage.setText("");
        InsertDataIntoFireBase(chatMessage);
        retrieveMessages();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void DeleteItemOnSwipe(int position) {
        /*Map<String, String> tempMap = null;
        for(int i=0;i<messagesList.size();i++)
        {
            tempMap.put(Integer.toString(i),messagesList.get(i));
            //Log.d(LOG,tempMap.)
        }
       // reference.updateChildren(tempMap);*/
        reference = reference.getDatabase().getReference(userId + "/messages");
        reference.setValue(messagesList);

    }

    @Override
    public void DeleteItem(int position) {
        Log.d(LOG,"Item swiped off at "+position);
        messagesList.remove(position);
        String userId = "messages";
//        reference = reference.getDatabase().getReference(userId);
        reference.setValue(messagesList);
        retrieveMessages();
    }

    @Override
    public void AddComment(int position) {
        createAlertDialog(position);
        if(message==null)
        {
            return;
        }

        //reference = reference.getDatabase().getReference(userId);
    }

    private void addMessage(int position) {
        Log.d(LOG,"Clicking add commnet");
        ArrayList<ChatMessage> tempList = new ArrayList<>();
        Log.d(LOG,"Retrieved to add comment"+messagesList.get(position).toString());
        ChatMessage chatMessage = new ChatMessage();//messagesList.get(position);
        tempList= messagesList.get(position).getCommentList();
        chatMessage.setUser(USER_NAME);
        chatMessage.setText(message);
        String currDate= new SimpleDateFormat("dd/MM/YYYY HH:mm:ss").format(new Date());
        chatMessage.setTime(currDate);
        chatMessage.setUrl("NO_IMAGE");
        //tempList = messagesList.get(position).getCommentList();
        tempList.add(chatMessage);
        messagesList.get(position).setCommentList(tempList);
        Log.d(LOG,"Adding comment "+chatMessage.toString());
        //messagesList.get(position);
        //messagesList.remove(position);
        //messagesList.add(position,chatMessage);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages");
        String userId = "messages";
        reference.setValue(messagesList);
    }


    private void createAlertDialog(final int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Comment");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                message = input.getText().toString();
                Log.d(LOG,"Comment to be added is "+message);
                addMessage(position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                message = null;
                dialog.cancel();
            }
        });
        builder.setCancelable(false);
        builder.show();
        Log.d(LOG,"Comment to be added is "+message);
    }
}
