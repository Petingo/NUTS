package com.nuts.nuts;
/* Created by petingo on 2018/2/9. */

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.lang.annotation.AnnotationTypeMismatchException;

public class AnimateManager {
    private Context context;

    Animation slideDown;
    Animation slideUp;
    Animation slideLeftAppear;
    Animation slideLeftDisappear;
    Animation slideRightAppear;
    Animation slideRightDisappear;

    AnimateManager(Context context) {
        this.context = context;
        slideDown = AnimationUtils.loadAnimation(this.context, R.anim.slide_down);
        slideUp = AnimationUtils.loadAnimation(this.context, R.anim.slide_up);
        slideLeftAppear = AnimationUtils.loadAnimation(this.context, R.anim.slide_left_appear);
        slideLeftDisappear = AnimationUtils.loadAnimation(this.context, R.anim.slide_left_disappear);
        slideRightAppear = AnimationUtils.loadAnimation(this.context, R.anim.slide_right_appear);
        slideRightDisappear = AnimationUtils.loadAnimation(this.context, R.anim.slide_right_disappear);

    }
    // A go down and GONE,
    // B come in from top
    void nextStep(View A, View B){
        if(A != null) {
            A.startAnimation(slideDown);
            A.setVisibility(View.GONE);
        }
        if(B != null) {
            B.setVisibility(View.VISIBLE);
            B.startAnimation(slideUp);
        }
    }
    // A go left and GONE,
    // B come in from right
    void leftSlide(View A, View B){
        if(A != null) {
            A.startAnimation(slideLeftDisappear);
            A.setVisibility(View.GONE);
        }
        if(B != null) {
            B.setVisibility(View.VISIBLE);
            B.startAnimation(slideLeftAppear);
        }
    }
    // A go right and GONE,
    // B come in from left
    void rightSlide(View A, View B){
        if(A != null) {
            A.startAnimation(slideRightDisappear);
            A.setVisibility(View.GONE);
        }
        if(B != null) {
            B.setVisibility(View.VISIBLE);
            B.startAnimation(slideRightAppear);
        }
    }

}
