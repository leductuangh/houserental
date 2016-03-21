package core.connection.volley;

import java.util.List;
import java.util.Map;

import core.base.BaseResponse;
import core.util.Constant;

@SuppressWarnings("ALL")
public class BackgroundResponse extends BaseResponse {

    private final Constant.RequestTarget target;

    public BackgroundResponse(byte[] content, Map<String, String> headers, Map<String, List<String>> rawHeaders,
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
