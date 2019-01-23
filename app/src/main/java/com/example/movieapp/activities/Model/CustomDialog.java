package com.example.movieapp.activities.Model;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.movieapp.R;

public class CustomDialog extends Dialog  implements View.OnClickListener{
  private TextView message;
  private String messageString;
  private Button okButton;
    public CustomDialog(@NonNull Context context,String message) {
        super(context);
        messageString = message;
     }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_layout);
        message= findViewById(R.id.message_custom_dialog);
        message.setText(messageString);
        okButton = findViewById(R.id.ok_button_custom_dialog);
        okButton.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok_button_custom_dialog:
                this.hide();
        }
    }
}
