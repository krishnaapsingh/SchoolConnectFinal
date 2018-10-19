package com.trieffects.ConnecttwoSchool.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.trieffects.ConnecttwoSchool.Adapter.HolidayAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.HolidayModel;
import com.trieffects.ConnecttwoSchool.R;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shashikeshkumar on 31/03/18.
 */

public class HolidayFragment extends Fragment {
    private ShimmerRecyclerView shimmerRecycler;
    AlertDialog dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.holiday_fragment,container,false);
        shimmerRecycler = (ShimmerRecyclerView)view.findViewById(R.id.recycler_view);
        dialog = new SpotsDialog(getActivity(),"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        shimmerRecycler.setLayoutManager(mLayoutManager);
        shimmerRecycler.setItemAnimator(new DefaultItemAnimator());
        dialog.show();
        shimmerRecycler.showShimmerAdapter();
        getHolidayList();
        return view;
    }

    public void getHolidayList(){
        final ApiInterface mApiSerive= ApiUtils.getInterfaceService();
        Call<HolidayModel> mServices=mApiSerive.getHoliday();
        mServices.enqueue(new Callback<HolidayModel>() {
            @Override
            public void onResponse(Call<HolidayModel> call, Response<HolidayModel> response) {
                HolidayModel object=response.body();
                if(object.status){
                    HolidayAdapter adapter=new HolidayAdapter(object.data,getActivity());
                    shimmerRecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(),"Holiday data not found",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<HolidayModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
