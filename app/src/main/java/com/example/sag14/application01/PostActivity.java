package com.example.sag14.application01;


import android.app.ProgressDialog;

import android.content.Intent;

import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostActivity extends AppCompatActivity {


    private ImageButton mSelectImage;
    private static final int GALLERY_REQUEST=1;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitBtn;
    private DatabaseReference mDatabaseActive;
    private StorageReference mStorage;
    private Uri mImageUri=null;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    public  static int timech,datech,datech1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mAuth=FirebaseAuth.getInstance();
        mDatabaseActive=FirebaseDatabase.getInstance().getReference().child("Invited");
        mCurrentUser=mAuth.getCurrentUser();
        mDatabaseUsers=FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mStorage= FirebaseStorage.getInstance().getReference();
        mSelectImage=(ImageButton) findViewById(R.id.imageSelect);

        mPostTitle=(EditText) findViewById(R.id.titleField);
        mPostDesc=(EditText) findViewById(R.id.descField);
        mSubmitBtn=(Button) findViewById(R.id.submitBtn);
        mProgress = new ProgressDialog(this);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Event");




        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent= new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);



            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });


    }



    private void startPosting() {
        mProgress.setMessage("Posting to Event...");
        final String setTime = TimeSettings.st.toString();
        final String setDate = DateSettings.st1.toString();
        final String setDateE = DateSettings1.st2.toString();

        final String title_val=mPostTitle.getText().toString().trim();
       final  String desc_val=mPostDesc.getText().toString().trim();
        if(!TextUtils.isEmpty(title_val)&& !TextUtils.isEmpty(desc_val)&&mImageUri!=null && timech==6 && datech==6 && datech1==6){
            mProgress.show();
                StorageReference filepath= mStorage.child("Events_Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUrl= taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = mDatabase.push();

                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("title").setValue(title_val);
                            //Log.i("addsd",newPost.getKey());

                            mDatabaseActive.child(newPost.getKey()).child(mCurrentUser.getUid()).setValue(dataSnapshot.child("name").getValue());

                            //mDatabaseActive.addValueEventListener(new )
                            newPost.child(mCurrentUser.getUid()).setValue(dataSnapshot.child("name").getValue());
                            newPost.child("desc").setValue(desc_val);
                            newPost.child("Settime").setValue(setTime);
                            newPost.child("startdate").setValue(setDate);
                            newPost.child("enddate").setValue(setDateE);
                            newPost.child("image").setValue(downloadUrl.toString());
                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                       // Intent EventIntent = new Intent(PostActivity.this, MainActivity.class);
                                       // EventIntent.putExtra("eventDate", millis);

                                       // startActivity(EventIntent);
                                        startActivity(new Intent(PostActivity.this,MainActivity.class));

                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                    mProgress.dismiss();


                }
            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK) {
            mImageUri= data.getData();
            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                mSelectImage.setImageURI(resultUri);
                mImageUri=resultUri;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {


                Exception error = result.getError();
            }
        }





        }

    public void setDate(View view)
    {
        PickerDialogs pickerDialogs= new PickerDialogs();

        pickerDialogs.show(getSupportFragmentManager(),"date_picker");
    }

    public void setDateE(View view)
    {
        PickerDialogs1 pickerDialogs= new PickerDialogs1();

        pickerDialogs.show(getSupportFragmentManager(),"date_picker");
    }


    public void showTime(View view)
    {
        DialogHandler dialogHandler= new DialogHandler();
        dialogHandler.show(getSupportFragmentManager(), "time_picker");

    }





}

