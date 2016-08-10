package core.connection.volley;

import java.util.List;
import java.util.Map;

import core.base.BaseResponse;
import core.util.Constant;

/**
 * Created by Tyrael on 8/9/16.
 */
public class FileResponse extends BaseResponse {

    private final Constant.RequestTarget target;
    private final String file;

    public FileResponse(byte[] content, Map<String, String> headers, Map<String, List<String>> rawHeaders, Constant.RequestTarget target, String file) {
        super(content, headers, rawHeaders);
        this.target = target;
        this.file = file;
    }

    /**
     * @return the target
     */
    public Constant.RequestTarget getRequestTarget() {
        return target;
    }

    public String getFile() {
        return file;
    }
}
