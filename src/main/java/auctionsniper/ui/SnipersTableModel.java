package auctionsniper.ui;


import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

import javax.swing.table.AbstractTableModel;


public class SnipersTableModel extends AbstractTableModel {
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);

    private SniperSnapshot snapshot = STARTING_UP;
    private static String[] STATUS_TEXT = { MainWindow.STATUS_JOINING,
            MainWindow.STATUS_BIDDING, MainWindow.STATUS_WINNING, MainWindow.STATUS_LOST, MainWindow.STATUS_WON };
    private String state;

    public int getColumnCount() {
        return Column.values().length;
    }
    public int getRowCount() {
        return 1;
    }
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return snapshot.itemId;
            case LAST_PRICE:
                return snapshot.lastPrice;
            case LAST_BID:
                return snapshot.lastBid;
            case SNIPER_STATE:
                return STATUS_TEXT[snapshot.state.ordinal()];
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);
        }
    }
    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        this.snapshot = newSnapshot;
        this.state = STATUS_TEXT[newSnapshot.state.ordinal()];
        fireTableRowsUpdated(0, 0);
    }



}
