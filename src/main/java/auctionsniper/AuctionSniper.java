package auctionsniper;


public class AuctionSniper implements AuctionEventListener {
    private SniperListener sniperListener;
    private final Auction auction;

    private SniperSnapshot snapshot;

    public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener)  {
        this.sniperListener = sniperListener;
        this.auction = auction;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    public AuctionSniper(String itemId, Auction auction) {
        this.auction = auction;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }


    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch(priceSource) {
            case FromSniper:
                snapshot = snapshot.winning(price);
                break;
            case FromOtherBidder:
                int bid = price + increment;
                auction.bid(bid);
                snapshot = snapshot.bidding(price, bid);
                break;
        }
        notifyChange();
    }

    private void notifyChange() {
        sniperListener.sniperStateChanged(snapshot);
    }

    public SniperSnapshot getSnapshot() {
        return snapshot;
    }

    public void addSniperListener(SniperListener swingThreadSniperListener) {
        this.sniperListener = swingThreadSniperListener;
    }

    @Override
    public String toString() {
        return "AuctionSniper{" +
                "sniperListener=" + sniperListener +
                ", auction=" + auction +
                ", snapshot=" + snapshot +
                '}';
    }
}
