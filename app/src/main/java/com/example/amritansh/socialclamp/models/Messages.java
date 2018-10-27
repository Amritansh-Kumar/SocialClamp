package com.example.amritansh.socialclamp.models;

public class Messages {

    private String message;
    private boolean seen;
    private long timestamp;
    private String type;

    public Messages(){
    }

    public Messages(String message, boolean seen, long timestamp, String type) {
        this.message = message;
        this.seen = seen;
        this.timestamp = timestamp;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
