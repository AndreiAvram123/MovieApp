package com.example.movieapp.activities.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Utilities;

public class LoginFragment extends Fragment {

    private TextView emailHint;
    private TextView passwordHint;
    private TextView errorMessage;
    private EditText emailField;
    private EditText passwordField;
    private ProgressBar loadingBar;
    private Button signInButton;
    private Button signUpButton;
    private LoginFragmentCallBack loginFragmentCallBack;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_login, container, false);
        loginFragmentCallBack = (LoginFragmentCallBack) getActivity();
        initializeUI(layout);
        return layout;
    }


    private void initializeUI(View layout) {
        initializeViews(layout);
        customiseEditTexts();
        attachListeners();

    }

    private void initializeViews(View layout) {
        emailHint = layout.findViewById(R.id.email_hint_login);
        passwordHint = layout.findViewById(R.id.password_hint_login);
        emailField = layout.findViewById(R.id.email_edit_text_main);
        passwordField = layout.findViewById(R.id.password_edit_text_main);
        errorMessage = layout.findViewById(R.id.error_message_text_view);
        loadingBar = layout.findViewById(R.id.logging_progress_bar);
        signInButton = layout.findViewById(R.id.sign_in_button);
        signUpButton = layout.findViewById(R.id.sign_up_button);
    }

    /**
     * This method calls the customiseEditText()
     * for the every EditText with its corresponding
     * TextView
     */
    private void customiseEditTexts() {
        customiseEditText(emailField, emailHint);
        customiseEditText(passwordField, passwordHint);

    }

    private void attachListeners() {
        signUpButton.setOnClickListener(view -> loginFragmentCallBack.showSignUpFragment());
        signInButton.setOnClickListener(view -> attemptSignIn());
    }

    /**
     * This method gets the text from the emailField and the
     * passwordField and then calls the method areLoginDetailsValid()
     * if the method called return true then call loginFragmentCallback.loginUser()
     */
    private void attemptSignIn() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        if (areLoginDetailsValid(email, password)) {
            toggleLoadingBar();
            loginFragmentCallBack.loginUser(email, password);
        }
    }

    /**
     * Once the user has pressed the signInButton and
     * the credentials are valid it may take some time
     * until Firebase processes our login request(usually this does not happen)
     * We hide the signInButton and the signUpButton and show the loadingBar
     * until Firebase has processed the request
     * THIS METHOD IS ALSO CALLED FROM @activity StartScreenActivity
     */
    public void toggleLoadingBar() {

        if (signInButton.getVisibility() == View.VISIBLE) {
            signInButton.setVisibility(View.INVISIBLE);
            loadingBar.setVisibility(View.VISIBLE);
            signUpButton.setClickable(false);

        } else {
            signUpButton.setClickable(true);
            signInButton.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * This method is used to change the color of both the editText
     * and the hint once the user touches on that editText
     * If the editText has focus change the color of the editText and
     * the hint to green
     * If the editText does not have focus change the color of the editText and
     * the hint to white
     */
    private void customiseEditText(EditText editText, TextView hint) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editText.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#76B900"), PorterDuff.Mode.SRC_ATOP);
                hint.setTextColor(Color.parseColor("#A5B253"));
            } else {
                editText.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                hint.setTextColor(Color.parseColor("#ffffff"));
            }
        });
    }

    /**
     * This method is used to check if the login
     * details are valid or not.We need to check the following:
     * <p>
     * If the email is valid using the method from the Utilities class
     * (the email should have the following format [a-zA-Z0-9]+@[a-z]+\.[a-z]+)
     * <p>
     * If the password field is not empty and the length of the password is AT LEST 6
     * characters( Firebase does not allow password that have less that 6 characters)
     *
     * @return
     */
    private boolean areLoginDetailsValid(String email, String password) {
        if (!Utilities.isEmailValid(email)) {
            displayErrorMessage("Please enter a valid email");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            displayErrorMessage("Please enter a password");
            return false;
        }
        return true;

    }

    private void displayErrorMessage(String message) {
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }


    public interface LoginFragmentCallBack {
        void showSignUpFragment();

        void loginUser(String email, String password);
    }

}
