package com.example.sag14.application01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mBlogList;
    private String mEvent_Date=null;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseInvite;
    private FirebaseAuth mAuth;
    int i=0;
    Boolean[] keys;
    int keyCount=0;
    private String uid;
    String name="";
    private DatabaseReference mDatabasePost,dummy,mDatabaseUser;
    private Query mQueryPost;
boolean keyss=false;
    int ijk=0;
ArrayList<Integer> p = new ArrayList<>();
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Event");
        //mDatabase.keepSynced(true);
        mDatabaseInvite=FirebaseDatabase.getInstance().getReference().child("Invited");


        uid = mAuth.getCurrentUser().getUid();

        if(uid==null){
            startActivity(new Intent(this, LoginActivity.class));
        }


    //Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG).show();

       // Log.i("Keys Added", uid);
SharedPreferences sp = getSharedPreferences("ep",MODE_PRIVATE);

Toast.makeText(getApplicationContext(),sp.getString("name","blablabla"),Toast.LENGTH_LONG).show();
        mQueryPost=mDatabase.orderByChild(uid).equalTo(sp.getString("name","blablabla"));

        mDatabasePost=FirebaseDatabase.getInstance().getReference().child("Event");





        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };




        mBlogList=(RecyclerView)findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));


        mDatabaseInvite.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                keyCount = (int) dataSnapshot.getChildrenCount();
                keys = new Boolean[(int) dataSnapshot.getChildrenCount()];

                //Log.i("Keys Added", uid);
                //keys = new Boolean[]{false};
                /*for (DataSnapshot ds : dataSnapshot.child(post_key).getChildren()) {




                    }*/
                }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        //if(dummy!=null) {
            FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                    Blog.class,
                    R.layout.blog_row,
                    BlogViewHolder.class,
                    mQueryPost


            ) {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                protected void populateViewHolder(final BlogViewHolder viewHolder, final  Blog model, final int position) {
                    final String post_key = getRef(position).getKey();
                    //Log.i("Keys Added", String.valueOf(getRef(position)));
                    uid = mAuth.getCurrentUser().getUid();

                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setImage(getApplicationContext(), model.getImage());
                    viewHolder.setUsername(model.getUsername());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent singleEventIntent = new Intent(MainActivity.this, EventSingleActivity.class);
                            singleEventIntent.putExtra("blog_id", post_key);
                            startActivity(singleEventIntent);


                        }
                    });

                    viewHolder.mAddInvites.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent Invite = new Intent(MainActivity.this, Invites.class);
                            Invite.putExtra("blog_id", post_key);
                            startActivity(Invite);

                        }
                    });
    }
};



            mBlogList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;


        private ImageButton mAddInvites;

        public BlogViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
            mAddInvites= (ImageButton) mView.findViewById(R.id.add_invites);
        }
        public void setTitle(String title){

            TextView post_title=(TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);

        }
        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible){
                param.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                param.width = RecyclerView.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            }else{
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
        public void setDesc(String desc){

            TextView post_desc=(TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);


        }
        public void setUsername(String username){
            TextView post_username =  (TextView) mView.findViewById(R.id.post_username);
            post_username.setText(username);

        }

        public void setImage(final Context ctx, final String image){

            final ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);

        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_add){

                startActivity(new Intent(MainActivity.this,PostActivity.class));
        }

        if(item.getItemId()==R.id.action_date){

           //
            // mEvent_Date = getIntent().getExtras().getString("eventDate");
            //Intent EventIntent = new Intent(MainActivity.this, Calender.class);
            //EventIntent.putExtra("eventDate", mEvent_Date);

            //startActivity(EventIntent);
            startActivity(new Intent(MainActivity.this,Calender.class));
        }

        if(item.getItemId()==R.id.action_logout){
            logout();

        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
        finish();
    }
}
