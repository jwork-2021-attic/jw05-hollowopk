import javafx.scene.input.KeyCode;
import screen.LoseScreen;
import screen.PlayScreen;
import screen.Screen;
import screen.WinScreen;
import world.MapData;
import world.ThingData;
import world.Tile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {

    private static Screen screen;

    public static void main(String[] args) throws IOException {
        screen = new PlayScreen();
        ServerSocket serverSocket = new ServerSocket(3457);
        int connect = 0;
        while (connect < 2) {
            Socket socket = serverSocket.accept();
            sendSceneMessage(socket);
            handleOnKeyPressed(socket);
            connect++;
        }
    }

    private static void sendSceneMessage(Socket socket) {
        new Thread(() -> {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                while (!socket.isClosed()) {
                    if (screen instanceof PlayScreen) {
                        PlayScreen playScreen = (PlayScreen) screen;
                        ThingData[] data = playScreen.getData();
                        Tile[][] tiles = playScreen.getTiles();
                        String[] messages = playScreen.getMessages();
                        String state = playScreen.getState();
                        MapData mapData = new MapData(data, tiles, messages, state);
                        screen = playScreen.check();
                        if (screen instanceof WinScreen) {
                            mapData.setGameState("win");
                        } else if (screen instanceof LoseScreen) {
                            mapData.setGameState("lose");
                        } else {
                            mapData.setGameState("playing");
                        }
                        oos.writeObject(mapData);
                        oos.flush();
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException ignored) {}
        }).start();
    }

    private static void handleOnKeyPressed(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        new Thread(() -> {
            String name = "";
            while (true) {
                try {
                    if ((name = reader.readLine()) == null) break;
                } catch (IOException e) {
                    break;
                }
                KeyCode keyCode = KeyCode.getKeyCode(name);
                screen = screen.respondToUserInput(keyCode);
            }
        }).start();
    }

}
