package airly.client.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Current {

    private Date fromDateTime;
    private Date tillDateTime;
    private List<Value> values;
    private List<Standard> standards;

    public List<Standard> getStandards() {
        return standards;
    }

    public void setStandards(List<Standard> standards) {
        this.standards = standards;
    }

    public Date getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(Date fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public Date getTillDateTime() {
        return tillDateTime;
    }

    public void setTillDateTime(Date tillDateTime) {
        this.tillDateTime = tillDateTime;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

}
