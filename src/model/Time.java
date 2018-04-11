package src.model;

public class Time {
    int hour, minute;

    public Time(String str){
        String[] data = str.split(":");
        this.hour = Integer.parseInt(data[0]);
        this.minute = Integer.parseInt(data[1]);
    }

    @Override
    public String toString() {
        return "Time{" +
                "hour=" + hour +
                ", minute=" + minute +
                '}';
    }

    public void plusTime(Time someTime){
        this.minute += someTime.getMinute();
        if (minute >= 60)
            this.hour += 1;
        this.hour += someTime.getHour();
        if (this.hour > 24 && this.minute > 0)
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void minusTime(Time someTime){
        this.minute -= someTime.getMinute();
        if(minute < 0){
            this.hour -= 1;
            this.minute += 60;
        }
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

}
