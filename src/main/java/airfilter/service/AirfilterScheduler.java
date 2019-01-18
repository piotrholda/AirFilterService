package airfilter.service;

import airly.client.Cache;
import irsend.executor.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static airfilter.service.FilterState.OFF;
import static airfilter.service.FilterState.ON;

@EnableScheduling
@Service
public class AirfilterScheduler {

    private FilterState filterState;

    @Autowired
    AirfilterProperties airfilterProperties;

    @Autowired
    TimeService timeService;

    @Autowired
    Cache cache;

    @Autowired
    Executor executor;

    @PostConstruct
    public void init() {
        filterState = OFF;
    }

    @Scheduled(fixedRate = 1000)
    public void update() throws IOException, InterruptedException {
        boolean isOn = cache.isNormExceeded() && !isAbsence();
        if (isOn) {
            on();
        } else {
            off();
        }
    }

    private void on() throws IOException, InterruptedException {
        if (OFF.equals(filterState)) {
            System.out.println("" + timeService.getCurrentTime() + " Air filter on");
            executor.execute(airfilterProperties.getDeviceName(), airfilterProperties.getDeviceOn());
            filterState = ON;
        }
    }

    private void off() throws IOException, InterruptedException {
        if (ON.equals(filterState)) {
            System.out.println("" + timeService.getCurrentTime() + " Air filter off");
            executor.execute(airfilterProperties.getDeviceName(), airfilterProperties.getDeviceOff());
            filterState = OFF;
        }
    }

    private boolean isAbsence() {
        Date now = timeService.getCurrentTime();
        return isWorkingDay(now) && isWorkingHour(now);
    }

    private boolean isWorkingHour(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY) >= airfilterProperties.getAbsenceHourFrom() && cal.get(Calendar.HOUR_OF_DAY) < airfilterProperties.getAbsenceHourTo();
    }

    private boolean isWorkingDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) >= Calendar.MONDAY && cal.get(Calendar.DAY_OF_WEEK) <= Calendar.FRIDAY;
    }
}
