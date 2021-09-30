package shr.training_camp.controller.games;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shr.training_camp.core.config.GameConfig;
import shr.training_camp.core.model.database.AutoActivity;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.PlayersForGroup;
import shr.training_camp.core.model.database.PlayersGroups;
import shr.training_camp.core.model.database.generators.PartAutoActivity;
import shr.training_camp.dev.education.data.FileUtils;
import shr.training_camp.sevice.interfaces.*;
import shr.training_camp.util.RandomUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class GeneratorController {

    @Autowired
    private GameConfig gameConfig;

    @Autowired
    private IAutoActivityService<AutoActivity> autoActivityService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private IPlayersForGroupService playersForGroupService;

    @Autowired
    private IGroupActivitiesService groupActivitiesService;

    @Autowired
    private IPlayerGroupService<PlayersGroups> playerGroupService;


    @PostMapping("/generateBot")
    public String generateBot(@RequestParam(value = "idGroupBot") Long idGroupBot,
                              @RequestParam(value = "idBot") Long idBot,
                              @RequestParam(value = "startDateBot") String startDateBot,
                              Model model) {
        List<String> fileInfo = FileUtils.readListFromFile(gameConfig.getResourceBotActivities());
        fileInfo = fileInfo.stream().skip(1).filter(fI -> Long.parseLong(fI.split("\\|")[9]) == idBot).collect(Collectors.toList());
        List<PartAutoActivity>  aaList;
        aaList = fileInfo.stream().map(s -> s.split("\\|"))
                .map(mask -> {
                    PartAutoActivity partAutoActivity = new PartAutoActivity();
                    partAutoActivity.setIdActivity(Long.parseLong(mask[1]));
                    partAutoActivity.setFirstElement(Double.parseDouble(mask[3]));
                    partAutoActivity.setRatio(Double.parseDouble(mask[4]));
                    partAutoActivity.setInitValue(Double.parseDouble(mask[5]));
                    partAutoActivity.setType(mask[6]);
                    partAutoActivity.setBonus(Double.parseDouble(mask[7]));
                    partAutoActivity.setRandom(Double.parseDouble(mask[8]));
                    partAutoActivity.setIdPlayer(Long.parseLong(mask[9]));
                    partAutoActivity.setIdGroup(idGroupBot);
                    partAutoActivity.setStartDate(LocalDate.parse(startDateBot));
                    return partAutoActivity;
                }).collect(Collectors.toList());
        for (PartAutoActivity partAutoActivity: aaList) {
            AutoActivity autoActivity = new AutoActivity();
            autoActivity.setIdPlayer(partAutoActivity.getIdPlayer());
            autoActivity.setIdActivity(partAutoActivity.getIdActivity());
            double firstElement = partAutoActivity.getFirstElement();
            double ratio = partAutoActivity.getRatio();
            double initValue = partAutoActivity.getInitValue();
            //Parser.getValueForActivity(description, partAutoActivity.getType());
            autoActivity.setFirstElement(firstElement);
            autoActivity.setRatio(ratio);
            autoActivity.setInitValue(initValue);
            autoActivity.setStartDate(LocalDate.parse(startDateBot));
            autoActivity.setBonus(partAutoActivity.getBonus());
            autoActivity.setRandomBonus(partAutoActivity.getRandom());
            autoActivity.setDaysFactor(1);
            autoActivity.setIdGroup(idGroupBot);
            autoActivityService.save(autoActivity);
        }

        return "catalogues/games/generate_for_group";
    }

    @GetMapping("/showGenerateForm")
    public String showGenerateForm(Model model) {

        return "catalogues/games/generate_for_group";
    }

    @PostMapping("/generateNewPlayers")
    public String generateNewPlayers(@RequestParam(value = "countP") Long count,
                                     @RequestParam(value = "genderP") int gender,
                                     @RequestParam(value = "prefixP") String prefix,
                                     @RequestParam(value = "idGroupP") Long idGroup,
                                     Model model) {
        for (int i = 1; i<= count; i++) {
            Player player = new Player();
            player.setFirstName(prefix + "_" + 100*i);
            player.setLastName(prefix + "_" + 100*i);
            player.setNickName(prefix + "_" + 100*i);
            player.setSecretName(RandomUtils.getReadableString(8, 12, true));
            player.setGender(gender);
            player.setPlayerType(30);
            playerService.savePlayer(player);
            Long idPlayer = playerService.getPlayerByNickName(player.getNickName()).getId_player();
            PlayersForGroup playersForGroup = new PlayersForGroup();
            playersForGroup.setIdPlayer(idPlayer);
            playersForGroup.setIdGroup(idGroup);
            playersForGroup.setPlayerStatus(0);
            playersForGroupService.save(playersForGroup);


        }
        return "catalogues/games/generate_for_group";
    }

    @PostMapping("/generatePlayersActivities")
    private String generatePlayersActivities(@RequestParam(value = "idGroupPA") Long idGroupBot,
                                             @RequestParam(value = "idBotPA") Long idBot,
                                             @RequestParam(value = "startDatePA") String startDatePA,
                                             @RequestParam(value = "genderPA") int gender,
                                             Model model) {

        List<PlayersForGroup> players = playersForGroupService.getAllPlayersForGroupByStatusAndGender(idGroupBot, 0, gender);
        Map<Long, List<AutoActivity>> playersMap = new HashMap<>();
        Collections.shuffle(players);
        for (PlayersForGroup playersForGroup: players) {
            List<AutoActivity> autoActivities = autoActivityService.getAllAutoActivitiesByPlayerId(idBot, idGroupBot);
            if (gender == 0) {
                List<AutoActivity> aaPlayersActivities = updateAAFirstElementsFemale(playersForGroup.getIdPlayer(),
                        LocalDate.parse(startDatePA), autoActivities);
                updateAARationFemale(aaPlayersActivities, idGroupBot);
                updateAARandomBonusFemale(aaPlayersActivities);
                updateAABonusFemale(aaPlayersActivities);
                playersMap.put(playersForGroup.getIdPlayer(), aaPlayersActivities);

            }
            for (Map.Entry<Long, List<AutoActivity>> map: playersMap.entrySet()) {
                for (AutoActivity activity : map.getValue()) {
                    autoActivityService.save(activity);
                }
            }
            //List<AutoActivity> aa = createAutoActivitiesByRandom(idBot, gender, idGroupBot);

        }
        if (gender == 1) {
            // ToDo 10 первых игроков, быстрые со слабым стартом
            List<AutoActivity> autoActivities = autoActivityService.getAllAutoActivitiesByPlayerId(idBot, idGroupBot);
            List<PlayersForGroup> sub1 = players.subList(0, 10);
            Map<Long, List<AutoActivity>> aaa = updateMen1_10(sub1, autoActivities, LocalDate.parse(startDatePA), idGroupBot);
            addListIntoTheDB(aaa, "WeakFast");
            List<PlayersForGroup> sub2 = players.subList(10, 20);
            aaa = update11_20(sub2, autoActivities, LocalDate.parse(startDatePA), idGroupBot);
            addListIntoTheDB(aaa, "StrongSuperSlow");
            List<PlayersForGroup> sub3 = players.subList(20, 50);
            aaa = update21_50(sub3, autoActivities, LocalDate.parse(startDatePA), idGroupBot);
            addListIntoTheDB(aaa, "Average");
            List<PlayersForGroup> sub4 = players.subList(50, 60);
            aaa = update51_60(sub4, autoActivities, LocalDate.parse(startDatePA), idGroupBot);
            addListIntoTheDB(aaa, "SuperWeakVeryFast");
            List<PlayersForGroup> sub5 = players.subList(60, 75);
            aaa = update61_75(sub5, autoActivities, LocalDate.parse(startDatePA), idGroupBot);
            addListIntoTheDB(aaa, "RandomGroup");
            List<PlayersForGroup> sub6 = players.subList(75, 100);
            aaa = update76_100(sub6, autoActivities, LocalDate.parse(startDatePA), idGroupBot);
            addListIntoTheDB(aaa, "StrongSlow");
            List<PlayersForGroup> sub7 = players.subList(100, 120);
            aaa = update101_120(sub7, autoActivities, LocalDate.parse(startDatePA), idGroupBot);
            addListIntoTheDB(aaa, "UnstableStart");
            List<PlayersForGroup> sub8 = players.subList(120, 140);
            aaa = update121_140(sub8, autoActivities, LocalDate.parse(startDatePA), idGroupBot);
            addListIntoTheDB(aaa, "UnstableRatio");
            List<PlayersForGroup> sub9 = players.subList(140, 150);
            aaa = update141_150(sub9, autoActivities, LocalDate.parse(startDatePA), idGroupBot);
            addListIntoTheDB(aaa, "OneStar");
            List<PlayersForGroup> sub10 = players.subList(150, 160);
            aaa = update151_160(sub10, autoActivities, LocalDate.parse(startDatePA), idGroupBot);
            addListIntoTheDB(aaa, "AdditinalHope");
            List<PlayersForGroup> sub11 = players.subList(160, 175);
            aaa = update161_175(sub11, autoActivities, LocalDate.parse(startDatePA), idGroupBot);
            addListIntoTheDB(aaa, "UnlimitedRandom");
            List<PlayersForGroup> sub12 = players.subList(175, 190);
            aaa = update176_190(sub12, autoActivities, LocalDate.parse(startDatePA), idGroupBot);
            addListIntoTheDB(aaa, "StrongUnlimitedRandom");

        }
        return "catalogues/games/generate_for_group";
    }

    @PostMapping("addPlayersIntoTheGroups")
    public String addPlayersIntoTheGroups(@RequestParam(value = "idGroupPG") Long idGroupPG,
                                          @RequestParam(value = "idBotPG") Long idBotPG,
                                          @RequestParam(value = "startDatePG") String startDatePG,
                                          Model model) {
        List<PlayersForGroup> allPlayers = playersForGroupService.getAllPlayersForGroupByStatus(idGroupPG, 0);
        PlayersGroups bot = new PlayersGroups();
        bot.setIdGroup(idGroupPG);
        bot.setIdPlayer(idBotPG);
        bot.setActiveStatus(1);
        bot.setIsHero(2);
        bot.setActualDate(LocalDate.parse(startDatePG));
        bot.setHeight(185D);
        playerGroupService.save(bot);
        PlayersGroups hero = new PlayersGroups();
        hero.setIdGroup(idGroupPG);
        hero.setIdPlayer(1L);
        hero.setActiveStatus(1);
        hero.setIsHero(1);
        hero.setActualDate(LocalDate.parse(startDatePG));
        hero.setHeight(171D);
        playerGroupService.save(hero);
        int[][] maleHeight = new int[][]{{1, 2, 2, 2, 3, 3, 3, 4, 4, 5, 6, 7, 9, 9, 9, 12, 9, 10, 9, 8, 7, 7, 7, 7, 6, 6, 6, 5, 5, 5, 4, 4, 3, 3, 2, 1, 1, 1}, {165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202}};
        int[][] femaleHeight = new int[][]{{1, 2, 2, 2, 3, 3, 3, 4, 4, 5, 6, 7, 9, 9, 9, 12, 9, 10, 9, 8, 7, 7, 7, 7, 6, 6, 6, 5, 5, 5, 4, 4, 3, 3, 2, 1, 1, 1}, {152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189}};
        for (PlayersForGroup virtual: allPlayers) {
            int gender = playerService.getPlayerById(virtual.getIdPlayer()).getGender();
            double height = (gender == 0) ? RandomUtils.getProbabilityForGroup(femaleHeight) : RandomUtils.getProbabilityForGroup(maleHeight);
            PlayersGroups virtualPlayer = new PlayersGroups();
            virtualPlayer.setIdGroup(idGroupPG);
            virtualPlayer.setIdPlayer(virtual.getIdPlayer());
            virtualPlayer.setHeight(height);
            virtualPlayer.setActiveStatus(1);
            virtualPlayer.setActualDate(LocalDate.parse(startDatePG));
            virtualPlayer.setIsHero(0);
            playerGroupService.save(virtualPlayer);
        }
        return "catalogues/games/generate_for_group";
    }

    private void addListIntoTheDB(Map<Long, List<AutoActivity>> playersMap, String prefix) {
        for (Map.Entry<Long, List<AutoActivity>> map: playersMap.entrySet()) {
            Player player = playerService.getPlayerById(map.getKey());
            player.setFirstName(prefix + player.getFirstName());
            player.setLastName(prefix + player.getLastName());
            playerService.savePlayer(player);
            for (AutoActivity playerActivity: map.getValue()) {
                autoActivityService.save(playerActivity);
            }
        }

    }

    private List<AutoActivity> createAutoActivitiesByRandom(List<AutoActivity> autoActivities, Long idPlayer, Long idBot, int scheme, Long idGroup) {

        List<AutoActivity> result = new ArrayList<>();
        if (scheme == 0) {
            //updateAAFirstElementsFemale(autoActivities);
            updateAARationFemale(autoActivities, idGroup);
            updateAARandomBonusFemale(autoActivities);
            updateAABonusFemale(autoActivities);
        }
        return autoActivities;
    }

    private List<AutoActivity> updateAAFirstElementsFemale(Long idPlayer, LocalDate startDate, final List<AutoActivity> autoActivities) {
        List<AutoActivity> result = new ArrayList<>();
        autoActivities.stream().forEach(aa -> {
            AutoActivity newAA = new AutoActivity();
            newAA.setIdPlayer(idPlayer);
            newAA.setIdActivity(aa.getIdActivity());
            newAA.setFirstElement(aa.getFirstElement());
            newAA.setRatio(aa.getRatio());
            newAA.setBonus(aa.getBonus());
            newAA.setIdGroup(aa.getIdGroup());
            newAA.setRandomBonus(aa.getRandomBonus());
            newAA.setDaysFactor(1);
            newAA.setInitValue(aa.getInitValue());
            newAA.setStartDate(startDate);

            result.add(newAA);

        });
        Collections.shuffle(result);
        result.get(0).setFirstElement(result.get(0).getFirstElement() * 2);
        result.stream().skip(1).forEach(element -> {
            double factor = 1.12 + 0.38*Math.random();
            BigDecimal value = new BigDecimal(factor);
            value = value.setScale(2, RoundingMode.HALF_UP);
            element.setFirstElement(element.getFirstElement() * value.doubleValue());
        });
        return result;
    }

    private List<AutoActivity> updateAARationFemale(List<AutoActivity> autoActivities, Long idGroup) {
        Collections.shuffle(autoActivities);
        AutoActivity maxRatioActivity = autoActivities.get(0);
        double maxRatio = Math.min(2.25*maxRatioActivity.getRatio(), groupActivitiesService.getActivitiesInTheGroup(idGroup).stream()
                .filter(e -> e.getIdActivity().equals(maxRatioActivity.getIdActivity())).findFirst().get().getLimitRatio());
        BigDecimal valueMax = new BigDecimal(maxRatio);
        valueMax = valueMax.setScale(2, RoundingMode.HALF_UP);
        maxRatioActivity.setRatio(valueMax.doubleValue());
        autoActivities.stream().skip(1).forEach(element -> {
            double factor = 1.12 + 0.88 * Math.random();
            factor = Math.min(factor * element.getRatio(), groupActivitiesService.getActivitiesInTheGroup(idGroup).stream()
                    .filter(e -> e.getIdActivity().equals(element.getIdActivity())).findFirst().get().getLimitRatio());
            BigDecimal value = new BigDecimal(factor);
            value = value.setScale(2, RoundingMode.HALF_UP);
            element.setRatio(value.doubleValue());
        });
        return autoActivities;
    }

    private List<AutoActivity> updateAARandomBonusFemale(List<AutoActivity> autoActivities) {
        Collections.shuffle(autoActivities);
        double randomFirst = 75D + RandomUtils.getRandom(26);
        autoActivities.get(0).setRandomBonus(randomFirst);
        autoActivities.stream().skip(1).forEach(element -> {
            double random = 5D + RandomUtils.getRandom(16);
            element.setRandomBonus(random);
        });
        return autoActivities;
    }

    private List<AutoActivity> updateAABonusFemale(List<AutoActivity> autoActivities) {
        Collections.shuffle(autoActivities);
        double bonusFirst = 15D + RandomUtils.getRandom(11);
        autoActivities.get(0).setBonus(bonusFirst);
        autoActivities.stream().skip(1).forEach(element -> {
            double bonus = 3D + RandomUtils.getRandom(8);
            element.setBonus(bonus);
        });
        return autoActivities;
    }

    private Map<Long, List<AutoActivity>> updateMen1_10(List<PlayersForGroup> playersForGroups, List<AutoActivity> botActivities, LocalDate startDate, Long idGroup) {
        // все первые элементы от -50 до -25%, все ратио от 20 до 50 процентов в плюс но не выше максимума
        Map<Long, List<AutoActivity>> result = new HashMap<>();
        for (PlayersForGroup playersForGroup: playersForGroups) {
            List<AutoActivity> newPlayerActivities = new ArrayList<>();
            for (AutoActivity activity: botActivities) {
                double feFactor = (-50 + RandomUtils.getRandom(26)) / 100D;
                double ratioFactor = (20 + RandomUtils.getRandom(31)) / 100D;
                double firstElement = activity.getFirstElement() * (1 + feFactor);
                BigDecimal value1 = new BigDecimal(firstElement);
                value1 = value1.setScale(2, RoundingMode.HALF_UP);
                double ratio = activity.getRatio() * (1 + ratioFactor);
                ratio = Math.min(ratio, getMaxRatio(idGroup, activity.getIdActivity()));
                BigDecimal value2 = new BigDecimal(ratio);
                value2 = value2.setScale(2, RoundingMode.HALF_UP);
                AutoActivity newAA = createNewAutoActivity(playersForGroup.getIdPlayer(), activity.getIdActivity(),
                        value1.doubleValue(), value2.doubleValue(), 0D, 0D, idGroup, startDate, 1, activity.getInitValue());
                newPlayerActivities.add(newAA);
            }
            result.put(playersForGroup.getIdPlayer(), newPlayerActivities);

        }
        return result;
    }

    private Map<Long, List<AutoActivity>> update11_20(List<PlayersForGroup> playersForGroups, List<AutoActivity> botActivities, LocalDate startDate, Long idGroup) {
        Map<Long, List<AutoActivity>> result = new HashMap<>();
        for (PlayersForGroup playersForGroup: playersForGroups) {
            List<AutoActivity> newPlayerActivities = new ArrayList<>();
            for (AutoActivity activity: botActivities) {
                double firstElement = activity.getFirstElement() * 1.7;
                double ratio = 0.01;
                AutoActivity newAA = createNewAutoActivity(playersForGroup.getIdPlayer(), activity.getIdActivity(),
                        firstElement, ratio, 0D, 0D, idGroup, startDate, 1, activity.getInitValue());
                newPlayerActivities.add(newAA);
            }
            result.put(playersForGroup.getIdPlayer(), newPlayerActivities);
        }
        return result;
    }

    private Map<Long, List<AutoActivity>> update21_50(List<PlayersForGroup> playersForGroups, List<AutoActivity> botActivities, LocalDate startDate, Long idGroup) {
        Map<Long, List<AutoActivity>> result = new HashMap<>();
        for (PlayersForGroup playersForGroup: playersForGroups) {
            List<AutoActivity> newPlayerActivities = new ArrayList<>();
            for (AutoActivity activity: botActivities) {
                double feFactor = (-20 + RandomUtils.getRandom(31)) / 100D;
                double ratioFactor = (-20 + RandomUtils.getRandom(26)) / 100D;
                double firstElement = activity.getFirstElement() * (1 + feFactor);
                BigDecimal value1 = BigDecimal.valueOf(firstElement);
                value1 = value1.setScale(2, RoundingMode.HALF_UP);
                double ratio = activity.getRatio() * (1 + ratioFactor);
                ratio = Math.min(ratio, getMaxRatio(idGroup, activity.getIdActivity()));
                BigDecimal value2 = new BigDecimal(ratio);
                value2 = value2.setScale(2, RoundingMode.HALF_UP);
                AutoActivity newAA = createNewAutoActivity(playersForGroup.getIdPlayer(), activity.getIdActivity(),
                        value1.doubleValue(), value2.doubleValue(), 0D, 2D, idGroup, startDate, 1, activity.getInitValue());
                newPlayerActivities.add(newAA);
            }
            result.put(playersForGroup.getIdPlayer(), newPlayerActivities);
        }
        return result;
    }

    private Map<Long, List<AutoActivity>> update51_60(List<PlayersForGroup> playersForGroups, List<AutoActivity> botActivities, LocalDate startDate, Long idGroup) {
        Map<Long, List<AutoActivity>> result = new HashMap<>();
        for (PlayersForGroup playersForGroup: playersForGroups) {
            List<AutoActivity> newPlayerActivities = new ArrayList<>();
            for (AutoActivity activity: botActivities) {
                double ratioFactor = (50 + RandomUtils.getRandom(151)) / 100D;
                double ratio = activity.getRatio() * (1 + ratioFactor);
                ratio = Math.min(ratio, getMaxRatio(idGroup, activity.getIdActivity()));
                BigDecimal value2 = new BigDecimal(ratio);
                value2 = value2.setScale(2, RoundingMode.HALF_UP);
                AutoActivity newAA = createNewAutoActivity(playersForGroup.getIdPlayer(), activity.getIdActivity(),
                        0D, value2.doubleValue(), 0D, 0D, idGroup, startDate, 1, activity.getInitValue());
                newPlayerActivities.add(newAA);
            }
            result.put(playersForGroup.getIdPlayer(), newPlayerActivities);
        }
        return result;
    }

    private Map<Long, List<AutoActivity>> update61_75(List<PlayersForGroup> playersForGroups, List<AutoActivity> botActivities, LocalDate startDate, Long idGroup) {
        Map<Long, List<AutoActivity>> result = new HashMap<>();
        for (PlayersForGroup playersForGroup: playersForGroups) {
            List<AutoActivity> newPlayerActivities = new ArrayList<>();
            for (AutoActivity activity: botActivities) {
                double feFactor = (-50 + RandomUtils.getRandom(101)) / 100D;
                double ratioFactor = (-25 + RandomUtils.getRandom(51)) / 100D;
                double firstElement = activity.getFirstElement() * (1 + feFactor);
                BigDecimal value1 = BigDecimal.valueOf(firstElement);
                value1 = value1.setScale(2, RoundingMode.HALF_UP);
                double ratio = activity.getRatio() * (1 + ratioFactor);
                ratio = Math.min(ratio, getMaxRatio(idGroup, activity.getIdActivity()));
                BigDecimal value2 = new BigDecimal(ratio);
                value2 = value2.setScale(2, RoundingMode.HALF_UP);
                AutoActivity newAA = createNewAutoActivity(playersForGroup.getIdPlayer(), activity.getIdActivity(),
                        value1.doubleValue(), value2.doubleValue(), 0D, -10D, idGroup, startDate, 1, activity.getInitValue());
                newPlayerActivities.add(newAA);
            }
            result.put(playersForGroup.getIdPlayer(), newPlayerActivities);
        }
        return result;
    }

    private Map<Long, List<AutoActivity>> update76_100(List<PlayersForGroup> playersForGroups, List<AutoActivity> botActivities, LocalDate startDate, Long idGroup) {
        Map<Long, List<AutoActivity>> result = new HashMap<>();
        for (PlayersForGroup playersForGroup: playersForGroups) {
            List<AutoActivity> newPlayerActivities = new ArrayList<>();
            for (AutoActivity activity: botActivities) {
                double feFactor = (10 + RandomUtils.getRandom(21)) / 100D;
                double ratioFactor = -0.5;
                double firstElement = activity.getFirstElement() * (1 + feFactor);
                BigDecimal value1 = BigDecimal.valueOf(firstElement);
                value1 = value1.setScale(2, RoundingMode.HALF_UP);
                double ratio = activity.getRatio() * (1 + ratioFactor);
                ratio = Math.min(ratio, getMaxRatio(idGroup, activity.getIdActivity()));
                BigDecimal value2 = new BigDecimal(ratio);
                value2 = value2.setScale(2, RoundingMode.HALF_UP);
                AutoActivity newAA = createNewAutoActivity(playersForGroup.getIdPlayer(), activity.getIdActivity(),
                        value1.doubleValue(), value2.doubleValue(), -10D, 10D, idGroup, startDate, 1, activity.getInitValue());
                newPlayerActivities.add(newAA);
            }
            result.put(playersForGroup.getIdPlayer(), newPlayerActivities);
        }
        return result;
    }

    private Map<Long, List<AutoActivity>> update101_120(List<PlayersForGroup> playersForGroups, List<AutoActivity> botActivities, LocalDate startDate, Long idGroup) {
        Map<Long, List<AutoActivity>> result = new HashMap<>();
        for (PlayersForGroup playersForGroup: playersForGroups) {
            List<AutoActivity> newPlayerActivities = new ArrayList<>();
            Collections.shuffle(botActivities);
            for (AutoActivity activity: botActivities.subList(0,7)) {
                AutoActivity newAA = createNewAutoActivity(playersForGroup.getIdPlayer(), activity.getIdActivity(),
                        0D, activity.getRatio(), 0D, 0D, idGroup, startDate, 1, activity.getInitValue());
                newPlayerActivities.add(newAA);
            }
            for (AutoActivity activity: botActivities.subList(7,14)) {
                AutoActivity newAA = createNewAutoActivity(playersForGroup.getIdPlayer(), activity.getIdActivity(),
                        2 * activity.getFirstElement(), activity.getRatio(), 0D, 0D, idGroup, startDate, 1, activity.getInitValue());
                newPlayerActivities.add(newAA);
            }
            result.put(playersForGroup.getIdPlayer(), newPlayerActivities);
        }
        return result;

    }

    private Map<Long, List<AutoActivity>> update121_140(List<PlayersForGroup> playersForGroups, List<AutoActivity> botActivities, LocalDate startDate, Long idGroup) {
        Map<Long, List<AutoActivity>> result = new HashMap<>();
        for (PlayersForGroup playersForGroup: playersForGroups) {
            List<AutoActivity> newPlayerActivities = new ArrayList<>();
            Collections.shuffle(botActivities);
            for (AutoActivity activity: botActivities.subList(0,7)) {
                AutoActivity newAA = createNewAutoActivity(playersForGroup.getIdPlayer(), activity.getIdActivity(),
                        activity.getFirstElement(), 0.01, 0D, 0D, idGroup, startDate, 1, activity.getInitValue());
                newPlayerActivities.add(newAA);
            }
            for (AutoActivity activity: botActivities.subList(7,14)) {
                double ratio = 2 * activity.getRatio();
                ratio = Math.min(ratio, getMaxRatio(idGroup, activity.getIdActivity()));
                BigDecimal value2 = new BigDecimal(ratio);
                value2 = value2.setScale(2, RoundingMode.HALF_UP);
                AutoActivity newAA = createNewAutoActivity(playersForGroup.getIdPlayer(), activity.getIdActivity(),
                        activity.getFirstElement(), value2.doubleValue(), 0D, 0D, idGroup, startDate, 1, activity.getInitValue());
                newPlayerActivities.add(newAA);
            }
            result.put(playersForGroup.getIdPlayer(), newPlayerActivities);
        }
        return result;

    }

    private Map<Long, List<AutoActivity>> update141_150(List<PlayersForGroup> playersForGroups, List<AutoActivity> botActivities, LocalDate startDate, Long idGroup) {
        Map<Long, List<AutoActivity>> result = new HashMap<>();
        for (PlayersForGroup playersForGroup: playersForGroups) {
            List<AutoActivity> newPlayerActivities = new ArrayList<>();
            Collections.shuffle(botActivities);
            AutoActivity firstActivity = botActivities.get(0);
            double ratio = 2 * firstActivity.getRatio();
            ratio = Math.min(ratio, getMaxRatio(idGroup, firstActivity.getIdActivity()));
            BigDecimal value2 = new BigDecimal(ratio);
            value2 = value2.setScale(2, RoundingMode.HALF_UP);
            AutoActivity firstAA = createNewAutoActivity(playersForGroup.getIdPlayer(), firstActivity.getIdActivity(),
                    2 * firstActivity.getFirstElement(), value2.doubleValue(), 0D, 0D, idGroup, startDate, 1, firstActivity.getInitValue());
            newPlayerActivities.add(firstAA);
            AutoActivity zeroActivity = botActivities.get(1);
            AutoActivity secondAA = createNewAutoActivity(playersForGroup.getIdPlayer(), zeroActivity.getIdActivity(),
                    0D, 0.01, 0D, 0D, idGroup, startDate, 1, zeroActivity.getInitValue());
            newPlayerActivities.add(secondAA);
            for (AutoActivity activity: botActivities.subList(2,14)) {
                AutoActivity newAA = createNewAutoActivity(playersForGroup.getIdPlayer(), activity.getIdActivity(),
                        activity.getFirstElement(), activity.getRatio(), 0D, 0D, idGroup, startDate, 1, activity.getInitValue());
                newPlayerActivities.add(newAA);
            }
            result.put(playersForGroup.getIdPlayer(), newPlayerActivities);
        }
        return result;

    }

    private Map<Long, List<AutoActivity>> update151_160(List<PlayersForGroup> playersForGroups, List<AutoActivity> botActivities, LocalDate startDate, Long idGroup) {
        Map<Long, List<AutoActivity>> result = new HashMap<>();
        for (PlayersForGroup playersForGroup: playersForGroups) {
            List<AutoActivity> newPlayerActivities = new ArrayList<>();
            for (AutoActivity activity: botActivities) {
                double feFactor = -0.25;
                double ratioFactor = -0.25;
                double firstElement = activity.getFirstElement() * (1 + feFactor);
                BigDecimal value1 = BigDecimal.valueOf(firstElement);
                value1 = value1.setScale(2, RoundingMode.HALF_UP);
                double ratio = activity.getRatio() * (1 + ratioFactor);
                ratio = Math.min(ratio, getMaxRatio(idGroup, activity.getIdActivity()));
                BigDecimal value2 = new BigDecimal(ratio);
                value2 = value2.setScale(2, RoundingMode.HALF_UP);
                AutoActivity newAA = createNewAutoActivity(playersForGroup.getIdPlayer(), activity.getIdActivity(),
                        value1.doubleValue(), value2.doubleValue(), 10D, 20D, idGroup, startDate, 1, activity.getInitValue());
                newPlayerActivities.add(newAA);
            }
            result.put(playersForGroup.getIdPlayer(), newPlayerActivities);
        }
        return result;
    }

    private Map<Long, List<AutoActivity>> update161_175(List<PlayersForGroup> playersForGroups, List<AutoActivity> botActivities, LocalDate startDate, Long idGroup) {
        Map<Long, List<AutoActivity>> result = new HashMap<>();
        for (PlayersForGroup playersForGroup: playersForGroups) {
            List<AutoActivity> newPlayerActivities = new ArrayList<>();
            for (AutoActivity activity: botActivities) {
                double feFactor = (-50 + RandomUtils.getRandom(61)) / 100D;
                double ratioFactor = (-75 + RandomUtils.getRandom(101)) / 100D;
                double firstElement = activity.getFirstElement() * (1 + feFactor);
                BigDecimal value1 = BigDecimal.valueOf(firstElement);
                value1 = value1.setScale(2, RoundingMode.HALF_UP);
                double ratio = activity.getRatio() * (1 + ratioFactor);
                ratio = Math.min(ratio, getMaxRatio(idGroup, activity.getIdActivity()));
                BigDecimal value2 = new BigDecimal(ratio);
                value2 = value2.setScale(2, RoundingMode.HALF_UP);
                AutoActivity newAA = createNewAutoActivity(playersForGroup.getIdPlayer(), activity.getIdActivity(),
                        value1.doubleValue(), value2.doubleValue(), 0D, 50D, idGroup, startDate, 1, activity.getInitValue());
                newPlayerActivities.add(newAA);
            }
            result.put(playersForGroup.getIdPlayer(), newPlayerActivities);
        }
        return result;
    }

    private Map<Long, List<AutoActivity>> update176_190(List<PlayersForGroup> playersForGroups, List<AutoActivity> botActivities, LocalDate startDate, Long idGroup) {
        Map<Long, List<AutoActivity>> result = new HashMap<>();
        for (PlayersForGroup playersForGroup: playersForGroups) {
            List<AutoActivity> newPlayerActivities = new ArrayList<>();
            for (AutoActivity activity: botActivities) {
                double feFactor = (-10 + RandomUtils.getRandom(61)) / 100D;
                double ratioFactor = (-25 + RandomUtils.getRandom(101)) / 100D;
                double firstElement = activity.getFirstElement() * (1 + feFactor);
                BigDecimal value1 = BigDecimal.valueOf(firstElement);
                value1 = value1.setScale(2, RoundingMode.HALF_UP);
                double ratio = activity.getRatio() * (1 + ratioFactor);
                ratio = Math.min(ratio, getMaxRatio(idGroup, activity.getIdActivity()));
                BigDecimal value2 = new BigDecimal(ratio);
                value2 = value2.setScale(2, RoundingMode.HALF_UP);
                AutoActivity newAA = createNewAutoActivity(playersForGroup.getIdPlayer(), activity.getIdActivity(),
                        value1.doubleValue(), value2.doubleValue(), 0D, -50D, idGroup, startDate, 1, activity.getInitValue());
                newPlayerActivities.add(newAA);
            }
            result.put(playersForGroup.getIdPlayer(), newPlayerActivities);
        }
        return result;
    }

    private AutoActivity createNewAutoActivity(Long idPlayer, Long idActivity, Double firstElement, Double ratio,
                                               Double bonus, Double random, Long idGroup, LocalDate startDate,
                                               int daysFactor, Double initValue) {
        AutoActivity newAA = new AutoActivity();
        newAA.setIdPlayer(idPlayer);
        newAA.setIdActivity(idActivity);
        newAA.setFirstElement(firstElement);
        newAA.setRatio(ratio);
        newAA.setBonus(bonus);
        newAA.setRandomBonus(random);
        newAA.setIdGroup(idGroup);
        newAA.setStartDate(startDate);
        newAA.setDaysFactor(daysFactor);
        newAA.setInitValue(initValue);
        return newAA;
    }

    private double getMaxRatio(Long idGroup, Long idActivity) {
        return groupActivitiesService.getActivitiesInTheGroup(idGroup).stream()
                .filter(e -> e.getIdActivity().equals(idActivity)).findFirst().get().getLimitRatio();
    }
}
