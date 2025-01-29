package onul.restapi.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseVolumeResponse {
    private List<String> dates;  // 날짜 리스트
    private Map<String, List<Double>> records;  // detailMuscleGroup 별 날짜별 운동량
}
