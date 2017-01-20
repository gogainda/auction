package auctionsniper.ui;

import auctionsniper.ApplicationRunner;
import auctionsniper.Main;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;


public class MainWindow extends JFrame {
    public static final String SNIPER_STATUS_NAME = "sniper status";
    public static final String MAIN_WINDOW_NAME = "Auction sniper";
    public static final String STATUS_LOST = "Lost";
    private final JLabel sniperStatus = createLabel(ApplicationRunner.STATUS_JOINING);

    public MainWindow() {
        super("Auction Sniper");
        setName(MAIN_WINDOW_NAME);
        add(sniperStatus);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        return result;
    }

    public void showStatus(String status) {
        sniperStatus.setText(status);
    }
}