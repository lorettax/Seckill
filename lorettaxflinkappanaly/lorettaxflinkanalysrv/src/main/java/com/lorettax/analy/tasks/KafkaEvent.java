package com.lorettax.analy.tasks;

public class KafkaEvent {
    private String message;
    private long timestamp;

    public KafkaEvent(){}

    public KafkaEvent(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return  message;
    }
}
