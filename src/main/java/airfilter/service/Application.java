package airfilter.service;

import airfilter.airly.AirlyProperties;
import airfilter.airly.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties({AirfilterProperties.class, ApplicationProperties.class, AirlyProperties.class})
@ComponentScan({"airfilter.airly", "airfilter.config", "airfilter.irsend", "airfilter.service"})
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class);
    }

}
