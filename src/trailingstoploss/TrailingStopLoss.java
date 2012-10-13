package trailingstoploss;

public class TrailingStopLoss
{
    private int price;
    private final Seller seller;
    private final MyTimer timer;
    private int tempPrice;

    public TrailingStopLoss(int price, Seller seller, MyTimer timer)
    {
        this.price = price;
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

    public void timeUp()
    {
        if (priceDecreasing())
        {
            seller.sell();
        }
        else
        {
            price = tempPrice;
        }
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
