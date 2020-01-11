package org.workholic.vault;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.MimeTypeFilter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import org.workholic.contracts.FileOperations;
import org.workholic.impl.FileOperationsImpl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Writer;

import services.files.properties.Props;

public class Operations extends AppCompatActivity {

    public static final int PICK_FILE_LOCATION= 4345;

    public static final int PICK_IMAGE_REQUEST_ID = 1093;

    private FileOperations fileOperations = new FileOperationsImpl();

    private String secureDirLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton hideImages=findViewById(R.id.hideFiles);
        ImageButton showImages=findViewById(R.id.showFiles);
        try{
            File cacheDir=new File(Environment.getExternalStorageDirectory(), Props.getInstance().getCacheLocation());
            File externalStorageDir=new File(cacheDir,Props.getInstance().getCacheFileName());
            if(!externalStorageDir.exists()){
                Intent fileLocationLocator=new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                //fileLocationLocator.setType("folder/*");
                startActivityForResult(Intent.createChooser(fileLocationLocator,"Select Directory"),PICK_FILE_LOCATION);
            }
        hideImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select pictures"), PICK_IMAGE_REQUEST_ID);
            }


        });
            final AppCompatActivity activity=this;
            showImages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent showImageGallery=new Intent(activity, org.workholic.vault.View.class);
                    startActivity(showImageGallery);
                }
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_ID && resultCode == RESULT_OK && data != null) {
            fileOperations.saveData(this, data);
        }else if(requestCode == PICK_FILE_LOCATION && resultCode == RESULT_OK && data != null){
           try {
               secureDirLocation = data.getDataString();

               File cacheDir = new File(Environment.getExternalStorageDirectory(), Props.getInstance().getCacheLocation());
               File externalStorageDir = new File(cacheDir, Props.getInstance().getCacheFileName());
               if (!externalStorageDir.exists()) {
                   externalStorageDir.createNewFile();
               }
               System.out.println("External dir location:"+secureDirLocation);
               BufferedOutputStream outputStream= new BufferedOutputStream(new FileOutputStream(externalStorageDir));
               outputStream.write(secureDirLocation.getBytes());
               outputStream.flush();
               outputStream.close();
           }catch (Exception ex){
               ex.printStackTrace();
           }
        }

    }

}
