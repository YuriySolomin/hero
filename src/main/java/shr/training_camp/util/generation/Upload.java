package shr.training_camp.util.generation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import shr.training_camp.core.config.GameConfig;
import shr.training_camp.core.model.database.Achievements;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.generators.AutoActivityCrowdUpload;
import shr.training_camp.core.model.database.generators.PartAutoActivity;
import shr.training_camp.core.model.database.generators.PlayerCrowdUpload;
import shr.training_camp.dev.education.data.FileUtils;
import shr.training_camp.repository.PlayerRepository;
import shr.training_camp.sevice.interfaces.PlayerService;
import shr.training_camp.util.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Upload {

    private final static String PLAYERS_FILE = "src/main/resources/storage/real_players.txt";
    private final static String AA_FILE = "src/main/resources/storage/auto_activities.txt";
    private final static String C_FILE = "src/main/resources/storage/crowd_players.txt";
    private final static String ACHIEVEMENT_FILE = "src/main/resources/storage/achievements.txt";

    @Autowired
    private GameConfig gameConfig;


    /*public static void main(String[] args) {
        Upload upload = new Upload();
        upload.uploadPlayersFromFile();
    }*/

    public void uploadPlayersFromFile() {
        List<Player> allPlayers = getPlayersFromFile();
        // playerService.saveAllPlayers(allPlayers);
    }

    public List<AutoActivityCrowdUpload> getAACrowdUpload() {

        List<String> fileInfo = FileUtils.readListFromFile(gameConfig.getResourcePlayersActivities());
        List<AutoActivityCrowdUpload> autoActivityCrowdUploads;
        autoActivityCrowdUploads = fileInfo.stream().skip(1).map(s -> s.split("\\|"))
                .map(mask -> {
                    AutoActivityCrowdUpload autoActivityCrowdUpload = new AutoActivityCrowdUpload();
                    autoActivityCrowdUpload.setIdActivity(Long.parseLong(mask[1]));
                    autoActivityCrowdUpload.setFirstElement(Double.parseDouble(mask[3]));
                    autoActivityCrowdUpload.setRatio(Double.parseDouble(mask[4]));
                    autoActivityCrowdUpload.setRandomFirstElement(Integer.parseInt(mask[5]));
                    autoActivityCrowdUpload.setRandomRatio(Integer.parseInt(mask[6]));
                    autoActivityCrowdUpload.setRandom(Integer.parseInt(mask[7]));
                    autoActivityCrowdUpload.setBonus(Integer.parseInt(mask[8]));
                    autoActivityCrowdUpload.setNickMask(mask[9]);
                    return autoActivityCrowdUpload;
                }).collect(Collectors.toList());
        return autoActivityCrowdUploads;
    }

    public List<PlayerCrowdUpload> crowdPlayers() {
        List<String> fileInfo = FileUtils.readListFromFile(gameConfig.getResourcePlayers());
        List<PlayerCrowdUpload> playerCrowdUploadList;
        playerCrowdUploadList = fileInfo.stream().skip(1).map( s -> s.split("\\|"))
                .map(mask -> {
                    PlayerCrowdUpload playerCrowdUpload = new PlayerCrowdUpload();
                    playerCrowdUpload.setNickNameMask(mask[1]);
                    playerCrowdUpload.setFlNameMask(mask[2]);
                    playerCrowdUpload.setGender(Integer.parseInt(mask[3]));
                    playerCrowdUpload.setType(Integer.parseInt(mask[4]));
                    playerCrowdUpload.setCount(Integer.parseInt(mask[5]));
                    playerCrowdUpload.setIdGroup(Long.parseLong(mask[6]));
                    return playerCrowdUpload;
                }).collect(Collectors.toList());
        return playerCrowdUploadList;
    }

    public List<PartAutoActivity> getPartAutoActivity() {
        List<String> aaList = FileUtils.readListFromFile(AA_FILE);
        List<PartAutoActivity> result;
        result = aaList.stream().skip(1).map(s -> s.split("\\|")).map(mask -> {
            PartAutoActivity partAutoActivity = new PartAutoActivity();
            partAutoActivity.setIdActivity(Long.parseLong(mask[1]));
            partAutoActivity.setFirstElement(Double.parseDouble(mask[3]));
            partAutoActivity.setRatio(Double.parseDouble(mask[4]));
            partAutoActivity.setInitValue(Double.parseDouble(mask[5]));
            partAutoActivity.setType(mask[6]);
            partAutoActivity.setBonus(Double.parseDouble(mask[7]));
            partAutoActivity.setRandom(Double.parseDouble(mask[8]));
            partAutoActivity.setIdPlayer(Long.parseLong(mask[9]));
            return partAutoActivity;
        }).collect(Collectors.toList());
        return result;
    }

    public List<Player> getPlayersFromFile() {
        List<String> playersList = FileUtils.readListFromFile(PLAYERS_FILE);
        List<Player> result;
        result = playersList.stream().skip(1).map(s -> s.split("\\|")).map(mask -> {
            Player player1 = new Player();
            player1.setNickName(mask[1]);
            player1.setFirstName(mask[2]);
            player1.setLastName(mask[3]);
            player1.setGender(Integer.parseInt(mask[4]));
            player1.setPlayerType(Integer.parseInt(mask[5]));
            player1.setSecretName(RandomUtils.getReadableString(8, 12, true));
            return player1;
        }).collect(Collectors.toList());
        for (String player: playersList) {
            System.out.println();
        }
        return result;
    }

    public List<Achievements> getAchievementsFromFile() {
        List<String> achievementList = FileUtils.readListFromFile(AA_FILE);
        return null;
    }


}
