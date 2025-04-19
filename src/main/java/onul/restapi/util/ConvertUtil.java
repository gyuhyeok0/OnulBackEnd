package onul.restapi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import onul.restapi.member.dto.MemberDTO;
import onul.restapi.member.entity.Members;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConvertUtil {

    public static Object convertObjectToJsonObject(Object obj){

        ObjectMapper mapper = new ObjectMapper();
        JSONParser parser = new JSONParser();
        String convertJsonString;
        Object converObj;

        try {
            convertJsonString = mapper.writeValueAsString(obj);
            converObj = parser.parse(convertJsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return converObj;
    }

    public static MemberDTO toMemberDTO(Members member) {
        return MemberDTO.builder()
                .memberId(member.getMemberId())
                .memberPassword(member.getMemberPassword()) // 비밀번호 필요 없으면 null도 가능
                .memberPhoneNumber(member.getMemberPhoneNumber())
                .phoneNumber(member.getMemberPhoneNumber()) // 같은 번호 쓰는 구조인 경우
                .memberStatus(member.getMemberStatus().name())
                .memberSignupDate(member.getMemberSignupDate())
                .build();
    }

}
