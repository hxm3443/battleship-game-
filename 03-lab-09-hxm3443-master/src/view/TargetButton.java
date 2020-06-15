package view;

import controller.BattleShip;
import controller.Observer;
import javafx.application.Platform;
import javafx.scene.control.Button;

/**
 * This class is responsible for observing the targetButtons. Each time a player attacks the other player's pane, this
 * class gets notified and depending on whether the attack was a hit or a miss it changes the color of the button on the
 * targetPane.
 * FileName: TargetButton.java
 *
 * @author Himani Munshi
 */
public class TargetButton implements Observer<Boolean> {
    private Button button;
    private BattleShip battleShipController;

    /**
     * Constructor
     *
     * @param button               the specific button that has been clicked
     * @param battleShipController object of Battleship class
     */
    public TargetButton(Button button, BattleShip battleShipController) {
        this.button = button;
        this.battleShipController = battleShipController;
    }

    /**
     * This method updates the color of the button that was clicked on the targetPane and depending on whether the attack
     * was a hit or a miss it changes the color of the button on the targetPane. In this case, color gets changed to white
     * if it was a miss, otherwise darkBlue. It also calls the done method to let the controller know that the player's
     * turn is over.
     *
     * @param hit boolean variable which reveals whether the attack was a hit or a miss
     */
    @Override
    public void update(Boolean hit) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (hit) {
                    button.setStyle("-fx-background-color: #5E7D7E");
                } else {
                    button.setStyle("-fx-background-color: #FFFFFF");
                }
                battleShipController.done();
            }
        });

    }
}
