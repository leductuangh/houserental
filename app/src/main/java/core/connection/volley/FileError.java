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
    private final String fullPath;

    public FileError(Constant.RequestTarget target, VolleyError error, String fullPath) {
        super(error);
        this.target = target;
        this.response = error.networkResponse;
        this.fullPath = fullPath;
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

    public String getFullPath() {
        return fullPath;
    }
}
