package com.sahil.matrimonyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathamesh.matrimonyapp.R;
import com.sahil.matrimonyapp.Adapter.ChatListAdapter;
import com.sahil.matrimonyapp.Model.ChatUser;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private List<ChatUser> mUsers;
    private List<ChatUser> finalUsers;
    private List<ChatUser> matchUser;
    private List<ChatUser> requestUsers;

    private RecyclerView recyclerView;

    private ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        bottomNavigationView = findViewById(R.id.bottomNavigatorHome);

        mUsers = new ArrayList<>();
        finalUsers = new ArrayList<>();
        matchUser = new ArrayList<>();
        requestUsers = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewChatList);

        bottomNavigationView.setSelectedItemId(R.id.nav_chat);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(ChatActivity.this, HomeActivity.class));
                        finish();
                        break;
                    case R.id.nav_profile:
                        Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
                        intent.putExtra("profileID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        intent.putExtra("UserPro", true);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_match:
                        startActivity(new Intent(ChatActivity.this, MatchActivity.class));
                        finish();
                        break;
                    case R.id.nav_request:
                        startActivity(new Intent(ChatActivity.this, RequestActivity.class));
                        finish();
                        break;
                }
                return false;
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatListAdapter = new ChatListAdapter(this, finalUsers);
        getUsers();
        requestUser();
        mainUser();
        recyclerView.setAdapter(chatListAdapter);

    }

    private void getUsers() {
        FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("match").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            matchUser.clear();
                            for (DataSnapshot i : snapshot.getChildren()) {
                                if (i.child("userId").exists()) {
                                    ChatUser user = new ChatUser();
                                    user.setmORr("m");
                                    user.setUserId(i.child("userId").getValue().toString());
                                    if (i.child("ImageUrl").exists())
                                        user.setImageUrl(i.child("ImageUrl").getValue().toString());
                                    if (i.child("name").exists())
                                        user.setName(i.child("name").getValue().toString());
                                    FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("common").child(user.getUserId()).setValue(user);
                                }
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void requestUser() {
        FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("request").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            requestUsers.clear();
                            for (DataSnapshot i : snapshot.getChildren()) {
                                if (i.child("userId").exists()) {
                                    ChatUser user = new ChatUser();
                                    user.setmORr("m");
                                    user.setUserId(i.child("userId").getValue().toString());
                                    if (i.child("ImageUrl").exists())
                                        user.setImageUrl(i.child("ImageUrl").getValue().toString());
                                    if (i.child("name").exists())
                                        user.setName(i.child("name").getValue().toString());
                                    FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("common").child(user.getUserId()).setValue(user);
                                }
//                                if (mUsers != null) {
//                                    for (ChatUser userChat : mUsers) {
//                                        if (!userChat.getUserId().equals(i.child("userId").getValue().toString())) {
//                                            Log.d("name", "Entered in equals loop");
//                                            if (i.child("userId").exists()) {
//                                                ChatUser user = new ChatUser();
//                                                user.setmORr("r");
//                                                user.setUserId(i.child("userId").getValue().toString());
//                                                if (i.child("ImageUrl").exists())
//                                                    user.setImageUrl(i.child("ImageUrl").getValue().toString());
//                                                if (i.child("name").exists())
//                                                    user.setName(i.child("name").getValue().toString());
//                                                finalUsers.add(user);
//                                            }
//                                        }
//                                    }
//                                }
//
//                                if (mUsers.isEmpty()){
//                                    ChatUser user = new ChatUser();
//                                    user.setmORr("r");
//                                    user.setUserId(i.child("userId").getValue().toString());
//                                    if (i.child("ImageUrl").exists())
//                                        user.setImageUrl(i.child("ImageUrl").getValue().toString());
//                                    if (i.child("name").exists())
//                                        user.setName(i.child("name").getValue().toString());
//                                    finalUsers.add(user);
//                                }
//
//                                chatListAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void mainUser(){
        FirebaseDatabase.getInstance().getReference().child("ChatUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("common").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            finalUsers.clear();
                            for (DataSnapshot i : snapshot.getChildren()) {
                                if (i.child("userId").exists()) {
                                    ChatUser user = new ChatUser();
                                    user.setmORr("m");
                                    user.setUserId(i.child("userId").getValue().toString());
                                    if (i.child("imageUrl").exists())
                                        user.setImageUrl(i.child("imageUrl").getValue().toString());
                                    if (i.child("name").exists())
                                        user.setName(i.child("name").getValue().toString());
                                    finalUsers.add(user);
                                }
                            }
                            chatListAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}

