package com.example.tailorar;

import java.io.Serializable;

public class Mesurment implements Serializable {

    int mesurmentId;
    double height;
    double chest;
    double waist;
    double leg;
    double hip;
    double shoulder;

    public int getMesurmentId() {
        return mesurmentId;
    }

    public void setMesurmentId(int mesurmentId) {
        this.mesurmentId = mesurmentId;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getChest() {
        return chest;
    }

    public void setChest(double chest) {
        this.chest = chest;
    }

    public double getWaist() {
        return waist;
    }

    public void setWaist(double waist) {
        this.waist = waist;
    }

    public double getLeg() {
        return leg;
    }

    public void setLeg(double leg) {
        this.leg = leg;
    }

    public double getHip() {
        return hip;
    }

    public void setHip(double hip) {
        this.hip = hip;
    }

    public double getShoulder() {
        return shoulder;
    }

    public void setShoulder(double shoulder) {
        this.shoulder = shoulder;
    }
}


