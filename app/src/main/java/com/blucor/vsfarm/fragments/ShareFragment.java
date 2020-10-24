package com.blucor.vsfarm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.blucor.vsfarm.R;
import com.blucor.vsfarm.activity.DrawerActivity;

import javax.security.auth.Subject;

public class ShareFragment extends Fragment {

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_fragment, container, false);
                Intent shareIntent =   new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody="Your body here";
                String subject="Your subject here";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                String app_url = " https://play.google.com/store/apps/details?id=com.blucor.vsfarm";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
        return view;
    }
}
