package com.sahil.matrimonyapp.Utility;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sahil.matrimonyapp.HomeActivity;
import com.sahil.matrimonyapp.Login_Page;
import com.sahil.matrimonyapp.Model.User;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class Utility {

    private List<String> li;
    public static User user = new User();

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static CollectionReference getCollectionReference() {
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("Notes")
                .document(currentuser.getUid()).collection("my_notes");
    }

    static String timeToString(Timestamp timestamp) {
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }

    public static void updateDatabase(User user, String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userID", user.getUserID());
        map.put("email", user.getEmail());
        map.put("password", user.getPassword());
        map.put("imageUrl", user.getImageUrl());
        map.put("adress", user.getAdress());
        map.put("fullName", user.getFullName());
        map.put("profession", user.getProfession());
        map.put("birthDate", user.getBirthDate());
        map.put("age", user.getAge());
        map.put("gender", user.getGender());
        map.put("number", user.getNumber());
        map.put("isMarried", user.isMarried());
        map.put("bio", user.getBio());
        map.put("hobbies", user.getHobbies());
        map.put("imagesUser", user.getImagesUser());

        FirebaseDatabase.getInstance().getReference().child("Users").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);
    }

    public static User getDatabase(String id) {
        User userDe = new User();
        FirebaseDatabase.getInstance().getReference().child("Users").child(id.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

//                userDe.setUserID(snapshot.child("hobbies").toString());
//                userDe.setHobbies(snapshot.child("isMarried").getValue(String.class));
                //userDe.setImagesUser(snapshot.child("imagesUser").getValue());
                Log.d("User", "Inside Utility");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return userDe;
    }


//    private HashMap<String , Object> map = new HashMap<>();
//    public User convertUser(List<Object> list){
//        int i = 0;
//        if (list.get(i)..equals("userID" , String))
//    }

}
