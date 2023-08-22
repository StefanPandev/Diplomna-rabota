package com.example.highspots;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.highspots.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    /* Views */
    private EditText emailET;
    private EditText passwordET;
    private EditText repeatPasswordET;
    private EditText nickNameET;
    private Button registerBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.getSupportActionBar().hide();

        initViews();
    }

    /**
     * This method is responsible for initializing the views in this activity.
     */
    private void initViews() {
        this.emailET = findViewById(R.id.RegistrationPageEmailET);
        this.passwordET = findViewById(R.id.RegistrationPagePasswordET);
        this.repeatPasswordET = findViewById(R.id.RegistrationPageRepPassET);
        this.nickNameET = findViewById(R.id.RegistrationPageNicknameET);
        this.registerBTN = findViewById(R.id.RegistrationPageRegisterBTN);

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
                hideSoftKeyboard(RegistrationActivity.this);
            }
        });
    }

    /**
     * This method is responsible for determining whether the user inputted data is correct.
     * @return - whether the user inputted data is correct
     */
    private boolean isDataValid() {
        boolean output = true;

        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String repPassword = repeatPasswordET.getText().toString().trim();
        String nickName = nickNameET.getText().toString().trim();

        if (email.isEmpty()) {
            emailET.setError("Input your email!");
            output = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("Input correct email!");
            output = false;
        }

        if (password.isEmpty()) {
            passwordET.setError("Input your password!");
            output = false;
        } else if (password.length() < 6) {
            passwordET.setError("Password should be at least 6 characters long!");
            output = false;
        }

        if (repPassword.isEmpty()) {
            repeatPasswordET.setError("Repeat your password!");
            output = false;
        } else if (!repPassword.equals(password)) {
            repeatPasswordET.setError("The passwords should match!");
            output = false;
        }

        if (nickName.isEmpty()) {
            nickNameET.setError("Input your nickname!");
            output = false;
        } else if (nickName.length() < 4) {
            nickNameET.setError("Nickname should be at least 4 characters long!");
            output = false;
        }

        return output;
    }

    /**
     * Tries to register the user via the FirebaseAuth and saves their data in the db.
     */
    private void register() {
        if (!isDataValid()) {
            return;
        }

        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String nickName = nickNameET.getText().toString().trim();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                FirebaseUser fUser = auth.getCurrentUser();
                User user = new User(fUser.getUid(), nickName, email);
                FirebaseDatabase.getInstance("https://diplomna-rabota-7977a-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users").child(fUser.getUid()).setValue(user);

                startActivity(new Intent(RegistrationActivity.this, HomePageActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(RegistrationActivity.this, "This is already a user with this email!", Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseNetworkException) {
                    Toast.makeText(RegistrationActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(RegistrationActivity.this, "Invalid email format!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegistrationActivity.this, "Registration was not successful! Please try again later!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}