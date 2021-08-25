package com.friday20.isyiar.helpers;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.friday20.isyiar.R;

public class SenderViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView msg, waktu;

    public SenderViewHolder(View itemView) {
        super(itemView);
        msg = itemView.findViewById(R.id.text_message_body);
        waktu = itemView.findViewById(R.id.text_message_time);
    }

}