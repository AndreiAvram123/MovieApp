package com.example.movieapp.activities.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.CustomDialog;
import com.example.movieapp.activities.Model.Useful;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth firebaseAuth;
    private TextView errorMessage;
    private ProgressBar loggingProgressBar;
    private Button signInButton;
    private Button signUpButton;
    private TextView emailHint;
    private TextView passwordHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeView();

    }


    private void initializeView() {
        emailHint = findViewById(R.id.email_hint_login);
        passwordHint = findViewById(R.id.password_hint_login);
        emailEditText = findViewById(R.id.email_edit_text_main);
        passwordEditText = findViewById(R.id.password_edit_text_main);
        errorMessage = findViewById(R.id.error_message_text_view);
        loggingProgressBar = findViewById(R.id.logging_progress_bar);

        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
           if(hasFocus){
               emailEditText.getBackground()
                       .mutate().setColorFilter(Color.parseColor("#76B900"), PorterDuff.Mode.SRC_ATOP);
               emailHint.setTextColor(Color.parseColor("#A5B253"));

           }else {
               emailEditText.getBackground()
                       .mutate().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
               emailHint.setTextColor(Color.parseColor("#ffffff"));
           }
        });

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                passwordEditText.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#76B900"), PorterDuff.Mode.SRC_ATOP);
                passwordHint.setTextColor(Color.parseColor("#A5B253"));
            }else {
                passwordEditText.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                passwordHint.setTextColor(Color.parseColor("#ffffff"));
            }
        });

        signInButton  = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(view -> signInUser());

        signUpButton= findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(view ->signUpUser());

    }

    private void signUpUser() {
          startActivity(new Intent(this,SignUpActivity.class));
    }

    private void signInUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(Useful.isEmailValid(email)){
            if(!password.isEmpty()) {
                updateViews();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,task -> {
                    updateViews();
                     if(task.isSuccessful()) {
                         if (!firebaseAuth.getCurrentUser().isEmailVerified()) {
                             alertUserAboutVerificationEmail();
                             firebaseAuth.signOut();
                         } else {
                              startMainActivity();
                         }
                     }else {
                         displayErrorMessage(getString(R.string.error_invalid_credentials));
                     }
                });
            }else {
                displayErrorMessage(getString(R.string.empty_password));
            }
        }else{
            displayErrorMessage(getString(R.string.error_invalid_email));
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(this,MainActivity.class));
    }

    private void displayErrorMessage(String message) {
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }

    private void alertUserAboutVerificationEmail() {
        CustomDialog customDialog = new CustomDialog(this,"Please use the instructions in the email we sent you to verify your account.");
        customDialog.show();
    }

    private void updateViews() {
        if(signInButton.getVisibility() == View.VISIBLE){
            signInButton.setVisibility(View.INVISIBLE);
            loggingProgressBar.setVisibility(View.VISIBLE);
            signUpButton.setClickable(false);

        }else {
            signUpButton.setClickable(true);
            signInButton.setVisibility(View.VISIBLE);
            loggingProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!=null){
              //here we have the case when the user
             //closed the app before we were able to sign him out
            if(!currentUser.isEmailVerified()){
                firebaseAuth.signOut();
            }else {
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //finish the activity when it is no longer visible
        finish();
    }
}
