package core.dialog;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.commonframe.R;

import core.base.BaseDialog;
import core.util.Utils;

@SuppressWarnings("ALL")
public class LoadingDialog extends BaseDialog {

    private String loading;
    private TextView loading_dialog_tv_loading;

    public LoadingDialog(Context context, String loading) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.loading = loading;
    }

    public LoadingDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
    }

    @Override
    protected void onBaseCreate() {

    }

    @Override
    protected void onBindView() {
        loading_dialog_tv_loading = (TextView) findViewById(R.id.loading_dialog_tv_loading);
        if (!Utils.isEmpty(loading))
            loading_dialog_tv_loading.setText(loading);
    }

}
