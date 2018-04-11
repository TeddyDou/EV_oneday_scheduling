package src.model;

public class EVCustomer {
    int evID, distance, evType, numberOfCharger, assignedCharger, assignedStartTime;
    Time startTime, endTime, chargingTime;
    float barWindowRatio, absoluteLength;
    boolean isAssigned;

    public EVCustomer(int evID, int distance, int evType, Time startTime, Time endTime) {
        this.evID = evID;
        this.distance = distance;
        this.evType = evType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAssigned = false;
    }

    @Override
    public String toString() {
        return "EVCustomer{" +
                "evID=" + evID +
                ", distance=" + distance +
                ", evType=" + evType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", chargingTime=" + chargingTime +
                ", isAssigned=" + isAssigned +
                '}';
    }
}
