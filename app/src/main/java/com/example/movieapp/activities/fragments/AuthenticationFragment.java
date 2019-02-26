package com.example.movieapp.activities.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This is a base class used to build fragments
 * such as a LOGIN fragment or a Sign up Fragment
 */
public abstract class AuthenticationFragment extends Fragment {


    abstract void attemptAction();

    abstract void clearFields();

    abstract void initializeUI(View layout);

    /**
     * This method should call the customiseField()
     * for the every EditText with its corresponding
     * TextView
     */
    abstract void customiseFields();

    abstract void configureButtons();


    public abstract void displayErrorMessage(String message);

    public abstract void toggleLoadingBar();

    /**
     * This method is used to change the color of both the editText
     * and the hint once the user touches on that editText
     * If the editText has focus change the color of the editText and
     * the hint to green
     * If the editText does not have focus change the color of the editText and
     * the hint to white
     */
    protected void customiseField(EditText editText, TextView hint) {
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


}
