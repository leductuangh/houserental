package com.example.houserental.core.connection.volley;

import com.example.houserental.core.base.BaseResponse;
import com.example.houserental.util.Constant.RequestTarget;

import java.util.List;
import java.util.Map;

public class QueueResponse extends BaseResponse {

    private final RequestTarget target;

    public QueueResponse(byte[] content, Map<String, String> headers, Map<String, List<String>> rawHeaders,
                         RequestTarget target) {
        super(content, headers, rawHeaders);
        this.target = target;
    }

    /**
     * @return the target
     */
    public RequestTarget getRequestTarget() {
        return target;
    }
}
