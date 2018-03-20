package network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static data.ConnectConstants.*;

public final class NetworkConnection {
    public static final boolean isConnected() {
        Socket socket = new Socket();
        Exception exception = null;
        try {
            socket.connect(new InetSocketAddress(IP_CONNECTION, Integer.valueOf(PORT_CONNECTION)));
        } catch (IOException e) {
            exception = e;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        return exception == null ? true : false;
    }
}