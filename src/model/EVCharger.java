package src.model;

import java.util.ArrayList;
import java.util.List;

public class EVCharger {
    int ID, chargerType;
    List<EVCustomer> assignedList;

    public EVCharger(int ID, int chargerType) {
        this.ID = ID;
        this.chargerType = chargerType;
        this.assignedList = new ArrayList();
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "EVCharger{" +
                "ID=" + ID +
                ", chargerType=" + chargerType +
                ", assignedList=" + assignedList +
                '}';
    }
}
