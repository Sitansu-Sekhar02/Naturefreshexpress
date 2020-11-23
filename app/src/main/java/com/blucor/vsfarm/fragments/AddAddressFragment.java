package com.blucor.vsfarm.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.blucor.vsfarm.activity.DrawerActivity;
import com.blucor.vsfarm.activity.Utils;
import com.blucor.vsfarm.extra.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class AddAddressFragment extends Fragment {

    public static final String update_address="http://vsfastirrigation.com/webservices/updateuser_details.php";
    EditText EdName,EdContact,EdAddress;
    Button btnSave;
    Preferences preferences;
    Dialog dialog;


    //view
    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v=inflater.inflate(R.layout.fragment_add_address, container, false);

        //setvalue
        DrawerActivity.tvHeaderText.setText("Add Address");
        DrawerActivity.iv_menu.setImageResource(R.drawable.ic_back);

        preferences=new Preferences(getActivity());

        EdName=v.findViewById(R.id.edName);
        EdAddress=v.findViewById(R.id.edAddress);
        EdContact=v.findViewById(R.id.edContact);
        btnSave=v.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EdContact.getText().toString().trim().length() == 0) {
                    EdContact.setError("Required Mobile number");
                    EdContact.requestFocus();

                } else if (EdAddress.getText().toString().trim().length() == 0) {
                    EdAddress.setError("Required address ");
                    EdAddress.requestFocus();

                } else {
                    if (Utils.isNetworkConnectedMainThred(getActivity())) {
                        UpdateAddress();
                        ProgressForAddess();
                        dialog.show();
                    } else {
                        Toasty.error(getActivity(), "No Internet Connection!", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });


        //Onclick listener
        DrawerActivity.iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithAnimation(new CheckoutFragment());
            }
        });
        return v;

    }

    private void ProgressForAddess() {
        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
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

    private void UpdateAddress() {
        StringRequest request = new StringRequest(Request.Method.POST,update_address, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.cancel();
                Log.e("address",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if(jsonObject.getString("success").equalsIgnoreCase("1"))
                    {

                        preferences.set("address",jsonObject.getString("address"));
                        preferences.commit();
                        replaceFragmentWithAnimation(new CheckoutFragment());
                        Toasty.success(getActivity(),"Address Update Successfully",Toast.LENGTH_SHORT).show();
                    }
                    //replaceFragmentWithAnimation(new CheckoutFragment());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                Log.e("error_response", "" + error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("user_id",preferences.get("user_id"));
                parameters.put("contact", EdContact.getText().toString());
                parameters.put("address", EdAddress.getText().toString());
                return parameters;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

   /* public class SpinnerAdapter extends ArrayAdapter<HashMap<String, String>> {

        ArrayList<HashMap<String, String>> list;

        public SpinnerAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String, String>> list) {

            super(context, textViewResourceId, list);

            this.list = list;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View row = inflater.inflate(R.layout.spinner_layout, parent, false);
            TextView label = (TextView) row.findViewById(R.id.tvName);
            //label.setTypeface(typeface3);
            label.setText(list.get(position).get("key"));
            return row;
        }
    }*/

    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.commit();
    }
}