package com.falc0n.mymessenger;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.falc0n.mymessenger.MainActivity.LOG;
import static com.falc0n.mymessenger.SignUp.RESULT_LOAD_IMAGE;

public class ChatActivity extends AppCompatActivity {
    ArrayList<ChatMessage> chatMessages = new ArrayList<>();
    String messageLastSentTime;
    Uri selectedImageUri = null;
    ListView listViewCAMessages;
    User user;
    ChatMessageAdapter chatMessageAdapter;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if (getIntent().getExtras() != null) {
            user = (User) getIntent().getExtras().get("USER_KEY");

            TextView textViewUserName = (TextView) findViewById(R.id.textViewCAsUserName);
            textViewUserName.setText(user.getFirstName() + " " + user.getLastName());

            ImageButton imageViewUserDP = (ImageButton) findViewById(R.id.imageButtonCArProfilePic);
            Picasso.with(this).load(user.getAvatarUrl()).into(imageViewUserDP);

            final EditText messageEditText = (EditText) findViewById(R.id.editTextCAMessage);
            listViewCAMessages = (ListView) findViewById(R.id.listViewCAMessages);

            chatMessageAdapter = new ChatMessageAdapter(ChatActivity.this, R.layout.received_message_item, chatMessages);
            listViewCAMessages.setAdapter(chatMessageAdapter);
            chatMessageAdapter.setNotifyOnChange(true);

            retrieveChatMessages(user);

            listViewCAMessages.smoothScrollToPosition(chatMessages.size());
            addChildListener(user);
            findViewById(R.id.imageButtonCAGallery).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImage();
                }
            });
            findViewById(R.id.imageButtonCASend).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message = messageEditText.getText().toString().trim();

                    if (message == null || message.matches("")) {
                        Toast.makeText(ChatActivity.this, "Empty Message, not sent!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    messageEditText.setText("");
                    UpdateListAndPostMessage(message);
                }
            });

        } else {
            Toast.makeText(this, "Internal error occurred!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void UpdateListAndPostMessage(String message) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        ChatMessage chatMessage = new ChatMessage(message, "NO_IMAGE", dateFormat.format(new Date()), auth.getCurrentUser().getUid());
        if (selectedImageUri != null) {
            Uri imageUri = uploadFileToFirebase(selectedImageUri, auth.getCurrentUser().getUid());
            if (imageUri != null) {
                chatMessage.setImageUrl(imageUri.toString());
            } else {
                chatMessage.setImageUrl("NO_IMAGE");
            }
        }

        chatMessages.add(chatMessage);
        Log.d(LOG, "THe message being POSTED is " + chatMessage.toString());
        Utils.postMessage(user.getUserId(), message, chatMessage.getImageUrl());
        chatMessageAdapter.notifyDataSetChanged();
        listViewCAMessages.smoothScrollToPosition(chatMessages.size());
    }

    private void sendMessageFromAlert(final String message, Uri uri) {
        Log.d(LOG, "sendMessageFromAlert MEssage" + message + " URI " + uri);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final ChatMessage chatMessage = new ChatMessage(message, "NO_IMAGE", dateFormat.format(new Date()), auth.getCurrentUser().getUid());
        final boolean[] flag = {true};
        if (uri != null) {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference("messageUploads/" + auth.getCurrentUser().getUid() + ".jpeg");//+firebaseAuth.getCurrentUser().getUid()+".jpeg");
            final Uri[] url = new Uri[1];
            storageReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            url[0] = taskSnapshot.getDownloadUrl();
                            Log.d(LOG, "Data from firebase is " + url[0]);
                            UpdateChatMessageImage(chatMessage, url[0]);
                            chatMessages.add(chatMessage);
                            Log.d(LOG, "THe message being POSTED is " + chatMessage.toString());
                            Utils.postMessage(user.getUserId(), message, chatMessage.getImageUrl());
                            chatMessageAdapter.notifyDataSetChanged();
                            listViewCAMessages.smoothScrollToPosition(chatMessages.size());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to upload the selected image. Try again later!", Toast.LENGTH_LONG).show();
                }
            });
            Log.d(LOG, "entering while loop");
            new SendMessage(ChatActivity.this).execute(storageReference);
           // while (flag[0]) ;
            //Uri imageUri = uploadFileToFirebase(uri,auth.getCurrentUser().getUid());
            //UpdateChatMessageImage(chatMessage, selectedImageUri);
        }
    }

    private void UpdateChatMessageImage(ChatMessage chatMessage, Uri uri) {
        Log.d(LOG, "Image uri from firebase is " + uri);
        if (uri != null) {
            chatMessage.setImageUrl(uri.toString());
        } else {
            chatMessage.setImageUrl("NO_IMAGE");
        }

    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://media/internal/images/media"));
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();
            final AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
            builder.setTitle("Selected Image is ");

            final EditText input = new EditText(getApplicationContext());
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);

            LayoutInflater factory = LayoutInflater.from(this);
            final View view = factory.inflate(R.layout.alert, null);
            ImageView image = (ImageView) view.findViewById(R.id.imageViewAlertImage);
            final Uri alertImageURl = selectedImageUri;
            image.setImageURI(alertImageURl);

            builder.setView(view);
            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String alertMessage = ((EditText) view.findViewById(R.id.editTextAlertMessage)).getText().toString().trim();
                    Log.d(LOG, " need to send message " + alertMessage + "   " + alertImageURl);
                    dialogInterface.dismiss();
                    sendMessageFromAlert(alertMessage, selectedImageUri);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).setCancelable(false);
            builder.show();
        }
    }

    private void addChildListener(final User user) {
        String key = Utils.xor(user.getUserId(), auth.getCurrentUser().getUid());
        DatabaseReference databaseReference = firebaseDatabase.getReference("messages/" + auth.getCurrentUser().getUid() + "/" + key);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                retrieveChatMessages(user);
                chatMessageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                retrieveChatMessages(user);
                chatMessageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                retrieveChatMessages(user);
                chatMessageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void retrieveChatMessages(User user) {
        //  Log.d(LOG,"In retrieveChatMessages");
        String key = Utils.xor(user.getUserId(), auth.getCurrentUser().getUid());
        DatabaseReference databaseReference = firebaseDatabase.getReference("messages/" + auth.getCurrentUser().getUid() + "/" + key);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> tempMap = new HashMap<>();
                chatMessages.clear();
                //        Log.d(LOG,"In onDataChange"+dataSnapshot.getChildren().toString());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //            Log.d(LOG,"Key is "+ds.getKey());
                    //           Log.d(LOG,"Value is "+ds.getValue());
                    if (ds.getKey().matches("UPDATED_TIME")) {
                        messageLastSentTime = ds.getValue(String.class);
                        continue;
                    }
                    tempMap = (Map<String, String>) ds.getValue();
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setMessage(tempMap.get("message"));
                    chatMessage.setTime(tempMap.get("time"));
                    chatMessage.setUserId(tempMap.get("userId"));
                    chatMessage.setImageUrl(tempMap.get("imageUrl"));
                    chatMessages.add(chatMessage);
                    //         Log.d(LOG,"Adding message "+chatMessage.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        chatMessageAdapter.notifyDataSetChanged();
        listViewCAMessages.smoothScrollToPosition(chatMessages.size());
    }

    private Uri uploadFileToFirebase(final Uri selectedImageUri, String currentUserId) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StorageReference storageReference = firebaseStorage.getReference("uploadedPics/"+currentUserId+dateFormat.format(new Date())+ ".jpeg");
        final Uri[] url = {null};
        storageReference.putFile(selectedImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        url[0] = taskSnapshot.getDownloadUrl();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to upload the selected image. Try again later!", Toast.LENGTH_LONG).show();
            }
        });
        return url[0];
    }
    public void setData(boolean flag)
    {

    }

    @Override
    protected void onDestroy() {
        lastSeen();
        super.onDestroy();
    }
    public static void lastSeen()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users/"+auth.getCurrentUser().getUid());
        databaseReference.push();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        databaseReference.child("LAST_SEEN").setValue(dateFormat.format(new Date()));
    }
}