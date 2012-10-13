package trailingstoploss;

public class TrailingStopLoss
{
    private final Seller seller;
    private final MyTimer timer;

    private int price;

    public TrailingStopLoss(int initialPrice, Seller seller, MyTimer timer)
    {
        this.price = initialPrice;
        this.seller = seller;
        this.timer = timer;
    }

    public void priceChanged(int newPrice)
    {
        timer.addListener(new TimeoutListener(newPrice));
        
        if (newPrice > price)
            timer.start(15);
        else if (newPrice < price)
            timer.start(30);
    }

    private final class TimeoutListener implements TimerListener
    {
        private int tempPrice;

        public TimeoutListener(int newPrice)
        {
            this.tempPrice = newPrice;
        }

        @Override
        public void timeUp()
        {
            setNewPrice(tempPrice);
        }
    }

    private void setNewPrice(int newPrice)
    {
        if (newPrice < price)
            seller.sell();
        else
            price = newPrice;
    }
}
