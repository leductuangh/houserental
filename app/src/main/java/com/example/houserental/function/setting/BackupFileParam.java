package com.example.houserental.function.setting;

import java.util.HashMap;

import core.base.BaseParam;

/**
 * Created by Tyrael on 3/29/16.
 */
public class BackupFileParam extends BaseParam {
    @Override
    public byte[] makeRequestBody() {
        return new byte[0];
    }

    @Override
    public HashMap<String, String> makeRequestHeaders() {
        return null;
    }

    @Override
    public String makeBodyContentType() {
        return null;
    }
}
