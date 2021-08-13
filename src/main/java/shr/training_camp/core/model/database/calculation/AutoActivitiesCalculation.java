package shr.training_camp.core.model.database.calculation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shr.training_camp.core.model.database.Activity;
import shr.training_camp.core.model.database.AutoActivity;
import shr.training_camp.core.model.database.GameActivityLog;
import shr.training_camp.core.model.database.GrowGroup;
import shr.training_camp.core.model.database.PlayersGroups;
import shr.training_camp.core.model.database.addition.GameActivityStatistics;
import shr.training_camp.core.model.database.addition.HeroActivityResult;
import shr.training_camp.core.model.database.addition.PlayerActivityStatistics;
import shr.training_camp.core.model.database.generators.PlayerAutoActivityInfo;
import shr.training_camp.sevice.interfaces.*;
import shr.training_camp.util.MathUtils;
import shr.training_camp.util.RandomUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import static io.vavr.API.*;

@Component
public class AutoActivitiesCalculation {

    @Autowired
    private IAutoActivityService<AutoActivity> autoActivityService;

    @Autowired
    private IPlayerActivityService playerActivityService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private IActivityService<Activity> activityService;

    @Autowired
    private IGrowGroupService<GrowGroup> growGroupService;

    @Autowired
    private IPlayerGroupService<PlayersGroups> playerGroupService;

    @Autowired
    private IGameActivityLogService gameActivityLogService;

    public AutoActivity getActivityValueForPlayer(final Long idPlayer, final Long idActivity, LocalDate actualDate) {
        AutoActivity autoActivity = autoActivityService.findAutoActivityForPlayer(idPlayer, idActivity);
        LocalDate startDate = Objects.isNull(autoActivity.getSaveDate()) ? autoActivity.getStartDate() : autoActivity.getSaveDate();
        long days = ChronoUnit.DAYS.between(startDate, actualDate);
        int firstDaysCount = Objects.nonNull(autoActivity.getDaysFactor()) ? autoActivity.getDaysFactor() : 1;
        long realPeriod = MathUtils.getSumOfAP(firstDaysCount, 1.0, days).longValue();
        double randomValue = 0.00;
        double bonusValue = 0.00;
        //double baseValue = MathUtils.getSumOfAP(autoActivity.getFirstElement(), autoActivity.getRatio(), realPeriod);
        double summary = Objects.isNull(autoActivity.getSummary()) ? 0.00 : autoActivity.getSummary();
        //for (int i=1; i<= realPeriod; i++) {
        for (int i = 1; i <= days; i++) {
            for (int j = 1; j < i + firstDaysCount; j++) {
                summary += autoActivity.getFirstElement();
                if (Objects.nonNull(autoActivity.getRandomBonus()) && autoActivity.getRandomBonus().intValue() != 0) {
                    randomValue = RandomUtils.getRandomForAAByDefaultScheme(autoActivity.getRandomBonus().intValue());
                }
                if (Objects.nonNull(autoActivity.getBonus()) && autoActivity.getBonus().intValue() != 0) {
                    bonusValue = autoActivity.getBonus() / 100;
                }
                summary += autoActivity.getFirstElement() * (0 + randomValue);
                summary += autoActivity.getFirstElement() * (0 + bonusValue);
            }
            autoActivity.setFirstElement(autoActivity.getFirstElement() + autoActivity.getRatio());
        }
        autoActivity.setSummary(summary);
        autoActivity.setSaveDate(actualDate);
        autoActivity.setDaysFactor(firstDaysCount + (int) days);
        return autoActivity;
    }

    public List<GameActivityLog> getRealPlayerActivities(Long idGroup, Long idPlayer, LocalDate startDate, LocalDate saveDate, LocalDate endDate) {
        List<GameActivityLog> result = new ArrayList<>();
        GrowGroup currentGroup = growGroupService.getGrowGroupById(idGroup);
        List<PlayerActivityStatistics> playerActivityStatisticsList = playerActivityService.getRealPlayerActivityStatistic(idPlayer, startDate, saveDate, endDate);
        for (PlayerActivityStatistics playerActivityStatistics : playerActivityStatisticsList) {
            GameActivityLog gameActivityLog = new GameActivityLog();
            gameActivityLog.setIdActivity(playerActivityStatistics.getIdActivity());
            gameActivityLog.setIdPlayer(idPlayer);
            gameActivityLog.setIdGroup(idGroup);
            gameActivityLog.setValue(playerActivityStatistics.getQuantity() * playerActivityStatistics.getPeriod());
            gameActivityLog.setDaysFactor(playerActivityStatistics.getPeriod().intValue());
            gameActivityLog.setFirstElement(0D);
            gameActivityLog.setRandomValue(0D);
            gameActivityLog.setBonusValue(0D);
            gameActivityLog.setSaveDate(playerActivityStatistics.getStartDate());
            result.add(gameActivityLog);
        }
        return result;
    }

    public PlayerAutoActivityInfo getGameActivityLogForPlayer(Long idGroup, Long idPlayer, LocalDate actualDate) {
        List<AutoActivity> playersAA = autoActivityService.getAllAutoActivitiesByPlayerId(idPlayer);
        List<GameActivityLog> gameActivityLogList = new ArrayList<>();
        PlayerAutoActivityInfo playerAutoActivityInfo = new PlayerAutoActivityInfo();
        for (AutoActivity autoActivity : playersAA) {
            LocalDate startDate = Objects.isNull(autoActivity.getSaveDate()) ? autoActivity.getStartDate() : autoActivity.getSaveDate();
            int firstDaysCount = Objects.nonNull(autoActivity.getDaysFactor()) ? autoActivity.getDaysFactor() : 1;
            int daysFactor = firstDaysCount + 1;
            long days = ChronoUnit.DAYS.between(startDate, actualDate);
            double firstElement = autoActivity.getFirstElement();
            double summary = 0.00;
            for (int i = 1; i <= days; i++) {
                GameActivityLog gameActivityLog = new GameActivityLog();
                LocalDate currentDate = startDate.plusDays(i - 1);
                double resultValue = 0.00;
                double randomValue = 0.00;
                double bonusValue = 0.00;
                for (int j = 1; j < i + firstDaysCount; j++) {
                    resultValue += firstElement;
                    if (Objects.nonNull(autoActivity.getRandomBonus()) && autoActivity.getRandomBonus().intValue() != 0) {
                        randomValue += firstElement * RandomUtils.getRandomForAAByDefaultScheme(autoActivity.getRandomBonus().intValue());
                        //resultValue += randomValue;
                    }
                    if (Objects.nonNull(autoActivity.getBonus()) && autoActivity.getBonus().intValue() != 0) {
                        bonusValue += firstElement * autoActivity.getBonus() / 100;
                        //resultValue += bonusValue;
                    }
                }
                resultValue += randomValue + bonusValue;
                BigDecimal resultValueBD = new BigDecimal(Double.toString(resultValue));
                resultValueBD = resultValueBD.setScale(4, RoundingMode.HALF_UP);
                BigDecimal randomValueBD = new BigDecimal(Double.toString(randomValue));
                randomValueBD = randomValueBD.setScale(4, RoundingMode.HALF_UP);
                BigDecimal bonusValueBD = new BigDecimal(Double.toString(bonusValue));
                bonusValueBD = bonusValueBD.setScale(4, RoundingMode.HALF_UP);
                firstElement += autoActivity.getRatio();
                BigDecimal firstElementBD = new BigDecimal(Double.toString(firstElement));
                firstElementBD = firstElementBD.setScale(4, RoundingMode.HALF_UP);
                BigDecimal nextElementBD = new BigDecimal(Double.toString(firstElement + autoActivity.getRatio()));
                nextElementBD = nextElementBD.setScale(4, RoundingMode.HALF_UP);

                gameActivityLog.setIdGroup(idGroup);
                gameActivityLog.setIdPlayer(idPlayer);
                gameActivityLog.setIdActivity(autoActivity.getIdActivity());
                gameActivityLog.setSaveDate(currentDate);
                gameActivityLog.setValue(resultValueBD.doubleValue());
                gameActivityLog.setRandomValue(randomValueBD.doubleValue());
                gameActivityLog.setBonusValue(bonusValueBD.doubleValue());
                gameActivityLog.setFirstElement(firstElementBD.doubleValue());
                gameActivityLog.setDaysFactor(daysFactor);
                gameActivityLog.setNextElement(nextElementBD.doubleValue());
                gameActivityLogList.add(gameActivityLog);
                summary += resultValue;
                daysFactor++;

            }
            playerAutoActivityInfo.setDaysFactor(firstDaysCount);
            playerAutoActivityInfo.setIdPlayer(idPlayer);
            playerAutoActivityInfo.setGameActivityLogList(gameActivityLogList);
        }
        return playerAutoActivityInfo;
    }

    public List<ComparePlayers> createSortedList(Long idGroup, LocalDate convertDate) {
        List<ComparePlayers> source = createComparePlayersList(idGroup, convertDate);
        return source.stream().sorted(Comparator.comparingDouble(ComparePlayers::getGrowFactor).reversed())
                .collect(Collectors.toList());

    }

    public Map<Long, Double> createCompareActivitiesPlayersList(Long idGroup, LocalDate convertDate) {
        List<ComparePlayers> source = createComparePlayersList(idGroup, convertDate);
        List<ComparePlayers> sortedList = source.stream().sorted(Comparator.comparingDouble(ComparePlayers::getGrowFactor).reversed())
                .filter(c -> c.getActiveStatus() > 0).collect(Collectors.toList());

        ComparePlayers lastPlayer = sortedList.get(sortedList.size()-1);

        Map<Long, Double> result = new HashMap<>();
        Map<Long, Double> result2 = new HashMap<>();
        for (ComparePlayers comparePlayers : source) {
            for (Map.Entry<Long, Double> map : comparePlayers.getActivityValues().entrySet()) {
                if (result.containsKey(map.getKey())) {
                    Double value = result.get(map.getKey()) + map.getValue();
                    result.put(map.getKey(), value);
                } else {
                    result.put(map.getKey(), map.getValue());
                }
            }
        }
        PlayersGroups heroPlayerGroups = playerGroupService.findPlayersGroupsByGroupId(idGroup, 1).get(0);
        List<GameActivityStatistics> heroList = gameActivityLogService.getGameActivityStatistics(convertDate, heroPlayerGroups.getIdPlayer());
        Map<Long, Double> heroMap = new HashMap<>();
        for (GameActivityStatistics hero : heroList) {
            heroMap.put(hero.getIdActivity(), hero.getValue());
        }
        for (Map.Entry<Long, Double> map : result.entrySet()) {
            double factor = heroMap.get(map.getKey()) * source.size();
            result2.put(map.getKey(), map.getValue() / factor);
        }

        return result2;
    }

    public List<ComparePlayers> createComparePlayersList(Long idGroup, LocalDate convertDate) {
        PlayersGroups heroPlayerGroups = playerGroupService.findPlayersGroupsByGroupId(idGroup, 1).get(0);
        List<GameActivityStatistics> heroList = gameActivityLogService.getGameActivityStatistics(convertDate, heroPlayerGroups.getIdPlayer());
        ComparePlayers heroPlayer = new ComparePlayers();
        heroPlayer.setCurrentDate(convertDate);
        heroPlayer.setHero(true);
        heroPlayer.setIdPlayer(heroPlayerGroups.getIdPlayer());
        heroPlayer.setPlayer(playerService.getPlayerById(heroPlayerGroups.getIdPlayer()));
        heroPlayer.setGrowFactor(1D);
        Map<Long, Double> heroMap = new HashMap<>();
        Map<String, Double> heroMap2 = new HashMap<>();
        for (GameActivityStatistics hero : heroList) {
            heroMap.put(hero.getIdActivity(), hero.getValue());
            heroMap2.put(activityService.getActivityById(hero.getIdActivity()).getName(), hero.getValue());
        }
        heroPlayer.setActivityValues(heroMap);
        heroPlayer.setActivityValueNames(heroMap2);

        List<PlayersGroups> playersGroupsList = playerGroupService.findPlayersGroupsByGroupId(idGroup, 0);
        List<ComparePlayers> resultCompareList = new ArrayList<>();
        for (PlayersGroups playersGroups : playersGroupsList) {
            ComparePlayers virtualPlayer = new ComparePlayers();

            Map<Long, Double> virtualMap = new HashMap<>();
            Map<String, Double> virtualNameMap = new HashMap<>();
            virtualPlayer.setHero(false);
            virtualPlayer.setCurrentDate(convertDate);
            virtualPlayer.setIdPlayer(playersGroups.getIdPlayer());
            virtualPlayer.setPlayer(playerService.getPlayerById(playersGroups.getIdPlayer()));
            virtualPlayer.setActiveStatus(playersGroups.getActiveStatus());
            for (GameActivityStatistics virtualStatistic : gameActivityLogService.getGameActivityStatistics(convertDate, playersGroups.getIdPlayer())) {
                virtualMap.put(virtualStatistic.getIdActivity(), virtualStatistic.getValue());
                virtualNameMap.put(activityService.getActivityById(virtualStatistic.getIdActivity()).getName(), virtualStatistic.getValue());
            }
            virtualPlayer.setActivityValues(virtualMap);
            virtualPlayer.setActivityValueNames(virtualNameMap);
            resultCompareList.add(virtualPlayer);
        }
        for (ComparePlayers comparePlayer : resultCompareList) {
            Double compareFactor = 0.00;
            Double factor2d = 1.000000;
            for (Map.Entry<Long, Double> compareMap : comparePlayer.getActivityValues().entrySet()) {
                Double virtualValue = compareMap.getValue();
                Double heroValue = heroPlayer.getActivityValues().get(compareMap.getKey());
                compareFactor += virtualValue / heroValue;
                Number factor2 = calcFactor2(virtualValue, heroValue);
                factor2d *= factor2.doubleValue();

            }
            Double average = compareFactor / 14.0;
            BigDecimal averageBD = new BigDecimal(Double.toString(average));
            averageBD = averageBD.setScale(6, RoundingMode.HALF_UP);
            comparePlayer.setGrowFactor(averageBD.doubleValue());
            BigDecimal factor2BD = new BigDecimal(Double.toString(factor2d));
            factor2BD = factor2BD.setScale(6, RoundingMode.HALF_UP);
            comparePlayer.setGrowFactor2(factor2BD.doubleValue());

        }

        //resultCompareList.stream().filter(x -> x.getGrowFactor2() <=1).collect(Collectors.toList());
        resultCompareList.add(heroPlayer);
        return resultCompareList;

    }

    public List<HeroActivityPlaces> getHeroActivityPlaces(Long idGroup, LocalDate convertDate) {
        List<ComparePlayers> allPlayers = createComparePlayersList(idGroup, convertDate);
        ComparePlayers heroPlayer = allPlayers.stream().filter(ComparePlayers::isHero).findFirst().get();
        List<ComparePlayers> virtualPlayers = allPlayers.stream().filter(c -> !c.isHero()).collect(Collectors.toList());
        List<HeroActivityPlaces> result = new ArrayList<>();
        for (Map.Entry<Long, Double> map: heroPlayer.getActivityValues().entrySet()) {
            HeroActivityPlaces heroActivityPlaces = new HeroActivityPlaces();
            long place = virtualPlayers.stream().filter(c -> c.getActivityValues().get(map.getKey()) > map.getValue()).count() + 1;
            heroActivityPlaces.setIdActivity(map.getKey());
            heroActivityPlaces.setActivity(activityService.getActivityById(map.getKey()));
            heroActivityPlaces.setPlace(place);
            heroActivityPlaces.setCompareDate(convertDate);
            double sum = virtualPlayers.stream().mapToDouble(c -> c.getActivityValues().get(map.getKey())).sum();
            double average = sum / virtualPlayers.size();
            heroActivityPlaces.setAverage(average);
            heroActivityPlaces.setHeroValue(map.getValue());
            heroActivityPlaces.setFactor(map.getValue() / average);
            result.add(heroActivityPlaces);
        }
        return result;
    }

    public List<Recommendations> getFewRecommendations(List<ComparePlayers> allPlayers, LocalDate startDate, int place, int period) {
        List<AutoActivity> autoActivities = autoActivityService.getAllAutoActivitiesByPlayerId(allPlayers.stream().filter(c -> !c.isHero()).findFirst().get().getIdPlayer());
        List<Recommendations> result = new ArrayList<>();
        ComparePlayers heroPlayer = allPlayers.stream().filter(ComparePlayers::isHero).findFirst().get();
        for (AutoActivity autoActivity : autoActivities) {
            List<ComparePlayers> aaPlayer = allPlayers.stream().filter(c -> !c.isHero())
                    .sorted(Comparator.comparingDouble((ToDoubleFunction<ComparePlayers>) c -> c.getActivityValues().get(autoActivity.getIdActivity())).reversed()).collect(Collectors.toList());
            ComparePlayers comparePlayer = aaPlayer.get(place-1);
            AutoActivity comparePlayerActivity = getActivityValueForPlayer(comparePlayer.getIdPlayer(), autoActivity.getIdActivity(), startDate.plusDays(period));
            Double actualSum = comparePlayer.getActivityValues().get(autoActivity.getIdActivity()) + comparePlayerActivity.getSummary();
            int daysFactor = comparePlayerActivity.getDaysFactor();
            double virtualPeriod = MathUtils.getSumOfAP(daysFactor - period, 1, period);
            double heroValue = heroPlayer.getActivityValues().get(autoActivity.getIdActivity());
            result.add(Recommendations.of(autoActivity.getIdActivity(),
                    activityService.getActivityById(autoActivity.getIdActivity()).getName(),
                    (actualSum - heroValue) / virtualPeriod,
                    place,
                    period));

        }
        return result;
    }

    public List<Recommendations> getFewRecommendations(Long idGroup, LocalDate startDate, int place, int period) {
        List<ComparePlayers> allPlayers = createComparePlayersList(idGroup, startDate);
        return getFewRecommendations(allPlayers, startDate, place, period);
    }

    public double getRecommendations(Long idGroup, LocalDate startDate, Long idActivity, int place, int period) {
        List<ComparePlayers> allPlayers = createComparePlayersList(idGroup, startDate).stream()
                .sorted(Comparator.comparingDouble((ToDoubleFunction<ComparePlayers>) c -> c.getActivityValues().get(idActivity)).reversed()).collect(Collectors.toList());
        ComparePlayers comparePlayer = allPlayers.get(place-1);
        AutoActivity comparePlayerActivity = getActivityValueForPlayer(comparePlayer.getIdPlayer(), idActivity, startDate.plusDays(period));
        Double oldSum = comparePlayer.getActivityValues().get(idActivity);
        Double actualSum = oldSum + comparePlayerActivity.getSummary();
        int daysFactor = comparePlayerActivity.getDaysFactor();
        double virtualPeriod = MathUtils.getSumOfAP(daysFactor - period, 1, period);
        double factorK = virtualPeriod / period;
        ComparePlayers heroPlayer = allPlayers.stream().filter(ComparePlayers::isHero).findFirst().get();
        double heroValue = heroPlayer.getActivityValues().get(idActivity);
        return (actualSum - heroValue) / virtualPeriod;
    }

    private Number calcFactor2(double virtualPlayer, double realPlayer) {
        double playerFactor = virtualPlayer / realPlayer;
        return Match(playerFactor).of(
                Case($(factor -> (factor == 1)), 1),
                Case($(factor -> (factor > 1 && factor <= 1.8)), 1.1),
                Case($(factor -> (factor < 1 && factor >= 1 / 1.8)), 1 / 1.1),
                Case($(factor -> (factor > 1.8 && factor <= 3.5)), 1.3),
                Case($(factor -> (factor < 1 / 1.8 && factor >= 1 / 3.5)), 1 / 1.3),
                Case($(factor -> (factor > 3.5 && factor <= 5)), 1.5),
                Case($(factor -> (factor < 1 / 3.5 && factor >= 1 / 5f)), 1 / 1.5),
                Case($(factor -> (factor > 5 && factor <= 7.5)), 1.75),
                Case($(factor -> (factor < 1 / 5f && factor >= 1 / 7.5)), 1 / 1.75),
                Case($(factor -> (factor > 7.5 && factor <= 10)), 2),
                Case($(factor -> (factor < 1 / 7.5 && factor >= 1 / 10f)), 1 / 2f),
                Case($(factor -> (factor > 10 && factor <= 15)), 2.5),
                Case($(factor -> (factor < 1 / 10f && factor >= 1 / 15f)), 1 / 2.5),
                Case($(factor -> (factor > 15 && factor <= 25)), 2.8),
                Case($(factor -> (factor < 1 / 15f && factor >= 1 / 25f)), 1 / 2.8),
                Case($(factor -> (factor > 25)), 3.5),
                Case($(factor -> (factor < 1 / 25f)), 1 / 3.5)
        );
    }

    public Map<Long, Double> getPlayersAutoActivitiesByDate(final Long idPlayer, final int shiftDays) {
        LocalDate resultDate = LocalDate.now().plusDays(shiftDays);
        Map<Long, Double> result = new HashMap<>();
        List<AutoActivity> autoActivities = autoActivityService.getAllAutoActivitiesByPlayerId(idPlayer);
        for (AutoActivity autoActivity : autoActivities) {
            long days = ChronoUnit.DAYS.between(autoActivity.getStartDate(), resultDate);
            Double summary = autoActivity.getInitValue() + MathUtils.getSumOfAP(autoActivity.getFirstElement(), autoActivity.getRatio(), days);
            result.put(autoActivity.getId(), summary);
        }
        return result;
    }

    public Map<Long, Double> getHeroRecommendedValuesByActivities(final Long idPlayer, final int shiftDays) {
        Map<Long, Double> mistressMap = getPlayersAutoActivitiesByDate(idPlayer, shiftDays);
        Map<Long, Double> result = new HashMap<>();
        for (Map.Entry<Long, Double> map : mistressMap.entrySet()) {
            HeroActivityResult heroActivityResult =
                    playerActivityService.getHeroActivityResult(map.getKey());
            Double recommendedValues = (map.getValue() - heroActivityResult.getQuantity()) / shiftDays;
            result.put(map.getKey(), recommendedValues);
        }
        return result;
    }

    public Map<String, Double> getHeroRecommendedValuesNamesByActivities(final Long idPlayer, final int shiftDays) {
        Map<String, Double> result = new HashMap<>();
        Map<Long, Double> recommendedMap = getHeroRecommendedValuesByActivities(idPlayer, shiftDays);
        for (Map.Entry<Long, Double> map : recommendedMap.entrySet()) {
            result.put(activityService.getActivityById(map.getKey()).getName(), map.getValue());
        }
        return result;
    }
}
