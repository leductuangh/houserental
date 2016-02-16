package com.example.commonframe.core.base;

import com.example.commonframe.util.Constant.StatusCode;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyrael
 * @version 1.0 <br>
 *          <br>
 *          <b>Class Overview</b> <br>
 *          <br>
 *          Represents a class for result from network response after parsing,
 *          including the data as an object or a failure object in general<br>
 * @since April 2014
 */
@SuppressWarnings("ALL")
public abstract class BaseResult {

    /**
     * The set of key-value for header and values
     */
    private Map<String, String> headers;

    /**
     * The string value of the return status
     */
    private StatusCode status;

    public BaseResult() {
        headers = new HashMap<>();
    }

    /**
     * @return the headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @param headers the headers to set
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * @return the status
     */
    public StatusCode getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(StatusCode status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
