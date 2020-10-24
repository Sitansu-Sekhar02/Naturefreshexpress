package com.blucor.vsfarm.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.blucor.vsfarm.R;

import java.util.ArrayList;
import java.util.Arrays;

public class Notifications extends AppCompatActivity {
    ArrayList productNames = new ArrayList<>(Arrays.asList("Notification 1", "Notification 2", "Notification 3", "Notification 4"));
    ArrayList productImages = new ArrayList<>(Arrays.asList(R.drawable.pipediv, R.drawable.pipediv, R.drawable.pipediv, R.drawable.pipediv));
    ArrayList productPrice=new ArrayList<>(Arrays.asList("\u20B9 499","\u20B9 499","\u20B9 499","\u20B9 499"));
    ArrayList productSize = new ArrayList<>(Arrays.asList("20kg", "20kg", "20kg", "20kg"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_product_items);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.notification);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NotificationsAdapter customAdapter = new NotificationsAdapter(Notifications.this, productNames,productImages,productPrice,productSize);
        recyclerView.setAdapter(customAdapter);
    }
}
