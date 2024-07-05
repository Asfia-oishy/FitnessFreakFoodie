package com.akapps.fitnessfreak;

public class DetailsProfile {
    int age, target_type, target_index, activity_index;
    String gender;
    float height, weight, goal_weight;

    public DetailsProfile() {}


    public DetailsProfile(int age, int target_type, int target_index, int activity_index, String gender, float height, float weight, float goal_weight) {
        this.age = age;
        this.target_type = target_type;
        this.target_index = target_index;
        this.activity_index = activity_index;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.goal_weight = goal_weight;
    }


    public int getAge() {
        return age;
    }

    public int getTarget_type() {
        return target_type;
    }

    public int getTarget_index() {
        return target_index;
    }

    public int getActivity_index() {
        return activity_index;
    }

    public String getGender() {
        return gender;
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public float getGoal_weight() {
        return goal_weight;
    }
}
