package airfilter.service;

import airly.client.AirlyProperties;
import airly.client.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AirfilterProperties.class, ApplicationProperties.class, AirlyProperties.class})
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class);
    }

}
