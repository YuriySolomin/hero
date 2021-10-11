package shr.training_camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shr.training_camp.core.model.database.*;
import shr.training_camp.core.model.database.addition.GameActivityStatistics;
import shr.training_camp.core.model.database.addition.PlayerRandomChoice;
import shr.training_camp.core.model.database.addition.PlayersForGeneration;
import shr.training_camp.core.model.database.calculation.AutoActivitiesCalculation;
import shr.training_camp.core.model.database.calculation.ComparePlayers;
import shr.training_camp.core.model.database.calculation.HeroActivityPlaces;
import shr.training_camp.core.model.database.calculation.Recommendations;
import shr.training_camp.core.model.database.generators.PlayerAutoActivityInfo;
import shr.training_camp.repository.PlayersGroupsRepository;
import shr.training_camp.sevice.interfaces.*;
import shr.training_camp.util.RandomUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static io.vavr.API.*;

@Controller
public class GrowGroupsController extends AbstractEntityController {

    @Autowired
    private IGrowGroupService<GrowGroup> growGroupService;

    @Autowired
    private IPlayerGroupService<PlayersGroups> playerGroupService;

    @Autowired
    private PlayersGroupsRepository playersGroupsRepository;

    @Autowired
    private AutoActivitiesCalculation autoActivitiesCalculation;

    @Autowired
    private IAutoActivityService autoActivityService;

    @Autowired
    private IActivityService<Activity> activityService;

    @Autowired
    private IGameActivityLogService gameActivityLogService;

    @Autowired
    private IGameActivityGrowLogService gameActivityGrowLogService;

    @Autowired
    private IGameActivityEventsLogService gameActivityEventsLogService;

    @Autowired
    private IGroupPropertiesService groupPropertiesService;

    @Autowired
    private PlayerService playerService;

    @GetMapping("/listOfGrowGroups")
    public String showListOfTheQuests(Model model) {
        return findPaginated(1, "startDate", "asc", model);
    }

    @GetMapping("/showNewGrowGroupsForm")
    public String showNewActivityForm(Model model) {
        GrowGroup groupGroup = new GrowGroup();
        model.addAttribute("growGroup", groupGroup);
        model.addAttribute("defaultDate", LocalDate.now());
        return "catalogues/grow/new_grow_group";
    }

    @GetMapping("/showUpdateGGForm/{id}")
    public String showUpdateGGForm(@PathVariable(value = "id") long id, Model model) {
        GrowGroup group = growGroupService.getGrowGroupById(id);
        model.addAttribute("group", group);
        //model.addAttribute("playerToGroup", new )
        model.addAttribute("listOfPlayerGroups", playerGroupService.findPlayersGroupsByGroupId(id));
        return "catalogues/grow/update_grow_group";
    }

    @PostMapping("/saveGrowGroup")
    public String savePlayer(@ModelAttribute("debt") GrowGroup growGroup) {
        growGroupService.save(growGroup);
        return "redirect:/";
    }

    @GetMapping("/grow_groups/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortdir,
                                Model model) {
        int pageSize = 250;
        Page<GrowGroup> page = growGroupService.findPaginated(pageNo, pageSize, sortField, sortdir);
        setModelParametersForPagination(page, pageNo, sortField, sortdir, model);
        model.addAttribute("listGrowGroups", page.getContent());
        return "catalogues/grow/list_grow_groups";
    }

    @GetMapping("/showActivityGameForm/{id}")
    public String showActivityGameForm(@PathVariable(value = "id") long id, Model model) {
        GrowGroup group = growGroupService.getGrowGroupById(id);
        model.addAttribute("growGroup", group);
        model.addAttribute("listOfPlayerGroups", playerGroupService.findPlayersGroupsByGroupId(id));
        return "catalogues/grow/activity_game";
    }

    @GetMapping("/showActivitySomeResults/{id}")
    public String showActivitySomeResults(@PathVariable(value = "id") long id, Model model) {
        LocalDate filterDate = LocalDate.now();
        List<ComparePlayers> playersGroupsList = autoActivitiesCalculation.createSortedList(id, filterDate);
        model.addAttribute("listComparePlayers", playersGroupsList);
        return "catalogues/grow/activity_game_results";
    }

    @GetMapping("/showSummaryResult/{id}")
    public String getShowSummaryResult(@PathVariable(value = "id") long id,
                                       Model model) {
        GrowGroup group = growGroupService.getGrowGroupById(id);
        List<GameActivityStatistics> gameActivityStatistics = gameActivityLogService.getGameActivityStatistics(id);
        model.addAttribute("gameActivityStatistics", gameActivityStatistics);
        model.addAttribute("growGroup", group);

        return "catalogues/grow/summary_results";
    }

    @GetMapping("/setupRoundResults/{id}")
    public String getSetupRoundResultsPage(@PathVariable(value = "id") long id,
                                           Model model) {
        GrowGroup group = growGroupService.getGrowGroupById(id);
        model.addAttribute("growGroup", group);
        return "catalogues/grow/group_properties_setup";
    }

    @PostMapping("/summaryFilter")
    public String getSummaryFilter(@ModelAttribute("growGroup") GrowGroup group,
                                   @RequestParam(value = "idActivity", required = false) Long idActivity,
                                   @RequestParam(value = "idPlayer", required = false) Long idPlayer, Model model) {
        model.addAttribute("growGroup", group);
        List<GameActivityStatistics> gameActivityStatistics = gameActivityLogService.getGameActivityStatistics(group.getIdGroup());
        if (Objects.nonNull(idActivity)) {
            gameActivityStatistics = gameActivityStatistics.stream().filter(stat -> stat.getIdActivity().equals(idActivity)).collect(Collectors.toList());
        }
        if (Objects.nonNull(idPlayer)) {
            gameActivityStatistics = gameActivityStatistics.stream().filter(stat -> stat.getIdPlayer().equals(idPlayer)).collect(Collectors.toList());
        }

        model.addAttribute("gameActivityStatistics", gameActivityStatistics);


        return "catalogues/grow/summary_results";

    }


    @GetMapping("/showActivityComparator/{id}")
    public String showActivityComparator(@PathVariable(value = "id") long id, Model model) {
        LocalDate filterDate = LocalDate.now();
        List<ComparePlayers> playersGroupsList = autoActivitiesCalculation.createComparePlayersList(id, filterDate);
        model.addAttribute("listComparePlayers", playersGroupsList);
        return "catalogues/grow/activity_comparator";
    }

    @GetMapping("/showAutoActivities/{id}")
    public String showAutoActivitiesInTheGroup(@PathVariable(value = "id") long id, Model model) {
        // ToSo метод должен отображать автоактивности игроков из группы, с сортировкой по игроку элементу.
        return "";
    }


    @GetMapping("/showHeroResults/{id}")
    public String showHeroResults(@PathVariable(value = "id") long id, Model model) {
        // ToDo получить списокк игроков, там будет матрица
        LocalDate filterDate = LocalDate.now();
        List<HeroActivityPlaces> playersGroupsList = autoActivitiesCalculation.getHeroActivityPlaces(id, filterDate);
        List<ComparePlayers> allPlayers = autoActivitiesCalculation.createComparePlayersList(id, filterDate);
        List<Recommendations> defaultRecommendations = new ArrayList<>();
        if (allPlayers.size() > 10) {
            defaultRecommendations = autoActivitiesCalculation.getFewRecommendations(id, allPlayers, filterDate, 10, 15);
        }
        for (HeroActivityPlaces heroActivityPlaces: playersGroupsList) {
            long deltaPlace = heroActivityPlaces.getPlace() / 10 ;
            if (deltaPlace < 1) {
                deltaPlace = 1;
            }
            long newPlace = (heroActivityPlaces.getPlace() - deltaPlace) > 1 ? heroActivityPlaces.getPlace() - deltaPlace : 1;

            double recommendationValues = autoActivitiesCalculation.getFewRecommendations(id, allPlayers, filterDate, (int)newPlace, 3)
                    .stream().filter(rec -> rec.getIdActivity().equals(heroActivityPlaces.getIdActivity())).findFirst().get().getRecommendation();
            heroActivityPlaces.setThreeDaysRecommendation(recommendationValues);
            heroActivityPlaces.setTextPlacesValue("( " + heroActivityPlaces.getLowerPlace() + " ) " +
                    heroActivityPlaces.getHeroValue() + " ( " + heroActivityPlaces.getUpperPlace() + " )");
        }
        GrowGroup group = growGroupService.getGrowGroupById(id);
        model.addAttribute("growGroup", group);
        model.addAttribute("compareList", playersGroupsList);
        model.addAttribute("defaultRecommendationList", defaultRecommendations);
        return "catalogues/grow/activity_hero_results";
    }

    @PostMapping("/placeRecommendation")
    public String getRecommendation(@ModelAttribute("growGroup") GrowGroup group,
                                    @RequestParam(value = "idActivity", required = false) Long idActivity,
                                    @RequestParam(value = "idPlayer", required = false) Long idPlayer,
                                    @RequestParam(value = "place", required = false) int place,
                                    @RequestParam(value = "period", required = false) int period,
                                    Model model) {

        model.addAttribute("growGroup", group);
        List<HeroActivityPlaces> playersGroupsList = autoActivitiesCalculation.getHeroActivityPlaces(group.getIdGroup(), LocalDate.now());
        double tempInfo = autoActivitiesCalculation.getRecommendations(group.getIdGroup(), LocalDate.now(), idActivity, place, period);
        model.addAttribute("recommendation", tempInfo);
        model.addAttribute("activityName", activityService.getActivityById(idActivity));
        model.addAttribute("period", period);
        model.addAttribute("place", place);
        model.addAttribute("compareList", playersGroupsList);
        model.addAttribute("defaultRecommendationList", autoActivitiesCalculation.getFewRecommendations(group.getIdGroup(), LocalDate.now(), place, period));
        return "catalogues/grow/activity_hero_results";
    }

    @PostMapping("/runAll")
    public String runAll(@ModelAttribute("growGroup") GrowGroup group,
                         @RequestParam(value = "runAllDate", required = false) String runAllDateString,
                         Model model) {
        LocalDate runAllDate = null;
        if (Objects.nonNull(runAllDateString) && !runAllDateString.isEmpty()) {
            runAllDate = LocalDate.parse(runAllDateString);
        }
        model.addAttribute("growGroup", group);
        List<PlayersGroups> virtualPlayers = playerGroupService.findActivePlayersGroupsByGroupId(group.getIdGroup(), 0);
        if (!playerGroupService.findActivePlayersGroupsByGroupId(group.getIdGroup(), 2).isEmpty()) {
            virtualPlayers.add(playerGroupService.findActivePlayersGroupsByGroupId(group.getIdGroup(), 2).get(0));
        }
        LocalDate startDate = virtualPlayers.get(0).getActualDate();
        long days = ChronoUnit.DAYS.between(startDate, runAllDate);
        for (int i = 0; i <= days; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            if (group.getIdGroup() == 4) {
                runRandomBeforeVirtualCalculation(group, currentDate); // Check if currentDate.minus(1L)
            } else
            {
                if (group.getIdGroup() == 5) {
                    runRandomBeforeVirtualCalculationGroup5(group, currentDate);
                } else {
                    runRandomForGroupTwoPlayers(group, currentDate);
                }

            }

            for (PlayersGroups playersGroups : virtualPlayers) {
                PlayerAutoActivityInfo xxx = autoActivitiesCalculation.getGameActivityLogForPlayer(playersGroups.getIdGroup(), playersGroups.getIdPlayer(), currentDate.plusDays(1L));
                List<GameActivityLog> activityLogs = xxx.getGameActivityLogList();
                for (GameActivityLog gameActivityLog : activityLogs) {
                    AutoActivity autoActivity = autoActivityService.findAutoActivityForPlayer(playersGroups.getIdPlayer(), gameActivityLog.getIdActivity());
                    autoActivity.setDaysFactor(gameActivityLog.getDaysFactor());
                    autoActivity.setFirstElement(gameActivityLog.getFirstElement());
                    autoActivity.setSaveDate(gameActivityLog.getSaveDate().plusDays(1L));
                    playersGroups.setActualDate(currentDate.plusDays(1L));
                    //autoActivity.setSummary(gameActivityLog.getValue());
                    autoActivityService.save(autoActivity);
                    gameActivityLogService.save(gameActivityLog);
                    playerGroupService.save(playersGroups);
                }
            }

        }
        return "catalogues/grow/activity_game";
    }

    @PostMapping("/runByProperties")
    public String runByProperties(@ModelAttribute("growGroup") GrowGroup group,
                                  @RequestParam(value = "runByPropertiesDate") String runByPropertiesDateString,
                                  Model model) {
        // ToDo check max date in the tc_game_activity_log, if null set as group start date
        // ToDo take all group_round_% properties between runByPropertiesDate and date from previous item
        // ToDo runAll method with parameter runByPropertiesDate
        // ToDo runRealPlayer with parameter runByPropertiesDate + 1 day
        // ToDo create list with GroupProperties objects were recieved in the second item, sort it by dates
        // ToDo change the methos compare grow, set the count of loosers according to parameter, by default = 1
        // ToDo run for all items compareGrow
        model.addAttribute("growGroup", group);
        LocalDate runByPropertiesDate = LocalDate.parse(runByPropertiesDateString);
        LocalDate realPlayerDate = runByPropertiesDate.plusDays(1L);
        LocalDate maxDate = Objects.nonNull(gameActivityLogService.getMaxDateGameActivityLogForGroup(group.getIdGroup())) ?
                gameActivityLogService.getMaxDateGameActivityLogForGroup(group.getIdGroup()) :
                group.getStartDate();
        List<GroupProperties> groupPropertiesList = groupPropertiesService.getAllPropertiesFromDiapason("round_date%",
                group.getIdGroup(), maxDate, runByPropertiesDate);
        for (GroupProperties groupProperty: groupPropertiesList) {
            runAll(group, groupProperty.getStartDate().toString(), model);
            runCalculationRealPlayer(group, groupProperty.getStartDate().plusDays(1L).toString(), model);
            runCompare(group, groupProperty.getStartDate().toString(), String.valueOf(groupProperty.getValue().intValue()), model);
        }





        return "catalogues/grow/activity_game";
    }

    @PostMapping("/runCalculationRealPlayer")
    public String runCalculationRealPlayer(@ModelAttribute("growGroup") GrowGroup group,
                                           @RequestParam(value = "actualRealDate", required = false) String actualDateString,
                                           Model model) {
        LocalDate actualDate = null;
        if (Objects.nonNull(actualDateString) && !actualDateString.isEmpty()) {
            actualDate = LocalDate.parse(actualDateString);
        }
        model.addAttribute("growGroup", group);
        List<PlayersGroups> realPlayers = playerGroupService.findActivePlayersGroupsByGroupId(group.getIdGroup(), 1);


        for (PlayersGroups playersGroups : realPlayers) {
            LocalDate saveDate = Objects.isNull(playersGroups.getActualDate()) ? group.getStartDate() : playersGroups.getActualDate();
            List<GameActivityLog> activityLogs = autoActivitiesCalculation.getRealPlayerActivities(playersGroups.getIdGroup(), playersGroups.getIdPlayer(), group.getStartDate(), saveDate, actualDate);
            for (GameActivityLog gameActivityLog : activityLogs) {
                gameActivityLogService.save(gameActivityLog);
            }
            playersGroups.setActualDate(actualDate);
            playerGroupService.save(playersGroups);

        }
        return "catalogues/grow/activity_game";
    }

    @PostMapping("/runCompare")
    public String runCompare(@ModelAttribute("growGroup") GrowGroup group,
                             @RequestParam(value = "compareDate", required = false) String compareDateString,
                             @RequestParam(value = "losersCount", required = false) String losersCount,
                             Model model) {
        LocalDate compareDate = null;
        LocalDate newGrowFactrorStartDate = groupPropertiesService.getPropertyByGroupIdAndCode("grow_factor_2", group.getIdGroup()).getStartDate();
        LocalDate firstFactorDate = groupPropertiesService.getPropertyByGroupIdAndCode("grow_factor_1", group.getIdGroup()).getStartDate();
        GrowGroup dbGroup = growGroupService.getGrowGroupById(group.getIdGroup());
        int groupType = dbGroup.getGroupType();
        double reduceFactor = 1.000;
        if (Objects.nonNull(compareDateString) && !compareDateString.isEmpty()) {
            compareDate = LocalDate.parse(compareDateString);
        }
        model.addAttribute("growGroup", group);
        int days = Math.max((int) ChronoUnit.DAYS.between(firstFactorDate, compareDate), 0);
        int days2 = Math.max((int) ChronoUnit.DAYS.between(newGrowFactrorStartDate, compareDate), 0);
        int lowPercent = Math.max(groupPropertiesService.getPropertyByGroupIdAndCode("grow_factor_1", group.getIdGroup()).getValue().intValue() - days, 0);
        int lowPercent2 = Math.max(groupPropertiesService.getPropertyByGroupIdAndCode("grow_factor_2", group.getIdGroup()).getValue().intValue() - days2, 0);
        List<ComparePlayers> comparePlayersList;
        if (groupType == 0) {
            comparePlayersList = autoActivitiesCalculation.createComparePlayersList(group.getIdGroup(), compareDate)
                    .stream().filter(c -> !c.isHero()).collect(Collectors.toList());
        } else {
            comparePlayersList = autoActivitiesCalculation.createComparePlayersList(group.getIdGroup(), compareDate);
        }
        // ToDo Add check if necessary to reduce
        List <GameActivityGrowLog> roundActivityLog = new ArrayList<>();
        for (ComparePlayers comparePlayers : comparePlayersList) {
            Double baseGrow = playerGroupService.findByGroupAndPlayerId(group.getIdGroup(), comparePlayers.getIdPlayer()).getHeight();
            GameActivityGrowLog gameActivityGrowLog = new GameActivityGrowLog();
            gameActivityGrowLog.setConvertDate(compareDate);
            gameActivityGrowLog.setIdGroup(group.getIdGroup());
            gameActivityGrowLog.setIdPlayer(comparePlayers.getIdPlayer());
            gameActivityGrowLog.setFactor(comparePlayers.getGrowFactor());
            gameActivityGrowLog.setFactor2(comparePlayers.getGrowFactor2());
            //Double changeGrowFactor = comparePlayers.getGrowFactor();
            Double changeGrowFactor = comparePlayers.getGrowFactorsMap().get("grow_factor_1");
            if (lowPercent > 0) {
                if (changeGrowFactor >= 1) {
                    changeGrowFactor = 1 + (changeGrowFactor - 1) * (1 - lowPercent / groupPropertiesService.getPropertyByGroupIdAndCode("grow_factor_1", group.getIdGroup()).getValue());
                } else {
                    changeGrowFactor = 1 - (1 - changeGrowFactor) * (1 - lowPercent / groupPropertiesService.getPropertyByGroupIdAndCode("grow_factor_1", group.getIdGroup()).getValue());
                }
            }
            //Double changeGrowFactor2 = comparePlayers.getGrowFactor2();
            Double changeGrowFactor2 = comparePlayers.getGrowFactorsMap().get("grow_factor_2");
            if (lowPercent2 > 0) {
                if (changeGrowFactor2 >= 1) {
                    changeGrowFactor2 = 1 + (changeGrowFactor2 - 1) * (1 - lowPercent2 / groupPropertiesService.getPropertyByGroupIdAndCode("grow_factor_2", group.getIdGroup()).getValue());
                } else {
                    changeGrowFactor2 = 1 - (1 - changeGrowFactor2) * (1 - lowPercent2 / groupPropertiesService.getPropertyByGroupIdAndCode("grow_factor_2", group.getIdGroup()).getValue());
                }
            }
            // ToDo add min and max factorValues add check if this values present - use them and use default in other cases
            if (changeGrowFactor2 < 0.0001) {
                changeGrowFactor2 = 0.0001;
            }
            BigDecimal growDB = new BigDecimal(baseGrow * changeGrowFactor*changeGrowFactor2);
            growDB = growDB.setScale(4, RoundingMode.HALF_UP);
            gameActivityGrowLog.setGrow(growDB.doubleValue() / reduceFactor);
            roundActivityLog.add(gameActivityGrowLog);
        }
        List <GameActivityGrowLog> highPlayer = roundActivityLog.stream().filter(c -> (c.getGrow() > dbGroup.getMaxHeight())).collect(Collectors.toList());
        if (!highPlayer.isEmpty()) {
            Double templateGrow = highPlayer.stream().sorted(Comparator.comparingDouble(GameActivityGrowLog::getGrow).reversed()).collect(Collectors.toList())
                    .get(dbGroup.getLevelsCount() - 1).getGrow();
            reduceFactor = templateGrow / dbGroup.getMaxHeight();
        }
        for (GameActivityGrowLog saveLog: roundActivityLog) {
            saveLog.setGrow(saveLog.getGrow() / reduceFactor);
            gameActivityGrowLogService.save(saveLog);
        }
        // ToDo зависит от группы
        if (groupType == 0) {
            GameActivityGrowLog heroGrowLog = new GameActivityGrowLog();
            heroGrowLog.setIdPlayer(1L);
            heroGrowLog.setIdGroup(group.getIdGroup());
            heroGrowLog.setConvertDate(compareDate);
            heroGrowLog.setGrow(171D / reduceFactor);
            heroGrowLog.setFactor(1D);
            heroGrowLog.setFactor2(1D);
            gameActivityGrowLogService.save(heroGrowLog);
        }
        if (Integer.parseInt(losersCount) > 0) {
            List<ComparePlayers> losersList = comparePlayersList.stream().sorted(Comparator.comparingDouble(ComparePlayers::getGrowFactor))
                    .filter(a -> a.getActiveStatus()>0).collect(Collectors.toList()).subList(0, Integer.parseInt(losersCount));
            for (ComparePlayers lastPlayer: losersList) {
                PlayersGroups playersGroups = playerGroupService.findByGroupAndPlayerId(group.getIdGroup(), lastPlayer.getIdPlayer());
                playersGroups.setActiveStatus(0);
                playerGroupService.save(playersGroups);
            }
        }

        return "catalogues/grow/activity_game";
    }

    // ToDo Check if work object by name. It means groupProperties works as object and I can use it fields
    @PostMapping("/createGroupProperties")
    public String createGroupProperties(// @ModelAttribute("groupProperties") GroupProperties groupProperties,
                                        @ModelAttribute("growGroup") GrowGroup group,
                                        @RequestParam(value = "startDate", required = false) String startDateString,
                                        @RequestParam(value = "countOfPeriods") int countOfPeriods,
                                        @RequestParam(value = "period") int period,
                                        @RequestParam(value = "countOfLosers") int countOfLosers
                                        ) {
        String prefix = "round_date_";
        List<GroupProperties> savedPeriods = groupPropertiesService
                .getAllPropertiesByGroupIdAndCodeTemplate(prefix + "%", group.getIdGroup());
        LocalDate startDate = LocalDate.parse(startDateString);
        int suffix = 1;
        if (!savedPeriods.isEmpty()) {
            GroupProperties lastGroupProperty = savedPeriods.stream().sorted(Comparator.comparing(GroupProperties::getStartDate).reversed())
                    .collect(Collectors.toList()).get(0);
            startDate = lastGroupProperty.getStartDate();
            suffix = Integer.parseInt(lastGroupProperty.getCode().replace(prefix, ""));

        }
        for (int i=1; i<= countOfPeriods; i++) {
            GroupProperties groupProperties1 = new GroupProperties();
            groupProperties1.setIdGroup(group.getIdGroup());
            groupProperties1.setCode(prefix + (suffix + i));
            groupProperties1.setValue((double)countOfLosers);
            startDate = startDate.plusDays(period);
            groupProperties1.setStartDate(startDate);
            groupPropertiesService.save(groupProperties1);

        }
        return "catalogues/grow/group_properties_setup";
    }

    // ToDo change some hard codes
    @PostMapping("/generateCharacters")
    public String generateCharacters(@ModelAttribute("growGroup") GrowGroup group,
                                     @RequestParam(value = "gender") int gender) {

        List<PlayersForGeneration> groupPlayers = playerGroupService.getPlayersFromGroupByGenderWithoutHero(group.getIdGroup(), gender);
        for (PlayersForGeneration playersForGeneration : groupPlayers) {
            String description = Objects.nonNull(playersForGeneration.getDescription()) ? playersForGeneration.getDescription() : "";
            int valueAG = RandomUtils.getRandom(100) + 1;
            int severity = RandomUtils.getRandom(50) + 1;
            if (valueAG % 10 == 0) {
                description += "AG - yes, ";
            } else {
                int valuePG = RandomUtils.getRandom(105) + 1;
                if (valuePG % 21 == 0) {
                    description += "PG - yes, ";
                }
            }
            description += " Severity = " + severity + ", ";
            int gameWithTiny = RandomUtils.getRandom(100) + 11;
            if (gameWithTiny % 4 == 0) {
                description += "PTiny - yes, ";

            }
            Player player = playerService.getPlayerById(playersForGeneration.getIdPlayer());
            player.setDescription(description);
            playerService.savePlayer(player);
        }


        return "catalogues/grow/group_properties_setup";
    }

    private void runRandomBeforeVirtualCalculationGroup5(GrowGroup group, LocalDate bonusDate) {
        LocalDate monthStartDate = growGroupService.getGrowGroupById(group.getIdGroup()).getStartDate();
        long days = ChronoUnit.DAYS.between(monthStartDate, bonusDate);
        calcMaleGroupRandom(bonusDate, group);
        if (days % 2 == 0) {
            calcNewGroupRandom(bonusDate, group);
        }
        if (days % 3 == 0) {
            calcNewGroupBonus(bonusDate, group);
        }
    }

    private void runRandomForGroupTwoPlayers(GrowGroup group, LocalDate bonusDate) {
        LocalDate monthStartDate = growGroupService.getGrowGroupById(group.getIdGroup()).getStartDate();
        long days = ChronoUnit.DAYS.between(monthStartDate, bonusDate);
        calcNewGroupRandom(bonusDate, group);
        if (days % 3 == 0) {
            calcNewGroupBonus(bonusDate, group);
        }
    }

    private void runRandomBeforeVirtualCalculation(GrowGroup group, LocalDate bonusDate) {
        LocalDate monthStartDate = LocalDate.parse("2021-06-01"); // ToDo remove to config
        long days = ChronoUnit.DAYS.between(monthStartDate, bonusDate);
        updateGeneralRandom(bonusDate, group);
        updateRealPlayersRandom(bonusDate, group);
        oneBigBonus(bonusDate, group, "VeryLowGirl%");
        updateRealPlayersBigBonus(bonusDate, group);
        // -------------------------- TEST

        if (days % 2 == 0) {
            oneBigBonus(bonusDate, group, "LowGirl%");
            oneBigProblem(bonusDate, group, "SuperBoy%");
        }
        if (days % 3 == 0) {
            calculateFandMRandoms3(bonusDate, group);
            oneBigBonus(bonusDate, group, "LowGirl%");
            oneBigProblem(bonusDate, group, "VeryHighBoy%");
        }
        if (days % 4 == 0) {
            updateWithDifferentActivitiesCountRandom(bonusDate, group);
            oneBigBonus(bonusDate, group, "HighGirl%");
            oneBigProblem(bonusDate, group, "HighBoy%");
        }
        if (days % 5 == 0) {
            updateAllPlayersWithNickNamePattern(bonusDate, group, "VeryLowGirl%");
            updateSupersProblems(bonusDate, group, "Super");
            oneBigBonus(bonusDate, group, "RandomGirlA%");
            oneBigProblem(bonusDate, group, "RandomBoyA%");
        }
        if (days % 6 == 0) {
            gold10(bonusDate, group);
            oneBigBonus(bonusDate, group, "VeryHighGirl%");
            oneBigProblem(bonusDate, group, "MediumBoy%");
        }
        if (days % 7 == 0) {
            int girlsCount = 1 + RandomUtils.getRandom(2);
            updateRealPlayerBonus(bonusDate, group, girlsCount);
            updateAllPlayersWithNickNamePattern(bonusDate, group, "LowGirl%");
            oneBigBonus(bonusDate, group, "RandomGirlB%");
            oneBigProblem(bonusDate, group, "RandomBoyB%");
        }
        if (days % 8 == 0) {
            highBoysProblems(bonusDate, group, "%HighBoy%");
            oneBigBonus(bonusDate, group, "SuperGirl%");
            oneBigProblem(bonusDate, group, "LowBoy%");
        }
        if (days % 9 == 0) {
            updateGeneralAntiRandom(bonusDate, group);
        }
        if (days % 10 == 0) {
            luckyOfSpecialGroups(bonusDate, group, "Super%");
            updateAnyGirlsBonus(bonusDate, group);
            updateRandomPlayers(bonusDate, group, "Random");
        }
        if (days % 11 == 0) {
            int playersCount = 3 + RandomUtils.getRandom(12);
            updateAnyGenderBonus(bonusDate, group, playersCount);
        }
        if (days % 12 == 0) {
            updateMediunUnequity(bonusDate, group, "Medium");
        }
        if (days % 13 == 0) {
            int playersCount = 3 + RandomUtils.getRandom(12);
            updateAnyGenderAntiBonus(bonusDate, group, playersCount);
        }
        if (days % 15 == 0) {
            int girlsCount = 3 + RandomUtils.getRandom(12);
            updateGirlPowerBonusOnly(bonusDate, group, girlsCount);
        }
        if (days % 17 == 0) {
            updateBigGame(bonusDate, group);
        }
        if (days % 19 == 0) {
            boysProblems(bonusDate, group);
        }
        if (days % 20 == 0) {
            girlsDay(bonusDate, group);
        }
        if (days % 21 == 0) {
            updateAllPlayersWithNickNamePatternFewActivities(bonusDate, group, "Medium%");
        }
        if (days % 23 == 0) {
            realGirlsStrength(bonusDate, group);
        }
        if ((days - 10) % 27 == 0) {
            bigBoysProblems(bonusDate, group);
        }
    }


    private void runRandomBeforeVirtualCalculationWrongLogic(GrowGroup group, LocalDate bonusDate) {
        LocalDate monthStartDate = LocalDate.parse("2021-06-01"); // ToDo remove to config
        long days = ChronoUnit.DAYS.between(monthStartDate, bonusDate);
        updateGeneralRandom(bonusDate, group);
        updateRealPlayersRandom(bonusDate, group);
        oneBigBonus(bonusDate, group, "VeryLowGirl%");
        // -------------------------- TEST

        Match(days).of(
                Case($(d -> d % 2 == 0), () -> run(() -> {
                    oneBigBonus(bonusDate, group, "LowGirl%");
                    oneBigProblem(bonusDate, group, "SuperBoy%");
                })),
                Case($(d -> d % 3 == 0), () -> run(() -> {
                    calculateFandMRandoms3(bonusDate, group);
                    oneBigBonus(bonusDate, group, "LowGirl%");
                    oneBigProblem(bonusDate, group, "VeryHighBoy%");
                })),
                Case($(d -> d % 4 == 0), () -> run(() -> {
                    updateWithDifferentActivitiesCountRandom(bonusDate, group);
                    oneBigBonus(bonusDate, group, "HighGirl%");
                    oneBigProblem(bonusDate, group, "HighBoy%");
                })),
                Case($(d -> d % 5 == 0), () -> run(() -> {
                    updateAllPlayersWithNickNamePattern(bonusDate, group, "VeryLowGirl%");
                    updateSupersProblems(bonusDate, group, "Super");
                    oneBigBonus(bonusDate, group, "RandomGirlA%");
                    oneBigProblem(bonusDate, group, "RandomBoyA%");
                })),
                Case($(d -> d % 6 == 0), () -> run(() -> {
                    gold10(bonusDate, group);
                    oneBigBonus(bonusDate, group, "VeryHighGirl%");
                    oneBigProblem(bonusDate, group, "MediumBoy%");
                })),
                Case($(d -> d % 7 == 0), () -> run(() -> {
                    int girlsCount = 1 + RandomUtils.getRandom(2);
                    updateRealPlayerBonus(bonusDate, group, girlsCount);
                    updateAllPlayersWithNickNamePattern(bonusDate, group, "LowGirl%");
                    oneBigBonus(bonusDate, group, "RandomGirlB%");
                    oneBigProblem(bonusDate, group, "RandomBoyB%");
                })),
                Case($(d -> d % 8 == 0), () -> run(() -> {
                    highBoysProblems(bonusDate, group, "%HighBoy%");
                    oneBigBonus(bonusDate, group, "SuperGirl%");
                    oneBigProblem(bonusDate, group, "LowBoy%");
                })),
                Case($(d -> d % 9 == 0), () -> run(() -> updateGeneralAntiRandom(bonusDate, group))),
                Case($(d -> d % 10 == 0), () -> run(() -> {
                    luckyOfSpecialGroups(bonusDate, group, "Super%");
                    updateAnyGirlsBonus(bonusDate, group);
                    updateRandomPlayers(bonusDate, group, "Random");
                })),
                Case($(d -> d % 11 == 0), () -> run(() -> {
                    int playersCount = 3 + RandomUtils.getRandom(12);
                    updateAnyGenderBonus(bonusDate, group, playersCount);
                })),
                Case($(d -> d % 12 == 0), () -> run(() -> updateMediunUnequity(bonusDate, group, "Medium"))),
                Case($(d -> d % 13 == 0), () -> run(() -> {
                    int playersCount = 3 + RandomUtils.getRandom(12);
                    updateAnyGenderAntiBonus(bonusDate, group, playersCount);
                })),
                Case($(d -> d % 15 == 0), () -> run(() -> {
                    int girlsCount = 3 + RandomUtils.getRandom(12);
                    updateGirlPowerBonusOnly(bonusDate, group, girlsCount);
                })),
                Case($(d -> d % 17 == 0), () -> run(() -> updateBigGame(bonusDate, group))),
                Case($(d -> d % 19 == 0), () -> run(() -> boysProblems(bonusDate, group))),
                Case($(d -> d % 20 == 0), () -> run(() -> girlsDay(bonusDate, group))),
                Case($(d -> d % 21 == 0), () -> run(() -> updateAllPlayersWithNickNamePatternFewActivities(bonusDate, group, "Medium%"))),
                Case($(d -> d % 23 == 0), () -> run(() -> realGirlsStrength(bonusDate, group))),
                Case($(d -> (d - 10) % 27 == 0), () -> run(() -> bigBoysProblems(bonusDate, group))),

                Case($(), () -> run(() ->System.out.println("There are no any matches")))
                );

    }


//    @PostMapping("/Update")

    private void updateGeneralRandom(LocalDate updateDate, GrowGroup group) {
        int girlsCount = 3 + RandomUtils.getRandom(12);
        List<PlayerRandomChoice> femalesRandom = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 0);
        Collections.shuffle(femalesRandom);
        List<PlayerRandomChoice> luckyFemales = femalesRandom.subList(0, girlsCount);
        List<PlayerRandomChoice> malesRandom = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 1);
        Collections.shuffle(malesRandom);
        List<PlayerRandomChoice> luckyMales = malesRandom.subList(0, 3);
        int[][] randomValues = new int[][]{{60, 30, 10}, {1, 2, 3}};
        calculateRoundAndSaveTheResults(luckyFemales, null, randomValues, group, updateDate, 0, true);
        calculateRoundAndSaveTheResults(luckyMales, null, randomValues, group, updateDate, 0, true);
    }


    private void updateGeneralAntiRandom(LocalDate updateDate, GrowGroup group) {
        int boysCount = 3 + RandomUtils.getRandom(12);
        List<PlayerRandomChoice> femalesRandom = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 0);
        Collections.shuffle(femalesRandom);
        List<PlayerRandomChoice> luckyFemales = femalesRandom.subList(0, 3);
        List<PlayerRandomChoice> malesRandom = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 1);
        Collections.shuffle(malesRandom);
        List<PlayerRandomChoice> luckyMales = malesRandom.subList(0, boysCount);
        int[][] randomValues = new int[][]{{2, 5, 25, 60, 8}, {-4, -3, -2, -1, 1}};
        calculateRoundAndSaveTheResults(luckyFemales, null, randomValues, group, updateDate, 0, true);
        calculateRoundAndSaveTheResults(luckyMales, null, randomValues, group, updateDate, 0, true);

    }

    private void updateRealPlayersRandom(LocalDate updateDate, GrowGroup group) {
        List<PlayerRandomChoice> luckyFemales = playerGroupService.findPlayerByGroupAndGenderAndPlayerType(group.getIdGroup(), 0, 2);
        int[][] randomValues = new int[][]{{75, 10, 10, 5}, {1, 2, 3, 4}};
        calculateRoundAndSaveTheResults(luckyFemales, null, randomValues, group, updateDate, 1, true);
    }

    private void updateRealPlayersBigBonus(LocalDate updateDate, GrowGroup group) {
        List<PlayerRandomChoice> luckyFemales = playerGroupService.findPlayerByGroupAndGenderAndPlayerType(group.getIdGroup(), 0, 2);
        int[][] probabilitiesActivity = new int[][]{{100}, {14}};
        int[][] randomValues = new int[][]{{50, 30, 15, 5}, {1, 2, 3, 4}};
        int[][] bonusValues = new int[][]{{60, 30, 10}, {1, 2, 3}};
        calculateRoundAndSaveTheResults(luckyFemales, probabilitiesActivity, randomValues, group, updateDate, 55, true);
        calculateRoundAndSaveTheResults(luckyFemales, probabilitiesActivity, bonusValues, group, updateDate, 66, false);

    }

    private void bigBoysProblems(LocalDate updateDate, GrowGroup group) {
        List<PlayerRandomChoice> malesRandom = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 1);
        Collections.shuffle(malesRandom);
        int[][] probabilitiesActivity = new int[][]{{10, 45, 45}, {0, 1, 2}};
        int[][] probabilitiesRandom = new int[][]{{46, 52, 2}, {-10, -5, 1}};
        calculateRoundAndSaveTheResults(malesRandom.subList(0, 20), probabilitiesActivity, probabilitiesRandom, group, updateDate, 35, true);
    }

    private void updateAllPlayersWithNickNamePattern(LocalDate updateDate, GrowGroup group, String pattern) {
        List<PlayerRandomChoice> luckyFemales = playerGroupService.findPlayerByGroupAndNickNamePattern(group.getIdGroup(), pattern);
        int[][] randomValues = new int[][]{{60, 30, 10}, {1, 2, 3}};
        calculateRoundAndSaveTheResults(luckyFemales, null, randomValues, group, updateDate, 2, true);
    }

    private void updateAllPlayersWithNickNamePatternFewActivities(LocalDate updateDate, GrowGroup group, String pattern) {
        List<PlayerRandomChoice> luckyPlayers = playerGroupService.findPlayerByGroupAndNickNamePattern(group.getIdGroup(), pattern);
        int[][] probabilitiesActivity = new int[][]{{40, 30, 20, 10}, {2, 3, 4, 5}};
        int[][] probabilitiesRandom = new int[][]{{5, 20, 40, 20, 10, 5}, {-2, -1, 1, 2, 3, 4}};
        calculateRoundAndSaveTheResults(luckyPlayers, probabilitiesActivity, probabilitiesRandom, group, updateDate, 31, true);
    }

    private void updateSupersProblems(LocalDate updateDate, GrowGroup group, String pattern) {
        String girlPattern = pattern + "Girl%";
        String boyPattern = pattern + "Boy%";
        List<PlayerRandomChoice> luckyGirls = playerGroupService.findPlayerByGroupAndNickNamePattern(group.getIdGroup(), girlPattern);
        List<PlayerRandomChoice> luckyBoys = playerGroupService.findPlayerByGroupAndNickNamePattern(group.getIdGroup(), boyPattern);
        Collections.shuffle(luckyGirls);
        Collections.shuffle(luckyBoys);
        int[][] probabilitiesActivity = new int[][]{{20, 18, 16, 14, 12, 7, 6, 4, 3}, {2, 3, 4, 5, 6, 7, 8, 9, 10}};
        int[][] probabilitiesRandom = new int[][]{{10, 20, 45, 20, 5}, {-4, -3, -2, -1, 1}};
        calculateRoundAndSaveTheResults(luckyGirls.subList(0, 1), probabilitiesActivity, probabilitiesRandom, group, updateDate, 32, true);
        calculateRoundAndSaveTheResults(luckyBoys, probabilitiesActivity, probabilitiesRandom, group, updateDate, 32, true);
    }

    private void updateMediunUnequity(LocalDate updateDate, GrowGroup group, String pattern) {
        String girlPattern = pattern + "Girl%";
        String boyPattern = pattern + "Boy%";
        List<PlayerRandomChoice> luckyGirls = playerGroupService.findPlayerByGroupAndNickNamePattern(group.getIdGroup(), girlPattern);
        List<PlayerRandomChoice> luckyBoys = playerGroupService.findPlayerByGroupAndNickNamePattern(group.getIdGroup(), boyPattern);
        int[][] probabilitiesActivity = new int[][]{{30, 30, 20, 15, 5}, {1, 2, 3, 4, 5}};
        int[][] probabilitiesRandomBoys = new int[][]{{20, 30, 45, 5}, {-3, -2, -1, 1}};
        int[][] probabilitiesRandomGirls = new int[][]{{20, 30, 45, 5}, {-1, 1, 2, 3}};
        calculateRoundAndSaveTheResults(luckyGirls, probabilitiesActivity, probabilitiesRandomGirls, group, updateDate, 33, true);
        calculateRoundAndSaveTheResults(luckyBoys, probabilitiesActivity, probabilitiesRandomBoys, group, updateDate, 33, true);
    }

    private void updateRandomPlayers(LocalDate updateDate, GrowGroup group, String pattern) {
        String girlPattern = pattern + "Girl%";
        String boyPattern = pattern + "Boy%";
        List<PlayerRandomChoice> luckyGirls = playerGroupService.findPlayerByGroupAndNickNamePattern(group.getIdGroup(), girlPattern);
        List<PlayerRandomChoice> luckyBoys = playerGroupService.findPlayerByGroupAndNickNamePattern(group.getIdGroup(), boyPattern);
        int[][] probabilitiesActivity = new int[][]{{30, 30, 20, 15, 5}, {1, 2, 3, 4, 5}};
        int[][] probabilitiesRandomBoys = new int[][]{{4, 6, 8, 10, 12, 20, 10, 10, 6, 6, 4, 4}, {-10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 1, 2}};
        int[][] probabilitiesRandomGirls = new int[][]{{2, 4, 8, 10, 10, 12, 34, 10, 5, 5}, {-5, -4, -3, -2, -1, 1, 2, 3, 4, 5}};
        calculateRoundAndSaveTheResults(luckyGirls, probabilitiesActivity, probabilitiesRandomGirls, group, updateDate, 34, true);
        calculateRoundAndSaveTheResults(luckyBoys, probabilitiesActivity, probabilitiesRandomBoys, group, updateDate, 34, true);
    }

    private void oneBigBonus(LocalDate updateDate, GrowGroup group, String pattern) {
        List<PlayerRandomChoice> luckyGirls = playerGroupService.findPlayerByGroupAndNickNamePattern(group.getIdGroup(), pattern);
        List<PlayerRandomChoice> realGirls = playerGroupService.findPlayerByGroupAndGenderAndPlayerType(group.getIdGroup(), 0, 2);
        Collections.shuffle(luckyGirls);
        Collections.shuffle(realGirls);
        int[][] probabilitiesActivity = new int[][]{{75, 25}, {1, 2}};
        int[][] probabilitiesRandom = new int[][]{{2, 52, 46}, {1, 5, 10}};
        calculateRoundAndSaveTheResults(luckyGirls.subList(0, 1), probabilitiesActivity, probabilitiesRandom, group, updateDate, 16, false);
        calculateRoundAndSaveTheResults(realGirls.subList(0, 1), probabilitiesActivity, probabilitiesRandom, group, updateDate, 16, false);
    }

    private void oneBigProblem(LocalDate updateDate, GrowGroup group, String pattern) {
        List<PlayerRandomChoice> luckyBoys = playerGroupService.findPlayerByGroupAndNickNamePattern(group.getIdGroup(), pattern);
        List<PlayerRandomChoice> realGirls = playerGroupService.findPlayerByGroupAndGenderAndPlayerType(group.getIdGroup(), 0, 2);
        Collections.shuffle(luckyBoys);
        Collections.shuffle(realGirls);
        int[][] probabilitiesActivityBoys = new int[][]{{40, 30, 20, 10}, {1, 2, 3, 4}};
        int[][] probabilitiesRandomBoys = new int[][]{{2, 52, 46}, {1, -3, -6}};
        int[][] probabilitiesActivityReal = new int[][]{{40, 40, 15, 5}, {1, 2, 3, 4}};
        int[][] probabilitiesRandomReal = new int[][]{{33, 34, 33}, {1, 3, 5}};
        calculateRoundAndSaveTheResults(luckyBoys.subList(0, 1), probabilitiesActivityBoys, probabilitiesRandomBoys, group, updateDate, 36, true);
        calculateRoundAndSaveTheResults(realGirls.subList(0, 1), probabilitiesActivityReal, probabilitiesActivityReal, group, updateDate, 36, true);

    }

    private void highBoysProblems(LocalDate updateDate, GrowGroup group, String pattern) {
        List<PlayerRandomChoice> luckyPlayers = playerGroupService.findPlayerByGroupAndNickNamePattern(group.getIdGroup(), pattern);
        int[][] probabilitiesActivity = new int[][]{{75, 25}, {1, 2}};
        int[][] probabilitiesRandom = new int[][]{{30, 65, 5}, {-2, -1, 1}};
        calculateRoundAndSaveTheResults(luckyPlayers, probabilitiesActivity, probabilitiesRandom, group, updateDate, 15, false);
    }

    private void luckyOfSpecialGroups(LocalDate updateDate, GrowGroup group, String pattern) {
        List<PlayerRandomChoice> luckyPlayers = playerGroupService.findPlayerByGroupAndNickNamePattern(group.getIdGroup(), pattern);
        int[][] probabilitiesActivity = new int[][]{{25, 50, 25}, {1, 2, 3}};
        int[][] probabilitiesRandom = new int[][]{{10, 15, 25, 25, 15, 10}, {-3, -2, -1, 1, 2, 3}};
        calculateRoundAndSaveTheResults(luckyPlayers, probabilitiesActivity, probabilitiesRandom, group, updateDate, 4, true);

    }

    private void updateRealPlayerBonus(LocalDate updateDate, GrowGroup group, int girlsCount) {
        List<PlayerRandomChoice> realFemales = playerGroupService.findPlayerByGroupAndGenderAndPlayerType(group.getIdGroup(), 0, 2);
        Collections.shuffle(realFemales);
        List<PlayerRandomChoice> luckyFemales = realFemales.subList(0, girlsCount);
        int[][] probabilitiesRandom = new int[][]{{95, 5}, {1, 2}};
        calculateRoundAndSaveTheResults(luckyFemales, null, probabilitiesRandom, group, updateDate, 10, false);
    }

    private void updateAnyGenderBonus(LocalDate updateDate, GrowGroup group, int playersCount) {
        List<PlayerRandomChoice> allFemales = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 0);
        List<PlayerRandomChoice> allMales = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 1);
        for (int i = 1; i <= playersCount; i++) {
            int gender = RandomUtils.getProbabilityForGroup(new int[]{80, 20});
            Collections.shuffle(allFemales);
            Collections.shuffle(allMales);
            if (gender == 0) {
                calculateRoundAndSaveTheResults(allFemales.subList(0, 1), null, new int[][]{{75, 20, 5}, {1, 2, 3}}, group, updateDate, 10, false);
            } else {
                calculateRoundAndSaveTheResults(allMales.subList(0, 1), null, new int[][]{{75, 20, 5}, {1, 2, 3}}, group, updateDate, 10, false);
            }
        }
    }

    private void updateAnyGenderAntiBonus(LocalDate updateDate, GrowGroup group, int playersCount) {
        List<PlayerRandomChoice> allFemales = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 0);
        List<PlayerRandomChoice> allMales = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 1);
        for (int i = 1; i <= playersCount; i++) {
            int gender = RandomUtils.getProbabilityForGroup(new int[]{20, 80});
            Collections.shuffle(allFemales);
            Collections.shuffle(allMales);
            if (gender == 0) {
                calculateRoundAndSaveTheResults(allFemales.subList(0, 1), null, new int[][]{{75, 20, 5}, {-1, -2, -3}}, group, updateDate, 12, false);
            } else {
                calculateRoundAndSaveTheResults(allMales.subList(0, 1), null, new int[][]{{75, 20, 5}, {-1, -2, -3}}, group, updateDate, 12, false);
            }
        }
    }

    private void updateGirlPowerBonusOnly(LocalDate updateDate, GrowGroup group, int girlsCount) {
        List<PlayerRandomChoice> allFemales = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 0);
        for (int i = 1; i <= girlsCount; i++) {
            Collections.shuffle(allFemales);
            int[][] activitiesCount = new int[][]{{20, 30, 35, 10, 5}, {1, 2, 3, 4, 5}};
            int[][] bonusValue = new int[][]{{60, 30, 10}, {1, 2, 3}};
            calculateRoundAndSaveTheResults(allFemales.subList(0, 1), activitiesCount, bonusValue, group, updateDate, 13, false);
        }
    }

    private void updateBigGame(LocalDate updateDate, GrowGroup group) {
        List<PlayerRandomChoice> luckyRealFemales = playerGroupService.findPlayerByGroupAndGenderAndPlayerType(group.getIdGroup(), 0, 2);
        List<PlayerRandomChoice> allFemales = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 0);
        List<PlayerRandomChoice> allMales = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 1);
        Collections.shuffle(luckyRealFemales);
        Collections.shuffle(allFemales);
        Collections.shuffle(allMales);
        int femaleCount = RandomUtils.getRandom(10) + 1;
        int maleCount = RandomUtils.getRandom(3) + 1;
        List<PlayerRandomChoice> luckyFemales = allFemales.subList(0, femaleCount);
        List<PlayerRandomChoice> luckyMales = allMales.subList(0, maleCount);

        int[][] activitiesCount = new int[][]{{15, 15, 10, 10, 8, 7, 6, 5, 5, 5, 5, 4, 4, 1}, {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}};
        int[][] randomValue = new int[][]{{20, 20, 20, 10, 10, 10, 3, 3, 3, 1}, {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}};
        calculateRoundAndSaveTheResults(luckyRealFemales.subList(1, 2), activitiesCount, randomValue, group, updateDate, 6, true);
        calculateRoundAndSaveTheResults(luckyFemales, activitiesCount, randomValue, group, updateDate, 6, true);
        calculateRoundAndSaveTheResults(luckyMales, activitiesCount, randomValue, group, updateDate, 6, true);
    }

    private void gold10(LocalDate updateDate, GrowGroup group) {
        List<PlayerRandomChoice> allFemales = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 0);
        List<PlayerRandomChoice> allMales = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 1);
        Collections.shuffle(allFemales);
        Collections.shuffle(allMales);
        int femaleCount = RandomUtils.getRandom(12) + 3;
        int maleCount = RandomUtils.getRandom(3) + 1;
        List<PlayerRandomChoice> luckyFemales = allFemales.subList(0, femaleCount);
        List<PlayerRandomChoice> luckyMales = allMales.subList(0, maleCount);
        int[][] activitiesCount = new int[][]{{100}, {1}};
        int[][] randomValue = new int[][]{{100}, {10}};
        calculateRoundAndSaveTheResults(luckyFemales, activitiesCount, randomValue, group, updateDate, 9, true);
        calculateRoundAndSaveTheResults(luckyMales, activitiesCount, randomValue, group, updateDate, 9, true);
    }


    private void boysProblems(LocalDate updateDate, GrowGroup group) {
        List<PlayerRandomChoice> allMales = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 1);
        int maleCount = RandomUtils.getRandom(3) + 12;
        Collections.shuffle(allMales);
        List<PlayerRandomChoice> luckyMales = allMales.subList(0, maleCount);
        int[][] activitiesCount = new int[][]{{20, 15, 10, 10, 8, 7, 6, 5, 4, 4, 4, 3, 3, 1}, {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}};
        int[][] randomValue = new int[][]{{20, 40, 30, 1, 9}, {-3, -2, -1, 0, 1}};
        calculateRoundAndSaveTheResults(luckyMales, activitiesCount, randomValue, group, updateDate, 7, true);
    }

    private void girlsDay(LocalDate updateDate, GrowGroup group) {
        List<PlayerRandomChoice> allFemales = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 0);
        int[][] activitiesCount = new int[][]{{17, 15, 13, 11, 8, 7, 5, 5, 5, 4, 4, 3, 2, 1}, {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}};
        int[][] randomValue = new int[][]{{30, 30, 20, 15, 5}, {1, 2, 3, 4, 5}};
        calculateRoundAndSaveTheResults(allFemales, activitiesCount, randomValue, group, updateDate, 8, true);
    }

    private void realGirlsStrength(LocalDate updateDate, GrowGroup group) {
        List<PlayerRandomChoice> luckyRealFemales = playerGroupService.findPlayerByGroupAndGenderAndPlayerType(group.getIdGroup(), 0, 2);
        int[][] activitiesCount = new int[][]{{33, 34, 33}, {2, 3, 4}};
        int[][] bonusValue = new int[][]{{15, 25, 35, 15, 10}, {1, 2, 3, 4, 5}};
        calculateRoundAndSaveTheResults(luckyRealFemales, activitiesCount, bonusValue, group, updateDate, 14, false);
    }

    private void updateAnyGirlsBonus(LocalDate updateDate, GrowGroup group) {
        List<PlayerRandomChoice> females = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 0);
        Collections.shuffle(females);
        int girlsCount = RandomUtils.getRandom(females.size() - 1);
        List<PlayerRandomChoice> luckyFemales = females.subList(0, girlsCount);
        int[][] bonusValue = new int[][]{{98, 2}, {1, 2}};
        calculateRoundAndSaveTheResults(luckyFemales, null, bonusValue, group, updateDate, 11, false);
    }

    private void calcNewGroupRandom(LocalDate updateDate, GrowGroup group) {
        List<PlayerRandomChoice> females = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 0);
        int[][] activitiesCount = new int[][]{{40, 30, 20, 5, 5}, {1, 2, 3, 4, 5}};
        int[][] randomValue = new int[][]{{80, 20}, {1, 2}};
        calculateRoundAndSaveTheResults(females, activitiesCount, randomValue, group, updateDate, 1, true);
    }

    private void calcNewGroupBonus(LocalDate updateDate, GrowGroup group) {
        List<PlayerRandomChoice> females = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 0);
        int[][] activitiesCount = new int[][]{{40, 30, 20, 10}, {1, 2, 3, 4}};
        int[][] randomValue = new int[][]{{90, 10}, {1, 2}};
        calculateRoundAndSaveTheResults(females, activitiesCount, randomValue, group, updateDate, 1, false);
    }

    private void calcMaleGroupRandom(LocalDate updateDate, GrowGroup group) {
        List<PlayerRandomChoice> males = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 1);
        int[][] activitiesCount = new int[][]{{98, 2}, {1, 2}};
        int[][] randomValue = new int[][]{{50, 50}, {1, -1}};
        calculateRoundAndSaveTheResults(males, activitiesCount, randomValue, group, updateDate, 1, true);
    }

    private void calculateFandMRandoms3(LocalDate updateDate, GrowGroup group) {
        List<PlayerRandomChoice> females = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 0);
        List<PlayerRandomChoice> males = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 1);
        int girlsCount = 10 + RandomUtils.getRandom(5);
        int boysCount = 10 + RandomUtils.getRandom(5);
        int[][] probabilitiesFemale = new int[][]{{10, 70, 10, 6, 4}, {-1, 0, 1, 2, 3}};
        int[][] probabilitiesMale = new int[][]{{10, 12, 18, 50, 10}, {-3, -2, -1, 0, 1}};
        Collections.shuffle(females);
        Collections.shuffle(males);
        List<PlayerRandomChoice> luckyFemales = females.subList(0, girlsCount);
        List<PlayerRandomChoice> luckyMales = males.subList(0, boysCount);
        calculateRoundAndSaveTheResults(luckyFemales, null, probabilitiesFemale, group, updateDate, 5, true);
        calculateRoundAndSaveTheResults(luckyMales, null, probabilitiesMale, group, updateDate, 5, true);
    }

    // ToDo everu 4 days
    private void updateWithDifferentActivitiesCountRandom(LocalDate updateDate, GrowGroup group) {
        int girlsCount = 3 + RandomUtils.getRandom(12);
        List<PlayerRandomChoice> femalesRandom = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 0);
        Collections.shuffle(femalesRandom);
        List<PlayerRandomChoice> luckyFemales = femalesRandom.subList(0, girlsCount);
        List<PlayerRandomChoice> malesRandom = playerGroupService.findPlayerByGroupAndGender(group.getIdGroup(), 1);
        Collections.shuffle(malesRandom);
        List<PlayerRandomChoice> luckyMales = malesRandom.subList(0, 3);
        int[][] activitiesCount = new int[][]{{15, 13, 12, 10, 9, 8, 7, 6, 5, 5, 5, 2, 2, 1},
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}};
        calculateRoundAndSaveTheResults(luckyFemales, activitiesCount, new int[][]{{100}, {1}}, group, updateDate, 3, true);
        calculateRoundAndSaveTheResults(luckyMales, activitiesCount, new int[][]{{100}, {1}}, group, updateDate, 3, true);
    }


    private void calculateRoundAndSaveTheResults(List<PlayerRandomChoice> playersRandomChoice, int[][] activitiesCount,
                                                 int[][] randomValue, GrowGroup group, LocalDate updateDate, int eventType, boolean isRandom) {
        for (PlayerRandomChoice playerRandomChoice : playersRandomChoice) {
            List<AutoActivity> playersActivities = autoActivityService.getAllAutoActivitiesByPlayerId(playerRandomChoice.getIdPlayer(), group.getIdGroup());
            int aCount = Objects.nonNull(activitiesCount) ? RandomUtils.getProbabilityForGroup(activitiesCount) : 1;
            Collections.shuffle(playersActivities);
            List<AutoActivity> randomActivities = playersActivities.subList(0, aCount);
            for (AutoActivity randomAutoActivity : randomActivities) {
                GameActivityEventsLog gameActivityEventsLog = new GameActivityEventsLog();
                gameActivityEventsLog.setIdPlayer(playerRandomChoice.getIdPlayer());
                gameActivityEventsLog.setIdActivity(randomAutoActivity.getIdActivity());
                gameActivityEventsLog.setIdGroup(group.getIdGroup());
                gameActivityEventsLog.setEventDate(updateDate);
                gameActivityEventsLog.setEventType(eventType);
                int updateValue = RandomUtils.getProbabilityForGroup(randomValue);
                if (isRandom) {
                    gameActivityEventsLog.setOldValue(randomAutoActivity.getRandomBonus());
                    randomAutoActivity.setRandomBonus(randomAutoActivity.getRandomBonus() + updateValue);
                    gameActivityEventsLog.setNewValue(randomAutoActivity.getRandomBonus());
                } else {
                    gameActivityEventsLog.setOldValue(randomAutoActivity.getBonus());
                    randomAutoActivity.setBonus(randomAutoActivity.getBonus() + updateValue);
                    gameActivityEventsLog.setNewValue(randomAutoActivity.getBonus());
                }
                autoActivityService.save(randomAutoActivity);
                gameActivityEventsLogService.save(gameActivityEventsLog);
            }
        }
    }

    private void saveEventLog(PlayerRandomChoice playerRandomChoice, AutoActivity autoActivity, Long groupId,
                              LocalDate updateDate, int eventType, Double newValue, String type) {
        GameActivityEventsLog gameActivityEventsLog = new GameActivityEventsLog();
        gameActivityEventsLog.setIdPlayer(playerRandomChoice.getIdPlayer());
        gameActivityEventsLog.setIdActivity(autoActivity.getIdActivity());
        gameActivityEventsLog.setIdGroup(groupId);
        gameActivityEventsLog.setEventDate(updateDate);
        gameActivityEventsLog.setEventType(eventType);
        gameActivityEventsLog.setNewValue(newValue);
        if (type.equalsIgnoreCase("random")) {
            gameActivityEventsLog.setOldValue(autoActivity.getRandomBonus());
        } else {
            gameActivityEventsLog.setOldValue(autoActivity.getBonus());
        }
        gameActivityEventsLogService.save(gameActivityEventsLog);
    }



}
