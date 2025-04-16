package onul.restapi.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebInquiryController {

    // /webinquiry 경로에서 webInquiry.html 페이지를 반환
    @GetMapping("/webinquiry")
    public String getInquiryPage() {
        return "webInquiry";  // /templates/webInquiry.html 파일을 반환
    }
}
