package onul.restapi.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoticeResponseDto {
    private boolean active;

    private String titleKo;
    private String contentKo;

    private String titleEn;
    private String contentEn;

    private String titleJa;
    private String contentJa;

    private String titleEs;
    private String contentEs;
}
