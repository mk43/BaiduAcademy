package org.fitzeng.slidedeletelist;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class SlideItemView extends LinearLayout{


    private LinearLayout contentView;
    private LinearLayout menuView;
    private OnSlideListener onSlideListener;
    private Scroller scroller;
    private int btnWidth = 80;

    private int lastX = 0;
    private int lastY = 0;



    interface OnSlideListener {

        int STATUS_SLIDE_OFF = 0;
        int STATUS_SLIDE_ON = 1;
        int STATUS_SLIDE_SCROLL = 2;

        void onSlide(View view, int status);
    }

    public SlideItemView(Context context) {
        super(context);
        initView();
    }

    public SlideItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SlideItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        Context context = getContext();
        scroller = new Scroller(context);
        // inflate layout
        View.inflate(context, R.layout.listitem, this);
        contentView = (LinearLayout) findViewById(R.id.content);
        menuView = (LinearLayout) findViewById(R.id.menu);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Get event start coordinate
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                // if another event unfinished, then finished
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                // get menu width
                btnWidth = menuView.getMeasuredWidth();
                if (onSlideListener != null) {
                    onSlideListener.onSlide(this, OnSlideListener.STATUS_SLIDE_SCROLL);
                }
                // represent the event was handled or consumed, see the resource code
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                // get offset
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                // if offsetX miner than offsetY or offsetY more than 20, then cancel this event
                if (offsetY > 20) {
                    break;
                }
                int newScrollX = scrollX - offsetX;
                if (offsetX != 0) {
                    if (newScrollX < 0) {
                        newScrollX = 0;
                    } else if (newScrollX > btnWidth){
                        newScrollX = btnWidth;
                    }
                    scrollTo(newScrollX, 0);
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                int newScroll = 0;
                // more than half of button width, scroll out. otherwise scroll in.
                if (scrollX - btnWidth * 0.7 > 0) {
                    newScroll = btnWidth;
                }
                smoothScrollTo(newScroll);
                // set item slide status
                if (onSlideListener != null) {
                    onSlideListener.onSlide(this, newScroll == 0 ? OnSlideListener.STATUS_SLIDE_OFF
                            : OnSlideListener.STATUS_SLIDE_ON);
                }
                break;
            }
        }
        lastX = x;
        lastY = y;
        return super.onTouchEvent(event);
    }

    private void smoothScrollTo(int x) {
        int scrollX = getScrollX();
        int offsetX = x - scrollX;
        scroller.startScroll(scrollX, 0, offsetX, 0, Math.abs(offsetX));
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    public void shrink() {
        if (getScaleX() != 0) {
            this.smoothScrollTo(0);
        }
    }

    public void setContentView(View content) {
        contentView.addView(content);
    }

    public void setMenuView(View menu) {
        menuView.addView(menu);
    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        this.onSlideListener = onSlideListener;
    }
}
