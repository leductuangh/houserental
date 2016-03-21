package core.base;

import java.util.HashMap;

/**
 * @author Tyrael
 * @version 1.0 <br>
 *          <br>
 *          <b>Class Overview</b> <br>
 *          <br>
 *          Represents an abstract class for forming common parameters content
 *          and headers of a web service message, every webservice must use a
 *          derived class from this abstract and override the makeRequestParams
 *          and makeRequestHeaders <br>
 * @since April 2014
 */
@SuppressWarnings("ALL")
public abstract class BaseParam implements Param {

    /**
     * The set of key-value headers for the webservice message
     */
    private final HashMap<String, String> headers;

    public BaseParam() {
        this.headers = new HashMap<>();
    }

    public abstract byte[] makeRequestBody();

    /**
     * This method forms a set of key-value headers applied for all the
     * webservice request, derived class should override this method to append
     * needed values
     *
     * @return a set of key-value parameters for the request
     */
    public HashMap<String, String> makeRequestHeaders() {
        return headers;
    }
}
