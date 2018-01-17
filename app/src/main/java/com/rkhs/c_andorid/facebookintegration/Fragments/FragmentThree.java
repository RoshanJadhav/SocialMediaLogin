package com.rkhs.c_andorid.facebookintegration.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rkhs.c_andorid.facebookintegration.R;

/**
 * Created by Admin on 16-01-2018.
 */

public class FragmentThree extends Fragment {

    public static final FragmentThree newInstance(String paramString) {
        FragmentThree fragmentThree = new FragmentThree();
        Bundle bundle = new Bundle();
        bundle.putString("EXTRA_MESSAGE", paramString);
        fragmentThree.setArguments(bundle);
        return fragmentThree;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_three,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
