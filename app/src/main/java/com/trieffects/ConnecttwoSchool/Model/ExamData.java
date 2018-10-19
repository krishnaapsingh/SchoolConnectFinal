package com.trieffects.ConnecttwoSchool.Model;

/**
 * Created by Trieffects on 03-Nov-17.
 */

public class ExamData {
    public String id;
    public String name;
    public String exam_id;


    public String session_id;
    public String teacher_subject_id;
    public String date_of_exam;
    public String start_to;
    public String end_from;
    public String room_no;
    public String full_marks;

    public String passing_marks;
    public String type;
    public String is_active;

    @Override
    public String toString() {
        return  name.toString();
    }
}
