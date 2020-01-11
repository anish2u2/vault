package org.workholic.contracts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public interface FileOperations {

    void saveData(AppCompatActivity activity, Intent data);

    void readEncryptedFiles(AppCompatActivity activity);
}
