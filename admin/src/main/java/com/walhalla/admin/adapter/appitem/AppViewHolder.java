package com.walhalla.admin.adapter.appitem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.admin.R;

public class AppViewHolder extends RecyclerView.ViewHolder {

    public ImageView installedIcon;

    public TextView titleTextView;
    public TextView rateTextView;
    public TextView countTextView;

    public AppViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.text_title);
        rateTextView = itemView.findViewById(R.id.text_rate);
        countTextView = itemView.findViewById(R.id.text_count);
        installedIcon = itemView.findViewById(R.id.icon);

    }
}
