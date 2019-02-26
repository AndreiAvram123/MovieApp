package com.example.movieapp.activities.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.CustomDialog;
import com.example.movieapp.activities.Model.Utilities;
import com.example.movieapp.activities.fragments.EditProfileFragment;
import com.example.movieapp.activities.fragments.ChangeProfileInfoFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditProfileActivity extends AppCompatActivity implements
        ChangeProfileInfoFragment.UpdateCredentialFragmentInterface, CustomDialog.CustomDialogInterface {

    private FragmentManager fragmentManager;
    private FirebaseUser firebaseUser;
    private CustomDialog customDialog;
    private EditProfileFragment editProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if (Utilities.isNetworkAvailable(this)) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            initializeUI();
            makeActivityFullscreen();
        } else {
            initializeViewsForOfflineMode();

        }

    }

    private void initializeViewsForOfflineMode() {
        TextView noInternetMessage = findViewById(R.id.error_message_edit_profile);
        noInternetMessage.setVisibility(View.VISIBLE);
        ImageView backImage = findViewById(R.id.back_image_edit_profile);
        backImage.setOnClickListener(image -> finish());
    }


    private void makeActivityFullscreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void showErrorDialog() {
        customDialog = new CustomDialog(this,
                getString(R.string.operation_unsuccessful), this);
        customDialog.setButton1Message("OK");
        customDialog.show();
    }


    public void initializeUI() {
        fragmentManager = getSupportFragmentManager();
        editProfileFragment = EditProfileFragment.newInstance();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_placeholder_edit_profile, editProfileFragment)
                .commit();


    }


    /**
     * Display a message dialog informing
     * the user that the operation
     * has been successful
     */
    private void showMessageDialog() {
        customDialog = new CustomDialog(this,
                getString(R.string.operation_successful), this);
        customDialog.setButton1Message("OK");
        customDialog.show();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     *                        INTERFACE METHODS FROM FRAGMENTS
     */


    /**
     * THE INTERFACE METHOD IS CALLED FROM THE
     * UpdateCredentialsFragment
     * <p>
     * Update the email in the Firebase database
     * IF THE TASK IS successful display a message to the
     * user informing him that everything has gone well
     * OTHERWISE display and error dialog
     *
     * @param newEmail
     */
    @Override
    public void updateEmail(String newEmail) {
        firebaseUser.updateEmail(newEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showMessageDialog();
            } else {
                showErrorDialog();
            }
            editProfileFragment.toggleProfileInfoBar();
        });
    }

    /**
     * THE INTERFACE METHOD IS CALLED FROM THE
     * UpdateCredentialsFragment
     * <p>
     * Update the password in the Firebase database
     * IF THE TASK IS successful display a message to the
     * user informing him that everything has gone well
     * OTHERWISE display and error dialog
     *
     * @param newPassword
     */
    @Override
    public void updatePassword(String newPassword) {
        firebaseUser.updatePassword(newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showMessageDialog();
            } else {
                showErrorDialog();
            }
            editProfileFragment.toggleProfileInfoBar();
        });
    }


    /**
     * THE INTERFACE METHOD IS CALLED FROM THE
     * UpdateCredentialsFragment
     * <p>
     * This method is used in order to change the nickname
     * of the current user in the Firebase database
     * We need to use a UserProfileChangeRequest object in
     * order to use the update .The object is created
     * using an inner class named Builder
     * <p>
     * IF THE TASK IS successful display a message to the
     * * user informing him that everything has gone well
     * * OTHERWISE display and error dialog
     *
     * @param newNickname
     */
    @Override
    public void updateNickname(String newNickname) {
        firebaseUser.updateProfile(new UserProfileChangeRequest.Builder()
                .setDisplayName(newNickname)
                .build()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showMessageDialog();
            } else {
                showErrorDialog();
            }
            editProfileFragment.toggleProfileInfoBar();
        });

    }


    @Override
    public void button1Pressed() {
        customDialog.hide();
    }

    @Override
    public void button2Pressed() {

    }
}
