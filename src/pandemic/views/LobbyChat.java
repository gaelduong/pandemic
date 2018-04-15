package pandemic.views;

import client.ClientCommands;
import client.PandemicClient;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import server.ServerCommands;

import java.io.Serializable;

public class LobbyChat
{
    public LobbyState getState() {
        return state;
    }

    // some sort of gui shit that has player list and chat box
    LobbyState state = new LobbyState();
    Label playerList = new Label();
    Label chatLabel = new Label(); // TODO: make not label
    TextField chatField = new TextField();

    public LobbyChat(VBox box)
    {
        box.getChildren().addAll(playerList, chatLabel, chatField);
        chatField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getCode());
                if (event.getCode().equals(KeyCode.ENTER)) {

                    sendMessage(chatField.getText());
                }
            }
        });
    }

    public void sendMessage(String message)
    {
        System.out.println("sendMessage");
        PandemicClient client = MenuLayout.getInstance().getPandemicClient();
        // make new lobby state
        state.addMessage(message);
        client.sendMessageToServer(ServerCommands.CLIENT_UPDATE_LOBBBY.name(), state);
    }

    public void update(LobbyState newState)
    {

        this.state = newState;

        // update labels
        StringBuilder players = new StringBuilder();
        for (int i = 0; i < state.playerList.size(); i++) {
            players.append("Player ").append(i + 1).append(" ").append(state.playerList.get(i)).append("\n");
        }

        // update chat box
        StringBuilder chatText = new StringBuilder();
        for (String message : state.chatMessages)
            chatText.append(message).append("\n");

        Platform.runLater(() -> {
            playerList.setText(players.toString());

            // chat box gui?
            chatLabel.setText(chatText.toString());
        });
    }
}