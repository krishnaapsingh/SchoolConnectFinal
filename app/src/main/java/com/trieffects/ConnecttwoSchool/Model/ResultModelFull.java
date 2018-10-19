package com.trieffects.ConnecttwoSchool.Model;

import java.util.List;

/**
 * Created by Trieffects on 27-Nov-17.
 */

public class ResultModelFull {

    private String name;
    private String rollno;
    private String fatherName;
    private String dob;
    private String image;

    public List<ResultData1> exam_array;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public List<ResultData1> getExam_array() {
        return exam_array;
    }

    public void setExam_array(List<ResultData1> exam_array) {
        this.exam_array = exam_array;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
