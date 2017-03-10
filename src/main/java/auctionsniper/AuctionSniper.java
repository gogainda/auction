package auctionsniper;


public class AuctionSniper implements AuctionEventListener {
    private final Item item;
    private SniperListener sniperListener;
    private final Auction auction;

    private SniperSnapshot snapshot;

    public AuctionSniper(Item item, Auction auction) {
        this.auction = auction;
        this.snapshot = SniperSnapshot.joining(item.identifier);
        this.item = item;
    }

    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    public void auctionFailed() {
        snapshot = snapshot.failed();
        sniperListener.sniperStateChanged(snapshot);
    }


    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch(priceSource) {
            case FromSniper:
                snapshot = snapshot.winning(price);
                break;
            case FromOtherBidder:
                int bid = price + increment;
                if (item.allowsBid(bid)) {
                    auction.bid(bid);
                    snapshot = snapshot.bidding(price, bid);
                } else {
                    snapshot = snapshot.losing(price);
                }
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
