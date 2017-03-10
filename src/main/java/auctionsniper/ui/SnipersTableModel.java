package auctionsniper.ui;


import auctionsniper.*;
import com.objogate.exception.Defect;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;


public class SnipersTableModel extends AbstractTableModel implements SniperListener, SniperCollector {
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);

    private SniperSnapshot snapshot = STARTING_UP;
    private static String[] STATUS_TEXT = { MainWindow.STATUS_JOINING,
            MainWindow.STATUS_BIDDING, MainWindow.STATUS_WINNING, MainWindow.STATUS_LOST, MainWindow.STATUS_WON, MainWindow.LOSING,  MainWindow.FAILED};
    private Vector<SniperSnapshot> snapshots = new Vector<SniperSnapshot>();



    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    public int getColumnCount() {
        return Column.values().length;
    }

    public int getRowCount() {
        return snapshots.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
    }

    @Override public String getColumnName(int column) {
        return Column.at(column).name;
    }

    public void addSniper(AuctionSniper sniper) {
        addSniperSnapshot(sniper.getSnapshot());
    }


    private void addSniperSnapshot(SniperSnapshot sniperSnapshot) {
        System.out.println("sniper added");
        snapshots.add(sniperSnapshot);
        int row = snapshots.size() - 1;
        fireTableRowsInserted(row, row);
    }

    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        System.out.println("sniper state Changed" + newSnapshot);
        int row = rowMatching(newSnapshot);

        snapshots.set(row, newSnapshot);
        fireTableRowsUpdated(row, row);
    }
    private int rowMatching(SniperSnapshot snapshot) {
        for (int i = 0; i < snapshots.size(); i++) {
            if (snapshot.isForSameItemAs(snapshots.get(i))) {
                return i;
            }
        }
        throw new Defect("Cannot find match for " + snapshot);
    }
}
