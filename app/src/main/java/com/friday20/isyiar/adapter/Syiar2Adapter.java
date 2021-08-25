package com.friday20.isyiar.adapter;

import android.content.Context;
import android.content.Entity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.friday20.isyiar.R;
import com.friday20.isyiar.model.EntitySyiar;
import com.friday20.isyiar.model.Syiar;

import java.util.List;

public class Syiar2Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Syiar> list;
    private AdapterListener listener;


    public Syiar2Adapter(Context context, AdapterListener listener) {
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_syiar_2, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Syiar current=list.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.judul.setText(current.getJudul());
        itemViewHolder.deskripsi.setText(current.getDeskripsi());
        itemViewHolder.pemilik.setText("By:  "+current.getUsername());
        itemViewHolder.jml.setText(String.valueOf(current.getSuka()));

        itemViewHolder.ivmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(position), position, ((ItemViewHolder) holder).ivmenu);
            }
        });

        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onKlick(list.get(position), position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setDataList(List<Syiar> dataList) {
        this.list = dataList;
        notifyDataSetChanged();
    }

    public interface AdapterListener {
        void onClick(Syiar syiar, int position, ImageView imageView);
        void onKlick(Syiar syiar, int position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView judul, deskripsi, pemilik, jml;
        ImageView ivplay, ivmenu;
        public ItemViewHolder(View itemView) {
            super(itemView);
            judul= itemView.findViewById(R.id.judul);
            deskripsi= itemView.findViewById(R.id.deskripsi);
            pemilik= itemView.findViewById(R.id.user);
            ivplay=itemView.findViewById(R.id.ivplay);
            ivmenu=itemView.findViewById(R.id.ivmenu);
            jml = itemView.findViewById(R.id.textView);
        }
    }
}
