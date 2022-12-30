package view.inputHandler;

import controller.playercontrol.IPlayerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

@Component
public class InputHandler implements KeyListener {
    @Autowired
    private IPlayerController playerController;

    public String input;

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        input = "" +key;

        if(checkInputString(input)){
            forwardInput(input);
        }else{
            System.out.println("invalid Key Input. Key typed: " + key);
        }
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        input = "" +key;

        if(checkInputString(input)){
            forwardInput(input);
        }else{
            System.out.println("invalid Key Input. Key pressed: " + key);
        }

    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        char key = e.getKeyChar();
        input = "" +key;

        if(checkInputString(input)){
            forwardInput(input);
        }else{
            System.out.println("invalid Key Input. Key released: " + key);
        }
    }

    public boolean checkInputString(String keyInput){
        //get the validKeys from the controller and check if the
        //user input is one of the valid keys

        playerController.getValidKeys();

        List<String> keyMapping = playerController.getValidKeys();

        boolean validInput = false;

        if(keyMapping.contains(keyInput)){
            validInput = true;
        }

        return validInput;
    }

    public void forwardInput(String keyInput){
        //onKeyPress from Controller

        playerController.onKeyPress(keyInput);

    }

}
