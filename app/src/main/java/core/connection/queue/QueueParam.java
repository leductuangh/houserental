package core.connection.queue;

import java.util.HashMap;

import core.base.Param;

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
