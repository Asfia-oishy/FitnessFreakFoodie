package com.akapps.fitnessfreak;

public class CalorieClass {
    int id, day, month, year, type;
    float intake, burnt;

    public CalorieClass(int id, int day, int month, int year, int type, float intake, float burnt) {
        this.id = id;
        this.day = day;
        this.month = month;
        this.year = year;
        this.type = type;
        this.intake = intake;
        this.burnt = burnt;
    }


    public int getId() {
        return id;
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

    public int getType() {
        return type;
    }

    public float getIntake() {
        return intake;
    }

    public float getBurnt() {
        return burnt;
    }

    public void setIntake(float intake) {
        this.intake = intake;
    }

    public void setBurnt(float burnt) {
        this.burnt = burnt;
    }
}
