package onul.restapi.inquiry.dto;

import java.time.LocalDateTime;

public class InquiryDTO {

    private Long id;
    private String memberId;  // 문의한 회원의 아이디, 비회원일 경우 null
    private String email;  // 이메일 (필수)
    private String title;  // 문의 제목 (필수)
    private String content;  // 문의 내용 (필수)
    private LocalDateTime createdAt;  // 문의 제출 시간

    // 기본 생성자
    public InquiryDTO() {}

    // 모든 필드를 포함하는 생성자
    public InquiryDTO(Long id, String memberId, String email, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.email = email;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "InquiryDTO{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
