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
//import java.util.Dictionary;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.CustomViewHolder> {

    private ArrayList<ItemListContainer> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView thumbnail;
        protected TextView name;
        protected TextView price;
        protected TextView date;


        public CustomViewHolder(View view) {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(R.id.chatlist_thumbnail);
            this.name = (TextView) view.findViewById(R.id.chatlist_name);
            this.price = (TextView) view.findViewById(R.id.chatlist_contents);
            this.date = (TextView) view.findViewById(R.id.chatlist_date);
        }
    }

    public ChatListAdapter(ArrayList<ItemListContainer> list) {
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
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        ItemListContainer data = mList.get(position);

        viewholder.thumbnail.setImageResource(data.getThumbnail());

        viewholder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
//
//        viewholder.name.setGravity(Gravity.CENTER);
//        viewholder.price.setGravity(Gravity.CENTER);
//        viewholder.date.setGravity(Gravity.CENTER);
//
//
        viewholder.name.setText(mList.get(position).getName());
        viewholder.price.setText(mList.get(position).getPrice());
        viewholder.date.setText(mList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }



}