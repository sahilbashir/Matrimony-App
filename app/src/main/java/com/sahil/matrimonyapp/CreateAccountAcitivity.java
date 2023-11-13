package com.sahil.matrimonyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prathamesh.matrimonyapp.R;
import com.sahil.matrimonyapp.Model.User;
import com.sahil.matrimonyapp.Utility.Utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreateAccountAcitivity extends AppCompatActivity {

    private EditText emailTxt,passwordTxt,confirmpassTxt;
    private Button createAccountTxt;
    private TextView logInTxt;
    private ProgressBar loadingTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_acitivity);


        emailTxt = findViewById(R.id.user_email_SignUp);
        passwordTxt = findViewById(R.id.user_password_SignUp);
        confirmpassTxt = findViewById(R.id.user_confirm_password_SignUp);
        createAccountTxt = findViewById(R.id.creating_account);
        logInTxt = findViewById(R.id.LogIn_Clicked);
        loadingTxt =findViewById(R.id.progressBar_loading_SignUp);


        createAccountTxt.setOnClickListener(v->{

            changeInProgress(false);
            createAcc();
        });

        logInTxt.setOnClickListener(v->{

            startActivity(new Intent(CreateAccountAcitivity.this , Login_Page.class ));
            finish();
        });


    }

    void createAcc(){
        String email = emailTxt.getText().toString().trim();
        //hideKeyboard(emailTxt);
        String password = passwordTxt.getText().toString().trim();
        //hideKeyboard(passwordTxt);
        String confirmPassword = confirmpassTxt.getText().toString().trim();
        //hideKeyboard(confirmpassTxt);

        boolean isValid = vallidateDetails(email , password , confirmPassword);
        if (!isValid)
            return;

        createAccountInFirebase(email , password);


    }

    private boolean vallidateDetails(String email, String password, String confirmPassword) {
        //Validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTxt.setError("Email is invalid!");
            return false;
        }else if (password.length() <= 8){
            passwordTxt.setError("Enter a strong password!");
            return false;
        }else if (!password.equals(confirmPassword)){
            confirmpassTxt.setError("Password doesn't match!");
            return false;
        }
        return true;
    }

    public void createAccountInFirebase(String email , String password){
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(CreateAccountAcitivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //sucessfull
                            Utility.toast(CreateAccountAcitivity.this , "Succesfully created account, Update Your Profile");
                            changeInProgress(false);
                            Intent intent = new Intent(CreateAccountAcitivity.this , CreateActivityPart2.class);
                            User user = new User();
                            user.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            user.setEmail(email);
                            user.setPassword(password);
                            user.setImageUrl("https://firebasestorage.googleapis.com/v0/b/matrimony-app-6da14.appspot.com/o/default%2FlOGIN.png?alt=media&token=1d070a64-d5a1-468c-b3ea-51dd0201de9e");
//                          intent.putExtra("Acticvity101" , user);
                            intent.putExtra("Ac1" , true);
                            intent.putExtra("Prev2" , false);
                            Utility.updateDatabase(user , user.getUserID());
                            startActivity(intent);
                            finish();

                        }else {
                            //faliure
                            Utility.toast(CreateAccountAcitivity.this , task.getException().getLocalizedMessage());
                            changeInProgress(false);
                        }
                    }
                });



    }

    public void changeInProgress(boolean isProgress){
        if (isProgress){
            loadingTxt.setVisibility(View.VISIBLE);
            createAccountTxt.setVisibility(View.GONE);
        }else{
            createAccountTxt.setVisibility(View.VISIBLE);
            loadingTxt.setVisibility(View.GONE);
        }
    }








    private void hideKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

}