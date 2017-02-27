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

    public static XMPPAuctionHouse connect(String hostname, String username, String password){
        XMPPConnection connection = new XMPPConnection(hostname);
        try {
            connection.connect();
            connection.login(username, password, AUCTION_RESOURCE);
        } catch (XMPPException e) {
            e.printStackTrace();
            throw new RuntimeException("");
        }
        return new XMPPAuctionHouse(connection);
    }

    public XMPPConnection getCon() {
        return con;
    }
}
