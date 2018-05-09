package com.ajdconsulting.pra.clubmanager.dates;

import java.time.Month;
import java.time.ZonedDateTime;

/**
 * Determine the current "fiscal" year for PRA.  This goes March to March.  So for points and work tracking purposes,
 * fiscal 2017 would start in March 2017 and end on 2/28/18.
 *
 * This class encapsulates that logic so it is centralized.
 *
 */
public final class CurrentFiscalYear {

    private int fiscalYear;
    private int duesFiscalYear;

    private CurrentFiscalYear() {
        ZonedDateTime now = ZonedDateTime.now();
        // this syntax sucks.  we need operator overloading in Java!
        boolean afterMarch = (now.getMonth().compareTo(Month.NOVEMBER) >= 0);
        if (afterMarch) {
            fiscalYear = now.getYear();
            duesFiscalYear = now.getYear()+1;
        } else {
            fiscalYear = now.getYear()-1;
            duesFiscalYear = now.getYear();
        }
    }

    public static int getFiscalYear() {
        CurrentFiscalYear year = new CurrentFiscalYear();
        return year.fiscalYear;
    }

    public static int getNextFiscalYear() {
        return getFiscalYear()+1;
    }

    public static int getDuesFiscalYear() {
        return new CurrentFiscalYear().duesFiscalYear;
    }
}
