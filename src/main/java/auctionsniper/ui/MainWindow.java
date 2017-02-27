package auctionsniper.ui;

import auctionsniper.UserRequestListener;
import org.jmock.example.announcer.Announcer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
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
    public static final String JOIN_BUTTON_NAME = "Join";

    private final AbstractTableModel snipers;
    private final Announcer<UserRequestListener> userRequests =
            Announcer.to(UserRequestListener.class);

    public MainWindow(AbstractTableModel snipers) {
        super(APPLICATION_TITLE);
        setName(APPLICATION_TITLE);
        this.snipers = snipers;
        fillContentPane(makeSnipersTable(), makeControls());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable, JPanel jPanel) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
        contentPane.add(jPanel, BorderLayout.CENTER);
    }
    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(APPLICATION_TITLE);
        return snipersTable;
    }
    private JPanel makeControls() {
        JPanel controls = new JPanel(new FlowLayout());
        final JTextField itemIdField = new JTextField();
        itemIdField.setColumns(25);
        itemIdField.setName(NEW_ITEM_ID_NAME);
        controls.add(itemIdField);
        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);
        joinAuctionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userRequests.announce().joinAuction(itemIdField.getText());
            }
        });
        controls.add(joinAuctionButton);
        return controls;
    }

    public void addUserRequestListener(UserRequestListener userRequestListener) {
        userRequests.addListener(userRequestListener);
    }
}
