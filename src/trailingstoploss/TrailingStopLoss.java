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
        
        timer.
        
        if (newPrice < price)
        {
            seller.sell();
        }
        else
        {
            price = newPrice;
        }
    }

    public void timeUp()
    {
        
    }

}
