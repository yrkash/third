package ru.alishev.springcourse.third.util;

public class JWTResponse {
    private String jwtToken;

    private long timestamp;

    public JWTResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public JWTResponse(String jwtToken, long timestamp) {
        this.jwtToken = jwtToken;
        this.timestamp = timestamp;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
