package com.example.commonframe.core.connection.volley;

import com.example.commonframe.core.base.BaseResponse;
import com.example.commonframe.util.Constant.RequestTarget;

import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class BackgroundResponse extends BaseResponse {

    private final RequestTarget target;

    public BackgroundResponse(byte[] content, Map<String, String> headers, Map<String, List<String>> rawHeaders,
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
