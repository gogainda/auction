package auctionsniper.ui;

import auctionsniper.AuctionSniperDriver;
import auctionsniper.Item;
import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;
import com.objogate.wl.swing.probe.ValueMatcherProbe;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class MainWindowTest {
    private final SnipersTableModel tableModel = new SnipersTableModel();
    private final SniperPortfolio portfolio = new SniperPortfolio(tableModel);
    private final MainWindow mainWindow = new MainWindow(portfolio);
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);
    @Test
    public void
    makesUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<Item> itemProbe =
                new ValueMatcherProbe<Item>(equalTo(new Item("an item-id", 789)), "item request");
        mainWindow.addUserRequestListener(
                new UserRequestListener() {
                    public void joinAuction(Item item) {
                        System.out.println(item);
                        itemProbe.setReceivedValue(item);
                    }
                });
        driver.startBiddingFor("an item-id", 789);
        driver.check(itemProbe);
    }
}