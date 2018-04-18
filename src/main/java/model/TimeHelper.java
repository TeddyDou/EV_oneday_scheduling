package model;

public class TimeHelper {

    // convert time from String to int, expressed by the number of minutes from the beginning of day
    // for example: String "6:00" -->  int 6*60 = 360
    int stringTimeToInt(String strTime){
        String[] data = strTime.split(":");
        return Integer.parseInt(data[0]) * 60 + Integer.parseInt(data[1]);
    }

    String intTimeToString(int intTime){
        int hour = intTime/60;
        int minute = intTime%60;
        return hour + ":" + String.format("%02d", minute);
    }

    public static void main(String[] args) {
        TimeHelper t = new TimeHelper();
        System.out.println(t.stringTimeToInt("10:30"));
        System.out.println(t.intTimeToString(660));
    }
}
