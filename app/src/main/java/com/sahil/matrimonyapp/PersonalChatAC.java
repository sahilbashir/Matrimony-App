package com.sahil.matrimonyapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.type.DateTime;
import com.sahil.matrimonyapp.Model.PsChatAdapter;
import com.sahil.matrimonyapp.Model.userMessage;
import com.prathamesh.matrimonyapp.R;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalChatAC extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<userMessage> mUsers;
    private PsChatAdapter adapter;


    private CircleImageView profileUser;
    private TextView nameUser;
    private CircleImageView profileCuUser;
    private EditText userMessage;
    private ImageView sendMessage;

    private Bundle bundle;

    private String profileId;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);


        bundle = getIntent().getExtras();
        profileId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = findViewById(R.id.recyclerViewChat);
        profileUser = findViewById(R.id.dpChatList);
        nameUser = findViewById(R.id.name);
        profileCuUser = findViewById(R.id.dpUserChatList);
        userMessage = findViewById(R.id.userMessage);
        sendMessage = findViewById(R.id.sendMessage);

        mUsers = new ArrayList<>();

        if (bundle.getBoolean("ChatUser" , false)){
            userId = bundle.getString("chatUserId");
        }

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager l = new LinearLayoutManager(this);
        l.setStackFromEnd(true);
        l.setReverseLayout(false);
        recyclerView.setLayoutManager(l);
        adapter = new PsChatAdapter(this , mUsers);
        recyclerView.setAdapter(adapter);


        getUserData();
        getMessages();


        sendMessage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + "";
                FirebaseDatabase.getInstance().getReference().child("Chats").child(profileId).child(userId).child(date)
                        .child("contex").setValue("send");
                FirebaseDatabase.getInstance().getReference().child("Chats").child(profileId).child(userId).child(date)
                        .child("message").setValue(userMessage.getText().toString().trim());

                FirebaseDatabase.getInstance().getReference().child("Chats").child(userId).child(profileId).child(date)
                        .child("contex").setValue("recevied");
                FirebaseDatabase.getInstance().getReference().child("Chats").child(userId).child(profileId).child(date)
                        .child("message").setValue(userMessage.getText().toString().trim());
                userMessage.setText("");
            }
        });

    }

    private void getUserData(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nameUser.setText(snapshot.child("fullName").getValue().toString());
                Picasso.get().load(snapshot.child("imageUrl").getValue().toString()).into(profileUser);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Users").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("imageUrl").getValue().toString()).into(profileCuUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMessages(){

//        FirebaseDatabase.getInstance().getReference().child("Chats").child(profileId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                String dateTime;
////                for (DataSnapshot i : snapshot.getChildren()){
////                    dateTime = i.getKey().toString() + "/";
////                    for (DataSnapshot j : i.getChildren()){
////                        dateTime = dateTime + j.getKey().toString() + "/";
////                        for (DataSnapshot k : j.getChildren()){
////                            dateTime = dateTime + k.getKey().toString();
////                        }
////                    }
////                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        FirebaseDatabase.getInstance().getReference().child("Chats").child(profileId).child(userId).orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()){
                    if (snapshot.exists()){
                        String date = snapshot.getKey().toString();
                        userMessage message = new userMessage();
                        if (snapshot.child("contex").exists()){
                            message.setContex(snapshot.child("contex").getValue().toString());
                            Log.d("NamePratham" , message.getContex());
                        }
                        if (snapshot.child("message").exists()){
                            message.setMessage(snapshot.child("message").getValue().toString());
                            Log.d("NamePratham", message.getMessage());
                        }
                        message.setDateTime(date);
                        Log.d("NamePratham" , date);
                        try {
                            message.setDateTimeFormat(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (message.getDateTimeFormat() != null)
                            Log.d("NamePratham" , message.getDateTimeFormat().toString());

                        mUsers.add(message);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter.notifyDataSetChanged();

    }
}