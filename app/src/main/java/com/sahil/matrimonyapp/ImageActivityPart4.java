package com.sahil.matrimonyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;
import com.prathamesh.matrimonyapp.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageActivityPart4 extends AppCompatActivity {



    UploadTask uploadTask;

    StorageReference storageReference;

    FirebaseUser firebaseUser;

    DatabaseReference refUser;

    ImageView cuView;

    private Uri imageUri;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;

    private ImageView delete1;
    private ImageView delete2;
    private ImageView delete3;
    private ImageView delete4;

    private String url;

    private String image1url;
    private String image2url;
    private String image3url;
    private String image4url;

    private Button save;
    private Button prev;

    private List<String> imageUrl;
    private List<Integer> imageId;

    private int imageDataInt;
    private String imageDataUrl;
    private int pos;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_part4);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        refUser = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        storageReference = FirebaseStorage.getInstance().getReference().child("Upoads");

        imageUrl = new ArrayList<>();
        imageId = new ArrayList<>();

        image1 = findViewById(R.id.image1User);
        image2 = findViewById(R.id.image2User);
        image3 = findViewById(R.id.image3User);
        image4 = findViewById(R.id.image4User);

        delete1 = findViewById(R.id.image1Delete);
        delete2 = findViewById(R.id.image2Delete);
        delete3 = findViewById(R.id.image3Delete);
        delete4 = findViewById(R.id.image4Delete);

        save = findViewById(R.id.Next3CreateAcc);
        prev = findViewById(R.id.Prev3CreateAcc);



        getDetailsFromFirebase();

        image1.setOnClickListener(v->{
            CropImage.activity().setCropShape(CropImageView.CropShape.RECTANGLE)
                    .start(ImageActivityPart4.this);
        });

        image2.setOnClickListener(v->{
            CropImage.activity().setCropShape(CropImageView.CropShape.RECTANGLE)
                    .start(ImageActivityPart4.this);
        });

        image3.setOnClickListener(v->{
            CropImage.activity().setCropShape(CropImageView.CropShape.RECTANGLE)
                    .start(ImageActivityPart4.this);
        });

        image4.setOnClickListener(v->{
            CropImage.activity().setCropShape(CropImageView.CropShape.RECTANGLE)
                    .start(ImageActivityPart4.this);
        });


        delete1.setOnClickListener(v->{
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("imagesUser").child("image1").removeValue();
        });

        delete2.setOnClickListener(v->{
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("imagesUser").child("image2").removeValue();
        });

        delete3.setOnClickListener(v->{
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("imagesUser").child("image3").removeValue();
        });

        delete4.setOnClickListener(v->{
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("imagesUser").child("image4").removeValue();
        });


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ImageActivityPart4.this , CreateActivityPart3.class));
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDetails();
            }
        });
    }


    private void uploadDetails(){
        Intent intent = new Intent(ImageActivityPart4.this , HomeActivity.class);
        startActivity(intent);
        finish();
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
                        String name;
                        Uri dowloadUri = (Uri) task.getResult();
                        if (image1url == null){
                            image1url = dowloadUri.toString();
                            cuView = image1;
                            imageDataInt = image1.getId();
                            url = image1url;
                            name = "image1";
                        }else if (image2url == null){
                            image2url = dowloadUri.toString();
                            cuView = image2;
                            imageDataInt = image2.getId();
                            url = image2url;
                            name = "image2";
                        }else if (image3url == null) {
                            image3url = dowloadUri.toString();
                            cuView = image3;
                            imageDataInt = image1.getId();
                            url = image3url;
                            name = "image3";
                        }else if (image4url == null) {
                            image4url = dowloadUri.toString();
                            cuView = image4;
                            imageDataInt = image4.getId();
                            url = image4url;
                            name = "image4";
                        }else {
                            return;
                        }
                        Picasso.get().load(url).into(cuView);
                        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid())
                                .child("imagesUser").child(name).setValue(url);
                        FirebaseDatabase.getInstance().getReference().child("Images").child(firebaseUser.getUid()).child(name).setValue(url);
                        pd.dismiss();

                    } else {
                        Toast.makeText(ImageActivityPart4.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(ImageActivityPart4.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDetailsFromFirebase(){


        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("imagesUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("image1").exists()){
                    image1url = snapshot.child("image1").getValue().toString();
                    Log.d("image1" , image1url +"");
                    Picasso.get().load(image1url).into(image1);
                }
                if (snapshot.child("image2").exists()) {
                    image2url = snapshot.child("image2").getValue().toString();
                    Picasso.get().load(image2url).into(image2);
                }
                if (snapshot.child("image3").exists()) {
                    image3url = snapshot.child("image3").getValue().toString();
                    Picasso.get().load(image3url).into(image3);
                }
                if (snapshot.child("image4").exists()) {
                    image4url = snapshot.child("image4").getValue().toString();
                    Picasso.get().load(image4url).into(image4);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

//    private void getImageUrl(){
//        refUser.child("imagesUser").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.child("image1url").exists()){
//                    image1url = snapshot.child("image1url").getValue().toString();
//                }
//                if (snapshot.child("image2url").exists()){
//                    image2url = snapshot.child("image2url").getValue().toString();
//                }
//                if (snapshot.child("image3url").exists()){
//                    image3url = snapshot.child("image3url").getValue().toString();
//                }
//                if (snapshot.child("image4url").exists()){
//                    image4url = snapshot.child("image4url").getValue().toString();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void loadImage(int imgId){
//        for (String j : imageUrl){
//            if (image1url == j){
//                imageDataUrl = j;
//            }
//            if (image2url == j){
//                imageDataUrl = j;
//            }
//            if (image3url == j){
//                imageDataUrl = j;
//            }
//            if (image4url == j){
//                imageDataUrl = j;
//            }
//        }
//
//        ImageView FireBaseimageView = findViewById(imageDataInt);
//        if (imageDataUrl != null){
//            Picasso.get().load(imageDataUrl).into(FireBaseimageView);
//        }else {
//
//        }
//    }

}