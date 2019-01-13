package airly.client;

import airly.client.entity.Measurements;
import airly.client.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class AirlyService {

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

    public Response<Measurements> getMeasurements(Integer installationId) {
        String url = applicationProperties.getApiUrl() + MEASUREMENTS_URI + installationId;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(ACCEPT));
        headers.set("apikey", airlyProperties.getKey());
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        RestTemplate template = new RestTemplate();
        ResponseEntity<Measurements> responseEntity = template.exchange(url, HttpMethod.GET, requestEntity, Measurements.class);
        Response<Measurements> response = readResponse(responseEntity, responseEntity.getBody());
        return response;
    }

    private Response<Measurements> readResponse(ResponseEntity<Measurements> responseEntity, Measurements body) {
        Response<Measurements> response = new Response<>(body);
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
