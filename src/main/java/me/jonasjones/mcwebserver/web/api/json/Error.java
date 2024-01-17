package me.jonasjones.mcwebserver.web.api.json;

public class Error {
    private int status;
    private String message;
    public Error(int status, String badRequest) {
        this.status = status;
        this.message = badRequest;
    }
}
