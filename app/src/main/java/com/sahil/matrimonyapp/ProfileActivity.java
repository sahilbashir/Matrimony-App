package com.sahil.matrimonyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathamesh.matrimonyapp.R;
import com.sahil.matrimonyapp.Adapter.SliderAdapter;
import com.sahil.matrimonyapp.Model.SliderImages;
import com.sahil.matrimonyapp.Model.User;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser realFirebaseUser;
    private DatabaseReference ref;

    private List<SliderImages> mImages;
    private SliderView sliderImages;
    private SliderAdapter sliderAdapter;




    private RelativeLayout setting;
    private BottomNavigationView bottomNavigationView;
    private Bundle bundle;
    private String profileID;

    private Button editProfile;
    private Button logOut;
    private User userDe;

    private String hobbies;

    private CircleImageView dp;
    private TextView name;
    private TextView gender;
    private TextView profession;
    private TextView birthDate;
    private TextView age;
    private TextView bio;
    private TextView number;
    private TextView emailId;
    private TextView marriageStatus;
    private TextView hobby;
    private TextView adress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mImages = new ArrayList<>();
        sliderImages = findViewById(R.id.slider);
        profileID = FirebaseAuth.getInstance().getUid();

        bundle = getIntent().getExtras();
        if (bundle.getBoolean("OtherProfile", false )) {
            profileID = bundle.getString("profileId");
        }
//        }else {
////            profileID = FirebaseAuth.getInstance().getUid();
//            profileID = bundle.getString("userProfileID");
//        }

        realFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();

        editProfile = findViewById(R.id.editProfile);
        logOut = findViewById(R.id.logOutButton);
        hobby = findViewById(R.id.hobbyProfile);
        dp = findViewById(R.id.profileImageUser);
        name = findViewById(R.id.usernameProfile);
        gender = findViewById(R.id.genderProfile);
        profession = findViewById(R.id.professionProfile);
        birthDate = findViewById(R.id.birthdate);
        bio = findViewById(R.id.bioProfile);
        age = findViewById(R.id.age);
        number = findViewById(R.id.numberUser);
        emailId = findViewById(R.id.emailUser);
        marriageStatus = findViewById(R.id.marriedSttus);
        adress = findViewById(R.id.adressUser);


        bottomNavigationView = findViewById(R.id.bottomNavigatorHome);
        setting = findViewById(R.id.userSettings);
        userDe = new User();



        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_chat:
                        startActivity(new Intent(ProfileActivity.this , ChatActivity.class));
                        finish();
                        break;
                    case R.id.nav_match:
                        Intent intent = new Intent(ProfileActivity.this , MatchActivity.class);
//                        intent.putExtra("profileID" , FirebaseAuth.getInstance().getCurrentUser().getUid());
//                        intent.putExtra("UserPro" , true);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(ProfileActivity.this , HomeActivity.class));
                        finish();
                        break;
                    case R.id.nav_request:
                        startActivity(new Intent(ProfileActivity.this , RequestActivity.class));
                        finish();
                        break;
                }
                return false;
            }
        });

        if (profileID != null){
            if(profileID.equals(FirebaseAuth.getInstance().getUid())){
                setting.setVisibility(View.VISIBLE);
                logOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        finish();
                    }
                });
                editProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(ProfileActivity.this, CreateActivityPart2.class));
                        finish();
                    }
                });

            }else {
                setting.setVisibility(View.GONE);
            }
        }

        getImagesFromFireBase();
        sliderAdapter = new SliderAdapter(mImages , ProfileActivity.this);
        sliderImages.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        sliderImages.setScrollTimeInSec(3);
        sliderImages.setAutoCycle(true);
        sliderImages.startAutoCycle();

        sliderImages.setSliderAdapter(sliderAdapter);


        getDataFromFireBase();
        getHobbies();

    }

    private void getImagesFromFireBase(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(profileID).child("imagesUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mImages.clear();
                if (snapshot.child("image1").exists()){
                    mImages.add(new SliderImages(snapshot.child("image1").getValue().toString()));
                }else {
                    mImages.add(new SliderImages("https://firebasestorage.googleapis.com/v0/b/matrimony-app-6da14.appspot.com/o/default%2FNoneImages.jpg?alt=media&token=0edb6fb9-6dca-4616-9ec2-b437e1fb0f4a"));
                }
                if (snapshot.child("image2").exists()) {
                    mImages.add(new SliderImages(snapshot.child("image2").getValue().toString()));
                }
                if (snapshot.child("image3").exists()) {
                    mImages.add(new SliderImages(snapshot.child("image3").getValue().toString()));
                }
                if (snapshot.child("image4").exists()) {
                    mImages.add(new SliderImages(snapshot.child("image4").getValue().toString()));
                }
                sliderAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataFromFireBase(){

        FirebaseDatabase.getInstance().getReference().child("Users").child(profileID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("userID").exists()) {
                    userDe.setUserID(snapshot.child("userID").getValue().toString());
                }
                if (snapshot.child("email").exists()) {
                    userDe.setEmail(snapshot.child("email").getValue().toString());
                    emailId.setText("EMAIL : " + userDe.getEmail());
                }
                if (snapshot.child("password").exists()) {
                    userDe.setPassword(snapshot.child("password").getValue().toString());
                }
                if (snapshot.child("imageUrl").exists()){
                    userDe.setImageUrl(snapshot.child("imageUrl").getValue().toString());
                    Picasso.get().load(userDe.getImageUrl()).into(dp);
                }
                if (snapshot.child("adress").exists()) {
                    userDe.setAdress(snapshot.child("adress").getValue().toString());
                    adress.setText("ADRESS : " + userDe.getAdress());
                }
                if (snapshot.child("fullName").exists()) {
                    userDe.setFullName(snapshot.child("fullName").getValue().toString());
                    name.setText(userDe.getFullName());
                }
                if (snapshot.child("profession").exists()) {
                    userDe.setProfession(snapshot.child("profession").getValue().toString());
                    profession.setText("POFESSION : " + userDe.getProfession());
                }
                if (snapshot.child("birthDate").exists()) {
                    userDe.setBirthDate(snapshot.child("birthDate").getValue().toString());
                    birthDate.setText("BIRTHDATE : " + userDe.getBirthDate());
                }
                if (snapshot.child("age").exists()) {
                    userDe.setAge(Integer.parseInt(snapshot.child("age").getValue().toString()));
                    age.setText("AGE : " + userDe.getAge());
                }
                if (snapshot.child("gender").exists()) {
                    userDe.setGender(snapshot.child("gender").getValue().toString());
                    if (userDe.getGender().equals("male")){
                        gender.setText("Gender : MALE");
                    }else if(userDe.getUserID().equals("female")) {
                        gender.setText("Gender : FEMALE");
                    }
                }
                if (snapshot.child("number").exists()) {
                    userDe.setNumber(snapshot.child("number").getValue().toString());
                    number.setText("NUMBER : " + userDe.getNumber());
                }
                if (snapshot.child("bio").exists()) {
                    userDe.setBio(snapshot.child("bio").getValue().toString());
                    bio.setText("Hello! I am " + userDe.getFullName() + "\n" + userDe.getBio());
                }
                if (snapshot.child("isMarried").exists()) {
                    Boolean b = Boolean.parseBoolean(snapshot.child("isMarried").getValue().toString());
                    userDe.setMarried(b);
                    if (userDe.isMarried() == true){
                        marriageStatus.setText("MARTIAL STATUS : MARRIED ");
                    }else {
                        marriageStatus.setText("MARTIAL STATUS : UNMARRIED ");
                    }
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getHobbies(){
        hobbies = "";
        FirebaseDatabase.getInstance().getReference().child("Hobbies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()){
                    if(i.child(profileID).exists()){
                        hobbies = hobbies + i.getKey().toString() + "   ";
                    }
                }
                hobby.setText(hobbies.trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}