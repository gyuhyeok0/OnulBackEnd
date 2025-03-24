package onul.restapi.agreement.service;

import lombok.RequiredArgsConstructor;
import onul.restapi.agreement.repository.TermsOfServiceRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermsOfServiceService {

    private final TermsOfServiceRepository repository;

    public String getTermsContentByLang(String lang) {
        return repository.findByLanguage(lang)
                .map(terms -> terms.getContent())
                .orElse("Terms of service not available in your language.");
    }
}
