package onul.restapi.member.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TokenDTO {

    private String grantType;
    private String memberName;
    private String accessToken;
    private Long accessTokenExpiresIn;

}
