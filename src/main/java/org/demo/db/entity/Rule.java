package org.demo.db.entity;

import org.springframework.data.annotation.Id;

/**
 * Created by barry.wong
 */
public class Rule {

    @Id
    private String id;

    private String name;

    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
