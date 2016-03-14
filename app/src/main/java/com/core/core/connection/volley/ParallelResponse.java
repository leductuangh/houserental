package com.core.core.connection.volley;

import com.core.core.base.BaseResponse;
import com.core.util.Constant;

import java.util.List;
import java.util.Map;

public class ParallelResponse extends BaseResponse {

    private final Constant.RequestTarget target;
    private final String tag;

    public ParallelResponse(byte[] content, Map<String, String> headers, Map<String, List<String>> rawHeaders,
                            Constant.RequestTarget target, String tag) {
        super(content, headers, rawHeaders);
        this.target = target;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    /**
     * @return the target
     */
    public Constant.RequestTarget getRequestTarget() {
        return target;
    }
}
