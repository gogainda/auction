package auctionsniper;

import com.objogate.exception.Defect;

/**
 * Created by igor on 2017-02-03.
 */
public enum SniperState {
    JOINING {
        @Override public SniperState whenAuctionClosed() { return LOST; }
    },
    BIDDING {
        @Override public SniperState whenAuctionClosed() { return LOST; }
    },
    WINNING {
        @Override public SniperState whenAuctionClosed() { return WON; }
    },
    LOST,
    WON,
    LOSING {
        @Override public SniperState whenAuctionClosed() { return LOST; }
    },FAILED {
        @Override public SniperState whenAuctionClosed() { return LOST; }
    },
    ;
    public SniperState whenAuctionClosed() {
        throw new Defect("Auction is already closed");
    }
}
