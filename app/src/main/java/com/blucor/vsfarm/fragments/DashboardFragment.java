package com.blucor.vsfarm.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blucor.vsfarm.Model.CategoryModel;
import com.blucor.vsfarm.Model.CategoryProducts;
import com.blucor.vsfarm.R;
import com.blucor.vsfarm.activity.Utils;
import com.blucor.vsfarm.adapter.CardPagerAdapter;
import com.blucor.vsfarm.extra.AppSettings;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class DashboardFragment extends Fragment {

    //Viewpager
    ViewPager mViewPager;
    //view
    View view;

    //Tablayout
    TabLayout tabLayout;
    EditText categorySearch;
    //SearchView catSearch;
    EditText catSearch;

    //Int
    int currentPage = 0;

    //timer
    Timer timer;

    //SearchView searchView;
    Dialog dialog;

    //long
    final long DELAY_MS = 1000;
    final long PERIOD_MS = 2000;

    //Images
    private static final int[] mResources = {
            R.drawable.farm1,
            R.drawable.irrigations,
            R.drawable.pipes,
            R.drawable.agri,
    };

    public static final String JSON_URL = "http://vsfastirrigation.com/webservices/category.php";


    private List<CategoryModel> categoryList;
    private ProductCategoryAdapter adapter;
    SwipeRefreshLayout refreshLayout;

    RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homefragment, container, false);

        mViewPager = view.findViewById(R.id.myviewpager);
        tabLayout = view.findViewById(R.id.tabDots);
        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout=view.findViewById(R.id.swipeToRefresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                /*refreshLayout.setRefreshing(true);
                jsonRequest();*/
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                 //jsonRequest();
                refreshLayout.setRefreshing(false);
            }
        });

        catSearch=view.findViewById(R.id.category_search);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        categoryList = new ArrayList<>();
        tabLayout.setupWithViewPager(mViewPager, true);

        catSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                adapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            jsonRequest();
            ProgressForMain();
            dialog.show();
        } else {
            Toasty.error(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
        //Page
        AppSettings.fromPage="1";

        //setTablayout
        tabLayout.setupWithViewPager(mViewPager, true);
        final int NUM_PAGES = 5;
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES - 1) {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        CardPagerAdapter mCardPagerAdapter =new CardPagerAdapter(getActivity(),mResources) {
            @Override
            protected void onCategoryClick(View view, String str) {

            }
        };
        mViewPager.setAdapter(mCardPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setClipToPadding(false);
        mViewPager.setCurrentItem(1, true);
        mViewPager.setPageMargin(10);


        return view;
    }

    private void filter(String toString) {
        List<CategoryModel> temp = new ArrayList();
        for(CategoryModel d: categoryList){
            if(d.getCategory_name().toLowerCase().contains(toString.toLowerCase())){
                temp.add(d);
            }else{
            }
        }
        //update recyclerview
        adapter.updateList(temp);
    }


    private void ProgressForMain() {
        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_for_main);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
    }

    private void setAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new ProductCategoryAdapter(categoryList,getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void jsonRequest() {
       //refreshLayout.setRefreshing(true);
        StringRequest request = new StringRequest(JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response1",response);
               // refreshLayout.setRefreshing(false);
                dialog.cancel();
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        CategoryModel user = new CategoryModel();

                        String category_id=jsonObject.getString("id");
                        String category_name=jsonObject.getString("category_name");
                        //String category_image=jsonObject.getString("category_image");
                        String Image_url="http://vsfastirrigation.com/upload/cat_image/"+jsonObject.getString("category_image");

                       //String res= category_image.replace("//","");
                       // Log.e("responsee",""+res);
                        user.setCategory_id(category_id);
                        user.setCategory_name(category_name);
                        user.setCategory_image(Image_url);

                        categoryList.add(user);

                    }
                    setAdapter();
                }
                catch (JSONException e) {
                     Log.d("JSONException", e.toString());
                }
                //refreshLayout.setRefreshing(false);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                Log.e("error_response", "" + error);
               // refreshLayout.setRefreshing(false);

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }


    //*Recyclerview Adapter*//
    private class ProductCategoryAdapter extends RecyclerView.Adapter<Holder> implements Filterable {

       private List<CategoryModel> category;
        private List<CategoryModel> mModel;
        private Context mContext;

        public ProductCategoryAdapter(List<CategoryModel>mModel,Context mContext) {
            this.mModel=mModel;
            this.mContext=mContext;
            category=new ArrayList<>(mModel);

        }
        public void updateList(List<CategoryModel> list){
            mModel = list;
            notifyDataSetChanged();
        }

        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items, parent, false));

        }

        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {

            holder.tvCatName.setText(mModel.get(position).getCategory_name());
            Glide.with(mContext)
                    .load(mModel.get(position).getCategory_image())
                    .into(holder.image);

            holder.rrMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String id=mModel.get(position).getCategory_id();
                    Log.e("success" ,""+id);
                    Fragment fragment = new Fragment();
                    replaceFragmentWithAnimation(new ProductListingFragment(),id);
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

        @Override
        public Filter getFilter() {
            return filter;
        }
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<CategoryModel> filteredList=new ArrayList<>();
                if (charSequence.toString().isEmpty()){
                    filteredList.addAll(categoryList);
                }else {
                    for (CategoryModel product:categoryList){
                        if (product.getCategory_name().toLowerCase().contains(charSequence.toString().toLowerCase())){
                            filteredList.add(product);
                        }
                    }
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mModel.clear();
                mModel.addAll((Collection<? extends CategoryModel>) filterResults.values);
                notifyDataSetChanged();
            }
        };


    }

    private class Holder extends RecyclerView.ViewHolder {
        LinearLayout rrMain;
        ImageView image;
        TextView tvCatName;
        public Holder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tvCatName = itemView.findViewById(R.id.tvCatName);
            rrMain = itemView.findViewById(R.id.rrMain);
        }
    }
    public void replaceFragmentWithAnimation(Fragment fragment,String id) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.commit();
    }
}
