package org.workholic.listener;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.List;
import java.util.Queue;

import services.files.crypto.Cipher;
import services.files.properties.Props;
import services.files.streams.CryptoFileReader;

public class CustomLinearLayoutListener implements  View.OnGenericMotionListener {

    private List<String> listOfFile;

    private ImageView view;

    Cipher cipher=new Cipher();

    CryptoFileReader fileReader=new CryptoFileReader();

    private int currentVal=0;
    {
        cipher.setProps(Props.getInstance());
        fileReader.setCrypto(cipher);
    }

    @Override
    public boolean onGenericMotion(View v, MotionEvent event) {
        try {
            System.out.println("Trying to scroll");
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                System.out.println("previous pic");
                if (currentVal > 0) {
                    view.setImageBitmap(BitmapFactory.decodeStream(fileReader.readFile(listOfFile.get(--currentVal))));
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                System.out.println("next pic");
                if (currentVal < (listOfFile.size() - 1)) {
                    view.setImageBitmap(BitmapFactory.decodeStream(fileReader.readFile(listOfFile.get(++currentVal))));
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }



    public  void setListOfFile(List<String> listOfFile){
        this.listOfFile=listOfFile;
    }

    public  void setView(ImageView view){
        this.view=view;
    }
}
