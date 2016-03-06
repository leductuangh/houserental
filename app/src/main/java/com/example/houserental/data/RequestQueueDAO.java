package com.example.houserental.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.example.houserental.core.connection.queue.QueueElement.Type;
import com.example.houserental.util.Constant.RequestMethod;
import com.example.houserental.util.Constant.RequestTarget;
import com.example.houserental.util.Constant.RequestType;

import java.util.HashMap;

@SuppressWarnings("ALL")
@Table(name = "RequestQueue")
public class RequestQueueDAO extends Model {

    @Column(name = "header")
    private HashMap<String, String> headers;

    @Column(name = "body")
    private byte[] body;

    @Column(name = "tag")
    private String tag;

    @Column(name = "protocol")
    private RequestType protocol;

    @Column(name = "method")
    private RequestMethod method;

    @Column(name = "address")
    private String address;

    @Column(name = "target")
    private RequestTarget target;

    @Column(name = "type")
    private Type type;

    @Column(name = "extra")
    private String[] extra;

    @Column(name = "sent")
    private boolean sent;

    public RequestQueueDAO() {
        super();
    }

    public RequestQueueDAO(String tag, RequestType protocol,
                           RequestMethod method, String address, RequestTarget target, Type type,
                           String[] extra, HashMap<String, String> headers, byte[] body, boolean sent) {
        super();
        this.tag = tag;
        this.protocol = protocol;
        this.method = method;
        this.address = address;
        this.target = target;
        this.extra = extra;
        this.headers = headers;
        this.body = body;
        this.type = type;
        this.sent = sent;
    }

    /**
     * @return the headers
     */
    public HashMap<String, String> getHeaders() {
        return headers;
    }

    /**
     * @return the body
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @return the protocol
     */
    public RequestType getProtocol() {
        return protocol;
    }

    /**
     * @return the method
     */
    public RequestMethod getMethod() {
        return method;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the target
     */
    public RequestTarget getTarget() {
        return target;
    }

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * @return the extra
     */
    public String[] getExtra() {
        return extra;
    }

    /**
     * @return the sent status
     */
    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
