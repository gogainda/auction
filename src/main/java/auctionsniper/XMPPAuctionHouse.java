package auctionsniper;


import auctionsniper.xmpp.XMPPAuction;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import static auctionsniper.Main.AUCTION_RESOURCE;

public class XMPPAuctionHouse implements AuctionHouse {

    private XMPPConnection con;

    public XMPPAuctionHouse(XMPPConnection connection) {
        this.con = connection;
    }

    public Auction auctionFor(String itemId) {
        return new XMPPAuction(con, itemId);
    }

    public void disconnect() {
        con.disconnect();
    }

    public static XMPPAuctionHouse connect(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return new XMPPAuctionHouse(connection);
    }
}
