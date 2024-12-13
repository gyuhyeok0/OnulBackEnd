package onul.restapi.exercise.dto;

public class SetDTO {

    private Boolean completed; // 완료 여부
    private Double kg;         // 킬로그램 (소수점 2자리)
    private Double km;         // 킬로미터 (소수점 2자리)
    private Double lbs;        // 파운드 (소수점 2자리)
    private Double mi;         // 마일 (소수점 2자리)
    private Integer reps;      // 반복 횟수
    private String time;       // 시간 (문자열 형식, 예: "00:10")

    // 기본 생성자
    public SetDTO() {
    }

    // 모든 필드를 포함하는 생성자
    public SetDTO(Boolean completed, Double kg, Double km, Double lbs, Double mi, Integer reps, String time) {
        this.completed = completed;
        this.kg = kg;
        this.km = km;
        this.lbs = lbs;
        this.mi = mi;
        this.reps = reps;
        this.time = time;
    }

    // Getter와 Setter
    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Double getKg() {
        return kg;
    }

    public void setKg(Double kg) {
        this.kg = kg;
    }

    public Double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public Double getLbs() {
        return lbs;
    }

    public void setLbs(Double lbs) {
        this.lbs = lbs;
    }

    public Double getMi() {
        return mi;
    }

    public void setMi(Double mi) {
        this.mi = mi;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SetDto{" +
                "completed=" + completed +
                ", kg=" + kg +
                ", km=" + km +
                ", lbs=" + lbs +
                ", mi=" + mi +
                ", reps=" + reps +
                ", time='" + time + '\'' +
                '}';
    }
}
