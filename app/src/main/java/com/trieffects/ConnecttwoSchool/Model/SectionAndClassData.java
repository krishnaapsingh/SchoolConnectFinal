package com.trieffects.ConnecttwoSchool.Model;

import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;

/**
 * Created by Trieffects on 18-Nov-17.
 */

public class SectionAndClassData {
    public String id;
    public String section;
    public String is_active;
    public String created_at;
    public String updated_at;
    public String my_class;
    public String section_id;

    @Override
    public String toString() {
        String data;
        if(!ApiUtils.isEmptyString(my_class))
        {
            data=my_class;
        }else{
            data=section;
        }
        return data ;
    }

}
