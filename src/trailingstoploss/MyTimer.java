package trailingstoploss;

public interface MyTimer
{

    void timeUp();

    void addListener(TrailingStopLoss trailingStopLoss);

}
