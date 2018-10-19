package com.trieffects.ConnecttwoSchool.Model;

import java.util.ArrayList;

/**
 * Created by Trieffects on 27-Nov-17.
 */

public class TeacherResultModel {
    public boolean status;
    public String message;
    public TeacherResultData data;

    public class TeacherResultData{
        public ExamSchedule examSchedule;

    }

    public class ExamSchedule{
        public String status;
        public ArrayList<StudentData> result;
    }

    public class StudentData{
        public String student_id;
        public String roll_no;
        public String firstname;
        public String lastname;
        public String admission_no;
        public String dob;
        public String father_name;
        public String image;
        public ArrayList<ResultData1> exam_array;
    }
}
