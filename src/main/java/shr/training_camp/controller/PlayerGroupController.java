package shr.training_camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import shr.training_camp.core.model.database.AutoActivity;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.PlayersGroups;
import shr.training_camp.core.model.database.generators.AutoActivityCrowdUpload;
import shr.training_camp.core.model.database.generators.PartAutoActivity;
import shr.training_camp.core.model.database.generators.PlayerCrowdUpload;
import shr.training_camp.sevice.interfaces.IAutoActivityService;
import shr.training_camp.sevice.interfaces.IPlayerGroupService;
import shr.training_camp.sevice.interfaces.PlayerService;
import shr.training_camp.util.RandomUtils;
import shr.training_camp.util.generation.Upload;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Controller
public class PlayerGroupController extends AbstractEntityController{

    @Autowired
    private IPlayerGroupService<PlayersGroups> playerGroupService;

    @Autowired
    private Upload upload;

    @Autowired
    private IAutoActivityService<AutoActivity> autoActivityService;

    @Autowired
    private PlayerService playerService;


    @PostMapping("/addPlayerToGroup")
    public String addPlayerToGroup(@ModelAttribute("playerToGroup") PlayersGroups playersGroups) {
        playerGroupService.save(playersGroups);
        return "catalogues/grow/update_grow_group";
    }

    @PostMapping("/uploadCraudPlayers")
    public String uploadCPlayers() {
        List<PlayerCrowdUpload> playerCrowdUploadList = upload.crowdPlayers();
        for (PlayerCrowdUpload playerCrowdUpload: playerCrowdUploadList) {
            for (int i=1; i<= playerCrowdUpload.getCount(); i++) {
                Player player = new Player();
                player.setFirstName(playerCrowdUpload.getFlNameMask() + "_" + 100*i);
                player.setLastName(playerCrowdUpload.getFlNameMask() + "_" + 100*i);
                player.setNickName(playerCrowdUpload.getNickNameMask() + "_" + 100*i);
                player.setSecretName(RandomUtils.getReadableString(8, 12, true));
                player.setGender(playerCrowdUpload.getGender());
                player.setPlayerType(playerCrowdUpload.getType());
                playerService.savePlayer(player);

                Long idPlayer = playerService.getPlayerByNickName(player.getNickName()).getId_player();
                PlayersGroups playersGroups = new PlayersGroups();
                playersGroups.setIdGroup(playerCrowdUpload.getIdGroup());
                playersGroups.setIdPlayer(idPlayer);
                playersGroups.setIsHero(0);
                Double height = playerCrowdUpload.getGender() == 0 ? 165D : 180D;
                playersGroups.setHeight(height);
                playerGroupService.save(playersGroups);
            }
        }
        return "entity";
    }

    @PostMapping("/uploadAACrowd")
    public String uploadAACrowd() {
        List<AutoActivityCrowdUpload> autoActivityCrowdUploadList = upload.getAACrowdUpload();
        LocalDate saveDate = LocalDate.parse("2021-05-17");
        for (AutoActivityCrowdUpload autoActivityCrowdUpload: autoActivityCrowdUploadList) {
            List<Player> playerList = playerService.findAllPlayersByNickNameMask(autoActivityCrowdUpload.getNickMask()+"%");
            for (Player player: playerList) {
                AutoActivity autoActivity = new AutoActivity();
                autoActivity.setIdPlayer(player.getId_player());
                autoActivity.setIdActivity(autoActivityCrowdUpload.getIdActivity());
                autoActivity.setRatio(autoActivityCrowdUpload.getRatio());
                int randomFirstElement = autoActivityCrowdUpload.getRandomFirstElement();
                int randomRatio = autoActivityCrowdUpload.getRandomRatio();
                int random = autoActivityCrowdUpload.getRandom();
                int randomBonus = autoActivityCrowdUpload.getBonus();
                BigDecimal randomFirstElementBD = new BigDecimal(Double.toString(autoActivityCrowdUpload.getFirstElement() + 0.01 * RandomUtils.getRandom(randomFirstElement)));
                randomFirstElementBD = randomFirstElementBD.setScale(4, RoundingMode.HALF_UP);
                autoActivity.setFirstElement(randomFirstElementBD.doubleValue());
                BigDecimal randomRatioBD = new BigDecimal(Double.toString(autoActivityCrowdUpload.getRatio() + 0.01 * RandomUtils.getRandom(randomRatio)));
                randomRatioBD = randomRatioBD.setScale(4, RoundingMode.HALF_UP);
                autoActivity.setFirstElement(randomFirstElementBD.doubleValue());
                autoActivity.setRatio(randomRatioBD.doubleValue());
                autoActivity.setRandomBonus((double)RandomUtils.getRandom(random));
                autoActivity.setBonus(autoActivityCrowdUpload.getBonus() == 0 ? 0 : (double)RandomUtils.getRandom(randomBonus));
                autoActivity.setStartDate(saveDate);
                autoActivityService.save(autoActivity);
            }
        }
        return "entity";
    }

    @PostMapping("/uploadAAPlayers")
    public String uploadAAPlayers() {
        List<PartAutoActivity> aaList = upload.getPartAutoActivity();
        List<PlayersGroups> playersGroupsList = playerGroupService.findPlayersGroupsByGroupId(4L);
        //for (PlayersGroups playersGroups: playersGroupsList) {
            //Long idPlayer = playersGroups.getIdPlayer();
            //String description = playersGroups.getDescription();
            //description = description.replace("Random - ", "");
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
                autoActivity.setStartDate(LocalDate.now().minusDays(1));
                autoActivity.setBonus(partAutoActivity.getBonus());
                autoActivity.setRandomBonus(partAutoActivity.getRandom());
                autoActivity.setDaysFactor(1);
                autoActivityService.save(autoActivity);
            }
        //}
        return "catalogues/grow/update_grow_group";
    }

    //ToDo

    //@GetMapping("players_activity/grouped/idPlayer={id}")

}
