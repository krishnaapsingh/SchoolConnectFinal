package com.trieffects.ConnecttwoSchool.Model;

import java.util.ArrayList;

/**
 * Created by Trieffects on 03-Nov-17.
 */

public class ExamModel {

    public boolean status;
    public String message;
    public DataModel data;

    public class DataModel{
       public ArrayList<ExamData> result_array;
    }
}
