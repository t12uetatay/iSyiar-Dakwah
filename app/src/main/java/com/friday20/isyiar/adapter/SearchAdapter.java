package com.friday20.isyiar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.friday20.isyiar.R;
import com.friday20.isyiar.model.Syiar;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Syiar> list;
    private List<Syiar> listFiltered;
    private AdapterListener listener;


    public SearchAdapter(Context context, List<Syiar> mlist, AdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.list=mlist;
        this.listFiltered=mlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_syiar, parent, false);

        return new MyViewHolder(itemView);
    }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            Syiar current=list.get(position);
            holder.judul.setText(current.getJudul());
            holder.deskripsi.setText(current.getDeskripsi());
            holder.pemilik.setText("By:  "+current.getUsername());
            holder.jml.setText(String.valueOf(current.getSuka()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onContactSelected(list.get(position), position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listFiltered.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        listFiltered = list;
                    } else {
                        List<Syiar> filteredList = new ArrayList<>();
                        for (Syiar row : list) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getJudul().toLowerCase().contains(charString.toLowerCase()) || row.getDeskripsi().toLowerCase().contains(charString.toLowerCase()) ||row.getUsername().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                        listFiltered = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = listFiltered;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    listFiltered = (ArrayList<Syiar>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

    public interface AdapterListener {
        void onContactSelected(Syiar syiar, int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView judul, deskripsi, pemilik, jml;

        public MyViewHolder(View itemView) {
            super(itemView);
            judul= itemView.findViewById(R.id.judul);
            deskripsi= itemView.findViewById(R.id.deskripsi);
            pemilik= itemView.findViewById(R.id.user);
            jml=itemView.findViewById(R.id.textView);

        }
    }
}
