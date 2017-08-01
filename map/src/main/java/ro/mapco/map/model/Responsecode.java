package ro.mapco.map.model;

import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by joliedd on 5/18/2016.
 */

@Component
public class Responsecode {

    String errorCode;
    String message;
    String documentationUrl;
    Object[] errorParameters;

    public Responsecode() {

    }

    public Responsecode(String errorCode, String message, String documentationUrl, Object[] errorParameters) {
        this.errorCode = errorCode;
        this.message = message;
        this.documentationUrl = documentationUrl;
        this.errorParameters = errorParameters;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public Object[] getErrorParameters() {
        return errorParameters;
    }

    public void setErrorParameters(Object[] errorParameters) {
        this.errorParameters = errorParameters;
    }

    @Override
    public String toString() {
        return "Responsecode{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + message + '\'' +
                ", documentationUrl='" + documentationUrl + '\'' +
                ", errorParameters=" + Arrays.toString(errorParameters) +
                '}';
    }
}
