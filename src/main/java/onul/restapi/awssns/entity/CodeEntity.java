package onul.restapi.awssns.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "verification_codes")
public class CodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phoneNumber;
    private String code;
    private Long expiryTime;

    // 기본 생성자
    public CodeEntity() {}

    // 모든 필드를 초기화하는 생성자
    public CodeEntity(String phoneNumber, String code, Long expiryTime) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.expiryTime = expiryTime;
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
}
