package de.haw.vsp.tron.middleware.middlewareconfig;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
@Data
public class MiddlwareConfig implements IMiddlewareConfig {
    private static final String ENVVAR_NAME = "TRON_MIDDLEWARE_CONFIG";
    private static final String DEFAULT_PATH = "configMiddleware.properties";

    private String nameServerIP = "127.0.0.1";
    private int nameServerPort = 5502;

    public MiddlwareConfig() {
        String configPath = System.getenv().getOrDefault(ENVVAR_NAME, DEFAULT_PATH);
        loadConfigFile(configPath);
    }

    private void loadConfigFile(String path) {
        nameServerIP = "127.0.0.1";
        nameServerPort = 5502;

        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream(path)) {
            prop.load(input);
            this.nameServerIP = prop.getProperty("nameServerIP", nameServerIP);
            this.nameServerPort = Integer.parseInt(prop.getProperty("nameServerPort", String.valueOf(nameServerPort)));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}