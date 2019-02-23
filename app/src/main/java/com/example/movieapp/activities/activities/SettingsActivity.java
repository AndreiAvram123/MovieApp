package com.example.movieapp.activities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Constraints;
import com.example.movieapp.activities.fragments.SettingsFragment;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity
implements SettingsFragment.SettingsFragmentInterface{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        showSettingsFragment(getIntent().getStringExtra(Constraints.KEY_NICKNAME_SETTINGS));

        ImageView backButton = findViewById(R.id.back_button_settings);
        backButton.setOnClickListener(button -> finish());

    }

    private void showSettingsFragment(String nickname) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_settings,SettingsFragment.newInstance(nickname))
                .commit();
    }

    @Override
    public void signOut() {
      FirebaseAuth.getInstance().signOut();
      startLogInActivity();
      finish();
    }

    private void startLogInActivity() {
        Intent intent = new Intent(this, StartScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}
