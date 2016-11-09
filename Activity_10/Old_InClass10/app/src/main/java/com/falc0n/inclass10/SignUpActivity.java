package com.falc0n.inclass10;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSUSignUp).setOnClickListener(this);
        findViewById(R.id.buttonSUCancel).setOnClickListener(this);
    }
    private void createAccount(String email, String password, String name) {
        Log.d(MainActivity.LOG_TAG, "createAccount:" + email);
        if (!validateForm(email,password,name)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(MainActivity.LOG_TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,"Login was not successful!",Toast.LENGTH_LONG).show();
                        }else {
                            MainActivity.currentUserId = mAuth.getCurrentUser().getUid();
                            Intent intent = new Intent(SignUpActivity.this,AllExpensesActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
    public  boolean validateForm(String email, String password, String name) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(SignUpActivity.this,"Email cannot be empty",Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(SignUpActivity.this,"Password cannot be empty",Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(SignUpActivity.this,"Name cannot be empty",Toast.LENGTH_LONG).show();
            valid = false;
        }
        return valid;
    }

    @Override
    public void onClick(View view) {
        Log.d("hh","Clicked");
        switch (view.getId())
        {
            case R.id.buttonSUSignUp:
                Log.d("hh","Sign up Clicked");
                String email = ((EditText)findViewById(R.id.editTextSUEmail)).getText().toString().trim();
                String name = ((EditText)findViewById(R.id.editTextSUFullName)).getText().toString().trim();
                String password = ((EditText)findViewById(R.id.editTextSUPassword)).getText().toString().trim();
                createAccount(email,password,name);
                break;
            case R.id.buttonSUCancel:
                finish();
                break;
        }
    }
}
