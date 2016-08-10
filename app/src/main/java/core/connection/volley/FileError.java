package core.connection.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import core.util.Constant;

/**
 * Created by Tyrael on 8/9/16.
 */
public class FileError extends VolleyError {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final Constant.RequestTarget target;
    private final NetworkResponse response;
    private final String file;
    private final String url;

    public FileError(Constant.RequestTarget target, VolleyError error, String url, String file) {
        super(error);
        this.target = target;
        this.response = error.networkResponse;
        this.file = file;
        this.url = url;
    }

    /**
     * @return the target
     */
    public Constant.RequestTarget getRequestTarget() {
        return target;
    }

    /**
     * @return the response
     */
    public NetworkResponse getResponse() {
        return response;
    }

    public String getFile() {
        return file;
    }

    public String getUrl() {
        return url;
    }
}
