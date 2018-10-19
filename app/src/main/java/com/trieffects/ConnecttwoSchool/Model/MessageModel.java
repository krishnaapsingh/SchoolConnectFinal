package com.trieffects.ConnecttwoSchool.Model;

import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;

import java.util.ArrayList;

/**
 * Created by Trieffects on 29-Nov-17.
 */

public class MessageModel {
    public boolean status;
    public String message;
    public ArrayList<MessageData> data;
    public class MessageData{
        public String id;
        public String teacher_id;
        public String parent_id;
        public String created_at;
        public String message;
        public String name;
        public String email;

        public String pid;
        public String father_name;


        @Override
        public String toString(){
            if(ApiUtils.isEmptyString(name)){
                return father_name;
            }
            return name;
        }

    }


}
