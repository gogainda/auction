package auctionsniper;


import java.util.EventListener;

public interface SniperListener extends EventListener {
    void sniperLost();

    void sniperWinning();

    void sniperWon();

    void sniperBidding(SniperSnapshot sniperSnapshot);

    void sniperWinning(SniperSnapshot sniperSnapshot);

    void sniperStateChanged(SniperSnapshot sniperSnapshot);
}
