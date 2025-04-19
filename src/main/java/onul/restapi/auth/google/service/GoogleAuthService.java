package onul.restapi.auth.google.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import lombok.RequiredArgsConstructor;
import onul.restapi.auth.google.entity.GoogleMemberLink;
import onul.restapi.auth.google.repository.GoogleMemberLinkRepository;
import onul.restapi.member.dto.MemberDTO;
import onul.restapi.member.dto.TokenDTO;
import onul.restapi.member.entity.MemberStatus;
import onul.restapi.member.entity.Members;
import onul.restapi.member.entity.UserConsentState;
import onul.restapi.member.repository.MemberRepository;
import onul.restapi.util.ConvertUtil;
import onul.restapi.util.TokenUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final MemberRepository memberRepository;
    private final GoogleMemberLinkRepository googleMemberLinkRepository;
    private final TokenUtils tokenUtils;

    @Value("${google.ios-id}")
    private String googleIosClientId;

    @Value("${google.web-id}")
    private String googleWebClientId;

    public JSONObject authenticateWithGoogle(String idTokenString) throws Exception {
        // 1️⃣ Google 토큰 검증기 생성
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance()
        )
                .setAudience(Arrays.asList(googleIosClientId, googleWebClientId))
                .setIssuer("https://accounts.google.com")
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new IllegalArgumentException("Invalid Google ID token.");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String googleUserId = payload.getSubject();

            // 2️⃣ Google ID로 기존 회원 조회 또는 새로 생성
            GoogleMemberLink link = googleMemberLinkRepository.findByGoogleId(googleUserId)
                    .orElseGet(() -> {
                        String newMemberId = "google_" + UUID.randomUUID().toString().substring(0, 8);
                        Members newMember = Members.builder()
                                .memberId(newMemberId)
                                .memberUserConsent(UserConsentState.AGREED)
                                .memberSignupDate(new java.sql.Date(System.currentTimeMillis()))
                                .memberStatus(MemberStatus.ACTIVE)
                                .build();
                        memberRepository.save(newMember);

                        GoogleMemberLink newLink = GoogleMemberLink.builder()
                                .googleId(googleUserId)
                                .memberId(newMemberId)
                                .build();
                        googleMemberLinkRepository.save(newLink);
                        return newLink;
                    });

            // 3️⃣ 사용자 정보 조회 및 JWT 발급
            Members memberEntity = memberRepository.findById(link.getMemberId())
                    .orElseThrow(() -> new IllegalStateException("회원 정보가 존재하지 않습니다."));
            MemberDTO member = ConvertUtil.toMemberDTO(memberEntity);

            String accessToken = tokenUtils.generateJwtToken(member);
            String refreshToken = tokenUtils.generateRefreshToken(member);

            // 4️⃣ 응답 반환
            Map<String, Object> result = new HashMap<>();
            result.put("accessToken", accessToken);
            result.put("refreshToken", refreshToken);
            result.put("memberId", member.getMemberId());

            return new JSONObject(result);

        } catch (Exception e) {
            throw new IllegalArgumentException("Google login failed: " + e.getMessage());
        }
    }

}
