package com.example.commonframe.util.swipe;

import android.view.View;

public class RecycleInterface {

    public interface OnItemClickListener<T> {
        void onItemClick(View view, T item, int position, int type);
    }

}
