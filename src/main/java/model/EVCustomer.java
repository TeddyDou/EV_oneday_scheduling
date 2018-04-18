package model;

import static model.Model.ALPHA;

public class EVCustomer {
    int evID, distance, typeID, numberOfCharger, assignedCharger, chargingTime, timeWindow;
    int startTime, endTime, assignedStartTime, assignedEndTime, preAssignStartTime, preAssignEndTime;
    double barWindowRatio, absoluteLength;
    boolean isAssigned;
    String typeName;

    /////////////////////////////////////////////////////////////////////////
    //      This formula combines barWindowRation and absoluteLength       //
    //      and weights them.                                              //
    //                                                                     //
    //      score = alpha * barWindowRatio + (1 - alpha) * absoluteLength  //
    //                                                                     //
    /////////////////////////////////////////////////////////////////////////
    double score;

    @Override
    public String toString() {
        return "EVCustomer{" +
                "evID=" + evID +
//                ", distance=" + distance +
                ", typeID=" + typeID +
//                ", assignedCharger=" + assignedCharger +
                ", chargingTime=" + chargingTime +
                ", timeWindow=" + timeWindow +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", assignedStartTime=" + assignedStartTime +
                ", assignedEndTime=" + assignedEndTime +
                ", barWindowRatio=" + barWindowRatio +
//                ", isAssigned=" + isAssigned + '\'' +
                '}' + "\n";
    }

    public EVCustomer(int evID, int distance, int typeID, int startTime, int endTime) {
        this.evID = evID;
        this.distance = distance;
        this.typeID = typeID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAssigned = false;
        this.timeWindow = endTime - startTime;
        this.initTypeName();
//        System.out.println(toString());
    }

    //dummy EVCustome constructor for max heap
    public EVCustomer(){
        this.barWindowRatio = 2;
        this.absoluteLength = 2;
    }

    public void initTypeName() {
        switch (this.typeID){
            case 1: setTypeName("Nissan");
            break;
            case 2: setTypeName("Chev");
            break;
            case 3: setTypeName("Tesla");
            break;
        }
    }

    public int getEvID() {
        return evID;
    }

    public void setEvID(int evID) {
        this.evID = evID;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public int getNumberOfCharger() {
        return numberOfCharger;
    }

    public void setNumberOfCharger(int numberOfCharger) {
        this.numberOfCharger = numberOfCharger;
    }

    public int getTimeWindow() {
        return timeWindow;
    }

    public int getAssignedCharger() {
        return assignedCharger;
    }

    public void setAssignedCharger(int assignedCharger) {
        this.assignedCharger = assignedCharger;
    }

    public int getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(int chargingTime) {
        this.chargingTime = chargingTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setAssignedStartTime(int assignedStartTime) {
        this.assignedStartTime = assignedStartTime;
    }

    public void setAssignedEndTime(int assignedEndTime) {
        this.assignedEndTime = assignedEndTime;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void updatPreAssignedTime(int preStartTime) {
        this.preAssignStartTime = preStartTime;
        this.preAssignEndTime = preStartTime + chargingTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getPreAssignStartTime() {
        return preAssignStartTime;
    }

    public int getPreAssignEndTime() {
        return preAssignEndTime;
    }

    public void updateAssignedTime() {

        this.assignedStartTime = preAssignStartTime;
        this.assignedEndTime = preAssignEndTime;
    }

    public int getAssignedEndTime() {
        return assignedEndTime;
    }

    public double getBarWindowRatio() {
        return barWindowRatio;
    }

    public void setBarWindowRatio(double barWindowRatio) {
        this.barWindowRatio = barWindowRatio;
    }

    public double getAbsoluteLength() {
        return absoluteLength;
    }

    public void setAbsoluteLength(double absoluteLength) {
        this.absoluteLength = absoluteLength;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setChargingTimeAndRatio(double rate) {
        this.setChargingTime((int)Math.ceil(this.distance/rate));
        double temp = (double) chargingTime / timeWindow;
        this.setBarWindowRatio(temp);
    }

    public void setRatioAndAbsAndScore(double rate) {
        this.setChargingTime((int)Math.ceil(this.distance/rate));
        double temp = (double) chargingTime / timeWindow;
//        double temp = (double) timeWindow / chargingTime;

        this.setBarWindowRatio(temp);
        temp = (double) chargingTime/ (24*60);
        this.setAbsoluteLength(temp);
        temp = ALPHA * barWindowRatio + (1 - ALPHA) * absoluteLength;
        this.setScore(temp);
    }

    public int getAssignedStartTime() {
        return this.assignedStartTime;
    }
}
