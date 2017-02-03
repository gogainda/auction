package auctionsniper;


public class AuctionSniper implements AuctionEventListener {
    private final SniperListener sniperListener;
    private final Auction auction;
    private boolean isWinning = false;
    private String itemId;

    public AuctionSniper(Auction auction, SniperListener sniperListener, String itemId) {
        this.sniperListener = sniperListener;
        this.auction = auction;
        this.itemId = itemId;
    }

    public void auctionClosed() {
        if (isWinning) {
            sniperListener.sniperWon();
        } else {
            sniperListener.sniperLost();
        }
    }

//    public void currentPrice(int price, int increment, PriceSource priceSource) {
//        isWinning = priceSource == PriceSource.FromSniper;
//        if (isWinning) {
//            int bid = price;
//            sniperListener.sniperWinning(new SniperSnapshot(itemId, price, bid));
//        } else {
//            int bid = price + increment;
//            auction.bid(bid);
//            sniperListener.sniperBidding(new SniperSnapshot(itemId, price, bid));
//        }
//    }

    public void currentPrice(int price, int increment, PriceSource priceSource) {
        isWinning = priceSource == PriceSource.FromSniper;
        if (isWinning) {
            sniperListener.sniperWinning();
        } else {
            final int bid = price + increment;
            auction.bid(bid);
            sniperListener.sniperStateChanged(
                    new SniperSnapshot(itemId, price, bid, SniperState.BIDDING));
        }
    }
}
