package auctionsniper;

import java.util.EventListener;

/**
 * Created by igor on 2017-03-03.
 */
public interface PortfolioListener extends EventListener {

    void sniperAdded(AuctionSniper sniper);
}
