package auctionsniper.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.Main;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jmock.example.announcer.Announcer;

import static auctionsniper.Main.AUCTION_ID_FORMAT;

public class XMPPAuction implements Auction {

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
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    private final Announcer<AuctionEventListener> auctionEventListeners = Announcer.to(AuctionEventListener.class);
    private final Chat chat;
    public XMPPAuction(XMPPConnection connection, String itemId) {
        chat = connection.getChatManager().createChat(
                auctionId(itemId, connection),
                new AuctionMessageTranslator(connection.getUser(),
                        auctionEventListeners.announce()));
    }
    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }
}
