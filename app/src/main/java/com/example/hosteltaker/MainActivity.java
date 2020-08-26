package com.example.hosteltaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;
public class MainActivity extends AppCompatActivity {


    CircleImageView profile_image;
    TextView username;
    FirebaseAuth auth;
    FirebaseUser current_user;                                               // 1st we do this.

    String current_user_ID;
    DatabaseReference rootRef,buddiesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile_image=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);

        auth=FirebaseAuth.getInstance();
        current_user=auth.getCurrentUser();                                  // 2nd we intiallize.
        current_user_ID=current_user.getUid();
        rootRef= FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        rootRef.child("Users").child(current_user_ID).addValueEventListener(new ValueEventListener() {    // 5th we fetch the data
            @Override                                                                                // and then show username
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {                          //  and image.
                if(dataSnapshot.hasChild("name")){
                    username.setText(dataSnapshot.child("name").getValue().toString());
                }

                if(dataSnapshot.hasChild("image")){
                    Picasso.get().load(dataSnapshot.child("image").getValue().toString()).placeholder(R.drawable.default_profile_image).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        CardView cardView_hosteller=findViewById(R.id.cardview_hosteller);
        CardView cardView_room=findViewById(R.id.cardview_room);
        CardView cardView_Attendance=findViewById(R.id.cardview_Attendance);


        cardView_hosteller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,hostellerActivity.class));
            }
        });
        cardView_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,roomActivity.class));
            }
        });
        cardView_Attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AttendanceActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);       // 8th now we are creating above menu.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {          // 8th now we are creating above menu.
        switch (item.getItemId()){

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        return false;
    }

    @Override
    protected void onStart() {               //  3rd (Actually at the beggining).
        super.onStart();
        if(current_user!=null){                // means if you login first time then you to have to set your profile.
            verifyUser();
        }
    }

    private void verifyUser() {
        rootRef.child("Users").child(current_user_ID).addListenerForSingleValueEvent(new ValueEventListener() {  // make 1st child Users.
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {                  // 4th to set your profile.
                if(!dataSnapshot.child("name").exists()){
                    Intent intent=new Intent(MainActivity.this,ProfileSetting.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
