package auctionsniper.ui;

import auctionsniper.Item;
import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;
import org.jmock.example.announcer.Announcer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainWindow extends JFrame {
    public static final String APPLICATION_TITLE = "Auction Sniper";
    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_BIDDING = "bidding";
    public static final String STATUS_WINNING = "winning";
    public static final String STATUS_WON = "Won";
    public static final String NEW_ITEM_ID_NAME = "new id";
    public static final String NEW_ITEM_STOP_PRICE_NAME = "stop price";
    public static final String JOIN_BUTTON_NAME = "Join";
    public static final String LOSING = "Losing";
    final JTextField itemIdField = new JTextField();
    final JFormattedTextField stopPriceField = new JFormattedTextField();

    private final Announcer<UserRequestListener> userRequests =
            Announcer.to(UserRequestListener.class);

    public MainWindow(SniperPortfolio portfolio) {
        super(APPLICATION_TITLE);
        setName(APPLICATION_TITLE);
        fillContentPane(makeSnipersTable(portfolio), makeControls());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    private void fillContentPane(JTable snipersTable, JPanel jPanel) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.SOUTH);
        contentPane.add(jPanel, BorderLayout.NORTH);
        contentPane.setPreferredSize(new Dimension(1000,800));

    }
    private JTable makeSnipersTable(SniperPortfolio portfolio) {
        SnipersTableModel model = new SnipersTableModel();
        portfolio.addPortfolioListener(model);
        JTable snipersTable = new JTable(model);
        snipersTable.setName(APPLICATION_TITLE);
        return snipersTable;
    }
    private JPanel makeControls() {
        JPanel controls = new JPanel(new FlowLayout());

        itemIdField.setColumns(25);
        itemIdField.setName(NEW_ITEM_ID_NAME);
        controls.add(itemIdField);


        stopPriceField.setColumns(25);
        stopPriceField.setName(NEW_ITEM_STOP_PRICE_NAME);
        stopPriceField.setValue(Integer.MAX_VALUE);
        controls.add(stopPriceField);

        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);
        joinAuctionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userRequests.announce().joinAuction(new Item(itemId(), stopPrice()));
            }
        });
        controls.add(joinAuctionButton);
        return controls;
    }

    private String itemId() {
        return itemIdField.getText();
    }
    private int stopPrice() {

        return ((Number) stopPriceField.getValue()).intValue();
    }

    public void addUserRequestListener(UserRequestListener userRequestListener) {
        userRequests.addListener(userRequestListener);
    }
}
