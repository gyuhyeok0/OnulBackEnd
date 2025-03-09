package onul.restapi.record.controller;

import onul.restapi.record.dto.MonthDataExistResponse;
import onul.restapi.record.dto.MonthDataRequest;
import onul.restapi.record.service.RecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/record")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }


    @PostMapping(value = "/isMonthDataExist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonthDataExistResponse> isMonthDataExist(@RequestBody MonthDataRequest request) {
        try {
            // 해당 월의 모든 날짜에 대해 데이터 존재 여부 체크
            MonthDataExistResponse response = recordService.checkMonthDataExist(request.getMemberId(), request.getMountMonth());

//            return null;
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // 예외 처리
            return new ResponseEntity<>(new MonthDataExistResponse(null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
