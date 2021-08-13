package shr.training_camp.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = { "shr.training_camp.*" })
@Configuration
@EnableConfigurationProperties({ GameConfig.class })
@EnableAutoConfiguration
public class TestConfiguration {

    @Autowired
    private GameConfig gameConfig;

    public GameConfig getGameConfig() {
        return gameConfig;
    }
}
