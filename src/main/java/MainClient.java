import world.Tile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainClient {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String host = "127.0.0.1";
        int port = 3457;
        Socket socket = new Socket(host, port);
        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        while (true) {
            Object obj1 = is.readObject();
            Object obj2 = is.readObject();
            if (obj1 != null) {
                Tile[][] tiles = (Tile[][]) obj1;
                System.out.println(tiles.length);
            }
            if (obj2 != null) {
                System.out.println(obj2);
            }
        }
    }

}
