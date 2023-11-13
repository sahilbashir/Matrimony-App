package com.sahil.matrimonyapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathamesh.matrimonyapp.Model.User;
import com.prathamesh.matrimonyapp.PersonalChatAC;
import com.prathamesh.matrimonyapp.ProfileActivity;
import com.prathamesh.matrimonyapp.R;
import com.prathamesh.matrimonyapp.RequestActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.ViewHolder> {

    private int viewLook;
    private List<User> mUsers;
    private Context mContext;

    public HomeItemAdapter(List mUsers, Context mContext , int viewLook) {
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.viewLook = viewLook;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int idActivity = 0;
        if (viewLook == 1){
            idActivity = R.layout.home_ativity_item;
        }else if (viewLook == 2){
            idActivity = R.layout.likerequestitem;
        }
        View view = LayoutInflater.from(mContext).inflate(idActivity, parent, false);

        return new ViewHolder(view , viewLook);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);

        if (viewLook == 1){
            holder.nameItem.setText(user.getFullName());
            holder.profession.setText("Profession : " + user.getProfession());
            holder.ageItem.setText("Age : " + String.valueOf(user.getAge()));
            Picasso.get().load(user.getImageUrl()).into(holder.profileImage);
            Log.d("Home Ac" , user.getGender());
            if (user.getGender().equals("male")){
                holder.genderItem.setText("Gender : Male");
            }else if(user.getGender().equals("female")) {
                holder.genderItem.setText("Gender : Female");
            }

            if (user.isMarried() == true){
                holder.marriageStatus.setText("First Marriage : No");
            }else {
                holder.marriageStatus.setText("First Marriage : Yes");
            }

            checkRequestStatus(user , holder.sendProposal);

            holder.sendProposal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!holder.sendProposal.isSelected()){
                        holder.sendProposal.setSelected(true);
                        holder.sendProposal.setText("SEND");
                        FirebaseDatabase.getInstance().getReference().child("Request").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("send").child(user.getUserID()).setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("Request").child(user.getUserID())
                                .child("received").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                    }else {
                        holder.sendProposal.setSelected(false);
                        holder.sendProposal.setText("REQUEST");
                        FirebaseDatabase.getInstance().getReference().child("Request").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("send").child(user.getUserID()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Request").child(user.getUserID())
                                .child("received").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                    }
                }
            });
        }else if (viewLook == 2){
            holder.nameItem.setText("Name : " + user.getFullName());
            holder.ageItem.setText("Age : " + String.valueOf(user.getAge()));
            holder.profession.setText(user.getProfession());
            Picasso.get().load(user.getImageUrl()).into(holder.profileImage);
            Log.d("Home Ac" , user.getGender());
            if (user.getGender().equals("male")){
                holder.genderItem.setText("Gender : Male");
            }else if(user.getGender().equals("female")) {
                holder.genderItem.setText("Gender : Female");
            }
            holder.chatWithUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("match").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("request").child(user.getUserID());
                                    FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("request").child(user.getUserID()).child("userId").setValue(user.getUserID());
                                    FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("request").child(user.getUserID()).child("ImageUrl").setValue(user.getImageUrl());
                                    FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("request").child(user.getUserID()).child("name").setValue(user.getFullName());

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                    Intent intent = new Intent(mContext , PersonalChatAC.class);
                    intent.putExtra("ChatUser" , true);
                    intent.putExtra("chatUserId", user.getUserID());
                    mContext.startActivity(intent);

                }
            });
            holder.removeUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("Request").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("removed").child(user.getUserID()).setValue(true);
                    mContext.startActivity(new Intent(mContext , RequestActivity.class));
                }
            });
        }

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext , ProfileActivity.class);
                intent.putExtra("OtherProfile" , true);
                intent.putExtra("profileId", user.getUserID());
                mContext.startActivity(intent);
            }
        });

        holder.nameItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext , ProfileActivity.class);
                intent.putExtra("OtherProfile" , true);
                intent.putExtra("profileId", user.getUserID());
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    private void checkRequestStatus(User user , Button button){
        if (user.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            button.setVisibility(View.GONE);
            return;
        }
        FirebaseDatabase.getInstance().getReference().child("Request").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("send").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(user.getUserID().toString()).exists()){
                   button.setSelected(true);
                   button.setText("SENT");
                }else {
                    button.setSelected(false);
                    button.setText("REQUEST");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//    private void checkRequestStatus(Button button , User user){
//
//        FirebaseDatabase.getInstance().getReference().child("Request").child("send").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.child(user.getUserID()).exists()){
//                    button.setSelected(true);
//                }else {
//                    button.setSelected(true);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView likesYou;
        public Button chatWithUser;
        public Button removeUser;
        public Button sendProposal;
        public CircleImageView profileImage;
        public TextView nameItem;
        public TextView profession;
        public TextView ageItem;
        public TextView genderItem;
        public TextView marriageStatus;


        public ViewHolder(@NonNull View itemView , int i) {
            super(itemView);

            if (i == 1){
                sendProposal = itemView.findViewById(R.id.sendProposal);
                profileImage = itemView.findViewById(R.id.profileImageItem);
                nameItem = itemView.findViewById(R.id.userNameItem);
                profession = itemView.findViewById(R.id.professionItem);
                ageItem = itemView.findViewById(R.id.ageItem);
                genderItem = itemView.findViewById(R.id.genderItemHome);
                marriageStatus = itemView.findViewById(R.id.isMarriedItem);
            }else if (i == 2){
                removeUser = itemView.findViewById(R.id.removeMatch);
                chatWithUser = itemView.findViewById(R.id.chat);
                profileImage = itemView.findViewById(R.id.profileImageItem);
                nameItem = itemView.findViewById(R.id.userNameItem);
                profession = itemView.findViewById(R.id.professionItem);
                likesYou = itemView.findViewById(R.id.likesYou);
                ageItem = itemView.findViewById(R.id.ageItem);
                genderItem = itemView.findViewById(R.id.genderItem);

            }



        }
    }
}
