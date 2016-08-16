package core.connection.volley;

import java.util.List;
import java.util.Map;

import core.base.BaseParser;
import core.base.BaseResponse;
import core.util.Constant;

public class ParallelResponse extends BaseResponse {

    private final Constant.RequestTarget target;
    private final BaseParser parser;
    private final String tag;

    public ParallelResponse(byte[] content, BaseParser parser, Map<String, String> headers, Map<String, List<String>> rawHeaders,
                            Constant.RequestTarget target, String tag) {
        super(content, headers, rawHeaders);
        this.target = target;
        this.parser = parser;
        this.tag = tag;
    }

    public BaseParser getParser() {
        return parser;
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
