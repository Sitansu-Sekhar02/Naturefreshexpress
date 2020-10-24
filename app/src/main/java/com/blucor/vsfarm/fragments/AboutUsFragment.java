package com.blucor.vsfarm.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.renderscript.Element;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blucor.vsfarm.R;
import com.blucor.vsfarm.activity.DrawerActivity;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;

import static androidx.core.os.LocaleListCompat.create;


public class AboutUsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AboutUsFragment() {
        // Required empty public constructor
    }

   // AlertDialog dialog;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        WebView mywebview = (WebView)view. findViewById(R.id.webView);
        ProgressDialog();
        mywebview.setWebViewClient(new WebViewClient());
        mywebview.loadUrl("http://farma.blucorsys.com/about-us/");
        DrawerActivity.rlsearchview.setVisibility(View.GONE);
        DrawerActivity.marquee.setVisibility(View.GONE);
        DrawerActivity.tvHeaderText.setText("About Us");
        DrawerActivity.iv_menu.setImageResource(R.drawable.ic_back);
        DrawerActivity.iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DrawerActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
            }
        });

        return view;

    }
    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dialog.cancel();
        }
    }
    public void ProgressDialog() {
        //Dialog
        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
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

    }