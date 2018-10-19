package com.trieffects.ConnecttwoSchool.Model;

import java.util.ArrayList;

public class UserData {
    public String id;
    public String student_id;
    public String role;
    public String teacher_id;
    public String username;
    public String date_format;
    public String currency_symbol;
    public String timezone;
    public String sch_name;
    public String section_id;
    public String class_id;
    public String is_rtl;
    public Language language;
    public String email;
    public String mobileno;
    public String dob;
    public String image;
    public String profile_pic;

    public String driver_id;
    public String d_name;
    public String d_phone;
    public String d_license_no;

    public String phone;
    public String sex;



    public class Language{
      public String lang_id;
      public String language;
    }

    public ArrayList<ParentData> parent_info;
    public ArrayList<ChildData> child_info;
}
