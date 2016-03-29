package core.connection.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import core.util.Constant;

@SuppressWarnings("ALL")
public class WebServiceError extends VolleyError {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final Constant.RequestTarget target;
    private final NetworkResponse response;

    public WebServiceError(Constant.RequestTarget target, VolleyError error) {
        super(error);
        this.target = target;
        this.response = error.networkResponse;
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

}
