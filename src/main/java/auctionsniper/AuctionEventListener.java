package auctionsniper;

public interface AuctionEventListener {
    void auctionClosed();

    void currentPrice(int bidPrice, int incrementBy);
}
