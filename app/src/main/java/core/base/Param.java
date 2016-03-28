package core.base;

import java.util.HashMap;

public interface Param {
    byte[] makeRequestBody();

    HashMap<String, String> makeRequestHeaders();

    String makeBodyContentType();
}
