package com.blucor.vsfarm.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blucor.vsfarm.R;
import com.blucor.vsfarm.activity.DrawerActivity;
import com.blucor.vsfarm.extra.Preferences;

import java.util.HashMap;
import java.util.Map;

public class OrderConfirmationFragment extends Fragment {

    //view
    View v;

    //Textview
    TextView tvViewOrder;
    TextView orderNumber,purchaseDate,billingMail,tvOrderTotal;
    Preferences preferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_order_confirmation, container, false);

        tvViewOrder=v.findViewById(R.id.tvViewOrder);
        orderNumber=v.findViewById(R.id.tvOrderNumber);
        purchaseDate=v.findViewById(R.id.tvDate);
        billingMail=v.findViewById(R.id.tvEmail);
        tvOrderTotal=v.findViewById(R.id.tvOrderTotal);

        preferences=new Preferences(getActivity());
        orderNumber.setText(preferences.get("order_id"));
        purchaseDate.setText(preferences.get("order_date"));
        billingMail.setText(preferences.get("useremail"));
        tvOrderTotal.setText(preferences.get("order_total"));

        DrawerActivity.tvHeaderText.setText("Order Confirmation");
        DrawerActivity.iv_menu.setImageResource(R.drawable.ic_back);

        DrawerActivity.iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DrawerActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
            }
        });

        //Setlistener
        tvViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragmentWithAnimation( new OrderFragment());

            }
        });

        //Activity
        DrawerActivity.ivCart.setVisibility(View.GONE);
        DrawerActivity.tvCount.setVisibility(View.GONE);
        return v;
    }

    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.commit();
    }
}