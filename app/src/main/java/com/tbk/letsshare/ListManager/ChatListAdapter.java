package com.tbk.letsshare.ListManager;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tbk.letsshare.R;

import java.util.ArrayList;
//import java.util.Dictionary;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.CustomViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    private ArrayList<ChatListContainer> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView lastStatement;


        public CustomViewHolder(View view) {
            super(view);
            this.title =  view.findViewById(R.id.chatlist_title);
            this.lastStatement =  view.findViewById(R.id.chatlist_last_statement);
        }


    }

    public ChatListAdapter(ArrayList<ChatListContainer> list) {
        this.mList = list;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_chat, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, final int position) {
        ChatListContainer data = mList.get(position);
//        Context context = viewholder.thumbnail.getContext();
//        //https://www.learn2crack.com/2016/02/image-loading-recyclerview-picasso.html picasso 사용 참조
//        Picasso.get().load(mList.get(position).getImageURL()).resize(300, 160).into(viewholder.thumbnail);

        viewholder.title.setText(data.getRoomTitle());
        viewholder.lastStatement.setText(data.getLast_statement());

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