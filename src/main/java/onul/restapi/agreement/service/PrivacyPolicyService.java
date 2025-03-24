package onul.restapi.agreement.service;

import lombok.RequiredArgsConstructor;
import onul.restapi.agreement.repository.PrivacyPolicyRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivacyPolicyService {

    private final PrivacyPolicyRepository repository;

    public String getPolicyContentByLang(String lang) {
        return repository.findByLanguage(lang)
                .map(policy -> policy.getContent())
                .orElse("Privacy policy not available in your language.");
    }
}
