package trailingstoploss;

public interface MyTimer
{
    void addListener(TrailingStopLoss trailingStopLoss);

    void start(int duration);
    void timeUp();
}
