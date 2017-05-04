package com.ajdconsulting.pra.clubmanager.util;

/**
 * Created by adelimon on 5/4/2017.
 */
public class LineEndStringBuilder {
    private StringBuilder builder = new StringBuilder();

    public void append(String line) {
        builder.append(line + "\n");
    }

    public void appendEmptyLine() {
        builder.append("\n");
    }

    public String toString() {
        return builder.toString();
    }
}
