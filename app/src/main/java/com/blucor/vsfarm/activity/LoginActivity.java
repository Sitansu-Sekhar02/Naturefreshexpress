package com.blucor.vsfarm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blucor.vsfarm.R;
import com.blucor.vsfarm.extra.Preferences;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    //Preferences
    Preferences preferences;

    //Textview
    TextView tvSignupbtn;
    TextView tvLoginButton,tvForgotPassword;
    private EditText email, pass;
    Dialog dialog;

    public static final String URL = "http://vsfarma.blucorsys.in/userlogin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvSignupbtn = findViewById(R.id.tvSignupbtn);
        tvForgotPassword=findViewById(R.id.tvForgotPassword);

        tvSignupbtn.setOnClickListener(this);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        preferences = new Preferences(this);

        email = (EditText) findViewById(R.id.EmailEdit);
        pass = (EditText) findViewById(R.id.PasswordEdit);

        tvLoginButton = findViewById(R.id.tvLoginButton);
        tvLoginButton.setOnClickListener(this);


        tvLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().trim().length() == 0) {
                    email.setError("EmailId Required");
                    email.requestFocus();

                } else if (pass.getText().toString().trim().length() == 0) {
                    pass.setError("Password Required");
                    pass.requestFocus();


                } else {


                    if (Utils.isNetworkConnectedMainThred(LoginActivity.this)) {

                        ProgressDialog();
                        dialog.show();
                        LoginSuccess();
                    } else {
                        Toasty.error(LoginActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    }

                }
            }

        });

    }

    private void LoginSuccess() {
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    dialog.cancel();
                    Log.d("success", "onResponse: "+response);
                    pass.setText("");
                    String res=response;
                    try {
                        JSONObject object=new JSONObject(response);
                        String error=object.getString("error");

                        if(error.equals("false"))
                        {
                            JSONObject user=object.getJSONObject("user");
                            String user_type=object.getString("usertype");
                            String useremail=user.getString("email");
                            String password=user.getString("password");
                            String user_id=user.getString("user_id");
                            String name=user.getString("name");
                            String address=user.getString("address");
                            String contact=user.getString("contact");


                            preferences.set("user_id",user_id);
                            preferences.set("useremail",useremail);
                            preferences.set("password",password);
                            preferences.set("usertype",user_type);
                            preferences.set("name",name);
                            preferences.set("address",address);
                            preferences.set("contact",contact);

                            preferences.commit();

                            if (user_type.equals("Admin")){
                                Intent i=new Intent(LoginActivity.this,AdminDashActivity.class);
                                startActivity(i);

                            }else {
                                Intent in=new Intent(LoginActivity.this,DrawerActivity.class);
                                startActivity(in);

                            }
                        }
                        else{
                            Toasty.warning(getApplicationContext(), "Wrong userId or password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.cancel();
                    Toasty.error(LoginActivity.this, "Some error occurred -> " + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("email", email.getText().toString());
                    parameters.put("password", pass.getText().toString());

                    return parameters;
                }
            };
            RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
            rQueue.add(request);
        }

    public void ProgressDialog() {
        //Dialog
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
        case R.id.tvSignupbtn:
        startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
        overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
        break;

      /*  case R.id.tvForgotPassword:
            startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
            break; */
        }
    }
}
