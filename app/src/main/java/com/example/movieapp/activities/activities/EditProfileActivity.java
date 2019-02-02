package com.example.movieapp.activities.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Constraints;
import com.example.movieapp.activities.Model.CustomDialog;
import com.example.movieapp.activities.Model.Useful;
import com.example.movieapp.activities.fragments.EditProfileFragment;
import com.example.movieapp.activities.fragments.UpdateCredentialFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditProfileActivity extends AppCompatActivity implements EditProfileFragment.EditProfileFragmentInterface,
        UpdateCredentialFragment.UpdateCredentialFragmentInterface , CustomDialog.CustomDialogInterface {

    private static final String KEY_UPDATE_CREDENTIAL_FRAGMENT = "KEY_UPDATE_CREDENTIAL_FRAGMENT";
    private EditProfileFragment editProfileFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FirebaseUser firebaseUser;
    private  CustomDialog customDialog;
    public static final String KEY_EDIT_PROFILE_FRAGMENT ="EDIT_PROFILE_FRAGMENT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

         firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

         Useful.makeActivityFullscreen(getWindow());

         initializeUI();


    }




    private void showErrorDialog() {
         customDialog = new CustomDialog(this,
                        getString(R.string.operation_unsuccessful),this,false);
         customDialog.setCanceledOnTouchOutside(false);
         customDialog.show();
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
        fragmentTransaction.addToBackStack(null);
         fragmentTransaction.commit();
    }

    private void goBackToViewPagerFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_edit_profile,editProfileFragment,KEY_EDIT_PROFILE_FRAGMENT);
        fragmentTransaction.commit();
    }

    /**
     * Display a message dialog informing
     * the user that the operation
     * has been successful
     */
    private void showMessageDialog() {
        customDialog = new CustomDialog(this,
                getString(R.string.operation_successful),this,false);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
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

    /**
     * THE INTERFACE METHOD IS CALLED FROM THE
     * UpdateCredentialsFragment
     *
     * Update the email in the Firebase database
     * IF THE TASK IS successful display a message to the
     * user informing him that everything has gone well
     * OTHERWISE display and error dialog
     * @param newEmail
     */
    @Override
    public void updateEmail(String newEmail) {
        firebaseUser.updateEmail(newEmail).addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                showMessageDialog();
                goBackToViewPagerFragment();
            }else {
                showErrorDialog();
            }
        });
    }

    /**
     * THE INTERFACE METHOD IS CALLED FROM THE
     * UpdateCredentialsFragment
     *
     * Update the password in the Firebase database
     * IF THE TASK IS successful display a message to the
     * user informing him that everything has gone well
     * OTHERWISE display and error dialog
     * @param newPassword
     */
    @Override
    public void updatePassword(String newPassword) {
        firebaseUser.updatePassword(newPassword).addOnCompleteListener(task->{
            if(task.isSuccessful()){
                showMessageDialog();
                goBackToViewPagerFragment();
            }else {
                showErrorDialog();
            }
        });
    }


    /**
     * THE INTERFACE METHOD IS CALLED FROM THE
     * UpdateCredentialsFragment
     *
     * This method is used in order to change the nickname
     * of the current user in the Firebase database
     * We need to use a UserProfileChangeRequest object in
     * order to use the update .The object is created
     * using an inner class named Builder
     *
     * IF THE TASK IS successful display a message to the
     *      * user informing him that everything has gone well
     *      * OTHERWISE display and error dialog
     * @param newNickname
     */
    @Override
    public void updateNickname(String newNickname) {
        firebaseUser.updateProfile(new UserProfileChangeRequest.Builder()
                .setDisplayName(newNickname)
                .build()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                showMessageDialog();
                goBackToViewPagerFragment();
            }else {
                showErrorDialog();
            }
        });

    }


    @Override
    public void positiveButtonPressed() {
        customDialog.hide();
    }

    @Override
    public void negativeButtonPressed() {

    }
}
