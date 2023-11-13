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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathamesh.matrimonyapp.Model.User;
import com.prathamesh.matrimonyapp.PersonalChatAC;
import com.prathamesh.matrimonyapp.ProfileActivity;
import com.prathamesh.matrimonyapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder>{

    private List<User> mUsers;
    private Context mContext;
    private String hobbies;

    public MatchAdapter(List mUsers, Context mContext) {
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.matchitem, parent, false);
        return new MatchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.nameItem.setText(user.getFullName());
        Picasso.get().load(user.getImageUrl()).into(holder.profileImage);
        holder.ageItem.setText("Age : " + String.valueOf(user.getAge()));
        if (user.getGender().equals("male")){
            holder.genderItem.setText("Gender : Male");
        }else if(user.getGender().equals("female")) {
            holder.genderItem.setText("Gender : Female");
        }

        getHobbiesDataBase(holder.hobbies , user);
//        getHobbies(holder.hobbies , user);

        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("request").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("match").child(user.getUserID());
                                FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("match").child(user.getUserID()).child("userId").setValue(user.getUserID());
                                FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("match").child(user.getUserID()).child("ImageUrl").setValue(user.getImageUrl());
                                FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("match").child(user.getUserID()).child("name").setValue(user.getFullName());

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


    public class ViewHolder extends RecyclerView.ViewHolder {


        public Button chat;
        public CircleImageView profileImage;
        public TextView nameItem;
        public TextView hobbies;
        public TextView ageItem;
        public TextView genderItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chat = itemView.findViewById(R.id.chat);
            profileImage = itemView.findViewById(R.id.profileImageItem);
            nameItem = itemView.findViewById(R.id.userNameItem);
            hobbies = itemView.findViewById(R.id.professionItem);
            ageItem = itemView.findViewById(R.id.ageItem);
            genderItem = itemView.findViewById(R.id.genderItem);
        }
    }


    public void getHobbiesDataBase(TextView hob , User user){
        hobbies = "Hobbies : ";
        FirebaseDatabase.getInstance().getReference().child("Hobbies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()){
                   if(i.child(user.getUserID()).exists()){
                       hobbies = hobbies + i.getKey().toString() + "   ";
                   }
                }
                hob.setText(hobbies.trim());
                hobbies = "Hobbies : ";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void getHobbies(TextView hob , User user){
        hobbies = "Hobbies : ";
        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserID()).child("hobbies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()){
                    int j = Integer.parseInt(i.getKey());
                    String k = intToHob(j);
                    hobbies = hobbies + k +"   ";
                }
                hob.setText(hobbies.trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public String intToHob(int hobID){
        switch (hobID) {
            case R.id.sportshobby:
                return "SPORTS";
            case R.id.movieshobby:
                return "MOVIES";
            case R.id.hikinghobby:
                return "HIKING";
            case R.id.technologyshobby:
                return "TECHNOLOGY";
            case R.id.cookhobby:
                return "COOK";
            case R.id.animeshobby:
                return "ANIME";
            case R.id.gamehobby:
                return "GAME";
            case R.id.gymhobby:
                return "GYM";
            case R.id.readhobby:
                return "READ";
            default:
                return null;
        }
    }
}
