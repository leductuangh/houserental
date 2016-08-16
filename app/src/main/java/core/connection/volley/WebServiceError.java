package core.connection.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import core.base.BaseParser;
import core.util.Constant.RequestTarget;


@SuppressWarnings("unused")
public class WebServiceError extends VolleyError {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final RequestTarget target;
    private final NetworkResponse response;
    private final BaseParser parser;

    public WebServiceError(RequestTarget target, BaseParser parser, VolleyError error) {
        super(error);
        this.target = target;
        this.parser = parser;
        this.response = error.networkResponse;
    }

    public BaseParser getParser() {
        return parser;
    }

    /**
     * @return the target
     */
    public RequestTarget getRequestTarget() {
        return target;
    }

    /**
     * @return the response
     */
    public NetworkResponse getResponse() {
        return response;
    }

}
