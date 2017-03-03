package auctionsniper;

import auctionsniper.ui.SnipersTableModel;

public class SniperLauncher implements UserRequestListener {
    private final AuctionHouse auctionHouse;
    private final SnipersTableModel collector;
    public SniperLauncher(AuctionHouse auctionHouse, SniperPortfolio sniperPortfolio) {
        this.auctionHouse = auctionHouse;
        collector = sniperPortfolio.getCollector();
    }
    public void joinAuction(String itemId) {
        System.out.println("joining" + itemId);
        Auction auction = auctionHouse.auctionFor(itemId);
        AuctionSniper sniper = new AuctionSniper(itemId, auction);
        sniper.addSniperListener(collector);
        auction.addAuctionEventListener(sniper);
        System.out.println(sniper);
        collector.addSniper(sniper);
        auction.join();
    }
}
