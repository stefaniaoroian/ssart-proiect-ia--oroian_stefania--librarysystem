package com.library.library_system.messaging;

import com.library.library_system.entity.User;

public class NotificationPayload {

    private User user;
    private String message;

    public NotificationPayload() {}

    public NotificationPayload(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}