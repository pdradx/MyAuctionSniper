import auctionsniper.ui.MainWindow;
import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

import static auctionsniper.ui.MainWindow.SNIPER_STATUS_NAME;
import static auctionsniper.ui.MainWindow.STATUS_LOST;
import static org.hamcrest.Matchers.equalTo;

@SuppressWarnings("unchecked")
public class AuctionSniperDriver extends JFrameDriver {

    public AuctionSniperDriver(int timeoutMillis) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame(
                        named(MainWindow.MAIN_WINDOW_NAME),
                        showingOnScreen()),
                new AWTEventQueueProber(timeoutMillis, 100));
    }

    public void showsSniperStatus(String statusText) {
        new JLabelDriver(this, named(SNIPER_STATUS_NAME)).hasText(equalTo(statusText));
    }

    public void showsSniperHasLostAuction() {
        showsSniperStatus(STATUS_LOST);
    }
}
