package onul.restapi.exercise.controller;

import onul.restapi.exercise.dto.ScheduleDTO;
import onul.restapi.exercise.service.ScheduleService;
import onul.restapi.member.controller.StateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 운동 스케줄 등록 엔드포인트
    @PostMapping("/registSchedule")
    public ResponseEntity<?> registSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        if (scheduleDTO.getWeekType() == null || scheduleDTO.getDay() == null || scheduleDTO.getPart() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("FAILURE: 잘못된 요청입니다."));
        }

        try {
            scheduleService.saveOrUpdateSchedule(scheduleDTO);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("SUCCESS"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("FAILURE: " + e.getMessage()));
        }
    }

    // 운동 스케줄 삭제 엔드포인트
    @DeleteMapping("/deleteSchedule")
    public ResponseEntity<?> deleteSchedule(@RequestBody ScheduleDTO scheduleDTO) {

        System.out.println("여기"+scheduleDTO);
        if (scheduleDTO.getWeekType() == null || scheduleDTO.getDay() == null || scheduleDTO.getPart() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("FAILURE: 잘못된 요청입니다."));
        }

        try {
            scheduleService.deleteSchedule(scheduleDTO);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("SUCCESS"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("FAILURE: " + e.getMessage()));
        }
    }
}
