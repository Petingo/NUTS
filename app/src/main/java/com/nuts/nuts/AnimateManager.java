package com.nuts.nuts;
/* Created by petingo on 2018/2/9. */

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AnimateManager {
    private Context context;
    public Animation slide_down;
    public Animation slide_up;

    AnimateManager(Context context) {
        this.context = context;
        slide_down = AnimationUtils.loadAnimation(this.context, R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(this.context, R.anim.slide_up);
    }
}
