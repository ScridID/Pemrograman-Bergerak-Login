package com.example.fragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class FourthFragment extends Fragment{
    public FourthFragment () {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourth,container, false);
        Button button = view.findViewById(R.id.btnGoBackToFirstFragment);
        button.setOnClickListener(v -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FirstFragment())
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }

}

