package onul.restapi.onboarding.dto;

public class OnboardingDTO {

    private Long id;
    private String phoneNumber;
    private String code;
    private Long expiryTime;
    private int requestCount;
    private Long lastRequestTime;
    private int dailyRequestCount;
    private Long lastRequestDay;

    public OnboardingDTO() {
    }

    public OnboardingDTO(Long id, String phoneNumber, String code, Long expiryTime, int requestCount, Long lastRequestTime, int dailyRequestCount, Long lastRequestDay) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.expiryTime = expiryTime;
        this.requestCount = requestCount;
        this.lastRequestTime = lastRequestTime;
        this.dailyRequestCount = dailyRequestCount;
        this.lastRequestDay = lastRequestDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public Long getLastRequestTime() {
        return lastRequestTime;
    }

    public void setLastRequestTime(Long lastRequestTime) {
        this.lastRequestTime = lastRequestTime;
    }

    public int getDailyRequestCount() {
        return dailyRequestCount;
    }

    public void setDailyRequestCount(int dailyRequestCount) {
        this.dailyRequestCount = dailyRequestCount;
    }

    public Long getLastRequestDay() {
        return lastRequestDay;
    }

    public void setLastRequestDay(Long lastRequestDay) {
        this.lastRequestDay = lastRequestDay;
    }

    @Override
    public String toString() {
        return "OnboardingDTO{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", code='" + code + '\'' +
                ", expiryTime=" + expiryTime +
                ", requestCount=" + requestCount +
                ", lastRequestTime=" + lastRequestTime +
                ", dailyRequestCount=" + dailyRequestCount +
                ", lastRequestDay=" + lastRequestDay +
                '}';
    }
}
