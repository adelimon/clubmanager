package com.ajdconsulting.pra.clubmanager.data.export.calendar;

import com.ajdconsulting.pra.clubmanager.util.LineEndStringBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by adelimon on 5/5/2017.
 */
public class ICalendar {

    private LineEndStringBuilder template;
    private LineEndStringBuilder events;

    public ICalendar(String templatePath) throws IOException {
        template = new LineEndStringBuilder();
        events = new LineEndStringBuilder();

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        this.template.append(
            new String(Files.readAllBytes(Paths.get(s + templatePath)))
        );
    }

    public void add(CalendarEvent calendarEvent) {
        events.append(calendarEvent.toString());
    }

    public String toString() {
        String strVal = this.template.toString();
        return strVal.replaceAll("#EVENTS#", events.toString());
    }
}
