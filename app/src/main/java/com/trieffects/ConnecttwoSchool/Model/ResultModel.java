package com.trieffects.ConnecttwoSchool.Model;

import java.util.ArrayList;

/**
 * Created by Trieffects on 15-Nov-17.
 */

public class ResultModel {
    public boolean status;
    public String message;
    public FirstObject data;

    public class FirstObject{
       public ExamSchedule examSchedule;
    }

    public class ExamSchedule{
     public String status;
     public ArrayList<ResultData> result;
    }
}
