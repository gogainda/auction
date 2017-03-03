import auctionsniper.*;
import auctionsniper.xmpp.XMPPAuction;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertTrue;

public class AuctionSniperEndToEndTest {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final FakeAuctionServer server = auction;
    private final FakeAuctionServer auction2 = new FakeAuctionServer("item-65432");
    private final ApplicationRunner application = new ApplicationRunner();


    @Test
    public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auction.startSellingItem(); // Step 1
        application.startBiddingIn(auction); // Step 2
        auction.hasReceivedJoinRequestFromSniper(); // Step 3
        auction.announceClosed(); // Step 4
        application.showsSniperHasLostAuction(auction.getItemId()); // Step 5
    }

    @Test public void
    sniperMakesAHigherBidButLoses() throws Exception {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(auction,1000, 1098); // last price, last bid
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
        auction.announceClosed();
        application.showsSniperHasLostAuction(auction, 1000, 1098);
    }
    @Test public void
    sniperWinsAnAuctionByBiddingHigher() throws Exception {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(auction,1000, 1098); // last price, last bid
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
        auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
        application.hasShownSniperIsWinning(auction, 1098); // winning bid
        auction.announceClosed();
        application.showsSniperHasWonAuction(auction,1098); // last price
    }
//
    @Test public void
    sniperBidsForMultipleItems() throws Exception {
        auction.startSellingItem();
        auction2.startSellingItem();
        application.startBiddingIn(auction, auction2);
        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auction2.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auction.reportPrice(1000, 98, "other bidder");
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
        auction2.reportPrice(500, 21, "other bidder");
        auction2.hasReceivedBid(521, ApplicationRunner.SNIPER_XMPP_ID);
        auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
        auction2.reportPrice(521, 22, ApplicationRunner.SNIPER_XMPP_ID);
        application.hasShownSniperIsWinning(auction, 1098);
        application.hasShownSniperIsWinning(auction2, 521);
        auction.announceClosed();
        auction2.announceClosed();
        application.showsSniperHasWonAuction(auction, 1098);
        application.showsSniperHasWonAuction(auction2, 521);
    }

    @Test
    public void
    receivesEventsFromAuctionServerAfterJoining() throws Exception {
        CountDownLatch auctionWasClosed = new CountDownLatch(1);
        server.startSellingItem();
        application.startBiddingIn(server);
        XMPPAuctionHouse connection = Main.connection("localhost", "sniper", "sniper");
        Auction auction = new XMPPAuction(connection.getCon(), server.getItemId());
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));
        auction.join();
        server.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        server.announceClosed();
        assertTrue("should have been closed", auctionWasClosed.await(2, SECONDS));
    }
    private AuctionEventListener
    auctionClosedListener(final CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            public void auctionClosed() { auctionWasClosed.countDown(); }
            public void currentPrice(int price, int increment, PriceSource priceSource) {
// not implemented
            }
        };
    }

    // Additional cleanup
    @After
    public void stopAuction() {
        auction.stop();
    }
    @After public void stopApplication() {
        application.stop();
    }
}