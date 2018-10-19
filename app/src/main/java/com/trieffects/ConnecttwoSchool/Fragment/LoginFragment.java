package com.trieffects.ConnecttwoSchool.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.trieffects.ConnecttwoSchool.Other.MyView;
import com.trieffects.ConnecttwoSchool.R;

public class LoginFragment extends Fragment {
     RelativeLayout name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.loginviewview,container,false);

        name.addView(new MyView(getActivity(),"Name of School") );


        return view;
    }










}
