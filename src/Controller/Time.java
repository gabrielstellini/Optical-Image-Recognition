package Controller;

public class Time {
    private long startTime;
    private long endTime;

    private boolean timerStarted = false;

    public void startTimer(){
        if(!timerStarted){
            startTime = System.nanoTime();
            timerStarted = true;
        }else {
            System.err.println("Timer already started, timing mechanism is incorrect");
        }
    }

    public void endTimer(){
        endTime = System.nanoTime();
//        System.out.println("TOTAL ALGORITHM EXECUTION TIME:" + (endTime-startTime));
    }

    public long getTotalTime(){
        return endTime-startTime;
    }

    public double getTotalTimeInSeconds(){
        return (double)(endTime-startTime) / 1000000000;
    }
}
