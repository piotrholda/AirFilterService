package airfilter.airly;

import airfilter.airly.entity.Current;
import airfilter.airly.entity.Measurements;
import airfilter.airly.entity.Standard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.nonNull;

@Service
public class Cache {

    private Logger logger = LoggerFactory.getLogger(Cache.class);

    private Map<Integer, Measurements> measurements;

    @PostConstruct
    public void init() {
        measurements = new ConcurrentHashMap<>();
    }

    public void update(Integer installationId, Measurements measurements) {
        if (nonNull(measurements)
                && nonNull(measurements.getCurrent())
                && nonNull(measurements.getCurrent().getStandards())
                && !measurements.getCurrent().getStandards().isEmpty()) {
            logger.info("Airly data updated.");
            this.measurements.put(installationId, measurements);
        }
    }

    public boolean isNormExceeded() {
        return measurements.values().stream()
                .map(Measurements::getCurrent)
                .filter(Objects::nonNull)
                .map(Current::getStandards)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(Standard::getPercent)
                .filter(Objects::nonNull)
                .anyMatch(d -> d.compareTo(100.0) > 0);
    }

}
