package com.example.commonframe.core.connection.volley;

import com.example.commonframe.core.base.BaseResponse;

import java.util.Map;

public class WebServiceResponse extends BaseResponse {

    public WebServiceResponse(byte[] content, Map<String, String> headers) {
        super(content, headers);
    }

}
