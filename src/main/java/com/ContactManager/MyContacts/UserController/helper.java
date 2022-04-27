package com.ContactManager.MyContacts.UserController;

public class helper {
    private String content;
    private String type;

    public helper(String content, String type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }
}
