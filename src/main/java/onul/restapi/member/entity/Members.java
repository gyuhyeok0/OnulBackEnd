package onul.restapi.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.Optional;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder // 빌더 패턴 추가
@Table(name = "tbl_members", indexes = {
        @Index(name = "idx_member_phone_number", columnList = "member_phone_number"),
        @Index(name = "idx_member_id", columnList = "member_id")  // 추가된 인덱스
})
@ToString
public class Members {

    @Id
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "member_password")
    private String memberPassword;

    @Column(name = "member_phone_number")
    private String memberPhoneNumber;

    @Column(name = "member_countryCode")
    private String memberCountryCode;

    // 약관 동의 상태 enum으로 처리
    @Enumerated(EnumType.STRING)
    @Column(name = "member_userConsent_state")
    private UserConsentState memberUserConsent;

    // 회원가입 일시
    @Column(name = "member_signup_date")
    private Date memberSignupDate;

    // 회원 상태 enum으로 처리
    @Enumerated(EnumType.STRING)
    @Column(name = "member_status")
    private MemberStatus memberStatus;


    // 상태 업데이트 메서드
    public Members updateMemberStatus(MemberStatus status) {
        this.memberStatus = status;
        return this;
    }

}
