package com.trieffects.ConnecttwoSchool.Notification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.trieffects.ConnecttwoSchool.Activity.HomeActivity;
import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;

import java.util.List;
import java.util.Random;

/**
 * Created by Trieffects on 16-Nov-17.
 */

public class FireMsgService extends FirebaseMessagingService {

     static int countNo=0;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


            String title=remoteMessage.getData().get("title");
            String id=remoteMessage.getData().get("subtitle");
            String msg=remoteMessage.getData().get("message");
            countNo=countNo+1;
            setBadge(this,countNo,0);
            notificationShow(id,msg,title);


    }


    private void notificationShow(String id,String msg,String title){

        int item=1;
        if(id.equalsIgnoreCase("1")){
            if(!ApiUtils.isEmptyString(PrefrencesUtils.getNoticecount())){
                item=item+Integer.parseInt(PrefrencesUtils.getNoticecount());
            }
            PrefrencesUtils.saveNoticecount(String.valueOf(item));

        }else if(id.equalsIgnoreCase("2")){
            if(!ApiUtils.isEmptyString(PrefrencesUtils.getAttendancecount())){
                item=item+Integer.parseInt(PrefrencesUtils.getAttendancecount());
            }
            PrefrencesUtils.saveAttendancecount(String.valueOf(item));

        }else if(id.equalsIgnoreCase("3")){
            if(!ApiUtils.isEmptyString(PrefrencesUtils.getFeecount())){
                item=item+Integer.parseInt(PrefrencesUtils.getFeecount());
            }
            PrefrencesUtils.saveFeecount(String.valueOf(item));
        }else if(id.equalsIgnoreCase("4")){
            if(!ApiUtils.isEmptyString(PrefrencesUtils.getTimeTablecount())){
                item=item+Integer.parseInt(PrefrencesUtils.getTimeTablecount());
            }
            PrefrencesUtils.saveTimeTablecount(String.valueOf(item));
        }else if(id.equalsIgnoreCase("5")){
            if(!ApiUtils.isEmptyString(PrefrencesUtils.getMapcount())){
                item=item+Integer.parseInt(PrefrencesUtils.getMapcount());
            }
            PrefrencesUtils.savetMapcount(String.valueOf(item));
        }else if(id.equalsIgnoreCase("6")){
            if(!ApiUtils.isEmptyString(PrefrencesUtils.getResultcount())){
                item=item+Integer.parseInt(PrefrencesUtils.getResultcount());
            }
            PrefrencesUtils.saveResultcount(String.valueOf(item));
        }else if(id.equalsIgnoreCase("7")){
            if(!ApiUtils.isEmptyString(PrefrencesUtils.getMessagecount())){
                item=item+Integer.parseInt(PrefrencesUtils.getMessagecount());
            }
            PrefrencesUtils.saveMessagecount(String.valueOf(item));
        }else if(id.equalsIgnoreCase("8")){
            if(!ApiUtils.isEmptyString(PrefrencesUtils.getLeavecount())){
                item=item+Integer.parseInt(PrefrencesUtils.getLeavecount());
            }
            PrefrencesUtils.saveLeavecount(String.valueOf(item));
        }else if(id.equalsIgnoreCase("9")){
            if(!ApiUtils.isEmptyString(PrefrencesUtils.getDigitalcount())){
                item=item+Integer.parseInt(PrefrencesUtils.getDigitalcount());
            }
            PrefrencesUtils.saveDigitalcount(String.valueOf(item));
        }else if(id.equalsIgnoreCase("10")){
            if(!ApiUtils.isEmptyString(PrefrencesUtils.getHomecount())){
                item=item+Integer.parseInt(PrefrencesUtils.getHomecount());
            }
            PrefrencesUtils.saveHomecount(String.valueOf(item));
        }else{

        }

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                intent, PendingIntent.FLAG_ONE_SHOT);

        Notification notification;
        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
        notification = notificationBuilder.build();
        notification.defaults |= Notification.FLAG_FOREGROUND_SERVICE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(getRequestCode(), notification);

        Intent smsIntent = new Intent("otp");
        LocalBroadcastManager.getInstance(this).sendBroadcast(smsIntent);

    }

    public static void setBadge(Context context, int count,int check) {
        if(check==1){
            countNo=0;
        }
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }
    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    private static int getRequestCode() {
        Random rnd = new Random();
        return 100 + rnd.nextInt(900000);
    }
}