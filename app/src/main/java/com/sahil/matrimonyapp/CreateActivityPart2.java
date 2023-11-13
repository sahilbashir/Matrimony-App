package com.sahil.matrimonyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prathamesh.matrimonyapp.R;
import com.sahil.matrimonyapp.Model.User;
import com.sahil.matrimonyapp.Utility.Utility;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

public class CreateActivityPart2 extends AppCompatActivity {


    private Uri imageUri;
    private Bundle bundle;
    private User userFrom1;

    HashMap<String , Object> map;


    private EditText fullName;
    private EditText occupation;
    private EditText adress;
    private EditText number;
    private Switch aSwitch;


    private int useryear;
    private int cuyear;
    private String birthdate;
    private int age;
    private int isMarried;

    private TextView displayBD;
    private TextView displayAge;
    private Button selectAge;

    private DatePickerDialog datePickerDialog;

    private ImageView dp;
    private TextView changeDp;
    private Button male;
    private Button female;

    private Button next;

    private String gender;


    private Task uploadTask;
    private DatabaseReference ref;
    //private DatabaseReference refUser;
    private String id;

    private FirebaseUser firebaseUser;
    private StorageReference storageReference;

    private String url;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_part2);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        id = firebaseUser.getUid();

        //refUser = ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        storageReference = FirebaseStorage.getInstance().getReference().child("Upoads");

        userFrom1 = new User();

        map = new HashMap<>();

//        bundle = getIntent().getExtras();
//        boolean us = bundle.getBoolean("Ac1" , false);
//        if(us != true){
//            userFrom1 = new User();
//        }else {
//            getDetailsFromFireBase();
//        }


        ref.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("imageUrl").exists()){
                }else {
                    String urlDefault = "https://firebasestorage.googleapis.com/v0/b/matrimony-app-6da14.appspot.com/o/default%2FlOGIN.png?alt=media&token=1d070a64-d5a1-468c-b3ea-51dd0201de9e";
                    ref.child(id).child("imageUrl").setValue(urlDefault);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        {
            fullName = findViewById(R.id.fullNameCreateAcc);
            occupation = findViewById(R.id.occupationNameCreateAcc);
            adress = findViewById(R.id.adressNameCreateAcc);
            number = findViewById(R.id.phoneNumCreateAcc);
            aSwitch = findViewById(R.id.switchActivity1);


            displayBD = findViewById(R.id.displayBirthDate);
            displayAge = findViewById(R.id.displayAge);

            fullName = findViewById(R.id.fullNameCreateAcc);
            selectAge = findViewById(R.id.selectDateUserCreateActivity);


            dp = findViewById(R.id.profileImageUserCreateActivity);
            changeDp = findViewById(R.id.textprofileImageUserCreateActivity);
            male = findViewById(R.id.maleUser);
            female = findViewById(R.id.femaleUser);
            next = findViewById(R.id.Next1CreateAcc54);
        }


        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //userFrom1 = snapshot.getValue(User.class);
                getDetailsFromFireBase();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        displayBD.setVisibility(View.GONE);
        displayAge.setVisibility(View.GONE);

        getDetailsFromFireBase();

        male.setOnClickListener(v -> {
            male.setSelected(true);
            female.setSelected(false);
            gender = "male";
        });

        female.setOnClickListener(v -> {
            male.setSelected(false);
            female.setSelected(true);
            gender = "female";
        });

        changeDp.setOnClickListener(v -> {
            CropImage.activity().setCropShape(CropImageView.CropShape.OVAL)
                    .start(CreateActivityPart2.this);
        });

        dp.setOnClickListener(v -> {
            CropImage.activity().setCropShape(CropImageView.CropShape.OVAL)
                    .start(CreateActivityPart2.this);
        });


        selectAge.setOnClickListener(v -> {
            //calendar.setMinDate(Long.parseLong("31-12-2005"));
            Calendar calendar = Calendar.getInstance();
            cuyear = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(CreateActivityPart2.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            i1 = i1 + 1;
                            useryear = i;
                            age = cuyear - useryear;
                            if (cuyear - useryear >= 18) {
                                birthdate = i2 + "-" + i1 + "-" + i;
                                displayBD.setText("Birth Date : " + birthdate);
                                displayBD.setVisibility(View.VISIBLE);
                                displayAge.setText("Age : " + age);
                                displayAge.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(CreateActivityPart2.this, "AGE IS LESS THAN 17 :)", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                    , cuyear, month, day);

            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();

        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
                finish();
            }
        });

    }

    private void getDetailsFromFireBase() {

        ref.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //userFrom1 = userdata;
                if (snapshot.child("fullName").exists())
                    fullName.setText(snapshot.child("fullName").getValue().toString());
                if (snapshot.child("profession").exists())
                    occupation.setText(snapshot.child("profession").getValue().toString());
                if (snapshot.child("adress").exists())
                    adress.setText(snapshot.child("adress").getValue().toString());
                if (snapshot.child("birthDate").exists()){
                    birthdate = snapshot.child("birthDate").getValue().toString();
                    displayBD.setVisibility(View.VISIBLE);
                    displayBD.setText("Birth Date : " + snapshot.child("birthDate").getValue().toString());
                }
                if (snapshot.child("age").exists()) {
                    displayAge.setVisibility(View.VISIBLE);
                    age = Integer.parseInt(snapshot.child("age").getValue().toString());
                    displayAge.setText("Age : " + snapshot.child("age").getValue().toString());
                }
                if (snapshot.child("gender").exists()) {
                    String gen = snapshot.child("gender").getValue().toString();
                    if (gen == "female") {
                        female.setSelected(true);
                        male.setSelected(false);
                        gender = "female";
                    } else {
                        female.setSelected(false);
                        male.setSelected(true);
                        gender = "male";
                    }
                }
                if (snapshot.child("gender").exists()) {
                    number.setText(snapshot.child("number").getValue().toString());
                }
                if (snapshot.child("isMarried").exists()) {
                    if (snapshot.child("isMarried").getValue().toString() == "true") {
                        aSwitch.setChecked(false);
                    } else {
                        aSwitch.setChecked(true);
                    }
                }
                if (snapshot.child("imageUrl").exists()) {
                    url = snapshot.child("imageUrl").getValue().toString();
                    userFrom1.setImageUrl(url);
                    Picasso.get().load(snapshot.child("imageUrl").getValue().toString()).into(dp);
                }

//                Log.d("User", "Inside Act" + userdata.getEmail());
//                Log.d("User", "Inside Act" + userdata.getGender());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            uploadImage();

        } else {
            Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveChanges() {

        if (fullName.getText().toString().trim().isEmpty()) {
            fullName.setError("Enter a Valid Name");
            return;
        } else {
            map.put("fullName" , fullName.getText().toString().trim());
        }

        if (birthdate == null) {
            Toast.makeText(this, "SelectYour Birth Date", Toast.LENGTH_SHORT);
            return;
        } else {

            map.put("birthDate" , birthdate);
            map.put("age" , age);
        }

        if (adress.getText().toString().trim().isEmpty()) {
            adress.setError("Enter a Valid Adress");
            return;
        } else {
            map.put("adress" ,adress.getText().toString().trim());
//            userFrom1.setAdress(adress.getText().toString().trim());
//            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("adress").setValue(adress.getText().toString());
        }

        if (occupation.getText().toString().trim().isEmpty()) {
//            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("profession").setValue("UNEMPLOYED");
//            userFrom1.setProfession("UNEMPLOYED");
            map.put("profession" , "UNEMPLOYED");
        } else {
//            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("profession").setValue(occupation.getText().toString());
//            userFrom1.setProfession(occupation.getText().toString().trim());
            map.put("profession" , occupation.getText().toString());
        }

        if (number.getText().toString().trim().isEmpty()) {
            map.put("number" , null);
//            userFrom1.setNumber(null);
//            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("number").setValue(number.getText());
        } else {
            map.put("number" , number.getText().toString().trim());
//            userFrom1.setNumber(number.getText().toString().trim());
//            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("number").setValue(number.getText().toString());
        }

        if (aSwitch.isSelected()) {
            //userFrom1.setMarried(false);
            map.put("isMarried" , false);
        } else {
            //userFrom1.setMarried(true);
            map.put("isMarried" , true);

        }

        if (gender == "male") {
            map.put("gender" , "male");
//            userFrom1.setGender("male");
//            FirebaseDatabase.getInstance().getReference().child("Gender").child("male").child(firebaseUser.getUid());

        } else if (gender == "female") {
            map.put("gender" , "female");
//            userFrom1.setGender("female");
//            FirebaseDatabase.getInstance().getReference().child("Gender").child("female").child(firebaseUser.getUid());

        } else {
            Toast.makeText(this, "select a gender", Toast.LENGTH_SHORT);
            return;
        }

        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid())
                .updateChildren(map);

        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("fullName").toString() != null) {

                }
                Intent intentNew = new Intent(CreateActivityPart2.this, CreateActivityPart3.class);
                startActivity(intentNew);

                //finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void uploadImage() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null) {
            StorageReference fileREf = storageReference.child(System.currentTimeMillis() + ".jpeg");
            uploadTask = fileREf.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileREf.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri dowloadUri = (Uri) task.getResult();
                        url = dowloadUri.toString();
                        userFrom1.setImageUrl(url);
                        Picasso.get().load(url).into(dp);
                        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid())
                                .child("imageUrl").setValue(url);
                        pd.dismiss();

                    } else {
                        Toast.makeText(CreateActivityPart2.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(CreateActivityPart2.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }




}