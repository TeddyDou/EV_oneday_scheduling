# EV_oneday_scheduling

EV(Electric Vehicle) charging network scheduling system.
The software product is a network system of EV charging scheduling.
It will assign as many as EV users for charging with a giving number of charging stations with different types in a 24-hour time slot.

There are 4 types of EV chargers and 3 types of EV, and the compatibility is shown below.

        ///////////////////////////////////////////////////////////
        //          Level2      CHAdeMO     CCS     Supercharger //
        // Nissan   0.37        1.20        -       -            //
        // CHev     0.40        -           2.20    -            //
        // Tesla    0.42        1.42        -       2.67         //
        // Unit: mile/min                                        //
        ///////////////////////////////////////////////////////////

Input: Excel spreadsheet,
       including: user requests(user ID, EV type, preferred charging time, distance for charging)
                  chargers (charger ID, charger type)
Output: Gantt Chart and Excel spreadsheet.
