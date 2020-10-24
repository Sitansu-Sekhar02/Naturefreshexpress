package com.blucor.vsfarm.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
    SearchView catSearch;

    //Int
    int currentPage = 0;

    //timer
    Timer timer;

    //searchView
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

    public static final String JSON_URL = "http://vsfarma.blucorsys.in/category.php";


    private List<CategoryModel> categoryList;
    private ProductCategoryAdapter adapter;
    SwipeRefreshLayout refreshLayout;

    //Recyclerview
    RecyclerView recyclerView;

    //Arraylist
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homefragment, container, false);

        mViewPager = view.findViewById(R.id.myviewpager);
        tabLayout = view.findViewById(R.id.tabDots);
        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout=view.findViewById(R.id.swipeToRefresh);
      /*  refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                        adapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
            }
        });*/

        //categorySearch = view.findViewById(R.id.category_search);
        catSearch=view.findViewById(R.id.category_search);
        catSearch.setQueryHint("Search Category");
        catSearch .clearFocus();
        catSearch.setFocusable(false);
        catSearch.setFocusableInTouchMode(true);


        categoryList = new ArrayList<>();
        tabLayout.setupWithViewPager(mViewPager, true);

       catSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String s) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String s) {
               adapter.getFilter().filter(s);
               return false;
           }
       });
        //Create method for Json Response(Volley)
        //jsonRequest();
        if (Utils.isNetworkConnectedMainThred(getActivity())) {
            jsonRequest();
            ProgressForMain();
            dialog.show();
        } else {
            Toasty.error(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();

           // Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_LONG).show();
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
        StringRequest request = new StringRequest(JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.cancel();
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        Log.e("response1",response);

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        CategoryModel user = new CategoryModel();

                        String category_id=jsonObject.getString("category_id");
                        String category_name=jsonObject.getString("category_name");
                        String Image_url="http://vsfarma.blucorsys.in/images/"+jsonObject.getString("category_image");

                       //String res= category_image.replace("//","");
                       // Log.e("responsee",""+res);
                        //category_image.replaceAll("\\/","//");
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
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                Log.e("error_response", "" + error);

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }


   /* public void setAdapter(RecyclerView mRecyclerview)
    {
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mRecyclerview.setAdapter(new ProductCategoryAdapter());
   }*/


    //*Recyclerview Adapter*//
    private class ProductCategoryAdapter extends RecyclerView.Adapter<Holder> implements Filterable {

       // ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
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
            //holder.image.setImageDrawable(getResources().getDrawable(image[position]));
            Glide.with(mContext)
                    .load(mModel.get(position).getCategory_image())
                    .into(holder.image);

            holder.rrMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String id=mModel.get(position).getCategory_id();
                    Log.e("success" ,""+id);
                    Fragment fragment = new Fragment();

                    //Toast.makeText(mContext, "Categories"+String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
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
            return exampleFilter;
        }
        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<CategoryModel> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(category);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (CategoryModel item : category) {
                        if (item.getCategory_name().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                categoryList.clear();
                categoryList.addAll((List) results.values);
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
        bundle.putString("category_id", id);
        fragment.setArguments(bundle);
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.commit();
    }
}
