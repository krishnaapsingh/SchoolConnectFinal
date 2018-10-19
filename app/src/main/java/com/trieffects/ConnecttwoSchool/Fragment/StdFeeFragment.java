package com.trieffects.ConnecttwoSchool.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.trieffects.ConnecttwoSchool.Adapter.FeeHistoryAdapter;
import com.trieffects.ConnecttwoSchool.Adapter.FeesDetailAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;

/**
 * Created by Trieffects on 12-Jan-18.
 */

public class StdFeeFragment extends Fragment implements View.OnClickListener{
    RecyclerView recyview_fee,recyview_history;
    FeesDetailAdapter adapter;
    FeeHistoryAdapter adapter1;

    private PopupWindowHelper popupWindowHelper;
    ImageView back_img,user_image;
    private View popView;
    Button hisbtn;
    private ShimmerRecyclerView shimmerRecycler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.stdfee_fragment,container,false);
        recyview_fee = (RecyclerView)view.findViewById(R.id.recyview_fee);
        user_image=view.findViewById(R.id.user_image);
        showHistor();
        if(!ApiUtils.isEmptyString(PrefrencesUtils.getImage())){
            Glide.with(this).load(ApiUtils.ImageBaseUrl+PrefrencesUtils.getImage()).error(R.drawable.mypic).into(user_image);
        }
        recyview_history = (RecyclerView)view.findViewById(R.id.recyview_history);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyview_fee.setLayoutManager(mLayoutManager);
        recyview_fee.setItemAnimator(new DefaultItemAnimator());
        hisbtn=(Button)view.findViewById(R.id.hisbtn);
        hisbtn.setOnClickListener(this);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyview_history.setLayoutManager(mLayoutManager1);
        recyview_history.setItemAnimator(new DefaultItemAnimator());

        adapter = new FeesDetailAdapter();
        recyview_fee.setAdapter(adapter);
       adapter.notifyDataSetChanged();
        adapter1 = new FeeHistoryAdapter();
        recyview_history.setAdapter(adapter1);
         adapter1.notifyDataSetChanged();
        return view;
    }

    public void showHistor(){
        popView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_student, null);
        popupWindowHelper = new PopupWindowHelper(popView);
        RelativeLayout lay=(RelativeLayout)popView.findViewById(R.id.main_layout);
        lay.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.yellowcolor));
        shimmerRecycler=(ShimmerRecyclerView)popView.findViewById(R.id.reclerview_std);
        back_img=(ImageView)popView.findViewById(R.id.back_img);
        back_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hisbtn:
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                shimmerRecycler.setLayoutManager(mLayoutManager);
                shimmerRecycler.setItemAnimator(new DefaultItemAnimator());
                shimmerRecycler.showShimmerAdapter();
                shimmerRecycler.setAdapter(adapter1);
                shimmerRecycler.hideShimmerAdapter();
                popupWindowHelper.showFromBottom(v);
                break;
            case R.id.back_img:
                popupWindowHelper.dismiss();
                break;
        }
    }
}
