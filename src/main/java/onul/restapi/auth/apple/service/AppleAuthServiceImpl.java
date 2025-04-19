package onul.restapi.auth.apple.service;

import onul.restapi.auth.apple.AppleTokenVerifier;
import onul.restapi.auth.apple.entity.AppleMemberLink;
import onul.restapi.auth.apple.repository.AppleMemberLinkRepository;
import onul.restapi.autoAdaptAi.service.ExerciseSettingService;
import onul.restapi.member.dto.MemberDTO;
import onul.restapi.member.dto.TokenDTO;
import onul.restapi.member.entity.MemberStatus;
import onul.restapi.member.entity.Members;
import onul.restapi.member.entity.UserConsentState;
import onul.restapi.member.repository.MemberRepository;
import onul.restapi.util.ConvertUtil;
import onul.restapi.util.TokenUtils;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AppleAuthServiceImpl implements AppleAuthService {

    private final MemberRepository memberRepository;
    private final ExerciseSettingService exerciseSettingService;
    private final AppleMemberLinkRepository appleMemberLinkRepository;
    private final TokenUtils tokenUtils;
    private final AppleTokenVerifier appleTokenVerifier;

    public AppleAuthServiceImpl(MemberRepository memberRepository, ExerciseSettingService exerciseSettingService, AppleMemberLinkRepository appleMemberLinkRepository, TokenUtils tokenUtils, AppleTokenVerifier appleTokenVerifier) {
        this.memberRepository = memberRepository;
        this.exerciseSettingService = exerciseSettingService;
        this.appleMemberLinkRepository = appleMemberLinkRepository;
        this.tokenUtils = tokenUtils;
        this.appleTokenVerifier = appleTokenVerifier;
    }

    @Override
    public JSONObject authenticateWithApple(String identityToken) throws Exception {
        // 1️⃣ identityToken → Apple 고유 유저 ID 추출
        String appleUserId = appleTokenVerifier.verifyAndExtractSub(identityToken);

        // 2️⃣ 기존 연결 존재하는지 확인 or 신규 가입
        AppleMemberLink link = appleMemberLinkRepository.findByAppleId(appleUserId)
                .orElseGet(() -> {
                    String generatedMemberId = "apple_" + UUID.randomUUID().toString().substring(0, 8);

                    Members newMember = Members.builder()
                            .memberId(generatedMemberId)
                            .memberPassword(null)
                            .memberCountryCode(null)
                            .memberPhoneNumber(null)
                            .memberUserConsent(UserConsentState.AGREED)
                            .memberSignupDate(new java.sql.Date(System.currentTimeMillis()))
                            .memberStatus(MemberStatus.ACTIVE)
                            .build();

                    memberRepository.save(newMember);

                    AppleMemberLink newLink = AppleMemberLink.builder()
                            .appleId(appleUserId)
                            .memberId(generatedMemberId)
                            .build();
                    appleMemberLinkRepository.save(newLink);

                    exerciseSettingService.autoAdaptDefaultSetting(generatedMemberId);

                    return newLink;
                });

        Members memberEntity = memberRepository.findById(link.getMemberId())
                .orElseThrow(() -> new IllegalStateException("회원 정보가 존재하지 않습니다."));

        MemberDTO member = ConvertUtil.toMemberDTO(memberEntity); // 🔁 변환

        // 3️⃣ 토큰 생성
        String accessToken = tokenUtils.generateJwtToken(member);
        String refreshToken = tokenUtils.generateRefreshToken(member);

        // 4️⃣ TokenDTO 생성
        TokenDTO tokenDTO = TokenDTO.builder()
                .memberId(member.getMemberId())
                .memberSignupDate(member.getMemberSignupDate())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType("Bearer")
                .build();

        // 5️⃣ 응답 구성 (기존과 동일한 JSON 구조)
        JSONObject jsonValue = (JSONObject) ConvertUtil.convertObjectToJsonObject(tokenDTO);

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        result.put("memberId", member.getMemberId());

        return new JSONObject(result);
    }
}
