package com.example.houserental.function.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class LockableScrollView extends ScrollView {

    public View blockView;

    // true if we can scroll (not locked)
    // false if we cannot scroll (locked)
    private boolean mScrollable = true;
    private IOnScrollChanged mOnScrollChanged;
    private int initX, initY;

    public LockableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollingEnabled(boolean enabled) {
        mScrollable = enabled;
    }

    public boolean isScrollable() {
        return mScrollable;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                if (mScrollable) {
                    return super.onTouchEvent(ev);
                }
                // only continue to handle the touch event if scrolling enabled

                // we are not scrollable
                if (!mScrollable) {
                    return false;
                }

                return mScrollable; // mScrollable is always false at this point
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        if (!mScrollable)
            return false;
        else {

            // block scroll when touch on blockview
            if (blockView != null) {

                float x = ev.getRawX();
                float y = ev.getRawY();

                int positon[] = new int[2];
                blockView.getLocationInWindow(positon);
                int left = positon[0];
                int top = positon[1];
                int width = blockView.getWidth();
                int height = blockView.getHeight();

                // block
                if (x >= left && x <= (width + left) && y >= top && y <= (height + top)) {
                    return false;
                }
            }
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (mOnScrollChanged != null)
            mOnScrollChanged.scrollChanged(l, t);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        post(new Runnable() {
            @Override
            public void run() {
                scrollTo(initX, initY);
            }
        });

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setOnScrollChanged(IOnScrollChanged mOnScrollChanged) {
        this.mOnScrollChanged = mOnScrollChanged;
    }

    /**
     *
     */
    public void initScrollXY(int x, int y) {
        initX = x;
        initY = y;
        measure(-1, -1);
    }

    public interface IOnScrollChanged {

        void scrollChanged(int x, int y);
    }

    public interface IBlockScrollEvent {

        void changeBlock(boolean scrollEnable);
    }

}
