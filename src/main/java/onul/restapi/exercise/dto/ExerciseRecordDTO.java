package onul.restapi.exercise.dto;

import java.time.LocalDate;

public class ExerciseRecordDTO {

    private Long id;
    private String memberId;  // Member 엔티티와 연결될 memberId
    private int setNumber; // 세트 번호
    private SetDTO set;
    private ExerciseDto exercise;

    private int exerciseService; // 운동 서비스 (ID로 변경)
    private int exerciseType;    // 운동 타입 (ID로 변경)

    private String volume;
    private String weightUnit;
    private String kmUnit;
    private LocalDate recordDate;

    private Double kmVolume; // km 단위의 거리
    private Double miVolume; // 마일 단위의 거리
    private Double kgVolume; // kg 단위의 무게
    private Double lbsVolume; // lbs 단위의 무게
    private String timeVolume; // TIME 타입의 시간
    private int repsVolume; // REPETITION 타입의 반복 횟수




    public ExerciseRecordDTO() {
    }

    public ExerciseRecordDTO(Long id, String memberId, int setNumber, SetDTO set, ExerciseDto exercise, int exerciseService, int exerciseType, String volume, String weightUnit, String kmUnit, LocalDate recordDate, Double kmVolume, Double miVolume, Double kgVolume, Double lbsVolume, String timeVolume, int repsVolume) {
        this.id = id;
        this.memberId = memberId;
        this.setNumber = setNumber;
        this.set = set;
        this.exercise = exercise;
        this.exerciseService = exerciseService;
        this.exerciseType = exerciseType;
        this.volume = volume;
        this.weightUnit = weightUnit;
        this.kmUnit = kmUnit;
        this.recordDate = recordDate;
        this.kmVolume = kmVolume;
        this.miVolume = miVolume;
        this.kgVolume = kgVolume;
        this.lbsVolume = lbsVolume;
        this.timeVolume = timeVolume;
        this.repsVolume = repsVolume;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public SetDTO getSet() {
        return set;
    }

    public void setSet(SetDTO set) {
        this.set = set;
    }

    public ExerciseDto getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseDto exercise) {
        this.exercise = exercise;
    }

    public int getExerciseService() {
        return exerciseService;
    }

    public void setExerciseService(int exerciseService) {
        this.exerciseService = exerciseService;
    }

    public int getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(int exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getKmUnit() {
        return kmUnit;
    }

    public void setKmUnit(String kmUnit) {
        this.kmUnit = kmUnit;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public Double getKmVolume() {
        return kmVolume;
    }

    public void setKmVolume(Double kmVolume) {
        this.kmVolume = kmVolume;
    }

    public Double getMiVolume() {
        return miVolume;
    }

    public void setMiVolume(Double miVolume) {
        this.miVolume = miVolume;
    }

    public Double getKgVolume() {
        return kgVolume;
    }

    public void setKgVolume(Double kgVolume) {
        this.kgVolume = kgVolume;
    }

    public Double getLbsVolume() {
        return lbsVolume;
    }

    public void setLbsVolume(Double lbsVolume) {
        this.lbsVolume = lbsVolume;
    }

    public String getTimeVolume() {
        return timeVolume;
    }

    public void setTimeVolume(String timeVolume) {
        this.timeVolume = timeVolume;
    }

    public int getRepsVolume() {
        return repsVolume;
    }

    public void setRepsVolume(int repsVolume) {
        this.repsVolume = repsVolume;
    }

    @Override
    public String toString() {
        return "ExerciseRecordDTO{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", setNumber=" + setNumber +
                ", set=" + set +
                ", exercise=" + exercise +
                ", exerciseService=" + exerciseService +
                ", exerciseType=" + exerciseType +
                ", volume='" + volume + '\'' +
                ", weightUnit='" + weightUnit + '\'' +
                ", kmUnit='" + kmUnit + '\'' +
                ", recordDate=" + recordDate +
                ", kmVolume=" + kmVolume +
                ", miVolume=" + miVolume +
                ", kgVolume=" + kgVolume +
                ", lbsVolume=" + lbsVolume +
                ", timeVolume='" + timeVolume + '\'' +
                ", repsVolume=" + repsVolume +
                '}';
    }
}
