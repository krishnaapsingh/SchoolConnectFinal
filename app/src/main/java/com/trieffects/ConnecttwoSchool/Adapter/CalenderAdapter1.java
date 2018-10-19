package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trieffects.ConnecttwoSchool.Model.CalenderModel;
import com.trieffects.ConnecttwoSchool.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trieffects on 28-Oct-17.
 */

public class CalenderAdapter1 extends RecyclerView.Adapter<CalenderAdapter1.ViewHolder>{
    List<CalenderModel> listData=new ArrayList<>();
    Context context;
    int mChg;
    public CalenderAdapter1( List<CalenderModel> list, Context mcontext,int cng){
       listData=list;
       context=mcontext;
       mChg=cng;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private RecyclerView.LayoutManager layoutManager;
        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView)itemView.findViewById(R.id.recyclerView);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.attendance_calenderview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int k=0;
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        holder.recyclerView.setLayoutManager(layoutManager);

            CalenderAdapter2 adapter2=new CalenderAdapter2(listData.get(position).getDay(),position,listData.get(position).getColorno(),context);
            holder.recyclerView.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mChg==0) {
            return 5;
        }else {
            return 6;
        }
    }
}
