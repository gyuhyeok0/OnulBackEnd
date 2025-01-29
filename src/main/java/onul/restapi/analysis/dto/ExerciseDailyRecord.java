package onul.restapi.analysis.dto;

public class ExerciseDailyRecord {

    private String date;   // 날짜 필드 추가
    private String detail;
    private double volume;

    public ExerciseDailyRecord() {
    }

    public ExerciseDailyRecord(double volume, String detail, String date) {
        this.volume = volume;
        this.detail = detail;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "ExerciseDailyRecord{" +
                "date='" + date + '\'' +
                ", detail='" + detail + '\'' +
                ", volume=" + volume +
                '}';
    }
}
