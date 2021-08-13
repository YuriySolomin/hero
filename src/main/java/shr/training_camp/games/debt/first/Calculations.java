package shr.training_camp.games.debt.first;

import shr.training_camp.util.MathUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Calculations {

    public static long getGamePeriod(long days) {
        return MathUtils.getSumOfAP(1.0, 1.0, days).longValue();
    }

    public static long getGamePeriod(LocalDate startDate, LocalDate endDate, int firstElement) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return MathUtils.getSumOfAP(firstElement, 1.0, days).longValue();
    }
}
