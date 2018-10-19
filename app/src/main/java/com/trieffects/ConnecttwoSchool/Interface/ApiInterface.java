package com.trieffects.ConnecttwoSchool.Interface;


import com.trieffects.ConnecttwoSchool.Model.AttendanceModel;
import com.trieffects.ConnecttwoSchool.Model.AttendanceUpload;
import com.trieffects.ConnecttwoSchool.Model.DigitalParentModel;
import com.trieffects.ConnecttwoSchool.Model.DriverServiceModel;
import com.trieffects.ConnecttwoSchool.Model.ExamModel;
import com.trieffects.ConnecttwoSchool.Model.GetClassModel;
import com.trieffects.ConnecttwoSchool.Model.GetLatLogBusModel;
import com.trieffects.ConnecttwoSchool.Model.GetSection;
import com.trieffects.ConnecttwoSchool.Model.GetSubjectModel;
import com.trieffects.ConnecttwoSchool.Model.HolidayModel;
import com.trieffects.ConnecttwoSchool.Model.HomeWorkModel;
import com.trieffects.ConnecttwoSchool.Model.HomeWorkModelUpload;
import com.trieffects.ConnecttwoSchool.Model.LeaveApplicationModel;
import com.trieffects.ConnecttwoSchool.Model.LeaveModel;
import com.trieffects.ConnecttwoSchool.Model.Login;
import com.trieffects.ConnecttwoSchool.Model.MessageModel;
import com.trieffects.ConnecttwoSchool.Model.Noticeboard;
import com.trieffects.ConnecttwoSchool.Model.ResultModel;
import com.trieffects.ConnecttwoSchool.Model.TeacherAttendanceModel;
import com.trieffects.ConnecttwoSchool.Model.TeacherResultModel;
import com.trieffects.ConnecttwoSchool.Model.TeacherTimeModel;
import com.trieffects.ConnecttwoSchool.Model.TimeTableModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;


public interface ApiInterface {
   /* @FormUrlEncoded
    @POST("api/signup")
    Call<SignUp> registration(@Field("name") String username, @Field("imei") String imei, @Field("email") String email, @Field("password") String password, @Field("mode") String type);
*/
   @FormUrlEncoded
    @POST("api/example/userslogin")
    Call<Login> authenticate(@Field("username") String username,
                             @Field("password") String password,
                             @Field("device_id") String device_id);



   @FormUrlEncoded
    @POST("api/example/notificationList")
    Call<Noticeboard> getNotice(@Field("class_id") String class_id,
                                @Field("section_id") String section_id,
                                @Field("student_id") String student_id,
                                @Field("page") String page);


   @FormUrlEncoded
    @POST("api/example/attendence")
    Call<AttendanceModel> GetAttendance(@Field("year") String year, @Field("month") String month, @Field("student_id") String student_id);

    @FormUrlEncoded
    @POST("api/example/getTimeTable")
    Call<TimeTableModel> getTime(@Field("student_id") String id);

  @FormUrlEncoded
    @POST("api/example/getexamList")
    Call<ExamModel> getExamList(@Field("class_id") String class_id,
                             @Field("section_id") String section_id);

    @FormUrlEncoded
    @POST("api/example/getexamdetail")
    Call<ExamModel> getExamTimeTable(@Field("class_id") String class_id,
                                     @Field("section_id") String section_id,
                                     @Field("exam_id") String exam_id);

    @FormUrlEncoded
    @POST("api/example/examResult")
    Call<ResultModel> getRes(@Field("exam_id") String exam_id,
                             @Field("class_id") String class_id,
                             @Field("section_id") String section_id,
                             @Field("std_sess_id") String std_sess_id);

    @GET("api/example/getclass")
    Call<GetClassModel> getTechClass();

    @FormUrlEncoded
    @POST("api/example/getsection")
    Call<GetSection> getTechSection(@Field("class_id") String class_id);

    @FormUrlEncoded
    @POST("api/example/getAttendence")
    Call<TeacherAttendanceModel> getStudentList(@Field("class_id") String class_id,
                                                @Field("section_id") String section_id,
                                                @Field("date") String date);
    @FormUrlEncoded
    @POST("api/example/teacher_timetable")
    Call<TeacherTimeModel> getTeacherTime(@Field("teacher_id") String class_id);

    @FormUrlEncoded
    @POST("api/example/class_result")
    Call<TeacherResultModel> getClassResult(@Field("exam_id") String exam_id,
                                            @Field("class_id") String class_id,
                                            @Field("section_id") String section_id);
    @FormUrlEncoded
    @POST("api/example/leave_application")
    Call<LeaveApplicationModel> submitLeave(@Field("std_id") String std_id,
                                            @Field("std_ldt_frm") String std_ldt_frm,
                                            @Field("std_ldt_till") String std_ldt_till,
                                            @Field("std_leave_r") String std_leave_r,
                                            @Field("class_id") String class_id,
                                            @Field("section_id") String section_id,
                                            @Field("std_name") String std_name);

    @FormUrlEncoded
    @POST("api/example/leave_list")
    Call<LeaveModel> getleaveApplication(@Field("student_id") String std_id);

    @FormUrlEncoded
    @POST("api/example/p2t_message")
    Call<LeaveApplicationModel> sendTeacherMessage(@Field("parent_id") String userId,
                                            @Field("message") String msg,
                                            @Field("json_data") String json_data,
                                             @Field("admin") String admin);


    @FormUrlEncoded
    @POST("api/example/t2p_message")
    Call<LeaveApplicationModel> sendParentMessage(@Field("teacher_id") String teacher_id,
                                             @Field("message") String message,
                                              @Field("json_data") String json_data,
                                              @Field("admin") String admin);

    @FormUrlEncoded
    @POST("api/example/class_homework_list")
    Call<HomeWorkModel> loadHomeWork(@Field("class_id") String class_id,
                                     @Field("section_id") String section_id);
    @FormUrlEncoded
    @POST("api/example/teacher_homework_list")
    Call<HomeWorkModel> loadHomeWorkTeacher(@Field("teacher_id") String teacher_id,
                                            @Field("class_id") String class_id,
                                            @Field("section_id") String section_id);

    @FormUrlEncoded
    @POST("api/example/teacher_message_list")
    Call<MessageModel> getMessage(@Field("teacher_id") String id);

    @FormUrlEncoded
    @POST("api/example/parent_message_list")
    Call<MessageModel> getParentMessage(@Field("parent_id") String id);

    @GET("api/example/teacher_list")
    Call<MessageModel> getTeacherName();

    @GET("api/example/parent_listing")
    Call<MessageModel> getparentName();


    @Multipart
    @POST("api/example/give_homework")
    Call<HomeWorkModelUpload> uploadFile(@Part("hw_file") MultipartBody.Part file1,
                                         @Part("teacher_id") RequestBody  teacher_id,
                                         @Part("subject_id") RequestBody  subject_id,
                                         @Part("class_id") RequestBody  class_id,
                                         @Part("section_id") RequestBody  section_id,
                                         @Part("hw_date") RequestBody  hw_date,
                                         @Part("hw_desc") RequestBody  hw_desc);
  @FormUrlEncoded
  @POST("api/example/subjects_list")
    Call<GetSubjectModel> getSubjectList(@Field("class_id") String classid,
                                         @Field("section_id") String section_id);
  @GET
  Call<ResponseBody> downloadFileWithDynamicUrl(@Url String fileUrl);

  @FormUrlEncoded
  @POST("api/example/stuattendence_update")
  Call<AttendanceUpload> uploadAttendance(@Field("class_id") String class_id,
                                          @Field("section_id") String section_id,
                                          @Field("date") String date,
                                          @Field("json_data") String jsondata);
  @FormUrlEncoded
  @POST("api/example/send_notice_by_teacher")
    Call<HomeWorkModelUpload> noticeUpload(@Field("teacher_id") String teacher_id,
                                           @Field("class_id") String class_id,
                                           @Field("section_id") String section_id,
                                           @Field("publish_date") String publish_date,
                                           @Field("title") String title,
                                           @Field("message") String message);
    @FormUrlEncoded
    @POST("api/example/leave_list_for_teacher")
    Call<LeaveModel> getTeacherleaveApplication(@Field("class_id") String class_id,
                                                           @Field("section_id") String section_id);
   @FormUrlEncoded
    @POST("api/example/update_leave")
    Call<LeaveApplicationModel> approveLeave(@Field("leave_id") String lv_id,
                                             @Field("status") String std_id);
    @FormUrlEncoded
    @POST("api/example/driver_lat_lng")
    Call<DriverServiceModel> sendLatLog(@Field("lat") String lat,
                                        @Field("lng") String log);
    @GET("api/example/get_lat_lng")
    Call<GetLatLogBusModel> getBusLocation();

    @FormUrlEncoded
    @POST("api/example/digital_file_list")
    Call<DigitalParentModel> getDigitalTeaacherList(@Field("class_id") String class_id,
                                                    @Field("section_id") String section_id);
    @FormUrlEncoded
    @POST("api/example/update_device_id")
    Call<HomeWorkModel> updatedevicetoken(@Field("username") String username,
                                          @Field("device_id") String token);
    @FormUrlEncoded
    @POST("api/example/userDetails")
    Call<Login> syncData(@Field("type") String type,
                         @Field("id") String id);

    @GET("api/example/get_holiday_list")
    Call<HolidayModel> getHoliday();
}
