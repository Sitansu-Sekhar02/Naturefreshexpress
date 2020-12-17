package com.blucor.vsfarm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.blucor.vsfarm.R;
import com.blucor.vsfarm.extra.Preferences;

public class SplashActivity extends AppCompatActivity {

    //Imageview
    ImageView img;
    TextView ivAppName;

    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        img= (ImageView)findViewById(R.id.image);

        preferences=new Preferences(this);
        ivAppName = (TextView)findViewById(R.id.ivAppName);
        Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
        img.startAnimation(aniSlide);

        Animation fadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        ivAppName.startAnimation(fadein);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(preferences.get("user_id").isEmpty())
                {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if (preferences.get("usertype").equals("Admin")){

                        Intent i = new Intent(SplashActivity.this, AdminDashActivity.class);
                        startActivity(i);
                    }else{

                        Intent intent = new Intent(SplashActivity.this, DrawerActivity.class);
                        startActivity(intent);
                    }
                }

                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                finish();
            }
        }, 3000);
    }
}