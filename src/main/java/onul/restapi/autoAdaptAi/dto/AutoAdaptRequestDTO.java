package onul.restapi.autoAdaptAi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AutoAdaptRequestDTO {
    private String memberId;
    private boolean checkDate;
}
