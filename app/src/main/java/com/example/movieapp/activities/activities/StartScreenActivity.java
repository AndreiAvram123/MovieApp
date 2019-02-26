package com.example.movieapp.activities.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.CustomDialog;
import com.example.movieapp.activities.fragments.AuthenticationFragment;
import com.example.movieapp.activities.fragments.LoginFragment;
import com.example.movieapp.activities.fragments.SignUpFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class StartScreenActivity extends AppCompatActivity implements CustomDialog.CustomDialogInterface,
        LoginFragment.LoginFragmentCallback, SignUpFragment.SignUpFragmentCallback {

    private FirebaseAuth firebaseAuth;
    private CustomDialog customDialog;
    private LoginFragment loginFragment;
    private SignUpFragment signUpFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen_activity);

        prepareFragments();
        displayLoginFragment();

        if (!isNetworkAvailable()) {
            displayMessageDialog(getString(R.string.no_internet_connection));
        }

    }

    /**
     * This method is used to initialize the LoginFragment
     * and the SignUpFragment along with the fragmentManager
     */
    private void prepareFragments() {
        loginFragment = LoginFragment.newInstance();
        signUpFragment = SignUpFragment.newInstance();
        fragmentManager = getSupportFragmentManager();
    }

    /**
     * This method is called in order to
     * show the login fragment
     */
    private void displayLoginFragment() {
        fragmentManager.beginTransaction().
                replace(R.id.start_screen_placeholder, loginFragment)
                .commit();

    }


    /**
     * This method is used to check weather or not there
     * is internet connection available in the given context
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }

    private void displayMessageDialog(String message) {
        customDialog = new CustomDialog(this, message, this);
        customDialog.setButton1Message("OK");
        customDialog.show();
    }

    private void displayVerificationEmailDialog() {
        customDialog = new CustomDialog(this, getString(R.string.email_verification_sent), this);
        customDialog.setButton1Message("ALRIGHT");
        customDialog.enableNegativeButton();
        customDialog.setButton2Message(getString(R.string.resend_verification_email));
        customDialog.show();
    }

    /**
     * This method pushes a login request with email and password to Firebase
     * IF we were able to log in the user,we check if his email address is verified
     * or not.If the email is not verified we call the method displayVerificationEmailDialog().If the
     * user has his email verified we call startMainActivity()
     * <p>
     * IF we were unable to log in the use we display a simple toast
     * telling him that the  login details are invalid
     *
     * @param email
     * @param password
     */
    private void pushLoginRequest(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                            startMainActivity();
                        } else {
                            displayVerificationEmailDialog();
                        }
                    } else {
                        loginFragment.displayErrorMessage(getString(R.string.error_invalid_login_details));
                    }
                    loginFragment.toggleLoadingBar();
                });
    }

    /**
     * This method pushed a signUp request to Firebase in order
     * to register a new user with email and password.Moreover
     * after we successfully added a new user to our database
     * we update his profile
     *
     * @param email
     * @param password
     * @param nickname
     */
    private void pushSignUpRequest(String email, String password, String nickname) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        displayMessageDialog(getString(R.string.account_created));
                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        updateNickname(nickname);
                    } else {
                        signUpFragment.displayErrorMessage(getString(R.string.error_create_account));
                    }
                    signUpFragment.toggleLoadingBar();
                });
    }

    private void updateNickname(String nickname) {
        firebaseAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                .setDisplayName(nickname)
                .build());
    }


    /**
     * The method start the MainActivity
     * IT  SETS THE FLAGS FOR THE INTENT IN ORDER TO DESTROY THIS
     * ACTIVITY AFTER STARTING THE INTENT
     */
    private void startMainActivity() {
        Intent startMainActivityIntent = new Intent(this, MainActivity.class);
        startMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startMainActivityIntent);
    }


    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null && currentUser.isEmailVerified()) {
            startMainActivity();
        }
    }


    @Override
    public void button1Pressed() {
        customDialog.hide();
    }

    @Override
    public void button2Pressed() {
        firebaseAuth.getCurrentUser().sendEmailVerification();
        customDialog.hide();
        Toast.makeText(this, "WE HAVE SENT YOU A NEW EMAIL.", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void login(String email, String password) {
        if (isNetworkAvailable()) {
            pushLoginRequest(email, password);
        } else {
            loginFragment.toggleLoadingBar();
            displayMessageDialog(getString(R.string.no_internet_connection));
        }
    }

    /**
     * This method is called in order to
     * show the signUpFragment
     */
    @Override
    public void showSignUpFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.start_screen_placeholder, signUpFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void signUp(String email, String password, String nickname) {
        if (isNetworkAvailable()) {
            pushSignUpRequest(email, password, nickname);
        } else {
            signUpFragment.toggleLoadingBar();
            displayMessageDialog(getString(R.string.no_internet_connection));
        }
    }
}
