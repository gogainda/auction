package auctionsniper;

import auctionsniper.ui.MainWindow;

import java.io.IOException;

import static auctionsniper.FakeAuctionServer.XMPP_HOSTNAME;
import static auctionsniper.ui.MainWindow.STATUS_LOST;
import static auctionsniper.ui.SnipersTableModel.textFor;
import static org.hamcrest.CoreMatchers.containsString;

public class ApplicationRunner {
    private AuctionLogDriver logDriver = new AuctionLogDriver();
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@192.168.0.12/Auction";
    private AuctionSniperDriver driver;

    public void reportsInvalidMessage(FakeAuctionServer auction, String message)
            throws IOException
    {
        logDriver.hasEntry(containsString(message));
    }


    public void startBiddingIn(final FakeAuctionServer... auctions) {
        startDriver(auctions);

        for (FakeAuctionServer auction : auctions) {
            final String itemId = auction.getItemId();
            driver.openBiddingFor(itemId);
            driver.showsSniperStatus(itemId, 0, 0, textFor(SniperState.JOINING));
        }
    }

    private void startDriver(final FakeAuctionServer... auctions) {
        logDriver.clearLog();
        Thread thread = new Thread("Test Application") {
            @Override public void run() {
                try {
                    Main.main(arguments(auctions));
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
    }

    protected static String[] arguments(FakeAuctionServer... auctions) {
        String[] arguments = new String[auctions.length + 3];
        arguments[0] = XMPP_HOSTNAME;
        arguments[1] = SNIPER_ID;
        arguments[2] = SNIPER_PASSWORD;
        for (int i = 0; i < auctions.length; i++) {
            arguments[i + 3] = auctions[i].getItemId();
        }
        return arguments;
    }
    public void showsSniperHasLostAuction(String itemId) {
        driver.showsSniperStatus(itemId, 0, 0,STATUS_LOST);
    }
    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }



    public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid,
                MainWindow.STATUS_BIDDING);
    }
    public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
        driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid,
                MainWindow.STATUS_WINNING);
    }
    public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice,
                MainWindow.STATUS_WON);
    }

    public void showsSniperHasLostAuction(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid,
                MainWindow.STATUS_LOST);
    }


    public void startBiddingWithStopPrice(FakeAuctionServer auction, int i) {
        startDriver(auction);
        driver.openBiddingFor(auction.getItemId(), i);
    }

    public void hasShownSniperIsLosing(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid,
                MainWindow.LOSING);
    }

    public void showsSniperHasFailed(FakeAuctionServer auction) {
        driver.showsSniperStatus(auction.getItemId(), 0, 0,
                MainWindow.FAILED);
    }
}
