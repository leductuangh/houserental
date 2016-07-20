package core.connection.volley;

import java.util.List;
import java.util.Map;

import core.base.BaseParser;
import core.base.BaseResponse;

public class WebServiceResponse extends BaseResponse {

    private final BaseParser parser;

    public WebServiceResponse(byte[] content, BaseParser parser, Map<String, String> headers, Map<String, List<String>> rawHeaders) {
        super(content, headers, rawHeaders);
        this.parser = parser;
    }

    public BaseParser getParser() {
        return parser;
    }
}
