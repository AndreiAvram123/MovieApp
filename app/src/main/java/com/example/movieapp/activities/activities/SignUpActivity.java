package com.example.movieapp.activities.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Useful;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText passwordField;
    private EditText reenteredPasswordField;
    private TextView errorMessage;
    private  Button nextButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeViews();


    }

    private void initializeViews() {
        emailField = findViewById(R.id.email_field_sign_up);
        passwordField = findViewById(R.id.password_field_sign_up);
        reenteredPasswordField = findViewById(R.id.reenter_password_field_sign_up);
        errorMessage = findViewById(R.id.error_message_sign_up);
        progressBar = findViewById(R.id.loading_progress_bar_sign_up);

        emailField.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                emailField.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
            }else {
                emailField.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#0063E0"),PorterDuff.Mode.SRC_ATOP);
            }
        });
        passwordField.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                passwordField.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
            }else {
                passwordField.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#0063E0"),PorterDuff.Mode.SRC_ATOP);
            }
        });
        reenteredPasswordField.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                reenteredPasswordField.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
            }else {
                reenteredPasswordField.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#0063E0"),PorterDuff.Mode.SRC_ATOP);
            }
        });


        nextButton = findViewById(R.id.next_button_sign_up);
        nextButton.setOnClickListener(view -> signUserUp());
    }

    private void signUserUp() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String reenteredPassword = reenteredPasswordField.getText().toString().trim();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(areCredentialsValid(email,password,reenteredPassword)){
            updateViews();
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()) {
                    startActivity(new Intent(this, EmailVerificationActivity.class));
                }else {
                    Toast.makeText(this, "Cannot create account", Toast.LENGTH_LONG).show();
                    clearFields();
                    updateViews();
                }

            });
            }
    }

    private void updateViews() {
        if(nextButton.getVisibility() == View.VISIBLE){
            nextButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            nextButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void clearFields() {
        emailField.setText("");
        passwordField.setText("");
        reenteredPasswordField.setText("");
    }

    private boolean areCredentialsValid(String email, String password, String reenteredPassword) {
        if(Useful.isEmailValid(email)){
            if(!password.isEmpty()) {
                if (password.equals(reenteredPassword)) {
                      return true;
                } else {
                    displayErrorMessage(getString(R.string.password_match));
                }
            }else {
                displayErrorMessage(getString(R.string.no_password));
            }
        }else {
            displayErrorMessage(getString(R.string.error_invalid_email));
        }
        return false;
    }

    private void displayErrorMessage(String message) {
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
        clearFields();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
