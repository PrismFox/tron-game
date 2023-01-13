package de.haw.vsp.tron.startGame;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("de.haw.vsp.tron")
public class StartGameModelController {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(TronConfig.class);

    }
}
