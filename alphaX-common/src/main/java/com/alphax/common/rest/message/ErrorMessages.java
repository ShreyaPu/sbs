package com.alphax.common.rest.message;

import java.util.ArrayList;
import java.util.List;

public class ErrorMessages {
    private List<ErrorMessage> messages;

    public ErrorMessages(List<ErrorMessage> messages) {
        this.messages = messages;
    }

    public ErrorMessages() {
        this.messages = new ArrayList<>();
    }

    public List<ErrorMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ErrorMessage> messages) {
        this.messages = messages;
    }
}
