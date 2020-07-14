package com.mobileapp.weatherpro;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.mobileapp.weatherpro.databinding.ActivitySubBinding;

public class SubActivity extends AppCompatActivity {
private String minTemp,maxTemp,actTemp,cityName;
private ActivitySubBinding subBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subBinding = ActivitySubBinding.inflate(getLayoutInflater());
        setContentView(subBinding.getRoot());
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.round_drawable));

         minTemp = getIntent().getStringExtra("minTemp");
        maxTemp = getIntent().getStringExtra("maxTemp");
        actTemp = getIntent().getStringExtra("actTemp");
        cityName = getIntent().getStringExtra("cityName");

        subBinding.state.setText(cityName);


         setMinTemp(minTemp);
         setMaxTemp(maxTemp);
         setActTemp(actTemp);





    }

    private void setActTemp(String actTemp) {
        SpannableString spannableString = new SpannableString(actTemp);
        int index = actTemp.indexOf(".");
        if(index!=-1){
            spannableString.setSpan(new RelativeSizeSpan((float) 1.5),0,index,0);
        }else {
            spannableString.setSpan(new RelativeSizeSpan((float) 1.5),0,actTemp.length(),0);
        }

        subBinding.actTempdata.setText(spannableString+"\u2103");

    }

    private void setMaxTemp(String maxTemp) {
        SpannableString spannableString = new SpannableString(maxTemp);
        int index = maxTemp.indexOf(".");
        System.out.println("index___"+index);
        if(index!=-1){
            spannableString.setSpan(new RelativeSizeSpan((float) 1.5),0,index,0);
        }else {
            spannableString.setSpan(new RelativeSizeSpan((float) 1.5),0,maxTemp.length(),0);
        }

        subBinding.maxTempdata.setText(spannableString+"\u2103");
    }

    private void setMinTemp(String minTemp) {
        SpannableString spannableString = new SpannableString(minTemp);
        int index = minTemp.indexOf(".");
        if(index!=-1){
            spannableString.setSpan(new RelativeSizeSpan((float) 1.5),0,index,0);
        }else {
            spannableString.setSpan(new RelativeSizeSpan((float) 1.5),0,minTemp.length(),0);
        }

        subBinding.minTempdata.setText(spannableString+"\u2103");
    }
}