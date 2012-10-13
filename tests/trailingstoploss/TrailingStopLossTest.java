package trailingstoploss;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TrailingStopLossTest
{
    final class MockTimer implements MyTimer
    {
        private TimerListener timerListener;
        private int duration;
        
        @Override
        public void addListener(TimerListener timerListener)
        {
            this.timerListener = timerListener;
        }

        @Override
        public void timeUp()
        {
            timerListener.timeUp();
        }

        @Override
        public void start(int duration)
        {
            this.duration = duration;
        }

        void fifteenSecondsElapsed()
        {
            if (duration == 15)
                timeUp();
        }

        void thirtySecondsElapsed()
        {
            if (duration == 30)
                timeUp();
        }
    }

    private Seller seller;
    private TrailingStopLoss trailingStopLoss;
    private MockTimer timer;

    @Before
    public void setUp()
    {
        seller = new MockSeller();
        timer = new MockTimer();

        trailingStopLoss = new TrailingStopLoss(10, seller, timer);
    }
    
    @Test
    public void stockNotSoldWhenPriceUnchanged()
    {
        // No price change
        assertFalse(seller.sold());
    }
    
    @Test
    public void stockNotSoldWhenPriceIncreasesAndStaysUp()
    {
        trailingStopLoss.priceChanged(11);
        timer.fifteenSecondsElapsed();
        assertFalse(seller.sold());
    }

    @Test
    public void stockSoldWhenPriceDecreasesAndStaysDown()
    {
        trailingStopLoss.priceChanged(9);
        timer.thirtySecondsElapsed();
        assertTrue(seller.sold());
    }

    @Test
    public void stockNotSoldWhenPriceDecreasesTemporarily()
    {
        trailingStopLoss.priceChanged(9);
        // Don't wait for price to become stable
        assertFalse(seller.sold());
    }

    @Test
    public void stockNotSoldWhenPriceIncreasesTemporarily()
    {
        trailingStopLoss.priceChanged(11);
        // Don't wait for price to become stable
        trailingStopLoss.priceChanged(10);
        timer.thirtySecondsElapsed();
        assertFalse(seller.sold());
    }

    @Test
    public void stockSoldWhenPriceDecreasesAfterIncrease()
    {
        trailingStopLoss.priceChanged(11);
        timer.fifteenSecondsElapsed();
        trailingStopLoss.priceChanged(10);
        timer.thirtySecondsElapsed();
        assertTrue(seller.sold());
    }

    @Test
    public void stockSoldWhenPriceDecreasesAfterTemporaryIncrease()
    {
        trailingStopLoss.priceChanged(11);
        // Don't wait for price to become stable
        trailingStopLoss.priceChanged(10);
        timer.thirtySecondsElapsed();

        trailingStopLoss.priceChanged(9);
        timer.thirtySecondsElapsed();
        
        assertTrue(seller.sold());
    }
}
