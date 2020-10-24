package com.blucor.vsfarm.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.blucor.vsfarm.R;


public class EditProductFragment extends Fragment {
    View v; @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_product, container, false);

        return view;
    }

}

