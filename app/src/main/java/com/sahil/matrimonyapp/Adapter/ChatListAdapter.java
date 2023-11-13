package com.sahil.matrimonyapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sahil.matrimonyapp.Model.ChatUser;
import com.sahil.matrimonyapp.PersonalChatAC;
import com.prathamesh.matrimonyapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder>{

    public Context mContext;
    public List<ChatUser> mUser;

    public ChatListAdapter(Context mContext, List<ChatUser> mUser) {
        this.mContext = mContext;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_activity_item, parent, false);
        return new ChatListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ChatUser user = mUser.get(position);

        if (user != null){
            Picasso.get().load(user.getImageUrl()).into(holder.image);

            holder.nameChat.setText(user.getName());

            if (user.getmORr().equals("m")){
                holder.context.setText("Matched User");
            }else {
                holder.context.setText("Requested User");
            }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext , PersonalChatAC.class);
                    intent.putExtra("ChatUser" , true);
                    intent.putExtra("chatUserId", user.getUserId());
                    mContext.startActivity(intent);
                }
            });

            holder.nameChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext , PersonalChatAC.class);
                    intent.putExtra("ChatUser" , true);
                    intent.putExtra("chatUserId", user.getUserId());
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView image;
        private TextView nameChat;
        private TextView context;
        private Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.userChatProfile);
            nameChat = itemView.findViewById(R.id.userNameChat);
            context = itemView.findViewById(R.id.textChat);
            delete = itemView.findViewById(R.id.removeMatch);

        }
    }
}
