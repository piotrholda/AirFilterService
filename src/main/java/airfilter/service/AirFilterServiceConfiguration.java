package airfilter.service;

import airfilter.airly.AirlyScheduler;
import airfilter.airly.AirlyService;
import airfilter.airly.Cache;
import airfilter.irsend.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AirFilterServiceConfiguration {

    @Bean
    Executor executor() {
        return new Executor();
    }

    @Bean
    Cache cache() {
        return new Cache();
    }

    @Bean
    AirlyScheduler airlyScheduler() {
        return new AirlyScheduler();
    }

    @Bean
    AirlyService airlyService() {
        return new AirlyService();
    }
}
