package com.trieffects.ConnecttwoSchool.Model;

import java.util.ArrayList;

/**
 * Created by Trieffects on 18-Nov-17.
 */

public class GetClassModel {
    public boolean status;
    public String message;
   public DataValue data;

    public class DataValue{
        public ArrayList<SectionAndClassData> classlist;
        public ArrayList<SectionAndClassData> sectionlist;
    }
}
