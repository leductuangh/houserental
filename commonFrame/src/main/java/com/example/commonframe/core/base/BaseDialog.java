package com.example.commonframe.core.base;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.example.commonframe.util.SingleClick;
import com.example.commonframe.util.SingleTouch;
import com.example.commonframe.util.Utils;

public abstract class BaseDialog extends Dialog {
    private static SingleClick singleClick;
    private static SingleTouch singleTouch;

    public BaseDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        onCreateObject();
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        onCreateObject();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onBindView();
    }

    protected abstract void onCreateObject();

    protected abstract void onBindView();

    protected synchronized static SingleClick getSingleClick() {
        if (singleClick == null)
            singleClick = new SingleClick();
        return singleClick;
    }

    protected synchronized static SingleTouch getSingleTouch() {
        if (singleTouch == null)
            singleTouch = new SingleTouch();
        return singleTouch;
    }

    @Override
    protected void onStop() {
        Utils.nullViewDrawablesRecursive(findViewById(android.R.id.content)
                .getRootView());
        Utils.unbindDrawables(findViewById(android.R.id.content).getRootView());
        super.onStop();
    }

    @Override
    public View findViewById(int id) {
        View view = super.findViewById(id);
        if (view != null && !BaseProperties.isExceptionalView(view)) {
            view.setOnClickListener(getSingleClick());
            view.setOnTouchListener(getSingleTouch());
        }
        return view;
    }
}
