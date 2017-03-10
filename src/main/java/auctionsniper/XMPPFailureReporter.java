package auctionsniper;


public interface XMPPFailureReporter {
    void cannotTranslateMessage(String auctionId, String failedMessage,
                                Exception exception);
}
