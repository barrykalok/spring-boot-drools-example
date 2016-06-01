package org.demo.web.model.validate;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by barry.wong
 */
public class ValidateCandidateResponse {

    private Boolean answer;

    private Map<String, String> detail;

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    public Map<String, String> getDetail() {
        return detail;
    }

    public void setDetail(Map<String, String> detail) {
        this.detail = detail;
    }
}
