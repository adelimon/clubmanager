package com.ajdconsulting.pra.clubmanager.data.export.calendar;

import com.ajdconsulting.pra.clubmanager.util.LineEndStringBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by adelimon on 5/5/2017.
 */
public class CalendarEvent {

    private static final String EVENT_START = "BEGIN:VEVENT";

    private static final String SUMMARY = "SUMMARY:";
    private static final String UID = "UID:";
    private static final String STATUS = "STATUS:CONFIRMED";
    private static final String DTSTART = "DTSTART:";
    private static final String DTEND = "DTEND:";

    private static final String EVENT_END = "END:VEVENT";

    private LineEndStringBuilder contents;

    public CalendarEvent(String summary, String uniqueId, LocalDateTime start, LocalDateTime end) {
        contents = new LineEndStringBuilder();
        contents.append(EVENT_START);
        contents.append(SUMMARY + summary);
        // generate our UID on our own
        contents.append(UID + uniqueId);
        contents.append(STATUS);
        contents.append(DTSTART + formatDate(start));
        contents.append(DTEND + formatDate(end));
        contents.append(EVENT_END);
    }

    private String formatDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public String toString() {
        return contents.toString();
    }
}
