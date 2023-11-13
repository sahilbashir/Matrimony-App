package com.sahil.matrimonyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathamesh.matrimonyapp.R;
import com.sahil.matrimonyapp.Model.User;
import com.sahil.matrimonyapp.Utility.Utility;

import java.util.HashMap;

public class Login_Page extends AppCompatActivity {


    private EditText emailTxt,passwordTxt;
    private Button LogInTxt;
    private TextView SignUpTxt;
    private ProgressBar loadingTxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);


        emailTxt = findViewById(R.id.user_email_login);
        passwordTxt = findViewById(R.id.user_password_login);
        LogInTxt = findViewById(R.id.LogInButton);
        SignUpTxt = findViewById(R.id.SignUp_Clicked);
        loadingTxt =findViewById(R.id.progressBar_loading_LogIN);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){

        }

        LogInTxt.setOnClickListener(v->{
            AuthenticateUser();
        });


        SignUpTxt.setOnClickListener(v-> {
            startActivity(new Intent(Login_Page.this , CreateAccountAcitivity.class));
            finish();
        });


    }

    private void AuthenticateUser(){
        String email = emailTxt.getText().toString().trim();
        String password = passwordTxt.getText().toString().trim();

        boolean valid = vallidateDetails(email , password);
        if(!valid){
            return;
        }
        LogInFIrebase(email , password);


    }

    private void LogInFIrebase(String email , String  password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    //is SucessFull
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if ( snapshot.child("fullName").exists()
                                    && snapshot.child("gender").exists() && snapshot.child("age").exists()
                                    && snapshot.child("number").exists() && snapshot.child("birthDate").exists() && snapshot.child("profession").exists()){
                                Utility.toast(Login_Page.this , "Login Sucessfull");
                                startActivity(new Intent(Login_Page.this , HomeActivity.class));
                                finish();
                            }else {
                                Intent intent = new Intent(Login_Page.this , CreateActivityPart2.class);
                                HashMap<String , Object> map = new HashMap<>();
                                map.put("userID" , firebaseAuth.getCurrentUser().getUid());
                                map.put("email" , email);
                                map.put("password" , password);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid())
                                        .updateChildren(map);
                                //intent.putExtra("Ac1" , true);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else {
                    //login failed
                    Utility.toast(Login_Page.this , task.getException().getLocalizedMessage());
                    changeInProgress(false);
                }
            }
        });

    }


    private boolean vallidateDetails(String email, String password) {
        //Validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTxt.setError("Email is invalid!");
            return false;
        }else if (password.length() <= 8){
            passwordTxt.setError("Enter a strong password!");
            return false;
        }
        return true;
    }


    public void changeInProgress(boolean isProgress){
        if (isProgress){
            loadingTxt.setVisibility(View.VISIBLE);
            SignUpTxt.setVisibility(View.GONE);
        }else{
            SignUpTxt.setVisibility(View.VISIBLE);
            loadingTxt.setVisibility(View.GONE);
        }
    }


}