package com.akapps.fitnessfreak;

public class RoundFloat {

    public float getRound(float value)
    {
        value = Math.round(value * 100);
        value = value/100;
        return value;
    }
}
