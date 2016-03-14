package com.core.core.connection.volley;

import com.core.core.base.BaseResponse;
import com.core.util.Constant;

import java.util.List;
import java.util.Map;

public class QueueResponse extends BaseResponse {

    private final Constant.RequestTarget target;

    public QueueResponse(byte[] content, Map<String, String> headers, Map<String, List<String>> rawHeaders,
                         Constant.RequestTarget target) {
        super(content, headers, rawHeaders);
        this.target = target;
    }

    /**
     * @return the target
     */
    public Constant.RequestTarget getRequestTarget() {
        return target;
    }
}
