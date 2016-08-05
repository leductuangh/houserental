package com.example.houserental.function.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Tyrael on 8/4/16.
 */
public class HouseRentalTextView extends TextView {
    public HouseRentalTextView(Context context) {
        super(context);
        init();
    }

    public HouseRentalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HouseRentalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        setTypeface(Typeface.DEFAULT);
//        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Regular.ttf"));
    }
}
