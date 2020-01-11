package org.workholic.vault;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.workholic.contracts.FileService;
import org.workholic.impl.FileServiceImpl;
import org.workholic.listener.CustomLinearLayoutListener;
import org.workholic.widget.CustomeScrollviewLayout;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import services.files.contracts.Crypto;
import services.files.contracts.FileServices;
import services.files.crypto.Cipher;
import services.files.imple.FileServiceImple;
import services.files.imple.StatusImpl;
import services.files.properties.Props;
import services.files.streams.CryptoFileReader;

public class View extends AppCompatActivity {

    FileService fileService=new FileServiceImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        LinearLayout layout=findViewById(R.id.imageScrollLayout);
        try{
            String storageLocation=fileService.getStorageLocation();
            File[] listOfImages=new File(storageLocation).listFiles();
            Cipher cipher=new Cipher();
            cipher.setProps(Props.getInstance());
            List<String> listOfFileLocation=new LinkedList<>();
            for (File imageFile:listOfImages){
                listOfFileLocation.add(imageFile.getAbsolutePath());
            }
            CryptoFileReader fileReader=new CryptoFileReader();
            fileReader.setCrypto(cipher);
            ImageView imageView=new ImageView(this);
            imageView.setImageBitmap(BitmapFactory.decodeStream(fileReader.readFile(listOfFileLocation.get(0))));
            imageView.setVisibility(android.view.View.VISIBLE);
            layout.addView(imageView);
            CustomLinearLayoutListener listener=new CustomLinearLayoutListener();
            listener.setListOfFile(listOfFileLocation);
            listener.setView(imageView);
            layout.setOnGenericMotionListener(listener);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
