package com.trieffects.ConnecttwoSchool.Model;

import java.util.ArrayList;

/**
 * Created by Trieffects on 29-Nov-17.
 */

public class HomeWorkModel {
    public boolean status;
    public String message;
    public HWData data;

    public class HWData{
        public ArrayList<HomeWorkData> Monday;
        public ArrayList<HomeWorkData> Tuesday;
        public ArrayList<HomeWorkData> Wednesday;
        public ArrayList<HomeWorkData> Thursday;
        public ArrayList<HomeWorkData> Friday;
        public ArrayList<HomeWorkData> Saturday;

    }
}
