package com.trieffects.ConnecttwoSchool.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    TextView seek_txt;
    SeekBar seekBar;
    LinearLayout radius_lay,lay_radus;
    ImageView img1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        img1=(ImageView)findViewById(R.id.img1);
        lay_radus=(LinearLayout)findViewById(R.id.lay_radus);
        radius_lay=(LinearLayout)findViewById(R.id.radius_lay);
        radius_lay.setOnClickListener(this);
        seek_txt=(TextView)findViewById(R.id.seek_txt);
        seekBar=(SeekBar)findViewById(R.id.seekbar_radius);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBar.setProgress(i);
                seek_txt.setText(i+" Km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.radius_lay:
                if(lay_radus.getVisibility()==View.VISIBLE){
                    img1.setImageResource(R.drawable.righ_arrow);
                    lay_radus.setVisibility(View.GONE);
                }else {
                    img1.setImageResource(R.drawable.down_arrow);
                    lay_radus.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
