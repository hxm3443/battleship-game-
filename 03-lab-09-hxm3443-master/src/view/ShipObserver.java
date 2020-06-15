package view;

import controller.Observer;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

/**
 * This class is responsible for observing the ship. Each time a cell of the ship gets hit, this observer class gets
 * notified and changes the color of that cell, letting the player know that one of the ships' cells has been hit.
 * FileName: ShipObserver.java
 *
 * @author Himani Munshi
 */
public class ShipObserver implements Observer<Integer> {
    private ArrayList<Label> shipButtons;

    /**
     * Constructor
     *
     * @param shipButtons list of labels containing all the cell locations for the ships
     */
    public ShipObserver(ArrayList<Label> shipButtons) {
        this.shipButtons = shipButtons;
    }

    /**
     * This method is responsible for updating the specific cell that has been hit to some other color (in this case black).
     *
     * @param shipPosition represents the nth cell that has been hit for a given ship
     */
    @Override
    public void update(Integer shipPosition) {
        Label label = shipButtons.get(shipPosition);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                label.setGraphic(createCircle(Color.BLACK));
            }
        });

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
}
