package com.example.sag14.application01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Invites extends AppCompatActivity {

    private RecyclerView mUserList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseActive,mDatabaseEvent;

    private boolean mProcessClick=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);

       // final String post_key=getIntent().getExtras().getString("blog_id");

        //Toast.makeText(Invites.this,post_key, Toast.LENGTH_LONG).show();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseActive=FirebaseDatabase.getInstance().getReference().child("Invited");
        mDatabaseEvent=FirebaseDatabase.getInstance().getReference().child("Event");
        mAuth=FirebaseAuth.getInstance();
        mUserList=(RecyclerView) findViewById(R.id.user_list);

        mUserList.setHasFixedSize(true);
        mUserList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<UserRow,UserViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<UserRow, UserViewHolder>(
            UserRow.class,
                R.layout.user_row,
                UserViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, UserRow model, int position) {
                final String post_key=getIntent().getExtras().getString("blog_id");
                final String user_key=getRef(position).getKey();

                viewHolder.setname(model.getName());



                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProcessClick=true;


                        mDatabaseEvent.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (mProcessClick) {


                                    if (dataSnapshot.child(post_key).hasChild(user_key)) {

                                        mDatabaseEvent.child(post_key).child(user_key).removeValue();
                                        mProcessClick=false;
                                    } else {

                                        mDatabase.child(user_key).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                String user=(String)dataSnapshot.child("name").getValue();
                                                mDatabaseEvent.child(post_key).child(user_key).setValue(user);
//                                                mDatabaseEvent.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue(dataSnapshot.child("name").getValue());
                                                mProcessClick=false;
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                            mDatabaseActive.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (mProcessClick) {


                                        if (dataSnapshot.child(post_key).hasChild(user_key)) {

                                            mDatabaseActive.child(post_key).child(user_key).removeValue();
                                            mProcessClick=false;
                                        } else {

                                            mDatabase.child(user_key).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    String user=(String)dataSnapshot.child("name").getValue();
                                                   mDatabaseActive.child(post_key).child(user_key).setValue(user);
                                                    mProcessClick=false;
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


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

        mUserList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageButton mActivebtn;
        public UserViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            mActivebtn=(ImageButton)mView.findViewById(R.id.active_btn);
        }

        public void setname( String name){

            TextView post_username= (TextView) mView.findViewById(R.id.post_username);
            post_username.setText(name);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.invites_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_back){

            startActivity(new Intent(Invites.this,MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
