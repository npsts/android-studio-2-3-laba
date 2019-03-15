package com.example.messenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.messenger.Entities.Message;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Activity mActivity;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    View.OnClickListener onClickListener;
    List<Message> sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(mActivity, NewSms.class);
                mActivity.startActivity(newIntent);
            }
        });

      onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(v);
                final Message item = sms.get(itemPosition);
                new AlertDialog.Builder(mActivity)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteSMS(mActivity,item.getBody(),item.getAddress());
                                setData();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        };
        recyclerView = findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        setData();
    }
    // specify an adapter (see also next example)

    @Override
    protected void onResume() {
        super.onResume();
       setData();
    }
    void setData(){
        recyclerView.setAdapter(new MyAdapter(getSms(),onClickListener));
    }

    public List<Message> getSms(){
           sms = new ArrayList<>();
            List<String> addressList = new ArrayList<>();
            Uri uriSMSURI = Uri.parse("content://sms");
            Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);
//            Log.d("TAG",DatabaseUtils.dumpCursorToString(cur));
            while (cur != null && cur.moveToNext()) {
                String address = cur.getString(cur.getColumnIndex("address"));
                if(!addressList.contains(address)) {
                    String date = cur.getString(cur.getColumnIndexOrThrow("date"));
                    if(System.currentTimeMillis() - Long.valueOf(date) <86400000) {
                        addressList.add(address);
                        String body = cur.getString(cur.getColumnIndexOrThrow("body"));
                        String type = cur.getString(cur.getColumnIndexOrThrow("type"));
                        sms.add(new Message(address, body, date, type));
                    }
                }
            }

            if (cur != null) {
                cur.close();
            }
            return sms;
    }
    public void deleteSMS(Context context, String message, String number) {
        try {
            Uri uriSms = Uri.parse("content://sms/inbox");
            Cursor c = context.getContentResolver().query(uriSms,
                    new String[] { "_id", "thread_id", "address",
                            "person", "date", "body" }, null, null, null);

            if (c != null && c.moveToFirst()) {
                do {
                    long id = c.getLong(0);
                    long threadId = c.getLong(1);
                    String address = c.getString(2);
                    String body = c.getString(5);
                    if (message.equals(body) && address.equals(number)) {
                        context.getContentResolver().delete(
                                Uri.parse("content://sms/" + id), null, null);
                    }
                } while (c.moveToNext());

            }
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
        }

    }


}

