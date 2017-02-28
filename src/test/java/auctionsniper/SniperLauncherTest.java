package auctionsniper;

import auctionsniper.ui.SnipersTableModel;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

/**
 * Created by igor on 2017-02-28.
 */
public class SniperLauncherTest {
    private final Mockery context = new Mockery();
    private final Auction auction = context.mock(Auction.class);
    private final States auctionState = context.states("auction state")
            .startsAs("not joined");
    private final AuctionHouse auctionHouse = context.mock(AuctionHouse.class);
    private final SnipersTableModel tableModel = new SnipersTableModel();
    private final SniperLauncher launcher = new SniperLauncher(auctionHouse,tableModel);
    private final SniperCollector collector = context.mock(SniperCollector.class);

    @Test
    public void
    addsNewSniperToCollectorAndThenJoinsAuction() {
        final String itemId = "item 123";
        context.checking(new Expectations() {{
            allowing(auctionHouse).auctionFor(itemId); will(returnValue(auction));
            oneOf(auction).addAuctionEventListener(with(eventListener(itemId)));
            when(auctionState.is("not joined"));
            oneOf(collector).addSniper(with(sniperForItem(itemId)));
            when(auctionState.is("not joined"));
            one(auction).join(); then(auctionState.is("joined"));
        }});
        launcher.joinAuction(itemId);
    }

    private Matcher<AuctionSniper> sniperForItem(String itemId) {
        return hasProperty("itemId", equalTo(itemId));
    }

    private Matcher<AuctionEventListener> eventListener(String itemId) {
        return hasProperty("snapshot", equalTo(SniperSnapshot.joining(itemId)));
    }

}