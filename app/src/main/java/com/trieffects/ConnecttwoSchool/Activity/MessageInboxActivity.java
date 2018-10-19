package com.trieffects.ConnecttwoSchool.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.trieffects.ConnecttwoSchool.Adapter.MessageInboxAdapter;
import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.MessageModel;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageInboxActivity extends AppCompatActivity {
    private ShimmerRecyclerView shimmerRecycler;
    AlertDialog dialog;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_inbox);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Inbox");
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        Drawable drawable=toolbar.getNavigationIcon();
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.DST_IN);

        shimmerRecycler = (ShimmerRecyclerView)findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        shimmerRecycler.setLayoutManager(mLayoutManager);
        shimmerRecycler.setItemAnimator(new DefaultItemAnimator());
        dialog = new SpotsDialog(this,"Please wait",R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
           shimmerRecycler.showShimmerAdapter();
            dialog.show();
            inboxMessage(PrefrencesUtils.getId());
    }


    public void inboxMessage(String id){
        final ApiInterface mApiService= ApiUtils.getInterfaceService();
        Call<MessageModel> mService;
        if(PrefrencesUtils.getUserMode().equalsIgnoreCase("2")) {
            mService = mApiService.getMessage(id);
        }else {
            mService = mApiService.getParentMessage(id);
        }
        mService.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                MessageModel object=response.body();
                if(object.status){
                    MessageInboxAdapter adapter=new MessageInboxAdapter(object.data,MessageInboxActivity.this);
                    shimmerRecycler.setAdapter(adapter);
                }else {
                    Toast.makeText(MessageInboxActivity.this,"No data available",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                shimmerRecycler.hideShimmerAdapter();
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                dialog.dismiss();
                shimmerRecycler.hideShimmerAdapter();
                Toast.makeText(MessageInboxActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showPopup(String msg){
        final Dialog dialog = new Dialog(MessageInboxActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.inboxmessageshow);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        TextView message_view=(TextView)dialog.findViewById(R.id.message_view);
        message_view.setText(msg);
        Button dialogButton = (Button) dialog.findViewById(R.id.save_homebtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            MessageInboxActivity.this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
