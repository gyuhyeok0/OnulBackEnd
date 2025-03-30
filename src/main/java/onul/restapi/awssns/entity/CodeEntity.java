package onul.restapi.awssns.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "verification_codes", indexes = {
        @Index(name = "idx_phone_number", columnList = "phone_number"),
        @Index(name = "idx_expiry_time", columnList = "expiry_time") // 만료 시간에 인덱스 추가
})
public class CodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;
    private String code;
    private Long expiryTime;

    @Column(nullable = false)
    private int requestCount;  // 요청 횟수를 저장하는 필드

    @Column(nullable = false)
    private Long lastRequestTime;  // 요청 제한을 위한 마지막 요청 시간

    @Column(nullable = false)
    private int dailyRequestCount;  // 하루 동안의 요청 횟수를 저장하는 필드

    @Column(nullable = false)
    private Long lastRequestDay;  // 일일 요청 제한을 위한 마지막 요청 날짜

    // 기본 생성자
    public CodeEntity() {}

    // 모든 필드를 초기화하는 생성자
    public CodeEntity(String phoneNumber, String code, Long expiryTime, Long lastRequestTime) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.expiryTime = expiryTime;
        this.requestCount = 1; // 첫 요청 시 기본값으로 1 설정
        this.lastRequestTime = lastRequestTime;
        this.dailyRequestCount = 1; // 첫 일일 요청으로 1 설정
        this.lastRequestDay = System.currentTimeMillis();  // 현재 날짜를 기준으로 저장
    }

    // Getter와 Setter
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
}
