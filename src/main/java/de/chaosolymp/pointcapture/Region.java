package de.chaosolymp.pointcapture;

import org.bukkit.Location;
import org.bukkit.Material;

public class Region {

    private String name;
    private String teamA;
    private String teamB;
    private Material teamAMat;
    private Material teamBMat;
    private int maxCaptureTime;
    private double multiplier;
    private int membersA;
    private int membersB;
    private Location[] towers;
    private double score;
    private int lockedScore;
    private Location aCapture;
    private Location bCapture;
    private Location neutralize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public Material getTeamAMat() {
        return teamAMat;
    }

    public void setTeamAMat(Material teamAMat) {
        this.teamAMat = teamAMat;
    }

    public Material getTeamBMat() {
        return teamBMat;
    }

    public void setTeamBMat(Material teamBMat) {
        this.teamBMat = teamBMat;
    }

    public int getMaxCaptureTime() {
        return maxCaptureTime;
    }

    public void setMaxCaptureTime(int maxCaptureTime) {
        this.maxCaptureTime = maxCaptureTime;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public int getMembersA() {
        return membersA;
    }

    public void setMembersA(int membersA) {
        this.membersA = membersA;
    }

    public int getMembersB() {
        return membersB;
    }

    public void setMembersB(int membersB) {
        this.membersB = membersB;
    }

    public void addMembersA(){
        membersA++;
    }

    public void addMembersB() {
        membersB++;
    }

    public Location[] getTowers() {
        return towers;
    }

    public void setTowers(Location[] towers) {
        this.towers = towers;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void addScore(int count){
        score += multiplier*count;
    }

    public void decreaseScore(int count){
        score -= multiplier*count;
    }

    public int getLockedScore() {
        return lockedScore;
    }

    public void setLockedScore(int lockedScore) {
        this.lockedScore = lockedScore;
    }

    public Location getaCapture() {
        return aCapture;
    }

    public void setaCapture(Location aCapture) {
        this.aCapture = aCapture;
    }

    public Location getbCapture() {
        return bCapture;
    }

    public void setbCapture(Location bCapture) {
        this.bCapture = bCapture;
    }

    public Location getNeutralize() {
        return neutralize;
    }

    public void setNeutralize(Location neutralize) {
        this.neutralize = neutralize;
    }
}
