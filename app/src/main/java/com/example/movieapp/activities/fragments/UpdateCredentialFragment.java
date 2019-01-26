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
import com.example.movieapp.activities.Model.Constraints;
import com.example.movieapp.activities.Model.Useful;

/**
 * This fragment is used in order to change
 * either the email , or password , or nickname
 */

public class UpdateCredentialFragment extends Fragment {
 private EditText field1;
 private EditText field2;
 private TextView hint1;
 private TextView hint2;
 private TextView error_message;
    private UpdateCredentialFragmentInterface updateCredentialFragmentInterface;

    public static UpdateCredentialFragment newInstance(String credential){

        UpdateCredentialFragment updateCredentialFragment = new UpdateCredentialFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constraints.KEY_CREDENTIAL,credential);
        updateCredentialFragment.setArguments(bundle);
        return updateCredentialFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_update_credential,container,false);

        updateCredentialFragmentInterface = (UpdateCredentialFragmentInterface) getActivity();

        ImageView closeButton = view.findViewById(R.id.cancel_button_update_credential);
        closeButton.setOnClickListener(button -> updateCredentialFragmentInterface.closeFragment() );


        TextView title = view.findViewById(R.id.new_credential_title);
         hint1 = view.findViewById(R.id.new_credential_hint1);
         hint2 = view.findViewById(R.id.new_credential_hint2);
        field1 = view.findViewById(R.id.new_credential_field1);
        field2 = view.findViewById(R.id.new_credential_field2);
        error_message = view.findViewById(R.id.error_message_update_credentials);
        Button finish = view.findViewById(R.id.finish_button_update_credential);


        String credential = getArguments().getString(Constraints.KEY_CREDENTIAL);

        switch (credential){

            case Constraints.KEY_EMAIL_CREDENTIAL:
                title.setText(getString(R.string.change_email));
                hint2.setText(getString(R.string.new_email));
                  configureField2();
                  break;

            case Constraints.KEY_NICKNAME_CREDENTIAL:
                title.setText(getString(R.string.change_nickname));
                hint2.setText(getString(R.string.new_nickname));
                configureField2();
                break;

            case Constraints.KEY_PASSWORD_CREDENTIAL:
                title.setText(getString(R.string.change_password));
                hint1.setText(getString(R.string.new_password));
                hint2.setText(getString(R.string.confirm_new_password));
                configureField2();
                configureField1();
                break;
        }

        finish.setOnClickListener(button ->{
             //show progress bar and hide the button
            ProgressBar loadingIcon = view.findViewById(R.id.progressBar_update_credentials);
            loadingIcon.setVisibility(View.VISIBLE);
            finish.setVisibility(View.INVISIBLE);

            switch (credential){
                case Constraints.KEY_EMAIL_CREDENTIAL :
                    String email = field2.getText().toString().trim();
                     if(!email.isEmpty()&& Useful.isEmailValid(email))
                         updateCredentialFragmentInterface.updateEmail(email);
                     else {
                         displayErrorMessage(R.string.error_invalid_email);
                     }
                    break;

                case Constraints.KEY_NICKNAME_CREDENTIAL :
                    String nickname = field2.getText().toString().trim();
                    if(!nickname.isEmpty())
                        updateCredentialFragmentInterface.updateNickname(nickname);
                    else
                        displayErrorMessage(R.string.error_invalid_nickname);
                    break;

                case Constraints.KEY_PASSWORD_CREDENTIAL:
                     String password = field1.getText().toString().trim();
                     String secondPassword = field2.getText().toString().trim();

                     if(password.isEmpty() || secondPassword.isEmpty())
                        displayErrorMessage(R.string.empty_password);
                     else
                         if(password.equals(secondPassword))
                         updateCredentialFragmentInterface.updatePassword(password);
                         else
                             displayErrorMessage(R.string.password_match);
                         break;

            }
        });


        return  view;
    }

    private void displayErrorMessage(int  messageID) {
        error_message.setVisibility(View.VISIBLE);
        error_message.setText(getString(messageID));
    }


    private void configureField1() {
        field1.setVisibility(View.VISIBLE);
        hint1.setVisibility(View.VISIBLE);
        field1.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                field1.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#76B900"), PorterDuff.Mode.SRC_ATOP);
                hint1.setTextColor(Color.parseColor("#A5B253"));
            }else {
                field1.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                hint1.setTextColor(Color.parseColor("#ffffff"));
            }
        });
    }

    private void configureField2() {
        field2.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                field2.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#76B900"), PorterDuff.Mode.SRC_ATOP);
                hint2.setTextColor(Color.parseColor("#A5B253"));
            }else {
                field2.getBackground()
                        .mutate().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                hint2.setTextColor(Color.parseColor("#ffffff"));
            }
        });
    }

    public interface UpdateCredentialFragmentInterface {
        void updateEmail(String newEmail);
        void updatePassword(String newPassword);
        void updateNickname(String newNickname);
        void closeFragment();

    }
}
