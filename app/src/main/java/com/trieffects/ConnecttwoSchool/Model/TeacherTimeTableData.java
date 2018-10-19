package com.trieffects.ConnecttwoSchool.Model;

import java.util.ArrayList;

/**
 * Created by Trieffects on 24-Nov-17.
 */

public class TeacherTimeTableData {

    public ArrayList<Monday> Monday;
    public ArrayList<Tuesday> Tuesday;

    public ArrayList<Wednesday> Wednesday;
    public ArrayList<Thursday> Thursday;
    public ArrayList<Friday> Friday;
    public ArrayList<Saturday> Saturday;
    public ArrayList<Saturday> Sunday;

    public class Monday{
        public  String status;
        public String start_time;
        public String end_time;
        public String room_no;
        public String name;
    }

    public class Tuesday{
        public String status;
        public String start_time;
        public String end_time;
        public String room_no;
        public String name;
    }

    public  class Wednesday{
        public String status;
        public String start_time;
        public String end_time;
        public String room_no;
        public String name;
    }

    public class Thursday{
        public String status;
        public String start_time;
        public String end_time;
        public String room_no;
        public String name;
    }

    public class Friday{
        public String status;
        public String start_time;
        public String end_time;
        public String room_no;
        public String name;
    }

    public class Saturday{
        public String status;
        public String start_time;
        public String end_time;
        public String room_no;
        public String name;
    }

    public class Sunday{
        public String status;
        public String start_time;
        public String end_time;
        public String room_no;
        public String name;
    }
}
