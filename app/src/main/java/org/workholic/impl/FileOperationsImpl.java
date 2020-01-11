package org.workholic.impl;

import android.app.NotificationManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.workholic.contracts.FileOperations;
import org.workholic.contracts.FileService;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import services.files.contracts.Crypto;
import services.files.contracts.Status;
import services.files.crypto.Cipher;
import services.files.imple.CryptoFilerService;
import services.files.imple.StatusImpl;
import services.files.properties.Props;
import services.files.streams.CryptoFileWriter;

public class FileOperationsImpl implements FileOperations {

    FileService fileService=new FileServiceImpl();

    @Override
    public void saveData(AppCompatActivity activity, Intent data) {
        try {
            if(data.getData()!=null) {
                fileService.initiateEncryptionJob(activity,data);
            }else if(data.getClipData()!=null){
                ClipData clipData=data.getClipData();
                List<ClipData.Item> itemList= new ArrayList< >(clipData.getItemCount());
                for(int counter=0;counter<clipData.getItemCount();counter++){
                    itemList.add(clipData.getItemAt(counter));
                    fileService.initiateEncryptionJob(activity,itemList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readEncryptedFiles(AppCompatActivity activity) {

    }


}
