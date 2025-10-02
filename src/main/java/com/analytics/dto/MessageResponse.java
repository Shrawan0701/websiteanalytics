package com.analytics.dto;

public class MessageResponse {
    private String message;
    private boolean success;
    private Object data;
    private long timestamp;

    public MessageResponse(String message) {
        this.message = message;
        this.success = true;
        this.timestamp = System.currentTimeMillis();
    }

    public MessageResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.timestamp = System.currentTimeMillis();
    }

    public MessageResponse(String message, boolean success, Object data) {
        this.message = message;
        this.success = success;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
