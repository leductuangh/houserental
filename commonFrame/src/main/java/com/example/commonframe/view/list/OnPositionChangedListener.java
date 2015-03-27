package com.example.commonframe.view.list;

import android.view.View;
import android.widget.ListView;

public interface OnPositionChangedListener {
	public void onPositionChanged(ListView listView, int position,
			View scrollBarPanel);
}
