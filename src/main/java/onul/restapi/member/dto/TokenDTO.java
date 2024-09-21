package onul.restapi.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TokenDTO {

    private String grantType;
    private String memberId;
    private String memberName;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date memberSignupDate;

}
