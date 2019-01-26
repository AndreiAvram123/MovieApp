package com.example.movieapp.activities.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Constraints;
import com.example.movieapp.activities.fragments.EditProfileFragment;
import com.example.movieapp.activities.fragments.UpdateCredentialFragment;

public class EditProfileActivity extends AppCompatActivity implements EditProfileFragment.EditProfileFragmentInterface,
        UpdateCredentialFragment.UpdateCredentialFragmentInterface {

    private static final String KEY_UPDATE_CREDENTIAL_FRAGMENT = "KEY_UPDATE_CREDENTIAL_FRAGMENT";
    EditProfileFragment editProfileFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    public static final String KEY_EDIT_PROFILE_FRAGMENT ="EDIT_PROFILE_FRAGMENT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        makeActivityFullscreen();

        initializeUI();


    }

    private void makeActivityFullscreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public void initializeUI(){
        fragmentManager = getSupportFragmentManager();
        editProfileFragment = new EditProfileFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout_edit_profile,editProfileFragment,KEY_EDIT_PROFILE_FRAGMENT);
        fragmentTransaction.commit();

    }
    private void showUpdateCredentialFragment(String credential) {
         fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_edit_profile,UpdateCredentialFragment.newInstance(credential),
                KEY_UPDATE_CREDENTIAL_FRAGMENT);
         fragmentTransaction.commit();
    }

    private void goBackToViewPagerFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_edit_profile,editProfileFragment,KEY_EDIT_PROFILE_FRAGMENT);
        fragmentTransaction.commit();
    }

    /**
     * ---------------------------------------------------------------------------------------------
     *                        INTERFACE METHODS FROM FRAGMENTS
     */
    @Override
    public void closeFragment(){
        goBackToViewPagerFragment();
    }


    @Override
    public void changeEmail() {
         showUpdateCredentialFragment(Constraints.KEY_EMAIL_CREDENTIAL);
    }


    @Override
    public void changePassword() {
        showUpdateCredentialFragment(Constraints.KEY_PASSWORD_CREDENTIAL);
    }

    @Override
    public void changeNickname() {
       showUpdateCredentialFragment(Constraints.KEY_NICKNAME_CREDENTIAL);
    }

    @Override
    public void changeProfilePicture() {

    }

    @Override
    public void updateEmail(String newEmail) {

    }

    @Override
    public void updatePassword(String newPassword) {

    }

    @Override
    public void updateNickname(String newNickname) {

    }
}
