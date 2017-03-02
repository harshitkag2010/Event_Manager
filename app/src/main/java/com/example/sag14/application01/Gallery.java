package com.example.sag14.application01;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Gallery extends AppCompatActivity {
    private static String mPost_key=null;
    private RecyclerView mImageList;
    private DatabaseReference mDatabase;
    private boolean mProcessLike=false;
    private DatabaseReference mDatabaseLike;
    private DatabaseReference mDatabaseCurrentEvent;
    private FirebaseAuth mAuth;
    private Query mQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mPost_key = getIntent().getExtras().getString("blog_id");
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Image");
       // mDatabase.keepSynced(true);
        mDatabaseCurrentEvent= FirebaseDatabase.getInstance().getReference().child("Image");
        mQuery=mDatabaseCurrentEvent.orderByChild("key").equalTo(mPost_key);
        mDatabaseLike=FirebaseDatabase.getInstance().getReference().child("Likes");
            mImageList=(RecyclerView) findViewById(R.id.image_list);
            mImageList.setHasFixedSize(true);
            mImageList.setLayoutManager(new LinearLayoutManager(this));





    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ImageRow,ImageViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<ImageRow, ImageViewHolder>(
                ImageRow.class,
                R.layout.image_row,
                ImageViewHolder.class,
                mQuery


        ) {
            @Override
            protected void populateViewHolder(ImageViewHolder viewHolder, ImageRow model, int position) {

                final String key = getRef(position).getKey();

                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setLikeBtn(key);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Gallery.this, key, Toast.LENGTH_LONG).show();
                    }
                });

                viewHolder.mLikebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mProcessLike = true;


                            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (mProcessLike) {

                                    if (dataSnapshot.child(key).hasChild(mAuth.getCurrentUser().getUid())) {
                                        mDatabaseLike.child(key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mProcessLike = false;
                                    } else {

                                        mDatabaseLike.child(key).child(mAuth.getCurrentUser().getUid()).setValue("Random");
                                        mProcessLike = false;
                                    }

                                }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }


                            });



                    }
                });
            }
        };

                mImageList.setAdapter(firebaseRecyclerAdapter);



    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageButton mLikebtn;
        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;
        public ImageViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            mLikebtn=(ImageButton) mView.findViewById(R.id.like_btn);
            mDatabaseLike= FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth=FirebaseAuth.getInstance();
        }

        public void setLikeBtn(final String key){
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(key).hasChild(mAuth.getCurrentUser().getUid())){
                            mLikebtn.setImageResource(R.mipmap.red);
                        }else{
                            mLikebtn.setImageResource(R.mipmap.like);



                        }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void setImage(final Context ctx,final  String image){
            final ImageView post_image =(ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.gallery,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_add){

            Intent singleEventIntent = new Intent(Gallery.this, GalleryPostActivity.class);
            singleEventIntent.putExtra("blog_id", mPost_key);
            startActivity(singleEventIntent);


        }




        return super.onOptionsItemSelected(item);
    }
}
