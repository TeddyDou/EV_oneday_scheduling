package model;

public class Time {
    int hour, minute;

    // take string as input, example 00:00
    public Time(String str){
        String[] data = str.split(":");
        this.hour = Integer.parseInt(data[0]);
        this.minute = Integer.parseInt(data[1]);
    }

    //take integer as input, example 600 will be formatted as 6:00
    public Time(int minute) throws Exception{
        this.hour = 0;
        this.minute = minute;
        format();
    }

    //add duration to some time, to create a new time
    public Time(Time t, int duration) throws Exception{
        this.hour = t.getHour();
        this.minute = t.getMinute() + duration;
        format();
    }

    @Override
    public String toString() {
        return hour + ":" + minute;
    }

    //add some duration to 'THIS' Time instance
    public void plusDuration(int duration) throws Exception{
        this.minute += duration;
        format();
    }

    //minus some duration to 'THIS' Time instance
    public void minusDuration(int duration) throws Exception{
        this.minute -= duration;
        format();
    }

    //get the delta duration between THIS instance and parameter Time instance
    //return the number of minutes
    //someTime must be an earlier time than this instance
    public int getDuration(Time someTime){
        int deltaH = this.hour - someTime.hour;
        int deltaM = this.minute - someTime.minute;
        while (deltaM < 0){
            deltaH -= 1;
            deltaM += 60;
        }
        return deltaH * 60 + deltaM;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    //format the time, make 0 <= hour <= 24, 0 <= minute <= 60
    public void format(){
        while (minute >= 60){
            minute -= 60;
            hour +=1;
        }
        while (minute < 0){
            minute += 60;
            hour -= 1;
        }
    }

    public boolean isEarlierOrEqual(Time someTime) {
        if (this.hour < someTime.hour){
            return true;
        }
        else if(this.hour == someTime.hour){
            if (this.minute <= someTime.minute)
                return true;
            else
                return false;
        }
        return false;
    }

    public boolean isEarlier(Time someTime){
        if (this.hour < someTime.hour){
            return true;
        }
        else if(this.hour == someTime.hour){
            if (this.minute < someTime.minute)
                return true;
            else
                return false;
        }
        return false;
    }
}
