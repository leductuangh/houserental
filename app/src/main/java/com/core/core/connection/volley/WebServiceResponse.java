package com.core.core.connection.volley;

import com.core.core.base.BaseResponse;

import java.util.List;
import java.util.Map;

public class WebServiceResponse extends BaseResponse {

    public WebServiceResponse(byte[] content, Map<String, String> headers, Map<String, List<String>> rawHeaders) {
        super(content, headers, rawHeaders);
    }

}
