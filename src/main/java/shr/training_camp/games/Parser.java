package shr.training_camp.games;

import lombok.Data;
import shr.training_camp.util.RandomUtils;


public class Parser {

    public static Double englishValue;
    public static Double physicValue;
    public static Double generalValue;

    // ToDo add method. it takes string such as Ramdom - E1,G1,P1 and value such as E,G,P and returns random percent

    public static Double getValueForActivity(final String resource, final String value) {
        String[] parsing = resource.split(",");
        double reValue = 10*Double.parseDouble(parsing[0].replaceAll("\\D", ""));
        return ((reValue-10) + (reValue + 20)*Math.random()) / 100.0 ;
    }
}
