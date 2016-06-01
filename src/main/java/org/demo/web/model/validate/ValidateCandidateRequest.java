package org.demo.web.model.validate;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by barry.wong
 */
public class ValidateCandidateRequest {

    @NotNull(message = "{DEMO-ERR-204}")
    private Map<String, Object> detail;

    public Map<String, Object> getDetail() {
        return detail;
    }

    public void setDetail(Map<String, Object> detail) {
        this.detail = detail;
    }
}
