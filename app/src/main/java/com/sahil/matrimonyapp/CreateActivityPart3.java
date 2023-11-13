package com.sahil.matrimonyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathamesh.matrimonyapp.R;
import com.sahil.matrimonyapp.Model.User;
import com.sahil.matrimonyapp.Utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class CreateActivityPart3 extends AppCompatActivity {

    private EditText hobby1;
    private EditText hobby2;
    private EditText hobby3;


    private EditText bio;
    private Button sports;
    private Button movies;
    private Button hiking;
    private Button tech;
    private Button cook;
    private Button anime;
    private Button game;
    private Button gym;
    private Button read;

    private Button next2;
    private Button prev2;

    private Bundle intentFrom2;
    private User userfrom3;

    private List<Integer> hob;

    private List<Integer> hobData;
    private int hobbycount;


    private DatabaseReference ref;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_part3);

        hob = new ArrayList<>();
        hobData = new ArrayList<>();


        sports = findViewById(R.id.sportshobby);
        movies = findViewById(R.id.movieshobby);
        hiking = findViewById(R.id.hikinghobby);
        tech = findViewById(R.id.technologyshobby);
        cook = findViewById(R.id.cookhobby);
        anime = findViewById(R.id.animeshobby);
        game = findViewById(R.id.gamehobby);
        gym = findViewById(R.id.gymhobby);
        read = findViewById(R.id.readhobby);

        next2 = findViewById(R.id.Next2CreateAcc);
        prev2 = findViewById(R.id.Prev2CreateAcc);
        bio = findViewById(R.id.bioNameCreateAcc);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());


        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userfrom3 = Utility.getDatabase(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getDetailsFromFireBase();


        sports.setOnClickListener(v -> {
            if (sports.isSelected()) {
                sports.setSelected(false);
            } else {
                sports.setSelected(true);
            }
            updateBio();
            changeColor(sports);
//            Log.d("name " , sports.isSelected() + "" );
//            Log.d("name " , hob.get(0) + "" );
//            Log.d("name " , hob.size() + "" );

        });


        movies.setOnClickListener(v -> {
            if (movies.isSelected()) {
                movies.setSelected(false);
            } else {
                movies.setSelected(true);
            }
            updateBio();
            changeColor(movies);
        });

        hiking.setOnClickListener(v -> {
            if (hiking.isSelected()) {
                hiking.setSelected(false);
            } else {
                hiking.setSelected(true);
            }
            updateBio();
            changeColor(hiking);
        });

        tech.setOnClickListener(v -> {
            if (tech.isSelected()) {
                tech.setSelected(false);
            } else {
                tech.setSelected(true);
            }
            updateBio();
            changeColor(tech);
        });

        cook.setOnClickListener(v -> {
            if (cook.isSelected()) {
                cook.setSelected(false);
            } else {
                cook.setSelected(true);
            }
            updateBio();
            changeColor(cook);
        });

        anime.setOnClickListener(v -> {
            if (anime.isSelected()) {
                anime.setSelected(false);
            } else {
                anime.setSelected(true);
            }
            updateBio();
            changeColor(anime);
        });

        game.setOnClickListener(v -> {
            if (game.isSelected()) {
                game.setSelected(false);
            } else {
                game.setSelected(true);
            }
            updateBio();
            changeColor(game);
        });

        gym.setOnClickListener(v -> {
            if (gym.isSelected()) {
                gym.setSelected(false);
            } else {
                gym.setSelected(true);
            }
            updateBio();
            changeColor(gym);
        });

        read.setOnClickListener(v -> {
            if (read.isSelected()) {
                read.setSelected(false);
            } else {
                read.setSelected(true);
            }
            updateBio();
            changeColor(read);
        });


        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDetails();

            }
        });

        prev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intentP = new Intent(CreateActivityPart3.this , CreateActivityPart2.class);
//                intentP.putExtra("Prev2" , true);
//                Utility.updateDatabase(userfrom2 , user.getUid());
//                startActivity(intentP);
//                finish();
//                if (bio.getText().toString().isEmpty()){
//                    userfrom3.setBio(null);
//                    userfrom3.setHobbies(hob);
//                    Utility.user.setBio(null);
//                    Utility.user.setHobbies(hob);
//                    Utility.updateDatabase(userfrom3 , user.getUid());
//                }else{
//                    uploadDetails();
//                    //Utility.updateDatabase(userfrom3 , user.getUid());
//                }
//                Intent in = new Intent(CreateActivityPart3.this, CreateActivityPart2.class);
//                startActivity(in);
                startActivity(new Intent(CreateActivityPart3.this , CreateActivityPart2.class));
                finish();
            }
        });

    }

    private void updateBio(){
        if (bio.getText().toString().trim() != null){
            ref.child("bio").setValue(bio.getText().toString().trim());
        }
    }


    private void uploadDetails() {

        if (bio.getText().toString().trim() == null) {
            bio.setError("Please add a Bio");
            return;
        } else {
            userfrom3.setBio(bio.getText().toString().trim());
            ref.child("bio").setValue(bio.getText().toString().trim());
        }
        userfrom3.setHobbies(hob);
//        if (hob.size() > 0) {
//            for (int id : hob) {
//                ref.child("hobbies").child(id + "").setValue(true);
//                return;
//            }
//        }
        Intent intent = new Intent(CreateActivityPart3.this, ImageActivityPart4.class);
        startActivity(intent);
        finish();

    }


    private void getDetailsFromFireBase() {
        userfrom3 = Utility.user;
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //User userdata = Utility.getDatabase(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ;
                if (snapshot.child("bio").exists()){
                    if (snapshot.child("bio").getValue().toString() != null) {
                        bio.setText(snapshot.child("bio").getValue().toString());
                        userfrom3.setBio(snapshot.child("bio").getValue().toString());
                    }
                }


                int j = 0;
                if (snapshot.child("hobbies").exists()) {
                    for (DataSnapshot h : snapshot.child("hobbies").getChildren()) {
                        int i = Integer.parseInt(h.getKey().toString());
                        hob.add(i);
                        Button button = findViewById(i);
                        if (button != null){
                            button.setSelected(true);
                            changeColor(button);
                        }
                    }
                } else {
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void onClick(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHobbies(button.getId());
            }
        });
    }

    private void selectHobbies(int hobID) {
        switch (hobID) {
            case R.id.sportshobby:
                changeColor(sports);
                break;
            case R.id.movieshobby:
                changeColor(movies);
                break;
            case R.id.hikinghobby:
                changeColor(hiking);
                break;
            case R.id.technologyshobby:
                changeColor(tech);
                break;
            case R.id.cookhobby:
                changeColor(cook);
                break;
            case R.id.animeshobby:
                changeColor(anime);
                break;
            case R.id.gamehobby:
                changeColor(game);
                break;
            case R.id.gymhobby:
                changeColor(gym);
                break;
            case R.id.readhobby:
                changeColor(read);
                break;
            default:
                break;
        }
    }

    private void changeColor(Button view) {

        if (view.isSelected() == true) {
            FirebaseDatabase.getInstance().getReference().child("Hobbies").child(view.getText().toString())
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
            FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("hobbies").child(view.getId() + "").setValue(true);
        }

        if (view.isSelected() == false) {
            FirebaseDatabase.getInstance().getReference().child("Hobbies").child(view.getText().toString())
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
            FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("hobbies").child(view.getId() + "").removeValue();
        }


//                for (int i = 0; i < hob.size(); i++) {
//                    if (hob.get(i) == view.getId()){
//
//                    }
//                }
            //if (hob.size() < 4 || hob.size() > 0)
//    private void Color(Button button){
//        if (button.getTag() == "selected"){
//            button.setSelected(true);
//        }else {
//            button.setSelected(true);
//        }
    }
}



