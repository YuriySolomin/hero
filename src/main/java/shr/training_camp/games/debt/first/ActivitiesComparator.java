package shr.training_camp.games.debt.first;

import lombok.Data;
import lombok.NoArgsConstructor;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.API.run;

import java.util.Map;

@Data
@NoArgsConstructor
public class ActivitiesComparator {

    private Map<Long, Double> fActivities;
    private Map<Long, Double> heroActivities;
    private double factor;

    public double calcFactor() {
        double factor = 0.00;
        double penalty = 0.00;
        for (Map.Entry<Long, Double> virtual: fActivities.entrySet()) {
            double heroValue = heroActivities.get(virtual.getKey());
            factor += heroValue/virtual.getValue();
            penalty += getPercent(heroValue, virtual.getValue());

        }
        penalty = Math.min(0.995, penalty);
        return factor*(1 - penalty);
    }

    private double getPercent(final double heroValue, final double fValue) {
        return Match(heroValue / fValue).of(
                Case($(x -> x >= 0.5), 0.00),
                Case($(x -> (x < 0.5) && (x >= 0.3)), 0.05),
                Case($(x -> (x < 0.3) && (x >= 0.2)), 0.15),
                Case($(x -> (x < 0.1) && (x >= 0.1)), 0.2),
                Case($(x -> x < 0.1), 0.5)
        );

    }


}
