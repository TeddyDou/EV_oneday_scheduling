package model;

import java.util.ArrayList;
import java.util.List;

public class EVCharger {
    int ID, chargerType;
    List<EVCustomer> assignedList;
    int availableDuration;

    public EVCharger(int ID, int chargerType) {
        this.ID = ID;
        this.chargerType = chargerType;
        this.assignedList = new ArrayList();
        this.availableDuration = 24 * 60;
//        System.out.println(toString());
    }

    public List<EVCustomer> getAssignedList() {
        return assignedList;
    }

    public void setAssignedList(List<EVCustomer> assignedList) {
        this.assignedList = assignedList;
    }

    public int getAvailableDuration() {
        return availableDuration;
    }

    public void setAvailableDuration(int availableDuration) {
        this.availableDuration = availableDuration;
    }

    @Override
    public String toString() {
        return "EVCharger{" +
                "ID=" + ID +
                ", chargerType=" + chargerType + "\n" +
                ", assignedList=" + "\n" + assignedList +
                '}';
    }

    // return the earliest available charging time regardless the capacity of time slot
    public Time getNaiveEarliestTime(Time startTime) {
        Time earliestTime = startTime;
        for(EVCustomer ev : assignedList){
            if(!ev.getAssignedEndTime().isEarlier(earliestTime) && ev.getAssignedStartTime().isEarlier(earliestTime))
                earliestTime = ev.getAssignedEndTime();
        }
        return earliestTime;
    }

    public boolean positioning(EVCustomer newEV, Time startTime) {
        Time earliestTime = startTime;
        //find the last EV assigned before start time
        int lastAssignedIndex = -1;
        for (EVCustomer ev : assignedList) {
            if (!ev.getAssignedEndTime().isEarlier(earliestTime) && ev.getAssignedStartTime().isEarlierOrEqual(earliestTime)
                     ) {
                earliestTime = ev.getAssignedEndTime();
                lastAssignedIndex = assignedList.indexOf(ev);
                break;
            }
        }
        newEV.updateAssignedTime(earliestTime);
        //changed
        if (!newEV.getEndTime().isEarlier(newEV.getAssignedEndTime()) &&
            calibrateNextEV(lastAssignedIndex + 1, newEV.getAssignedEndTime())){
            this.assignEV(newEV, lastAssignedIndex + 1);
            return true;
        } else {
            newEV.undoAssignedTime();
            return false;

        }

        //calibration of following EV customers
//        if (calibrateNextEV(lastAssignedIndex + 1, newEV.getAssignedEndTime())) {
//            this.assignEV(newEV, lastAssignedIndex + 1);
//            return true;
//        } else {
//            newEV.undoAssignedTime();
//            return false;
//        }
    }

    private void assignEV(EVCustomer newEV, int i) {
        newEV.setAssigned(true);
        newEV.setAssignedCharger(this.ID);
        this.assignedList.add(i, newEV);
        this.availableDuration -= newEV.timeWindow;
    }

    private void assignEV(EVCustomer newEV) {
        newEV.setAssigned(true);
        newEV.setAssignedCharger(this.ID);
        this.assignedList.add(newEV);
        this.availableDuration -= newEV.timeWindow;
    }

    private boolean calibrateNextEV(int evIndex, Time exceptedShiftTime) {

        if(evIndex == assignedList.size() && exceptedShiftTime.isEarlierOrEqual(Model.MIDNIGHT)){
            return true;
        }

        EVCustomer exceptedShiftEV = assignedList.get(evIndex);

        if (exceptedShiftEV.getAssignedStartTime().isEarlier(exceptedShiftTime)) {
        try{
            Time shiftedTime = new Time(exceptedShiftTime, exceptedShiftEV.getChargingTime());
            if (shiftedTime.isEarlierOrEqual(exceptedShiftEV.getEndTime())){
                if(calibrateNextEV(evIndex +1, shiftedTime))
                    exceptedShiftEV.updateAssignedTime(shiftedTime);
                else
                    return false;
            }
            else
                return false;
        }catch (Exception e){
            return false;
        }

        }

        return true;

    }
}
