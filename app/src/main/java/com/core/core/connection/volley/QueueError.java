package com.core.core.connection.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.core.util.Constant;

public class QueueError extends VolleyError {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final Constant.RequestTarget target;
    private final NetworkResponse response;

    public QueueError(Constant.RequestTarget target, VolleyError error) {
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
