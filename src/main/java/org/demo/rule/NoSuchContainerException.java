package org.demo.rule;

import org.demo.DemoException;

/**
 * Created by barry.wong
 */
public class NoSuchContainerException extends DemoException {

    public NoSuchContainerException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode(){
        return "DEMO-ERR-301";
    }
}
