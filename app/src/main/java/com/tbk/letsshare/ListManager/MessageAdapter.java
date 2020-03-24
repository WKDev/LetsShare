package com.tbk.letsshare.ListManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tbk.letsshare.ChatRoomActivity;
import com.tbk.letsshare.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }


    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private MessageAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(MessageAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public ArrayList<MessageContainer> mList;

    public MessageAdapter(ArrayList<MessageContainer> list) {
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view;

        switch (viewType) {
            case 0:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_my_message, viewGroup, false);
                return new myMessageViewHolder(view);
            case 1:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_message, viewGroup, false);
                return new MessageViewHolder(view);
        }
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_message, viewGroup, false);
        return new MessageViewHolder(view);
    }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position)
        {
            MessageContainer data = mList.get(position);
//        Context context = viewholder.thumbnail.getContext();
//        //https://www.learn2crack.com/2016/02/image-loading-recyclerview-picasso.html picasso 사용 참조
//        Picasso.get().load(mList.get(position).getImageURL()).resize(300, 160).into(viewholder.thumbnail);
            if(data.getWriter() == "me"){ // 내 메세지인 경우
                myMessageViewHolder holder1 = (myMessageViewHolder) holder;
                holder1.mymessageText.setText(data.getMessage());
                holder1.mydateText.setText(data.getDate());

                holder1.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = position;
                        if (pos != RecyclerView.NO_POSITION) {
                            if (mListener != null) {
                                mListener.onItemClick(v, pos);
                            }
                        }
                    }
                });

            }
            else{ // 내 메세지인 경우
                MessageViewHolder holder2 = (MessageViewHolder) holder;
                holder2.messageText.setText(data.getMessage());
                holder2.dateText.setText(data.getDate());

                holder2.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = position;
                        if (pos != RecyclerView.NO_POSITION) {
                            if (mListener != null) {
                                mListener.onItemClick(v, pos);
                            }
                        }
                    }
                });

            }

        }

        @Override
        public int getItemViewType (int position){
            MessageContainer data = mList.get(position);

            if (data.getWriter().equals("me")) {
                return 0;
            } else {
                return 1;
            }
            // 컨테이너의 writer 값이 SharedPreference에 저장된 user_id 값과 같다면 0, 아니면 1 반환
        }

        @Override
        public int getItemCount () {
            return (null != mList ? mList.size() : 0);
        }


    public class myMessageViewHolder extends RecyclerView.ViewHolder {
        protected TextView mymessageText;
        protected TextView mydateText;


        public myMessageViewHolder(View view) {
            super(view);
            this.mymessageText = view.findViewById(R.id.my_msg_context);
            this.mydateText = view.findViewById(R.id.my_msg_date);
        }
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        protected TextView messageText;
        protected TextView dateText;


        public MessageViewHolder(View view) {
            super(view);
            this.messageText = view.findViewById(R.id.msg_context);
            this.dateText = view.findViewById(R.id.msg_date);
        }
    }


}
