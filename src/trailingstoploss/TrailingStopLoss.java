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
        
        timer.start(15);
    }

    public void timeUp()
    {
        if (tempPrice < price)
        {
            seller.sell();
        }
        else
        {
            price = tempPrice;
        }
    }
}
