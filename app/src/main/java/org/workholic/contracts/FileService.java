package org.workholic.contracts;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import services.files.contracts.FileData;

public interface FileService {

        void initiateEncryptionJob(AppCompatActivity activity, List<ClipData.Item> items);

        void initiateEncryptionJob(AppCompatActivity activity, Intent data);

        String getStorageLocation();

}
