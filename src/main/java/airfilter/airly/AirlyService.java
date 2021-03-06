package airfilter.airly;

import airfilter.airly.entity.Measurements;
import airfilter.airly.entity.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

@Service
public class AirlyService {

    private Logger logger = LoggerFactory.getLogger(AirlyService.class);

    private static final String MEASUREMENTS_URI = "/measurements/installation?installationId=";
    private static final MediaType[] ACCEPT = {MediaType.APPLICATION_JSON};
    private static final String X_RATE_LIMIT_LIMIT_DAY = "X-RateLimit-Limit-day";
    private static final String X_RATE_LIMIT_LIMIT_MINUTE = "X-RateLimit-Limit-minute";
    private static final String X_RATE_LIMIT_REMAINING_DAY = "X-RateLimit-Remaining-day";
    private static final String X_RATE_LIMIT_REMAINING_MINUTE = "X-RateLimit-Remaining-minute";


    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    AirlyProperties airlyProperties;

    public Optional<Response<Measurements>> getMeasurements(Integer installationId) {
        String url = applicationProperties.getApiUrl() + MEASUREMENTS_URI + installationId;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(ACCEPT));
        headers.set("apikey", airlyProperties.getKey());
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        RestTemplate template = new RestTemplate();
        try {
            ResponseEntity<Measurements> responseEntity = template.exchange(url, HttpMethod.GET, requestEntity, Measurements.class);

                Response<Measurements> response = readResponse(responseEntity, responseEntity.getBody());
                return Optional.of(response);

        } catch (RestClientException e) {
            logger.error("Error during get data from Airly.", e);
            return Optional.empty();
        }
    }

    private Response<Measurements> readResponse(ResponseEntity<Measurements> responseEntity, Measurements body) {
        Response<Measurements> response;
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            response = new Response<>(body);
        } else {
            logger.info("Response code: {}", responseEntity.getStatusCode());
            response = new Response<>();
        }
        HttpHeaders responseHeaders = responseEntity.getHeaders();
        if (responseHeaders.containsKey(X_RATE_LIMIT_LIMIT_DAY)) {
            response.setLimitDay(Integer.parseInt(responseHeaders.getFirst(X_RATE_LIMIT_LIMIT_DAY)));
        }
        if (responseHeaders.containsKey(X_RATE_LIMIT_LIMIT_MINUTE)) {
            response.setLimitMinute(Integer.parseInt(responseHeaders.getFirst(X_RATE_LIMIT_LIMIT_MINUTE)));
        }
        if (responseHeaders.containsKey(X_RATE_LIMIT_REMAINING_DAY)) {
            response.setRemainingDay(Integer.parseInt(responseHeaders.getFirst(X_RATE_LIMIT_REMAINING_DAY)));
        }
        if (responseHeaders.containsKey(X_RATE_LIMIT_REMAINING_MINUTE)) {
            response.setRemainingMinute(Integer.parseInt(responseHeaders.getFirst(X_RATE_LIMIT_REMAINING_MINUTE)));
        }
        return response;
    }
}
