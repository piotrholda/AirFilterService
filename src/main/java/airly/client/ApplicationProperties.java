package airly.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("application")
public class ApplicationProperties {

    private String apiUrl;
    private Integer limitDay;
    private Integer limitMinute;
    private List<Integer> installationIds;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public Integer getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(Integer limitDay) {
        this.limitDay = limitDay;
    }

    public Integer getLimitMinute() {
        return limitMinute;
    }

    public void setLimitMinute(Integer limitMinute) {
        this.limitMinute = limitMinute;
    }

    public List<Integer> getInstallationIds() {
        return installationIds;
    }

    public void setInstallationIds(List<Integer> installationIds) {
        this.installationIds = installationIds;
    }
}
