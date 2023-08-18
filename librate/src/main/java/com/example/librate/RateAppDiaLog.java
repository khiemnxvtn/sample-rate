package com.example.librate;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;

import com.example.librate.callback.IClickBtn;
import com.example.librate.callback.onCallBack;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;


public class RateAppDiaLog extends Dialog {
    private TextView tvTitle, tvContent, btnRate, btnNotnow;
    private Builder builder;
    private Context context;
    private ImageView imgRate;
    private AppCompatRatingBar rtb;
    private LinearLayout dialog;
    private RelativeLayout bg_star;

    private boolean isZoomedIn = false;


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
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
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
        bg_star = findViewById(R.id.bg_star);
        if (builder.title != null)
            tvTitle.setText(builder.title);
        if (builder.content != null)
            tvContent.setText(builder.content);
        if (builder.titleColor != 0)
            tvTitle.setTextColor(builder.titleColor);
        if (builder.contentColor != 0)
            tvContent.setTextColor(builder.contentColor);
        if (builder.rateUsColor != 0) {
            btnRate.setTextColor(builder.rateUsColor);
        }
        if (builder.notNowColor != 0) {
            btnNotnow.setTextColor(builder.notNowColor);
        }

        if (builder.colorStart != null && builder.colorEnd != null) {
            TextPaint paint = tvTitle.getPaint();
            float width = paint.measureText(tvTitle.getText().toString());
            Shader textShader = new LinearGradient(0, 0, width, tvTitle.getTextSize(), new int[]{Color.parseColor(builder.colorStart),
                    Color.parseColor(builder.colorEnd),}, null, Shader.TileMode.CLAMP);
            tvTitle.getPaint().setShader(textShader);
        }


        if (builder.titleSize != 0) {
            tvTitle.setTextSize(builder.titleSize);
        }
        if (builder.notNow != null && builder.rateUs != null) {
            btnRate.setText(builder.rateUs);
            btnNotnow.setText(builder.notNow);
        }
        if (builder.drawableRateUs != 0) {
            btnRate.setBackgroundResource(builder.drawableRateUs);
        }
        if (builder.contentSize != 0) {
            tvContent.setTextSize(builder.contentSize);
        }
        if (builder.typeface != null) {
            tvTitle.setTypeface(builder.typeface);
            tvContent.setTypeface(builder.typeface);
            btnRate.setTypeface(builder.typeface);
            btnNotnow.setTypeface(builder.typeface);
        }
        if (builder.typefaceTitle != null) {
            tvTitle.setTypeface(builder.typefaceTitle);
        }
        if (builder.typefaceContent != null) {
            tvContent.setTypeface(builder.typefaceContent);
        }
        if (builder.typefaceRateUs != null) {
            btnRate.setTypeface(builder.typefaceRateUs);
        }
        if (builder.typefaceNotNow != null) {
            btnNotnow.setTypeface(builder.typefaceNotNow);
        }

        if (builder.drawableDialog != 0) {
            dialog.setBackgroundResource(builder.drawableDialog);
        }
        if (builder.drawableBgStar != 0) {
            bg_star.setBackgroundResource(builder.drawableBgStar);
        }

        btnNotnow.setOnClickListener(v -> {
            builder.onClickBtn.onclickNotNow();
            dismiss();
        });
        btnRate.setOnClickListener(v -> {
            builder.onClickBtn.onClickRate(rtb.getRating());
            if (rtb.getRating() >= builder.numberRateInApp) {
                if (builder.isRateInApp) {
                    reviewApp(context);
                } else {
                    dismiss();
                }
            } else {
                if (rtb.getRating() > 0) {
                    dismiss();
                }
            }

        });

        changeRating();

        if (builder.colorRatingBar != null)
            rtb.setProgressTintList(ColorStateList.valueOf(Color.parseColor(builder.colorRatingBar)));
        if (builder.colorRatingBarBg != null)
            rtb.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor(builder.colorRatingBarBg)));

        if (builder.numberRateDefault > 0 && builder.numberRateDefault < 6) {
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
                animationStar();
                switch (getRating) {
                    case "1.0":
                        imgRate.setImageResource(builder.arrStar[1]);
                        break;
                    case "2.0":
                        imgRate.setImageResource(builder.arrStar[2]);
                        break;
                    case "3.0":
                        imgRate.setImageResource(builder.arrStar[3]);
                        break;
                    case "4.0":
                        imgRate.setImageResource(builder.arrStar[4]);
                        break;
                    case "5.0":
                        imgRate.setImageResource(builder.arrStar[5]);
                        break;
                    default:
                        rtb.setRating(1f);
                        imgRate.setImageResource(builder.arrStar[0]);
                        break;
                }
            }
        });
    }

    private void animationStar() {
        if (!isZoomedIn) {
            animateZoomIn();
        }
    }

    private void animateRotation(){
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(imgRate, "rotation", 0f, 360f);
        rotationAnimator.setDuration(500); // Animation duration in milliseconds
        rotationAnimator.setInterpolator(new LinearInterpolator()); // Use a linear interpolator for constant speed
        rotationAnimator.start();
        animateZoomOut();
    }

    private void animateZoomIn() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imgRate, "scaleX", 1f, 1.2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imgRate, "scaleY", 1f, 1.2f);
        scaleX.setDuration(500);
        scaleY.setDuration(500);
        scaleX.start();
        scaleY.start();

        isZoomedIn = true;
        animateRotation();

    }

    private void animateZoomOut() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imgRate, "scaleX", 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imgRate, "scaleY", 1.2f, 1f);
        scaleX.setDuration(800);
        scaleY.setDuration(800);
        scaleX.start();
        scaleY.start();

        isZoomedIn = false;
    }

    public static class Builder {
        private String title, content, rateUs, notNow;
        private int titleColor = 0, contentColor = 0, rateUsDra, rateUsColor = 0, notNowColor = 0;
        private String colorStart, colorEnd;
        private int titleSize = 0, contentSize = 0;
        private final Activity context;
        private int drawableRateUs = 0;
        private IClickBtn onClickBtn;
        private boolean isExitApp = false;
        private boolean isRateInApp = true;
        private int numberRateInApp = 4;
        private String colorRatingBar;
        private String colorRatingBarBg;
        private Typeface typeface = null;
        private Typeface typefaceTitle = null;
        private Typeface typefaceContent = null;
        private Typeface typefaceRateUs = null;
        private Typeface typefaceNotNow = null;
        private int drawableDialog = 0;
        private int drawableBgStar = 0;
        private int numberRateDefault = 5;
        private int[] arrStar = {R.drawable.ic_star_0, R.drawable.ic_star_1, R.drawable.ic_star_2, R.drawable.ic_star_3, R.drawable.ic_star_4, R.drawable.ic_star_5};

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

        public Builder setTextButton(String rateUs, String notNow) {
            this.rateUs = rateUs;
            this.notNow = notNow;
            return this;
        }

        public Builder setRateInApp(Boolean isRateInApp) {
            this.isRateInApp = isRateInApp;
            return this;
        }

        public Builder setTextTitleColorLiner(String colorStart, String colorEnd) {
            this.colorStart = colorStart;
            this.colorEnd = colorEnd;
            return this;
        }

        public Builder setDrawableButtonRate(int drawable) {
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

        public Builder setTextContentSize(int contentSize) {
            this.contentSize = contentSize;
            return this;
        }

        public Builder setTextContentColor(int color) {
            this.contentColor = color;
            return this;
        }

        public Builder setColorRatingBar(String color) {
            this.colorRatingBar = color;
            return this;
        }

        public Builder setColorRatingBarBG(String color) {
            this.colorRatingBarBg = color;
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

        public Builder setFontFamily(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        public Builder setFontFamilyTitle(Typeface typeface) {
            this.typefaceTitle = typeface;
            return this;
        }

        public Builder setFontFamilyContent(Typeface typeface) {
            this.typefaceContent = typeface;
            return this;
        }

        public Builder setFontFamilyRateUs(Typeface typeface) {
            this.typefaceRateUs = typeface;
            return this;
        }

        public Builder setFontFamilyNotNow(Typeface typeface) {
            this.typefaceNotNow = typeface;
            return this;
        }

        public Builder setBackgroundDialog(int drawable) {
            this.drawableDialog = drawable;
            return this;
        }

        public Builder setBackgroundStar(int drawable) {
            this.drawableBgStar = drawable;
            return this;
        }

        public Builder setNumberRateDefault(int number) {
            this.numberRateDefault = number;
            return this;
        }

        public Builder setArrStar(int[] arr) {
            if (arr.length == arrStar.length) {
                this.arrStar = arr;
            }
            return this;
        }

        public RateAppDiaLog build() {
            return new RateAppDiaLog(context, this);
        }

    }
}
