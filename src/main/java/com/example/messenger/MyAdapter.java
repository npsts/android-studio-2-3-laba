package com.example.messenger;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.messenger.Entities.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Message> mMessages;
    private View.OnClickListener mOnClickListener;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView number;
        TextView textSms;
        TextView dateSms;
        ImageView stateSms;


        MyViewHolder(View v) {
            super(v);
            number = v.findViewById(R.id.number);
            stateSms = v.findViewById(R.id.stateSms);
            textSms = v.findViewById(R.id.textSms);
            dateSms = v.findViewById(R.id.dateSms);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    MyAdapter(List<Message> myMessages, View.OnClickListener onClickListener) {
        mMessages = myMessages;
        mOnClickListener = onClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
//        view.setOnClickListener(mOnClickListener);
        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


            holder.number.setText(mMessages.get(position).getAddress());
            holder.dateSms.setText(getDate(mMessages.get(position).getDate()));
        if (!mMessages.get(position).getType().equals("1")) {
            holder.stateSms.setImageResource(R.mipmap.sent);
        } else {
            holder.stateSms.setImageResource(R.mipmap.inbox);
        }
            holder.textSms.setText(mMessages.get(position).getBody());
    }

    private static String getDate(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault());
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mMessages==null? 0: mMessages.size();
    }


}