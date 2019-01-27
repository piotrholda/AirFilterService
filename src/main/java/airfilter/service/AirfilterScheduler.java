package airfilter.service;

import airfilter.airly.Cache;
import airfilter.config.ConfigService;
import airfilter.irsend.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.time.LocalDateTime;

import static airfilter.service.FilterState.OFF;
import static airfilter.service.FilterState.ON;

@EnableScheduling
@Service
public class AirfilterScheduler {

    private Logger logger = LoggerFactory.getLogger(AirfilterScheduler.class);

    private FilterState filterState;

    @Autowired
    AirfilterProperties airfilterProperties;

    @Autowired
    ConfigService configService;

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
        boolean isOn = cache.isNormExceeded() && !configService.isAbsence(LocalDateTime.now());
        if (isOn) {
            on();
        } else {
            off();
        }
    }

    private void on() throws IOException, InterruptedException {
        if (OFF.equals(filterState)) {
            logger.info("Air filter on");
            executor.execute(airfilterProperties.getDeviceName(), airfilterProperties.getDeviceOn());
            filterState = ON;
        }
    }

    private void off() throws IOException, InterruptedException {
        if (ON.equals(filterState)) {
            logger.info("Air filter off");
            executor.execute(airfilterProperties.getDeviceName(), airfilterProperties.getDeviceOff());
            filterState = OFF;
        }
    }

}
