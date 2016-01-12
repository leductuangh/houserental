package com.example.commonframe.core.connection.volley;

import com.example.commonframe.core.base.BaseResponse;
import com.example.commonframe.util.Constant.RequestTarget;

import java.util.Map;

public class QueueResponse extends BaseResponse {

    private final RequestTarget target;

    public QueueResponse(byte[] content, Map<String, String> headers,
                         RequestTarget target) {
        super(content, headers);
        this.target = target;
    }

    /**
     * @return the target
     */
    public RequestTarget getRequestTarget() {
        return target;
    }
}
