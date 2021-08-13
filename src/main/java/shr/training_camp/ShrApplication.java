package shr.training_camp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shr.training_camp.core.config.Config;
import shr.training_camp.core.config.GameConfig;

@SpringBootApplication
public class ShrApplication /*implements CommandLineRunner*/ {

    @Autowired
    private GameConfig gameConfig;
    @Autowired
    private Config config;
    private static final Logger log = LoggerFactory.getLogger(ShrApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ShrApplication.class, args);
    }

}
