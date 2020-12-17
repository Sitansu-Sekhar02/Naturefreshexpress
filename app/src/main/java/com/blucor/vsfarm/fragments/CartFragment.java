package com.blucor.vsfarm.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Vibrator;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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


public class CartFragment extends Fragment {


    //recyclerview
    RecyclerView recyclerView;

    Preferences preferences;
    int final_price= 0;
    double Total_price=0.0;

    String product_id;
    String cart_id;

    Dialog dialog;
    double result;
    //view
    View view;

    private List<CartItem> cartList;
    private CartAdapter cartAdapter;
    public static final String cart_url = "http://vsfastirrigation.com/webservices/fetchCartItem.php";
    public static final String cart_delete = "http://vsfastirrigation.com/webservices/deleteCartItem.php";
    public  static  final  String qnty_update="http://vsfastirrigation.com/webservices/update_quantity.php";


    //Gridlayout
    GridLayoutManager mGridLayoutManager;

    //Textview
    TextView tvProceed;
    TextView tvCartPrice;
    ImageView emptyCart;
    Button shopNow;
   //EditText productQuantity;

    LinearLayout llcartItem;
    LinearLayout cartEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.cartlistfragment, container, false);

        preferences=new Preferences(getActivity());

        //DrawerActivity
        DrawerActivity.marquee.setVisibility(View.GONE);
        DrawerActivity.rlsearchview.setVisibility(View.GONE);
        DrawerActivity.tvHeaderText.setText("Mycart");
        DrawerActivity.iv_menu.setImageResource(R.drawable.ic_back);
        DrawerActivity.ivCart.setVisibility(View.GONE);
        DrawerActivity.tvCount.setVisibility(View.GONE);

        DrawerActivity.iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DrawerActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
               /* if(AppSettings.fromPage.equalsIgnoreCase("1"))
                {
                    Intent i = new Intent(getActivity(), DrawerActivity.class);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                }
                else if(AppSettings.fromPage.equalsIgnoreCase("2"))
                {
                    replaceFragmentWithAnimation(new ProductListingFragment());
                }
                else
                {

                }*/
            }
        });

        cartList=new ArrayList<>();

        recyclerView= view.findViewById(R.id.recyclerview);
        tvProceed= view.findViewById(R.id.tvProceed);
        tvCartPrice=view.findViewById(R.id.tvCartprice);
        emptyCart = view.findViewById(R.id.gif);
        llcartItem = view.findViewById(R.id.llcartItem);
        shopNow=view.findViewById(R.id.shopNow);
        cartEmpty=view.findViewById(R.id.empty_cart);
        //productQuantity=view.findViewById(R.id.quantity);

        shopNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragmentWithAnimation(new DashboardFragment());
            }
        });
        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            cartFragmentItem();
            ProgressForCart();
            dialog.show();
        } else {
           // Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_LONG).show();
            Toasty.error(getActivity(), "No Internet Connection!", Toast.LENGTH_LONG).show();

        }

        tvProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnectedMainThred(getActivity())) {
                    replaceFragmentWithAnimation(new CheckoutFragment());
                } else {
                    Toasty.error(getActivity(), "No Internet Connection!", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void cartFragmentItem() {
        StringRequest request = new StringRequest(Request.Method.POST,cart_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.cancel();
                Log.e("cart",response);
                //progressDialog.cancel();
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        CartItem cart = new CartItem();
                        String cart_id=jsonObject.getString("cart_id");
                        String user_id=jsonObject.getString("user_id");
                        String product_id=jsonObject.getString("product_id");
                        String product_name=jsonObject.getString("product_name");
                        String product_price=jsonObject.getString("product_price");
                        String product_image=jsonObject.getString("product_image");
                        String product_qnty=jsonObject.getString("product_quantity");
                        String product_size=jsonObject.getString("product_size");

                        String res= product_image.replace("//","");
                        //Log.e("responsee",""+res);
                        //category_image.replaceAll("\\/","//");
                        cart.setCart_id(cart_id);
                        cart.setUser_id(user_id);
                        cart.setProduct_id(product_id);
                        cart.setProduct_name(product_name);
                        cart.setProduct_image(product_image);
                        cart.setProduct_price(product_price);
                        cart.setProduct_quantity(product_qnty);
                        cart.setProduct_size(product_size);
                        cartList.add(cart);

                        double price=Double.parseDouble(product_price);
                        double qty=Double.parseDouble(product_qnty);

                        Total_price = Total_price + ( price* qty);
                        Log.e("price add",""+Total_price);
                        tvCartPrice.setText(String.valueOf( Total_price));
                        tvCartPrice.setText("Total Amount \u20b9" +Total_price);
                        Log.e("total price","price"+Total_price);

                    }

                    setAdapter();
                }

                catch (JSONException e) {
                    Log.d("JSONException", e.toString());
                }
                Log.e("sss","cartlist"+cartList);

                if (cartList.isEmpty()){
                    llcartItem.setVisibility(View.GONE);
                    cartEmpty.setVisibility(View.VISIBLE);
                } else {
                    llcartItem.setVisibility(View.VISIBLE);
                    cartEmpty.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();

                // progressDialog.cancel();
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
            cartAdapter = new CartAdapter(cartList, getActivity());
            Log.e("msg", "cart adapter" + cartAdapter);
            recyclerView.setAdapter(cartAdapter);

    }

    private class Holder extends RecyclerView.ViewHolder {
        TextView tvOldPrice;
        TextView cart_item_number;
        TextView tvSave;
        ImageButton cart_quant_minus;
        ImageButton cart_quant_add;
        ImageButton checkquantity;
        TextView tvProductName,qntyPrice;
        TextView tvFinalprice,tvSize,tvcartProductSize;
        EditText productQuantity;
        TextView cart_item_delete;
        ImageView cart_item_image;

        public Holder(View itemView) {
            super(itemView);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);
            tvFinalprice = itemView.findViewById(R.id.price);
            tvSize = itemView.findViewById(R.id.tvSize);
            cart_item_number = itemView.findViewById(R.id.cart_item_number);
            cart_quant_minus = itemView.findViewById(R.id.cart_quant_minus);
            cart_quant_add = itemView.findViewById(R.id.cart_quant_add);
            productQuantity = itemView.findViewById(R.id.quantity);
            checkquantity=itemView.findViewById(R.id.check);
            tvSave = itemView.findViewById(R.id.tvSave);
            cart_item_image = itemView.findViewById(R.id.cart_item_image);
            cart_item_delete = itemView.findViewById(R.id.cart_item_delete);
            tvProductName = itemView.findViewById(R.id.tvcartProductName);
            qntyPrice = itemView.findViewById(R.id.totalPrice);
            tvcartProductSize=itemView.findViewById(R.id.tvcartProductSize);


        }
    }

    private class CartAdapter extends RecyclerView.Adapter<Holder> {
        //ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        private List<CartItem> mModel;
        private Context mContext;

        public CartAdapter(List<CartItem> mModel, Context mContext) {
            this.mModel = mModel;
            this.mContext = mContext;
        }

        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {

            holder.tvProductName.setText(mModel.get(position).getProduct_name());
            Glide.with(mContext)
                    .load(mModel.get(position).getProduct_image())
                    .into(holder.cart_item_image);
            holder.tvFinalprice.setText(mModel.get(position).getProduct_price());
            holder.tvcartProductSize.setText(mModel.get(position).getProduct_size());
            holder.productQuantity.setText(mModel.get(position).getProduct_quantity());

            double productprice = Double.parseDouble(mModel.get(position).getProduct_price());
            double qnty = Double.parseDouble(mModel.get(position).getProduct_quantity());
            result=productprice*qnty;

            Log.e("11productprice",""+productprice);
            Log.e("11qnty",""+qnty);
            Log.e("11result",""+result);
            holder.qntyPrice.setText("\u20b9" +result);

            holder.checkquantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.productQuantity.getText().toString().trim().isEmpty())
                    {
                        holder.productQuantity.setError("Please enter quantity");
                        holder.productQuantity.requestFocus();
                    }else
                    {
                        String productQnty= String.valueOf(holder.productQuantity.getText());
                        UpdateQnty(productQnty);
                        cart_id=cartList.get(position).getCart_id();
                        Log.e("qnty","rrr"+productQnty);
                        Log.e("id","cart no"+cart_id);
                    }
                }
            });

            holder.cart_quant_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = Integer.parseInt(holder.cart_item_number.getText().toString());
                    count=count+1;
                    holder.cart_item_number.setText(String.valueOf(count));
                    Vibrator vibe = (Vibrator)getActivity(). getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    holder.cart_item_number.setText(String.valueOf(count));
                }
            });
            holder.cart_quant_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = Integer.parseInt(holder.cart_item_number.getText().toString());

                    if (count > 1) {
                        count=count-1;

                            holder.cart_item_number.setText(String.valueOf(count));
                        }
                     else {

                    }
                }
            });
            holder.cart_item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCartItem();
                    product_id=cartList.get(position).getProduct_id();
                    mModel.remove(position);
                    notifyDataSetChanged();

                    int count = Integer.parseInt(holder.cart_item_number.getText().toString());
                    if (count > 1) {
                        Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        vibe.vibrate(100);
                        count=count-1;

                        int counter=preferences.getInt("count");
                        int totalcount=counter-1;
                        preferences.set("count",totalcount);
                        preferences.commit();
                        DrawerActivity.tvCount.setText(""+totalcount);
                        holder.cart_item_number.setText(String.valueOf(count));

                    } else {
                        int counter = preferences.getInt("count");
                        int totalcount = counter - 1;
                        preferences.set("count", totalcount);
                        preferences.commit();
                        DrawerActivity.tvCount.setText("" + totalcount);
                        //deleteCartItem(mModel.get(getAdapterPosition()).getProduct_id());
                    }

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

    private void UpdateQnty(final String productQnty) {
        StringRequest request = new StringRequest(Request.Method.POST, qnty_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //dialog.cancel();
                replaceFragmentWithAnimation(new CartFragment());
                Log.e("update",response);
                Toasty.success(getActivity(), "Quantity Update Successfully", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(),"Quantity Update Successfully",Toast.LENGTH_LONG).show();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //dialog.cancel();
                Log.e("error_response", "" + error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("cart_id",cart_id);
                parameters.put("product_quantity",productQnty);
                Log.e("update parameter", "" + parameters);

                return parameters;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }


    private void ProgressForCart() {
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

    private void deleteCartItem() {
        StringRequest request = new StringRequest(Request.Method.POST,cart_delete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // dialog.cancel();
                Toast.makeText(getActivity(),"Item Deleted Successfully",Toast.LENGTH_SHORT).show();
                replaceFragmentWithAnimation(new CartFragment());
                Log.e("delete",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //dialog.cancel();
                Log.e("error_response", "" + error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("product_id",product_id);
                Log.e("delete",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }
    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.commit();
    }
    public void ShakeAnimation(View view)
    {
        final Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        view.startAnimation(animShake);
    }

}