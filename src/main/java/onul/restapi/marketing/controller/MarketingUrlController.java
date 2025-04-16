package onul.restapi.marketing.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class MarketingUrlController {

    @GetMapping(value = "/ads.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String serveAdsTxt() {
        return "google.com, pub-7000340961307617, DIRECT, f08c47fec0942fa0";
    }

    @GetMapping(value = "/app-ads.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String serveAppAdsTxt() {
        return "google.com, pub-7000340961307617, DIRECT, f08c47fec0942fa0";
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String serveMarketingPage() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/index.html");
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }


}
