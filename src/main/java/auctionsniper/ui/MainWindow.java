package auctionsniper.ui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;


public class MainWindow extends JFrame {
    public static final String APPLICATION_TITLE = "Auction Sniper";
    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_BIDDING = "bidding";
    public static final String STATUS_WINNING = "winning";
    public static final String STATUS_WON = "Won";

    private final AbstractTableModel snipers;

    public MainWindow(AbstractTableModel snipers) {
        super(APPLICATION_TITLE);
        setName(APPLICATION_TITLE);
        this.snipers = snipers;
        fillContentPane(makeSnipersTable());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }
    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(APPLICATION_TITLE);
        return snipersTable;
    }
}
