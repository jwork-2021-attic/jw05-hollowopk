import javafx.scene.input.KeyCode;
import screen.LoseScreen;
import screen.PlayScreen;
import screen.Screen;
import screen.WinScreen;
import world.MapData;
import world.ThingData;
import world.Tile;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class NIOServer {

    private static Screen screen;

    private static ServerSocketChannel serverSocketChannel;
    private static Selector selector;

    private static List<SelectionKey> clients;

    public static void main(String[] args) throws IOException {
        init();
        new Thread(() -> {
            try {
                buildConnection();
            } catch (IOException ignored) {}
        }).start();
    }

    private static void init() throws IOException {
        clients = new ArrayList<>();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(3456));
        serverSocketChannel.configureBlocking(false);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private static void buildConnection() throws IOException {
        int connect = 0;
        while (true) {
            if (selector.select(30) == 0) {
                continue;
            }

            Set<SelectionKey> keySet = selector.selectedKeys();

            for (SelectionKey key : keySet) {

                if(!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    if (connect < 2) {
                        handleKeyAccept(key, connect++);
                    }
                    if (connect == 2) {
                        screen = new PlayScreen();
                        sendMapData();
                    }
                }

                if (key.isReadable()) {
                    handleKeyRead(key);
                }

            }

            keySet.clear();
        }
    }

    private static void handleKeyAccept(SelectionKey key, int playerNum) throws IOException {
        int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
        ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();
        SocketChannel client = socketChannel.accept();
        client.configureBlocking(false);
        clients.add(client.register(selector, interestSet, playerNum));
    }

    private static void handleKeyRead(SelectionKey key) throws IOException {
        int playerNum = (int) key.attachment();
        SocketChannel socketChannel = (SocketChannel) key.channel();
        byte[] bytes = new byte[1024];
        int len = socketChannel.read(ByteBuffer.wrap(bytes));
        if (len != 0) {
            String name = new String(bytes, 0, len);
            KeyCode keyCode = KeyCode.getKeyCode(name);
            screen = screen.respondToUserInput(keyCode, playerNum);
        }
    }

    private static MapData getMapData(int playerNum) {
        if (screen instanceof PlayScreen) {
            PlayScreen playScreen = (PlayScreen) screen;
            ThingData[] data = playScreen.getData();
            Tile[][] tiles = playScreen.getTiles();
            String[] messages = playScreen.getMessages(playerNum);
            String state = playScreen.getState(playerNum);
            MapData mapData = new MapData(data, tiles, messages, state);
            mapData.setGameState(playScreen.check(playerNum));
            return mapData;
        }
        return null;
    }

    private static void sendMapData() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (SelectionKey key : clients) {
                    int playerNum = (int) key.attachment();
                    SocketChannel channel = (SocketChannel) key.channel();
                    MapData mapData = getMapData(playerNum);
                    if (mapData != null) {
                        byte[] bytes;
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        try {
                            ObjectOutputStream oos = new ObjectOutputStream(bos);
                            oos.writeObject(mapData);
                            oos.flush();
                            bytes = bos.toByteArray();
                            oos.close();
                            bos.close();
                        } catch (IOException ex) {
                            break;
                        }
                        try {
                            channel.write(ByteBuffer.wrap(bytes));
                        } catch (IOException e) {
                            break;
                        }
                    }
                }
            }
        }).start();
    }

}
