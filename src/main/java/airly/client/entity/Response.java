package airly.client.entity;

public class Response<T> {

    private Integer limitDay;
    private Integer limitMinute;
    private Integer remainingDay;
    private Integer remainingMinute;

    private T body;

    public Response() {
    }

    public Response(T body) {
        this.body = body;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
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

    public Integer getRemainingDay() {
        return remainingDay;
    }

    public void setRemainingDay(Integer remainingDay) {
        this.remainingDay = remainingDay;
    }

    public Integer getRemainingMinute() {
        return remainingMinute;
    }

    public void setRemainingMinute(Integer remainingMinute) {
        this.remainingMinute = remainingMinute;
    }
}
