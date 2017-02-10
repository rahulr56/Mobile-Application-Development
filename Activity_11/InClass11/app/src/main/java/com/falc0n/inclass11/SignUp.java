package com.falc0n.inclass11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.falc0n.inclass11.LoginActivity.LOG;

public class SignUp extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    String fullName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.d(LOG, "Auth is" + auth.toString());
        findViewById(R.id.buttonSUCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.buttonSUSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText) findViewById(R.id.editTextSUFirstName)).getText().toString();
                String lastName = ((EditText) findViewById(R.id.editTextSUFirstName)).getText().toString();
                String email = ((EditText) findViewById(R.id.editTextSUEmail)).getText().toString();
                String password = ((EditText) findViewById(R.id.editTextSUPassword)).getText().toString();
                String repPassword = ((EditText) findViewById(R.id.editTextREPSUPassword)).getText().toString();

                if (!password.matches(repPassword) || name.matches("") || lastName.matches("") || email.matches("")) {
                    Toast.makeText(SignUp.this, "Verify the enterd fields and try again!", Toast.LENGTH_LONG).show();
                    return;
                }
                fullName = name + lastName;
                registerUser(email, password);
            }
        });


    }

    private void registerUser(String email, String password) {
        Log.d(LOG, "In registerUser");
        Task task1 = auth.createUserWithEmailAndPassword(email, password);
        Log.d(LOG, "Task 1 Result of complete : " + task1.isComplete());
        Log.d(LOG, "Task 1 Success of auth : " + task1.isSuccessful());
        Log.d(LOG, "Task 1 Result of excp : " + task1.getException());
        Log.d(LOG, "Task 1 AUth is : " + auth.toString());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        task1 = auth.signInWithEmailAndPassword(email, password);
        Toast.makeText(SignUp.this, "Registered as a new user!", Toast.LENGTH_LONG).show();

        Log.d(LOG, "Current user UID : " + auth.getCurrentUser().getUid());
        databaseReference.push();
        databaseReference.setValue(auth.getCurrentUser().getUid(), fullName);
        Intent intent = new Intent(SignUp.this, ChatActivity.class);
        startActivity(intent);
    }

}
