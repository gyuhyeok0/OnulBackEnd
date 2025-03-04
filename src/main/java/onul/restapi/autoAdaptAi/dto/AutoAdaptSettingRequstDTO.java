package onul.restapi.autoAdaptAi.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AutoAdaptSettingRequstDTO {
    private String memberId;          // 회원 ID
    private String exerciseGoal;      // 운동 목표 (예: 근비대, 체지방 감소)
    private int exerciseSplit;        // 운동 분할 (예: 3분할)
    private List<String> priorityParts; // 제외할 부위 (예: ["어깨"])

    private String difficulty;        // 운동 난이도 (예: 초급, 중급, 고급)
    private String exerciseTime;      // 운동 시간 (예: 60분 이하)
    private List<String> exerciseStyle; // 운동 스타일 (예: ["머신", "프리웨이트"])
    private List<String> excludedParts; // 제외할 부위 (예: ["어깨"])
    private boolean includeCardio;    // 유산소 포함 여부
}
