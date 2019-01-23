package com.example.movieapp.activities.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.CustomDialog;
import com.google.firebase.auth.FirebaseAuth;

public class EmailVerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        Button resendEmail = findViewById(R.id.resend_verification_email_button);
        resendEmail.setOnClickListener(view -> FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification());

        Button next = findViewById(R.id.next_button_email_verification);
        next.setOnClickListener(view -> {
            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                startActivity(new Intent(this,LoginActivity.class));
            }else {
                displayDialog();
            }
        });
    }

    private void displayDialog() {
        CustomDialog customDialog = new CustomDialog(this,"Please use the email we've sent to validate your account first");
        customDialog.show();
    }
}
