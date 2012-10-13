package trailingstoploss;

public class MockSeller implements Seller
{
    private boolean sold;

    @Override
    public boolean sold()
    {
        return sold;
    }

    @Override
    public void sell()
    {
        sold = true;
    }
}
