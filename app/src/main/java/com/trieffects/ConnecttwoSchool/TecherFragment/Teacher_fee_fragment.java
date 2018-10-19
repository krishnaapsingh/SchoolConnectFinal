package com.trieffects.ConnecttwoSchool.TecherFragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.trieffects.ConnecttwoSchool.Adapter.StudentInformationAdapter;
import com.trieffects.ConnecttwoSchool.R;


/**
 * Created by yarolegovich on 25.03.2017.
 */

public class Teacher_fee_fragment extends android.support.v4.app.Fragment {

    StudentInformationAdapter adapter;
    private ShimmerRecyclerView shimmerRecycler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_text, container, false);
        shimmerRecycler = (ShimmerRecyclerView)view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        shimmerRecycler.setLayoutManager(mLayoutManager);
        shimmerRecycler.setItemAnimator(new DefaultItemAnimator());
        shimmerRecycler.showShimmerAdapter();
        adapter=new StudentInformationAdapter();
        shimmerRecycler.showShimmerAdapter();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                shimmerRecycler.setAdapter(adapter);
                shimmerRecycler.hideShimmerAdapter();
            }
        }, 3000);
        return view;
    }


}
