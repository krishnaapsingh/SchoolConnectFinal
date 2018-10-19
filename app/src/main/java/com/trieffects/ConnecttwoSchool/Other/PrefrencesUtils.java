package com.trieffects.ConnecttwoSchool.Other;

import com.orhanobut.hawk.Hawk;
import com.trieffects.ConnecttwoSchool.Model.ChildData;
import com.trieffects.ConnecttwoSchool.Model.UserData;

import java.util.ArrayList;

public class PrefrencesUtils {

    private static String UserId="UserId";
    private static String UserName="UserName";
    private static String Id="Id";
    private static String Role="Role";
    private static String CurrencySymbol="CurrencySymbol";
    private static String ChildInfo="ChildInfo";
    private static String Mode="Mode";
    private static String EMAIL_ID="email_id";
    private static String MOBILE="mobile";

    private static String CLASS_ID="classId";
    private static String SECTION_ID="sectionId";
    private static String Image="Image";
    private static String DriverLicnce="DL";
    private static String SchoolName="SchoolName";


    private static String NOTICEBOARD_COUNT="noticeboard";
    private static String ATTENDANCE_COUNT="ATTENDANCE_COUNT";
    private static String FEE_COUNT="FEE_COUNT";
    private static String HOMEWORK="HOME_WORK";
    private static String TIMETABLE_COUNT="TIMETABLE_COUNT";
    private static String MAP_COUNT="MAP_COUNT";
    private static String RESULT_COUNT="RESULT_COUNT";
    private static String MESSAGE_COUNT="MESSAGE_COUNT";
    private static String LEAVE_COUNT="LEAVE_COUNT";
    private static String DIGITAL_COUNT="DIGITAL_COUNT";
    private static String LoginUserName="LoginUserName";
    private static String StudentLoginData ="StudentLoginData";



    public static void saveLoginStudentData(UserData value){
        Hawk.put(StudentLoginData,value);

    }
    public static UserData getLoginStudentdata(){
        return Hawk.get(StudentLoginData,null);
    }


    public static void saveLoginUserName(String value){
        Hawk.put(LoginUserName,value);

    }
    public static String getLoginUserName(){
        return Hawk.get(LoginUserName,null);
    }




    public static void saveDriverLicnce(String value){
        Hawk.put(DriverLicnce,value);

    }
    public static String getDriverLicnce(){
        return Hawk.get(DriverLicnce,null);
    }


    public static void saveSchoolName(String value){
        Hawk.put(SchoolName,value);

    }
    public static String getSchoolName(){
        return Hawk.get(SchoolName,null);
    }





    public static void saveImage(String value){
        Hawk.put(Image,value);

    }
    public static String getImage(){
        return Hawk.get(Image,null);
    }

    public static void saveEmail(String value){
        Hawk.put(EMAIL_ID,value);

    }
    public static String getEmail(){
        return Hawk.get(EMAIL_ID,null);
    }

    public static void saveMobile(String value){
        Hawk.put(MOBILE,value);
    }
    public static String getMobile(){
        return Hawk.get(MOBILE,null);
    }


    public static void saveCurrencySymbol(String value){
        Hawk.put(CurrencySymbol,value);
    }
    public static String getCurrencySymbol(){
        return Hawk.get(CurrencySymbol,null);
    }

    public static void saveUserId(String value){
        Hawk.put(UserId,value);
    }
    public static String getUserId(){
        return Hawk.get(UserId,null);
    }

    public static void saveUserName(String value){
        Hawk.put(UserName,value);
    }
    public static String getUserName(){
        return Hawk.get(UserName,null);
    }

    public static void saveId(String value){
        Hawk.put(Id,value);
    }
    public static String getId(){
        return Hawk.get(Id,"");
    }

    public static void saveUserMode(String value){
        Hawk.put(Mode,value);
    }
    public static String getUserMode(){
        return Hawk.get(Mode,"");
    }

    public static void saveRole(String value){
        Hawk.put(Role,value);
    }
    public static String getRole(){
        return Hawk.get(Role,null);
    }


    public static void saveSectionId(String value){
        Hawk.put(SECTION_ID,value);
    }
    public static String getSectionId(){
        return Hawk.get(SECTION_ID,null);
    }

    public static void saveClassId(String value){
        Hawk.put(CLASS_ID,value);
    }
    public static String getClassId(){
        return Hawk.get(CLASS_ID,null);
    }

    public static void saveChildInfo(ArrayList<ChildData> value){
        Hawk.put(ChildInfo,value);
    }
    public static ArrayList<ChildData>  getChildInfo(){
        return Hawk.get(ChildInfo,null);
    }


    public static void saveNoticecount(String value){
        Hawk.put(NOTICEBOARD_COUNT,value);
    }
    public static String getNoticecount(){
        return Hawk.get(NOTICEBOARD_COUNT,null);
    }

    public static void saveAttendancecount(String value){
        Hawk.put(ATTENDANCE_COUNT,value);
    }
    public static String getAttendancecount(){
        return Hawk.get(ATTENDANCE_COUNT,null);
    }

    public static void saveFeecount(String value){
        Hawk.put(FEE_COUNT,value);
    }
    public static String getFeecount(){
        return Hawk.get(FEE_COUNT,null);
    }

    public static void saveHomecount(String value){
        Hawk.put(HOMEWORK,value);
    }
    public static String getHomecount(){
        return Hawk.get(HOMEWORK,null);
    }

    public static void saveTimeTablecount(String value){
        Hawk.put(TIMETABLE_COUNT,value);
    }
    public static String getTimeTablecount(){
        return Hawk.get(TIMETABLE_COUNT,null);
    }

    public static void savetMapcount(String value){
        Hawk.put(MAP_COUNT,value);
    }
    public static String getMapcount(){
        return Hawk.get(MAP_COUNT,null);
    }

    public static void saveResultcount(String value){
        Hawk.put(RESULT_COUNT,value);
    }
    public static String getResultcount(){
        return Hawk.get(RESULT_COUNT,null);
    }

    public static void saveMessagecount(String value){
        Hawk.put(MESSAGE_COUNT,value);
    }
    public static String getMessagecount(){
        return Hawk.get(MESSAGE_COUNT,null);
    }

    public static void saveLeavecount(String value){
        Hawk.put(LEAVE_COUNT,value);
    }
    public static String getLeavecount(){
        return Hawk.get(LEAVE_COUNT,null);
    }

    public static void saveDigitalcount(String value){
        Hawk.put(DIGITAL_COUNT,value);
    }
    public static String getDigitalcount(){
        return Hawk.get(DIGITAL_COUNT,null);
    }

    public static void saveLattu(double latitude) {
            Hawk.put("Lattu",latitude);
    }

    public static double getLattu(){
        return Hawk.get("Lattu",null);
    }

    public static void saveLogude(double latitude) {
        Hawk.put("Logude",latitude);
    }

    public static double getLogude(){
        return Hawk.get("Logude",null);
    }
}
