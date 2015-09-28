package com.example.commonframe.core.connection.queue;

import com.example.commonframe.core.base.Param;

import java.util.HashMap;

@SuppressWarnings("ALL")
final public class QueueParam implements Param {

    private final HashMap<String, String> headers;

    private final byte[] body;

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
