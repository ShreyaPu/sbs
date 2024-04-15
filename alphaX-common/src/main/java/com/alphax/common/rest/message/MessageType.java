package com.alphax.common.rest.message;

/**
 * Represents the severity of a problem. Higher severity has higher value.
 */
public enum MessageType {
    ERROR(40, "ERROR", 'E'), WARNING(30, "WARNING", 'W'), INFO(20, "INFORMATION", 'I'), DEBUG(10, "DEBUG", 'D'),
    SUCCESS(0, "SUCCESS", 'S');

    private int levelInt;
    private String levelStr;
    private char levelChar;

    MessageType(int i, String s, char c) {
        levelInt = i;
        levelStr = s;
        levelChar = c;
    }

    public int toInt() {
        return levelInt;
    }

    public char toChar() {
        return levelChar;
    }

    @Override
    public String toString() {
        return levelStr;
    }
}
