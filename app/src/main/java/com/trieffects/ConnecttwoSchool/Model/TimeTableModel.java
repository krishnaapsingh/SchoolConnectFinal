package com.trieffects.ConnecttwoSchool.Model;

import java.util.ArrayList;

/**
 * Created by Trieffects on 31-Oct-17.
 */

public class TimeTableModel {
    public boolean status;
    public boolean message;
    public DataJson data;
    public class DataJson{
        public ArrayList<TimeTableData> result_array;
    }

}
