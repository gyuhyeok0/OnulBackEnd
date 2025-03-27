package onul.restapi.notice.controller;

import onul.restapi.notice.dto.NoticeResponseDto;
import onul.restapi.notice.entity.Notice;
import onul.restapi.notice.service.NoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
public class NoticeController {



    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/getNotice")
    public ResponseEntity<?> getNotice() {
        Notice notice = noticeService.getLatestNotice();

        if (notice == null || !notice.isActive()) {
            return ResponseEntity.ok("No active notice");
        }

        NoticeResponseDto responseDto = new NoticeResponseDto(
                notice.isActive(),
                notice.getTitleKo(), notice.getContentKo(),
                notice.getTitleEn(), notice.getContentEn(),
                notice.getTitleJa(), notice.getContentJa(),
                notice.getTitleEs(), notice.getContentEs()
        );

        return ResponseEntity.ok(responseDto);
    }

}
