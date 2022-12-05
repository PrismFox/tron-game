package view.inputHandler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class InputHandler implements KeyListener {

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
        //TODO methode vom Controller muss aufgerufen werden
        //hier soll eine Methode vom Controller aufgerufen werden, um den
        //player input weiterzugeben.

    }

}
