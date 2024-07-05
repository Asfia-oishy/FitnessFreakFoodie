package com.akapps.fitnessfreak;

public class Mainprofile {
    String name, email, phonenumber;

    public Mainprofile() {}

    public Mainprofile(String name, String email, String phonenumber) {
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

}
