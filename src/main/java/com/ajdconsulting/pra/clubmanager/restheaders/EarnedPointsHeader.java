package com.ajdconsulting.pra.clubmanager.restheaders;

import com.ajdconsulting.pra.clubmanager.domain.EarnedPoints;

/**
 * Header for earned points resources since there is logic there that should be encapsulated
 * in a class
 *
 * @author adelimon
 */
public class EarnedPointsHeader {

    private StringBuilder headerString;

    public EarnedPointsHeader(EarnedPoints result) {
        headerString = new StringBuilder();
        boolean paid = result.isPaid();
        headerString.append(result.getMember().getName() + "'s ");
        if (!paid) {
            headerString.append(result.getPointValue() + " pts recorded for ");
        } else {
            headerString.append("pay of " + result.getCashValue() + " recorded for ");
        }

        headerString.append(result.getDescription() + " on " + result.getDate());
    }

    public String toString() {
        return headerString.toString();
    }
}
