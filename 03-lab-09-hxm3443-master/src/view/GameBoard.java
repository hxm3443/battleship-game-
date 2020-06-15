package view;

import controller.BattleShip;
import controller.Observer;
import controller.ShipData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Location;

import java.util.ArrayList;
import java.util.List;


/**
 * This class is responsible for setting up the GUI which contains targetPane, Console, and a PlayerPane. This class
 * extends Application to make use of javafx components.
 * File Name: GameBoard.java
 *
 * @author Himani Munshi
 */
public class GameBoard extends Application implements Observer<ShipData> {
    private BattleShip battleShipController;
    private Label console;
    private Label[][] playerButtons;
    private Boolean[][] wasAHit;

    /**
     * Constructor
     */
    public GameBoard() {
        console = new Label();
        playerButtons = new Label[BattleShip.NUM_ROWS][BattleShip.NUM_COLS];
        wasAHit = new Boolean[BattleShip.NUM_ROWS][BattleShip.NUM_COLS];
    }

    /**
     * This method is responsible for: 1) calling the appropriate constructor from controller.Battleship class depending
     * on the number of arguments.
     * 2) creating a BorderPane which holds two gridPanes (one containing buttons and the
     * other containing labels) and a label for displaying messages on the console.
     * 3) calling the method to add all the ships to the model.
     *
     * @param stage object of Stage which holds the scene and is responsible for the GUI that's displayed to the user
     */
    public void start(Stage stage) {
        Parameters params = getParameters();
        List<String> args = params.getRaw();
        MyConsoleWriter myConsoleWriter = new MyConsoleWriter(console);
        if (args.size() == 0) {
            battleShipController = new BattleShip(myConsoleWriter);
        } else if (args.size() == 1) {
            battleShipController = new BattleShip(myConsoleWriter, Integer.parseInt(args.get(0)));
        } else if (args.size() == 2) {
            battleShipController = new BattleShip(myConsoleWriter, args.get(0), Integer.parseInt(args.get(1)));
        } else {
            System.out.println("Invalid input!");
            System.exit(0);
        }
        BorderPane border = new BorderPane();

        GridPane targetPane = targetPane();
        border.setTop(targetPane);

        border.setCenter(console);

        GridPane playerPane = playerPane();
        border.setBottom(playerPane);

        Scene scene = new Scene(border);
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();

        battleShipController.addShips(this);

    }

    /**
     * This method is responsible for creating the targetPane. The target pane is where one player tracks the attacks on
     * the other player. This targetPane is a grid of buttons. Later in this method, we also create an event handler using
     * anonymous method and call the attack method from controller.Battleship to attack a specific button.
     *
     * @return gridPane containing buttons in the form of a 10 by 10 grid
     */
    public GridPane targetPane() {
        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setStyle("-fx-padding: 10;-fx-background-color: black;");

        for (int row = 0; row < BattleShip.NUM_ROWS; row++) {
            RowConstraints rowIndex = new RowConstraints(30, 30, Double.MAX_VALUE);
            rowIndex.setFillHeight(true);
            rowIndex.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(rowIndex);

        }

        for (int col = 0; col < BattleShip.NUM_COLS; col++) {
            ColumnConstraints colIndex = new ColumnConstraints(30, 30, Double.MAX_VALUE);
            colIndex.setFillWidth(true);
            colIndex.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(colIndex);
        }

        for (int row = 0; row < BattleShip.NUM_ROWS; row++) {
            for (int col = 0; col < BattleShip.NUM_COLS; col++) {
                wasAHit[row][col] = false;
                Button button = new Button();
                button.setStyle("-fx-background-color: #add8e6");
                button.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setAlignment(Pos.CENTER);
                TargetButton targetButton = new TargetButton(button, battleShipController);
                Location loc = new Location(row, col);
                battleShipController.registerTarget(loc, targetButton);
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (battleShipController.isMyTurn() && !wasAHit[loc.getRow()][loc.getCol()]) {
                            battleShipController.attack(loc);
                            wasAHit[loc.getRow()][loc.getCol()] = true;
                        }
                    }
                });
                grid.add(button, col, row);
            }
        }
        //grid.setGridLinesVisible(true);
        return grid;
    }

    /**
     * This method is responsible for creating the playerPane. The playerPane is where the player's ships are placed. The
     * playerPane is a grid of labels.
     *
     * @return gridPane of labels in the form of a 10 by 10 grid
     */
    public GridPane playerPane() {
        GridPane grid = new GridPane();

        grid.setVgap(5);
        grid.setHgap(5);
        grid.setStyle("-fx-padding: 10;-fx-background-color: black;");

        for (int row = 0; row < BattleShip.NUM_ROWS; row++) {
            RowConstraints rowIndex = new RowConstraints(30, 30, Double.MAX_VALUE);
            rowIndex.setFillHeight(true);
            rowIndex.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(rowIndex);
        }


        for (int col = 0; col < BattleShip.NUM_COLS; col++) {
            ColumnConstraints colIndex = new ColumnConstraints(30, 30, Double.MAX_VALUE);
            colIndex.setFillWidth(true);
            colIndex.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(colIndex);
        }

        for (int row = 0; row < BattleShip.NUM_ROWS; row++) {
            for (int col = 0; col < BattleShip.NUM_COLS; col++) {
                Label label = new Label();
                label.setStyle("-fx-background-color: #9CB071");


                label.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
                label.setAlignment(Pos.CENTER);
                playerButtons[row][col] = label;
                grid.add(label, col, row);
            }
        }

        //grid.setGridLinesVisible(true);
        return grid;
    }

    /**
     * This method is responsible for storing the player's ships in a list called shipButtons (based on starting row and
     * col values, orientation, and length of ship). If the ship is not sunk, it registers the shipObserver to the ship
     * in the model. It also creates a circle for each cell (or label) on the playerPane if a ship is located in that cell.
     *
     * @param ship object of ShipData which holds information about the ship (such as starting row and col values,
     *             orientation, length, ship's status etc.)
     */
    @Override
    public void update(ShipData ship) {
        Location loc = ship.getBowLocation();
        ShipData.Orientation ort = ship.getOrientation();
        ArrayList<Label> shipButtons = new ArrayList<>();

        int row = loc.getRow();
        int col = loc.getCol();
        int shipLength = ship.getSize();

        if (ort.equals(ShipData.Orientation.HORIZONTAL)) {
            for (int j = col; j < (shipLength + col); j++) {
                shipButtons.add(playerButtons[row][j]);
            }
        } else if (ort.equals(ShipData.Orientation.VERTICAL)) {
            for (int i = row; i < (shipLength + row); i++) {
                shipButtons.add(playerButtons[i][col]);
            }
        }

        setBackground(shipButtons, ort, Color.GRAY);

        if (!ship.sunk()) {
            ShipObserver shipObserver = new ShipObserver(shipButtons);
            ((model.ShipModel) ship).register(shipObserver);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < shipButtons.size(); i++) {
                        shipButtons.get(i).setGraphic(createCircle(Color.YELLOW));
                    }
                }
            });

        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < shipButtons.size(); i++) {
                        setBackground(shipButtons, ort, Color.MAROON);

                    }
                }
            });
        }
    }

    /**
     * This methods creates a circle. The class Circle is a part of the javaFx components.
     *
     * @param color specified by the programmer to fill the circle
     * @return an object of Circle which holds properties (such as a specific radius and a color)
     */
    private Circle createCircle(Color color) {
        Circle circle = new Circle();

        circle.setRadius(7);
        circle.setFill(color);

        return circle;
    }

    /**
     * This method is responsible for creating the cornerRadii of the labels so that the player knows which cell belongs
     * to which ship. It also sets the background color for the buttons.
     *
     * @param shipButtons list containing the labels for all the ship locations
     * @param ort         orientation of the ship (i.e. either horizontal or vertical)
     * @param color       specified by the programmer for highlighting that it is a special label since it contains a part of
     *                    the ship
     */
    private void setBackground(ArrayList<Label> shipButtons, ShipData.Orientation ort, Color color) {
        if (ort.equals(ShipData.Orientation.HORIZONTAL)) {
            for (int i = 0; i < shipButtons.size(); i++) {
                if (i == 0) {
                    shipButtons.get(i).setBackground(new Background(new BackgroundFill(color, new CornerRadii(10, 0, 0, 10, false), Insets.EMPTY)));
                } else if (i == shipButtons.size() - 1) {
                    shipButtons.get(i).setBackground(new Background(new BackgroundFill(color, new CornerRadii(0, 10, 10, 0, false), Insets.EMPTY)));
                } else {
                    shipButtons.get(i).setBackground(new Background(new BackgroundFill(color, new CornerRadii(0, 0, 0, 0, false), Insets.EMPTY)));
                }
            }
        } else if (ort.equals(ShipData.Orientation.VERTICAL)) {
            for (int i = 0; i < shipButtons.size(); i++) {
                if (i == 0) {
                    shipButtons.get(i).setBackground(new Background(new BackgroundFill(color, new CornerRadii(10, 10, 0, 0, false), Insets.EMPTY)));
                } else if (i == shipButtons.size() - 1) {
                    shipButtons.get(i).setBackground(new Background(new BackgroundFill(color, new CornerRadii(0, 0, 10, 10, false), Insets.EMPTY)));
                } else {
                    shipButtons.get(i).setBackground(new Background(new BackgroundFill(color, new CornerRadii(0, 0, 0, 0, false), Insets.EMPTY)));
                }
            }
        }
    }

    /**
     * This method is called if the user quits before the game is finished.
     */
    @Override
    public void stop() {
        battleShipController.endEarly();
    }
}
