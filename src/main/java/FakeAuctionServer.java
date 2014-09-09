import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.bytestreams.socks5.Socks5Proxy;

import java.io.IOException;

import static auctionsniper.ui.Main.AUCTION_RESOURCE;
import static auctionsniper.ui.Main.SERVER_PORT;
import static java.lang.String.format;

public class FakeAuctionServer {
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String XMPP_HOSTNAME = "localhost";
    public static final String AUCTION_PASSWORD = "auction";

    private final String itemId;
    private final XMPPConnection connection;
    private final SingleMessageListener messageListener = new SingleMessageListener();
    private Chat currentChat;

    public FakeAuctionServer(String itemId) {
        Socks5Proxy.setLocalSocks5ProxyPort(7778);
        this.itemId = itemId;
        this.connection = new XMPPTCPConnection(new ConnectionConfiguration(XMPP_HOSTNAME, SERVER_PORT));
    }

    public void startSellingItem() throws IOException, XMPPException, SmackException {
        connection.connect();
        connection.login(format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);
        ChatManager.getInstanceFor(connection).addChatListener(
                new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean b) {
                        currentChat = chat;
                        chat.addMessageListener(messageListener);
                    }
                }
        );
    }

    public String getItemId() {
        return itemId;
    }

    public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
        messageListener.receivesAMessage();
    }

    public void announceClosed() throws XMPPException, SmackException.NotConnectedException {
        currentChat.sendMessage(new Message());
    }

    public void stop() throws SmackException.NotConnectedException {
        connection.disconnect();
    }
}
