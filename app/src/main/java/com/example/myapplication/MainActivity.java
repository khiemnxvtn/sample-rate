package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.widget.Toast;

import com.example.librate.RateAppDiaLog;
import com.example.librate.callback.IClickBtn;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        RateAppDiaLog rateAppDiaLog = new RateAppDiaLog.Builder(this)
                .setTextTitle("Your Opinion Matter To Us!")
                .setTextContent("If you enjoy this Office Reader, would you mind rating us on the Google Play, then?")
                .setTextButton("Rate us","Not now")
                .setTextTitleColorLiner("#F47500","#FFBC3A")
                .setDrawableButtonRate(R.drawable.border_rate)
                .setBackgroundDialog(R.drawable.border_rate)
                .setColorRatingBar("#EC5656")
                .setNumberRateInApp(5)
                .setFontFamily(ResourcesCompat.getFont(this, R.font.xxx))
                .setOnclickBtn(new IClickBtn() {
                    @Override
                    public void onclickNotNow() {
                        Toast.makeText(MainActivity.this,"onclickNotNow",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClickRate(float rate) {
                        Toast.makeText(MainActivity.this,rate+"",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReviewAppSuccess() {
                        finishAffinity();
                    }
                })
                .build();

        rateAppDiaLog.show();

    }
}