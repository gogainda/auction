package auctionsniper;


import auctionsniper.xmpp.XMPPAuction;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static auctionsniper.AuctionLogDriver.LOG_FILE_NAME;
import static auctionsniper.Main.AUCTION_RESOURCE;
import static org.apache.commons.io.FilenameUtils.getFullPath;

public class XMPPAuctionHouse implements AuctionHouse {

    private static final java.lang.String LOGGER_NAME = "Logger";
    private final XMPPFailureReporter failureReporter;
    private XMPPConnection connection;

    public XMPPAuctionHouse(XMPPConnection connection) throws XMPPAuctionException {

        this.connection = connection;
        this.failureReporter = new LoggingXMPPFailureReporter(makeLogger());
    }

    public Auction auctionFor(String itemId) {
        return new XMPPAuction(connection, itemId, failureReporter);
    }

    public void disconnect() {
        connection.disconnect();
    }

    public static XMPPAuctionHouse connect(String hostname, String username, String password) throws XMPPAuctionException {
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

    public XMPPConnection getConnection() {
        return connection;
    }
    private Logger makeLogger() throws XMPPAuctionException {
        Logger logger = Logger.getLogger(LOGGER_NAME);
        logger.setUseParentHandlers(false);
        logger.addHandler(simpleFileHandler());
        return logger;
    }

    private FileHandler simpleFileHandler() throws XMPPAuctionException {
        try {
            FileHandler handler = new FileHandler(LOG_FILE_NAME);
            handler.setFormatter(new SimpleFormatter());
            return handler;
        } catch (Exception e) {
            throw new XMPPAuctionException("Could not create logger FileHandler "
                    + getFullPath(LOG_FILE_NAME), e);
        }
    }
}
