package core.connection.volley;

import java.util.List;
import java.util.Map;

import core.base.BaseResponse;
import core.util.Constant;

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
