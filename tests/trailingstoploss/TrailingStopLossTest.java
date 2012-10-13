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

        @Override
        public void timeUp()
        {
            trailingStopLoss.timeUp();
        }

        @Override
        public void addListener(TrailingStopLoss trailingStopLoss)
        {
            this.trailingStopLoss = trailingStopLoss;
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
    public void stockNotSoldWhenPriceIncreases()
    {
        trailingStopLoss.priceChanged(11);
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
        assertFalse(seller.sold());
    }
    
    @Test
    public void stockSoldWhenPriceDecreasesAfterIncrease()
    {
        trailingStopLoss.priceChanged(11);
        trailingStopLoss.priceChanged(10);
        assertTrue(seller.sold());
    }
    
    
}
