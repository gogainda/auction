package auctionsniper;

import java.util.logging.Logger;

/**
 * Created by igor on 2017-03-11.
 */
public class LoggingXMPPFailureReporter implements XMPPFailureReporter {
    private final Logger logger;

    public LoggingXMPPFailureReporter(Logger logger) {
        this.logger = logger;

    }

    public void cannotTranslateMessage(String auctionId, String failedMessage, Exception exception) {
        System.out.println(String.format("<%s> "
                + "Could not translate message \"%s\" "
                + "because \"%s\"", auctionId, failedMessage, exception.toString()));
        logger.severe(String.format("<%s> "
                + "Could not translate message \"%s\" "
                + "because \"%s\"", auctionId, failedMessage, exception.toString()));

    }
}
