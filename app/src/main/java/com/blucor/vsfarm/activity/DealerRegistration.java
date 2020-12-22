package com.blucor.vsfarm.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.blucor.vsfarm.R;
import com.blucor.vsfarm.extra.Preferences;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class DealerRegistration extends AppCompatActivity {

    //Textview
    TextView tvSignIn;
    TextView tvRegisterButton;

    private EditText editTextName;
    private EditText editEmail;
    private EditText editContact;
    private EditText editAddress;
    private EditText editTextState;
    private EditText editTextPassword;
    private EditText editTextGstNo;


    AwesomeValidation awesomeValidation;
    Dialog dialog;

    private Button buttonRegister;
    public static final String dealer_URL = "http://vsfastirrigation.com/webservices/dealer_registration.php";

    //Preferences
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_sign_up);
        tvSignIn = findViewById(R.id.tvSignIn);
        editTextName = (EditText) findViewById(R.id.etName);
        editEmail = (EditText) findViewById(R.id.etEmail);
        editContact = (EditText) findViewById(R.id.etContact);
        editAddress = (EditText) findViewById(R.id.etAddress);
        editTextPassword = (EditText) findViewById(R.id.etPassword);
        editTextGstNo = (EditText) findViewById(R.id.etGstNo);
        editTextState = (EditText) findViewById(R.id.etState);

        buttonRegister = (Button) findViewById(R.id.tvRegisterButton);

        tvRegisterButton = findViewById(R.id.tvRegisterButton);

        //validate form
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.etName, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        awesomeValidation.addValidation(this, R.id.etContact, "[5-9]{1}[0-9]{9}$", R.string.invalid_PhoneNumber);
        awesomeValidation.addValidation(this, R.id.etEmail, Patterns.EMAIL_ADDRESS, R.string.invalid_Email);
        awesomeValidation.addValidation(this, R.id.etAddress, RegexTemplate.NOT_EMPTY, R.string.invalid_address);
        awesomeValidation.addValidation(this, R.id.etState, RegexTemplate.NOT_EMPTY, R.string.invalid_state);
        awesomeValidation.addValidation(this, R.id.etPassword, RegexTemplate.NOT_EMPTY, R.string.invalid_Password);
        awesomeValidation.addValidation(this, R.id.etGstNo, RegexTemplate.NOT_EMPTY, R.string.invalid_Password);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (awesomeValidation.validate()) {
                    Log.d("success", "onResponse: ");
                    if (Utils.isNetworkConnectedMainThred(DealerRegistration.this)) {
                        Validation();
                        ProgressForSignup();
                        dialog.show();
                    } else {
                        Toasty.error(DealerRegistration.this,"No Internet Connection!", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Log.d("error", "onResponse: ");
                }
            }

        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DealerRegistration.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
            }
        });

    }

    private void ProgressForSignup() {
        dialog = new Dialog(DealerRegistration.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_for_cart);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
    }

    private void Validation() {
            StringRequest request = new StringRequest(Request.Method.POST, dealer_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.cancel();
                    if (response.equals(""))
                        Log.d("success", "onResponse: " + response);
                     startActivity(new Intent(DealerRegistration.this, LoginActivity.class));
                     Toast.makeText(DealerRegistration.this, response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.cancel();
                    Toasty.error(DealerRegistration.this, "Some error occurred -> " + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("name", editTextName.getText().toString());
                    parameters.put("email", editEmail.getText().toString());
                    parameters.put("contact", editContact.getText().toString());
                    parameters.put("address", editAddress.getText().toString());
                    parameters.put("state",editTextState.getText().toString());
                    parameters.put("gst_number",editTextGstNo.getText().toString());
                    parameters.put("password", editTextPassword.getText().toString());
                    parameters.put("user_type","dealer");

                    return parameters;
                }
            };
            RequestQueue rQueue = Volley.newRequestQueue(DealerRegistration.this);
            rQueue.add(request);
        }
}





