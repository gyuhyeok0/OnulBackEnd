package onul.restapi.agreement;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;
import java.util.Map;

@Controller
public class PrivacyPolicyController {

    private static final Map<String, String> LANGUAGE_MAP = Map.of(
            "ko", "ko", "ja", "ja", "es", "es", "fr", "fr",
            "de", "de", "it", "it", "nl", "nl", "en", "en"
    );

    private String detectLanguage(HttpServletRequest request) {
        Locale userLocale = request.getLocale();
        String language = userLocale.getLanguage();

        // 지원되지 않는 언어는 기본적으로 영어로 설정
        return LANGUAGE_MAP.getOrDefault(language, "en");
    }

    @GetMapping("/privacy-policy.html")
    public ModelAndView getPrivacyPolicy(HttpServletRequest request) {
        String lang = detectLanguage(request);
        return new ModelAndView("privacy-policy_" + lang);
    }

    @GetMapping("/terms-of-service.html")
    public ModelAndView getServicePolicy(HttpServletRequest request) {
        String lang = detectLanguage(request);
        return new ModelAndView("terms-of-service_" + lang);
    }
}
