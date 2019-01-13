package airly.client;

import airly.client.entity.Current;
import airly.client.entity.Measurements;
import airly.client.entity.Standard;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.nonNull;

@Service
public class Cache {

    private Map<Integer, Measurements> measurements;

    @PostConstruct
    public void init() {
        measurements = new ConcurrentHashMap<>();
    }

    public void update(Integer installationId, Measurements measurements) {
        if (nonNull(measurements)) {
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
