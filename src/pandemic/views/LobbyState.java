package pandemic.views;

import javafx.scene.layout.VBox;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.io.Serializable;
import java.util.ArrayList;

public class LobbyState implements Serializable {
    public ArrayList<String> playerList = new ArrayList<>();
    public ArrayList<String> chatMessages = new ArrayList<>();

    public void addMessage(String s)
    {
        chatMessages.add(s);
    }
    public void addPlayer(String s)
    {
        playerList.add(s);
    }
    public void removePlayer(String s)
    {
        playerList.remove(s);
    }
}


