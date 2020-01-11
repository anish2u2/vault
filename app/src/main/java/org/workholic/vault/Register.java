package org.workholic.vault;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

import services.files.properties.Props;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final AppCompatActivity activity=this;
        Button savePassword=findViewById(R.id.savePassword);
        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String password = ((EditText) findViewById(R.id.loginPassword2)).getText().toString();
                    String confirmPassword = ((EditText) findViewById(R.id.confirmPassword)).getText().toString();
                    ((TextView) findViewById(R.id.result)).setText("");
                    if (password.equals(confirmPassword)) {
                        File appCacheDir = new File(Environment.getExternalStorageDirectory(), Props.getInstance().getCacheLocation());
                        if (!appCacheDir.exists()) {
                            appCacheDir.mkdirs();
                        }
                        File secureCacheFile = new File(appCacheDir, Props.getInstance().getSecureFileName());
                        if (!secureCacheFile.exists()) {
                            secureCacheFile.createNewFile();
                        }
                        FileOutputStream outputStream=new FileOutputStream(secureCacheFile);
                        outputStream.write(Base64.encode(password.getBytes(),1));
                        outputStream.flush();outputStream.close();
                        Intent loginIntent=new Intent(activity,Login.class);
                        startActivity(loginIntent);
                    } else {
                        ((TextView) findViewById(R.id.result)).setText("Password do not match.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

}
