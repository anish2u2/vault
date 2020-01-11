package org.workholic.notification;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import services.files.contracts.Status;

public class NotificationWrapper {

    private Status status;

    private NotificationCompat.Builder notificationBuilder;

    private int id;

   public  NotificationWrapper(AppCompatActivity activity,String fileName){
        notificationBuilder=new NotificationCompat.Builder(activity);
        notificationBuilder.setContentTitle(fileName);
        notificationBuilder.setContentText("Encrypting "+fileName);
        notificationBuilder.setSmallIcon(android.support.compat.R.drawable.notification_icon_background);
    }

    public void updateStatus(NotificationManager notificationManager){
       if(Status.ProcessState.IN_PROGRESS.equals(status.getProcessState()))
        notificationBuilder.setProgress(100,status.status(),false);
       else if(Status.ProcessState.DONE.equals(status.getProcessState())){
           notificationBuilder.setProgress(100,100,false);
           notificationBuilder.setContentText("Encryption completed.").setProgress(0, 0, false);
       }else{
           notificationBuilder.setContentText("Unable to complete encryption.").setProgress(0, 0, false);
       }
        notificationManager.notify(this.id,notificationBuilder.build());
    }

    public void setStatus(Status status){
        this.status=status;
    }

    public Status getStatus(){
        return status;
    }

    public void setId(int id){
       this.id=id;
    }
}
