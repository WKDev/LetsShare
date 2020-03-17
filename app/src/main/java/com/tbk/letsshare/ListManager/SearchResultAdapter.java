package com.tbk.letsshare.ListManager;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tbk.letsshare.R;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SRViewHolder> {

    private ArrayList<SearchResultContainer> mList;

    public class SRViewHolder extends RecyclerView.ViewHolder {
        protected ImageView thumbnail;
        protected TextView name;
        protected TextView price;
        protected TextView date;


        public SRViewHolder(View view) {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(R.id.itemlist_thumbnail);
            this.name = (TextView) view.findViewById(R.id.itemlist_name);
            this.price = (TextView) view.findViewById(R.id.itemlist_price);
            this.date = (TextView) view.findViewById(R.id.itemlist_date);
        }
    }

    public SearchResultAdapter(ArrayList<SearchResultContainer> list) {
        this.mList = list;
    }

    @Override
    public SRViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);

        SRViewHolder viewHolder = new SRViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SRViewHolder viewholder, int position) {

        SearchResultContainer data = mList.get(position);

        viewholder.thumbnail.setImageResource(data.getThumbnail());

//        viewholder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//        viewholder.price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//        viewholder.date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);

        viewholder.name.setText(mList.get(position).getName());
        viewholder.price.setText(mList.get(position).getPrice());
        viewholder.date.setText(mList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}



