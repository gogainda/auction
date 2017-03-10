package auctionsniper.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.Main;
import auctionsniper.XMPPFailureReporter;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jmock.example.announcer.Announcer;

import static auctionsniper.Main.AUCTION_ID_FORMAT;

public class XMPPAuction implements Auction {
    private final XMPPFailureReporter failureReporter;

    public void bid(int amount) {
        sendMessage(String.format(Main.BID_COMMAND_FORMAT, amount));
    }

    public void join() {
        System.out.println("sending");
        sendMessage(Main.JOIN_COMMAND_FORMAT);
    }


    public void addAuctionEventListener(AuctionEventListener auctionSniper) {
        auctionEventListeners.addListener(auctionSniper);
    }

    private void sendMessage(final String message) {
        System.out.println("chat join" + chat);
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    private final Announcer<AuctionEventListener> auctionEventListeners = Announcer.to(AuctionEventListener.class);
    private final Chat chat;

    public XMPPAuction(XMPPConnection connection, String auctionJID, XMPPFailureReporter failureReporter) {
        System.out.println("xmmp");
        System.out.println(auctionJID);
        this.failureReporter = failureReporter;
        AuctionMessageTranslator translator = translatorFor(connection);
        this.chat = connection.getChatManager().createChat(auctionId(auctionJID, connection), translator);
        System.out.println(chat);
        addAuctionEventListener(chatDisconnectorFor(translator));
    }

    public static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    private AuctionMessageTranslator translatorFor(XMPPConnection connection) {
        return new AuctionMessageTranslator(connection.getUser(),
                auctionEventListeners.announce(), failureReporter);
    }

    private AuctionEventListener
    chatDisconnectorFor(final AuctionMessageTranslator translator) {
        return new AuctionEventListener() {
            public void auctionClosed() {

            }

            public void auctionFailed() {
                chat.removeMessageListener(translator);
            }

            public void currentPrice(int bidPrice, int incrementBy, PriceSource fromOtherBidder) {

            }
        };
    }
}
