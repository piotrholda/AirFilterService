package airfilter.service;

import airly.client.AirlyScheduler;
import airly.client.AirlyService;
import airly.client.Cache;
import irsend.executor.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

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
