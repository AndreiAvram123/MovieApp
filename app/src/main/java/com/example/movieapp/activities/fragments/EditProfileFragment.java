package com.example.movieapp.activities.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Utilities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditProfileFragment extends Fragment {

 private EditProfileFragmentInterface editProfileFragmentInterface;
 private TextView email;
 private TextView nickname;
 private ImageView backImage;
 private Button changeEmailButton;
 private Button changePasswordButton;
 private Button changeNicknameButton;


    public static EditProfileFragment newInstance(){
         return new EditProfileFragment();
     }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_edit_profile_fragment,container,false);
        editProfileFragmentInterface = (EditProfileFragmentInterface) getActivity();
        initializeUI(layout);



        return layout;
    }
    private void initializeUI(View layout){
         email = layout.findViewById(R.id.email_edit_profile);
         nickname = layout.findViewById(R.id.nickname_edit_profile);
         backImage = layout.findViewById(R.id.back_image_edit_profile);
         changeEmailButton = layout.findViewById(R.id.change_email_button);
         changePasswordButton = layout.findViewById(R.id.change_password_button);
         changeNicknameButton = layout.findViewById(R.id.change_nickname_button);

         displayUserDetails();

        attachListenersToViews();
    }

    private void attachListenersToViews() {
        backImage.setOnClickListener(image-> getActivity().finish());
        changeEmailButton.setOnClickListener(button ->editProfileFragmentInterface.changeEmail());
        changePasswordButton.setOnClickListener(button ->editProfileFragmentInterface.changePassword());
        changeNicknameButton.setOnClickListener(button -> editProfileFragmentInterface.changeNickname());
    }

    private void displayUserDetails() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        email.setText(currentUser.getEmail());
        if(currentUser.getDisplayName() != null && !currentUser.getDisplayName().isEmpty())
            nickname.setText(currentUser.getDisplayName());
    }

    public interface EditProfileFragmentInterface{
        void changeEmail();
        void changePassword();
        void changeNickname();
        void changeProfilePicture();
    }


}
