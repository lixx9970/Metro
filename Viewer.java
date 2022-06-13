package comp1110.ass2.gui;

import comp1110.ass2.Coordinate;
import comp1110.ass2.Dragger;
import comp1110.ass2.Metro;
import comp1110.ass2.Piece;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * A very simple viewer for piece placements in the Metro game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */
public class Viewer extends Application {
    /* board layout */
    private static final int SQUARE_SIZE = 70;
    private static final int VIEWER_WIDTH = 1024;
    private static final int VIEWER_HEIGHT = 768;

    private static final String URI_BASE = "assets/";

    public static boolean isRefreshClicked = false;

    private final Group root = new Group();
    private final Group mainMenuRoot = new Group();

    private final Group controls = new Group();
    private final Group game = new Group();
    private final Group playerLabel = new Group();

    private ArrayList<Dragger> draggerArray = new ArrayList<>(60);
    private Dragger[][] gridDraggers = new Dragger[9][9];

    private TextField textField;
    private TextField playerNumber;

    GridPane gameGrid = new GridPane();

    VBox scores = new VBox();
    HBox hand_tile_box = new HBox();


    //String to represent the overall sequencePlacement so far on the board
    String overall = "";
    //String to represent all the tiles that kept in players' hands.
    String all_hands = "";
    ArrayList<String> allHandsArray = new ArrayList<>();

    ArrayList<ArrayList<String>> playerHandTileArray = new ArrayList<>();
    ArrayList<ImageView> playerHandImage = new ArrayList<>();


    int numberOfPlayers = 0;
    int numberofAI = 0;
    ArrayList<String> playerOrder = new ArrayList<>();
    String currentPlayer = "";

    double mouseXOffset;
    double mouseYOffset;

    ImageView drawnTileImage;
    String drawnTileString = "";

    ImageView anotherTileImage;
    String anotherTileString = "";

    List<ImageView> currentPlayerHandImages = new ArrayList<>();
    List<String> currentPlayerHandStrings = new ArrayList<>();

    Dragger highlighted;

    Button drawTileButton;
    Button drawAnotherButton;
    Button playHandButton;


    /**
     * Draw a basic layout of stations and central stations on the game board
     *
     * @author Zihao Li
     */

    void drawBasicLayout() {
        System.out.println("[debug] drawBasicLayout called");

        //Draw the cover picture on the top left corner
        Image corner = new Image(Viewer.class.getResource(Viewer.URI_BASE + "tile_back_cover.jpg").toString(), SQUARE_SIZE, SQUARE_SIZE, false, false);
        gameGrid.add(new ImageView(corner), 0, 0);

        int j = 1;

        //Draw all the top edge stations
        for (int i = 8; i > 0; i = i - 1) {
            Image station = new Image(Viewer.class.getResource(Viewer.URI_BASE + "station" + String.valueOf(i) + ".jpg").toString(), SQUARE_SIZE, SQUARE_SIZE, false, false);
            ImageView IV = new ImageView(station);
            IV.setRotate(IV.getRotate() + 180);
            gameGrid.add(IV, j, 0);
            j++;
        }

        // adds a dragger rectangle in every spot except middle station locations.
        for (int col = 1; col < 9; col++) {
            for (int row = 1; row < 9; row++) {
                if (col == 4 && row == 5 || col == 4 && row == 4 || col == 5 && row == 4 || col == 5 && row == 5) {
                    continue;
                } else {
                    Dragger d = new Dragger(SQUARE_SIZE);
                    d.StoreAndTransferCoordinate(col, row);
                    draggerArray.add(d);
                    gridDraggers[d.x_cord][d.y_cord] = d;
                    gameGrid.add(d, col, row);
                }
            }
        }

        //Draw the cover picture on the bottom left corner
        gameGrid.add(new ImageView(corner), 9, 0);

        int k = 1;

        //Draw all the bottom edge stations
        for (int i = 9; i < 17; i++) {
            Image station = new Image(Viewer.class.getResource(Viewer.URI_BASE + "station" + String.valueOf(i) + ".jpg").toString(), SQUARE_SIZE, SQUARE_SIZE, false, false);
            ImageView IV = new ImageView(station);
            IV.setRotate(IV.getRotate() + 90);
            gameGrid.add(IV, 0, k);
            k++;
        }

        //Draw the cover picture on the top right corner
        gameGrid.add(new ImageView(corner), 0, 9);

        int l = 1;

        //Draw all the right edge stations
        for (int i = 17; i < 25; i++) {
            Image station = new Image(Viewer.class.getResource(Viewer.URI_BASE + "station" + String.valueOf(i) + ".jpg").toString(), SQUARE_SIZE, SQUARE_SIZE, false, false);
            gameGrid.add(new ImageView(station), l, 9);
            l++;
        }

        //Draw the cover picture on the bottom right corner
        gameGrid.add(new ImageView(corner), 9, 9);

        int m = 1;

        //Draw all the left edge stations
        for (int i = 32; i > 24; i = i - 1) {
            Image station = new Image(Viewer.class.getResource(Viewer.URI_BASE + "station" + String.valueOf(i) + ".jpg").toString(), SQUARE_SIZE, SQUARE_SIZE, false, false);
            ImageView IV = new ImageView(station);
            IV.setRotate(IV.getRotate() + 270);
            gameGrid.add(IV, 9, m);
            m++;
        }

        //Draw all the centre stations
        Image centre = new Image(Viewer.class.getResource(Viewer.URI_BASE + "centre_station.jpg").toString(), SQUARE_SIZE, SQUARE_SIZE, false, false);
        ImageView IV = new ImageView(centre);
        ImageView IV1 = new ImageView(centre);
        ImageView IV2 = new ImageView(centre);
        ImageView IV3 = new ImageView(centre);


        gameGrid.add(IV, 5, 4);
        IV1.setRotate(IV1.getRotate() + 90);
        gameGrid.add(IV1, 5, 5);
        IV2.setRotate(IV2.getRotate() + 180);
        gameGrid.add(IV2, 4, 5);
        IV3.setRotate(IV3.getRotate() + 270);
        gameGrid.add(IV3, 4, 4);


    }

    /**
     * Find the nearest dragger on the board when we drag the image around
     *
     * @param x A double value represents the x coordinate
     * @param y A double value represents the y coordinate
     * @return closest - The closest dragger object
     * @author Zihao Li
     */

    Dragger findNearestDragger(double x, double y) {

        if (draggerArray.size() > 0) {
            Dragger closest = null;
            double closestDistance = Double.MAX_VALUE;

            for (Dragger dragger : draggerArray) {
                double dist = getDistanceToDragger(dragger, x, y);

                if (dist < closestDistance) {
                    closestDistance = dist;
                    closest = dragger;
                }
            }
            return closest;
        } else {
            throw new IllegalStateException("Trying to find the closest dragger with the empty dragger list");
        }

    }

    /**
     * Find the distance between dragging image and dragger objects on the board
     *
     * @param d A dragger object
     * @param x A double value represents the x coordinate
     * @param y A double value represents the y coordinate
     * @return An double value which represents the distance between dragging image and draggers on the board
     * @author Zihao Li
     */

    private double getDistanceToDragger(Dragger d, double x, double y) {
        double dx = d.getLayoutX() - x;
        double dy = d.getLayoutY() - y;

        return Math.sqrt(dx*dx + dy*dy);
    }


    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement A valid placement string
     * @author Zihao Li
     */
    void makePlacement(String placement) {

        //Zihao: At First we need to create a class called GPiece which extends the ImageView. That's used to place a particular playing piece
        //which will be added to the board. We can implement this by calling setImage, and passed the argument of piece_type String to
        //obtain the corresponding image of adding piece. Then we will create another class called Piece which extends GPiece and that will
        //set up the initial placement of all pieces according to the parameter 'placement'. To accomplish that, we will also need a constructor
        //called Piece with parameters Piece_type (which is a string of four characters) and position of that piece (which are two numbers representing
        //x and y coordinates of the piece). After the completion of above two, we can start the implementation of makePlacement. First, we will
        //erase the previous drawing of Piece object by calling Piece.getChildren().clear(), Then we will use for loop to iterate String placement to
        //add every new object Piece with their own type (like aaaa) and position (like 1,2)




        String[] pieceArray = Metro.splitPlacement(placement, Piece.Pieces.pieceAndPlacement);

        //draw the given tile on each position given in the complete placementSequence
        for (int i = 0; i < pieceArray.length; i++) {
            String piece = pieceArray[i].substring(0, 4);
            Coordinate c = Coordinate.GetCord(pieceArray[i].substring(4, 6));
            Image p = new Image(Viewer.class.getResource(Viewer.URI_BASE + piece + ".jpg").toString(), SQUARE_SIZE, SQUARE_SIZE, false, false);
            // remove the old first
            int row = Integer.parseInt(pieceArray[i].substring(4,5));
            int col = Integer.parseInt(pieceArray[i].substring(5,6));
            Dragger old = gridDraggers[row][col];
            if (old != null)
                gameGrid.getChildren().remove(old);
            gameGrid.add(new ImageView(p), c.x, c.y);
        }

    }

    /**
     * @author Bharath Kulkarni
     */

    private void makeSimpleAIMoves() {
        for (int i = 0; i < numberofAI; i++) {
            String AITile = Metro.drawFromDeck(overall, all_hands); // AI draws new tile
            String AITileAndPlacement = Metro.generateMove(overall, AITile, numberOfPlayers); // a move is generated from drawn tile
            Image AImove = new Image(Viewer.class.getResource(Viewer.URI_BASE + AITile + ".jpg").toString(), SQUARE_SIZE, SQUARE_SIZE, false, false);
            Coordinate c = Coordinate.GetCord(AITileAndPlacement.substring(4, 6));
            overall = overall + AITileAndPlacement;
            gameGrid.add(new ImageView(AImove), c.x, c.y);
        }
    }

    /**
     * @author Bharath Kulkarni
     */
    private void updateScores() {
        int totalPlayers = numberOfPlayers + numberofAI;
        int[] scoreList = Metro.getScore(overall, totalPlayers);
        scores.getChildren().clear();
        for (String player : playerOrder) {
            Label label = new Label("Player " + player + " score = " + scoreList[playerOrder.indexOf(player)]);
            scores.getChildren().add(label);

        }
    }

    /** update hand and also handle the rotating part
     * @author Bharath Kulkarni (rewrite by Jiyuan Chen, in order to work with rotating and fixed bugs)
     */

    private void updateHands() {
        List<String> currentHand = playerHandTileArray.get(playerOrder.indexOf(currentPlayer));
        for(ImageView imageView: currentPlayerHandImages) {
            root.getChildren().remove(imageView);
        }
        if (currentHand.isEmpty()) {
            playHandButton.setDisable(true);
        }
        // if player's hand is not empty
        else {
            playHandButton.setDisable(false);
            currentPlayerHandStrings = playerHandTileArray.get(playerOrder.indexOf(currentPlayer));
            currentPlayerHandImages.clear();
            int startPosX = 800;
            for(int i = 0; i < currentPlayerHandStrings.size(); i++) {
                String tile = currentPlayerHandStrings.get(i);
                ImageView imageView = new ImageView(loadImage(tile));
                imageView.setLayoutX(startPosX);
                imageView.setLayoutY(600);
                startPosX += SQUARE_SIZE + 10;
                root.getChildren().add(imageView);
                currentPlayerHandImages.add(imageView);

                final int imageIdx = i;
                imageView.setOnContextMenuRequested(e -> {
                    String originTile = currentPlayerHandStrings.get(imageIdx);
                    String rotatedStr = originTile.substring(3) + originTile.substring(0, 3);
                    imageView.setImage(loadImage(rotatedStr));
                    currentPlayerHandStrings.set(imageIdx, rotatedStr);
                });

            }
        }


    }

    /** Initialize the player array when there is no AI in the game
     * @author Bharath Kulkarni
     */

    private void initialisePlayerHands() {
        for (String player : playerOrder) {
            // if player is not an AI
            if (!player.contains("AI")) {
                playerHandTileArray.add(new ArrayList<>());
            }
        }
    }

    /** Action Listener for the draggable tile image
     * @param tempTileImage The selected tile image
     * @param tempTile The string of selected tile
     *
     * @author Bharath Kulkarni & Zihao Li & Jiyuan Chen
     */

    private void mouseReleasedAction(ImageView tempTileImage, String tempTile) {


        Dragger d = findNearestDragger(tempTileImage.getLayoutX(), tempTileImage.getLayoutY());
        String newSequence = overall + tempTile + d.CoordToString(d);

        //if this is the first tile being placed
        if (overall.isEmpty()) {
            // check that the tile placement is valid
            if (Metro.isPlacementSequenceValid(tempTile + d.CoordToString(d))) {
                tempTileImage.setLayoutX(d.getLayoutX());
                tempTileImage.setLayoutY(d.getLayoutY());
                overall = newSequence;

                // if drawAnother button was pressed, it is disabled. This is to stop players drawing more than one tile.
                if (tempTileImage == anotherTileImage) {
                    drawTileButton.setDisable(false);
                }
                // current player is updated to next player
                currentPlayer = playerOrder.get(playerOrder.indexOf(currentPlayer) + 1);

                // updated player's name is displayed on screen.
                Label newLabel = new Label("Player " + currentPlayer);
                newLabel.setLayoutX(VIEWER_WIDTH - 200);
                newLabel.setLayoutY(VIEWER_HEIGHT - 700);
                playerLabel.getChildren().clear();
                playerLabel.getChildren().add(newLabel);

                updateScores();
                updateHands();


            } else {
                // if placement is invalid, snap the tile back to starting position
                if(tempTileImage == drawnTileImage) {
                    tempTileImage.setLayoutX(VIEWER_WIDTH - 200);
                    tempTileImage.setLayoutY(VIEWER_HEIGHT - 580);
                }else if (tempTileImage == anotherTileImage) {
                    tempTileImage.setLayoutX(VIEWER_WIDTH - 200);
                    tempTileImage.setLayoutY(VIEWER_HEIGHT - 480);
                }else{
                    tempTileImage.setLayoutX(VIEWER_WIDTH - 800);
                    tempTileImage.setLayoutY(VIEWER_HEIGHT - 600);
                }
            }
        } else {
            // checking validity of placement sequence
            if (Metro.isPlacementSequenceValid(newSequence)) {
                tempTileImage.setLayoutX(d.getLayoutX());
                tempTileImage.setLayoutY(d.getLayoutY());
                overall = newSequence;
                root.getChildren().remove(tempTileImage);

                // enable draw tile button for next player
                if (tempTileImage == anotherTileImage) {
                    drawTileButton.setDisable(false);
                }

                // updates current player to next player.
                // if current player is the last player in playerOrder, next player is first player in playerOrder.
                if (playerOrder.indexOf(currentPlayer) == playerOrder.size() - 1) {
                    currentPlayer = playerOrder.get(0);
                } else {
                    currentPlayer = playerOrder.get(playerOrder.indexOf(currentPlayer) + 1);
                }

                // updated player's name is displayed on screen.
                Label newLabel = new Label("Player " + currentPlayer);
                newLabel.setLayoutX(VIEWER_WIDTH - 200);
                newLabel.setLayoutY(VIEWER_HEIGHT - 700);
                playerLabel.getChildren().clear();
                playerLabel.getChildren().add(newLabel);
                updateScores();

                makePlacement(overall);


            } else {
                tempTileImage.setLayoutX(VIEWER_WIDTH - 200);
                tempTileImage.setLayoutY(VIEWER_HEIGHT - 580);
            }
        }


        // Bharath:
        // last tiles are for AI to place.
        if (overall.length() / 6 == (60 - numberofAI)) {
            drawTileButton.setDisable(true);
        }


        //Bharath:
        //simple AI that places a valid tile randomly after player.
        if (currentPlayer.contains("AI")) { //
            // makes moves for all AI players
            makeSimpleAIMoves();

            //updates current player to first player in playerOrder
            currentPlayer = playerOrder.get(0);

            // new label to show its first player's turn
            Label newLabel = new Label("Player " + currentPlayer);
            newLabel.setLayoutX(VIEWER_WIDTH - 200);
            newLabel.setLayoutY(VIEWER_HEIGHT - 700);
            playerLabel.getChildren().clear();
            playerLabel.getChildren().add(newLabel);
            updateScores();

            // at the end of the AI's turn(s), enable draw tile button for next player.
            if (tempTileImage == anotherTileImage) {
                drawTileButton.setDisable(false);
            }


        }
        updateHands();
        System.out.println(playerHandTileArray);
        System.out.println(overall);
    }

    /** Action event been trigger when the draw Another button is pressed
     *
     */
    private void drawAnotherAction() {
        updateHands();
        // once player draws another tile, the first tile is removed from the screen.
        root.getChildren().remove(drawnTileImage);

        //If player choose to draw another, he has to place that tile
        drawTileButton.setDisable(true);
        drawAnotherButton.setDisable(true);
        playHandButton.setDisable(true);
        all_hands = all_hands + drawnTileString;




        // generate another tile
        anotherTileString = Metro.drawFromDeck(overall, all_hands);
        anotherTileImage = new ImageView(loadImage(anotherTileString));
        anotherTileImage.setOnContextMenuRequested(e -> {
            anotherTileString = anotherTileString.substring(3) + anotherTileString.substring(0, 3);
            anotherTileImage.setImage(loadImage(anotherTileString));
        });

        anotherTileImage.setLayoutX(VIEWER_WIDTH - 200);
        anotherTileImage.setLayoutY(VIEWER_HEIGHT - 480);
        root.getChildren().add(anotherTileImage);

        anotherTileImage.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) { // ignore right click
                return;
            }
            anotherTileImage.toFront();
            mouseXOffset = anotherTileImage.getLayoutX() - event.getSceneX();
            mouseYOffset = anotherTileImage.getLayoutY() - event.getSceneY();

        });

        anotherTileImage.setOnMouseDragged(event -> {
            if (event.isSecondaryButtonDown()) { // ignore right click
                return;
            }
            anotherTileImage.setLayoutX(event.getSceneX() + mouseXOffset);
            anotherTileImage.setLayoutY(event.getSceneY() + mouseYOffset);

            updateHighlightTileOnDrag(anotherTileImage);
        });

        anotherTileImage.setOnMouseReleased(event -> {
            if (event.isSecondaryButtonDown() || event.isDragDetect()) { // ignore right click
                return;
            }
            resetOldHighlightedTile();
            drawAnotherButton.setDisable(true);

            updateHands();

            mouseReleasedAction(anotherTileImage, anotherTileString);

            if (playerHandTileArray.get(playerOrder.indexOf(currentPlayer)).isEmpty()) {
                playHandButton.setDisable(true);
            } else {
                playHandButton.setDisable(false);
            }


        });

        int order = playerOrder.indexOf(currentPlayer);
        List<String> currentPlayerHands = playerHandTileArray.get(order);
        if (currentPlayerHands.contains(drawnTileString)) {
            currentPlayerHands.add(anotherTileString);
        } else {
            currentPlayerHands.add(drawnTileString);
        }
        updateHands();
    }

    /** Action event trigger by pressing the Draw Tile button
     *
     */
    private void drawTileAction() {
        updateHands();
        drawTileButton.setDisable(true); // cant click draw more than one time.
        // after drawing the first tile, the draw another button is enabled.
        drawAnotherButton.setDisable(false);

        drawnTileString = Metro.drawFromDeck(overall, all_hands);
        drawnTileImage = new ImageView(loadImage(drawnTileString));
        drawnTileImage.setOnContextMenuRequested(e -> {
            drawnTileString = drawnTileString.substring(3) + drawnTileString.substring(0, 3);
            drawnTileImage.setImage(loadImage(drawnTileString));
        });
        drawnTileImage.setLayoutX(VIEWER_WIDTH - 200);
        drawnTileImage.setLayoutY(VIEWER_HEIGHT - 580);
        root.getChildren().add(drawnTileImage);

        // updated Zihao's code - Bharath. added offset variables.
        drawnTileImage.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) { // ignore right click
                return;
            }
            drawnTileImage.toFront();
            mouseXOffset = drawnTileImage.getLayoutX() - event.getSceneX();
            mouseYOffset = drawnTileImage.getLayoutY() - event.getSceneY();

        });
        // updated Zihao's code - Bharath. Added offset so the tile doesnt snap to mouse centre.
        drawnTileImage.setOnMouseDragged(event -> {
            if (event.isSecondaryButtonDown()) { // ignore right click
                return;
            }

            drawnTileImage.setLayoutX(event.getSceneX() + mouseXOffset);
            drawnTileImage.setLayoutY(event.getSceneY() + mouseYOffset);

            updateHighlightTileOnDrag(drawnTileImage);
        });

        drawnTileImage.setOnMouseReleased(event -> {
            if (event.isSecondaryButtonDown() || event.isDragDetect()) { // ignore right click
                return;
            }

            resetOldHighlightedTile();
            // at the start of new player's turn, disable the draw another button.
            // Once the player presses draw tile button, draw another button will be enabled.
            drawAnotherButton.setDisable(true);

            if (playerHandTileArray.get(playerOrder.indexOf(currentPlayer)).isEmpty()) {
                playHandButton.setDisable(true);
            } else {
                playHandButton.setDisable(false);
            }

            updateHands();
            drawTileButton.setDisable(false);

            mouseReleasedAction(drawnTileImage, drawnTileString);

        });

    }

    /**
     * Load a tile image by the tile string
     * @param tileString A tile string
     * @return Image object representing the tile
     * @author Jiyuan Chen
     */
    private Image loadImage(String tileString) {
        return new Image(Viewer.class.getResource(Viewer.URI_BASE + tileString + ".jpg").toString(),
                SQUARE_SIZE, SQUARE_SIZE, false, false);
    }

    /**
     * Used to handle the highlight of tile when you dragging tile into the board
     *
     * @param drawnTileImage A tile image
     * @return Return the highlight of closest block of a tile
     * @author Jiyuan Chen
     */
    private void updateHighlightTileOnDrag(ImageView drawnTileImage) {
        Dragger closest = findNearestDragger(drawnTileImage.getLayoutX(), drawnTileImage.getLayoutY());

        if (highlighted != closest) {
            if (highlighted != null) {
                highlighted.setFill(Color.BLACK);
            }
            closest.setFill(Color.GREEN);
            highlighted = closest;
        }
    }

    /**
     * Reset the highlight tile
     *
     * @author Jiyuan Chen
     */
    private void resetOldHighlightedTile() {
        if (highlighted != null) {
            highlighted.setFill(Color.BLACK);
        }
    }


    /**
     * rewrite version of playerHandPlay method
     * used to handle play hand button
     *
     * @author Jiyuan Chen
     */
    private void playHandAction() {
        updateHands();
        drawAnotherButton.setDisable(true);
        drawTileButton.setDisable(true);
        bindHandTileEvents();
    }

    /**
     * control the play hand action, and handle the multiple tiles in hand
     *
     * @author Jiyuan Chen
     */
    private void bindHandTileEvents() {
        for(int i = 0; i < currentPlayerHandImages.size(); i++) {
            ImageView view = currentPlayerHandImages.get(i);
            final int imageIndex = i;

            view.setOnMousePressed(event -> {
                if (event.isSecondaryButtonDown()) { // ignore right click
                    return;
                }
                view.toFront();
                mouseXOffset = view.getLayoutX() - event.getSceneX();
                mouseYOffset = view.getLayoutY() - event.getSceneY();
            });

            view.setOnMouseDragged(event -> {
                if (event.isSecondaryButtonDown()) { // ignore right click
                    return;
                }
                view.setLayoutX(event.getSceneX() + mouseXOffset);
                view.setLayoutY(event.getSceneY() + mouseYOffset);

                updateHighlightTileOnDrag(view);
            });

            view.setOnMouseReleased(event -> {
                if (event.isSecondaryButtonDown() || event.isDragDetect()) { // ignore right click
                    return;
                }
                resetOldHighlightedTile();
                root.getChildren().remove(view);
                drawAnotherButton.setDisable(true);

                // removing played hand tile from player's hand
                int order = playerOrder.indexOf(currentPlayer);
                playerHandTileArray.get(order).remove(imageIndex);

                // remove hand tile from all_hands
                if (currentPlayerHandStrings.size() > imageIndex) {
                    int index = all_hands.indexOf(currentPlayerHandStrings.get(imageIndex));
                    if (index >= 0) {
                        all_hands = all_hands.substring(0, index) + all_hands.substring(index + 4);
                    }
                }

                drawTileButton.setDisable(false);

                if (currentPlayerHandStrings.size() > imageIndex) {
                    mouseReleasedAction(view, currentPlayerHandStrings.get(imageIndex));
                }

                updateHands();
            });

        }

    }

    /**
     * Create a basic text field for input and a refresh button.
     *
     * @author Zihao Li and Bharath Kulkarno
     */
    private void makeControls() {

        //To set the basic layout for the game board
        drawBasicLayout();


        controls.getChildren().add(gameGrid);

        //Draw Tile button
        drawTileButton = new Button("Draw Tile");
        drawTileButton.setLayoutX(VIEWER_WIDTH - 310);
        drawTileButton.setLayoutY(VIEWER_HEIGHT - 560);
        controls.getChildren().add(drawTileButton);

        //Draw Another button
        drawAnotherButton = new Button("Draw Another");
        drawAnotherButton.setLayoutX(VIEWER_WIDTH - 310);
        drawAnotherButton.setLayoutY(VIEWER_HEIGHT - 460);
        controls.getChildren().add(drawAnotherButton);

        //play hand button
        playHandButton = new Button("Play Hand");
        playHandButton.setLayoutX(710);
        playHandButton.setLayoutY(600);
        controls.getChildren().add(playHandButton);


        drawAnotherButton.setOnAction(e -> {
            drawAnotherAction();
        });

        drawTileButton.setOnAction(e -> {
            drawTileAction();
        });

        playHandButton.setOnAction(e -> {
            playHandAction();
        });


        Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);


        Button button = new Button("Refresh");
        button.setOnAction(refreshEvent -> {
            makePlacement(textField.getText());
            textField.clear();

        });


        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(50);
        hb.setLayoutY(VIEWER_HEIGHT - 50);


        controls.getChildren().add(hb);

    }

    /**
     * @param primaryStage
     * @author Bharath Kulkarni.
     */
    @Override
    public void start(Stage primaryStage) {


        Label label1 = new Label("How many players? (2-6)");
        Label label2 = new Label("AI");
        ChoiceBox<Integer> players = new ChoiceBox<>();
        players.getItems().addAll(2, 3, 4, 5, 6);
        players.setValue(2);

        ChoiceBox<Integer> ai = new ChoiceBox<>();
        ai.getItems().addAll(0, 1, 2, 3, 4);
        ai.setValue(0);


        Button button = new Button("Start");
        button.setOnAction(event -> {
            if (players.getValue() + ai.getValue() <= 6) { // total players must be less than 6.
                numberOfPlayers = players.getValue();
                numberofAI = ai.getValue();
                for (int i = 0; i < numberOfPlayers; i++) {
                    playerOrder.add(String.valueOf(i + 1));
                }
                if (numberofAI > 0) {
                    for (int i = 0; i < numberofAI; i++) {
                        playerOrder.add("AI" + (i + 1));
                    }
                }

                Scene game = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
                makeControls();

                root.getChildren().add(controls);
                root.getChildren().add(playerLabel);


                scores.setLayoutX(800);
                scores.setLayoutY(400);

                root.getChildren().add(scores);
                updateScores();


                initialisePlayerHands();


                currentPlayer = "1";
                Label label = new Label("Player " + currentPlayer);
                label.setLayoutX(VIEWER_WIDTH - 200);
                label.setLayoutY(VIEWER_HEIGHT - 700);
                playerLabel.getChildren().add(label);

                // draw another and play hand buttons are disabled when starting the game.
                drawAnotherButton.setDisable(true);
                playHandButton.setDisable(true);

                primaryStage.setScene(game);
                primaryStage.show();
            }


        });


        primaryStage.setTitle("FocusGame Viewer");
        Scene main_menu = new Scene(mainMenuRoot, VIEWER_WIDTH, VIEWER_HEIGHT);

        HBox hb = new HBox();
        hb.getChildren().addAll(label1, players, label2, ai, button);
        hb.setSpacing(10);
        hb.setLayoutX(VIEWER_WIDTH - 700);
        hb.setLayoutY(VIEWER_HEIGHT - 400);

        mainMenuRoot.getChildren().add(hb);


        primaryStage.setScene(main_menu);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
// java --module-path "C:\Program Files\Java\javafx-sdk-11.0.2\lib" --add-modules=javafx.controls,javafx.fxml,javafx.media -jar game.jar