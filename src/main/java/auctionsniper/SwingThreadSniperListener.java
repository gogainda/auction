package auctionsniper;

import auctionsniper.ui.SnipersTableModel;

import javax.swing.*;

public class SwingThreadSniperListener implements SniperListener {
    private final SnipersTableModel snipers;

    public SwingThreadSniperListener(SnipersTableModel snipers) {
    this.snipers = snipers;
    }

    public void sniperStateChanged(final SniperSnapshot sniperSnapshot) {


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                snipers.sniperStateChanged(sniperSnapshot);
            }
        });
    }
}
