package com.example.highspots;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.highspots.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class LogInActivity extends AppCompatActivity {

    /* Views */
    private EditText emailET;
    private EditText passwordET;
    private Button logInBTN;
    private TextView goToRegisterBTN;
    private ProgressBar progressBar;
    private TextView forgotPasswordTV;

    /* Vars */
    private long lastClickTime = 0;

    /* Dialog */
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    /* Dialog Views */
    private EditText resetPasswordEmailET;
    private Button resetPasswordBTN;

    /* Database */
    private DatabaseReference usersDataReference = FirebaseDatabase.getInstance("https://diplomna-rabota-7977a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        this.getSupportActionBar().hide();

        autoLogIn();
    }

    /**
     * This method is responsible to check if a user is logged.
     * If a user is logged and they exist in the db then, the user is prompted to home page.
     * If no user is logged then the log in view are initialized and the log in page is prepared
     * to be used.
     */
    private void autoLogIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || !isNetworkAvailable()) { // In case of no logged user
            initViews();
            return;
        }

        // Checks if such user exists in the db
        usersDataReference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    initViews();
                    return;
                }

                User user = task.getResult().getValue(User.class);

                if (user != null) { // In case the user exits in the db
                    Toast.makeText(LogInActivity.this, "Login was successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LogInActivity.this, HomePageActivity.class));
                    finish();
                } else { // In case the user does not exits in the db
                    initViews();
                }
            }
        });

    }

    private boolean isNetworkAvailable() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = process.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method is responsible for initializing the views in this activity.
     */
    private void initViews() {
        this.emailET = findViewById(R.id.LogInPageEmailET);
        this.passwordET = findViewById(R.id.LogInPagePasswordET);
        this.logInBTN = findViewById(R.id.LogInPageLogInBTN);
        this.goToRegisterBTN = findViewById(R.id.LogInPageRegisterBTN);

        logInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                //Log.e(String.valueOf(LogInActivity.this), "LOGIN");
                logIn();
                hideSoftKeyboard(LogInActivity.this);
            }
        });

        goToRegisterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, RegistrationActivity.class));
            }
        });

        this.progressBar = findViewById(R.id.logInPagePB);
        progressBar.setVisibility(View.GONE);

        this.forgotPasswordTV = findViewById(R.id.logInPageForgotPasswordTV);
        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResetEmailDialog();
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

        return output;
    }

    /**
     * Tries to log the user in via the FirebaseAuth.
     */
    private void logIn() {
        if (!isDataValid()) {
            return;
        }

        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                usersDataReference.child(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        User user = task.getResult().getValue(User.class);

                        if (user == null) {
                            Toast.makeText(LogInActivity.this, "No user found for those credentials", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Toast.makeText(LogInActivity.this, "Log In was successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LogInActivity.this, HomePageActivity.class));
                        finish();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidUserException) {
                    Toast.makeText(LogInActivity.this, "There is no user with those credentials!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LogInActivity.this, "Log In was not successful! Please try again later!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Reset password via email logic is handled in this method.
     */
    private void openResetEmailDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.forgot_password_dialog, null);

        // Init dialog views
        this.resetPasswordEmailET = popupView.findViewById(R.id.forgotPassDialogEmailET);
        this.resetPasswordBTN = popupView.findViewById(R.id.forgotPassDialogBTN);

        resetPasswordBTN.setEnabled(false); // Button is initially disabled

        resetPasswordEmailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String email = resetPasswordEmailET.getText().toString().trim();

                if (!email.isEmpty()) {
                    resetPasswordBTN.setEnabled(true);
                } else {
                    resetPasswordBTN.setEnabled(false);
                }
            }
        });

        resetPasswordEmailET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPasswordEmailET.setCursorVisible(true);
            }
        });

        resetPasswordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = resetPasswordEmailET.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    resetPasswordEmailET.setError("You should input valid email!");
                    return;
                }

                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LogInActivity.this, "Check your email!", Toast.LENGTH_LONG).show();
                            Toast.makeText(LogInActivity.this, "Check your spam folder!", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(LogInActivity.this, "User with the specified email does not exist!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LogInActivity.this, "Something went wrong! Try again later!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                resetPasswordEmailET.setCursorVisible(false);
                hideSoftKeyboard(popupView);
            }
        });

        // show dialog
        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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