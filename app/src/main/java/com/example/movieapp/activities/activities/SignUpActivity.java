package com.example.movieapp.activities.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.CustomDialog;
import com.example.movieapp.activities.Model.Useful;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity
implements CustomDialog.CustomDialogInterface {
    private EditText emailField,passwordField,reenteredPasswordField,nicknameField;
    private TextView errorMessage;
    private  Button nextButton;
    private ProgressBar progressBar;
    private TextView hint1,hint2,hint3,hint4;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeUI();

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void initializeUI() {
        initializeViews();


        customiseEditText(emailField,hint1);
        customiseEditText(passwordField,hint2);
        customiseEditText(reenteredPasswordField,hint3);
        customiseEditText(nicknameField,hint4);



        nextButton = findViewById(R.id.next_button_sign_up);
        nextButton.setOnClickListener(view -> signUserUp());
    }

    private void initializeViews() {
        hint1 = findViewById(R.id.hint_enter_email);
        hint2 = findViewById(R.id.hint_enter_password);
        hint3 = findViewById(R.id.hint_reenter_password);
        hint4 = findViewById(R.id.hint_enter_nickname);

        emailField = findViewById(R.id.email_field_sign_up);
        passwordField = findViewById(R.id.password_field_sign_up);
        reenteredPasswordField = findViewById(R.id.reentered_password_field);
        nicknameField = findViewById(R.id.nickname_field);

        errorMessage = findViewById(R.id.error_message_sign_up);
        progressBar = findViewById(R.id.loading_progress_bar_sign_up);
        ImageView backButton = findViewById(R.id.back_button_sign_up);
        backButton.setOnClickListener(view -> finish());
    }

    private void customiseEditText(EditText editText ,TextView hint) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                editText.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#76B900"), PorterDuff.Mode.SRC_ATOP);
                hint.setTextColor(Color.parseColor("#A5B253"));
            }else {
                editText.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#ffffff"),PorterDuff.Mode.SRC_ATOP);
                hint.setTextColor(Color.parseColor("#ffffff"));
            }
        });
    }



    private void signUserUp() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String reenteredPassword = reenteredPasswordField.getText().toString().trim();
        String nickname = nicknameField.getText().toString().trim();



        if(areCredentialsValid(email,password,reenteredPassword,nickname)){
            updateViews();
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()) {
                    updateUserNickname(nickname);
                    informUserAboutVerificationEmail();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                }else {
                    Toast.makeText(this, "Cannot create account", Toast.LENGTH_LONG).show();
                    clearFields();
                    updateViews();
                }

            });
            }
    }

    private void updateUserNickname(String nickname) {
        firebaseAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                                                     .setDisplayName(nickname)
                                                     .build());
    }

    private void informUserAboutVerificationEmail() {
        CustomDialog customDialog = new CustomDialog(this, getString(R.string.email_verification_sent), this,false);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();

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

    private boolean areCredentialsValid(String email, String password, String reenteredPassword,
                                        String nickname) {
        if(Useful.isEmailValid(email)){
            if(!password.isEmpty()) {
                if (password.equals(reenteredPassword)) {
                     if(!nickname.isEmpty())
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


    @Override
    public void positiveButtonPressed() {
        finish();
    }

    @Override
    public void negativeButtonPressed() {

    }
}
