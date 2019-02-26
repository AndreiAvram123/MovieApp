package com.example.movieapp.activities.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
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

/**
 * This fragment is used in order to change
 * either the email , or password , or nickname
 */

public class ChangeProfileInfoFragment extends Fragment {

    private EditText field1;
    private EditText field2;
    private TextView hint1;
    private TextView hint2;
    private TextView error_message;
    private TextView title;
    private ProgressBar loadingBar;
    private Button finishButton;
    private UpdateCredentialFragmentInterface updateCredentialFragmentInterface;
    private static final String KEY_PROFILE_INFO = "KEY_PROFILE_INFO";
    public static final String KEY_EMAIL = "KEY_EMAIL";
    public static final String KEY_PASSWORD = "KEY_PASSWORD";
    public static final String KEY_NICKNAME = "KEY_NICKNAME";


    public static ChangeProfileInfoFragment newInstance(String credential) {

        ChangeProfileInfoFragment changeProfileInfoFragment = new ChangeProfileInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putString(KEY_PROFILE_INFO, credential);
        changeProfileInfoFragment.setArguments(bundle);
        return changeProfileInfoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_update_info, container, false);

        updateCredentialFragmentInterface = (UpdateCredentialFragmentInterface) getActivity();

        initializeUI(layout);


        String profileInfo = getArguments().getString(KEY_PROFILE_INFO);


        switch (profileInfo) {

            case KEY_EMAIL:
                title.setText(getString(R.string.change_email));
                hint1.setText(getString(R.string.new_email));
                attachListenerForEmail();
                break;

            case KEY_NICKNAME:
                title.setText(getString(R.string.change_nickname));
                hint1.setText(getString(R.string.new_nickname));
                attachListenerForNickname();
                break;

            case KEY_PASSWORD:
                title.setText(getString(R.string.change_password));
                hint1.setText(getString(R.string.new_password));
                hint2.setText(getString(R.string.confirm_new_password));

                configureSecondField();
                attachListenerForPassword();
                break;
        }


        return layout;
    }

    private void configureSecondField() {
        customiseField(field2, hint2);
        field2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        field2.setVisibility(View.VISIBLE);
        hint2.setVisibility(View.VISIBLE);
    }

    private void attachListenerForEmail() {
        finishButton.setOnClickListener(button -> {
            String email = field2.getText().toString().trim();
            if (!email.isEmpty() && Utilities.isEmailValid(email)) {
                toggleLoadingBar();
                updateCredentialFragmentInterface.updateEmail(email);
            } else {
                displayErrorMessage(getString(R.string.error_invalid_email));
            }
        });
    }

    private void attachListenerForNickname() {
        finishButton.setOnClickListener(button -> {
            String nickname = field1.getText().toString().trim();
            if (!nickname.isEmpty()) {
                toggleLoadingBar();
                updateCredentialFragmentInterface.updateNickname(nickname);
            } else
                displayErrorMessage(getString(R.string.error_invalid_nickname));
        });
    }

    private void attachListenerForPassword() {
        finishButton.setOnClickListener(button -> {
            String password = field1.getText().toString().trim();
            String secondPassword = field2.getText().toString().trim();
            if (password.isEmpty() || secondPassword.isEmpty())
                displayErrorMessage(getString(R.string.empty_password));
            else if (password.equals(secondPassword)) {
                toggleLoadingBar();
                updateCredentialFragmentInterface.updatePassword(password);
            } else
                displayErrorMessage(getString(R.string.password_match));
        });
    }

    private void initializeUI(View layout) {

        ImageView closeButton = layout.findViewById(R.id.cancel_button_update_credential);
        closeButton.setOnClickListener(button -> getFragmentManager().popBackStack());

        finishButton = layout.findViewById(R.id.finish_button_update_detail);
        loadingBar = layout.findViewById(R.id.progress_bar_change_detail);
        title = layout.findViewById(R.id.new_credential_title);
        hint1 = layout.findViewById(R.id.new_credential_hint1);
        hint2 = layout.findViewById(R.id.new_credential_hint2);
        field1 = layout.findViewById(R.id.new_credential_field1);
        field2 = layout.findViewById(R.id.new_credential_field2);
        error_message = layout.findViewById(R.id.error_message_update_credentials);
        customiseField(field1, hint1);
    }

    public void toggleLoadingBar() {

        if (loadingBar.getVisibility() == View.INVISIBLE) {
            loadingBar.setVisibility(View.VISIBLE);
            finishButton.setVisibility(View.INVISIBLE);
        } else {
            loadingBar.setVisibility(View.INVISIBLE);
            finishButton.setVisibility(View.VISIBLE);
        }
        clearFields();
    }

    private void clearFields() {
        field1.setText("");
        field2.setText("");
    }


    private void displayErrorMessage(String message) {
        error_message.setVisibility(View.VISIBLE);
        error_message.setText(message);
    }


    private void customiseField(EditText field, TextView hint) {
        field.setVisibility(View.VISIBLE);
        hint.setVisibility(View.VISIBLE);
        field1.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                field.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#76B900"), PorterDuff.Mode.SRC_ATOP);
                hint.setTextColor(Color.parseColor("#A5B253"));
            } else {
                field.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                hint.setTextColor(Color.parseColor("#ffffff"));
            }
        });
    }


    public interface UpdateCredentialFragmentInterface {
        void updateEmail(String newEmail);

        void updatePassword(String newPassword);

        void updateNickname(String newNickname);

    }
}
