package com.example.commonframe.core.connection.queue;

import java.util.HashMap;

import com.example.commonframe.core.base.Param;

final public class QueueParam implements Param {

    private HashMap<String, String> headers;

    private byte[] body;

    public QueueParam(HashMap<String, String> headers, byte[] body) {
        this.headers = headers;
        this.body = body;
    }

    @Override
    public byte[] makeRequestBody() {
        return body;
    }

    @Override
    public HashMap<String, String> makeRequestHeaders() {
        return headers;
    }

}
