package shr.training_camp.core.config;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import shr.training_camp.core.config.factory.YamlPropertySourceFactory;
import shr.training_camp.core.model.config.Game;

@Configuration
@EnableConfigurationProperties
@PropertySource(
        value = "classpath:application.yml"
 //       factory = YamlPropertySourceFactory.class
)
@ConfigurationProperties("game-config")
@Data
public class GameConfig {
    private String game;
    private String testValues;
    private String resourcePlayers;
    private String resourcePlayersActivities;

}
