package com.example.movieapp.activities.fragments;

import android.os.Bundle;
import android.os.Parcelable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditProfileFragment extends Fragment {
private EditProfileFragmentInterface editProfileFragmentInterface;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_edit_profile_fragment,container,false);

        editProfileFragmentInterface = (EditProfileFragmentInterface) getActivity();


        TextView email = view.findViewById(R.id.email_edit_profile);
        TextView nickname = view.findViewById(R.id.nickname_edit_profile);
        ImageView backImage = view.findViewById(R.id.back_image_edit_profile);
        Button changeEmailButton = view.findViewById(R.id.change_email_button);
        Button changePasswordButton = view.findViewById(R.id.change_password_button);
        Button changeNicknameButton = view.findViewById(R.id.change_nickname_button);
        Button changeProfilePictureButton = view.findViewById(R.id.change_profile_picture_button);

        backImage.setOnClickListener(image-> getActivity().finish());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        email.setText(currentUser.getEmail());

        if(currentUser.getDisplayName() != null && !currentUser.getDisplayName().isEmpty())
            nickname.setText(currentUser.getDisplayName());

         changeEmailButton.setOnClickListener(button ->editProfileFragmentInterface.changeEmail());
         changePasswordButton.setOnClickListener(button ->editProfileFragmentInterface.changePassword());
         changeNicknameButton.setOnClickListener(button -> editProfileFragmentInterface.changeNickname());


        return view;
    }

    public interface EditProfileFragmentInterface{
        void changeEmail();
        void changePassword();
        void changeNickname();
        void changeProfilePicture();
    }


}
