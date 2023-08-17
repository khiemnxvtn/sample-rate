package com.example.librate;

import android.content.Context;
import android.view.ViewGroup;

import com.example.librate.callback.onCallBack;


public class RateUtils {
    public static void showDialogRate(Context context, onCallBack callbackListener) {
        RateAppDiaLog dialog = new RateAppDiaLog(context, callbackListener);
        int w = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
        int h = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(w, h);
        dialog.setCancelable(true);
        dialog.show();
    }
}
