package auctionsniper;

import auctionsniper.ui.MainWindow;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.lang.String.format;

public class Main {
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT =
            ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
    public static final String SNIPER_STATUS_NAME = "sniper status";
    public static final String BID_COMMAND_FORMAT = "";
    public static final String JOIN_COMMAND_FORMAT = "join";
    private MainWindow ui;
    @SuppressWarnings("unused")
    private Chat notToBeGCd;

    public Main() throws Exception {
        startUserInterface();

    }

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.joinAuction(
                connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]),
                args[ARG_ITEM_ID]);

    }

    private void joinAuction(XMPPConnection connection, String itemId)
            throws XMPPException {
        disconnectWhenUICloses(connection);
        final Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection),null);
        this.notToBeGCd = chat;
        Auction auction = new XMPPAuction(chat);
        chat.addMessageListener(new AuctionMessageTranslator(connection.getUser(), new AuctionSniper(auction, new SniperStateDisplayer(), itemId)));
        auction.join();
    }

    private void disconnectWhenUICloses(final XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                ui = new MainWindow();

            }
        });
    }

    private static XMPPConnection connection(String hostname, String username, String password)
            throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return connection;
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return format(AUCTION_ID_FORMAT, itemId,
                connection.getServiceName());
    }

    public void sniperLost() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ui.showStatus(MainWindow.STATUS_LOST);
            }
        });
    }

    public void sniperBidding() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ui.showStatus(MainWindow.STATUS_BIDDING);
            }
        });
    }

    public class SniperStateDisplayer implements SniperListener {
        public void sniperBidding() {
            showStatus(MainWindow.STATUS_BIDDING);
        }
        public void sniperLost() {
            showStatus(MainWindow.STATUS_LOST);
        }
        public void sniperWinning() {
            showStatus(MainWindow.STATUS_WINNING);
        }

        public void sniperWon() {
            showStatus(MainWindow.STATUS_WON);
        }

        public void sniperBidding(SniperSnapshot sniperSnapshot) {
            sniperStateChanged(sniperSnapshot);
        }

        public void sniperWinning(SniperSnapshot sniperSnapshot) {
            sniperStateChanged(sniperSnapshot);
        }

//        public void sniperBidding(final SniperSnapshot state) {
//            SwingUtilities.invokeLater(new Runnable() {
//                public void run() {
//                    ui.sniperStatusChanged(state, MainWindow.STATUS_BIDDING);
//                }
//            });
//
//        }
//
//        public void sniperWinning(final SniperSnapshot state) {
//            SwingUtilities.invokeLater(new Runnable() {
//                public void run() {
//                    ui.sniperStatusChanged(state, MainWindow.STATUS_WINNING);
//                }
//            });
//        }

        public void sniperStateChanged(final SniperSnapshot sniperSnapshot) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ui.sniperStatusChanged(sniperSnapshot);
                }
            });
        }

        private void showStatus(final String status) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() { ui.showStatus(status); }
            });
        }
    }
}