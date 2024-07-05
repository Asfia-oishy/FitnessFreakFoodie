package com.akapps.fitnessfreak;

import java.util.Objects;

public class WeightClass {
    int day, month, year;
    float current_weight, goal_weight;

    public WeightClass(int day, int month, int year, float current_weight, float goal_weight) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.current_weight = current_weight;
        this.goal_weight = goal_weight;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public float getCurrent_weight() {
        return current_weight;
    }

    public float getGoal_weight() {
        return goal_weight;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeightClass that = (WeightClass) o;
        return day == that.day &&
                month == that.month &&
                year == that.year &&
                Float.compare(that.current_weight, current_weight) == 0 &&
                Float.compare(that.goal_weight, goal_weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year, current_weight, goal_weight);
    }
}
