package shr.training_camp.core.model.database.calculation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shr.training_camp.core.model.database.*;
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

    @Autowired
    private IGroupActivitiesService groupActivitiesService;

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
        // ToDo переделать, взять активности из группы
        List<AutoActivity> playersAA = autoActivityService.getAllAutoActivitiesByPlayerId(idPlayer, idGroup);
        PlayersGroups playersGroups = playerGroupService.findByGroupAndPlayerId(idGroup, idPlayer);
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
                if (playersGroups.getIsHero() == 2) {
                    int probability = RandomUtils.getRandom(100);
                    if (probability % 10 == 0) {
                        resultValue = 2 * resultValue;
                    }
                    if (probability == 77) {
                        resultValue = 3 * resultValue;
                    }
                }
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
        List<GameActivityStatistics> heroList = gameActivityLogService.getGameActivityStatistics(convertDate, heroPlayerGroups.getIdPlayer(), idGroup);
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

    public ComparePlayers getPlayerResults(LocalDate convertDate, PlayersGroups playersGroups, boolean isHero) {
        ComparePlayers comparePlayers = new ComparePlayers();
        comparePlayers.setCurrentDate(convertDate);
        comparePlayers.setHero(isHero);
        comparePlayers.setIdPlayer(playersGroups.getIdPlayer());
        comparePlayers.setPlayer(playerService.getPlayerById(playersGroups.getIdPlayer()));
        //heroPlayer.setGrowFactor(1D); // ToDo change!
        comparePlayers.setActiveStatus(playersGroups.getActiveStatus());
        Map<Long, Double> heroMap = new HashMap<>();
        Map<String, Double> heroMap2 = new HashMap<>();
        List<GameActivityStatistics> heroList = gameActivityLogService.getGameActivityStatistics(convertDate, playersGroups.getIdPlayer(), playersGroups.getIdGroup());
        for (GameActivityStatistics hero : heroList) {
            heroMap.put(hero.getIdActivity(), hero.getValue());
            heroMap2.put(activityService.getActivityById(hero.getIdActivity()).getName(), hero.getValue());
        }
        comparePlayers.setActivityValues(heroMap);
        comparePlayers.setActivityValueNames(heroMap2);
        return comparePlayers;
    }

    public List<ComparePlayers> createComparePlayersList(Long idGroup, LocalDate convertDate) {
        GrowGroup growGroup = growGroupService.getGrowGroupById(idGroup);
        ComparePlayers heroPlayer = getPlayerResults(convertDate,
                playerGroupService.findPlayersGroupsByGroupId(idGroup, 1).get(0),
                true);
        int type = growGroup.getGroupType();
        ComparePlayers masterPlayer = (type == 0) ? heroPlayer : getPlayerResults(convertDate,
                playerGroupService.findPlayersGroupsByGroupId(idGroup, 2).get(0), false);
        // ToDo investigate why Match(type) checks all cases
        /* ComparePlayers masterPlayer = Match(type).of(
                Case($(0), heroPlayer),
                Case($(1), getPlayerResults(convertDate,
                        playerGroupService.findPlayersGroupsByGroupId(idGroup, 2).get(0),
                        false)));
         */

        List<PlayersGroups> playersGroupsList = playerGroupService.findPlayersGroupsByGroupId(idGroup, 0);
        if (growGroup.getGroupType() == 1) {
            playersGroupsList.addAll(playerGroupService.findPlayersGroupsByGroupId(idGroup, 1));
        }
        List<ComparePlayers> resultCompareList = new ArrayList<>();
        for (PlayersGroups playersGroups : playersGroupsList) {
            ComparePlayers virtualPlayer = getPlayerResults(convertDate, playersGroups, false);
            if (playersGroups.getIsHero() == 1) {
                virtualPlayer.setHero(true);
            }

            resultCompareList.add(virtualPlayer);
        }
        for (ComparePlayers comparePlayer : resultCompareList) {
            Double compareFactor = 0.00;
            Double factor2d = 1.000000;
            for (Map.Entry<Long, Double> compareMap : comparePlayer.getActivityValues().entrySet()) {
                Double virtualValue = compareMap.getValue();
                Double masterValue = masterPlayer.getActivityValues().get(compareMap.getKey());
                if (Objects.isNull(masterValue) || masterValue == 0) {
                    masterValue = 1D;
                }
                compareFactor += virtualValue / masterValue;
                Number factor2 = calcFactor2(virtualValue, masterValue);
                factor2d *= factor2.doubleValue();

            }
            Double average = compareFactor / masterPlayer.getActivityValues().size();
            BigDecimal averageBD = new BigDecimal(Double.toString(average));
            averageBD = averageBD.setScale(6, RoundingMode.HALF_UP);
            comparePlayer.setGrowFactor(averageBD.doubleValue());
            BigDecimal factor2BD = new BigDecimal(Double.toString(factor2d));
            factor2BD = factor2BD.setScale(6, RoundingMode.HALF_UP);
            comparePlayer.setGrowFactor2(factor2BD.doubleValue());
            Map<String, Double> growFactors = new HashMap<>();
            growFactors.put("grow_factor_1", averageBD.doubleValue());
            growFactors.put("grow_factor_2", factor2BD.doubleValue());
            comparePlayer.setGrowFactorsMap(growFactors);


        }
        if (growGroup.getGroupType() == 1) {
            // It means we add bot player. It's neessary to think about other groups
            Map<String, Double> growFactors = new HashMap<>();
            growFactors.put("grow_factor_1", 1D);
            growFactors.put("grow_factor_2", 1D);
            masterPlayer.setGrowFactorsMap(growFactors);
            masterPlayer.setGrowFactor(1D);
            masterPlayer.setGrowFactor2(1D);
            resultCompareList.add(masterPlayer);


        } else {
            resultCompareList.add(heroPlayer);
        }
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

    public List<Recommendations> getFewRecommendations(Long idGroup, List<ComparePlayers> allPlayers, LocalDate startDate, int place, int period) {
        //List<AutoActivity> autoActivities = autoActivityService.getAllAutoActivitiesByPlayerId(allPlayers.stream().filter(c -> !c.isHero()).findFirst().get().getIdPlayer());
        List<GroupActivities> groupActivities = groupActivitiesService.getActivitiesInTheGroup(idGroup);
        List<Recommendations> result = new ArrayList<>();
        ComparePlayers heroPlayer = allPlayers.stream().filter(ComparePlayers::isHero).findFirst().get();
        for (GroupActivities autoActivity : groupActivities) {
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
        return getFewRecommendations(idGroup, allPlayers, startDate, place, period);
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
