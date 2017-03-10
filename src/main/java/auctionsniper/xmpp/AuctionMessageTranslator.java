package auctionsniper.xmpp;


import auctionsniper.AuctionEventListener;
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

    public AuctionMessageTranslator(String sniperId, AuctionEventListener listener) {
        this.sniperId = sniperId;
        this.listener = listener;
    }

    public void processMessage(Chat chat, Message message) {
        System.out.println("Processing a message" + message.getBody());
        System.out.println("Processing a message for char" + chat);


        try {
            if (message.getBody() != null) {
                translate(message);
            }
        } catch (MissingValueException e) {
            e.printStackTrace();
            listener.auctionFailed();
        } catch (Exception parseException) {
            parseException.printStackTrace();
            listener.auctionFailed();
        }
    }

    private void translate(Message message) throws MissingValueException {
        AuctionEvent event = AuctionEvent.from(message.getBody());
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
