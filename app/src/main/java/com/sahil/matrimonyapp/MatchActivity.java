package com.sahil.matrimonyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathamesh.matrimonyapp.R;
import com.sahil.matrimonyapp.Adapter.MatchAdapter;
import com.sahil.matrimonyapp.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MatchActivity extends AppCompatActivity {


    private String gender;
    private int count;
    private String usergender;


    private RelativeLayout layout;
    
    private DatabaseReference ref;
    private FirebaseUser cuUser ;
    private DatabaseReference refGender;

    private List<User> mUser;
    private List<String> matchUser;
    private List<String> finalUser;
    private List<String> sendUser;
    private List<String> recivedUser;

    private List<String> hobbiesUser;
    private List<String> hobbiesGender;

    private RecyclerView recyclerViewMatch;
    private MatchAdapter matchAdapter;

    private BottomNavigationView bottomNavigationView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        layout = findViewById(R.id.layoutMatch);

        ref = FirebaseDatabase.getInstance().getReference();
        cuUser = FirebaseAuth.getInstance().getCurrentUser();
        refGender = FirebaseDatabase.getInstance().getReference().child("Users");

        ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usergender = snapshot.child("gender").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        hobbiesGender = new ArrayList<>();
        hobbiesUser = new ArrayList<>();
        ref.child("Hobbies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot j : snapshot.getChildren()){
                    if (j.child(cuUser.getUid()).exists()){
                        for (DataSnapshot i : j.getChildren()){
                            if (!i.getKey().equals(cuUser.getUid())){
                                Log.d("Match" , i.getKey());
                                FirebaseDatabase.getInstance().getReference().child("Matching").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("hobbyMatch").child(i.getKey()).child(j.getKey()).setValue(true);
                            }
                        }
                    }
                }
                hobbyUserGender();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        matchUser = new ArrayList<>();
        sendUser = new ArrayList<>();
        recivedUser = new ArrayList<>();
        mUser = new ArrayList<>();

        recyclerViewMatch = findViewById(R.id.recycllerViewMatch);
        recyclerViewMatch.setHasFixedSize(true);
        recyclerViewMatch.setLayoutManager(new LinearLayoutManager(MatchActivity.this));
        matchAdapter = new MatchAdapter(mUser , MatchActivity.this);
        recyclerViewMatch.setAdapter(matchAdapter);

        hobbyUserGender();
        sendRecivedRequest();
        matchUser44();
        matchHobbyUser();

        getDataUser();

        bottomNavigationView = findViewById(R.id.bottomNavigatorHome);

        bottomNavigationView.setSelectedItemId(R.id.nav_match);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_chat:
                        startActivity(new Intent(MatchActivity.this , ChatActivity.class));
                        finish();
                        break;
                    case R.id.nav_profile:
                        Intent intent = new Intent(MatchActivity.this , ProfileActivity.class);
                        intent.putExtra("profileID" , FirebaseAuth.getInstance().getCurrentUser().getUid());
                        intent.putExtra("UserPro" , true);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(MatchActivity.this , HomeActivity.class));
                        finish();
                        break;
                    case R.id.nav_request:
                        startActivity(new Intent(MatchActivity.this , RequestActivity.class));
                        finish();
                        break;
                }
                return false;
            }
        });


        recyclerViewMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

    }

    public void sendRecivedRequest(){
        FirebaseDatabase.getInstance().getReference().child("Request").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("send").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sendUser.clear();
                        Log.d("name" , "entered send");
                        for (DataSnapshot i : snapshot.getChildren()){
                            sendUser.add(i.getKey().toString());
                            Log.d("name" , "entered send" + i.getKey().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        FirebaseDatabase.getInstance().getReference().child("Request").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("received").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        recivedUser.clear();
                        Log.d("name" , "entered recevied");
                        for (DataSnapshot i : snapshot.getChildren()){
                            recivedUser.add(i.getKey().toString());
                            Log.d("name" , "entered recevide" + i.getKey().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        matchUser44();
    }

    public void matchUser44(){
        Log.d("name" , "entered match method" );
        for (String i : sendUser){
            Log.d("name" , "entered match i " + i );
            for (String j : recivedUser){
                Log.d("name" , "entered match j " + j );
                if (i.equals(j)){
                    FirebaseDatabase.getInstance().getReference().child("Matching").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("finalSort").child(i).setValue("request");
                    Log.d("name" , "entered match done " + i );
                }
            }
        }
    }

    private void matchHobbyUser(){
        FirebaseDatabase.getInstance().getReference().child("Matching").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("genderSort").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot i: snapshot.getChildren()){
                            if (i.exists()){
                                if (!i.getValue().toString().equals(usergender.trim())){
                                    matchUser.add(i.getKey());
                                    Log.d("Match Added " , i.getKey() + "  " + i.getValue().toString());
                                    FirebaseDatabase.getInstance().getReference().child("Matching").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("finalSort").child(i.getKey()).setValue("hobbies");
                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getDataUser(){
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    matchUserSorted();
                    for (String us : matchUser){
                        Log.d("name" , snapshot.getKey().toString() + "Entered1");
                        if (snapshot.child("userID").getValue().toString().equals(us)){
                            Log.d("name" , snapshot.getValue().toString()+ "," + us + "Enteredif");
                            User userDe = new User();
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
                }
                matchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void matchUserSorted(){

        FirebaseDatabase.getInstance().getReference().child("Matching").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("finalSort").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        matchUser.clear();
                        for (DataSnapshot i : snapshot.getChildren()){
                            matchUser.add(i.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void hobbyUserGender(){
        hobbiesUser.clear();
        FirebaseDatabase.getInstance().getReference().child("Matching").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("hobbyMatch").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot i: snapshot.getChildren()){
                            hobbiesUser.add(i.getKey());

                            ref.child("Users").child(i.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    gender = snapshot.child("gender").getValue().toString();
                                    Log.d("Match Gender" , i + "  " + gender);
                                    if (gender != usergender) {
                                        FirebaseDatabase.getInstance().getReference().child("Matching").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("genderSort").child(i.getKey()).setValue(gender);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


}