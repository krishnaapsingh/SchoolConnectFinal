package com.trieffects.ConnecttwoSchool.Model;

/**
 * Created by Trieffects on 18-Dec-17.
 */

public class SubjectDataList {
    public String id;
    public String subject_id;
    public String name;
    public String type;
    public String img_name;
    public String teacher_name;

    @Override
    public String toString() {
        return name;
    }
}
