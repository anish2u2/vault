package org.workholic.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.ListIterator;

import services.files.contracts.Status;

public class NotificationService {

    NotificationManager notificationManager = null;

    private NotificationWrapper wrapper;

    private List<NotificationWrapper> notificationWrapperList;

    public NotificationService(AppCompatActivity activity) {
        this.notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void startNotification(NotificationWrapper wrapper) {
        this.wrapper = wrapper;
        startNotifying();
    }

    public void startNotification(List<NotificationWrapper> wrapperList) {
        this.notificationWrapperList = wrapperList;
        startNotifying();
    }

    public void startNotifying() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean flag = true;
                    if (wrapper != null) {
                        while (Status.ProcessState.IN_PROGRESS.equals(wrapper.getStatus().getProcessState())) {
                            wrapper.updateStatus(notificationManager);
                            Thread.sleep(30);
                        }
                    } else {
                        while (flag) {
                            if (notificationWrapperList.size() > 0) {
                                ListIterator<NotificationWrapper> notification = notificationWrapperList.listIterator();
                                while (notification.hasNext()) {
                                    NotificationWrapper nw = notification.next();
                                    if (Status.ProcessState.IN_PROGRESS.equals(nw.getStatus().getProcessState())) {
                                        nw.updateStatus(notificationManager);
                                    } else {
                                        notification.remove();
                                    }
                                }
                            } else {
                                flag = false;
                            }
                            Thread.sleep(30);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

}
