package core.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.widget.TextView;

import com.example.houserental.R;

import core.base.BaseDialog;
import core.util.Utils;


public class LoadingDialog extends BaseDialog {

    private String loading;
    private int layout;

    public LoadingDialog(Context context, @LayoutRes int layout, String loading) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.loading = loading;
        this.layout = layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
    }

    @Override
    protected void onBaseCreate() {

    }

    @Override
    protected void onBindView() {
        TextView loading_dialog_tv_loading = (TextView) findViewById(R.id.loading_dialog_tv_loading);
        try {
            if (loading_dialog_tv_loading == null)
                throw new Exception("Missing @id/loading_dialog_tv_loading in layout xml");
            if (findViewById(R.id.loading_dialog_progress) == null)
                throw new Exception("Missing @id/loading_dialog_progress in layout xml");

            if (!Utils.isEmpty(loading))
                loading_dialog_tv_loading.setText(loading);
        } catch (Exception e) {
            e.printStackTrace();
            dismiss();
        }
    }

}
