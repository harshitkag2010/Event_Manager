package com.example.sag14.application01;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class GalleryPostActivity extends AppCompatActivity {

    private ImageButton mSelectImage;
    private static final int GALLERY_INTENT=1;
    private Uri mImageUri=null;
    private Button mSubmitBtn;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private String mPost_key=null;
    private String key=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_post);
        key = getIntent().getExtras().getString("blog_id");
        mPost_key=key;
        Toast.makeText(getApplicationContext(),mPost_key,Toast.LENGTH_SHORT).show();
        mSubmitBtn=(Button) findViewById(R.id.submitBtn);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Image");
        mSelectImage=(ImageButton) findViewById(R.id.imageSelect);
        mProgress=new ProgressDialog(this);
        mStorage= FirebaseStorage.getInstance().getReference();
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent =  new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_INTENT);
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });

    }

    private void startPosting() {
                mProgress.setMessage("Posting....");
        mProgress.show();
        if(mImageUri!= null){

            StorageReference filepath=mStorage.child("EventImages").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUri= taskSnapshot.getDownloadUrl();

                    final DatabaseReference newPost = mDatabase.push();

                    newPost.child("image").setValue(downloadUri.toString());
                    newPost.child("key").setValue(mPost_key);



                    mProgress.dismiss();
                    startActivity(new Intent(GalleryPostActivity.this,Gallery.class));

                }
            });
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){
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
}
