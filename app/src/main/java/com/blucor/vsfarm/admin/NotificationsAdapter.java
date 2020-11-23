package com.blucor.vsfarm.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.blucor.vsfarm.R;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    ArrayList productNames;
    ArrayList productImages;
    ArrayList productPrice;
    ArrayList productSize;
    Context context;

    public NotificationsAdapter(Context context, ArrayList productNames, ArrayList productImages,ArrayList productPrice,ArrayList productSize) {
        this.context = context;
        this.productNames = productNames;
        this.productImages = productImages;
        this.productPrice = productPrice;
        this.productSize = productSize;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_notification, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(productNames.get(position).toString());

        
        holder.image.setImageResource((Integer) productImages.get(position));
        holder.price.setText(productPrice.get(position).toString());
        holder.size.setText(productSize.get(position).toString());
    }
    @Override
    public int getItemCount() {
        return productNames.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,size,price;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvnotifyName);
            image = itemView.findViewById(R.id.notification_item_image);
            size = itemView.findViewById(R.id.tvSz);
            price = itemView.findViewById(R.id.price);

        }
    }
}
