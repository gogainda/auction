package auctionsniper;


public class AuctionSniper implements AuctionEventListener {
    private final SniperListener sniperListener;
    private final Auction auction;
    private boolean isWinning = false;
    private String itemId;

    private SniperSnapshot snapshot;

    public AuctionSniper(Auction auction, SniperListener sniperListener, String itemId) {
        this.sniperListener = sniperListener;
        this.auction = auction;
        this.itemId = itemId;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    public void auctionClosed() {
        if (isWinning) {
            SniperSnapshot wonSnapshot = new SniperSnapshot(snapshot.itemId, snapshot.lastPrice, snapshot.lastBid, SniperState.WON);
            sniperListener.sniperStateChanged(wonSnapshot);
        } else {
            SniperSnapshot lostSnapshot = new SniperSnapshot(snapshot.itemId, snapshot.lastPrice, snapshot.lastBid, SniperState.LOST);
            sniperListener.sniperStateChanged(lostSnapshot);
        }
    }


    public void currentPrice(int price, int increment, PriceSource priceSource) {
        isWinning = priceSource == PriceSource.FromSniper;
        if (isWinning) {
            snapshot = snapshot.winning(price);
        } else {
            final int bid = price + increment;
            auction.bid(bid);
            snapshot = snapshot.bidding(price, bid);
        }
        sniperListener.sniperStateChanged(snapshot);
    }
}
