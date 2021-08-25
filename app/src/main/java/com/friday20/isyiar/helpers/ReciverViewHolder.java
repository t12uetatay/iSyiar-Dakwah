package com.friday20.isyiar.helpers;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.friday20.isyiar.R;

public class ReciverViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView nama, msg, waktu;

    public ReciverViewHolder(View itemView) {
        super(itemView);
        nama = itemView.findViewById(R.id.text_message_name);
        msg = itemView.findViewById(R.id.text_message_body);
        waktu = itemView.findViewById(R.id.text_message_time);
    }

}