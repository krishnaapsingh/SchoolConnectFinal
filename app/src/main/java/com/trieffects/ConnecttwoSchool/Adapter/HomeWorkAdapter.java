package com.trieffects.ConnecttwoSchool.Adapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trieffects.ConnecttwoSchool.Interface.ApiInterface;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Model.HomeWorkData;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.support.v4.app.NotificationCompat.Builder;


public class HomeWorkAdapter extends RecyclerView.Adapter<HomeWorkAdapter.ViewHolder>{
    private NotificationManager mNotifyManager;
    private Builder mBuilder;
    int id = 1;
    Context mContext;
    String n;
    List<HomeWorkData> datalist;
    public HomeWorkAdapter(Context context,List<HomeWorkData> list){
        mContext=context;
        datalist=list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       // GridView grid;
        RelativeLayout name_teacher_layout;
        ImageView fil_view;
        TextView textnotice,subject_name,teachername_txt,date_txt;
        public ViewHolder(View itemView) {
            super(itemView);
            name_teacher_layout=(RelativeLayout)itemView.findViewById(R.id.name_teacher_layout);
            textnotice=(TextView)itemView.findViewById(R.id.textnotice);
            subject_name=(TextView)itemView.findViewById(R.id.subject_name);
            teachername_txt=(TextView)itemView.findViewById(R.id.teachername_txt);
            date_txt=(TextView)itemView.findViewById(R.id.date_txt);
            fil_view=(ImageView)itemView.findViewById(R.id.down_load);
           // grid=(GridView)itemView.findViewById(R.id.grid);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.home_work_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textnotice.setText(datalist.get(position).description);
        if(!PrefrencesUtils.getUserMode().equalsIgnoreCase("2")){
            if(!ApiUtils.isEmptyString(datalist.get(position).tname)) {
                holder.teachername_txt.setText(datalist.get(position).tname);
            }
        }else {
            holder.name_teacher_layout.setVisibility(View.GONE);
        }
        if(ApiUtils.isEmptyString(datalist.get(position).file_name)){
            holder.fil_view.setVisibility(View.GONE);
        }
        if(!ApiUtils.isEmptyString(datalist.get(position).subname)) {
            holder.subject_name.setText(datalist.get(position).subname);
        }
        String[] date =datalist.get(position).created_at.split(" ");
        holder.date_txt.setText(date[0]);


        holder.fil_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile("uploads/homework/"+datalist.get(position).file_name,datalist.get(position).file_name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }


public void downloadFile(String url,String name){
      n=name;
    Toast.makeText(mContext,"Download start",Toast.LENGTH_SHORT).show();
    final ApiInterface mApiService=ApiUtils.getInterfaceService();
    Call<ResponseBody> mService=mApiService.downloadFileWithDynamicUrl(url);
    mService.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if(response.isSuccess()){
                mNotifyManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(mContext);
                mBuilder.setContentTitle(n)
                        .setContentText("Download in progress")
                        .setSmallIcon(R.mipmap.ic_launcher);
                boolean writtenToDisk = writeResponseBodyToDisk(response.body(),n);
            }else {
                Toast.makeText(mContext,"Try again",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    });

}


    private boolean writeResponseBodyToDisk(ResponseBody body,String name) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(mContext.getExternalFilesDir(null) + File.separator+name);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("File Download: " , fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("file://"+futureStudioIconFile.getAbsolutePath()));
            //    String mimeType = myMime.getMimeTypeFromExtension(fileExt(name).substring(1));
                String mimeType = myMime.getMimeTypeFromExtension(fileExt(name));
                intent1.setDataAndType(Uri.fromFile(futureStudioIconFile),mimeType);
                TaskStackBuilder stackBuilder = TaskStackBuilder
                        .create(mContext);
                stackBuilder.addNextIntent(intent1);
                final PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder.setContentText("Download complete");
                // Removes the progress bar
                mBuilder.setProgress(0, 0, false);
                mBuilder.setContentIntent(resultPendingIntent);
                mNotifyManager.notify(id, mBuilder.build());
                Toast.makeText(mContext,"Download complete",Toast.LENGTH_SHORT).show();
                return true;
            } catch (IOException e) {
                mBuilder.setContentText("Download Faild");
                // Removes the progress bar
                mBuilder.setProgress(0, 0, false);
                mNotifyManager.notify(id, mBuilder.build());
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            mBuilder.setContentText("Download Faild");
            // Removes the progress bar
            mBuilder.setProgress(0, 0, false);
            mNotifyManager.notify(id, mBuilder.build());
            return false;
        }
    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }


}
