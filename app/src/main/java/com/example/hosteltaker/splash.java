package com.example.hosteltaker;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.hosteltaker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash extends AppCompatActivity {


    FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mFirebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        new CountDownTimer(2000,1000){
            @Override
            public void onTick(long millisUntilFinished)
            {
            }

            @Override
            public void onFinish(){
                Intent intent;

                if (mFirebaseUser!=null)       // 3rd  for Auto Login
                    intent=new Intent(splash.this,MainActivity.class);
                else
                    intent=new Intent(splash.this,LoginActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }.start();
    }
}
