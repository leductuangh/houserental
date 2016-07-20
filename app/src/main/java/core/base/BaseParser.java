package core.base;

/**
 * @author Tyrael
 * @version 1.0 <br>
 *          <br>
 *          <b>Class Overview</b> <br>
 *          <br>
 *          - Represents an abstract class for performing parsing the content of
 *          a response. The parsing action based on the return format and the
 *          request target that requested<br>
 *          - Every request must have its own parser and extended from this base
 *          parser<br>
 * @since April 2014
 */

@SuppressWarnings({"unused", "ConstantConditions"})
public abstract class BaseParser {

    /**
     * This method is required for the derived class to perform the data parsing
     * of the content <br>
     * <p/>
     * The derived class must implemented this method and return
     * <code>null</code> if there is any error occurs
     */
    public abstract BaseResult parseData(String content);
}
