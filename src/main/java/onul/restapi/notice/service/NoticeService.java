package onul.restapi.notice.service;

import lombok.RequiredArgsConstructor;
import onul.restapi.notice.entity.Notice;
import onul.restapi.notice.repository.NoticeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Notice getLatestNotice() {
        // 가장 최근 등록된 공지 1개 가져오기
        Optional<Notice> optionalNotice = noticeRepository.findTopByOrderByIdDesc();
        return optionalNotice.orElse(null);
    }
}
