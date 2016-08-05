package com.example.houserental.function.view;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by Tyrael on 8/4/16.
 */
public class HouseRentalButton extends AppCompatButton {
    public HouseRentalButton(Context context) {
        super(context);
        init();
    }

    public HouseRentalButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HouseRentalButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        setTypeface(Typeface.DEFAULT);
//        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Regular.ttf"));
    }
}
