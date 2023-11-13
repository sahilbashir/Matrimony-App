package com.sahil.matrimonyapp.Model;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sahil.matrimonyapp.Adapter.MatchAdapter;
import com.prathamesh.matrimonyapp.R;

import java.util.List;

public class PsChatAdapter extends RecyclerView.Adapter<PsChatAdapter.ViewHolder>{

    private Context mCOntext;
    private List<userMessage> messageList;

    public PsChatAdapter(Context mCOntext, List<userMessage> messageList) {
        this.mCOntext = mCOntext;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCOntext).inflate(R.layout.chatitemmessage, parent, false);
        return new PsChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        userMessage user = messageList.get(position);
        Log.d("NamePratham" , user.getMessage());
        Log.d("NamePratham" , user.getContex());

        if (user.getContex().equals("send")){
            holder.layout1.setVisibility(View.VISIBLE);
            holder.layout2.setVisibility(View.GONE);
            holder.sendmessage.setText(user.getMessage());
        }else if (user.getContex().equals("recevied")){
            holder.layout1.setVisibility(View.GONE);
            holder.layout2.setVisibility(View.VISIBLE);
            holder.receviedmessage.setText(user.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout layout1;
        private RelativeLayout layout2;
        private TextView sendmessage;
        private TextView receviedmessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout1 = itemView.findViewById(R.id.layoutChat);
            layout2 = itemView.findViewById(R.id.layoutChat2);
            sendmessage = itemView.findViewById(R.id.messagesend);
            receviedmessage = itemView.findViewById(R.id.messagerecevied);



        }
    }
}
