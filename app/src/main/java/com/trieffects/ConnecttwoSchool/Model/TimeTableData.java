package com.trieffects.ConnecttwoSchool.Model;

/**
 * Created by Trieffects on 31-Oct-17.
 */

public class TimeTableData {

    public Monday Monday;
    public Tuesday Tuesday;

    public Wednesday Wednesday;
    public Thursday Thursday;
    public Friday Friday;
    public Saturday Saturday;


    public class Monday{
        public  String status;
        public String start_time;
        public String end_time;
        public String room_no;
        public String subject;
    }

    public class Tuesday{
        public String status;
        public String start_time;
        public String end_time;
        public String room_no;
    }

    public  class Wednesday{
        public String status;
        public String start_time;
        public String end_time;
        public String room_no;
    }

    public class Thursday{
        public String status;
        public String start_time;
        public String end_time;
        public String room_no;
    }

    public class Friday{
        public String status;
        public String start_time;
        public String end_time;
        public String room_no;
    }

    public class Saturday{
        public String status;
        public String start_time;
        public String end_time;
        public String room_no;
    }
}
