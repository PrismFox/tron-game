package view.inputHandler;

import controller.playercontrol.IPlayerController;
import controller.playercontrol.PlayerControllerMovementImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

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
        //TODO ArrayList mit der KeyMapping muss hier noch irgendwie rein
        //keyMapping ist die Liste aller validen eingaben, also alle "pfeil" tasten der
        //einzelnen Player
        ArrayList<String> keyMapping = new ArrayList<>();

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
