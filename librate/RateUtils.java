package com.rate.amazic;

import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;

import com.rate.amazic.callback.onCallBack;

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
