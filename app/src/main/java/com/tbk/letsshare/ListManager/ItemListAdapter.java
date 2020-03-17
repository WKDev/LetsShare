package com.tbk.letsshare.ListManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tbk.letsshare.ItemDetailedActivity;
import com.tbk.letsshare.R;

import java.util.ArrayList;
//import java.util.Dictionary;


public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.CustomViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    private ArrayList<ItemListContainer> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView thumbnail;
        protected TextView name;
        protected TextView price;
        protected TextView date;


        public CustomViewHolder(View view) {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(R.id.itemlist_thumbnail);
            this.name = (TextView) view.findViewById(R.id.itemlist_name);
            this.price = (TextView) view.findViewById(R.id.itemlist_price);
            this.date = (TextView) view.findViewById(R.id.itemlist_date);
        }


    }

    public ItemListAdapter(ArrayList<ItemListContainer> list) {
        this.mList = list;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, final int position) {

        ItemListContainer data = mList.get(position);

        viewholder.thumbnail.setImageResource(data.getThumbnail());

        viewholder.name.setText(mList.get(position).getName());
        viewholder.price.setText(mList.get(position).getPrice());
        viewholder.date.setText(mList.get(position).getDate());

        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = position;

                if(pos!= RecyclerView.NO_POSITION){
                    if(mListener!=null){
                        mListener.onItemClick(v, pos);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}