package com.ajdconsulting.pra.clubmanager.scheduled;

import com.ajdconsulting.pra.clubmanager.service.MemberDuesCalculationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Task to calculate the member dues.  This will run periodically to calculate the dues and store them in the DB.
 * Then all future runs can grab the data from that table instead of calculating on the fly.
 *
 * @author adelimon
 */
@Component
public class MemberDuesCalculationTask {

    @Inject
    private MemberDuesCalculationService duesCalculationService;

    @Scheduled(cron = "0 0 0 ? * SUN")
    public void calculateDues() {
        duesCalculationService.runAndStoreDuesCalculations();
    }
}
