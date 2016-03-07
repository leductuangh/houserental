package com.example.commonframe.helper.fetchable.list;

import android.view.View;
import android.widget.ListView;

public interface OnPositionChangedListener {
    void onPositionChanged(ListView listView, int position,
                           View scrollBarPanel);
}
