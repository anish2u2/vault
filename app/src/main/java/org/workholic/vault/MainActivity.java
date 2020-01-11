package org.workholic.vault;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.apache.log4j.Logger;
import org.workholic.contracts.FileOperations;
import org.workholic.impl.FileOperationsImpl;

import java.io.File;

import services.files.properties.Props;

public class MainActivity extends AppCompatActivity {




    private FileOperations fileOperations = new FileOperationsImpl();

    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            final ImageButton button = findViewById(R.id.vault);
            final AppCompatActivity activity=this;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File cacheDir=new File(Environment.getExternalStorageDirectory(), Props.getInstance().getCacheLocation());
                    if(!cacheDir.exists()){
                        cacheDir.mkdirs();
                        Intent registerIntent=new Intent(activity,Register.class);
                        startActivity(registerIntent);
                    }else{
                        Intent operationsIntent=new Intent(activity,Login.class);
                        startActivity(operationsIntent);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
