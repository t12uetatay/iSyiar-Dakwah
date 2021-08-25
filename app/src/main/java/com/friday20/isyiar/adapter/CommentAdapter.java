package com.friday20.isyiar.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.friday20.isyiar.R;
import com.friday20.isyiar.helpers.ReciverViewHolder;
import com.friday20.isyiar.helpers.SenderViewHolder;
import com.friday20.isyiar.model.Comment;
import com.friday20.isyiar.model.User;
import com.friday20.isyiar.preference.DataUser;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int RECIVER = 1;
    private static final int SENDER = 2;
    private List<Comment> commentList;
    Context context;
    User user;

    public CommentAdapter(Context context) {
        this.context = context;
        user= DataUser.getInstance(context).getUser();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case RECIVER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_reciver, parent, false);
                return new ReciverViewHolder(view);
            case SENDER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_sender, parent, false);
                return new SenderViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_reciver, parent, false);
                return new ReciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == RECIVER) {
            ((ReciverViewHolder) holder).nama.setText(commentList.get(position).getUsername());
            ((ReciverViewHolder) holder).msg.setText(commentList.get(position).getMsg());
            ((ReciverViewHolder) holder).waktu.setText(commentList.get(position).getIdcom());
        } else {
            SenderViewHolder sender = (SenderViewHolder) holder;
            sender.msg.setText(commentList.get(position).getMsg());
            sender.waktu.setText(commentList.get(position).getIdcom());
        }
    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (commentList.get(position).getUsername().equals(user.getUsername())) {
            return SENDER;
        } else {
            return RECIVER;
        }
    }

    public void setDataList(List<Comment> comments) {
        this.commentList = comments;
        notifyDataSetChanged();
    }
}