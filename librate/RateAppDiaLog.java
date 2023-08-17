package com.rate.amazic;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.content.ContextCompat;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.rate.amazic.callback.IClickBtn;
import com.rate.amazic.callback.onCallBack;

public class RateAppDiaLog extends Dialog {
    private Handler handler;
    private onCallBack callback;
    private TextView tvTitle, tvContent,btnRate,btnNotnow;
    private Builder builder;
    private Context context;
    private ImageView imgRate;
    private AppCompatRatingBar rtb;
    private LinearLayout dialog;

    public RateAppDiaLog(@NonNull Context context, onCallBack callback) {
        super(context);
        this.callback = callback;
    }

    public RateAppDiaLog(Activity context, Builder builder) {
        super(context);
        this.context = context;
        this.builder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_rate);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = -1;
        getWindow().setAttributes(attributes);
        initView();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        imgRate = findViewById(R.id.imgRate);
        rtb = findViewById(R.id.rtb);
        btnRate = findViewById(R.id.btnRate);
        btnNotnow = findViewById(R.id.btnNotnow);
        dialog = findViewById(R.id.dialog);
        if (builder.title != null)
            tvTitle.setText(builder.title);
        if (builder.content != null)
            tvContent.setText(builder.content);
        if (builder.titleColor != 0)
            tvTitle.setTextColor(builder.titleColor);
        if (builder.contentColor != 0)
            tvContent.setTextColor(builder.contentColor);
        if(builder.rateUsColor!=0){
            btnRate.setTextColor(builder.rateUsColor);
        }
        if(builder.notNowColor!=0){
            btnNotnow.setTextColor(builder.notNowColor);
        }

        if(builder.colorStart!=null&&builder.colorEnd!=null){
            TextPaint paint = tvTitle.getPaint();
            float width = paint.measureText(tvTitle.getText().toString());
            Shader textShader = new LinearGradient(0, 0, width, tvTitle.getTextSize(), new int[]{   Color.parseColor(builder.colorStart),
                    Color.parseColor(builder.colorEnd),}, null, Shader.TileMode.CLAMP);
            tvTitle.getPaint().setShader(textShader);
        }


        if(builder.titleSize!=0){
            tvTitle.setTextSize(builder.titleSize);
        }
        if(builder.notNow!=null&&builder.rateUs!=null){
            btnRate.setText(builder.rateUs);
            btnNotnow.setText(builder.notNow);
        }
        if(builder.drawableRateUs!=0){
            btnRate.setBackgroundResource(builder.drawableRateUs);
        }
        if(builder.contentSize!=0){
            tvContent.setTextSize(builder.contentSize);
        }
        if(builder.typeface!=null){
            tvTitle.setTypeface(builder.typeface);
            tvContent.setTypeface(builder.typeface);
            btnRate.setTypeface(builder.typeface);
            btnNotnow.setTypeface(builder.typeface);
        }
        if(builder.drawableDialog!=0){
            dialog.setBackgroundResource(builder.drawableDialog);
        }

        btnNotnow.setOnClickListener(v->{builder.onClickBtn.onclickNotNow();dismiss();});
        btnRate.setOnClickListener(v->{builder.onClickBtn.onClickRate(rtb.getRating());
            if(rtb.getRating()>=builder.numberRateInApp){
                if(builder.isRateInApp){
                    reviewApp(context);
                }else{
                    dismiss();
                }
            }else{
                dismiss();
            }

        });

        changeRating();

        if(builder.colorRatingBar!=null)
        rtb.setProgressTintList(ColorStateList.valueOf(Color.parseColor(builder.colorRatingBar)));

        if(builder.numberRateDefault>0&&builder.numberRateDefault<6){
            rtb.setRating(builder.numberRateDefault);
        }
    }
    public void reviewApp(Context context) {
        ReviewManager manager = ReviewManagerFactory.create(context);
        com.google.android.play.core.tasks.Task<com.google.android.play.core.review.ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(((Activity) context), reviewInfo);
                        flow.addOnCompleteListener(task2 -> {
                            builder.onClickBtn.onReviewAppSuccess();
                            dismiss();
                        });
                    } else {
                        Log.e("ReviewError", "" + task.getException().toString());
                    }
                }
        );
    }

    public void changeRating() {
        rtb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String getRating = String.valueOf(rtb.getRating());
                switch (getRating) {
                    case "1.0":
                     //   btnRate.setText("Thank you!");
                        imgRate.setImageResource(R.drawable.ic_star_1);
                      //  btnLater.setVisibility(View.GONE);
                        /* tvTitle.setTypeface(null, Typeface.BOLD);*/

                        break;
                    case "2.0":
                       // editFeedback.setVisibility(View.GONE);
                     //   btnRate.setText("Thank you!");
                        imgRate.setImageResource(R.drawable.ic_star_2);
                     //   btnLater.setVisibility(View.GONE);
                        /* tvTitle.setTypeface(null, Typeface.BOLD);*/

                        break;
                    case "3.0":
                      //  editFeedback.setVisibility(View.GONE);
                     //   btnRate.setText("Thank you!");
                        imgRate.setImageResource(R.drawable.ic_star_3);
                     //   btnLater.setVisibility(View.GONE);
                        /*tvTitle.setTypeface(null, Typeface.BOLD);*/

                        break;
                    case "4.0":
                      //  editFeedback.setVisibility(View.GONE);
                     //   btnRate.setText("Thank you!");
                        imgRate.setImageResource(R.drawable.ic_star_4);
                     //   btnLater.setVisibility(View.GONE);
                        /*  tvTitle.setTypeface(null, Typeface.BOLD);*/

                        break;
                    case "5.0":
                     //   editFeedback.setVisibility(View.GONE);
                     //   btnRate.setText("Thank you!");
                        imgRate.setImageResource(R.drawable.ic_star_5);
                     //   btnLater.setVisibility(View.GONE);

                        /* tvTitle.setTypeface(null, Typeface.BOLD);*/

                        break;
                    default:
                    //    btnRate.setText("Rate us");
                   //     editFeedback.setVisibility(View.GONE);
                        imgRate.setImageResource(R.drawable.ic_star_0);
                   //     btnLater.setVisibility(View.VISIBLE);


                        break;
                }
            }
        });
    }
    public static class Builder {
        private String title, content,rateUs,notNow;
        private int titleColor = 0, contentColor = 0, rateUsDra,rateUsColor=0,notNowColor=0;
        private String colorStart,colorEnd;
        private int titleSize=0,contentSize=0;
        private final Activity context;
        private int drawableRateUs =0;
        private  IClickBtn onClickBtn;
        private boolean isExitApp = false;
        private boolean isRateInApp = true;
        private int numberRateInApp = 4;
        private String colorRatingBar;
        private Typeface typeface = null;
        private int drawableDialog =0;
        private int numberRateDefault =0;

        public Builder(Activity context) {
            this.context = context;
        }

        public Builder setTextTitle(String title) {
            this.title = title;
            return this;
        }
        public Builder setTextContent(String content) {
            this.content = content;
            return this;
        }
        public Builder setTextButton(String rateUs,String notNow){
            this.rateUs = rateUs;
            this.notNow = notNow;
            return this;
        }

        public Builder setRateInApp(Boolean isRateInApp){
            this.isRateInApp = isRateInApp;
            return this;
        }

        public Builder setTextTitleColorLiner(String colorStart, String colorEnd){
            this.colorStart  = colorStart;
            this.colorEnd  = colorEnd;
            return this;
        }

        public Builder setDrawableButtonRate(int drawable){
            this.drawableRateUs = drawable;
            return this;
        }
        public Builder setTextTitleColor(int color) {
            this.titleColor = color;
            return this;
        }
        public Builder setTextRateUsColor(int color) {
            this.rateUsColor = color;
            return this;
        }
        public Builder setTextNotNowColor(int color) {
            this.notNowColor = color;
            return this;
        }
        public Builder setTextTitleSize(int titleSize) {
            this.titleSize = titleSize;
            return this;
        }
        public Builder setTextContentSize(int contentSize){
            this.contentSize = contentSize;
            return this;
        }
        public Builder setTextContentColor(int color) {
            this.contentColor = color;
            return this;
        }
        public Builder setColorRatingBar(String color){
            this.colorRatingBar = color;
            return this;
        }

        public Builder setOnclickBtn(IClickBtn onClickBtn) {
            this.onClickBtn = onClickBtn;
            return this;
        }
       /* public Builder setExitApp(Boolean isExitApp) {
            this.isExitApp = isExitApp;
            return this;
        }*/
        public Builder setNumberRateInApp(int numberRate) {
            this.numberRateInApp = numberRate;
            return this;
        }
        public Builder setFontFamily(Typeface typeface){
            this.typeface = typeface;
            return this;
        }
        public Builder setBackgroundDialog(int drawable){
            this.drawableDialog = drawable;
            return this;
        }

        public Builder setNumberRateDefault(int number){
            this.numberRateDefault = number;
            return this;
        }

        public RateAppDiaLog build() {
            return new RateAppDiaLog(context, this);
        }

    }
}
