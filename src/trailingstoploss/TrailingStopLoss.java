package trailingstoploss;

public class TrailingStopLoss implements TimerListener
{
    private final Seller seller;
    private final MyTimer timer;

    private int price;
    private int tempPrice;

    public TrailingStopLoss(int initialPrice, Seller seller, MyTimer timer)
    {
        this.price = initialPrice;
        this.seller = seller;
        this.timer = timer;
        
        timer.addListener(this);
    }

    public void priceChanged(int newPrice)
    {
        this.tempPrice = newPrice;
        
        if (priceIncreasing())
            timer.start(15);
        else if (priceDecreasing())
            timer.start(30);
    }

    /* (non-Javadoc)
     * @see trailingstoploss.TimerListener#timeUp()
     */
    @Override
    public void timeUp()
    {
        if (priceDecreasing())
            seller.sell();
        else
            price = tempPrice;
    }

    private boolean priceDecreasing()
    {
        return tempPrice < price;
    }

    private boolean priceIncreasing()
    {
        return tempPrice > price;
    }
}
