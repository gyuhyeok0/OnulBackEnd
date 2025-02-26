package onul.restapi.awssns.service;

import onul.restapi.awssns.entity.CodeEntity;
import onul.restapi.awssns.repository.CodeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeEntityService {

    private final CodeRepository codeRepository;

    public CodeEntityService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    // 매일 자정에 1주일 지난 데이터 삭제
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void deleteExpiredCodes() {
        long oneWeekAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000; // 일주일 전의 타임스탬프

        // 일주일이 지난 인증 코드 삭제
        List<CodeEntity> expiredCodes = codeRepository.findByExpiryTimeBefore(oneWeekAgo);
        codeRepository.deleteAll(expiredCodes);
        System.out.println("만료된 코드 삭제 작업 실행됨");
    }
}
