package model;

public class Time {
    int hour, minute;

    public Time(String str){
        String[] data = str.split(":");
        this.hour = Integer.parseInt(data[0]);
        this.minute = Integer.parseInt(data[1]);
    }

    public Time(int minute) throws Exception{
        this.hour = 0;
        this.minute = minute;
        format();
    }

    public Time(Time t, int duration) throws Exception{
        this.hour = t.getHour();
        this.minute = t.getMinute() + duration;
        format();
    }

    @Override
    public String toString() {
        return "Time{" +
                "hour=" + hour +
                ", minute=" + minute +
                '}';
    }

    public void plusDuration(int duration) throws Exception{
        this.minute += duration;
        format();
    }

    public void minusDuration(int duration) throws Exception{
        this.minute -= duration;
        format();
    }

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

    public void format() throws Exception{
        while (minute >= 60){
            minute -= 60;
            hour +=1;
        }
        while (minute < 0){
            minute += 60;
            hour -= 1;
        }
        if ((this.hour < 0) || (this.hour > 24 && this.minute > 0)) {
//            try {
                throw new Exception();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
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
