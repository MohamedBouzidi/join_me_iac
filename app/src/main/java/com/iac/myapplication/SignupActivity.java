package com.iac.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {

    private static String TAG = "SIGNUP";

    private FirebaseAuth            mAuth;
    private FirebaseDatabase        mDatabase;
    private DatabaseReference       mReference;
    private ValueEventListener      mValueListener;

    private EditText emailField;
    private EditText passwordField;
    private EditText passwordConfirmationField;
    private EditText nameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameField                   = (EditText) findViewById(R.id.name_field);
        emailField                  = (EditText) findViewById(R.id.email_field);
        passwordField               = (EditText) findViewById(R.id.password_field);
        passwordConfirmationField   = (EditText) findViewById(R.id.password_confirmation_field);
        Button signupButton         = (Button)   findViewById(R.id.signup_button);

        mAuth       = FirebaseAuth.getInstance();
        mDatabase   = FirebaseDatabase.getInstance();
        mReference  = mDatabase.getReference("users");

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nameField.getText().toString().trim();
                final String email = emailField.getText().toString().trim();
                final String password = passwordField.getText().toString().trim();
                String passwordConfirmation = passwordConfirmationField.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(SignupActivity.this, "Name is required", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignupActivity.this, "Email is required", Toast.LENGTH_LONG).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Password must be a minimum of 6 characters long", Toast.LENGTH_LONG).show();
                    return;
                }

                if (password.equals(passwordConfirmation)) {
                    Toast.makeText(SignupActivity.this, "Password not confirmed", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(SignupActivity.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
