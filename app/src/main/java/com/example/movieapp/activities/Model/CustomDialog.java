package com.example.movieapp.activities.Model;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.movieapp.R;

public class CustomDialog extends Dialog {
  private String message;
  private Button negativeButton;
  private CustomDialogInterface customDialogInterface;
  private boolean showNegativeButton;
  private String positiveButtonMessage = "OK";
  private String negativeButtonMessage ="CANCEL";


     public CustomDialog (@NonNull Context context,String message,
                          Activity activity,boolean showNegativeButton)
     {
        super(context);
        this.message = message;
        this.showNegativeButton = showNegativeButton;
        customDialogInterface = (CustomDialogInterface) activity;
     }

     public void setPositiveButtonMessage(String message){
       positiveButtonMessage = message;
     }
     public void setNegativeButtonMessage(String message){
       negativeButtonMessage = message;
     }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_layout);

        TextView message = findViewById(R.id.message_custom_dialog);

        message.setText(this.message);


        Button positiveButton = findViewById(R.id.positive_button_custom_dialog);

        positiveButton.setText(positiveButtonMessage);
        positiveButton.setOnClickListener(button-> customDialogInterface.positiveButtonPressed());

        if(showNegativeButton) {
            negativeButton = findViewById(R.id.negative_button_custom_dialog);
            negativeButton.setVisibility(View.VISIBLE);
            negativeButton.setText(negativeButtonMessage);
            negativeButton.setOnClickListener(button -> {
                customDialogInterface.negativeButtonPressed();
            });
        }
    }

    /**
     * USE THIS INTERFACE IN ORDER TO SET ACTIONS FOR
     * THE POSITIVE AND NEGATIVE BUTTONS
     */
    public interface CustomDialogInterface{
       void positiveButtonPressed();
       void negativeButtonPressed();
    }


}
