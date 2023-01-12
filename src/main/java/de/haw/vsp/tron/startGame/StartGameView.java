package de.haw.vsp.tron.startGame;

import de.haw.vsp.tron.view.screens.ScreenHandler;
import javafx.application.Application;
import javafx.stage.Stage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan("de.haw.vsp.tron")
public class StartGameView extends Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(TronConfig.class);

        launch(args);
    }


    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        //ScreenHandler screenHandler = new ScreenHandler(primaryStage);

        ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
        ScreenHandler screenHandler = appCtx.getBean("screenHandler", ScreenHandler.class);
        screenHandler.init(primaryStage);

    }

}
