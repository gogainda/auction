package auctionsniper.xmpp;

/**
 * Created by igor on 2017-03-09.
 */
public class MissingValueException extends RuntimeException {
    public MissingValueException(String name) {
        super(name);
    }
}
