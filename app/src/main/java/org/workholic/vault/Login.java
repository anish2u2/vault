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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import services.files.properties.Props;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        File appCacheDir = new File(Environment.getExternalStorageDirectory(), Props.getInstance().getCacheLocation());
        if (!appCacheDir.exists()) {
            appCacheDir.mkdirs();
        }
        try {
            File secureCacheFile = new File(appCacheDir, Props.getInstance().getSecureFileName());
            FileInputStream inputStream = new FileInputStream(secureCacheFile);
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            byte[] data=new byte[4096];
            int length=0;
            while((length=inputStream.read(data))!=-1){
                outputStream.write(data,0,length);
            }
            inputStream.close();
            String storedPassword= new String(Base64.decode(outputStream.toByteArray(),1));

            AppCompatActivity activity = this;
            Button loginButton = findViewById(R.id.login);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String password=((EditText)findViewById(R.id.loginPassword)).getText().toString();
                    System.out.println("Stored password:"+storedPassword);
                    if(storedPassword.equals(password)){
                        Intent showOperations=new Intent(activity,Operations.class);
                        startActivity(showOperations);
                    }else{
                        ((TextView)findViewById(R.id.loginError)).setText("Incorrect password.");
                    }
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
