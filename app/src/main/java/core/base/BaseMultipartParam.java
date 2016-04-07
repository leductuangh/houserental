package core.base;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import core.util.Constant;

@SuppressWarnings("unused")
public abstract class BaseMultipartParam implements Param {

    /**
     * The multipart form data header
     */
    private static final String MULTIPART_FORM_DATA = "multipart/form-data;boundary=";
    /**
     * The break line for multipart header
     */
    private static final String BREAK_LINE = "\r\n";
    /**
     * The hyphens for multipart header
     */
    private static final String HYPHENS = "--";
    /**
     * The content of multipart header
     */
    private static final String CONTENT = "%s%s%sContent-Disposition: form-data; name=\"%s\"; filename=\"uploadedFile\"%s Content-Type: %s %s Content-Transfer-Encoding: binary %s%s";
    /**
     * The set of key-value headers for the webservice message
     */
    private final HashMap<String, String> headers;
    /**
     * The set of key-value text parts
     */
    private final HashMap<String, String> texts;
    /**
     * The set of key-value file parts
     */
    private final HashMap<String, File> files;
    /**
     * The boundary string for the request
     */
    private String boundary = "*****";

    public BaseMultipartParam() {
        this.headers = new HashMap<>();
        this.texts = new HashMap<>();
        this.files = new HashMap<>();
    }

    public final BaseMultipartParam addTextPart(String key, String value) {
        texts.put(key, value);
        return this;
    }

    public final BaseMultipartParam addBinaryPart(String key, String type,
                                                  byte[] value) {
        files.put(key, new File(type, value));
        return this;
    }

    /**
     * @return the boundary
     */
    public final String getBoundary() {
        return boundary;
    }

    /**
     * @param boundary the boundary to set
     */
    public final void setBoundary(String boundary) {
        this.boundary = boundary;
    }

    @Override
    public final byte[] makeRequestBody() {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setBoundary("");
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (String key : texts.keySet()) {
            builder.addPart(key, new StringBody(texts.get(key),
                    ContentType.MULTIPART_FORM_DATA));
        }

        for (String key : files.keySet()) {
            builder.addBinaryBody(
                    key,
                    createUploadFile(key, files.get(key).getType(),
                            files.get(key).getContent()));
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            builder.build().writeTo(os);
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public HashMap<String, String> makeRequestHeaders() {
        headers.put(Constant.Header.CONTENT_TYPE.toString(),
                MULTIPART_FORM_DATA + boundary);
        return headers;
    }

    private byte[] createUploadFile(String name, String type, byte[] file) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            String header = String.format(CONTENT, HYPHENS, boundary,
                    BREAK_LINE, name, BREAK_LINE, type, BREAK_LINE, BREAK_LINE,
                    BREAK_LINE);
            String footer = String.format("%s%s%s%s%s", BREAK_LINE, HYPHENS,
                    boundary, HYPHENS, BREAK_LINE);
            os.write(header.getBytes());
            os.write(file);
            os.write(footer.getBytes());
            os.flush();
            byte[] result = os.toByteArray();
            os.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private class File {
        private final String type;
        private final byte[] content;

        public File(String type, byte[] content) {
            super();
            this.type = type;
            this.content = content;
        }

        /**
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * @return the content
         */
        public byte[] getContent() {
            return content;
        }
    }
}
