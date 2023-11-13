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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathamesh.matrimonyapp.R;
import com.sahil.matrimonyapp.Adapter.HomeItemAdapter;
import com.sahil.matrimonyapp.Model.User;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private DatabaseReference refUser;
    private FirebaseUser cuUser ;


    private List<User> mUser;
    private RecyclerView usersRecycler;
    private HomeItemAdapter homeItemAdapter;

    String gender ;

    private BottomNavigationView bottomNavigationView ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ref = FirebaseDatabase.getInstance().getReference();
        refUser = FirebaseDatabase.getInstance().getReference();
        cuUser = FirebaseAuth.getInstance().getCurrentUser();

        ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gender = snapshot.child("gender").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mUser = new ArrayList<>();
        usersRecycler = findViewById(R.id.recycllerViewHome);

        bottomNavigationView = findViewById(R.id.bottomNavigatorHome);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_chat:
                        startActivity(new Intent(HomeActivity.this , ChatActivity.class));
                        finish();
                        break;
                    case R.id.nav_profile:
                        Intent intent = new Intent(HomeActivity.this , ProfileActivity.class);
                        intent.putExtra("profileID" , FirebaseAuth.getInstance().getCurrentUser().getUid());
                        intent.putExtra("UserPro" , true);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_match:
                        startActivity(new Intent(HomeActivity.this , MatchActivity.class));
                        finish();
                        break;
                    case R.id.nav_request:
                        startActivity(new Intent(HomeActivity.this , RequestActivity.class));
                        finish();
                        break;
                }
                return false;
            }
        });


        getUserFromFireBase();
        usersRecycler.setHasFixedSize(true);
        usersRecycler.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        homeItemAdapter = new HomeItemAdapter(mUser , HomeActivity.this , 1);
        usersRecycler.setAdapter(homeItemAdapter);

    }

    private void getUserFromFireBase(){
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User userDe = new User();
                    if (!snapshot.child("userID").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        if (!snapshot.child("gender").getValue().toString().equals(gender)) {
                        }
                        Log.d("HomeAct cond" , gender);
                        Log.d("HomeAct cond" , snapshot.child("gender").getValue().toString());
                        if (snapshot.child("userID").exists())
                            userDe.setUserID(snapshot.child("userID").getValue().toString());
                        if (snapshot.child("email").exists())
                            userDe.setEmail(snapshot.child("email").getValue().toString());
                        if (snapshot.child("password").exists())
                            userDe.setPassword(snapshot.child("password").getValue().toString());
                        if (snapshot.child("imageUrl").exists())
                            userDe.setImageUrl(snapshot.child("imageUrl").getValue().toString());
                        if (snapshot.child("adress").exists())
                            userDe.setAdress(snapshot.child("adress").getValue().toString());
                        if (snapshot.child("fullName").exists())
                            userDe.setFullName(snapshot.child("fullName").getValue().toString());
                        if (snapshot.child("profession").exists())
                            userDe.setProfession(snapshot.child("profession").getValue().toString());
                        if (snapshot.child("birthDate").exists())
                            userDe.setBirthDate(snapshot.child("birthDate").getValue().toString());
                        if (snapshot.child("age").exists())
                            userDe.setAge(Integer.parseInt(snapshot.child("age").getValue().toString()));
                        if (snapshot.child("gender").exists())
                            userDe.setGender(snapshot.child("gender").getValue().toString());
                        if (snapshot.child("number").exists())
                            userDe.setNumber(snapshot.child("number").getValue().toString());
                        if (snapshot.child("bio").exists())
                            userDe.setBio(snapshot.child("bio").getValue().toString());
                        if (snapshot.child("isMarried").exists()) {
                            Boolean b = Boolean.parseBoolean(snapshot.child("isMarried").getValue().toString());
                            userDe.setMarried(b);
                        }
                        List <String> imageUrl = new ArrayList<>();
                        if (snapshot.child("imagesUser").exists()){
                            for(DataSnapshot i : snapshot.child("imagesUser").getChildren()){
                                imageUrl.add(i.getValue().toString());
                            }
                            userDe.setImagesUser(imageUrl);
                        }
                        List <Integer> hobId = new ArrayList<>();
                        if (snapshot.child("hobbies").exists()){
                            for(DataSnapshot i : snapshot.child("hobbies").getChildren()){
                                hobId.add(Integer.parseInt(i.getKey().toString()));
                            }
                            userDe.setHobbies(hobId);
                        }
                        mUser.add(userDe);

                    }
                    }
                homeItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}