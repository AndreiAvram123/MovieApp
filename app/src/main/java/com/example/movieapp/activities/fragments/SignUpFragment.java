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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Utilities;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpFragment extends Fragment {

    private EditText emailField;
    private EditText passwordField;
    private EditText reenteredPasswordField;
    private EditText nicknameField;
    private TextView errorMessage;
    private Button finishButton;
    private ProgressBar progressBar;
    private TextView hintEmail;
    private TextView hintPassword;
    private TextView hintReenteredPassword;
    private TextView hintNickname;
    private SignUpFragmentCallback signUpFragmentCallback;

    public static SignUpFragment newInstance(){
        return new SignUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_sign_up,container,false);

        signUpFragmentCallback = (SignUpFragmentCallback) getActivity();

        initializeUI(layout);

        return layout;

    }

    private void initializeUI(View layout) {
        initializeViews(layout);
        customiseEditTexts();
        configureBackButton(layout);
        configureFinishButton();
    }

    private void configureFinishButton() {
        finishButton.setOnClickListener(view -> attemptSignUp());
    }

    private void attemptSignUp() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String reenteredPassword = reenteredPasswordField.getText().toString().trim();
        String nickname = nicknameField.getText().toString().trim();
        if(areCredentialsValid(email,password,reenteredPassword,nickname)){
            toggleLoadingBar();
            signUpFragmentCallback.signUp(email,password,nickname);
            clearFields();
        }
    }

    private void customiseEditTexts() {
        customiseEditText(emailField,hintEmail);
        customiseEditText(passwordField,hintPassword);
        customiseEditText(reenteredPasswordField, hintReenteredPassword);
        customiseEditText(nicknameField,hintNickname);
    }

    private void initializeViews(View layout) {
        hintEmail = layout.findViewById(R.id.hint_enter_email);
        hintPassword= layout.findViewById(R.id.hint_enter_password);
        hintReenteredPassword = layout.findViewById(R.id.hint_reenter_password);
        hintNickname = layout.findViewById(R.id.hint_enter_nickname);

        emailField = layout.findViewById(R.id.email_field_sign_up);
        passwordField = layout.findViewById(R.id.password_field_sign_up);
        reenteredPasswordField = layout.findViewById(R.id.reentered_password_field);
        nicknameField = layout.findViewById(R.id.nickname_field);

        finishButton = layout.findViewById(R.id.finish_sign_up);
        errorMessage = layout.findViewById(R.id.error_message_sign_up);
        progressBar = layout.findViewById(R.id.loading_progress_bar_sign_up);

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

    private void configureBackButton(View layout) {
        ImageView backButton = layout.findViewById(R.id.back_button_sign_up);
        backButton.setOnClickListener(view -> getFragmentManager().popBackStack());
    }

    private boolean areCredentialsValid(String email, String password, String reenteredPassword,
                                        String nickname) {

        if(!Utilities.isEmailValid(email)){
            displayErrorMessage(getString(R.string.error_invalid_email));
            return false;
        }
        if(password.isEmpty()){
            displayErrorMessage(getString(R.string.no_password));
            return false;
        }
        if(!password.equals(reenteredPassword)){
            displayErrorMessage(getString(R.string.password_match));
            return false;
        }
        if(nickname.isEmpty()){
            displayErrorMessage(getString(R.string.error_no_nickname));
        }
        return true;

    }

    private void displayErrorMessage(String message) {
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
        clearFields();
    }
    private void clearFields() {
        emailField.setText("");
        passwordField.setText("");
        reenteredPasswordField.setText("");
    }

    private void toggleLoadingBar() {
        if(finishButton.getVisibility() == View.VISIBLE){
            finishButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            finishButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    public interface SignUpFragmentCallback{
        void signUp(String email,String password,String nickname);
    }
}
