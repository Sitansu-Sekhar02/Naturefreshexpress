package com.blucor.vsfarm.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blucor.vsfarm.R;
import com.blucor.vsfarm.admin.Notifications;
import com.blucor.vsfarm.extra.Preferences;

public class AdminDashActivity extends AppCompatActivity {
    Button addProduct,categoryProduct;
    ImageView notify,logout;
    public static int count;

    TextView tvCount;
    public static int backPressed = 0;
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);

        addProduct = findViewById(R.id.btnProduct_add);
        categoryProduct = findViewById(R.id.btn_categoryProduct);


        //editProduct=findViewById(R.id.btnProduct_edit);

        notify = findViewById(R.id.notify_admin);
        logout=findViewById(R.id.admin_logout);

        tvCount = findViewById(R.id.tvCount);

        try {
            tvCount.setText(String.valueOf(count));
        } catch (Exception e) {

        }

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //replaceFragmentWithAnimation(new AddProductFragment());

                Intent i =new Intent(AdminDashActivity.this, AddCategory.class);
                startActivity(i);
              /*  FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction4 = fm.beginTransaction();
                AddProductFragment admin = new AddProductFragment();
                fragmentTransaction4.replace(R.id.container_main, admin);
                fragmentTransaction4.commit();*/
            }
        });
        categoryProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(AdminDashActivity.this, AddProductToCategory.class);
                startActivity(i);
            }
        });

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                tvCount.setText(String.valueOf(count));
                Intent intent = new Intent(AdminDashActivity.this, Notifications.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutAdmin();
            }
        });
    }

    private void LogoutAdmin() {
        final Dialog dialog = new Dialog(AdminDashActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertadminlogout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);


        //findId
        TextView tvYes = (TextView) dialog.findViewById(R.id.tvOkadmin);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvcanceladmin);
        TextView tvReason = (TextView) dialog.findViewById(R.id.textView2);
        TextView tvAlertMsg = (TextView) dialog.findViewById(R.id.tvAlertMsg);

        //set value
        tvAlertMsg.setText("Confirmation Alert..!!!");
        tvReason.setText("Are you sure you want to logout?");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        //set listener
        tvYes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
//                pref.set(AppSettings.CustomerID, "");
//                pref.commit();
                preferences.set("user_id","");
                preferences.commit();
                finishAffinity();
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void close(View view){
        finish();
    }

    @Override
    public void onBackPressed() {
        backPressed = backPressed + 1;
        if (backPressed == 1) {
            Toast.makeText(AdminDashActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new CountDownTimer(5000, 1000) { // adjust the milli seconds here
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    backPressed = 0;
                }
            }.start();
        }
        if (

                backPressed == 2) {
            backPressed = 0;
            finishAffinity();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.admin_fragment_container, fragment);
        transaction.commit();
    }


}

