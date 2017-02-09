package auctionsniper.ui;


import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;
import com.objogate.exception.Defect;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;


public class SnipersTableModel extends AbstractTableModel {
//    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
//
//    private SniperSnapshot snapshot = STARTING_UP;
    private static String[] STATUS_TEXT = { MainWindow.STATUS_JOINING,
            MainWindow.STATUS_BIDDING, MainWindow.STATUS_WINNING, MainWindow.STATUS_LOST, MainWindow.STATUS_WON };
    private Vector<SniperSnapshot> snapshots = new Vector<SniperSnapshot>();

    public SnipersTableModel() {
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

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

//    public void showSnapshot(SniperSnapshot snapshot) {
//        this.snapshot = snapshot;
//        fireTableRowsUpdated(0, 0);
//    }

    public void addSniper(SniperSnapshot joining) {
        this.snapshots.add(joining);
        fireTableRowsInserted(this.snapshots.size() - 1, this.snapshots.size() - 1);
    }

    public void sniperStateChanged(SniperSnapshot newSnapshot) {
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
