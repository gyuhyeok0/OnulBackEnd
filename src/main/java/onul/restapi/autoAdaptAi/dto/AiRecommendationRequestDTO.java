package onul.restapi.autoAdaptAi.dto;

import onul.restapi.analysis.dto.ExerciseVolumeResponse;
import onul.restapi.analysis.dto.MuscleFatigueDTO;
import onul.restapi.exercise.entity.AiExerciseRecordDTO;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AiRecommendationRequestDTO {

    private AutoAdaptSettingDTO exerciseSetting; // 운동 세팅 (무조건 있음)
    private ExerciseVolumeResponse recentVolumeByMuscleGroup; // 최근 운동량 변화 (없을 수도 있음)
    private Map<String, List<MuscleFatigueDTO>> todayExerciseFatigue; // 오늘 운동 피로도 (없을 수도 있음)
    private Map<LocalDate, List<AiExerciseRecordDTO>> recentExercisesRecord; // 최근 운동 기록 (없을 수도 있음)

    public AiRecommendationRequestDTO(
            AutoAdaptSettingDTO exerciseSetting,
            ExerciseVolumeResponse recentVolumeByMuscleGroup,
            Map<String, List<MuscleFatigueDTO>> todayExerciseFatigue,
            Map<LocalDate, List<AiExerciseRecordDTO>> recentExercisesRecord
    ) {
        this.exerciseSetting = exerciseSetting;
        this.recentVolumeByMuscleGroup = recentVolumeByMuscleGroup;
        this.todayExerciseFatigue = (todayExerciseFatigue != null) ? todayExerciseFatigue : Collections.emptyMap();
        this.recentExercisesRecord = (recentExercisesRecord != null) ? recentExercisesRecord : Collections.emptyMap();
    }

    public AutoAdaptSettingDTO getExerciseSetting() {
        return exerciseSetting;
    }

    public ExerciseVolumeResponse getRecentVolumeByMuscleGroup() {
        return recentVolumeByMuscleGroup;
    }

    public Map<String, List<MuscleFatigueDTO>> getTodayExerciseFatigue() {
        return todayExerciseFatigue;
    }

    public Map<LocalDate, List<AiExerciseRecordDTO>> getRecentExercisesRecord() {
        return recentExercisesRecord;
    }

    @Override
    public String toString() {
        return "AiRecommendationRequestDTO{" +
                "exerciseSetting=" + exerciseSetting +
                ", recentVolumeByMuscleGroup=" + recentVolumeByMuscleGroup +
                ", todayExerciseFatigue=" + todayExerciseFatigue +
                ", recentExercisesRecord=" + recentExercisesRecord +
                '}';
    }
}
