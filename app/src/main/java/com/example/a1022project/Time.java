package com.example.a1022project;

public class Time {
    int startTime;
    int endTime;
    int timeSpent;

    public Time(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Time() {
        this(0, 0);
    }

    public void startTimer() {
        startTime = (int) System.currentTimeMillis();
        startTime = Math.abs(startTime) / 1000;
    }

    public String getStartTime() {
        return String.valueOf(startTime);
    }

    public String getEndTime() {
        return String.valueOf(endTime);
    }

    public String getTimeSpent() {
        ;
        return String.valueOf(timeSpent);
    }

    public String getTimer() {
        endTime = (int) System.currentTimeMillis();
        endTime = Math.abs(endTime) / 1000;
        timeSpent = Math.abs(endTime - startTime);
        int seconds = ((timeSpent % 86400) % 3600) % 60;
        int minutes = ((timeSpent % 86400) % 3600) / 60;
        int hours = ((timeSpent % 86400) / 3600);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        //return String.format("%d.%02d", hours, minutes);
    }
}






//add at the start of program to reset the time
//will make times retrieved afters have correct time
//timeAA.startTimer();
//String empty = timeAA.getTimer();
/////////




