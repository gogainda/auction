package auctionsniper.xmpp;


import auctionsniper.AuctionEventListener;
import auctionsniper.XMPPFailureReporter;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.HashMap;
import java.util.Map;

import static auctionsniper.AuctionEventListener.PriceSource.FromOtherBidder;
import static auctionsniper.AuctionEventListener.PriceSource.FromSniper;

public class AuctionMessageTranslator implements MessageListener {
    private final AuctionEventListener listener;
    private final String sniperId;
    private final XMPPFailureReporter failureReporter;

    public AuctionMessageTranslator(String sniperId, AuctionEventListener listener, XMPPFailureReporter failureReporter) {
        this.sniperId = sniperId;
        this.listener = listener;
        this.failureReporter = failureReporter;
    }

    public void processMessage(Chat chat, Message message) {
        System.out.println("Processing a message" + message.getBody());
        System.out.println("Processing a message for char" + chat);

        String messageBody = message.getBody();
        try {
            translate(messageBody);
        } catch (RuntimeException exception) {
            listener.auctionFailed();
            System.out.println("hereeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            failureReporter.cannotTranslateMessage(sniperId, messageBody, exception);
            exception.printStackTrace();
        }
    }

    private void translate(String message) throws MissingValueException {
        AuctionEvent event = AuctionEvent.from(message);
        String eventType = event.type();
        if ("CLOSE".equals(eventType)) {
            listener.auctionClosed();
        }
        if ("PRICE".equals(eventType)) {
            listener.currentPrice(event.currentPrice(),
                    event.increment(),
                    event.isFrom(sniperId));
        }
    }

    private static class AuctionEvent {
        private final Map<String, String> fields = new HashMap<String, String>();
        public String type() throws MissingValueException { return get("Event"); }
        public int currentPrice() throws MissingValueException { return getInt("CurrentPrice"); }
        public int increment() throws MissingValueException { return getInt("Increment"); }
        private int getInt(String fieldName) throws MissingValueException {
            return Integer.parseInt(get(fieldName));
        }
        private String get(String name) throws MissingValueException {
            String value = fields.get(name);
            if (null == value) {
                throw new MissingValueException(name);
            }
            return value;
        }
        private void addField(String field) {
            String[] pair = field.split(":");
            fields.put(pair[0].trim(), pair[1].trim());
        }
        static AuctionEvent from(String messageBody) {
            AuctionEvent event = new AuctionEvent();
            for (String field : fieldsIn(messageBody)) {
                event.addField(field);
            }
            return event;
        }
        static String[] fieldsIn(String messageBody) {
            System.out.println(messageBody);
            return messageBody.split(";");
        }

        public AuctionEventListener.PriceSource isFrom(String sniperId) throws MissingValueException {
            return sniperId.equals(bidder()) ? FromSniper : FromOtherBidder;
        }

        private String bidder() throws MissingValueException { return get("Bidder"); }
    }
}
