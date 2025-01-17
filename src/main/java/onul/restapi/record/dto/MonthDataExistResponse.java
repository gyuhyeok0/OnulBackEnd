package onul.restapi.record.dto;

import java.util.List;

public class MonthDataExistResponse {

    private List<DayDataExist> dayDataExists;

    public MonthDataExistResponse(List<DayDataExist> dayDataExists) {
        this.dayDataExists = dayDataExists;
    }

    public List<DayDataExist> getDayDataExists() {
        return dayDataExists;
    }

    public void setDayDataExists(List<DayDataExist> dayDataExists) {
        this.dayDataExists = dayDataExists;
    }

    public static class DayDataExist {
        private String date;
        private boolean exerciseDataExists;
        private boolean foodDataExists;
        private boolean bodyDataExists;

        public DayDataExist(String date, boolean exerciseDataExists, boolean foodDataExists, boolean bodyDataExists) {
            this.date = date;
            this.exerciseDataExists = exerciseDataExists;
            this.foodDataExists = foodDataExists;
            this.bodyDataExists = bodyDataExists;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public boolean isExerciseDataExists() {
            return exerciseDataExists;
        }

        public void setExerciseDataExists(boolean exerciseDataExists) {
            this.exerciseDataExists = exerciseDataExists;
        }

        public boolean isFoodDataExists() {
            return foodDataExists;
        }

        public void setFoodDataExists(boolean foodDataExists) {
            this.foodDataExists = foodDataExists;
        }

        public boolean isBodyDataExists() {
            return bodyDataExists;
        }

        public void setBodyDataExists(boolean bodyDataExists) {
            this.bodyDataExists = bodyDataExists;
        }
    }
}
