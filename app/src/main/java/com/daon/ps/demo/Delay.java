package com.daon.ps.demo;

public enum Delay {
    NONE(0),
    DEFAULT(500),
    SHORT(1500),
    MEDIUM(2500),
    LONG(3500);

    private final int milliseconds;

    Delay (int milliseconds) {
        this.milliseconds = milliseconds;
    }

    public int getMilliseconds() {
        return milliseconds;
    }
}
