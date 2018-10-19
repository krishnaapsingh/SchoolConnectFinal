package com.trieffects.ConnecttwoSchool.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trieffects.ConnecttwoSchool.R;

/**
 * Created by Trieffects on 12-Jan-18.
 */

public class FeeHistoryAdapter extends RecyclerView.Adapter<FeeHistoryAdapter.ViewHolder> {


    public FeeHistoryAdapter(){

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.feehistory, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }


}
