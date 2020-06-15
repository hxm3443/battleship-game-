package view;

import controller.ConsoleWriter;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * This class is responsible for writing to the console. Whenever an event occurs (such as when a battleship sinks, etc.)
 * the controller notifies view and sends appropriate message to be displayed on the console.
 * FileName: MyConsoleWriter.java
 *
 * @author Himani Munshi
 */
public class MyConsoleWriter implements ConsoleWriter {
    private Label console;

    /**
     * Constructor
     *
     * @param console label (where the message gets displayed)
     */
    public MyConsoleWriter(Label console) {
        this.console = console;
    }

    /**
     * This method is responsible for writing the message to the console using setText method.
     *
     * @param text The message to write
     */
    @Override
    public void write(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                console.setText(text);
            }
        });
    }
}
