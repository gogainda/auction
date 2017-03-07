package auctionsniper;

import auctionsniper.ui.SnipersTableModel;

public class SniperLauncher implements UserRequestListener {
    private final AuctionHouse auctionHouse;
    private final SnipersTableModel collector;
    public SniperLauncher(AuctionHouse auctionHouse, SniperPortfolio sniperPortfolio) {
        this.auctionHouse = auctionHouse;
        collector = sniperPortfolio.getCollector();
    }


    public void joinAuction(Item item) {
        System.out.println("joining" + item.identifier);
        Auction auction = auctionHouse.auctionFor(item.identifier);
        AuctionSniper sniper = new AuctionSniper(item, auction);
        sniper.addSniperListener(collector);
        auction.addAuctionEventListener(sniper);
        System.out.println(sniper);
        collector.addSniper(sniper);
        auction.join();
    }
}
