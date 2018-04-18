package model;

import static model.Model.*;

public class ChargingHelper {
    ///////////////////////////////////////////////////////////
    //          Level2      CHAdeMO     CCS     Supercharger //
    // Nissan   0.37        1.20        -       -            //
    // CHev     0.40        -           2.20    -            //
    // Tesla    0.42        1.42        -       2.67         //
    // Unit: mile/min                                        //
    ///////////////////////////////////////////////////////////
    double[][] chargeMap = {
            {0.37, 1.20, 0, 0},
            {0.40, 0, 2.20, 0},
            {0.42, 1.42, 2.67}
    };

    double rate;
    int priority;

    double getChargingRate(int evType, int chargerType){
        if (evType == NISSAN){
            if (chargerType == LEVEL2)
                return 0.27;
            else if (chargerType == CHADEMO)
                return 1.20;
            else
                return 0;
        }
        else if(evType == CHEV){
            if (chargerType == LEVEL2)
                return 0.40;
            else if (chargerType == CCS)
                return 2.20;
            else
                return 0;
        }
        else if (evType == TESLA){
            if (chargerType == LEVEL2)
                return 0.42;
            else if (chargerType == CHADEMO)
                return 1.42;
            else if (chargerType == SUPERCHARGER)
                return 2.67;
            else
                return 0;
        }
        else
            return 0;
    }

    double getRateByPriority(int evType, int priority){
        if (evType == NISSAN){
            if (priority == 2)
                return 1.20;
            else if (priority == 1)
                return 0.37;
            else
                return 0;
        }
        else if (evType == CHEV){
            if (priority == 3)
                return 2.2;
            else if (priority == 1)
                return 0.40;
            else
                return 0;
        }
        else if (evType == TESLA){
            if (priority == 3)
                return 2.67;
            else if (priority == 2)
                return 1.42;
            else
                return 0.42;
        }
        else
            return 0;
    }

    int getChargerByPriority(int evType, int priority) {
        if (priority ==1 ){
            return LEVEL2;
        }
        else if (priority == 2){
            if (evType == CHEV)
                return 0;
            else
                return CHADEMO;
        }
        else if (priority == 3){
            if (evType == NISSAN)
                return 0;
            else if (evType == CHEV)
                return CCS;
            else if (evType == TESLA)
                return SUPERCHARGER;
        }
        return 0;
    }
}
