package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
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
                .setArrStar(new int[]{R.drawable.ic_mstar_0,R.drawable.ic_mstar_1,R.drawable.ic_mstar_2,R.drawable.ic_mstar_3,R.drawable.ic_mstar_4,R.drawable.ic_mstar_5})
                .setTextTitle("Rate us")
                .setTextContent("Tap a star to set your rating")
                .setTextButton("Rate now","Not now")
                .setTextTitleColor(Color.parseColor("#000000"))
                .setTextNotNowColor(Color.parseColor("#EDEDED"))
                .setDrawableButtonRate(R.drawable.border_rate)
                .setBackgroundDialog(R.drawable.border_bg_dialog)
                .setBackgroundStar(R.drawable.border_bg_star)
                .setColorRatingBar("#FAFF00")
                .setColorRatingBarBG("#E0E0E0")
                .setNumberRateInApp(5)
                .setFontFamily(ResourcesCompat.getFont(this, R.font.poppins_regular))
                .setFontFamilyTitle(ResourcesCompat.getFont(this, R.font.poppins_semibold))
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