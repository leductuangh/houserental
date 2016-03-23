package core.util;

import com.android.volley.Request;
import com.example.houserental.R;

@SuppressWarnings("ALL")
public class Constant {

	/* REQUEST SECTION */

    /* COMMON VARIABLES */
    public static final int[] DEFAULT_ADD_ANIMATION = {R.anim.slide_in_right,
            R.anim.slide_out_left};
    public static final int[] DEFAULT_BACK_ANIMATION = {R.anim.slide_in_left,
            R.anim.slide_out_right};
    public static final String NOTIFICATION_DEFINED = "Notification_Defined";
    public static final String NOTIFICATION_ID = "Notification_Id";
    /* DEBUG */
    public static final boolean DEBUG = true;

	/* END REQUEST SECTION */

    /* DECLARE VARIABLES SECTION */
    /* SYSTEM */
    public static final String BLANK = "";
    public static final String EOF = System.getProperty("line.separator");
    public static final int INTERVAL_CLICK = 500; // 500ms
    public static final int INTERVAL_BACK_PRESS = 300; // 300ms

    /* END COMMON VARIABLES */
    public static final boolean MEMORY_CACHE = true;
    /* END DEBUG */
    public static final boolean DISC_CACHE = true;
    public static final int LRU_CACHE_SIZE = 20 * 1024 * 1024; // 20MB
    public static final int MEMORY_CACHE_SIZE = 20 * 1024 * 1024; // 20MB
    public static final int DISC_CACHE_SIZE = 100 * 1024 * 1024; // 100MB
    public static final int DISC_CACHE_COUNT = 200; // 200 files cached
    public static final int TINT_LEVEL = 0xFFaaaaaa; // 0xFFaaaaaa
    public static final float TINT_COLOR_LEVEL = 0.68f; // 0.68f
    /* NETWORK */
    public static final boolean NETWORK_ERROR_DATA_HANDLE = true;
    public static final String SERVER_URL = "gcm.pe.hu";
    public static final String KEY_STORE_TYPE = "BKS";
    public static final String KEY_STORE_PASSWORD = "";
    public static final int KEY_STORE_ID = 0;
    public static final boolean SSL_ENABLED = !(Utils.isEmpty(KEY_STORE_PASSWORD) && KEY_STORE_ID == 0);

    /* END SYSTEM */
    public static final int TIMEOUT_BACKGROUND_CONNECT = DEBUG ? 15000 : 20000;
    public static final int TIMEOUT_QUEUE_CONNECT = DEBUG ? 15000 : 20000;
    public static final int TIMEOUT_CONNECT = DEBUG ? 5000 : 10000;
    public static final int RETRY_CONNECT = DEBUG ? 0 : 2;
    public static final int RETRY_BACKGROUND_CONNECT = DEBUG ? 1 : 3;
    public static final int RETRY_QUEUE_CONNECT = DEBUG ? 0 : 0;

    /* GCM */
    public static final String SENDER_ID = "809303350857";

    /* END GCM*/
    /* DIALOG ID */
    public static final int EXIT_APPLICATION_DIALOG = 1;
    public static final int DELETE_FLOOR_DIALOG = 2;
    public static final int DELETE_FLOOR_ERROR_DIALOG = 3;
    public static final int DELETE_DEVICE_DIALOG = 4;
    public static final int DELETE_USER_DIALOG = 5;
    public static final int DELETE_ROOM_DIALOG = 6;
    public static final int DELETE_ROOM_TYPE_DIALOG = 7;
    public static final int REMOVE_RENTAL_DIALOG = 8;
    /* END NETWORK */

    public enum RequestType {
        HTTP {
            @Override
            public String toString() {
                return "http://";
            }
        },
        HTTPS {
            @Override
            public String toString() {
                return "https://";
            }
        }
    }

	/* END DECLARE VARIABLES SECTION */

    public enum StatusCode {
        OK, ERR_SSL, ERR_UNKNOWN, ERR_PARSING, ERR_AUTH_FAIL, ERR_SERVER_FAIL, ERR_NO_CONNECTION, ERR_TIME_OUT
    }

    public enum RequestMethod {
        GET, POST, DELETE, HEAD, OPTIONS, PATCH, PUT, TRACE;

        public int getValue() {
            switch (this) {
                case GET:
                    return Request.Method.GET;
                case POST:
                    return Request.Method.POST;
                case PUT:
                    return Request.Method.PUT;
                case DELETE:
                    return Request.Method.DELETE;
                case HEAD:
                    return Request.Method.HEAD;
                case PATCH:
                    return Request.Method.PATCH;
                case TRACE:
                    return Request.Method.TRACE;
                case OPTIONS:
                    return Request.Method.OPTIONS;
                default:
                    return -1;
            }
        }
    }

    public enum RequestTarget {
        WEBSERVICE_REQUEST, BACKGROUND_REQUEST;

        public static String build(RequestTarget target, String... extras) {
            switch (target) {
                case WEBSERVICE_REQUEST:
                    return "/wait.php";
                default:
                    break;
            }
            return "";
        }
    }

    public enum Header {
        ACCEPT {
            @Override
            public String toString() {
                return "Accept";
            }
        },
        ACCEPT_CHARSET {
            @Override
            public String toString() {
                return "Accept-Charset";
            }
        },
        ACCEPT_ENCODING {
            @Override
            public String toString() {
                return "Accept-Encoding";
            }
        },
        ACCEPT_LANGUAGE {
            @Override
            public String toString() {
                return "Accept-Language";
            }
        },
        AUTHORIZATION {
            @Override
            public String toString() {
                return "Authorization";
            }
        },
        CACHE_CONTROL {
            @Override
            public String toString() {
                return "Cache-Control";
            }
        },
        CONNECTION {
            @Override
            public String toString() {
                return "Connection";
            }
        },
        CONTENT_LENGTH {
            @Override
            public String toString() {
                return "Content-Length";
            }
        },
        CONTENT_TYPE {
            @Override
            public String toString() {
                return "Content-Type";
            }
        },
        USER_AGENT {
            @Override
            public String toString() {
                return "UserDAO-Agent";
            }
        }
    }
    /* END DIALOG ID */
}
