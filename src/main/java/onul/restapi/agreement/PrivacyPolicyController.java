package onul.restapi.agreement;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
public class PrivacyPolicyController {

    @GetMapping("/privacy-policy.html")
    public ModelAndView getPrivacyPolicy(HttpServletRequest request) {
        // 사용자의 언어 감지
        Locale userLocale = request.getLocale();
        String language = userLocale.getLanguage();


        // 기본 언어 설정 (영어)
        String viewName = "privacy-policy_en";

        // 언어에 따라 뷰 선택
        switch (language) {
            case "ko":
                viewName = "privacy-policy_ko";
                break;
            case "ja":
                viewName = "privacy-policy_ja";
                break;
            case "es":
                viewName = "privacy-policy_es";
                break;
            case "fr":
                viewName = "privacy-policy_fr";
                break;
            case "de":
                viewName = "privacy-policy_de";
                break;
            case "pt":
                viewName = "privacy-policy_pt";
                break;
            case "hi":
                viewName = "privacy-policy_hi";
                break;
            case "id":
                viewName = "privacy-policy_id";
                break;
            case "th":
                viewName = "privacy-policy_th";
                break;
            case "it":
                viewName = "privacy-policy_it";
                break;
            case "en": // 기본 설정은 영어
            default:
                viewName = "privacy-policy_en";
                break;
        }

        return new ModelAndView(viewName);
    }
}
