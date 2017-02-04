package auctionsniper;

import auctionsniper.ui.MainWindow;

import static auctionsniper.ui.MainWindow.STATUS_JOINING;
import static auctionsniper.ui.MainWindow.STATUS_LOST;

public class ApplicationRunner {
    private String itemId;
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@192.168.0.12/Auction";
    private AuctionSniperDriver driver;


    public void startBiddingIn(final FakeAuctionServer auction) {
        itemId = auction.getItemId();
        Thread thread = new Thread("Test Application") {
            @Override public void run() {
                try {
                    Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, itemId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
        driver.hasTitle(MainWindow.APPLICATION_TITLE);
        driver.hasColumnTitles();
        driver.showsSniperStatus("", 0, 0,
                STATUS_JOINING);
    }
    public void showsSniperHasLostAuction() {
        driver.showsSniperStatus(itemId, 0, 0,STATUS_LOST);
    }
    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }



    public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid,
                MainWindow.STATUS_BIDDING);
    }
    public void hasShownSniperIsWinning(int winningBid) {
        driver.showsSniperStatus(itemId, winningBid, winningBid,
                MainWindow.STATUS_WINNING);
    }
    public void showsSniperHasWonAuction(int lastPrice) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice,
                MainWindow.STATUS_WON);
    }

    public void showsSniperHasLostAuction(int lastPrice, int lastBid) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid,
                MainWindow.STATUS_LOST);
    }


}
