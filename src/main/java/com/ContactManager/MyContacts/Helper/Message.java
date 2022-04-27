package com.ContactManager.MyContacts.Helper;

public class Message {
    private String content;
    private String typeString;

    public Message(String content, String typeString) {
        this.content = content;
        this.typeString = typeString;
    }

    public String getContent() {
        return content;
    }

    public String getTypeString() {
        return typeString;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", typeString='" + typeString + '\'' +
                '}';
    }
}
