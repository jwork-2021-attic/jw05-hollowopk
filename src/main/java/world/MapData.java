package world;

import java.io.Serializable;

public class MapData implements Serializable {

    private ThingData[] data;

    public ThingData[] getData() {
        return data;
    }

    private Tile[][] tiles;

    public Tile[][] getTiles() {
        return tiles;
    }

    private String[] messages;

    public String[] getMessages() {
        return messages;
    }

    private String stateMessage;

    public String getStateMessage() {
        return stateMessage;
    }

    private String gameState;

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public MapData(ThingData[] data, Tile[][] tiles, String[] messages, String stateMessage) {
        this.data = data;
        this.tiles = tiles;
        this.messages = messages;
        this.stateMessage = stateMessage;
    }

}
