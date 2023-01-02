package de.haw.vsp.tron.view.inputHandler;

import de.haw.vsp.tron.controller.playercontrol.IPlayerInputManager;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public
class InputHandler implements EventHandler<KeyEvent> {

    @Autowired
    private IPlayerInputManager playerController;

    String input;

    @Override
    public void handle(KeyEvent event) {
        if (KeyEvent.KEY_PRESSED.equals(event.getEventType())) {

            //soll hier dann der aufruf zum controller? oder in der unteren methode?
            //valid Keys?
            KeyCode key = event.getCode();
            input = key.getName();

            if(checkInputString(input)){
                forwardInput(input);
            }else{
                System.out.println("invalid Key Input. Key typed: " + key);
            }
        }
    }



    public boolean checkInputString(String keyInput){
        //get the validKeys from the controller and check if the
        //user input is one of the valid keys

        //TODO keys irgendwo speichern
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
