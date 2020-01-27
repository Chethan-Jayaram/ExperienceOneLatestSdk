package com.example.experienceone.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.experienceone.R;

public class SwipeButton  extends RelativeLayout {

    private boolean isClicked;
    private ImageView mSwipeButton;
    private int initialButtonWidth;
    private int animationButtonWidth;




    public SwipeButton(Context context) {
        super(context);
        init(context);
    }

    public SwipeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        isClicked = false;

        mSwipeButton = new ImageView(context);
        mSwipeButton.setBackground(ContextCompat.getDrawable(context, R.drawable.sos));

        LayoutParams layoutParamsButton = new LayoutParams(
                250,
               250);

        layoutParamsButton.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParamsButton.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);


        addView(mSwipeButton, layoutParamsButton);
        final TextView centerText = new TextView(context);
        centerText.setGravity(Gravity.CENTER);

        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        centerText.setText("SWIPE"); //add any text you need
        centerText.setTextColor(Color.WHITE);
        centerText.setTextSize(18.0f);
        addView(centerText, layoutParams);

      /*  OnClickListener clickListener = v -> {
            if(!isClicked){
              ///  animateCheck();
                isClicked = true;
            } else{
               // animateUncheck();
                isClicked = false;
            }
        };*/

        //setOnClickListener(clickListener);
        mSwipeButton.setOnTouchListener(getButtonTouchListener());


    }

    @SuppressLint("ClickableViewAccessibility")
    private OnTouchListener getButtonTouchListener() {
        return (v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (initialButtonWidth == 0) {
                        initialButtonWidth = mSwipeButton.getWidth();
                    }
                   /* if (event.getX() > initialButtonWidth + mSwipeButton.getWidth() / 2 &&
                            event.getX() + mSwipeButton.getWidth() / 2 < getWidth()) {
                        mSwipeButton.setX(event.getX() - mSwipeButton.getWidth() / 2);
                        //centerText.setAlpha(1 - 1.3f * (mSwipeButton.getX() + mSwipeButton.getWidth()) / getWidth());
                    }

                    if  (event.getX() + mSwipeButton.getWidth() / 2 > getWidth() &&
                            mSwipeButton.getX() + mSwipeButton.getWidth() / 2 < getWidth()) {
                        mSwipeButton.setX(getWidth() - mSwipeButton.getWidth());
                    }*/

                    if  (event.getX() < mSwipeButton.getWidth() / 2 &&
                            mSwipeButton.getX() > 0) {
                        mSwipeButton.setX(0);
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    //Release logic here
                    initialButtonWidth = mSwipeButton.getWidth();

                    if (mSwipeButton.getX() + mSwipeButton.getWidth() > getWidth() * 0.85) {
                        expandButton();
                    } else {
                        //moveButtonBack();
                    }
                    return true;
            }

            return false;
        };
    }
    private void expandButton() {
        final ValueAnimator positionAnimator =
                ValueAnimator.ofFloat(mSwipeButton.getX(), 0);
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (Float) positionAnimator.getAnimatedValue();
                mSwipeButton.setX(x);
            }
        });


        final ValueAnimator widthAnimator = ValueAnimator.ofInt(
                mSwipeButton.getWidth(),
                getWidth());

        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams params = mSwipeButton.getLayoutParams();
                params.width = (Integer) widthAnimator.getAnimatedValue();
                mSwipeButton.setLayoutParams(params);
            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
               /* active = true;
                mSwipeButton.setImageDrawable(enabledDrawable);*/
            }
        });

        animatorSet.playTogether(positionAnimator, widthAnimator);
        animatorSet.start();
    }
    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        initialButtonWidth = mSwipeButton.getWidth();
        animationButtonWidth = getWidth();
    }

 /*   private void animateCheck(){
        final ValueAnimator expandAnimator = ValueAnimator.ofInt(initialButtonWidth, animationButtonWidth);
        expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSwipeButton.setWidth((Integer) animation.getAnimatedValue());
            }
        });

        expandAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setGravity(Gravity.LEFT);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        final ValueAnimator shirinkAnimator = ValueAnimator.ofInt(animationButtonWidth, initialButtonWidth);
        shirinkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSwipeButton.setWidth((Integer) animation.getAnimatedValue());
            }
        });
        shirinkAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setGravity(Gravity.RIGHT);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playSequentially(expandAnimator, shirinkAnimator);

        animatorSet.addListener(getClickableListener());

        animatorSet.start();
    }

    private void animateUncheck(){
        final ValueAnimator expandAnimator = ValueAnimator.ofInt(initialButtonWidth, animationButtonWidth);
        expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSwipeButton.setWidth((Integer) animation.getAnimatedValue());
            }
        });

        expandAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setGravity(Gravity.RIGHT);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        final ValueAnimator shirinkAnimator = ValueAnimator.ofInt(animationButtonWidth, initialButtonWidth);
        shirinkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSwipeButton.setWidth((Integer) animation.getAnimatedValue());
            }
        });



        shirinkAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setGravity(Gravity.LEFT);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playSequentially(expandAnimator, shirinkAnimator);

        animatorSet.addListener(getClickableListener());

        animatorSet.start();
    }

    private Animator.AnimatorListener getClickableListener(){
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setClickable(false);
                mSwipeButton.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setClickable(true);
                mSwipeButton.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        };
    }*/

}