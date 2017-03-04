package org.fitzeng.slideshow;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Set scroller duration, make it smoothly
 */
class FixSpeedScroller extends Scroller {

    private int _duration = 1000;

    FixSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, _duration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, _duration);
    }

    public void setDuration(int duration) {
        _duration = duration;
    }
}
