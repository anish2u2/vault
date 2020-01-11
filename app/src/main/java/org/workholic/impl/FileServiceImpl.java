package org.workholic.impl;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.graphics.PathUtils;
import android.support.v7.app.AppCompatActivity;

import org.workholic.contracts.FileService;
import org.workholic.notification.NotificationService;
import org.workholic.notification.NotificationWrapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.SimpleTimeZone;

import services.files.contracts.FileData;
import services.files.contracts.FileHandlerJob;
import services.files.contracts.Status;
import services.files.imple.FileHandlerJobImpl;
import services.files.imple.StatusImpl;
import services.files.properties.Props;

public class FileServiceImpl implements FileService {

    private FileHandlerJob job=new FileHandlerJobImpl();

    private NotificationService notificationService;

    Random random=new Random();

    private static String  SECURE_FILE_DIR;

    private static final String DIR_NAME="Vault-Secured-Files";

    @Override
    public String getStorageLocation() {
        try{
            File root=new File("/storage");
            File[] sdCard=null;
            if(root.isDirectory()){
                sdCard=root.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return !(name.equals("sdcard1")||name.equals("self")||name.equals("emulated"));
                    }
                });

            }
            return new File(sdCard[0],DIR_NAME).getAbsolutePath();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void initiateEncryptionJob(AppCompatActivity activity, Intent data) {
        try {
            notificationService = new NotificationService(activity);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            File cacheDir = new File(Environment.getExternalStorageDirectory(), Props.getInstance().getCacheLocation());
            File externalStorageDir = new File(cacheDir, Props.getInstance().getCacheFileName());
            FileInputStream inputStreamReader = new FileInputStream(externalStorageDir);
            byte[] externalFileLoc = new byte[4096];
            int length = 0;
            while ((length=inputStreamReader.read(externalFileLoc))!=-1) {
                byteArrayOutputStream.write(externalFileLoc);
            }
            inputStreamReader.close();
            File root=new File("/storage");
            File[] sdCard=null;
            if(root.isDirectory()){
                sdCard=root.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return !(name.equals("sdcard1")||name.equals("self")||name.equals("emulated"));
                    }
                });

            }
            SECURE_FILE_DIR=sdCard[0].getAbsolutePath();//System.getenv("SECONDARY_STORAGE").split(":")[0];

            File dir = new File(new File(SECURE_FILE_DIR), DIR_NAME);

            if (!dir.exists()) {
                System.out.println("Directory does not exists.");
                dir.mkdirs();
            }

            Status status = new StatusImpl();
            status.updateStatus(Status.ProcessState.IN_PROGRESS);
            Cursor record = activity.getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATA}, null, null, null);
            record.moveToFirst();
            String name = record.getString(record.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            NotificationWrapper notificationWrapper = new NotificationWrapper(activity, name);
            notificationWrapper.setId(143);
            notificationWrapper.setStatus(status);
            notificationService.startNotification(notificationWrapper);
            File selectedFile = new File(dir, name);
            String size = record.getString(record.getColumnIndex(MediaStore.Images.Media.SIZE));

            System.out.println("On delete operation:");
            job.encryptAndSave(new FileData() {
                @Override
                public Status getStatus() {
                    return status;
                }

                @Override
                public InputStream getInputStream() throws Exception {
                    return activity.getContentResolver().openInputStream(data.getData());
                }

                @Override
                public void delete() throws Exception {
                    try{
                    System.out.println("Deleting file.");
                    DocumentsContract.deleteDocument(activity.getContentResolver(),data.getData());
                    System.out.println("File has been deleted");}catch (Exception ex){
                        ex.printStackTrace();
                        throw ex;
                    }
                }

                @Override
                public OutputStream getFileOutputStream() throws Exception {
                    return new FileOutputStream(selectedFile);
                }

                @Override
                public long getFileSize() {
                    return Long.valueOf(size);
                }
            });
            record.close();
        }catch (Exception exce){
            exce.printStackTrace();
        }
    }

    @Override
    public void initiateEncryptionJob(AppCompatActivity activity, List<ClipData.Item> items) {
        try{
        notificationService=new NotificationService(activity);
        List<FileData> fileDataList=new ArrayList<>(items.size());
            File root=new File("/storage");
            File[] sdCard=null;
            if(root.isDirectory()){
                sdCard=root.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return !(name.equals("sdcard1")||name.equals("self")||name.equals("emulated"));
                    }
                });

            }
            SECURE_FILE_DIR=sdCard[0].getAbsolutePath();

        File dir = new File(new File(SECURE_FILE_DIR), DIR_NAME);
        if(!dir.exists()){
            dir.mkdirs();
        }
        Iterator<ClipData.Item> itemIterator=items.iterator();
        List<NotificationWrapper> notificationWrapperList=new ArrayList<>(items.size());
        int counter=198;
        while (itemIterator.hasNext()) {
            ClipData.Item item = itemIterator.next();
            Status status = new StatusImpl();
            status.updateStatus(Status.ProcessState.IN_PROGRESS);
            Cursor record = activity.getContentResolver().query(item.getUri(), new String[]{MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATA}, null, null, null);
            record.moveToFirst();
            String name = record.getString(record.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            NotificationWrapper notificationWrapper=new NotificationWrapper(activity,name);
            notificationWrapper.setId(counter++);
            notificationWrapper.setStatus(status);
            notificationWrapperList.add(notificationWrapper);
            File selectedFile = new File(dir, name);
            String size = record.getString(record.getColumnIndex(MediaStore.Images.Media.SIZE));
            fileDataList.add(new FileData() {
                @Override
                public Status getStatus() {
                    return status;
                }

                @Override
                public void delete() throws Exception {
                    DocumentsContract.deleteDocument(activity.getContentResolver(),item.getUri());
                }

                @Override
                public InputStream getInputStream() throws Exception {
                    return activity.getContentResolver().openInputStream(item.getUri());

                }

                @Override
                public OutputStream getFileOutputStream() throws Exception {
                    return new FileOutputStream(selectedFile);
                }

                @Override
                public long getFileSize() {
                    return Long.valueOf(size);
                }
            });
            record.close();
        }
        notificationService.startNotification(notificationWrapperList);
        job.encryptAndSave(fileDataList);
    }catch (Exception exc){
        exc.printStackTrace();
        }
    }
}
