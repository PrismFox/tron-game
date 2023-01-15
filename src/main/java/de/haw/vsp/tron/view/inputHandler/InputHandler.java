package de.haw.vsp.tron.view.inputHandler;

import de.haw.vsp.tron.controller.playercontrol.IPlayerInputManager;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public
class InputHandler implements EventHandler<KeyEvent>, IInputHandler {

    @Autowired
    private IPlayerInputManager playerController;

    private String input;

    private List<String> keyMapping;

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


    @Override
    public boolean checkInputString(String keyInput){
        //get the validKeys from the controller and check if the
        //user input is one of the valid keys
        
        if(keyMapping == null) {
            keyMapping = Arrays.asList(playerController.getValidKeys()).stream().map(k -> {
                System.out.println("{--DEBUG} InputHandler keyInput = " + keyInput);
                String[] seperatedPrefix = k.split("\\|");
                System.out.println("{--DEBUG} InputHandler keyInput without prefix = " + seperatedPrefix[seperatedPrefix.length-1]);

                return seperatedPrefix[seperatedPrefix.length -1];
            }).collect(Collectors.toList());
        }
        boolean validInput = false;

        if(keyMapping.contains(keyInput)){
            validInput = true;
        }

        return validInput;
    }

    @Override
    public void forwardInput(String keyInput){
        //onKeyPress from Controller

        playerController.onKeyPress(keyInput);

    }
}
