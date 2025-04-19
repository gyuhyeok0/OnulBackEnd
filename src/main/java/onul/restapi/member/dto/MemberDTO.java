package onul.restapi.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class MemberDTO implements UserDetails {

    @JsonProperty("memberId")
    private String memberId;

    @JsonProperty("memberPassword")
    private String memberPassword;

    @JsonProperty("memberPhoneNumber")
    private String memberPhoneNumber;

    @JsonProperty("phoneNumber") // 추가된 필드
    private String phoneNumber;

    @JsonProperty("memberStatus")
    private String memberStatus;

    @JsonProperty("memberSignupDate") // 회원가입 날짜 필드
    private Date memberSignupDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // 권한 설정 비어 있음
    }

    @Override
    public String getPassword() {
        return this.memberPassword;
    }

    @Override
    public String getUsername() {
        return this.memberId;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
