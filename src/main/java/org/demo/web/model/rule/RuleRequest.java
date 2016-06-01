package org.demo.web.model.rule;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by barry.wong
 */
public class RuleRequest {

    @NotNull(message = "{DEMO-ERR-204}")
    @Size(min = 1, max = 4096, message = "{DEMO-ERR-203}")
    private String content;

    @NotNull(message = "{DEMO-ERR-204}")
    @Size(min = 1, max = 64, message = "{DEMO-ERR-203}")
    @Pattern(regexp = "^[\\w\\-_]*$", message = "{DEMO-ERR-202}")
    private String name;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
