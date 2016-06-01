package org.demo.web.controller;

import org.demo.DemoException;

/**
 * Created by barry.wong
 */
public class DuplicateRuleNameException extends DemoException {

    public DuplicateRuleNameException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "DEMO-ERR-300";
    }
}
