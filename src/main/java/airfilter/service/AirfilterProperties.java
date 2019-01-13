package airfilter.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@PropertySource("classpath:airfilter.properties")
@ConfigurationProperties("airfilter")
public class AirfilterProperties {

    private String deviceName;
    private List<String> deviceOn;
    private List<String> deviceOff;

    private Integer absenceHourFrom;
    private Integer absenceHourTo;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public List<String> getDeviceOn() {
        return deviceOn;
    }

    public void setDeviceOn(List<String> deviceOn) {
        this.deviceOn = deviceOn;
    }

    public List<String> getDeviceOff() {
        return deviceOff;
    }

    public void setDeviceOff(List<String> deviceOff) {
        this.deviceOff = deviceOff;
    }

    public Integer getAbsenceHourFrom() {
        return absenceHourFrom;
    }

    public void setAbsenceHourFrom(Integer absenceHourFrom) {
        this.absenceHourFrom = absenceHourFrom;
    }

    public Integer getAbsenceHourTo() {
        return absenceHourTo;
    }

    public void setAbsenceHourTo(Integer absenceHourTo) {
        this.absenceHourTo = absenceHourTo;
    }
}
