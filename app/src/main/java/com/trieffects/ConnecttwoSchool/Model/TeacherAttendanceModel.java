package com.trieffects.ConnecttwoSchool.Model;

import java.util.ArrayList;

/**
 * Created by Trieffects on 18-Nov-17.
 */

public class TeacherAttendanceModel {
    public boolean status;
    public String message;
    public ObjectClass data;

    public class ObjectClass{
        public ArrayList<StudentListModel> resultlist;
    }
}
