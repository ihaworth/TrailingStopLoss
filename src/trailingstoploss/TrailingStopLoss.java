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
        timer.stop();
        
        if (newPrice > price)
        {
            waitForPriceIncreaseToStabilise(newPrice);
        }
        else if (newPrice < price)
        {
            waitForPriceDecreaseToStabilise();
        }
    }

    private void waitForPriceIncreaseToStabilise(int newPrice)
    {
        timer.addListener(new PriceIncreaseTimeout(newPrice));
        timer.start(15);
    }

    private void waitForPriceDecreaseToStabilise()
    {
        timer.addListener(new PriceDecreaseTimeout());
        timer.start(30);
    }

    private final class PriceDecreaseTimeout implements TimerListener
    {
        @Override
        public void timeUp()
        {
            seller.sell();
        }
    }

    private final class PriceIncreaseTimeout implements TimerListener
    {
        private int tempPrice;

        public PriceIncreaseTimeout(int newPrice)
        {
            this.tempPrice = newPrice;
        }

        @Override
        public void timeUp()
        {
            price = tempPrice;
        }
    }
}
