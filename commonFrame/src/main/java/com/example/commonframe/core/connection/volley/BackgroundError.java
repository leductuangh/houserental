package com.example.commonframe.core.connection.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.example.commonframe.util.Constant.RequestTarget;

public class BackgroundError extends VolleyError {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private RequestTarget target;
    private NetworkResponse response;

    public BackgroundError(RequestTarget target, VolleyError error) {
        super(error);
        this.target = target;
        this.response = error.networkResponse;
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
