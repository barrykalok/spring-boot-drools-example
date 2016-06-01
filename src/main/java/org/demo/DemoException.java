package org.demo;

/**
 * Created by barry.wong
 */
public class DemoException extends RuntimeException {

    public DemoException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return "DEMO-ERR-100";
    }
}
