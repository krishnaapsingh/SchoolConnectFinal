package com.trieffects.ConnecttwoSchool.TecherFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trieffects.ConnecttwoSchool.R;


public class SyllabusUpload extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.syllabus_upload_fragment,container,false);

        return view;
    }
}
