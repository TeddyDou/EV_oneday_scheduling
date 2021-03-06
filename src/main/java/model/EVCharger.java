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


    public int getID() {
        return ID;
    }

    public int getChargerType() {
        return chargerType;
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
//    public Time getNaiveEarliestTime(Time startTime) {
//        Time earliestTime = startTime;
//        for(EVCustomer ev : assignedList){
//            if(!ev.getAssignedEndTime().isEarlier(earliestTime) && ev.getAssignedStartTime().isEarlier(earliestTime))
//                earliestTime = ev.getAssignedEndTime();
//        }
//        return earliestTime;
//    }
//
//    public boolean positioning(EVCustomer newEV, Time startTime) {
//        Time earliestTime = startTime;
//        //find the last EV assigned before start time
//        int lastAssignedIndex = -1;
//        for (EVCustomer ev : assignedList) {
//            if (!ev.getAssignedEndTime().isEarlier(earliestTime) && ev.getAssignedStartTime().isEarlierOrEqual(earliestTime)
//                     ) {
//                earliestTime = ev.getAssignedEndTime();
//                lastAssignedIndex = assignedList.indexOf(ev);
//                break;
//            }
//        }
//        newEV.updateAssignedTime(earliestTime);
//
//        //before changed
//        if (!newEV.getEndTime().isEarlier(newEV.getAssignedEndTime()) &&
//            calibrateNextEV(lastAssignedIndex + 1, newEV.getAssignedEndTime())){
//            this.assignEV(newEV, lastAssignedIndex + 1);
//            return true;
//        } else {
//            newEV.undoAssignedTime();
//            return false;
//
//        }
//
//    }

    public boolean positioning(EVCustomer newEV){
        int earliestTime = newEV.getStartTime();
        int lastAssignedIndex = -1;
        for (EVCustomer ev : assignedList) {
            if (ev.getAssignedEndTime() < newEV.getEndTime() && ev.getAssignedEndTime() >= newEV.getStartTime()) {
                earliestTime = ev.getAssignedEndTime();
                lastAssignedIndex = assignedList.indexOf(ev);
                break;
            }
        }
        newEV.updatPreAssignedTime(earliestTime);

        while(newEV.getEndTime() >= newEV.getPreAssignEndTime() && newEV.getPreAssignStartTime() >= newEV.getStartTime()){
            if(calibrateNextEV(lastAssignedIndex + 1, newEV.getPreAssignEndTime())){
                this.assignEV(newEV, lastAssignedIndex + 1);
                return true;
            }
            else{
                lastAssignedIndex++;
                EVCustomer tempEV = assignedList.get(lastAssignedIndex);
                if (tempEV.getAssignedEndTime() < newEV.getEndTime() && tempEV.getAssignedEndTime() >= newEV.getStartTime()){
                    newEV.updatPreAssignedTime(tempEV.getAssignedEndTime());
                }
                else
                    return false;
            }
        }

        //new change
//        while (!newEV.getEndTime().isEarlier(newEV.getAssignedEndTime()) && newEV.getStartTime().isEarlierOrEqual
//                (newEV.getAssignedStartTime())){
//            if(!calibrateNextEV(lastAssignedIndex + 1, newEV.getAssignedEndTime())){
//                lastAssignedIndex ++;
//                EVCustomer tempEV = assignedList.get(lastAssignedIndex);
//                if (!tempEV.getAssignedEndTime().isEarlier(earliestTime) && tempEV.getAssignedStartTime().isEarlierOrEqual(earliestTime))
//                    newEV.updateAssignedTime(assignedList.get(lastAssignedIndex).getAssignedEndTime());
//                else
//                    return false;
//            }
//            else {
//                this.assignEV(newEV, lastAssignedIndex + 1);
//                return true;
//            }
//        }

//        newEV.undoAssignedTime();
        return false;
    }

    private void assignEV(EVCustomer newEV, int i) {
        newEV.setAssigned(true);
        newEV.setAssignedCharger(this.ID);
        newEV.updateAssignedTime();
        this.assignedList.add(i, newEV);
        this.availableDuration -= newEV.getChargingTime();
    }

//    private boolean calibrateNextEV(int evIndex, Time exceptedShiftTime) {
//
//        if (exceptedShiftTime.isEarlierOrEqual(Model.MIDNIGHT)) {
//            if (evIndex == assignedList.size()) {
//                return true;
//            }
//
//            EVCustomer exceptedShiftEV = assignedList.get(evIndex);
//
//            if (exceptedShiftEV.getAssignedStartTime().isEarlier(exceptedShiftTime)) {
//                try {
//                    Time shiftedTime = new Time(exceptedShiftTime, exceptedShiftEV.getChargingTime());
//                    if (shiftedTime.isEarlierOrEqual(exceptedShiftEV.getEndTime())) {
//                        if (calibrateNextEV(evIndex + 1, shiftedTime)) {
//                            exceptedShiftEV.updateAssignedTime(exceptedShiftTime);
//                            return true;
//                        }
//                        else
//                            return false;
//                    } else
//                        return false;
//                } catch (Exception e) {
//                    return false;
//                }
//
//            }
//            else
//                return true;
//            }
//        else
//            return false;
//
//    }

    private boolean calibrateNextEV(int evIndex, int exceptedShiftTime) {

        if (exceptedShiftTime <= Model.MIDNIGHT) {
            if (evIndex == assignedList.size()) {
                return true;
            }

            EVCustomer exceptedShiftEV = assignedList.get(evIndex);

            if (exceptedShiftEV.getAssignedStartTime()< exceptedShiftTime) {
                int shiftedTime = exceptedShiftTime + exceptedShiftEV.getChargingTime();
                if (shiftedTime <= exceptedShiftEV.getEndTime()){
                    if (calibrateNextEV(evIndex + 1, shiftedTime)){
                        exceptedShiftEV.updatPreAssignedTime(exceptedShiftTime);
                        exceptedShiftEV.updateAssignedTime();
                        return true;
                    }
                    else
                        return false;
                }
                else
                    return false;
            }
            else
                return true;
        }
        else
            return false;

    }
}
