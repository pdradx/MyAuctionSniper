import org.jivesoftware.smack.SmackException;
import org.junit.After;
import org.junit.Test;

public class AuctionSniperEndToEndTest {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();

    @Test
    public void sniperJoinsAuctionsUntilAuctionCloses() throws Exception {
        auction.startSellingItem();                 //Step 1
        application.startBiddingIn(auction);        //Step 2
        auction.hasReceivedJoinRequestFromSniper(); //Step 3
        auction.announceClosed();                   //Step 4
        application.showsSniperHasLostAuction();    //Step 5
    }

    //Additional cleanup
    @After
    public void stopAuction() throws SmackException.NotConnectedException {
        auction.stop();
    }

    @After
    public void stopApplication() {
        application.stop();
    }
}
