package trailingstoploss;

public interface MyTimer
{
    void addListener(TimerListener timerListener);

    void start(int duration);
    void timeUp();
}
