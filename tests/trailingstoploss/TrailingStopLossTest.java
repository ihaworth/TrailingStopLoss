package trailingstoploss;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TrailingStopLossTest
{
    private final class MockTimer implements MyTimer
    {
        private TrailingStopLoss trailingStopLoss;
        private int duration;
        
        @Override
        public void addListener(TrailingStopLoss trailingStopLoss)
        {
            this.trailingStopLoss = trailingStopLoss;
        }

        @Override
        public void timeUp()
        {
            trailingStopLoss.timeUp();
        }

        @Override
        public void start(int duration)
        {
            this.duration = duration;
        }
    }

    private Seller seller;
    private TrailingStopLoss trailingStopLoss;
    private MyTimer timer;

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
        timer.timeUp();
        assertFalse(seller.sold());
    }

    @Test
    public void stockSoldWhenPriceDecreasesAndStaysDown()
    {
        trailingStopLoss.priceChanged(9);
        timer.timeUp();
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
        timer.timeUp();
        assertFalse(seller.sold());
    }

    @Test
    public void stockSoldWhenPriceDecreasesAfterIncrease()
    {
        trailingStopLoss.priceChanged(11);
        timer.timeUp();
        trailingStopLoss.priceChanged(10);
        timer.timeUp();
        assertTrue(seller.sold());
    }

    @Test
    public void stockSoldWhenPriceDecreasesAfterTemporaryIncrease()
    {
        trailingStopLoss.priceChanged(11);
        // Don't wait for price to become stable
        trailingStopLoss.priceChanged(10);
        timer.timeUp();

        trailingStopLoss.priceChanged(9);
        timer.timeUp();
        
        assertTrue(seller.sold());
    }
}
