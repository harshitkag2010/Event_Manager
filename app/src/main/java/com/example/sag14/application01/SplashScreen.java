package com.example.sag14.application01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.content.Intent;
import android.os.Handler;

import com.felipecsl.gifimageview.library.GifImageView;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;



public class SplashScreen extends AppCompatActivity {

    private GifImageView gifImageView;
    //private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        gifImageView=(GifImageView) findViewById(R.id.gifImageView);
       // progressBar=(ProgressBar)findViewById(R.id.progressBar);

        //progressBar.setVisibility(progressBar.VISIBLE);

        try{
            InputStream inputStream = getAssets().open("giphy.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();




        } catch (IOException e) {
            e.printStackTrace();
        }

        //Wait for 3 seconds and start Activity Main
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                SplashScreen.this.finish();
            }
        },5000); // 3000 = 3seconds


    }
}
