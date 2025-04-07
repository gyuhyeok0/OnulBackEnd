package onul.restapi.autoAdaptAi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import onul.restapi.autoAdaptAi.dto.AutoAdaptDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AiRecommendationService {

    private final AutoAdaptService autoAdaptService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)  // 🚀 HTTP/1.1 강제 설정
            .build();

    public AiRecommendationService(AutoAdaptService autoAdaptService) {
        this.autoAdaptService = autoAdaptService;
    }

    @Value("${PYTHON_SERVER}")
    private String pythonServerHost;

    @Async("aiExecutor")
    public CompletableFuture<ResponseEntity<?>> sendAiRequest(String jsonPayload, LocalDate date, String memberId) {
        try {

            // ✅ Python 서버로 HTTP POST 요청
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://" + pythonServerHost + ":8000/aiRequest"))  // 환경 변수로 설정한 Python 서버 주소
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // ✅ 응답 받기
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body();

            // ✅ JSON 배열을 List<Long>로 변환
            List<Long> exerciseList = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

            // ✅ AutoAdaptDTO 객체 생성 및 저장
            AutoAdaptDTO autoAdaptDTO = new AutoAdaptDTO(exerciseList, date, memberId);
            autoAdaptService.saveOrUpdateAutoAdapt(autoAdaptDTO);


            return CompletableFuture.completedFuture(ResponseEntity.ok().build());

        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }

}
