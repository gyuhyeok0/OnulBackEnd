package onul.restapi.version.service;

import onul.restapi.version.entity.AppVersionEntity;
import onul.restapi.version.repository.AppVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppVersionService {
    private final AppVersionRepository appVersionRepository;

    public AppVersionService(AppVersionRepository appVersionRepository) {
        this.appVersionRepository = appVersionRepository;
    }

    @Transactional(readOnly = true)
    public String getLatestVersion() {
        return appVersionRepository.findTopByOrderByIdDesc()
                .map(AppVersionEntity::getVersion)
                .orElse("1.0.0"); // 데이터 없을 경우 기본값 설정
    }
}
