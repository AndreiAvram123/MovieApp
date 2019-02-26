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
    private String dialogMessage;
    private Button button2;
    private Button button1;
    private CustomDialogInterface customDialogInterface;
    private boolean enableSecondButton;
    private String button1Message;
    private String button2Message;


    public CustomDialog(@NonNull Context context, String dialogMessage,
                        Activity activity) {
        super(context);
        this.dialogMessage = dialogMessage;
        customDialogInterface = (CustomDialogInterface) activity;
    }

    public void setButton1Message(String message) {
        button1Message = message;
    }

    public void setButton2Message(String message) {
        button2Message = message;
    }

    public void enableNegativeButton() {
        enableSecondButton = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_layout);
        setCanceledOnTouchOutside(false);

        TextView message = findViewById(R.id.message_custom_dialog);

        message.setText(this.dialogMessage);


        button1 = findViewById(R.id.positive_button_custom_dialog);

        button1.setText(button1Message);
        button1.setOnClickListener(button -> customDialogInterface.button1Pressed());

        if (enableSecondButton) {
            button2 = findViewById(R.id.negative_button_custom_dialog);
            button2.setVisibility(View.VISIBLE);
            button2.setText(button2Message);
            button2.setOnClickListener(button -> customDialogInterface.button2Pressed());
        }
    }

    /**
     * USE THIS INTERFACE IN ORDER TO SET ACTIONS FOR
     * THE BUTTONS
     */
    public interface CustomDialogInterface {
        void button1Pressed();

        void button2Pressed();
    }


}
