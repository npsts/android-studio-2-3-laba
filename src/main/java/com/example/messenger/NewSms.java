package com.example.messenger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;

import com.example.messenger.Entities.Message;

public class NewSms extends AppCompatActivity {
    int PERMISSIONS_REQUEST_RECEIVE_SMS = 130;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sms);

        final EditText phone = findViewById(R.id.phone_receiver);
        final EditText textSms = findViewById(R.id.textSmsSent);

        FloatingActionButton fab = findViewById(R.id.fab_sent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phone.getText().length()<7){
                    phone.setError("Введите номер!");
                    phone.requestFocus();
                }
                if(textSms.getText().length()==0){
                    textSms.setError("Введите сообщение!");
                    textSms.requestFocus();
                }
                if (phone.getError()==null&&textSms.getError()==null){
                    if( checkPermissionMethod()) {
                        Message message = new Message(phone.getText().toString(), textSms.getText().toString(), String.valueOf(System.currentTimeMillis()), "SENT");
                        message.save();
                        sendSMS(phone.getText().toString(), textSms.getText().toString());
                    }
                }
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.setError(null);
            }
        });
        textSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSms.setError(null);
            }
        });
    }
    private void sendSMS(String phoneNumber, String message) {

        SmsManager sms = SmsManager.getDefault();

        sms.sendTextMessage(phoneNumber, null, message, null, null);
        finish();
    }

    boolean checkPermissionMethod(){
        int hasSendSMSPermission = 0;

            hasSendSMSPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if (hasSendSMSPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                        PERMISSIONS_REQUEST_RECEIVE_SMS);
            } else {
                return true;
            }
            return false;
    }

}
