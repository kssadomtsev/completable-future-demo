package ru.oneonyx.future.model;

public class Response {
    private long time;
    private String status;

    public Response() {
    }

    public Response(long time, String status) {
        this.time = time;
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
