package com.blucor.vsfarm.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blucor.vsfarm.Model.CartItem;
import com.blucor.vsfarm.Model.CategoryProducts;
import com.blucor.vsfarm.Model.OrderDetails;
import com.blucor.vsfarm.R;
import com.blucor.vsfarm.activity.DrawerActivity;
import com.blucor.vsfarm.activity.Utils;
import com.blucor.vsfarm.extra.Preferences;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class OrderFragment extends Fragment {

    //View
    View v;

    //GridLayoutManger
    GridLayoutManager mGridLayoutManager;
    public static final String order_details = "http://vsfarma.blucorsys.in/fetch_UserOrderDetails.php";
    private List<OrderDetails> userOrderList;
    OrderAdapter adapter;
    //Recycler
    RecyclerView recyclerView;
    Preferences preferences;
    Dialog dialog;
    LinearLayout llcartItem;
    LinearLayout empty;
    Button shopNow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_order,container, false);

        //initialize
        recyclerView=v.findViewById(R.id.recyclerView);
        llcartItem=v.findViewById(R.id.llcartItem);
        shopNow=v.findViewById(R.id.bAddNew);
        empty=v.findViewById(R.id.empty);


        userOrderList=new ArrayList<>();
        preferences=new Preferences(getActivity());


        //DrawerActivity
        DrawerActivity.marquee.setVisibility(View.GONE);
        DrawerActivity.rlsearchview.setVisibility(View.GONE);
        DrawerActivity.tvHeaderText.setText("Your Orders");
        DrawerActivity.iv_menu.setImageResource(R.drawable.ic_back);
        DrawerActivity.iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DrawerActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
            }
        });

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            progressDialog();
            dialog.show();
            UserOrderDetails();
        } else {

            Toasty.error(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    private void progressDialog() {
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

    private void UserOrderDetails() {
        StringRequest request = new StringRequest(Request.Method.POST, order_details, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                dialog.cancel();
                Log.e("order details",response);

                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        OrderDetails order = new OrderDetails();
                        String user_id=jsonObject.getString("user_id");
                        String product_id=jsonObject.getString("product_id");
                        String product_name=jsonObject.getString("product_name");
                        String product_quantity=jsonObject.getString("product_quantity");
                        String order_date_month=jsonObject.getString("order_date_month");
                        String product_price=jsonObject.getString("product_price");

                        String order_id=jsonObject.getString("order_id");
                        String product_image="http://vsfarma.blucorsys.in/productimages/"+jsonObject.getString("product_image");

                        order.setUser_id(user_id);
                        order.setProduct_id(product_id);
                        order.setProduct_name(product_name);
                        order.setProduct_quantity(product_quantity);
                        order.setOrder_date_month(order_date_month);
                        order.setOrder_id(order_id);
                        order.setProduct_image(product_image);
                        order.setProduct_price(product_price);

                        userOrderList.add(order);

                    }

                    setAdapter();
                }
                catch (JSONException e) {
                    Log.d("JSONException", e.toString());
                }
                if (userOrderList.isEmpty()){
                    llcartItem.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                } else {
                    llcartItem.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
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
                return parameters;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }

    private void setAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new OrderAdapter(userOrderList,getActivity());
        recyclerView.setAdapter(adapter);
    }

    public void replaceFragmentWithAnimation(Fragment fragment,String order_id,String order_name,String order_date,String order_qnty,String product_price) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        Bundle bundle = new Bundle();
        bundle.putString("order_id",order_id);
        bundle.putString("product_name",order_name);
        bundle.putString("order_date_month",order_date);
        bundle.putString("product_quantity",order_qnty);
        bundle.putString("product_price",product_price);


        //bundle.putString("order_date_month",);
        fragment.setArguments(bundle);
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.commit();
    }

    private class Holder extends RecyclerView.ViewHolder {
        TextView qnty;
        TextView tvProductName,orderDate;
        ImageView productImage;

        LinearLayout llMain;

        public Holder(View itemView) {
            super(itemView);


            llMain = itemView.findViewById(R.id.llMain);
            qnty = itemView.findViewById(R.id.tvQty);
            productImage=itemView.findViewById(R.id.productImage);
            orderDate=itemView.findViewById(R.id.tvOrderDate);
            tvProductName = itemView.findViewById(R.id.tvProductName);

        }
    }

    private class OrderAdapter extends RecyclerView.Adapter<Holder> {
        private List<OrderDetails> mModel;
        private Context mContext;

        public OrderAdapter(List<OrderDetails> mModel, Context mContext) {
            this.mModel = mModel;
            this.mContext = mContext;
        }


        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlistitems, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {
            holder.tvProductName.setText(mModel.get(position).getProduct_name());

            //holder.tvDesc.setText(mModel.get(position).getProduct_desc());
            holder.qnty.setText(mModel.get(position).getProduct_quantity());
            holder.orderDate.setText(mModel.get(position).getOrder_date_month());
            Glide.with(mContext)
                    .load(mModel.get(position).getProduct_image())
                    .into(holder.productImage);

            holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String order_id=mModel.get(position).getOrder_id();
                String order_name=mModel.get(position).getProduct_name();
                String order_date=mModel.get(position).getOrder_date_month();
                String order_qnty=mModel.get(position).getProduct_quantity();
                String product_price=mModel.get(position).getProduct_price();

                Log.e("order_id","order_id"+order_id);
                replaceFragmentWithAnimation(new OrderDetailsFragment(),order_id,order_name,order_date,order_qnty,product_price);
            }
        });

        }
        public int getItemCount() {
            return mModel.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

    }
}