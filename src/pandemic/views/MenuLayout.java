package pandemic.views;

import api.socketcomm.Server;
import client.ClientCommands;
import client.PandemicClient;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.Accordion;
import javafx.application.Platform;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javafx.util.Duration;
import pandemic.*;
import server.PandemicServer;
import server.ServerCommands;
import shared.GameState;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Thread.sleep;
import static javafx.scene.media.AudioClip.INDEFINITE;

public class MenuLayout extends Parent {
    PandemicServer pandemicServer;
    PandemicClient pandemicClient;
    //GUI frame;
    GameManager gameManager;
    boolean backButtonNotPressed = true;

    Label playerc1;
    Label playerc2 ;
    Label playerc3;
    Label playerc4;
    final AudioClip menuMusic = new AudioClip(new File("src/pandemic/resources/Music/AlienSwarmSoundtrackRybergBattle.wav").toURI().toString());
    final AudioClip startGameSound = new AudioClip(new File("src/pandemic/resources/Music/416385__fredzed__flash-and-a-bang.wav").toURI().toString());
    final AudioClip selectSound = new AudioClip(new File("src/pandemic/resources/Music/50559__broumbroum__sf3-sfx-menu-select-l.wav").toURI().toString());

    int currentNumOfPlayerConnected = 0;
	public MenuLayout(PandemicServer ps, PandemicClient pc) {
        pandemicServer = ps;
        pandemicClient = pc;
		CreateGameObjectData tracker = new CreateGameObjectData();

        VBox mainMenu = new VBox(10);
        VBox optionsMenu = new VBox(10);
        VBox createMenu = new VBox(10);
        VBox joinMenu = new VBox(10);
        VBox joinLobby = new VBox(10);
        VBox createLobby = new VBox();
        int centerX = 512;
        
        mainMenu.setTranslateX(centerX-125);
        mainMenu.setTranslateY(200);

        optionsMenu.setTranslateX(100);
        optionsMenu.setTranslateY(200);
        
        createMenu.setTranslateX(100);
        createMenu.setTranslateY(200);
        
        joinMenu.setTranslateX(100);
        joinMenu.setTranslateY(200);
        
        joinLobby.setTranslateX(100);
        joinLobby.setTranslateY(200);
        
        createLobby.setTranslateX(100);
        createLobby.setTranslateY(200);
        
        // how much to translate the menu by
        final int offset = centerX+200;

        optionsMenu.setTranslateX(offset);
        createMenu.setTranslateX(offset);
        joinMenu.setTranslateX(offset);
        joinLobby.setTranslateX(offset);
        createLobby.setTranslateX(offset);

        MenuButton btnCreate = new MenuButton("Create Game");
        btnCreate.setOnMouseClicked(event -> {
            selectSound.play();

        	getChildren().add(createMenu);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), mainMenu);
            tt.setToX(mainMenu.getTranslateX() - offset);

            TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), createMenu);
            tt1.setToX(mainMenu.getTranslateX());

            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
                getChildren().remove(mainMenu);
            });
        	
        });


        MenuButton btnOptions = new MenuButton("OPTIONS");
        btnOptions.setOnMouseClicked(event -> {
            selectSound.play();

            getChildren().add(optionsMenu);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), mainMenu);
            tt.setToX(mainMenu.getTranslateX() - offset);

            TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), optionsMenu);
            tt1.setToX(mainMenu.getTranslateX());

            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
                getChildren().remove(mainMenu);
            });
        });

        MenuButton btnExit = new MenuButton("EXIT");
        btnExit.setOnMouseClicked(event -> {
            selectSound.play();
        	System.exit(0);
        });
        
        /********************************************************************* 
         * ************Options Menu Stuff ********************************** *
         *********************************************************************/
        
        MenuButton btnOptionsBack = new MenuButton("BACK");
        btnOptionsBack.setOnMouseClicked(event -> {
            selectSound.play();

            getChildren().add(mainMenu);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), optionsMenu);
            tt.setToX(optionsMenu.getTranslateX() + offset);

            TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), mainMenu);
            tt1.setToX(optionsMenu.getTranslateX());

            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
                getChildren().remove(optionsMenu);
            });
        });
        MenuButton btnSound = new MenuButton("SOUND");
        MenuButton btnVideo = new MenuButton("VIDEO");
        // End of Options Menu after clicking Back


 
        /********************************************************************* 
         * ************Create Game Menu Stuff ****************************** *
         *********************************************************************/
        Label difficulty = new Label("Select Difficulty:");
        difficulty.setMinWidth(250);
        
        ChoiceBox<String> difficulties = new ChoiceBox<>(FXCollections.observableArrayList("Introductory", "Standard", "Heroic", "Legendary"));
        difficulties.setMinWidth(250);
        difficulties.setValue("Introductory");
        
        Label challenge = new Label("Select Challenges:");
        difficulty.setMinWidth(250);
        
        MenuButton btnMutation = new MenuButton("Mutation");
        btnMutation.setOnMouseClicked(event -> {
            selectSound.play();

        	if(btnMutation.bg.getFill() == Color.GREEN)
        	{
        		btnMutation.bg.setFill(Color.RED);
        		tracker.mutation = 0;
        	}
        	else
        	{
        		btnMutation.bg.setFill(Color.GREEN);
        		tracker.mutation = 1;
        	}
            
        });
        MenuButton btnVirulent = new MenuButton("Virulent");
        btnVirulent.setOnMouseClicked(event -> {
            selectSound.play();

        	if(btnVirulent.bg.getFill() == Color.GREEN)
        	{
        		btnVirulent.bg.setFill(Color.RED);
        		tracker.virulent = 0;
        	}
        	else
        	{
        		btnVirulent.bg.setFill(Color.GREEN);
        		tracker.virulent = 1;
        	}
            
        });

        MenuButton btnBioTerrorist = new MenuButton("BioTerrorist");
        btnBioTerrorist.setOnMouseClicked(event -> {
            selectSound.play();

        	if(btnBioTerrorist.bg.getFill() == Color.GREEN)
        	{
        		btnBioTerrorist.bg.setFill(Color.RED);
        		tracker.bioTerrorist = 0;
        	}
        	else
        	{
        		btnBioTerrorist.bg.setFill(Color.GREEN);
        		tracker.bioTerrorist = 1;
        	}
            
        });
        MenuButton btnCardShowing = new MenuButton("Can See All Players Cards");
        btnCardShowing.setOnMouseClicked(event -> {
            selectSound.play();

        	if(btnCardShowing.bg.getFill() == Color.GREEN)
        	{
        		btnCardShowing.bg.setFill(Color.RED);
        		tracker.seeCards = 0;
        	}
        	else
        	{
        		btnCardShowing.bg.setFill(Color.GREEN);
        		tracker.seeCards = 1;
        	}
          
        });
        MenuButton btnCreateGame = new MenuButton("Ready: Create Game Lobby!");
        btnCreateGame.setOnMouseClicked(event -> {
            selectSound.play();

        	if(difficulties.getValue().equals("Introductory"))
        	{
        		tracker.difficulty = 0;
        	}
        	else if (difficulties.getValue().equals("Standard"))
        	{
        		tracker.difficulty = 1;
        	}
        	else if (difficulties.getValue().equals("Heroic"))
        	{
        		tracker.difficulty = 2;
        	}
        	else if(difficulties.getValue().equals("Legendary"))
        	{
        		tracker.difficulty = 3;
        	}	
        	// print tracker for good measure
        	//magicalPrintingFunction(tracker);

            setUpCreateGame(pandemicServer, pandemicClient);

            // transition time
        	getChildren().add(createLobby);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), createMenu);
            tt.setToX(createMenu.getTranslateX() + offset);

            TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), createLobby);
            tt1.setToX(createMenu.getTranslateX());

            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
                getChildren().remove(createMenu);
            });
        	
        });
        
        MenuButton btnCreateBack = new MenuButton("BACK");
        btnCreateBack.setOnMouseClicked(event -> {
            selectSound.play();

            getChildren().add(mainMenu);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), createMenu);
            tt.setToX(createMenu.getTranslateX() + offset);

            TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), mainMenu);
            tt1.setToX(createMenu.getTranslateX());

            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
                getChildren().remove(createMenu);
            });
        });
        menuMusic.setCycleCount(INDEFINITE);
        menuMusic.play();

        /********************************************************************* 
         * ************Join Game Menu Stuff ******************************** *
         *********************************************************************/
        
        MenuButton btnJoin = new MenuButton("Join Game");
        btnJoin.setOnMouseClicked(event -> {
            selectSound.play();

        	getChildren().add(joinMenu);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), mainMenu);
            tt.setToX(mainMenu.getTranslateX() - offset);

            TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), joinMenu);
            tt1.setToX(mainMenu.getTranslateX());

            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
                getChildren().remove(mainMenu);
            });
        });
        Label enterIP = new Label("Enter IP Address i.e. 192.168.1.1");
        TextField ipAddress = new TextField("");
        ipAddress.setMinWidth(250);

        Label username = new Label("Enter Username");
        TextField usernameTF = new TextField("");
        usernameTF.setMinWidth(250);

        MenuButton btnJoinIP = new MenuButton("Join");
        btnJoinIP.setOnMouseClicked((MouseEvent event) -> {
            selectSound.play();

            String usernameTFText = usernameTF.getText();
            try {
                setPandemicClient(new PandemicClient(ipAddress.getText(), usernameTFText,  1301));

            } catch (IOException e) {
                e.printStackTrace();
            }


            System.out.println("Pandemic client: " + pandemicClient);




            GUI clientGUI = new GUI(usernameTFText, pandemicClient);
            pandemicClient.setGUI(clientGUI);
            System.out.println("Client GUI" + clientGUI.getUsername());
            //System.out.println("pandemicClient.getNumOfPlayersConnectedToServer(): " + pandemicClient.getNumOfPlayersConnectedToServer());

            pandemicClient.sendMessageToServer(ServerCommands.REGISTER_USERNAME.name(), usernameTFText);

            System.out.println("clientGUI in MenuLayout:" + clientGUI);
            System.out.println("clientGUI from pandemicClient: " + pandemicClient.getGui());
            /***********************************
        	 * Verify some network shit, i.e. the game actually does exist
        	 * Write some error if no good, else join the game via the code below
        	 * *************************************
        	 */
        	
            getChildren().add(joinLobby);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), joinMenu);
            tt.setToX(joinMenu.getTranslateX() + offset);

            TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), joinLobby);
            tt1.setToX(joinMenu.getTranslateX());

            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
                getChildren().remove(joinMenu);
            });

            while(clientGUI.getGameState() == null && backButtonNotPressed)
            {

//               if (clientGUI.getGameState() != null)
                    System.out.println(clientGUI.getGameState());

                //wait for a gameState or the user to backout from the lobby ? might work
            }
            startGameSound.play();
            menuMusic.stop();
            backButtonNotPressed = true;
            System.out.println("hello");

            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        System.out.println(pandemicServer + "test");
                        pandemicClient.getGui().setVisible(true);
                        pandemicClient.getGui().draw();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            getParent().setVisible(false);
        });
        
        MenuButton btnJoinBack = new MenuButton("BACK");
        btnJoinBack.setOnMouseClicked(event -> {
            selectSound.play();

            getChildren().add(mainMenu);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), joinMenu);
            tt.setToX(joinMenu.getTranslateX() + offset);

            TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), mainMenu);
            tt1.setToX(joinMenu.getTranslateX());

            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
                getChildren().remove(joinMenu);
            });
        });
        
        /********************************************************************* 
         * ************Join Lobby Stuff ************************************ *
         *********************************************************************/
        
        Label player1 = new Label("Player 1: The host get via network");
        Label player2 = new Label("Player 2: You");
        Label player3 = new Label("Player 3: The other get via network");
        Label player4 = new Label("Player 4: The other get via network");
        Label waitingToStart = new Label("WAITING TO START");
        
        MenuButton btnJoinLobbyBack = new MenuButton("BACK");
        btnJoinLobbyBack.setOnMouseClicked(event -> {
            selectSound.play();

            backButtonNotPressed = false;
            getChildren().add(mainMenu);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), joinLobby);
            tt.setToX(joinLobby.getTranslateX() + offset);

            TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), mainMenu);
            tt1.setToX(joinLobby.getTranslateX());

            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
                getChildren().remove(joinLobby);
        });
        });
        
        
        /********************************************************************* 
         * ************Create Lobby Stuff ********************************** *
         *********************************************************************/
        
        playerc1 = new Label("Player 1: You");
        playerc2 = new Label("Player 2: waiting to connect");
        playerc3 = new Label("Player 3: waiting to connect");
        playerc4 = new Label("Player 4: waiting to connect");
        //thread here

        MenuButton btnCreateGameNow = new MenuButton("Start Game");
        btnCreateGameNow.setOnMouseClicked(event -> {
        this.setVisible(false);
        	// Actually start the game
            startGameSound.play();
            menuMusic.stop();


            GameState gameStateTest = gameManager.getGame().generateCondensedGameState();
            //frame = new GUI("sdfsd", pandemicServer, gameStateTest);
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        System.out.println(pandemicServer + "test");
                      //  if (pandemicClient.getGui() == null) return;
                        pandemicClient.getGui().setVisible(true);
                        pandemicClient.getGui().draw();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            getParent().setVisible(false);

            pandemicServer.sendMessageToClients(ClientCommands.RECEIVE_UPDATED_GAMESTATE.name(), gameManager.getGame().generateCondensedGameState());
        });



        MenuButton btnCreateLobbyBack = new MenuButton("BACK");
        btnCreateLobbyBack.setOnMouseClicked(event -> {
            selectSound.play();

            getChildren().add(mainMenu);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), createLobby);
            tt.setToX(createLobby.getTranslateX() + offset);

            TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), mainMenu);
            tt1.setToX(createLobby.getTranslateX());

            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
                getChildren().remove(createLobby);
            });
        });


        mainMenu.getChildren().addAll(btnCreate, btnJoin, btnOptions, btnExit);
        optionsMenu.getChildren().addAll(btnSound, btnVideo, btnOptionsBack);
        createMenu.getChildren().addAll(difficulty, difficulties, challenge, btnMutation, btnVirulent, btnBioTerrorist, btnCardShowing, btnCreateGame, btnCreateBack);
        joinMenu.getChildren().addAll(username, usernameTF, enterIP, ipAddress, btnJoinIP, btnJoinBack);
        joinLobby.getChildren().addAll(player1, player2, player3, player4, waitingToStart, btnJoinLobbyBack);
        createLobby.getChildren().addAll(playerc1, playerc2, playerc3, playerc4, btnCreateGameNow, btnCreateLobbyBack);

        Rectangle bg = new Rectangle(1024, 768);
        bg.setFill(Color.GREY);
        bg.setOpacity(0.4);

        getChildren().addAll(bg, mainMenu);



    }
    private void setPandemicClient(PandemicClient c)
    {
        pandemicClient = c;
    }

    public void updateCreateLabel(String newPlayerName) {
	    //TODO
        if (newPlayerName.equals("host")) return;

        Platform.runLater(() -> {
            if (playerc2.getText().toLowerCase().contains("waiting to connect"))
                playerc2.setText("Player 2: " + newPlayerName);
            else if (playerc3.getText().toLowerCase().contains("waiting to connect"))
                playerc3.setText("Player 3: " + newPlayerName);
            else if (playerc4.getText().toLowerCase().contains("waiting to connect"))
                playerc4.setText("Player 4: " + newPlayerName);
        });
    }

	public void setUpCreateGame(PandemicServer s, PandemicClient c)
    {
        try {
            //User hostUser = new User("HOST", "kjsheofh", "127.0.0.1");
            //Player hostPlayer = new Player(hostUser);
            gameManager =  new GameManager(3, 6, ChallengeKind.OriginalBaseGame);

            gameManager.createNewGame();
            pandemicServer = new PandemicServer(this, gameManager.getGame(), 1301);
            System.out.println("in try");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pandemicClient = new PandemicClient("127.0.0.1", "host", 1301);
            GUI pandemicHostClientGUI = new GUI("host", pandemicClient);
            pandemicClient.setGUI(pandemicHostClientGUI);

            pandemicClient.sendMessageToServer(ServerCommands.REGISTER_USERNAME.name(), "host");

        } catch (IOException e) {
            e.printStackTrace();
        }
        serverThing serverT = new serverThing(s, c);
        Runtime.getRuntime().addShutdownHook(serverT);
    }

	public void magicalPrintingFunction(CreateGameObjectData tracker)
	{
	DateFormat dateFormat = new SimpleDateFormat("HH mm ss");
	Date date = new Date();
	File parent = new File("C:\\Users\\G\\eclipse-workspace\\pandemic\\bin\\temp\\");
	parent.mkdirs();
    File file = new File("C:\\Users\\G\\eclipse-workspace\\pandemic\\bin\\temp\\" + dateFormat.format(date) + ".cfg");
    String data = Integer.toString(tracker.difficulty) + "\r\n" + Integer.toString(tracker.mutation) + "\r\n" +
    Integer.toString(tracker.virulent) + "\r\n" +
    		Integer.toString(tracker.bioTerrorist) + "\r\n" + Integer.toString(tracker.seeCards);
    FileWriter fr = null;
    try {
    	file.createNewFile();

        fr = new FileWriter(file);
        fr.write(data);
    } catch (IOException e) {
        e.printStackTrace();
    }finally{
        //close resources
        try {
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

private class serverThing extends Thread {
	    PandemicServer server;
	    PandemicClient client;
	    serverThing(PandemicServer s, PandemicClient c)
        {
            server = s;
            client = c;
        }
	    public void run()
        {
            System.out.println("HELLO");
            System.out.println("HELLO");
            System.out.println("HELLO");
            System.out.println("HELLO");
            System.out.println("HELLO");
            if(server != null)
                server.close();
            if(client != null)
                client.close();
        }
}
 private class LobbyLabelUpdater extends Thread {

	    public LobbyLabelUpdater(Label l1, Label l2, Label l3, Label l4) {

        }

        public void run() {

        }

 }

	private static class MenuButton extends StackPane {
        private Text text;
        Rectangle bg;
        public MenuButton(String s) {
            text = new Text(s);
            text.getFont();
			text.setFont(Font.font(20));
            text.setFill(Color.WHITE);

            bg = new Rectangle(250, 30);
            bg.setOpacity(0.6);
            bg.setEffect(new GaussianBlur(4.5));
            bg.setFill(Color.BLACK);

            setAlignment(Pos.CENTER_LEFT);
            setRotate(-0.7);
            getChildren().addAll(bg, text);

            setOnMouseEntered(event -> {
            	if(bg.getFill() != Color.GREEN && bg.getFill() != Color.RED)
				{
					bg.setTranslateX(10);
					text.setTranslateX(10);
					bg.setFill(Color.WHITE);
					text.setFill(Color.BLACK);
            	}
            });

            setOnMouseExited(event -> {
				if (bg.getFill() != Color.GREEN && bg.getFill() != Color.RED) {
					bg.setTranslateX(0);
					text.setTranslateX(0);
					bg.setFill(Color.BLACK);
					text.setFill(Color.WHITE);
				}
			});

            DropShadow drop = new DropShadow(50, Color.WHITE);
            drop.setInput(new Glow());

            setOnMousePressed(event -> setEffect(drop));
            setOnMouseReleased(event -> setEffect(null));
        }
        void setBGColor(Color c)
        {
        	bg.setFill(c);
        }
        Color getBGColor()
        {
        	return (Color) bg.getFill();
        }
    }
}
	
	
    

