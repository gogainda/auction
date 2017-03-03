package auctionsniper;

import auctionsniper.ui.SnipersTableModel;

/**
 * Created by igor on 2017-03-03.
 */
public class SniperPortfolio implements SniperCollector {
    private SnipersTableModel portfolioListener;

    public void addSniper(AuctionSniper sniper) {
        portfolioListener.addSniper(sniper);
    }

    public void addPortfolioListener(SnipersTableModel model) {
        portfolioListener = model;
    }

    public SnipersTableModel getCollector() {
        return portfolioListener;
    }
}
