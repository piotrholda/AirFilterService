package airfilter.airly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.lang.Math.max;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@EnableScheduling
@Service
public class AirlyScheduler {

    private Logger logger = LoggerFactory.getLogger(AirlyScheduler.class);

    private static final int SAFE_SHIFT_TIME = 1000;
    private Date lastUpdateTime;
    private Integer limitDay;
    private Integer limitMinute;
    private Integer remainingDay;
    private Integer remainingMinute;

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    AirlyService airlyService;

    @Autowired
    Cache cache;

    @PostConstruct
    public void init() {
        limitDay = applicationProperties.getLimitDay();
        limitMinute = applicationProperties.getLimitMinute();
        remainingDay = limitDay;
        remainingMinute = limitMinute;
    }

    @Scheduled(fixedRate = 1000)
    public void update() {
        if (isNull(lastUpdateTime)) {
            read();
        } else {
            Date current = new Date();
            Date nextReadTime = nextReadTime();
            if (current.after(nextReadTime)) {
                read();
            }
        }
    }

    private void read() {
        logger.info("Load Airly data.");
        for (Integer id : applicationProperties.getInstallationIds()) {
            airlyService.getMeasurements(id).ifPresent(response -> {
                lastUpdateTime = new Date();
                cache.update(id, response.getBody());
                if (nonNull(response.getLimitDay())) {
                    if (!limitDay.equals(response.getLimitDay())) {
                        logger.info("limitDay {} is different than property value {}", response.getLimitDay(), limitDay);
                        limitDay = response.getLimitDay();
                    }
                }
                if (nonNull(response.getRemainingMinute())) {
                    if (!limitMinute.equals(response.getLimitMinute())) {
                        logger.info("limitMinute {} is different than property value {}", response.getLimitMinute(), limitMinute);
                        limitMinute = response.getLimitMinute();
                    }
                }
                if (nonNull(response.getRemainingDay())) {
                    remainingDay = response.getRemainingDay();
                }
                if (nonNull(response.getRemainingMinute())) {
                    remainingMinute = response.getRemainingMinute();
                }
            });
        }
    }

    private Date nextReadTime() {
        Calendar timeout = Calendar.getInstance();
        timeout.setTimeInMillis(lastUpdateTime.getTime() + intervalInMilliseconds());
        return timeout.getTime();
    }

    private long intervalInMilliseconds() {
        long toMidnight = todayMidnight() - lastUpdateTime.getTime();
        int numberOfInstallations = applicationProperties.getInstallationIds().size();
        if (remainingDay < numberOfInstallations) {
            remainingDay = applicationProperties.getLimitDay();
            return toMidnight + SAFE_SHIFT_TIME;
        }
        long dailyTick = toMidnight / remainingDay;
        long toNextMinute = nextMinute() - lastUpdateTime.getTime();
        if (remainingMinute < numberOfInstallations) {
            remainingMinute = applicationProperties.getLimitMinute();
            return toNextMinute + SAFE_SHIFT_TIME;
        }
        long minutelyTick = toNextMinute / remainingMinute;
        return max(dailyTick, minutelyTick) * numberOfInstallations;
    }

    private long todayMidnight() {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        date.add(Calendar.DAY_OF_MONTH, 1);
        return date.getTimeInMillis();
    }

    private long nextMinute() {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        date.add(Calendar.MINUTE, 1);
        return date.getTimeInMillis();
    }

}
