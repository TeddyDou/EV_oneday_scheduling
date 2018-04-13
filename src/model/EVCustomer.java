package model;

public class EVCustomer {
    int evID, distance, typeID, numberOfCharger, assignedCharger, chargingTime, timeWindow;
    Time startTime, endTime, assignedStartTime, assignedEndTime;
    double barWindowRatio, absoluteLength;
    boolean isAssigned;
    String typeName;

    @Override
    public String toString() {
        return "EVCustomer{" +
                "evID=" + evID +
//                ", distance=" + distance +
//                ", typeID=" + typeID +
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

    public EVCustomer(int evID, int distance, int typeID, Time startTime, Time endTime) {
        this.evID = evID;
        this.distance = distance;
        this.typeID = typeID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAssigned = false;
        this.timeWindow = endTime.getDuration(startTime);
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

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Time getAssignedStartTime() {
        return assignedStartTime;
    }

    public void updateAssignedTime(Time assignedStartTime) {
        this.assignedStartTime = assignedStartTime;
        try{
            this.assignedEndTime = new Time(assignedStartTime, chargingTime);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Time getAssignedEndTime() {
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

    public void undoAssignedTime() {
        this.assignedStartTime = null;
        this.assignedEndTime = null;
    }
}
