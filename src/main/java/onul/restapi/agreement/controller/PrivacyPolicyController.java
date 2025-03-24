package onul.restapi.agreement.controller;

import jakarta.servlet.http.HttpServletRequest;
import onul.restapi.agreement.service.PrivacyPolicyService;
import onul.restapi.agreement.service.TermsOfServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;
import java.util.Map;

@Controller
public class PrivacyPolicyController {

    private static final Map<String, String> LANGUAGE_MAP = Map.of(
            "ko", "ko",
            "ja", "ja",
            "es", "es",
            "en", "en"
    );

    private final PrivacyPolicyService privacyPolicyService;
    private final TermsOfServiceService termsOfServiceService;

    public PrivacyPolicyController(PrivacyPolicyService privacyPolicyService, TermsOfServiceService termsOfServiceService) {
        this.privacyPolicyService = privacyPolicyService;
        this.termsOfServiceService = termsOfServiceService;
    }

    private String detectLanguage(HttpServletRequest request) {
        Locale userLocale = request.getLocale();
        String language = userLocale.getLanguage();
        return LANGUAGE_MAP.getOrDefault(language, "en");
    }

    // 개인정보 처리방침
    @GetMapping("/privacy-policy.html")
    public ModelAndView getPrivacyPolicy(HttpServletRequest request) {
        String lang = detectLanguage(request);
        String content = privacyPolicyService.getPolicyContentByLang(lang);
        String pageTitle = switch (lang) {
            case "ko" -> "Onul 개인정보 처리방침";
            case "ja" -> "Onul プライバシーポリシー";
            case "es" -> "Política de privacidad de Onul";
            default -> "Onul Privacy Policy";
        };

        ModelAndView mav = new ModelAndView("privacy-policy-template");
        mav.addObject("policyContent", content);
        mav.addObject("pageTitle", pageTitle);
        return mav;
    }

    // 이용약관
    @GetMapping("/terms-of-service.html")
    public ModelAndView getTermsOfService(HttpServletRequest request) {
        String lang = detectLanguage(request);
        String content = termsOfServiceService.getTermsContentByLang(lang);
        String pageTitle = switch (lang) {
            case "ko" -> "Onul 이용약관";
            case "ja" -> "Onul 利用規約";
            case "es" -> "Términos de servicio de Onul";
            default -> "Onul Terms of Service";
        };

        ModelAndView mav = new ModelAndView("terms-of-service-template");
        mav.addObject("policyContent", content);
        mav.addObject("pageTitle", pageTitle);
        return mav;
    }

}
