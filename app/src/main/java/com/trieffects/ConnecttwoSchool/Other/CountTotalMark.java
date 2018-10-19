package com.trieffects.ConnecttwoSchool.Other;

import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.ResultData1;

import java.util.List;

/**
 * Created by Trieffects on 27-Nov-17.
 */

public class CountTotalMark {

    public  static String count(List<ResultData1> list){
        int totalmark=-1,obtainMark=-1;

        for(int i=0;i<list.size();i++){
            if(!ApiUtils.isEmptyString(list.get(i).get_marks)){
                if(obtainMark==-1){
                    obtainMark=(int) Float.parseFloat(list.get(i).get_marks);
                   totalmark=(int) Float.parseFloat(list.get(i).full_marks);
                }else {
                    obtainMark=obtainMark+(int) Float.parseFloat(list.get(i).get_marks);
                    totalmark=totalmark+(int) Float.parseFloat(list.get(i).full_marks);
                }
            }
        }

        if(obtainMark>=0){
            String item=obtainMark+"/"+totalmark;
            return item;
        }else {
            return "-";
        }
    }
}
