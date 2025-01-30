package onul.restapi.analysis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class ExerciseVolumeDataResponse {

    private List<Map<String, Object>> weeklyVolume;  // 주간 운동량 데이터
    private List<Map<String, Object>> monthlyVolume;  // 월간 운동량 데이터

}
